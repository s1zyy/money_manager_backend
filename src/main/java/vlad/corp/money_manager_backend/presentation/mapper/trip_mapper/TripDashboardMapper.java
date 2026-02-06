package vlad.corp.money_manager_backend.presentation.mapper.trip_mapper;

import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.application.trip.TripDashboard;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.presentation.dto.expense.ExpenseDto;
import vlad.corp.money_manager_backend.presentation.dto.trip.ParticipantBalanceDto;
import vlad.corp.money_manager_backend.presentation.dto.trip.TripDashboardDto;

@Component
public class TripDashboardMapper {
    private final TripMapperDto tripMapperDto;

    public TripDashboardMapper(TripMapperDto tripMapperDto) {
        this.tripMapperDto = tripMapperDto;
    }

    public TripDashboardDto toDto(TripDashboard dashboard) {
        return new TripDashboardDto(
                tripMapperDto.toDto(dashboard.trip()),
                dashboard.dailyLimit().amount(),
                dashboard.balances().entrySet().stream()
                        .map(e -> new ParticipantBalanceDto(e.getKey(), e.getValue().getAmount()))
                        .toList(),
                dashboard.expenses().stream()
                        .map(this::toExpenseDto)
                        .toList()
        );

    }
    private ExpenseDto toExpenseDto(Expense expense) {
        return new ExpenseDto(
                expense.getId(),
                expense.getPayerId(),
                expense.getAmount().getAmount(),
                expense.getDate(),
                expense.getDescription(),
                expense.getParticipantIds()
        );
    }
}
