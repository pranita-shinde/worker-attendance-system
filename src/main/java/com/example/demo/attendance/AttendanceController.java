package com.example.demo.attendance;

import lombok.AllArgsConstructor;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/attendance")
@AllArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/clock-in")
    public void clockIn(@RequestParam Long workerId,
                        @RequestParam Long siteId) {
        attendanceService.clockIn(workerId, siteId);
    }

    @PostMapping("/clock-out")
    public void clockOut(@RequestParam Long workerId) {
        attendanceService.clockOut(workerId);
    }

    @GetMapping("/active")
    public Map<Object, Object> getActiveWorkers() {
        return attendanceService.getActiveWorkers();
    }

    @GetMapping("/log")
    public Page<AttendanceLog> getLogs(
        @RequestParam Long workerId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
    return attendanceService.getLogs(workerId, PageRequest.of(page, size));
    }
}