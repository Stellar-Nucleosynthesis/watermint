package ua.pp.watermint.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PrivateChatRequestDto {
    @NotNull
    private UUID user_1_id;

    @NotNull
    private UUID user_2_id;
}
