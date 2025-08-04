package ua.pp.watermint.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.pp.watermint.backend.entity.UserAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
    Optional<UserAccount> findByUsername(String username);
    List<UserAccount> findAllByNameContainingIgnoreCase(String name);
}
