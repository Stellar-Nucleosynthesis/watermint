package ua.pp.watermint.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.pp.watermint.backend.repository.PrivateChatRepository;
import ua.pp.watermint.backend.service.ChatContentAccessService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrivateChatContentAccessService implements ChatContentAccessService {
    private final PrivateChatRepository privateChatRepository;

    @Override
    public boolean hasAccess(UUID userId, UUID chatContentId) {
        return privateChatRepository.existsByUserAccount1_IdAndChatContent_Id(userId, chatContentId)
                || privateChatRepository.existsByUserAccount2_IdAndChatContent_Id(userId, chatContentId);
    }
}
