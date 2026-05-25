package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateWorkerException extends RuntimeException {

    public DuplicateWorkerException(String message) {
        super(message);
    }
}
