package ua.pp.watermint.backend.mapper;

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
@ContextConfiguration(classes = ChatContentMapperImpl.class)
class ChatContentMapperTest {
    @Autowired
    private ChatContentMapper chatContentMapper;

    @Test
    void chatContentToDtoTest() {
        ChatContent chatContent = TestFixtures.getExampleChatContent();
        ChatContentResponseDto dto = chatContentMapper.chatContentToDto(chatContent);
        DtoAssertions.assertChatContentEquals(chatContent, dto);
    }
}