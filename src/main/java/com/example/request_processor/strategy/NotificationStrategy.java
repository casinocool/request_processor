package com.example.request_processor.strategy;


import com.example.request_processor.dto.NotificationRequest;

public interface NotificationStrategy {
    void process(NotificationRequest request);
}