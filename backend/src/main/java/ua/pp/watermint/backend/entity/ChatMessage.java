package ua.pp.watermint.backend.entity;

import jakarta.persistence.*;
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

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
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

    @NotNull
    @Column(nullable = false)
    private Boolean wasUpdated;

    @NotBlank
    @Size(max = 4096)
    @Column(nullable = false, length = 4096)
    private String text;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_account_id", updatable = false, nullable = false)
    private UserAccount userAccount;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "chat_content_id", updatable = false, nullable = false)
    private ChatContent chatContent;
}
