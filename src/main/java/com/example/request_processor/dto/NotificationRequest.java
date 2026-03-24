package dto;
import enums.KafkaTopics;
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