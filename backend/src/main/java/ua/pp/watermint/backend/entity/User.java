package ua.pp.watermint.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(nullable = false, unique = true, updatable = false,
            length = 36, columnDefinition="varchar(36)")
    private UUID id;

    @Version
    private Integer version;

    @NotNull
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    @NotNull
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updateTime;

    @NotBlank
    @Size(max = 254)
    @Email
    @Column(nullable = false, length = 254, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false)
    private Boolean verified;

    @Size(max = 24)
    @Column(length = 24, unique = true)
    private String username;

    @NotBlank
    @Size(max = 60)
    @Column(nullable = false, length = 60)
    private String password;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    private LocalDate birthDate;

    @Lob
    private byte[] profilePicture;
}
