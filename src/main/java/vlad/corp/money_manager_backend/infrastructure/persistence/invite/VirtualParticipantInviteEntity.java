package vlad.corp.money_manager_backend.infrastructure.persistence.invite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "virtual_participant_invites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VirtualParticipantInviteEntity {

    @Id
    @Column(length = 8)
    private String token;

    @Column(name = "virtual_participant_id", nullable = false)
    private UUID virtualParticipantId;

    @Column(name = "trip_id", nullable = false)
    private UUID tripId;

    @Column(name = "invited_email", nullable = false)
    private String invitedEmail;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}
