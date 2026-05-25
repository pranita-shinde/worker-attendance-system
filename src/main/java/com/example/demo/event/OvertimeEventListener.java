package com.example.demo.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
public class OvertimeEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOvertimeSettled(OvertimeSettledEvent event) {

        // Simulate SMS
        System.out.println("SMS SENT to Worker: " 
                + event.getWorker().getName()
                + " | Amount: " + event.getAmount());
    }
}