package ua.pp.watermint.backend.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.pp.watermint.backend.dto.response.ChatMessageResponseDto;
import ua.pp.watermint.backend.entity.ChatMessage;
import ua.pp.watermint.backend.mapper.response.ChatMessageResponseMapper;
import ua.pp.watermint.backend.mapper.response.ChatMessageResponseMapperImpl;
import ua.pp.watermint.backend.util.DtoAssertions;
import ua.pp.watermint.backend.util.TestFixtures;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ChatMessageResponseMapperImpl.class)
class ChatMessageResponseMapperTest {
    @Autowired
    private ChatMessageResponseMapper chatMessageResponseMapper;

    @Test
    void chatMessageToDtoTest() {
        ChatMessage message = TestFixtures.getExampleChatMessage();
        ChatMessageResponseDto dto = chatMessageResponseMapper.chatMessageToDto(message);
        DtoAssertions.assertChatMessageEquals(message, dto);
    }
}