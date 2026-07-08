package vlad.corp.money_manager_backend.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record VirtualParticipantInvite(
        String token,
        UUID virtualParticipantId,
        UUID tripId,
        String invitedEmail,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
) {}
