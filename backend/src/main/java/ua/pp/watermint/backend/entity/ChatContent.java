package ua.pp.watermint.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatContent {
    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(nullable = false, unique = true, updatable = false,
            length = 36, columnDefinition="varchar(36)")
    private UUID id;

    @Version
    private Integer version;
}
