package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record TripDashboard(
        Trip trip,
        Money dailyLimit,
        Map<UUID, Money> balances,
        List<Expense> expenses
) {}
