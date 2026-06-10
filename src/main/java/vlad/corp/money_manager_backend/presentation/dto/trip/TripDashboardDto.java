package vlad.corp.money_manager_backend.presentation.dto.trip;

import vlad.corp.money_manager_backend.presentation.dto.expense.ExpenseDto;

import java.math.BigDecimal;
import java.util.List;

public record   TripDashboardDto (
    TripDto trip,
    BigDecimal dailyLimit,
    List<ParticipantBalanceDto> participants,
    List<ExpenseDto> expenseDtoList
){}
