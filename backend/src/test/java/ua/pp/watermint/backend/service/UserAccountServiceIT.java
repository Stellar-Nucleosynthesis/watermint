package ua.pp.watermint.backend.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import ua.pp.watermint.backend.dto.request.UserAccountRequestDto;
import ua.pp.watermint.backend.dto.response.UserAccountResponseDto;
import ua.pp.watermint.backend.entity.UserAccount;
import ua.pp.watermint.backend.repository.UserAccountRepository;
import ua.pp.watermint.backend.util.TestDatabaseInitializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.pp.watermint.backend.util.DtoAssertions.assertUserAccountEquals;

@SpringBootTest
@Transactional
@Import(TestDatabaseInitializer.class)
public class UserAccountServiceIT {
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    void getById_withExistingId_returnsUserAccount() {
        UserAccount account = userAccountRepository.findAll().getFirst();
        UserAccountResponseDto response = userAccountService.getById(account.getId());
        assertThat(response).isNotNull();
        assertUserAccountEquals(account, response);
    }

    @Test
    void getById_withNonExistentId_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                userAccountService.getById(UUID.randomUUID())
        );
    }

    @Test
    void getByUsername_withExistingUsername_returnsChatMessage() {
        UserAccount account = userAccountRepository.findAll().getFirst();
        UserAccountResponseDto response =
                userAccountService.getByUsername(account.getUsername());
        assertThat(response).isNotNull();
        assertUserAccountEquals(account, response);
    }

    @Test
    void getByUsername_withNonExistentUsername_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                userAccountService.getByUsername(UUID.randomUUID().toString())
        );
    }

    @ParameterizedTest
    @CsvSource({
            "name,2",
            "first,1",
            "hdhwubdghjdhd,0"
    })
    void search_withGivenName_returnsMatchingUserAccounts(String name, int expectedSize) {
        List<UserAccountResponseDto> response = userAccountService.search(name);
        assertThat(response).hasSize(expectedSize);
    }

    @Test
    void create_withValidUserAccount_persistsUserAccount() {
        UserAccountRequestDto dto = UserAccountRequestDto.builder()
                .email("super_email@example.com")
                .username("super_username")
                .password("super_password")
                .name("User Name")
                .birthDate(LocalDate.EPOCH)
                .profilePicture(new byte[]{4, 5, 6})
                .build();
        UserAccountResponseDto saved = userAccountService.create(dto);
        assertThat(saved).isNotNull();
        assertThat(saved.getEmail()).isEqualTo(dto.getEmail());
        assertThat(saved.getVerified()).isFalse();
        assertThat(saved.getUsername()).isEqualTo(dto.getUsername());
        assertThat(saved.getPassword()).isEqualTo(dto.getPassword());
        assertThat(saved.getName()).isEqualTo(dto.getName());
        assertThat(saved.getBirthDate()).isEqualTo(dto.getBirthDate());
        assertThat(saved.getProfilePicture()).isEqualTo(dto.getProfilePicture());
        assertThat(userAccountRepository.count()).isEqualTo(3);
    }

    @ParameterizedTest
    @CsvSource({
            ",super_password,User Name",
            "super_email@example.com,,User Name",
            "super_email@example.com,super_password,"
    })
    void create_withGivenInput_throwsConstraintViolationException(
            String email, String password, String name
    ) {
        assertThrows(ConstraintViolationException.class, () -> {
            userAccountService.create(
                    UserAccountRequestDto.builder()
                            .email(email)
                            .password(password)
                            .name(name)
                            .build()
            );
            userAccountRepository.flush();
        });
    }

    @ParameterizedTest
    @CsvSource({
            "user1@example.com,super_username",
            "super_email@example.com,user1"
    })
    void create_withGivenInput_throwsDataIntegrityViolationException(
            String email, String username
    ) {
        assertThrows(DataIntegrityViolationException.class, () -> {
            userAccountService.create(
                    UserAccountRequestDto.builder()
                            .email(email)
                            .username(username)
                            .password("super_password")
                            .name("User Name")
                            .build()
            );
            userAccountRepository.flush();
        });
    }

    @Test
    void update_withValidUserAccount_persistsUserAccount() {
        UserAccount initial = userAccountRepository.findAll().getFirst();
        LocalDateTime initialUpdateTime = initial.getUpdateTime();
        UserAccountRequestDto request = UserAccountRequestDto.builder()
                .email("super_email@example.com")
                .username("super_username")
                .password("super_password")
                .name("User Name")
                .birthDate(LocalDate.EPOCH)
                .profilePicture(new byte[]{4, 5, 6})
                .build();
        UserAccountResponseDto updated = userAccountService.update(initial.getId(), request);
        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(initial.getId());
        assertThat(updated.getEmail()).isEqualTo(initial.getEmail());
        assertThat(updated.getVerified()).isFalse();
        assertThat(updated.getUsername()).isEqualTo(initial.getUsername());
        assertThat(updated.getPassword()).isEqualTo(initial.getPassword());
        assertThat(updated.getName()).isEqualTo(initial.getName());
        assertThat(updated.getBirthDate()).isEqualTo(initial.getBirthDate());
        assertThat(updated.getProfilePicture()).isEqualTo(initial.getProfilePicture());
        assertThat(updated.getUpdateTime()).isNotEqualTo(initialUpdateTime);
        assertThat(userAccountRepository.count()).isEqualTo(2);
    }

    @Test
    void update_withNonExistentId_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            userAccountService.update(UUID.randomUUID(),
                    UserAccountRequestDto.builder()
                            .email("super_email@example.com")
                            .password("super_password")
                            .name("User Name")
                            .build()
            );
            userAccountRepository.flush();
        });
    }

    @ParameterizedTest
    @CsvSource({
            "'',super_password,User Name",
            "super_email@example.com,'',User Name",
            "super_email@example.com,super_password,''"
    })
    void update_withGivenInput_throwsConstraintViolationException(
            String email, String password, String name
    ) {
        UUID id = userAccountRepository.findAll().getFirst().getId();
        assertThrows(ConstraintViolationException.class, () -> {
            userAccountService.update(id,
                    UserAccountRequestDto.builder()
                            .email(email)
                            .password(password)
                            .name(name)
                            .build()
            );
            userAccountRepository.flush();
        });
    }

    @ParameterizedTest
    @CsvSource({
            "user1@example.com,super_username",
            "super_email@example.com,user1"
    })
    void update_withGivenInput_throwsDataIntegrityViolationException(
            String email, String username
    ) {
        UUID id = userAccountRepository.save(
                UserAccount.builder()
                        .email("smth@example.com")
                        .password("password")
                        .name("New name")
                        .build()
        ).getId();
        assertThrows(DataIntegrityViolationException.class, () -> {
            userAccountService.update(id,
                    UserAccountRequestDto.builder()
                            .email(email)
                            .username(username)
                            .password("password")
                            .name("User Name")
                            .build()
            );
            userAccountRepository.flush();
        });
    }

    @Test
    void delete_withExistingUserAccountId_deletesUserAccount() {
        UUID id = userAccountRepository.findAll().getFirst().getId();
        userAccountService.delete(id);
        assertThat(userAccountRepository.count()).isEqualTo(1);
        assertThat(userAccountRepository.existsById(id)).isFalse();
    }

    @Test
    void delete_withNonExistentUserAccountId_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                userAccountService.delete(UUID.randomUUID())
        );
    }
}
