package vlad.corp.money_manager_backend.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record PasswordResetToken(
        String token,
        UUID participantId,
        LocalDateTime expiresAt
) {}
