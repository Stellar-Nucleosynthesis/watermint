package ua.pp.watermint.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.pp.watermint.backend.entity.PrivateChat;

import java.util.UUID;

public interface PrivateChatRepository extends JpaRepository<PrivateChat, UUID> {
}
