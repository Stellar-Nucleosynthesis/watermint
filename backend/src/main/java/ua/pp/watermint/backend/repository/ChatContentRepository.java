package ua.pp.watermint.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.pp.watermint.backend.entity.ChatContent;

import java.util.UUID;

public interface ChatContentRepository extends JpaRepository<ChatContent, UUID> {
}
