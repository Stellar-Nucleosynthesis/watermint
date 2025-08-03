package ua.pp.watermint.backend.mapper.request;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.pp.watermint.backend.dto.request.ChatEventRequestDto;
import ua.pp.watermint.backend.entity.ChatContent;
import ua.pp.watermint.backend.entity.ChatEvent;
import ua.pp.watermint.backend.repository.ChatContentRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatEventRequestMapper {
    private final ChatContentRepository chatContentRepository;

    public ChatEvent dtoToChatEvent(ChatEventRequestDto dto){
        UUID contentId = dto.getChatContentId();
        ChatContent content = chatContentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Chat content with id " + contentId + " not found!"));

        return ChatEvent.builder()
                .text(dto.getText())
                .chatContent(content)
                .build();
    }
}
