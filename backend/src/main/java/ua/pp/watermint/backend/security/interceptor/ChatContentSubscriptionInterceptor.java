package ua.pp.watermint.backend.security.interceptor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import ua.pp.watermint.backend.entity.UserAccount;
import ua.pp.watermint.backend.security.service.AuthorizationService;
import ua.pp.watermint.backend.service.ChatContentAccessRegistry;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatContentSubscriptionInterceptor implements ChannelInterceptor {
    private final ChatContentAccessRegistry chatContentAccessRegistry;
    private final AuthorizationService authorizationService;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            UserAccount currentUser = authorizationService.getCurrentUser();
            if (destination != null && destination.startsWith("/topic/room/")) {
                UUID chatContentId = UUID.fromString(destination.substring("/topic/room/".length()));
                if (!chatContentAccessRegistry.hasAccess(currentUser.getId(), chatContentId)) {
                    throw new AccessDeniedException("User " + currentUser.getUsername() + " has no access to the chat!");
                }
            }
        }
        return message;
    }
}
