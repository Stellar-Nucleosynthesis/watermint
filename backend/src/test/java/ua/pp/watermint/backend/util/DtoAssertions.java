package ua.pp.watermint.backend.util;

import ua.pp.watermint.backend.dto.response.*;
import ua.pp.watermint.backend.entity.*;

import static org.assertj.core.api.Assertions.assertThat;

public class DtoAssertions {

    public static void assertUserAccountEquals(UserAccount user, UserAccountResponseDto dto) {
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(user.getId());
        assertThat(dto.getCreateTime()).isEqualTo(user.getCreateTime());
        assertThat(dto.getUpdateTime()).isEqualTo(user.getUpdateTime());
        assertThat(dto.getEmail()).isEqualTo(user.getEmail());
        assertThat(dto.getVerified()).isEqualTo(user.getVerified());
        assertThat(dto.getUsername()).isEqualTo(user.getUsername());
        assertThat(dto.getPassword()).isEqualTo(user.getPassword());
        assertThat(dto.getName()).isEqualTo(user.getName());
        assertThat(dto.getBirthDate()).isEqualTo(user.getBirthDate());
        assertThat(dto.getProfilePicture()).isEqualTo(user.getProfilePicture());
    }

    public static void assertChatContentEquals(ChatContent content, ChatContentResponseDto dto) {
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(content.getId());
    }

    public static void assertChatMessageEquals(ChatMessage message, ChatMessageResponseDto dto) {
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(message.getId());
        assertThat(dto.getCreateTime()).isEqualTo(message.getCreateTime());
        assertThat(dto.getUpdateTime()).isEqualTo(message.getUpdateTime());
        assertThat(dto.getText()).isEqualTo(message.getText());
        assertUserAccountEquals(message.getUserAccount(), dto.getUserAccount());
        assertChatContentEquals(message.getChatContent(), dto.getChatContent());
    }

    public static void assertChatEventEquals(ChatEvent event, ChatEventResponseDto dto) {
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(event.getId());
        assertThat(dto.getCreateTime()).isEqualTo(event.getCreateTime());
        assertThat(dto.getText()).isEqualTo(event.getText());
        assertChatContentEquals(event.getChatContent(), dto.getChatContent());
    }

    public static void assertPrivateChatEquals(PrivateChat chat, PrivateChatResponseDto dto) {
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(chat.getId());
        assertThat(dto.getCreateTime()).isEqualTo(chat.getCreateTime());
        assertUserAccountEquals(chat.getUserAccount1(), dto.getUserAccount1());
        assertUserAccountEquals(chat.getUserAccount2(), dto.getUserAccount2());
        assertChatContentEquals(chat.getChatContent(), dto.getChatContent());
    }
}