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
                .userAccount1(UserAccountMapperTest.getExampleUserAccount())
                .userAccount2(UserAccountMapperTest.getExampleUserAccount())
                .chatContent(ChatContentMapperTest.getExampleChatContent())
                .build();
    }

    private static boolean areEqual(PrivateChat chat, PrivateChatResponseDto dto){
        if(chat == null && dto == null)
            return true;
        return chat != null && dto != null
                && chat.getId().equals(dto.getId())
                && chat.getCreateTime().equals(dto.getCreateTime())
                && UserAccountMapperTest.areEqual(
                        chat.getUserAccount1(), dto.getUserAccount1())
                && UserAccountMapperTest.areEqual(
                        chat.getUserAccount1(), dto.getUserAccount1())
                && ChatContentMapperTest.areEqual(
                        chat.getChatContent(), dto.getChatContent());
    }
}