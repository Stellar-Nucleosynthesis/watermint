package ua.pp.watermint.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrivateChat {
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
    @ManyToOne
    @JoinColumn(name = "user_account_1_id", updatable = false, nullable = false)
    private UserAccount userAccount1;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_account_2_id", updatable = false, nullable = false)
    private UserAccount userAccount2;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_content_id", updatable = false, nullable = false)
    private ChatContent chatContent;
}
