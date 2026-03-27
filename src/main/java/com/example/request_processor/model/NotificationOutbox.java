package com.example.request_processor.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification_outbox")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationOutbox {
    @Id
    private UUID id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    private String topic;

    @NotNull
    private String key;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String value;

    @NotNull
    private Boolean sent;

    @NotNull
    private Integer attempt;

    @PrePersist
    public void prePersist() {
        if(attempt == null)
            this.attempt = 1;
        if(sent == null)
            this.sent = false;

    }

}
