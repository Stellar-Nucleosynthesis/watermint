package ua.pp.watermint.backend.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.pp.watermint.backend.entity.UserAccount;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class UserAccountRepositoryTest {
    @Autowired
    private UserAccountRepository userAccountRepository;

    private UserAccount storedUserAccount;

    @BeforeEach
    public void setup() {
        storedUserAccount = userAccountRepository.saveAndFlush(
                UserAccount.builder()
                        .email("email@example.com")
                        .verified(false)
                        .username("username")
                        .password("password")
                        .name("name")
                        .build()
        );
    }

    @AfterEach
    public void teardown() {
        userAccountRepository.delete(storedUserAccount);
        userAccountRepository.flush();
    }

    @Test
    public void findAllUserAccountsTest(){
        assertThat(userAccountRepository.findAll())
                .containsExactly(storedUserAccount);
    }

    @Test
    public void findUserAccountByIdTest(){
        UUID id = storedUserAccount.getId();
        assertThat(userAccountRepository.findById(id))
                .isPresent()
                .contains(storedUserAccount);
    }

    @Test
    public void addUserAccountTest(){
        UserAccount saved = userAccountRepository.saveAndFlush(
                UserAccount.builder()
                        .email("another_email@example.com")
                        .verified(false)
                        .username("another_username")
                        .password("password")
                        .name("name")
                        .build()
        );
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVersion())
                .isNotNull()
                .isEqualTo(0);
        assertThat(saved.getCreateTime()).isNotNull();
        assertThat(saved.getUpdateTime()).isNotNull();
        assertThat(userAccountRepository.findAll())
                .contains(saved)
                .contains(storedUserAccount);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addUserAccountWithExistingEmailTest(){
        assertThrows(DataIntegrityViolationException.class, () ->
                userAccountRepository.saveAndFlush(
                        UserAccount.builder()
                                .email("email@example.com")
                                .verified(false)
                                .username("new_username")
                                .password("password")
                                .name("name")
                                .build()
                )
        );
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addUserAccountWithExistingUsernameTest(){
        assertThrows(DataIntegrityViolationException.class, () ->
                userAccountRepository.saveAndFlush(
                        UserAccount.builder()
                                .email("another_email@example.com")
                                .verified(false)
                                .username("username")
                                .password("password")
                                .name("name")
                                .build()
                )
        );
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addUserAccountWithBlankUsernameTest(){
        assertThrows(ConstraintViolationException.class, () ->
                userAccountRepository.saveAndFlush(
                        UserAccount.builder()
                                .email("another_email@example.com")
                                .verified(false)
                                .username("")
                                .password("password")
                                .name("name")
                                .build()
                )
        );
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addUserAccountWithEmptyPasswordTest(){
        assertThrows(ConstraintViolationException.class, () ->
                userAccountRepository.saveAndFlush(
                        UserAccount.builder()
                                .email("another_email@example.com")
                                .verified(false)
                                .username("new_username")
                                .password("")
                                .name("name")
                                .build()
                )
        );
    }

    @Test
    public void updateUserAccountTest(){
        UUID id = storedUserAccount.getId();
        LocalDateTime lastUpdate = storedUserAccount.getUpdateTime();
        storedUserAccount.setName("new_name");
        userAccountRepository.saveAndFlush(storedUserAccount);
        assertThat(userAccountRepository.count()).isEqualTo(1);
        UserAccount updated = userAccountRepository
                .findById(id).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo("new_name");
        assertThat(updated.getUpdateTime()).isNotEqualTo(lastUpdate);
        assertThat(updated.getVersion()).isEqualTo(1);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void updateUserAccountWithInvalidValueTest(){
        storedUserAccount.setName("");
        assertThrows(ConstraintViolationException.class, () ->
                userAccountRepository.saveAndFlush(storedUserAccount)
        );
    }

    @Test
    public void deleteUserAccountTest(){
        userAccountRepository.deleteById(storedUserAccount.getId());
        assertThat(userAccountRepository.count()).isZero();
    }
}
