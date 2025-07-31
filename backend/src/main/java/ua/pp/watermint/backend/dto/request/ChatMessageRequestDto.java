package ua.pp.watermint.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class ChatMessageRequestDto {
    @NotBlank
    @Size(max = 4096)
    private String text;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID chatId;
}
