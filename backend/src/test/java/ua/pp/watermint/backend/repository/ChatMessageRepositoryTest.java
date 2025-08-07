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
import ua.pp.watermint.backend.entity.ChatMessage;
import ua.pp.watermint.backend.entity.UserAccount;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class ChatMessageRepositoryTest {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatContentRepository chatContentRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    private ChatMessage storedChatMessage;

    private ChatContent storedChatContent;

    private UserAccount storedUserAccount;

    @BeforeEach
    public void setUp() {
        storedChatContent = chatContentRepository.saveAndFlush(new ChatContent());
        storedUserAccount = userAccountRepository.saveAndFlush(
                UserAccount.builder()
                        .email("email@example.com")
                        .verified(true)
                        .username("username")
                        .password("password")
                        .name("name")
                        .build()
        );
        storedChatMessage = chatMessageRepository.saveAndFlush(
                ChatMessage.builder()
                        .text("Words")
                        .wasUpdated(false)
                        .userAccount(storedUserAccount)
                        .chatContent(storedChatContent)
                        .build()
        );
    }

    @AfterEach
    public void tearDown() {
        chatMessageRepository.deleteAll();
        chatContentRepository.deleteAll();
        userAccountRepository.deleteAll();
        chatMessageRepository.flush();
        chatContentRepository.flush();
        userAccountRepository.flush();
    }

    @Test
    public void findAllChatMessagesTest() {
        assertThat(chatMessageRepository.findAll())
                .containsExactly(storedChatMessage);
    }

    @Test
    public void findChatMessageByIdTest() {
        UUID id = storedChatMessage.getId();
        assertThat(chatMessageRepository.findById(id))
                .isPresent()
                .contains(storedChatMessage);
    }

    @Test
    public void addChatMessageTest() {
        ChatMessage saved = chatMessageRepository.saveAndFlush(
                ChatMessage.builder()
                        .text("Other words")
                        .wasUpdated(false)
                        .userAccount(storedUserAccount)
                        .chatContent(storedChatContent)
                        .build()
        );
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVersion())
                .isNotNull()
                .isEqualTo(0);
        assertThat(saved.getCreateTime()).isNotNull();
        assertThat(saved.getText()).isEqualTo("Other words");
        assertThat(saved.getChatContent()).isEqualTo(storedChatContent);
        assertThat(saved.getUserAccount()).isEqualTo(storedUserAccount);
        assertThat(chatMessageRepository.findAll())
                .contains(saved)
                .contains(storedChatMessage);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addChatMessageWithoutUserTest() {
        assertThrows(ConstraintViolationException.class, () ->
                chatMessageRepository.saveAndFlush(
                        ChatMessage.builder()
                                .text("Other words")
                                .wasUpdated(false)
                                .chatContent(storedChatContent)
                                .build()
                )
        );
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addChatMessageWithoutChatContentTest() {
        assertThrows(ConstraintViolationException.class, () ->
                chatMessageRepository.saveAndFlush(
                        ChatMessage.builder()
                                .text("Other words")
                                .wasUpdated(false)
                                .userAccount(storedUserAccount)
                                .build()
                )
        );
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addChatMessageWithEmptyTextTest() {
        assertThrows(ConstraintViolationException.class, () ->
                chatMessageRepository.saveAndFlush(
                        ChatMessage.builder()
                                .text("")
                                .wasUpdated(false)
                                .userAccount(storedUserAccount)
                                .chatContent(storedChatContent)
                                .build()
                )
        );
    }

    @Test
    public void updateChatMessageTest() {
        UUID id = storedChatMessage.getId();
        LocalDateTime lastUpdate = storedChatMessage.getUpdateTime();
        storedChatMessage.setText("New text");
        chatMessageRepository.saveAndFlush(storedChatMessage);
        assertThat(chatMessageRepository.count()).isEqualTo(1);
        ChatMessage updated = chatMessageRepository
                .findById(id).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getText()).isEqualTo("New text");
        assertThat(updated.getUpdateTime()).isNotEqualTo(lastUpdate);
        assertThat(updated.getVersion()).isEqualTo(1);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void updateChatMessageWithInvalidTextTest() {
        storedChatMessage.setText("");
        assertThrows(ConstraintViolationException.class, () ->
                chatMessageRepository.saveAndFlush(storedChatMessage)
        );
    }


    @Test
    public void deleteChatMessageTest() {
        chatMessageRepository.deleteById(storedChatMessage.getId());
        assertThat(chatMessageRepository.count()).isZero();
    }
}
