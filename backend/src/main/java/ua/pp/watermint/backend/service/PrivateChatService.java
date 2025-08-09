package ua.pp.watermint.backend.service;

import ua.pp.watermint.backend.dto.filter.PrivateChatFilterDto;
import ua.pp.watermint.backend.dto.request.PrivateChatRequestDto;
import ua.pp.watermint.backend.dto.response.PrivateChatResponseDto;

import java.util.List;
import java.util.UUID;

public interface PrivateChatService {
    PrivateChatResponseDto getById(UUID id);
    List<PrivateChatResponseDto> search(PrivateChatFilterDto filter);
    PrivateChatResponseDto create(PrivateChatRequestDto dto);
    void delete(UUID id);
}
