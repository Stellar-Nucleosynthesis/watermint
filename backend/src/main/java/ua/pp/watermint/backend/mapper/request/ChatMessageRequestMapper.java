package ua.pp.watermint.backend.mapper.request;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.pp.watermint.backend.dto.request.ChatMessageRequestDto;
import ua.pp.watermint.backend.entity.ChatContent;
import ua.pp.watermint.backend.entity.ChatMessage;
import ua.pp.watermint.backend.entity.UserAccount;
import ua.pp.watermint.backend.repository.ChatContentRepository;
import ua.pp.watermint.backend.repository.UserAccountRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatMessageRequestMapper {
    private final ChatContentRepository chatContentRepository;

    private final UserAccountRepository userAccountRepository;

    public ChatMessage dtoToChatMessage(ChatMessageRequestDto dto) {
        UUID contentId = dto.getChatContentId();
        ChatContent content = chatContentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Chat content with id " + contentId + " not found!"));
        UUID userId = dto.getUserAccountId();
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User account with id " + userId + " not found!"));

        return ChatMessage.builder()
                .text(dto.getText())
                .wasUpdated(false)
                .chatContent(content)
                .userAccount(userAccount)
                .build();
    }
}
