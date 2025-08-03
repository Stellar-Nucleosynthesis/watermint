package ua.pp.watermint.backend.mapper.response;

import org.mapstruct.Mapper;
import ua.pp.watermint.backend.dto.response.ChatMessageResponseDto;
import ua.pp.watermint.backend.entity.ChatMessage;

@Mapper
public interface ChatMessageResponseMapper {
    ChatMessageResponseDto chatMessageToDto(ChatMessage chatMessage);
}
