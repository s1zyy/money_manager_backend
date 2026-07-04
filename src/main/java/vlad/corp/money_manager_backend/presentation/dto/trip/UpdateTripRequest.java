package vlad.corp.money_manager_backend.presentation.dto.trip;

import java.time.LocalDate;

public record UpdateTripRequest(
        String name,
        LocalDate startDate,
        LocalDate endDate,
        String currency
) {}
