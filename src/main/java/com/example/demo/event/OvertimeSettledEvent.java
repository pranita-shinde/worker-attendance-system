package com.example.demo.event;

import com.example.demo.worker.Worker;

public class OvertimeSettledEvent {

    private final Worker worker;
    private final Double amount;

    public OvertimeSettledEvent(Worker worker, Double amount) {
        this.worker = worker;
        this.amount = amount;
    }

    public Worker getWorker() {
        return worker;
    }

    public Double getAmount() {
        return amount;
    }
}