package ua.pp.watermint.backend.notifier;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ua.pp.watermint.backend.dto.notification.ChatMessageNotificationDto;
import ua.pp.watermint.backend.dto.response.ChatMessageResponseDto;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatMessageNotifier {
    private final SimpMessagingTemplate messagingTemplate;

    public void broadcastCreate(ChatMessageResponseDto message) {
        UUID chatContentId = message.getChatContent().getId();
        messagingTemplate.convertAndSend("/chatContent/" + chatContentId + "/messages",
                new ChatMessageNotificationDto(ChatMessageNotificationDto.Action.CREATE, message));
    }

    public void broadcastUpdate(ChatMessageResponseDto message) {
        UUID chatContentId = message.getChatContent().getId();
        messagingTemplate.convertAndSend("/chatContent/" + chatContentId + "/messages",
                new ChatMessageNotificationDto(ChatMessageNotificationDto.Action.UPDATE, message));
    }

    public void broadcastDelete(ChatMessageResponseDto message) {
        UUID chatContentId = message.getChatContent().getId();
        ChatMessageResponseDto dummy = new ChatMessageResponseDto();
        dummy.setId(message.getId());
        messagingTemplate.convertAndSend("/chatContent/" + chatContentId + "/messages",
                new ChatMessageNotificationDto(ChatMessageNotificationDto.Action.DELETE, dummy));
    }
}
