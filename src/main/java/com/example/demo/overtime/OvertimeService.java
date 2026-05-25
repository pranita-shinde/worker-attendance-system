package com.example.demo.overtime;

import com.example.demo.event.OvertimeSettledEvent;
import com.example.demo.exception.WorkerNotFoundException;
import com.example.demo.worker.Worker;
import com.example.demo.worker.WorkerRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
 import org.springframework.context.ApplicationEventPublisher;

@AllArgsConstructor
@Service
public class OvertimeService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final OvertimeRepository overtimeRepository;
    private final WorkerRepository workerRepository;

    public List<OvertimeEntry> getMonthlySummary(
            Worker worker,
            LocalDate start,
            LocalDate end
    ) {
        return overtimeRepository.findByWorkerAndDateBetween(worker, start, end);
    }

    public Double calculateAmount(Double hours, Double wage) {

        if (hours <= 2) {
            return hours * wage * 1.5;
        }

        double firstTwo = 2 * wage * 1.5;
        double remaining = (hours - 2) * wage * 2;

        return firstTwo + remaining;
    }

    @Transactional
    public Double settleOvertime(Long workerId, YearMonth month) {

        Worker worker = workerRepository.findById(workerId)
            .orElseThrow(() -> new WorkerNotFoundException("Worker not found"));

            // Prevent settling current month
            if (month.equals(YearMonth.now())) {
                throw new RuntimeException("Cannot settle current month");
            }

            LocalDate start = month.atDay(1);
            LocalDate end = month.atEndOfMonth();

            List<OvertimeEntry> entries =
                overtimeRepository.findByWorkerAndDateBetween(worker, start, end);

            double totalAmount = 0;

            for (OvertimeEntry entry : entries) {

                if (entry.getSettlementStatus() == SettlementStatus.SETTLED) {
                    continue;
                }

            totalAmount += entry.getAmount();

            entry.setSettlementStatus(SettlementStatus.SETTLED);
        }

        overtimeRepository.saveAll(entries);

        //S PUBLISH EVENT AFTER DB SUCCESS
        applicationEventPublisher.publishEvent(
            new OvertimeSettledEvent(worker, totalAmount)
        );

        return totalAmount;
    }
}