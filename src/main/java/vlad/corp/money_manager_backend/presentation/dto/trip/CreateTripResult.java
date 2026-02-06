package vlad.corp.money_manager_backend.presentation.dto.trip;

import java.time.LocalDate;
import java.util.UUID;

public record CreateTripResult(
        UUID tripId,
        UUID ownerId,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        String  joinCode
) {}
