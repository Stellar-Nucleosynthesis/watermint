package ua.pp.watermint.backend.mapper.response;

import org.mapstruct.Mapper;
import ua.pp.watermint.backend.dto.response.ChatEventResponseDto;
import ua.pp.watermint.backend.entity.ChatEvent;

@Mapper
public interface ChatEventResponseMapper {
    ChatEventResponseDto chatEventToDto(ChatEvent chatEvent);
}
