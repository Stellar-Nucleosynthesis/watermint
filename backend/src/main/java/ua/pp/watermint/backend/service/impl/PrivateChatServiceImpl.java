package ua.pp.watermint.backend.service.impl;

import org.springframework.stereotype.Service;
import ua.pp.watermint.backend.dto.filter.PrivateChatFilterDto;
import ua.pp.watermint.backend.dto.request.PrivateChatRequestDto;
import ua.pp.watermint.backend.dto.response.PrivateChatResponseDto;
import ua.pp.watermint.backend.service.PrivateChatService;

import java.util.List;
import java.util.UUID;

@Service
public class PrivateChatServiceImpl implements PrivateChatService {
    @Override
    public PrivateChatResponseDto getById(UUID id) {
        return null;
    }

    @Override
    public List<PrivateChatResponseDto> search(PrivateChatFilterDto filter) {
        return List.of();
    }

    @Override
    public PrivateChatResponseDto create(PrivateChatRequestDto privateChat) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }
}
