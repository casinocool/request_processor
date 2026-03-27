package com.example.request_processor.outbox;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.request_processor.model.NotificationOutbox;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionalOutboxService {
    private final OutboxRepository outboxRepository;

    @Transactional
    public void saveMessage(UUID id, String topic, String key, String value) {
        NotificationOutbox outbox = NotificationOutbox.builder()
                .id(id)
                .topic(topic)
                .key(key)
                .value(value)
                .sent(false)
                .attempt(1)
                .build();
        outboxRepository.save(outbox);
        log.info("Подготовлено сообщение для отправки. Key: {}, Payload: {}, topic: {}", key, value, topic);
    }
}
