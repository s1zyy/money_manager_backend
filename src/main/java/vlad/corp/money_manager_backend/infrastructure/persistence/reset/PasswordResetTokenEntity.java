package vlad.corp.money_manager_backend.infrastructure.persistence.reset;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_reset_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetTokenEntity {

    @Id
    @Column(length = 8)
    private String token;

    @Column(name = "participant_id", nullable = false)
    private UUID participantId;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}
