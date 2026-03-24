package outbox;

import jakarta.transaction.Transactional;
import model.NotificationOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<NotificationOutbox, UUID> {
    List<NotificationOutbox> findBySentFalseOrderByCreatedAtAsc();

    @Modifying
    @Transactional
    @Query("UPDATE NotificationOutbox o SET o.sent=true where o.id=:id")
    void markAsSent(@Param("id") UUID id);

    @Modifying
    @Transactional
    @Query("UPDATE NotificationOutbox o SET o.attempt=o.attempt+1 where o.id=:id")
    void incrementAttempt(@Param("id") UUID id);

}
