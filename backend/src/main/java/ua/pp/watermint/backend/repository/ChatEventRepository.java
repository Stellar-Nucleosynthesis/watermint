package ua.pp.watermint.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.pp.watermint.backend.entity.ChatEvent;

import java.util.UUID;

public interface ChatEventRepository extends JpaRepository<ChatEvent, UUID>, JpaSpecificationExecutor<ChatEvent> {
}
