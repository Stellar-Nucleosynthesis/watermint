package ua.pp.watermint.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.pp.watermint.backend.dto.filter.ChatMessageFilterDto;
import ua.pp.watermint.backend.dto.request.ChatMessageRequestDto;
import ua.pp.watermint.backend.dto.response.ChatMessageResponseDto;

import java.util.UUID;

public interface ChatMessageService {
    ChatMessageResponseDto getById(UUID id);
    Page<ChatMessageResponseDto> search(ChatMessageFilterDto filter, Pageable pageable);
    ChatMessageResponseDto create(ChatMessageRequestDto dto);
    ChatMessageResponseDto update(UUID id, ChatMessageRequestDto dto);
    void delete(UUID id);
}
