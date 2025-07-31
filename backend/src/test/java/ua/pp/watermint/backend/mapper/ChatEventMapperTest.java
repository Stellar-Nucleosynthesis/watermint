package ua.pp.watermint.backend.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.pp.watermint.backend.dto.response.ChatEventResponseDto;
import ua.pp.watermint.backend.entity.ChatEvent;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ChatEventMapperImpl.class)
class ChatEventMapperTest {
    @Autowired
    private ChatEventMapper chatEventMapper;

    @Test
    public void chatEventToChatEventDtoTest(){
        ChatEvent event = getExampleChatEvent();
        ChatEventResponseDto dto = chatEventMapper.chatEventToChatEventDto(event);
        assertThat(areEqual(event, dto)).isTrue();
    }

    public static boolean areEqual(ChatEvent event, ChatEventResponseDto dto){
        if(event == null && dto == null)
            return true;
        return event != null && dto != null
                && event.getId().equals(dto.getId())
                && event.getCreateTime().equals(dto.getCreateTime())
                && event.getText().equals(dto.getText())
                && ChatContentMapperTest.areEqual(event.getChatContent(), dto.getChatContent());
    }

    public static ChatEvent getExampleChatEvent() {
        return ChatEvent.builder()
                .id(UUID.randomUUID())
                .version(2)
                .createTime(LocalDateTime.now())
                .text("Example example example")
                .chatContent(ChatContentMapperTest.getExampleChatContent())
                .build();
    }
}