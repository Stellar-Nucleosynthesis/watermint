package ua.pp.watermint.backend.service;

import ua.pp.watermint.backend.dto.filter.ChatEventFilterDto;
import ua.pp.watermint.backend.dto.request.ChatEventRequestDto;
import ua.pp.watermint.backend.dto.response.ChatEventResponseDto;

import java.util.List;
import java.util.UUID;

public interface ChatEventService {
    ChatEventResponseDto getById(UUID id);
    ChatEventResponseDto create(ChatEventRequestDto chatEvent);
    List<ChatEventResponseDto> search(ChatEventFilterDto filter);
}
