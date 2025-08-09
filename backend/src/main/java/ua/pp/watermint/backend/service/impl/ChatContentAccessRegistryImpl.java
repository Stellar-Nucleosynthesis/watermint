package ua.pp.watermint.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.pp.watermint.backend.service.ChatContentAccessRegistry;
import ua.pp.watermint.backend.service.ChatContentAccessService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatContentAccessRegistryImpl implements ChatContentAccessRegistry {
    private final List<ChatContentAccessService> chatContentAccessServices;


    @Override
    public boolean hasAccess(UUID userId, UUID chatContentId) {
        return chatContentAccessServices.stream()
                .anyMatch(service -> service.hasAccess(userId, chatContentId));
    }
}
