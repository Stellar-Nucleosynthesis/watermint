package ua.pp.watermint.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.pp.watermint.backend.entity.ChatEvent;

import java.util.List;
import java.util.UUID;

public interface ChatEventRepository extends JpaRepository<ChatEvent, UUID> {
    List<ChatEvent> findByChatContentIdAndTextContainingIgnoreCase(UUID chatContentId, String text);
}
