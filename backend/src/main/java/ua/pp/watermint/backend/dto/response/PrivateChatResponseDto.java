package ua.pp.watermint.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrivateChatResponseDto {
    private UUID id;

    private LocalDateTime createTime;

    private UserAccountResponseDto userAccount1;

    private UserAccountResponseDto userAccount2;

    private ChatContentResponseDto chatContent;
}
