package ua.pp.watermint.backend.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.pp.watermint.backend.dto.response.PrivateChatResponseDto;
import ua.pp.watermint.backend.entity.PrivateChat;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PrivateChatMapperImpl.class)
class PrivateChatMapperTest {
    @Autowired
    private PrivateChatMapper privateChatMapper;

    @Test
    public void PrivateChatToPrivateChatDtoTest() {
        PrivateChat privateChat = getExamplePrivateChat();
        PrivateChatResponseDto dto = privateChatMapper.privateChatToPrivateChatDto(privateChat);
        assertThat(areEqual(privateChat, dto)).isTrue();
    }

    private static PrivateChat getExamplePrivateChat(){
        return PrivateChat.builder()
                .id(UUID.randomUUID())
                .version(3)
                .createTime(LocalDateTime.now())
                .user1(UserMapperTest.getExampleUser())
                .user2(UserMapperTest.getExampleUser())
                .chatContent(ChatContentMapperTest.getExampleChatContent())
                .build();
    }

    private static boolean areEqual(PrivateChat chat, PrivateChatResponseDto dto){
        if(chat == null && dto == null)
            return true;
        return chat != null && dto != null
                && chat.getId().equals(dto.getId())
                && chat.getCreateTime().equals(dto.getCreateTime())
                && UserMapperTest.areEqual(chat.getUser1(), dto.getUser1())
                && UserMapperTest.areEqual(chat.getUser2(), dto.getUser2())
                && ChatContentMapperTest.areEqual(
                        chat.getChatContent(), dto.getChatContent());
    }
}