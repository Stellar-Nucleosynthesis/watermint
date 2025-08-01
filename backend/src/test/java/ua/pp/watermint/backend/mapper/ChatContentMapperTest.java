package ua.pp.watermint.backend.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.pp.watermint.backend.dto.response.ChatContentResponseDto;
import ua.pp.watermint.backend.entity.ChatContent;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ChatContentMapperImpl.class)
class ChatContentMapperTest {
    @Autowired
    private ChatContentMapper chatContentMapper;

    @Test
    void chatContentToDtoTest() {
        ChatContent chatContent = getExampleChatContent();
        ChatContentResponseDto dto = chatContentMapper.chatContentToDto(chatContent);
        assertThat(areEqual(chatContent, dto)).isTrue();
    }

    public static ChatContent getExampleChatContent(){
        return ChatContent.builder()
                .id(UUID.randomUUID())
                .version(2)
                .build();
    }

    public static boolean areEqual(ChatContent content, ChatContentResponseDto dto){
        if(content == null && dto == null)
            return true;
        return content != null && dto != null
                && content.getId().equals(dto.getId());
    }
}