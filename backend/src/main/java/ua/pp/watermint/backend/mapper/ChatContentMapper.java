package ua.pp.watermint.backend.mapper;

import org.mapstruct.Mapper;
import ua.pp.watermint.backend.dto.response.ChatContentResponseDto;
import ua.pp.watermint.backend.entity.ChatContent;

@Mapper
public interface ChatContentMapper {
    ChatContentResponseDto chatContentToChatContentDto(ChatContent chatContent);
}
