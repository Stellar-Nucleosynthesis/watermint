package ua.pp.watermint.backend.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountRequestDto {
    @NotBlank
    @Size(max = 254)
    @Email
    private String email;

    @NotBlank
    @Size(max = 24)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Size(max = 100)
    private String name;

    private LocalDate birthDate;

    @Lob
    private byte[] profilePicture;
}
