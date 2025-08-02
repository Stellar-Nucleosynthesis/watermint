package ua.pp.watermint.backend.service;

import ua.pp.watermint.backend.dto.request.PrivateChatRequestDto;
import ua.pp.watermint.backend.dto.response.PrivateChatResponseDto;

import java.util.List;
import java.util.UUID;

public interface PrivateChatService {
    PrivateChatResponseDto getById(UUID id);
    List<PrivateChatResponseDto> search(UUID user1Id, String user2Name);
    PrivateChatResponseDto create(PrivateChatRequestDto privateChat);
    void delete(UUID id);
}
