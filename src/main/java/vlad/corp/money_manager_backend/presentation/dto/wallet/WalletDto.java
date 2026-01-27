package vlad.corp.money_manager_backend.presentation.dto.wallet;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record WalletDto(
        UUID walletId,
        UUID ownerId,
        String name,
        String joinCode,
        String currencyCode,
        Set<UUID> members,
        LocalDateTime createdAt

) {}