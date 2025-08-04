package ua.pp.watermint.backend.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
