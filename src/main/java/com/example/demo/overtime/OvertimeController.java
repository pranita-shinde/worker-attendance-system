package com.example.demo.overtime;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("api/v1/overtime")
@AllArgsConstructor
public class OvertimeController {

    private final OvertimeService overtimeService;

    // SETTLEMENT API
    @PostMapping("/settle/{workerId}")
    public Double settleOvertime(
            @PathVariable Long workerId,
            @RequestParam String month // format: YYYY-MM
    ) {
        YearMonth yearMonth = YearMonth.parse(month);
        return overtimeService.settleOvertime(workerId, yearMonth);
    }
}