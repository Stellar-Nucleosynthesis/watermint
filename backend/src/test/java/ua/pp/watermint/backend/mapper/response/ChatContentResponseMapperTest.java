package ua.pp.watermint.backend.mapper.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.pp.watermint.backend.dto.response.ChatContentResponseDto;
import ua.pp.watermint.backend.entity.ChatContent;
import ua.pp.watermint.backend.util.DtoAssertions;
import ua.pp.watermint.backend.util.TestFixtures;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ChatContentResponseMapperImpl.class)
class ChatContentResponseMapperTest {
    @Autowired
    private ChatContentResponseMapper chatContentResponseMapper;

    @Test
    void chatContentToDtoTest() {
        ChatContent chatContent = TestFixtures.getExampleChatContent();
        ChatContentResponseDto dto = chatContentResponseMapper.chatContentToDto(chatContent);
        DtoAssertions.assertChatContentEquals(chatContent, dto);
    }
}