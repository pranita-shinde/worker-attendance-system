package com.example.demo.overtime;

import com.example.demo.attendance.AttendanceLog;
import com.example.demo.worker.Worker;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "overtime_entries")
public class OvertimeEntry {

    @Id
    @SequenceGenerator(
            name = "overtime_sequence",
            sequenceName = "overtime_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "overtime_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @OneToOne
    @JoinColumn(name = "attendance_id", nullable = false, unique = true)
    private AttendanceLog attendance;

    @Column(nullable = false)
    private LocalDate date;

    private Double overtimeHours;

    private Double overtimeRate;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private SettlementStatus settlementStatus = SettlementStatus.PENDING;
}