package ua.pp.watermint.backend.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserAccountRequestDto {
    @NotBlank
    @Size(max = 254)
    @Email
    private String email;

    @NotNull
    private Boolean verified;

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
