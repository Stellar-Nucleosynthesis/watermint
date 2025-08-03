package ua.pp.watermint.backend.dto.filter;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChatEventFilterDto {
    @NotNull
    private UUID chatContentId;
    private String text;
    private LocalDateTime from;
    private LocalDateTime to;
}
