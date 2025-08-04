package ua.pp.watermint.backend.dto.filter;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class PrivateChatFilterDto {
    @NotBlank
    private UUID userAccount1Id;
    private String userAccount2Name;
}
