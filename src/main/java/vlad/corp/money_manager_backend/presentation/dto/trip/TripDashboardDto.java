package vlad.corp.money_manager_backend.presentation.dto.trip;

import vlad.corp.money_manager_backend.presentation.dto.expense.ExpenseDto;

import java.util.List;

public record TripDashboardDto(
        TripDto trip,
        MyStatsDto myStats,
        List<ParticipantBalanceDto> participants,
        List<ExpenseDto> expenseDtoList,
        boolean isOwner,
        boolean canLeave
) {}
