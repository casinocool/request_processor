package strategy;

import enums.KafkaTopics;
import org.springframework.stereotype.Component;
import java.util.EnumMap;
import java.util.Map;

@Component
public class NotificationStrategyFactory {

    private final Map<KafkaTopics, NotificationStrategy> strategies;

    public NotificationStrategyFactory(SmsStrategy smsStrategy,
                                       EmailStrategy emailStrategy,
                                       PushStrategy pushStrategy,
                                       TelegramStrategy telegramStrategy) {
        strategies = new EnumMap<>(KafkaTopics.class);
        strategies.put(KafkaTopics.SMS, smsStrategy);
        strategies.put(KafkaTopics.EMAIL, emailStrategy);
        strategies.put(KafkaTopics.PUSH, pushStrategy);
        strategies.put(KafkaTopics.TG_MESSAGE, telegramStrategy);
    }

    public NotificationStrategy getStrategy(KafkaTopics type) {
        NotificationStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported notification type: " + type);
        }
        return strategy;
    }
}