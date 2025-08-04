package ua.pp.watermint.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {
    @NotBlank
    @Size(max = 4096)
    private String text;

    @NotNull
    private UUID userAccountId;

    @NotNull
    private UUID chatContentId;
}
