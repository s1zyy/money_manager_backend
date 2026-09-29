package vlad.corp.money_manager_backend.application.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.SplitMode;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.model.TripStatus;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CalculateBalancesUseCaseTest {

    private CalculateBalancesUseCase useCase;

    // fixed UUIDs so it's clear who is who in each test
    private static final UUID ALICE = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID BOB = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @BeforeEach
    void setUp() {
        useCase = new CalculateBalancesUseCase();
    }

    // ─── helpers ─────────────────────────────────────────────────────────────

    private Trip tripWith(UUID... participantIds) {
        Map<UUID, Money> budgets = new HashMap<>();
        for (UUID id : participantIds) {
            budgets.put(id, Money.zero());
        }
        return new Trip(
                UUID.randomUUID(),
                participantIds[0],          // first participant is the owner
                "Test Trip",
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(5),
                budgets,
                JoinCode.of("12345678"),
                TripStatus.ACTIVE,
                "EUR"
        );
    }

    private Expense equalExpense(UUID payerId, Money amount, UUID... participantIds) {
        Map<UUID, BigDecimal> shares = new HashMap<>();
        for (UUID id : participantIds) {
            shares.put(id, null); // null means equal split (legacy format)
        }
        return new Expense(
                UUID.randomUUID(),
                UUID.randomUUID(),
                amount,
                payerId,
                SplitMode.EQUAL,
                shares,
                LocalDate.now().minusDays(1),
                "Test expense",
                false
        );
    }

    private Expense customExpense(UUID payerId, Money amount, Map<UUID, BigDecimal> shares) {

        return new Expense(
                UUID.randomUUID(),
                UUID.randomUUID(),
                amount,
                payerId,
                SplitMode.CUSTOM,
                shares,
                LocalDate.now().minusDays(1),
                "Test expense",
                false
        );
    }

    // ─── tests ───────────────────────────────────────────────────────────────

    @Test
    void twoParticipants_equalSplit_correctBalances() {
        // Alice pays €60, split equally between Alice and Bob (€30 each)
        Trip trip = tripWith(ALICE, BOB);
        Expense expense = equalExpense(ALICE, Money.of(new BigDecimal("60.00")), ALICE, BOB);

        Map<UUID, Money> balances = useCase.execute(trip, List.of(expense));

        // Alice paid €60 but owes €30 herself, so her net balance is +€30
        assertThat(balances.get(ALICE).amount())
                .isEqualByComparingTo(new BigDecimal("30.00"));

        // Bob paid nothing but owes €30, so his balance is -€30
        assertThat(balances.get(BOB).amount())
                .isEqualByComparingTo(new BigDecimal("-30.00"));
    }

    @Test
    void threeParticipants_equalSplit_100euros_noLostCent() {
        UUID VERA = UUID.fromString("00000000-0000-0000-0000-000000000003");

        Trip trip = tripWith(ALICE, BOB, VERA);
        Expense expense = equalExpense(ALICE, Money.of(new BigDecimal("100.00")), ALICE, BOB, VERA);

        Map<UUID, Money> balances = useCase.execute(trip, List.of(expense));

        // all balances must sum to exactly zero — if rounding is wrong
        // (100 / 3 = 33.33 * 3 = 99.99) this will catch the missing cent
        BigDecimal sum = balances.values().stream()
                .map(Money::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertThat(sum).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void customSplit_unequalShares_correctBalances() {
        Trip trip = tripWith(ALICE, BOB);
        Map<UUID, BigDecimal> shares = new HashMap<>();
        shares.put(ALICE, new BigDecimal("20.00"));
        shares.put(BOB, new BigDecimal("40.00"));
        Expense expense = customExpense(ALICE, Money.of(new BigDecimal("60.00")), shares);

        Map<UUID, Money> balances = useCase.execute(trip, List.of(expense));

        // Alice paid €60 but owes €20 herself, so her net balance is +€40
        assertThat(balances.get(ALICE).amount())
                .isEqualByComparingTo(new BigDecimal("40.00"));

        // Bob paid nothing but owes €40, so his balance is -€40
        assertThat(balances.get(BOB).amount())
                .isEqualByComparingTo(new BigDecimal("-40.00"));
    }

    @Test
    void participant_whoNeverPaid_hasNegativeBalance() {
        Trip trip = tripWith(ALICE, BOB);
        Expense expense = equalExpense(ALICE, Money.of(new BigDecimal("50.00")), ALICE, BOB);

        Map<UUID, Money> balances = useCase.execute(trip, List.of(expense));

        // Bob never paid but owes €25, so his balance is -€25
        assertThat(balances.get(BOB).amount())
                .isEqualByComparingTo(new BigDecimal("-25.00"));

        assertThat(balances.get(ALICE).amount())
                .isEqualByComparingTo(new BigDecimal("25.00"));
    }

    @Test
    void expense_withNullPayerId_isIgnored() {
        Trip trip = tripWith(ALICE, BOB);
        HashMap<UUID, BigDecimal> shares = new HashMap<>();
        shares.put(ALICE, null);
        shares.put(BOB, null);
        Expense expense = new Expense(
                UUID.randomUUID(),
                UUID.randomUUID(),
                Money.of(new BigDecimal("50.00")),
                null, // no payer
                SplitMode.EQUAL,
                shares,
                LocalDate.now().minusDays(1),
                "Test expense",
                false
        );

        Map<UUID, Money> balances = useCase.execute(trip, List.of(expense));

        // both participants should have zero balance since the expense is ignored
        assertThat(balances.get(ALICE).amount())
                .isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(balances.get(BOB).amount())
                .isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void multipleExpenses_balancesAccumulate() {
        Trip trip = tripWith(ALICE, BOB);
        Expense expense1 = equalExpense(ALICE, Money.of(new BigDecimal("60.00")), ALICE, BOB);
        Expense expense2 = equalExpense(BOB, Money.of(new BigDecimal("40.00")), ALICE, BOB);

        Map<UUID, Money> balances = useCase.execute(trip, List.of(expense1, expense2));

        // Alice paid €60 and owes €30 from the first expense and €20 from the second
        // Total owed by Alice: €50, so her net balance is 60 - 50 = +€10
        assertThat(balances.get(ALICE).amount())
                .isEqualByComparingTo(new BigDecimal("10.00"));

        // Bob paid €40 and owes €30 from the first expense and €20 from the second
        // Total owed by Bob: €50, so his net balance is 40 - 50 = -€10
        assertThat(balances.get(BOB).amount())
                .isEqualByComparingTo(new BigDecimal("-10.00"));
    }

    @Test
    void participant_notInAnyExpense_hasZeroBalance() {
        UUID CHARLIE = UUID.fromString("00000000-0000-0000-0000-000000000003");
        Trip trip = tripWith(ALICE, BOB, CHARLIE);
        Expense expense = equalExpense(ALICE, Money.of(new BigDecimal("60.00")), ALICE, BOB);

        Map<UUID, Money> balances = useCase.execute(trip, List.of(expense));

        // Charlie was not part of any expense, so his balance should be zero
        assertThat(balances.get(CHARLIE).amount())
                .isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void emptyExpenseList_allBalancesZero() {
        Trip trip = tripWith(ALICE, BOB);

        Map<UUID, Money> balances = useCase.execute(trip, List.of());

        assertThat(balances.get(ALICE).amount())
                .isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(balances.get(BOB).amount())
                .isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void emptyGroup_returnsEmptyMap() {
        Trip trip = new Trip (
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Empty Trip",
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(5),
                new HashMap<>(),
                JoinCode.of("00000000"),
                TripStatus.ACTIVE,
                "EUR"
        ); // no participants

        Map<UUID, Money> balances = useCase.execute(trip, List.of());

        assertThat(balances).isEmpty();
    }
}
