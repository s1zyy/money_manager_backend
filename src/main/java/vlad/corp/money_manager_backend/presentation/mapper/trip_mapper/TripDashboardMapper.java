package vlad.corp.money_manager_backend.presentation.mapper.trip_mapper;

import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.application.trip.TripDashboard;
import vlad.corp.money_manager_backend.presentation.dto.trip.ParticipantBalanceDto;
import vlad.corp.money_manager_backend.presentation.dto.trip.TripDashboardDto;
import vlad.corp.money_manager_backend.presentation.mapper.expense_mapper.ExpenseMapperDto;

@Component
public class TripDashboardMapper {
    private final TripMapperDto tripMapperDto;
    private final ExpenseMapperDto expenseMapperDto;

    public TripDashboardMapper(TripMapperDto tripMapperDto, ExpenseMapperDto expenseMapperDto) {
        this.tripMapperDto = tripMapperDto;
        this.expenseMapperDto = expenseMapperDto;
    }

    public TripDashboardDto toDto(TripDashboard dashboard) {
        return new TripDashboardDto(
                tripMapperDto.toDto(dashboard.trip()),
                dashboard.dailyLimit().amount(),
                dashboard.balances().entrySet().stream()
                        .map(e -> new ParticipantBalanceDto(e.getKey(), e.getValue().getAmount()))
                        .toList(),
                dashboard.expenses().stream()
                        .map(expenseMapperDto::toDto)
                        .toList(),
                dashboard.isOwner(),
                dashboard.canLeave()
        );

    }
}
