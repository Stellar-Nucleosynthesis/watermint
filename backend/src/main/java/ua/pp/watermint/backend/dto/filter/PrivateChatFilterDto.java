package ua.pp.watermint.backend.dto.filter;

import lombok.Data;

import java.util.UUID;

@Data
public class PrivateChatFilterDto {
    private UUID userAccountId;
    private String sortBy;
    private String sortOrder;
}
