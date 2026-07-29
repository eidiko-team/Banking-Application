package com.example.cruds.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class SmsNotificationService implements NotificationService{
    @Override
    public void sendNotification(String message) {
        System.out.println(
                "SMS notification: " + message
        );
    }
}
