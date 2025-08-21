package ua.pp.watermint.backend.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.pp.watermint.backend.dto.response.ChatMessageResponseDto;

@Data
@AllArgsConstructor
public class ChatMessageNotificationDto {
    private Action action;
    private ChatMessageResponseDto message;

    public enum Action {
        CREATE, UPDATE, DELETE
    }
}