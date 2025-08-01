package ua.pp.watermint.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PrivateChatRequestDto {
    @NotNull
    private UUID userAccount1Id;

    @NotNull
    private UUID userAccount2Id;
}
