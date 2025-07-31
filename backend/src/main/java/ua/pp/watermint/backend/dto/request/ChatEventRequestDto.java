package ua.pp.watermint.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class ChatEventRequestDto {
    @NotBlank
    @Size(max = 255)
    private String text;

    @NotNull
    private UUID chatId;
}
