package ua.pp.watermint.backend.service;

import java.util.UUID;

public interface ChatContentAccessRegistry {
    boolean hasAccess(UUID userId, UUID chatContentId);
}
