package com.example.demo.attendance;

import com.example.demo.worker.Worker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttendanceLog, Long> {

    //  Get all attendance logs for a worker (with pagination)
    @Query(
        value = "SELECT a FROM AttendanceLog a " +
                "JOIN FETCH a.worker " +
                "JOIN FETCH a.site " +
                "WHERE a.worker.id = :workerId",
        countQuery = "SELECT COUNT(a) FROM AttendanceLog a " +
                     "WHERE a.worker.id = :workerId"
    )
    Page<AttendanceLog> findByWorkerWithPagination(
            @Param("workerId") Long workerId,
            Pageable pageable
    );

    // Get currently active attendance (worker hasn't clocked out yet)
    Optional<AttendanceLog> findTopByWorkerAndClockOutIsNull(Worker worker);

    // Alternative (if you want by workerId instead of Worker object)
    Optional<AttendanceLog> findTopByWorkerIdAndClockOutIsNull(Long workerId);
}

