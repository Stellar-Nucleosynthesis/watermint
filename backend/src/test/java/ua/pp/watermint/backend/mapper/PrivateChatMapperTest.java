package ua.pp.watermint.backend.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.pp.watermint.backend.dto.response.PrivateChatResponseDto;
import ua.pp.watermint.backend.entity.PrivateChat;
import ua.pp.watermint.backend.util.DtoAssertions;
import ua.pp.watermint.backend.util.TestFixtures;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PrivateChatMapperImpl.class)
class PrivateChatMapperTest {
    @Autowired
    private PrivateChatMapper privateChatMapper;

    @Test
    void privateChatToDtoTest() {
        PrivateChat chat = TestFixtures.getExamplePrivateChat();
        PrivateChatResponseDto dto = privateChatMapper.privateChatToDto(chat);
        DtoAssertions.assertPrivateChatEquals(chat, dto);
    }
}