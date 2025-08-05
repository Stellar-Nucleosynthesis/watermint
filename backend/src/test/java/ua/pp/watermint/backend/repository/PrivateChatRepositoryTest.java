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
import ua.pp.watermint.backend.entity.PrivateChat;
import ua.pp.watermint.backend.entity.UserAccount;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class PrivateChatRepositoryTest {
    @Autowired
    private PrivateChatRepository privateChatRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ChatContentRepository  chatContentRepository;

    private PrivateChat storedPrivateChat;

    private UserAccount storedUserAccount1;

    private UserAccount storedUserAccount2;

    @BeforeEach
    public void setup() {
        storedUserAccount1 = userAccountRepository.saveAndFlush(
                UserAccount.builder()
                        .email("email@example.com")
                        .verified(true)
                        .password("password")
                        .name("name")
                        .build()
        );
        storedUserAccount2 = userAccountRepository.saveAndFlush(
                UserAccount.builder()
                        .email("another_email@example.com")
                        .verified(true)
                        .password("password")
                        .name("name")
                        .build()
        );

        storedPrivateChat = privateChatRepository.saveAndFlush(
                PrivateChat.builder()
                        .userAccount1(storedUserAccount1)
                        .userAccount2(storedUserAccount2)
                        .chatContent(new ChatContent())
                        .build()
        );
    }

    @AfterEach
    public void tearDown() {
        privateChatRepository.deleteAll();
        userAccountRepository.deleteAll();
        chatContentRepository.deleteAll();
        privateChatRepository.flush();
        userAccountRepository.flush();
        chatContentRepository.flush();
    }

    @Test
    public void chatContentIsAutomaticallyStoredTest(){
        assertThat(chatContentRepository.count()).isEqualTo(1);
    }

    @Test
    public void findAllPrivateChatsTest(){
        assertThat(privateChatRepository.count()).isEqualTo(1);
    }

    @Test
    public void findPrivateChatByIdTest(){
        UUID id = storedPrivateChat.getId();
        assertThat(privateChatRepository.findById(id))
                .isPresent()
                .contains(storedPrivateChat);
    }

    @Test
    public void addPrivateChatTest(){
        PrivateChat chat = privateChatRepository.saveAndFlush(
                PrivateChat.builder()
                        .userAccount1(storedUserAccount1)
                        .userAccount2(storedUserAccount2)
                        .chatContent(new ChatContent())
                        .build()
        );
        assertThat(chat).isNotNull();
        assertThat(chat.getId()).isNotNull();
        assertThat(chat.getVersion())
                .isNotNull()
                .isEqualTo(0);
        assertThat(chat.getCreateTime()).isNotNull();
        assertThat(chat.getUserAccount1()).isEqualTo(storedUserAccount1);
        assertThat(chat.getUserAccount2()).isEqualTo(storedUserAccount2);
        assertThat(chat.getChatContent()).isNotNull();
        assertThat(privateChatRepository.count()).isEqualTo(2);
        assertThat(chatContentRepository.count()).isEqualTo(2);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addPrivateChatWithEmptyUserTest(){
        assertThrows(ConstraintViolationException.class, () ->
                privateChatRepository.saveAndFlush(
                        PrivateChat.builder()
                                .userAccount2(storedUserAccount2)
                                .chatContent(new ChatContent())
                                .build()
                )
        );
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addPrivateChatWithEmptyChatContentTest(){
        assertThrows(ConstraintViolationException.class, () ->
                privateChatRepository.saveAndFlush(
                        PrivateChat.builder()
                                .userAccount1(storedUserAccount1)
                                .userAccount2(storedUserAccount2)
                                .build()
                )
        );
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addPrivateChatWithSimilarUsersTest(){
        assertThrows(ConstraintViolationException.class, () ->
                privateChatRepository.saveAndFlush(
                        PrivateChat.builder()
                                .userAccount1(storedUserAccount1)
                                .userAccount2(storedUserAccount1)
                                .chatContent(new ChatContent())
                                .build()
                )
        );
    }

    @Test
    public void deletePrivateChatTest(){
        privateChatRepository.deleteById(storedPrivateChat.getId());
        assertThat(privateChatRepository.count()).isZero();
    }
}
