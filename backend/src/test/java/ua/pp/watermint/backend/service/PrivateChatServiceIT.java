package ua.pp.watermint.backend.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ua.pp.watermint.backend.dto.filter.PrivateChatFilterDto;
import ua.pp.watermint.backend.dto.request.PrivateChatRequestDto;
import ua.pp.watermint.backend.dto.response.PrivateChatResponseDto;
import ua.pp.watermint.backend.entity.PrivateChat;
import ua.pp.watermint.backend.repository.ChatContentRepository;
import ua.pp.watermint.backend.repository.PrivateChatRepository;
import ua.pp.watermint.backend.repository.UserAccountRepository;
import ua.pp.watermint.backend.util.BaseTestEnv;
import ua.pp.watermint.backend.util.TestDatabaseInitializer;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.pp.watermint.backend.util.DtoAssertions.assertPrivateChatEquals;

@SpringBootTest
@Transactional
@Import(TestDatabaseInitializer.class)
public class PrivateChatServiceIT extends BaseTestEnv {
    @Autowired
    private PrivateChatService privateChatService;

    @Autowired
    private PrivateChatRepository privateChatRepository;

    @Autowired
    private ChatContentRepository chatContentRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    private UUID storedUserAccount1Id;

    private UUID storedUserAccount2Id;

    @BeforeEach
    void setUp() {
        storedUserAccount1Id = userAccountRepository.findByUsername("user1").orElseThrow().getId();
        storedUserAccount2Id = userAccountRepository.findByUsername("user2").orElseThrow().getId();
    }

    @Test
    void getById_withExistingId_returnsPrivateChat() {
        PrivateChat chat = privateChatRepository.findAll().getFirst();
        PrivateChatResponseDto response = privateChatService.getById(chat.getId());
        assertThat(response).isNotNull();
        assertPrivateChatEquals(chat, response);
    }

    @Test
    void getById_withNonExistentId_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                privateChatService.getById(UUID.randomUUID())
        );
    }

    @Test
    void search_withNoFilters_returnsAllPrivateChats() {
        PrivateChatFilterDto filter = new PrivateChatFilterDto();
        filter.setUserAccount1Id(storedUserAccount1Id);
        List<PrivateChatResponseDto> response = privateChatService.search(filter);
        assertThat(response).hasSize(1);
    }

    @Test
    void search_withExistingUserAccount2Name_returnsMatchingPrivateChats() {
        PrivateChatFilterDto filter = new PrivateChatFilterDto();
        filter.setUserAccount1Id(storedUserAccount1Id);
        filter.setUserAccount2Name("second name");
        List<PrivateChatResponseDto> response = privateChatService.search(filter);
        assertThat(response).hasSize(1);
    }

    @Test
    void search_withNonExistentUserAccount2Name_returnsNoPrivateChats() {
        PrivateChatFilterDto filter = new PrivateChatFilterDto();
        filter.setUserAccount1Id(storedUserAccount1Id);
        filter.setUserAccount2Name(UUID.randomUUID().toString());
        List<PrivateChatResponseDto> response = privateChatService.search(filter);
        assertThat(response).hasSize(0);
    }

    @Test
    void search_withNonUserAccount1Id_throwsEntityNotFoundException() {
        PrivateChatFilterDto filter = new PrivateChatFilterDto();
        filter.setUserAccount1Id(UUID.randomUUID());
        assertThrows(EntityNotFoundException.class, () ->
                privateChatService.search(filter)
        );
    }

    @Test
    void create_withValidPrivateChat_persistsPrivateChat() {
        privateChatRepository.deleteAll();

        PrivateChatResponseDto saved = privateChatService.create(
                PrivateChatRequestDto.builder()
                        .userAccount1Id(storedUserAccount1Id)
                        .userAccount2Id(storedUserAccount2Id)
                        .build()
        );

        assertThat(saved).isNotNull();
        assertThat(saved.getChatContent()).isNotNull();
        assertThat(saved.getUserAccount1().getId()).isEqualTo(storedUserAccount1Id);
        assertThat(saved.getUserAccount2().getId()).isEqualTo(storedUserAccount2Id);
        assertThat(privateChatRepository.count()).isEqualTo(1);
    }

    @Test
    void create_withNonExistentUserAccountId_throwsEntityNotFoundException() {
        privateChatRepository.deleteAll();

        assertThrows(EntityNotFoundException.class, () -> {
            privateChatService.create(
                    PrivateChatRequestDto.builder()
                            .userAccount1Id(storedUserAccount1Id)
                            .userAccount2Id(UUID.randomUUID())
                            .build()
            );
            privateChatRepository.flush();
        });
    }

    @Test
    void create_withExistingUserAccounts_throwsEntityExistsException() {
        assertThrows(EntityExistsException.class, () -> {
            privateChatService.create(
                    PrivateChatRequestDto.builder()
                            .userAccount1Id(storedUserAccount1Id)
                            .userAccount2Id(storedUserAccount2Id)
                            .build()
            );
            privateChatRepository.flush();
        });
    }

    @Test
    void delete_withExistingPrivateChatId_deletesPrivateChat() {
        UUID id = privateChatRepository.findAll().getFirst().getId();
        privateChatService.delete(id);
        assertThat(privateChatRepository.count()).isEqualTo(0);
        assertThat(chatContentRepository.count()).isEqualTo(0);
        assertThat(privateChatRepository.existsById(id)).isFalse();
    }

    @Test
    void delete_withNonExistentPrivateChatId_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                privateChatService.delete(UUID.randomUUID())
        );
    }
}
