package ua.pp.watermint.backend.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.pp.watermint.backend.entity.ChatContent;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ChatContentRepositoryTest {
    @Autowired
    private ChatContentRepository chatContentRepository;

    private ChatContent storedChatContent;

    @BeforeEach
    public void setup() {
        storedChatContent = chatContentRepository.saveAndFlush(new ChatContent());
    }

    @AfterEach
    public void tearDown() {
        chatContentRepository.deleteAll();
        chatContentRepository.flush();
    }

    @Test
    void findAllChatContentsTest() {
        assertThat(chatContentRepository.findAll())
                .containsExactly(storedChatContent);
    }

    @Test
    void findChatContentByIdTest() {
        UUID id = storedChatContent.getId();
        assertThat(chatContentRepository.findById(id))
                .isPresent()
                .contains(storedChatContent);
    }

    @Test
    void addChatContentTest() {
        ChatContent saved = chatContentRepository.saveAndFlush(new ChatContent());
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVersion())
                .isNotNull()
                .isEqualTo(0);
        assertThat(chatContentRepository.findAll())
                .contains(saved)
                .contains(storedChatContent);
    }

    @Test
    void deleteStoredChatContentTest() {
        chatContentRepository.deleteById(storedChatContent.getId());
        assertThat(chatContentRepository.count()).isZero();
    }
}
