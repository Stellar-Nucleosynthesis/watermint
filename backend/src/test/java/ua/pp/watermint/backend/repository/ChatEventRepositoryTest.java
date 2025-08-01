package ua.pp.watermint.backend.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.pp.watermint.backend.entity.ChatContent;
import ua.pp.watermint.backend.entity.ChatEvent;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class ChatEventRepositoryTest {
    @Autowired
    private ChatEventRepository chatEventRepository;

    @Autowired
    private ChatContentRepository chatContentRepository;

    private ChatEvent storedChatEvent;

    private ChatContent storedChatContent;

    @BeforeEach
    public void setUp() {
        storedChatContent = chatContentRepository.saveAndFlush(new ChatContent());
        storedChatEvent = chatEventRepository.saveAndFlush(
                ChatEvent.builder()
                        .text("Something happened")
                        .chatContent(storedChatContent)
                        .build()
        );
    }

    @AfterEach
    public void tearDown() {
        chatEventRepository.deleteAll();
        chatContentRepository.deleteAll();
        chatEventRepository.flush();
        chatContentRepository.flush();
    }

    @Test
    public void findAllChatEventsTest() {
        assertThat(chatEventRepository.findAll())
                .containsExactly(storedChatEvent);
    }

    @Test
    public void findChatEventByIdTest() {
        UUID id = storedChatEvent.getId();
        assertThat(chatEventRepository.findById(id))
                .isPresent()
                .contains(storedChatEvent);
    }

    @Test
    public void addChatEventTest() {
        ChatEvent saved = chatEventRepository.saveAndFlush(
                ChatEvent.builder()
                        .text("Anything happened")
                        .chatContent(storedChatContent)
                        .build()
        );
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVersion())
                .isNotNull()
                .isEqualTo(0);
        assertThat(saved.getCreateTime()).isNotNull();
        assertThat(saved.getText()).isEqualTo("Anything happened");
        assertThat(saved.getChatContent()).isEqualTo(storedChatContent);
        assertThat(chatEventRepository.findAll())
                .contains(saved)
                .contains(storedChatEvent);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addChatEventWithEmptyTextTest() {
        assertThrows(ConstraintViolationException.class, () ->
            chatEventRepository.saveAndFlush(
                    ChatEvent.builder()
                            .text("")
                            .chatContent(storedChatContent)
                            .build()
            )
        );
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addChatEventWithoutChatContentTest() {
        assertThrows(ConstraintViolationException.class, () ->
                chatEventRepository.saveAndFlush(
                        ChatEvent.builder()
                                .text("Something")
                                .build()
                )
        );
    }


    @Test
    public void deleteChatEventTest() {
        chatEventRepository.deleteById(storedChatEvent.getId());
        assertThat(chatEventRepository.count()).isZero();
    }
}
