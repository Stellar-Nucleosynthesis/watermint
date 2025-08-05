package ua.pp.watermint.backend.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import ua.pp.watermint.backend.entity.*;
import ua.pp.watermint.backend.repository.*;

import java.util.List;

@RequiredArgsConstructor
public class TestDatabaseInitializer implements CommandLineRunner {
    private final UserAccountRepository userAccountRepository;
    private final PrivateChatRepository privateChatRepository;
    private final ChatContentRepository chatContentRepository;
    private final ChatEventRepository chatEventRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public void run(String... args) {
        addUsers();
        addPrivateChats();
        addChatEvents();
        addChatMessages();
    }

    private void addUsers(){
        userAccountRepository.saveAllAndFlush(List.of(
                UserAccount.builder()
                        .email("user1@example.com")
                        .verified(true)
                        .username("user1")
                        .password("password")
                        .name("first name")
                        .build(),
                UserAccount.builder()
                        .email("user2@example.com")
                        .verified(true)
                        .username("user2")
                        .password("password")
                        .name("second name")
                        .build()
        ));
    }

    private void addPrivateChats(){
        List<UserAccount> userAccounts = userAccountRepository.findAll();
        privateChatRepository.saveAndFlush(
                PrivateChat.builder()
                        .userAccount1(userAccounts.getFirst())
                        .userAccount2(userAccounts.getLast())
                        .chatContent(new ChatContent())
                        .build()
        );
    }

    private void addChatEvents(){
        ChatContent content = chatContentRepository.findAll().getFirst();
        chatEventRepository.saveAllAndFlush(
                List.of(
                        ChatEvent.builder()
                                .text("Something happened")
                                .chatContent(content)
                                .build(),
                        ChatEvent.builder()
                                .text("Birthday!")
                                .chatContent(content)
                                .build()
                )
        );
    }

    private void addChatMessages(){
        ChatContent content = chatContentRepository.findAll().getFirst();
        List<UserAccount> userAccounts = userAccountRepository.findAll();
        chatMessageRepository.saveAllAndFlush(List.of(
                ChatMessage.builder()
                        .wasUpdated(false)
                        .text("Message!")
                        .userAccount(userAccounts.getFirst())
                        .chatContent(content)
                        .build(),
                ChatMessage.builder()
                        .wasUpdated(false)
                        .text("Another message!")
                        .userAccount(userAccounts.getLast())
                        .chatContent(content)
                        .build()
        ));
    }
}
