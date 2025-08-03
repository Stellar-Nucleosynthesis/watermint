package ua.pp.watermint.backend.mapper.response;

import org.mapstruct.Mapper;
import ua.pp.watermint.backend.dto.response.ChatContentResponseDto;
import ua.pp.watermint.backend.entity.ChatContent;

@Mapper
public interface ChatContentResponseMapper {
    ChatContentResponseDto chatContentToDto(ChatContent chatContent);
}
