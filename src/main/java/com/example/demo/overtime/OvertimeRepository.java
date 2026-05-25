package com.example.demo.overtime;

import com.example.demo.worker.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OvertimeRepository extends JpaRepository<OvertimeEntry, Long> {

    List<OvertimeEntry> findByWorkerAndDateBetween(
            Worker worker,
            LocalDate start,
            LocalDate end
    );
}