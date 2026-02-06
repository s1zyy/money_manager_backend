package vlad.corp.money_manager_backend.presentation.dto.trip;

import java.math.BigDecimal;
import java.util.UUID;

public record ParticipantBalanceDto(
        UUID participantId,
        BigDecimal balance
) {
}
