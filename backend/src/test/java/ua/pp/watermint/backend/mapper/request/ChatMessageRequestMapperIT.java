package ua.pp.watermint.backend.mapper.request;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ua.pp.watermint.backend.dto.request.ChatMessageRequestDto;
import ua.pp.watermint.backend.entity.ChatMessage;
import ua.pp.watermint.backend.repository.ChatContentRepository;
import ua.pp.watermint.backend.repository.UserAccountRepository;
import ua.pp.watermint.backend.util.TestDatabaseInitializer;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({ChatMessageRequestMapper.class, TestDatabaseInitializer.class})
public class ChatMessageRequestMapperIT {
    @Autowired
    private ChatContentRepository chatContentRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ChatMessageRequestMapper chatMessageRequestMapper;

    @Test
    void dtoToChatMessage_withValidDto_returnsEntity(){
        UUID contentId = chatContentRepository.findAll().getFirst().getId();
        UUID userId = userAccountRepository.findAll().getFirst().getId();
        ChatMessageRequestDto request = ChatMessageRequestDto.builder()
                .chatContentId(contentId)
                .userAccountId(userId)
                .text("Important message")
                .build();

        ChatMessage entity = chatMessageRequestMapper.dtoToChatMessage(request);
        assertThat(entity).isNotNull();
        assertThat(entity.getText()).isEqualTo(request.getText());
        assertThat(entity.getWasUpdated()).isFalse();
        assertThat(entity.getChatContent()).isEqualTo(
                chatContentRepository.findById(contentId).orElse(null));
        assertThat(entity.getUserAccount()).isEqualTo(
                userAccountRepository.findById(userId).orElse(null));
    }

    @Test
    void dtoToChatMessage_withNonExistentChatContentId_throwsEntityNotFoundException(){
        UUID userId = userAccountRepository.findAll().getFirst().getId();
        assertThrows(EntityNotFoundException.class, () ->
                chatMessageRequestMapper.dtoToChatMessage(
                        ChatMessageRequestDto.builder()
                                .chatContentId(UUID.randomUUID())
                                .userAccountId(userId)
                                .text("Nothing happened")
                                .build()
                )
        );
    }

    @Test
    void dtoToChatMessage_withNonExistentUserAccountId_throwsEntityNotFoundException(){
        UUID contentId = chatContentRepository.findAll().getFirst().getId();
        assertThrows(EntityNotFoundException.class, () ->
                chatMessageRequestMapper.dtoToChatMessage(
                        ChatMessageRequestDto.builder()
                                .chatContentId(contentId)
                                .userAccountId(UUID.randomUUID())
                                .text("Nothing happened")
                                .build()
                )
        );
    }
}
