package strategy;

import dto.NotificationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import outbox.TransactionalOutboxService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailStrategy implements NotificationStrategy {

    private final TransactionalOutboxService service;
    private final ObjectMapper objectMapper;

    @Override
    public void process(NotificationRequest request) {
        UUID key = UUID.randomUUID();
        String topic = "email-events";

        try {
            String payload = objectMapper.writeValueAsString(request);
            service.saveMessage(key, topic, key.toString(), payload);
        } catch (Exception e) {
            log.error("Ошибка при подготовке email сообщения", e);
            throw new RuntimeException("Ошибка сериализации сообщения", e);
        }
    }
}