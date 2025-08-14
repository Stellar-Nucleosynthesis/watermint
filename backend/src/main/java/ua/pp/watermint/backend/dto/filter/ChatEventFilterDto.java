package ua.pp.watermint.backend.dto.filter;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ChatEventFilterDto {
    @NotNull
    private UUID chatContentId;
    private String text;
    private Instant from;
    private Instant to;
}
