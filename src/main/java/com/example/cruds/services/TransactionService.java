package com.example.cruds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TransactionService {

    @Autowired
    @Qualifier("emailService")
    private NotificationService notificationService;

    public void service(){
        System.out.println("*************i am usig the Javabased***************");
    }
}
