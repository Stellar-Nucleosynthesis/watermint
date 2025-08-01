package ua.pp.watermint.backend.mapper;

import org.mapstruct.Mapper;
import ua.pp.watermint.backend.dto.response.ChatEventResponseDto;
import ua.pp.watermint.backend.entity.ChatEvent;

@Mapper
public interface ChatEventMapper {
    ChatEventResponseDto chatEventToDto(ChatEvent chatEvent);
}
