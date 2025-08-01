package ua.pp.watermint.backend.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.pp.watermint.backend.dto.response.ChatMessageResponseDto;
import ua.pp.watermint.backend.entity.ChatMessage;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ChatMessageMapperImpl.class)
class ChatMessageMapperTest {
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Test
    public void chatMessageToChatMessageDtoTest(){
        ChatMessage chatMessage = getExampleChatMessage();
        ChatMessageResponseDto dto = chatMessageMapper.chatMessageToChatMessageDto(chatMessage);
        assertThat(areEqual(chatMessage, dto)).isTrue();
    }

    public static boolean areEqual(ChatMessage message, ChatMessageResponseDto dto){
        if(message == null && dto == null)
            return true;
        return message != null && dto != null
                && message.getId().equals(dto.getId())
                && message.getCreateTime().equals(dto.getCreateTime())
                && message.getUpdateTime().equals(dto.getUpdateTime())
                && message.getText().equals(dto.getText())
                && UserAccountMapperTest.areEqual(
                        message.getUserAccount(), dto.getUserAccount())
                && ChatContentMapperTest.areEqual(
                        message.getChatContent(), dto.getChatContent());
    }

    public static ChatMessage getExampleChatMessage() {
        return ChatMessage.builder()
                .id(UUID.randomUUID())
                .version(2)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .text("Example example example")
                .userAccount(UserAccountMapperTest.getExampleUserAccount())
                .chatContent(ChatContentMapperTest.getExampleChatContent())
                .build();
    }
}