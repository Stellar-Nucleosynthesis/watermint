package ua.pp.watermint.backend.mapper.request;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.pp.watermint.backend.dto.request.ChatEventRequestDto;
import ua.pp.watermint.backend.entity.ChatEvent;
import ua.pp.watermint.backend.repository.ChatContentRepository;
import ua.pp.watermint.backend.util.BaseTestEnv;
import ua.pp.watermint.backend.util.TestDatabaseInitializer;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({ChatEventRequestMapper.class, TestDatabaseInitializer.class, BCryptPasswordEncoder.class})
public class ChatEventRequestMapperIT extends BaseTestEnv {
    @Autowired
    private ChatContentRepository chatContentRepository;

    @Autowired
    private ChatEventRequestMapper chatEventRequestMapper;

    @Test
    void dtoToChatEvent_withValidDto_returnsEntity(){
        UUID contentId = chatContentRepository.findAll().getFirst().getId();
        ChatEventRequestDto request = ChatEventRequestDto.builder()
                .chatContentId(contentId)
                .text("Nothing happened")
                .build();

        ChatEvent entity = chatEventRequestMapper.dtoToChatEvent(request);
        assertThat(entity).isNotNull();
        assertThat(entity.getText()).isEqualTo(request.getText());
        assertThat(entity.getChatContent()).isEqualTo(
                chatContentRepository.findById(contentId).orElse(null));
    }

    @Test
    void dtoToChatEvent_withNonExistentChatContentId_throwsEntityNotFoundException(){
        assertThrows(EntityNotFoundException.class, () ->
                chatEventRequestMapper.dtoToChatEvent(
                        ChatEventRequestDto.builder()
                                .chatContentId(UUID.randomUUID())
                                .text("Nothing happened")
                                .build()
                )
        );
    }
}
