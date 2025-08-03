package ua.pp.watermint.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.pp.watermint.backend.dto.filter.ChatEventFilterDto;
import ua.pp.watermint.backend.dto.request.ChatEventRequestDto;
import ua.pp.watermint.backend.dto.response.ChatEventResponseDto;
import ua.pp.watermint.backend.service.ChatEventService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatEventServiceImpl implements ChatEventService {
    @Override
    public ChatEventResponseDto getById(UUID id) {
        return null;
    }

    @Override
    public ChatEventResponseDto create(ChatEventRequestDto chatEvent) {
        return null;
    }

    @Override
    public List<ChatEventResponseDto> search(ChatEventFilterDto filter) {
        return List.of();
    }
}
