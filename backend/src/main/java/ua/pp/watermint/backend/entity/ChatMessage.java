package ua.pp.watermint.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
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

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createTime;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant updateTime;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserAccount userAccount;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "chat_content_id", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChatContent chatContent;
}
