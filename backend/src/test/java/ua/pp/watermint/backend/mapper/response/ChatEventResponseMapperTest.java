package ua.pp.watermint.backend.mapper.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.pp.watermint.backend.dto.response.ChatEventResponseDto;
import ua.pp.watermint.backend.entity.ChatEvent;
import ua.pp.watermint.backend.util.DtoAssertions;
import ua.pp.watermint.backend.util.TestFixtures;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ChatEventResponseMapperImpl.class)
class ChatEventResponseMapperTest {
    @Autowired
    private ChatEventResponseMapper chatEventResponseMapper;

    @Test
    void chatEventToDtoTest() {
        ChatEvent event = TestFixtures.getExampleChatEvent();
        ChatEventResponseDto dto = chatEventResponseMapper.chatEventToDto(event);
        DtoAssertions.assertChatEventEquals(event, dto);
    }
}