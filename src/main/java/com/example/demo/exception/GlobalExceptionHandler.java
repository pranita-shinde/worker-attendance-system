package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRuntimeException(RuntimeException ex) {
        return new ErrorResponse(
                "ERROR",
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(WorkerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWorkerNotFound(WorkerNotFoundException ex) {
        return new ErrorResponse(
                "WORKER_NOT_FOUND",
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(DuplicateWorkerException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateWorker(DuplicateWorkerException ex) {
        return new ErrorResponse(
                "DUPLICATE_WORKER",
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

}