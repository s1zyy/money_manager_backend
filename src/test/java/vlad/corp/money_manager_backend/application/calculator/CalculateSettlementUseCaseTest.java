package vlad.corp.money_manager_backend.application.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CalculateSettlementUseCaseTest {

    private CalculateSettlementUseCase useCase;

    private static final UUID ALICE = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID BOB   = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @BeforeEach
    void setUp() {
        useCase = new CalculateSettlementUseCase();
    }

    @Test
    void oneCreditorOneDebtor_singleTransfer() {
        // Alice overpaid by €30, Bob owes €30
        Map<UUID, Money> balances = Map.of(
                ALICE, new Money(new BigDecimal("30.00")),
                BOB,   new Money(new BigDecimal("-30.00"))
        );

        List<SettlementTransfer> transfers = useCase.execute(balances);

        // should be exactly one transfer to settle everything
        assertThat(transfers).hasSize(1);

        SettlementTransfer transfer = transfers.getFirst();

        // money goes from the debtor to the creditor
        assertThat(transfer.fromId()).isEqualTo(BOB);
        assertThat(transfer.toId()).isEqualTo(ALICE);

        // amount must match exactly
        assertThat(transfer.amount().amount())
                .isEqualByComparingTo(new BigDecimal("30.00"));
    }

    @Test
    void allBalancesZero_noTransfers() {
        Map<UUID, Money> balances = Map.of(
                ALICE, new Money(new BigDecimal("0.00")),
                BOB,   new Money(new BigDecimal("0.00"))
        );

        List<SettlementTransfer> transfers = useCase.execute(balances);

        // no transfers should be needed
        assertThat(transfers).isEmpty();
    }

    @Test
    void oneCreditor_twoDebtors_twoTransfers() {
        // Alice overpaid by €30, Bob owes €20, Charlie owes €10
        UUID CHARLIE = UUID.fromString("00000000-0000-0000-0000-000000000003");
        Map<UUID, Money> balances = Map.of(
                ALICE, new Money(new BigDecimal("30.00")),
                BOB,   new Money(new BigDecimal("-20.00")),
                CHARLIE, new Money(new BigDecimal("-10.00"))
        );

        List<SettlementTransfer> transfers = useCase.execute(balances);

        // should be exactly two transfers to settle everything
        assertThat(transfers).hasSize(2);

        // check that the total amount transferred equals the total owed
        BigDecimal totalTransferred = transfers.stream()
                .map(t -> t.amount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertThat(totalTransferred).isEqualByComparingTo(new BigDecimal("30.00"));
    }

    @Test
    void twoCreditors_oneDebtor_twoTransfers() {
        // Alice overpaid by €20, Bob overpaid by €10, Charlie owes €30
        UUID CHARLIE = UUID.fromString("00000000-0000-0000-0000-000000000003");
        Map<UUID, Money> balances = Map.of(
                ALICE, new Money(new BigDecimal("20.00")),
                BOB,   new Money(new BigDecimal("10.00")),
                CHARLIE, new Money(new BigDecimal("-30.00"))
        );

        List<SettlementTransfer> transfers = useCase.execute(balances);

        // should be exactly two transfers to settle everything
        assertThat(transfers).hasSize(2);

        // check that the total amount transferred equals the total owed
        BigDecimal totalTransferred = transfers.stream()
                .map(t -> t.amount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertThat(totalTransferred).isEqualByComparingTo(new BigDecimal("30.00"));
    }


    @Test
    void tenParticipants_transferCountIsAtMostEight(){
        UUID FRIEND1 = UUID.fromString("00000000-0000-0000-0000-000000000003");
        UUID FRIEND2 = UUID.fromString("00000000-0000-0000-0000-000000000004");
        UUID FRIEND3 = UUID.fromString("00000000-0000-0000-0000-000000000005");
        UUID FRIEND4 = UUID.fromString("00000000-0000-0000-0000-000000000006");
        UUID FRIEND5 = UUID.fromString("00000000-0000-0000-0000-000000000007");
        UUID FRIEND6 = UUID.fromString("00000000-0000-0000-0000-000000000008");
        UUID FRIEND7 = UUID.fromString("00000000-0000-0000-0000-000000000009");
        UUID FRIEND8 = UUID.fromString("00000000-0000-0000-0000-000000000010");

        Map<UUID, Money> balances = Map.of(
                ALICE, new Money(new BigDecimal("100.00")),
                BOB,   new Money(new BigDecimal("-50.00")),
                FRIEND1, new Money(new BigDecimal("-30.00")),
                FRIEND2, new Money(new BigDecimal("45.00")),
                FRIEND3, new Money(new BigDecimal("-70.00")),
                FRIEND4, new Money(new BigDecimal("-10.00")),
                FRIEND5, new Money(new BigDecimal("+15.00")),
                FRIEND6, new Money(new BigDecimal("-50.00")),
                FRIEND7, new Money(new BigDecimal("+50.00")),
                FRIEND8, new Money(new BigDecimal("00.00"))
        );

        List<SettlementTransfer> transfers = useCase.execute(balances);

        assertThat(transfers).hasSizeLessThanOrEqualTo(8);

        BigDecimal totalTransferred = transfers.stream()
                .map(t -> t.amount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertThat(totalTransferred).isEqualByComparingTo(new BigDecimal("210.00"));




    }

    @Test
    void sumOfAllTransfers_equalsSum_ofAllDebts() {
        UUID CHARLIE = UUID.fromString("00000000-0000-0000-0000-000000000003");
        Map<UUID, Money> balances = Map.of(
                ALICE, new Money(new BigDecimal("50.00")),
                BOB,   new Money(new BigDecimal("40.00")),
                CHARLIE, new Money(new BigDecimal("-90.00"))
        );

        List<SettlementTransfer> transfers = useCase.execute(balances);

        BigDecimal totalTransferred = transfers.stream()
                .map(t -> t.amount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDebt = balances.values().stream()
                .filter(m -> m.amount().compareTo(BigDecimal.ZERO) < 0)
                .map(m -> m.amount().abs())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertThat(totalTransferred).isEqualByComparingTo(totalDebt);
    }
}
