package ua.pp.watermint.backend.mapper;

import org.mapstruct.Mapper;
import ua.pp.watermint.backend.dto.response.ChatMessageResponseDto;
import ua.pp.watermint.backend.entity.ChatMessage;

@Mapper
public interface ChatMessageMapper {
    ChatMessageResponseDto chatMessageToChatMessageDto(ChatMessage chatMessage);
}
