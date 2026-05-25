package com.example.demo.attendance;

import com.example.demo.exception.WorkerNotFoundException;
import com.example.demo.overtime.OvertimeEntry;
import com.example.demo.overtime.OvertimeRepository;
import com.example.demo.site.Site;
import com.example.demo.site.SiteRepository;
import com.example.demo.worker.Worker;
import com.example.demo.worker.WorkerRepository;


import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;

@AllArgsConstructor
@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final WorkerRepository workerRepository;
    private final OvertimeRepository overtimeRepository;
    private final SiteRepository siteRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    // CLOCK-IN
    public void clockIn(Long workerId, Long siteId) {

        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new WorkerNotFoundException("Worker not found"));

        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site not found"));

        // check already clocked in
        attendanceRepository.findTopByWorkerAndClockOutIsNull(worker)
                .ifPresent(a -> {
                    throw new RuntimeException("Worker already clocked in");
        });

        AttendanceLog log = new AttendanceLog();
        log.setWorker(worker);
        log.setSite(site);
        log.setClockIn(LocalDateTime.now());

        attendanceRepository.save(log);
        
        try {
            String key = "active_workers";
            String value = siteId + "|" + log.getClockIn();

            redisTemplate.opsForHash()
                .put(key, workerId.toString(), value);

            redisTemplate.expire(key, Duration.ofHours(16));

        } catch (Exception e) {
            System.out.println("⚠️ Redis failed during clock-in, skipping cache");
        }
    }

    // CLOCK-OUT
    @Transactional
    public void clockOut(Long workerId) {

        Worker worker = workerRepository.findById(workerId)
            .orElseThrow(() -> new WorkerNotFoundException("Worker not found"));

        AttendanceLog log = attendanceRepository
            .findTopByWorkerAndClockOutIsNull(worker)
            .orElseThrow(() -> new RuntimeException("Worker not clocked in"));

        log.setClockOut(LocalDateTime.now());

        double hours = java.time.Duration.between(
            log.getClockIn(),
            log.getClockOut()
        ).toHours();

        log.setTotalHours(hours);

        double overtimeHours = 0.0;

        if (hours > 8) {
            overtimeHours = hours - 8;
        }

        log.setOvertimeHours(overtimeHours);

        if (hours > 16) {
            log.setFlagged(true);
        }

        attendanceRepository.save(log);

        try {
        redisTemplate.opsForHash()
            .delete("active_workers", workerId.toString());
        } catch (Exception e) {
            System.out.println("⚠️ Redis failed during clock-out");
        }

        // OVERTIME ENTRY CREATION
        if (overtimeHours > 0) {

            LocalDate today = LocalDate.now();

            // Monthly range
            LocalDate start = today.withDayOfMonth(1);
            LocalDate end = today.withDayOfMonth(today.lengthOfMonth());

            List<OvertimeEntry> monthlyEntries =
                overtimeRepository.findByWorkerAndDateBetween(worker, start, end);

            double totalMonthlyHours = monthlyEntries.stream()
                .mapToDouble(OvertimeEntry::getOvertimeHours)
                .sum();

            // Apply 60-hour cap
            double allowedHours = Math.max(0, 60 - totalMonthlyHours);
            double finalOvertime = Math.min(overtimeHours, allowedHours);

            if (finalOvertime > 0) {

                double wage = worker.getDailyWageRate();

                double amount;

                if (finalOvertime <= 2) {
                    amount = finalOvertime * wage * 1.5;
                } else {
                    double firstTwo = 2 * wage * 1.5;
                    double remaining = (finalOvertime - 2) * wage * 2;
                    amount = firstTwo + remaining;
                }

                OvertimeEntry entry = new OvertimeEntry();
                entry.setWorker(worker);
                entry.setAttendance(log);
                entry.setDate(today);
                entry.setOvertimeHours(finalOvertime);
                entry.setOvertimeRate(wage);
                entry.setAmount(amount);

                overtimeRepository.save(entry);
            }
        }
    }

    public Map<Object, Object> getActiveWorkers() {
            return redisTemplate.opsForHash().entries("active_workers");
    }

    public Page<AttendanceLog> getLogs(Long workerId, Pageable pageable) {
        return attendanceRepository.findByWorkerWithPagination(workerId, pageable);
    }
}