package ua.pp.watermint.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.pp.watermint.backend.dto.filter.ChatEventFilterDto;
import ua.pp.watermint.backend.dto.request.ChatEventRequestDto;
import ua.pp.watermint.backend.dto.response.ChatEventResponseDto;

import java.util.UUID;

public interface ChatEventService {
    ChatEventResponseDto getById(UUID id);
    ChatEventResponseDto create(ChatEventRequestDto dto);
    Page<ChatEventResponseDto> search(ChatEventFilterDto filter, Pageable pageable);
}
