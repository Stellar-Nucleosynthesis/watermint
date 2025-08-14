package ua.pp.watermint.backend.entity;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
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

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_account_1_id", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserAccount userAccount1;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_account_2_id", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserAccount userAccount2;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_content_id", updatable = false, nullable = false)
    private ChatContent chatContent;

    @AssertTrue(message = "Users in a chat must be different")
    public boolean isDifferentUsers() {
        return userAccount1 != null && userAccount2 != null && !userAccount1.equals(userAccount2);
    }
}
