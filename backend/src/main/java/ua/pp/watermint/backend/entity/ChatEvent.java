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

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatEvent {
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
    private LocalDateTime createTime;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String text;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "chat_content_id", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChatContent chatContent;
}
