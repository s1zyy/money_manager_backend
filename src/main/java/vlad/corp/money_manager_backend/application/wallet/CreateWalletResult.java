package vlad.corp.money_manager_backend.application.wallet;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record CreateWalletResult(
        UUID walletId,
        UUID ownerId,
        String name,
        String joinCode,
        Set<UUID> members,
        LocalDateTime createdAt

) {
}
