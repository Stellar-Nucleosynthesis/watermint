package ua.pp.watermint.backend.service;

import ua.pp.watermint.backend.dto.filter.ChatMessageFilterDto;
import ua.pp.watermint.backend.dto.request.ChatMessageRequestDto;
import ua.pp.watermint.backend.dto.response.ChatMessageResponseDto;

import java.util.List;
import java.util.UUID;

public interface ChatMessageService {
    ChatMessageResponseDto getById(UUID id);
    List<ChatMessageResponseDto> search(ChatMessageFilterDto filter);
    ChatMessageResponseDto create(ChatMessageRequestDto chatMessage);
    ChatMessageResponseDto update(UUID id, ChatMessageRequestDto chatMessage);
    void delete(UUID id);
}
