package ua.pp.watermint.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private UserAccountResponseDto userAccount;

    private ChatContentResponseDto chatContent;
}
