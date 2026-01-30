package vlad.corp.money_manager_backend.infrastructure.security;

import java.util.List;
import java.util.UUID;

public record JwtPayload(
        UUID userId,
        String email,
        List<String> roles
) {}
