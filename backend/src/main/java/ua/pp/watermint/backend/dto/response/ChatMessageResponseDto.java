package ua.pp.watermint.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.pp.watermint.backend.entity.ChatContent;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {
    private UUID id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean wasUpdated;

    private String text;

    private UserResponseDto user;

    private ChatContent chatContent;
}
