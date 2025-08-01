package ua.pp.watermint.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.pp.watermint.backend.entity.ChatMessage;

import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
}
