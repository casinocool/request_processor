package strategy;


import dto.NotificationRequest;

import java.util.UUID;

public interface NotificationStrategy {
    void process(NotificationRequest request);
}