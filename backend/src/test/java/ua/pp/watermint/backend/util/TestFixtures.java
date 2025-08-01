package ua.pp.watermint.backend.util;

import ua.pp.watermint.backend.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestFixtures {

    public static UserAccount getExampleUserAccount() {
        return UserAccount.builder()
                .id(UUID.randomUUID())
                .version(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .email("email@example.com")
                .verified(true)
                .username("username")
                .password("password")
                .name("name")
                .birthDate(LocalDate.EPOCH)
                .profilePicture(new byte[]{3, 4, 5, 6, 7, 8})
                .build();
    }

    public static ChatContent getExampleChatContent() {
        return ChatContent.builder()
                .id(UUID.randomUUID())
                .version(0)
                .build();
    }

    public static ChatMessage getExampleChatMessage() {
        return ChatMessage.builder()
                .id(UUID.randomUUID())
                .version(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .text("Example example example")
                .userAccount(getExampleUserAccount())
                .chatContent(getExampleChatContent())
                .build();
    }

    public static ChatEvent getExampleChatEvent() {
        return ChatEvent.builder()
                .id(UUID.randomUUID())
                .version(0)
                .createTime(LocalDateTime.now())
                .text("Example example example")
                .chatContent(getExampleChatContent())
                .build();
    }

    public static PrivateChat getExamplePrivateChat() {
        return PrivateChat.builder()
                .id(UUID.randomUUID())
                .version(0)
                .createTime(LocalDateTime.now())
                .userAccount1(getExampleUserAccount())
                .userAccount2(getExampleUserAccount())
                .chatContent(getExampleChatContent())
                .build();
    }
}

