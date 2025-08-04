package ua.pp.watermint.backend.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PrivateChatRequestDto {
    @NotNull
    private UUID userAccount1Id;

    @NotNull
    private UUID userAccount2Id;

    @AssertTrue(message = "Users in a chat must be different")
    public boolean isDifferentUsers() {
        return userAccount1Id != null && userAccount2Id != null && !userAccount1Id.equals(userAccount2Id);
    }
}
