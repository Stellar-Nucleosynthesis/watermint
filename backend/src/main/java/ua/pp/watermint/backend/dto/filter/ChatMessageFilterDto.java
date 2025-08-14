package ua.pp.watermint.backend.dto.filter;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ChatMessageFilterDto {
    @NotNull
    private UUID chatContentId;
    private UUID userAccountId;
    private String text;
    private Instant from;
    private Instant to;
}
