package vlad.corp.money_manager_backend.infrastructure.security;

import java.util.UUID;

public record AuthenticatedUser(
        UUID userId,
        String email
) {
}
