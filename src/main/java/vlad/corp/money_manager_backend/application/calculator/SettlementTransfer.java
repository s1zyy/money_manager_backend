package vlad.corp.money_manager_backend.application.calculator;

import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.util.UUID;

public record SettlementTransfer(
        UUID fromId,
        UUID toId,
        Money amount
) {}
