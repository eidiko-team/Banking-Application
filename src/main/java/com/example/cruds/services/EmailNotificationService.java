package com.example.cruds.services;

import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailNotificationService implements NotificationService{

    @Override
    public void sendNotification(String message) {
        System.out.println("Email notification: " + message);
    }

}
