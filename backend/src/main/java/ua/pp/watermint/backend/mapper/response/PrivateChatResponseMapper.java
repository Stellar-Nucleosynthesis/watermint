package ua.pp.watermint.backend.mapper.response;

import org.mapstruct.Mapper;
import ua.pp.watermint.backend.dto.response.PrivateChatResponseDto;
import ua.pp.watermint.backend.entity.PrivateChat;

@Mapper
public interface PrivateChatResponseMapper {
    PrivateChatResponseDto privateChatToDto(PrivateChat privateChat);
}
