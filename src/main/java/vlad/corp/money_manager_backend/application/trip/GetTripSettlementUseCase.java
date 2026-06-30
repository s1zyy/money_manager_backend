package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.calculator.CalculateBalancesUseCase;
import vlad.corp.money_manager_backend.application.calculator.CalculateSettlementUseCase;
import vlad.corp.money_manager_backend.application.calculator.SettlementTransfer;
import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class GetTripSettlementUseCase {

    private final TripRepository tripRepository;
    private final ExpenseRepository expenseRepository;
    private final ParticipantRepository participantRepository;
    private final CalculateBalancesUseCase calculateBalancesUseCase;
    private final CalculateSettlementUseCase calculateSettlementUseCase;

    public GetTripSettlementUseCase(TripRepository tripRepository, ExpenseRepository expenseRepository,
                                    ParticipantRepository participantRepository,
                                    CalculateBalancesUseCase calculateBalancesUseCase,
                                    CalculateSettlementUseCase calculateSettlementUseCase) {
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
        this.participantRepository = participantRepository;
        this.calculateBalancesUseCase = calculateBalancesUseCase;
        this.calculateSettlementUseCase = calculateSettlementUseCase;
    }

    public TripSettlement execute(UUID tripId, UUID participantId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found"));

        trip.ensureParticipant(participantId);

        List<Expense> expenses = expenseRepository.findAllByTripId(tripId);
        Map<UUID, Money> balances = calculateBalancesUseCase.execute(trip, expenses);
        List<SettlementTransfer> transfers = calculateSettlementUseCase.execute(balances);

        Set<UUID> allIds = trip.getParticipantIds();
        Map<UUID, String> names = participantRepository.findAllByIds(allIds).stream()
                .collect(Collectors.toMap(Participant::getId, Participant::getName));

        return new TripSettlement(transfers, names);
    }
}
