package ua.pp.watermint.backend.service;

import java.util.UUID;

public interface ChatContentAccessService {
    boolean hasAccess(UUID userId, UUID chatContentId);
}
