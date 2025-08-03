package ua.pp.watermint.backend.service.impl;

import org.springframework.stereotype.Service;
import ua.pp.watermint.backend.dto.filter.ChatMessageFilterDto;
import ua.pp.watermint.backend.dto.request.ChatMessageRequestDto;
import ua.pp.watermint.backend.dto.response.ChatMessageResponseDto;
import ua.pp.watermint.backend.service.ChatMessageService;

import java.util.List;
import java.util.UUID;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    @Override
    public ChatMessageResponseDto getById(UUID id) {
        return null;
    }

    @Override
    public List<ChatMessageResponseDto> search(ChatMessageFilterDto filter) {
        return List.of();
    }

    @Override
    public ChatMessageResponseDto create(ChatMessageRequestDto chatMessage) {
        return null;
    }

    @Override
    public ChatMessageResponseDto update(UUID id, ChatMessageRequestDto chatMessage) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }
}
