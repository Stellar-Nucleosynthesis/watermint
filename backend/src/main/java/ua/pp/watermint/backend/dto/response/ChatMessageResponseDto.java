package ua.pp.watermint.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {
    private UUID id;

    private Instant createTime;

    private Instant updateTime;

    private Boolean wasUpdated;

    private String text;

    private UserAccountResponseDto userAccount;

    private ChatContentResponseDto chatContent;
}
