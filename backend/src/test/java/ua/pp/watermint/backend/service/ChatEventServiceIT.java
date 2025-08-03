package ua.pp.watermint.backend.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ua.pp.watermint.backend.dto.filter.ChatEventFilterDto;
import ua.pp.watermint.backend.dto.request.ChatEventRequestDto;
import ua.pp.watermint.backend.dto.response.ChatEventResponseDto;
import ua.pp.watermint.backend.entity.ChatEvent;
import ua.pp.watermint.backend.repository.ChatContentRepository;
import ua.pp.watermint.backend.repository.ChatEventRepository;
import ua.pp.watermint.backend.util.TestDatabaseInitializer;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ua.pp.watermint.backend.util.DtoAssertions.assertChatEventEquals;

@SpringBootTest
@Transactional
@Import(TestDatabaseInitializer.class)
class ChatEventServiceIT {
    @Autowired
    private ChatEventService chatEventService;

    @Autowired
    private ChatEventRepository chatEventRepository;

    @Autowired
    private ChatContentRepository chatContentRepository;

    private UUID storedChatContentId;

    @BeforeEach
    void setUp() {
        storedChatContentId = chatContentRepository.findAll().getFirst().getId();
    }

    @Test
    void getById_withExistingId_returnsChatEvent() {
        ChatEvent event = chatEventRepository.findAll().getFirst();
        ChatEventResponseDto response = chatEventService.getById(event.getId());
        assertThat(response).isNotNull();
        assertChatEventEquals(event, response);
    }

    @Test
    void getById_withNonExistentId_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                chatEventService.getById(UUID.randomUUID())
        );
    }

    @Test
    void create_withValidChatEvent_persistsChatEvent() {
        ChatEventResponseDto saved = chatEventService.create(
                ChatEventRequestDto.builder()
                        .text("Something happened")
                        .chatContentId(storedChatContentId)
                        .build()
        );
        assertThat(saved).isNotNull();
        assertThat(saved.getText()).isEqualTo("Something happened");
        assertThat(saved.getChatContent().getId()).isEqualTo(storedChatContentId);
        assertThat(chatEventRepository.count()).isEqualTo(3);
    }

    @Test
    void create_withNonExistentChatContentId_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                chatEventService.create(
                        ChatEventRequestDto.builder()
                                .text("Something happened")
                                .chatContentId(UUID.randomUUID())
                                .build()
                )
        );
    }

    @Test
    void create_withEmptyTextField_throwsIllegalArgumentException() {
        assertThrows(ConstraintViolationException.class, () -> {
                    chatEventService.create(
                            ChatEventRequestDto.builder()
                                    .text("")
                                    .chatContentId(storedChatContentId)
                                    .build()
                    );
                    chatEventRepository.flush();
        });
    }

    @Test
    void search_withNoFilters_returnsAllChatEvents() {
        ChatEventFilterDto filter = new ChatEventFilterDto();
        filter.setChatContentId(storedChatContentId);
        List<ChatEventResponseDto> response = chatEventService.search(filter);
        assertThat(response).hasSize(2);
    }

    @Test
    void search_withTextFilter_returnsMatchingChatEvents() {
        ChatEventFilterDto filter = new ChatEventFilterDto();
        filter.setChatContentId(storedChatContentId);
        filter.setText("birthday");
        List<ChatEventResponseDto> response = chatEventService.search(filter);
        assertThat(response).hasSize(1);
    }

    @Test
    void search_withInvalidTextFilter_returnsNoChatEvents() {
        ChatEventFilterDto filter = new ChatEventFilterDto();
        filter.setChatContentId(storedChatContentId);
        filter.setText("veryinvalidtestext");
        List<ChatEventResponseDto> response = chatEventService.search(filter);
        assertThat(response).hasSize(0);
    }

    @Test
    void search_withNonExistentChatContentId_throwsEntityNotFoundException() {
        ChatEventFilterDto filter = new ChatEventFilterDto();
        filter.setChatContentId(UUID.randomUUID());
        assertThrows(EntityNotFoundException.class, () ->
                chatEventService.search(filter)
        );
    }
}