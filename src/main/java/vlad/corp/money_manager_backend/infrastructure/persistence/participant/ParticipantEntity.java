package vlad.corp.money_manager_backend.infrastructure.persistence.participant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantEntity {

    @Id
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "is_virtual", nullable = false)
    private boolean isVirtual;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "avatar_url")
    private String avatarUrl;
}
