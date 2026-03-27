package com.example.request_processor.dto;
import com.example.request_processor.enums.KafkaTopics;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationRequest {
    @NotNull
    private KafkaTopics type;

    @NotBlank
    private String message;
}