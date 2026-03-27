package com.example.request_processor.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.request_processor.model.NotificationOutbox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxScheduler {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${outbox.batch-size:50}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${outbox.delay-ms:1000}")


    @Transactional
    public void processOutbox() {
        List<NotificationOutbox> messages = outboxRepository.findBySentFalseOrderByCreatedAtAsc()
                .stream()
                .limit(batchSize)
                .toList();

        for (NotificationOutbox message : messages) {
            try {
                kafkaTemplate.send(message.getTopic(), message.getKey(), message.getValue()).get();
                outboxRepository.markAsSent(message.getId());
                log.info("Успешно отправлено сообщение в Kafka. Key: {}", message.getKey());
            } catch (Exception e) {
                outboxRepository.incrementAttempt(message.getId());
                log.error("Ошибка отправки сообщения. Key: {}, attempt: {}",
                        message.getKey(), message.getAttempt() + 1, e);
            }
        }
    }
}