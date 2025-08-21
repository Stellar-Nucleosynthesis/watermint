package ua.pp.watermint.backend.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.pp.watermint.backend.dto.response.ChatEventResponseDto;

@Data
@AllArgsConstructor
public class ChatEventNotificationDto {
    private Action action;
    private ChatEventResponseDto event;

    public enum Action {
        CREATE
    }
}