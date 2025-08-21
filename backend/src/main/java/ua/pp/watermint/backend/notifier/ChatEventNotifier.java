package ua.pp.watermint.backend.notifier;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ua.pp.watermint.backend.dto.response.ChatEventResponseDto;
import ua.pp.watermint.backend.dto.notification.ChatEventNotificationDto;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatEventNotifier {
    private final SimpMessagingTemplate messagingTemplate;

    public void broadcastCreate(ChatEventResponseDto event) {
        UUID chatContentId = event.getChatContent().getId();
        messagingTemplate.convertAndSend("/chatContent/" + chatContentId + "/events",
                new ChatEventNotificationDto(ChatEventNotificationDto.Action.CREATE, event));
    }
}
