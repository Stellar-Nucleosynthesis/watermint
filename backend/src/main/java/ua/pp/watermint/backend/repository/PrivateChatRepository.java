package ua.pp.watermint.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.pp.watermint.backend.entity.PrivateChat;

import java.util.UUID;

public interface PrivateChatRepository extends JpaRepository<PrivateChat, UUID>, JpaSpecificationExecutor<PrivateChat> {
    boolean existsByUserAccount1_IdAndUserAccount2_Id(UUID user1Id, UUID user2Id);
}
