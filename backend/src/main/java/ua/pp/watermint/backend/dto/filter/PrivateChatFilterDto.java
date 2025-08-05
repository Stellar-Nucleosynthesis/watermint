package ua.pp.watermint.backend.dto.filter;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PrivateChatFilterDto {
    @NotNull
    private UUID userAccount1Id;
    private String userAccount2Name;
}
