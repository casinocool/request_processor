package model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    private LocalDateTime createdAt;

    @NotNull
    private String topic;

    @NotNull
    private String key;

    @Column(columnDefinition = "TEXT")
    private String value;

    private Boolean sent;

    private Integer attempt;

}
