package ua.pp.watermint.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountResponseDto {
    private UUID id;

    private Instant createTime;

    private Instant updateTime;

    private String email;

    private Boolean verified;

    private String username;

    private String name;

    private LocalDate birthDate;

    private byte[] profilePicture;
}
