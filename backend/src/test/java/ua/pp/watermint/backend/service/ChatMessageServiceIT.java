package ua.pp.watermint.backend.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import ua.pp.watermint.backend.dto.filter.ChatMessageFilterDto;
import ua.pp.watermint.backend.dto.request.ChatMessageRequestDto;
import ua.pp.watermint.backend.dto.response.ChatMessageResponseDto;
import ua.pp.watermint.backend.entity.ChatMessage;
import ua.pp.watermint.backend.repository.ChatContentRepository;
import ua.pp.watermint.backend.repository.ChatMessageRepository;
import ua.pp.watermint.backend.repository.UserAccountRepository;
import ua.pp.watermint.backend.util.BaseTestEnv;
import ua.pp.watermint.backend.util.TestDatabaseInitializer;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.pp.watermint.backend.util.DtoAssertions.assertChatMessageEquals;

@SpringBootTest
@Transactional
@Import(TestDatabaseInitializer.class)
public class ChatMessageServiceIT extends BaseTestEnv {
    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatContentRepository chatContentRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    private UUID storedChatContentId;

    private UUID storedUserAccountId;

    private Pageable defaultPageable;

    @BeforeEach
    void setUp() {
        storedChatContentId = chatContentRepository.findAll().getFirst().getId();
        storedUserAccountId = userAccountRepository.findByUsername("user1").orElseThrow().getId();
        defaultPageable = PageRequest.of(0, 10);
    }

    @Test
    void getById_withExistingId_returnsChatMessage() {
        ChatMessage message = chatMessageRepository.findAll().getFirst();
        ChatMessageResponseDto response = chatMessageService.getById(message.getId());
        assertThat(response).isNotNull();
        assertChatMessageEquals(message, response);
    }

    @Test
    void getById_withNonExistentId_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                chatMessageService.getById(UUID.randomUUID())
        );
    }

    @Test
    void create_withValidChatMessage_persistsChatMessage() {
        ChatMessageResponseDto saved = chatMessageService.create(
                ChatMessageRequestDto.builder()
                        .text("Cool message")
                        .chatContentId(storedChatContentId)
                        .userAccountId(storedUserAccountId)
                        .build()
        );
        assertThat(saved).isNotNull();
        assertThat(saved.getText()).isEqualTo("Cool message");
        assertThat(saved.getChatContent().getId()).isEqualTo(storedChatContentId);
        assertThat(saved.getUserAccount().getId()).isEqualTo(storedUserAccountId);
        assertThat(saved.getWasUpdated()).isFalse();
        assertThat(chatMessageRepository.count()).isEqualTo(3);
    }

    @Test
    void create_withNonExistentChatContentId_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                chatMessageService.create(
                        ChatMessageRequestDto.builder()
                                .text("Cool message")
                                .chatContentId(UUID.randomUUID())
                                .userAccountId(storedUserAccountId)
                                .build()
                )
        );
    }

    @Test
    void create_withNonExistentUserAccountId_throwsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                chatMessageService.create(
                        ChatMessageRequestDto.builder()
                                .text("Cool message")
                                .chatContentId(storedChatContentId)
                                .userAccountId(UUID.randomUUID())
                                .build()
                )
        );
    }

    @Test
    void create_withEmptyTextField_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> {
            chatMessageService.create(
                    ChatMessageRequestDto.builder()
                            .text("")
                            .chatContentId(storedChatContentId)
                            .userAccountId(storedUserAccountId)
                            .build()
            );
            chatMessageRepository.flush();
        });
    }

    @Test
    void update_withValidChatMessage_persistsChatMessage() {
        ChatMessage initial = chatMessageRepository.findAll().getFirst();
        Instant initialUpdateTime = initial.getUpdateTime();
        ChatMessageRequestDto request = ChatMessageRequestDto.builder()
                .text("New message")
                .build();
        ChatMessageResponseDto updated = chatMessageService.update(initial.getId(), request);
        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(initial.getId());
        assertThat(updated.getText()).isEqualTo("New message");
        assertThat(updated.getWasUpdated()).isTrue();
        assertThat(updated.getUpdateTime()).isNotEqualTo(initialUpdateTime);
        assertThat(chatMessageRepository.count()).isEqualTo(2);
    }

    @Test
    void update_withEmptyText_throwsConstraintViolationException() {
        UUID id = chatMessageRepository.findAll().getFirst().getId();
        ChatMessageRequestDto request = ChatMessageRequestDto.builder()
                .text("")
                .build();
        assertThrows(ConstraintViolationException.class, () -> {
            chatMessageService.update(id, request);
            chatMessageRepository.flush();
        });
    }

    @Test
    void update_withNonExistentId_throwsEntityNotFoundException() {
        ChatMessageRequestDto request = ChatMessageRequestDto.builder()
                .text("Some text")
                .build();
        assertThrows(EntityNotFoundException.class, () -> {
            chatMessageService.update(UUID.randomUUID(), request);
            chatMessageRepository.flush();
        });
    }

    @Test
    void search_withNoFilters_returnsAllChatMessages() {
        ChatMessageFilterDto filter = new ChatMessageFilterDto();
        filter.setChatContentId(storedChatContentId);
        Page<ChatMessageResponseDto> response = chatMessageService.search(filter, defaultPageable);
        assertThat(response).hasSize(2);
    }

    @Test
    void search_withUserAccountFilter_returnsAllUserAccountMessages() {
        ChatMessageFilterDto filter = new ChatMessageFilterDto();
        filter.setChatContentId(storedChatContentId);
        filter.setUserAccountId(storedUserAccountId);
        Page<ChatMessageResponseDto> response = chatMessageService.search(filter, defaultPageable);
        assertThat(response).hasSize(1);
    }

    @Test
    void search_withTextFilter_returnsMatchingChatMessages() {
        ChatMessageFilterDto filter = new ChatMessageFilterDto();
        filter.setChatContentId(storedChatContentId);
        filter.setText("another");
        Page<ChatMessageResponseDto> response = chatMessageService.search(filter, defaultPageable);
        assertThat(response).hasSize(1);
    }

    @Test
    void search_withInvalidTextFilter_returnsNoChatMessages() {
        ChatMessageFilterDto filter = new ChatMessageFilterDto();
        filter.setChatContentId(storedChatContentId);
        filter.setText("veryinvalidtestext");
        Page<ChatMessageResponseDto> response = chatMessageService.search(filter, defaultPageable);
        assertThat(response).hasSize(0);
    }

    @Test
    void search_withNonExistentChatContentId_throwsEntityNotFoundException() {
        ChatMessageFilterDto filter = new ChatMessageFilterDto();
        filter.setChatContentId(UUID.randomUUID());
        assertThrows(EntityNotFoundException.class, () ->
                chatMessageService.search(filter, defaultPageable)
        );
    }

    @Test
    void search_withNonExistentUserAccountId_throwsEntityNotFoundException() {
        ChatMessageFilterDto filter = new ChatMessageFilterDto();
        filter.setChatContentId(storedChatContentId);
        filter.setUserAccountId(UUID.randomUUID());
        assertThrows(EntityNotFoundException.class, () ->
                chatMessageService.search(filter, defaultPageable)
        );
    }

    @Test
    void delete_withExistingChatMessageId_deletesChatMessage() {
        UUID id = chatMessageRepository.findAll().getFirst().getId();
        chatMessageService.delete(id);
        assertThat(chatMessageRepository.count()).isEqualTo(1);
        assertThat(chatMessageRepository.existsById(id)).isFalse();
    }

    @Test
    void delete_withNonExistentChatMessageId_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                chatMessageService.delete(UUID.randomUUID())
        );
    }
}
