package ua.pp.watermint.backend.mapper;

import org.mapstruct.Mapper;
import ua.pp.watermint.backend.dto.response.PrivateChatResponseDto;
import ua.pp.watermint.backend.entity.PrivateChat;

@Mapper
public interface PrivateChatMapper {
    PrivateChatResponseDto privateChatToPrivateChatDto(PrivateChat privateChat);
}
