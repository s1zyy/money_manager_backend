package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.calculator.SettlementTransfer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record TripSettlement(
        List<SettlementTransfer> transfers,
        Map<UUID, String> participantNames
) {}
