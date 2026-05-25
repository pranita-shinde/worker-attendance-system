package com.example.demo.attendance;

import com.example.demo.worker.Worker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttendanceLog, Long> {

    Optional<AttendanceLog> findTopByWorkerAndClockOutIsNull(Worker worker);

    @Query("SELECT a FROM AttendanceLog a JOIN FETCH a.worker JOIN FETCH a.site WHERE a.worker.id = :workerId")
        Page<AttendanceLog> findByWorkerWithPagination(
            @Param("workerId") Long workerId,
            Pageable pageable
    );
}

