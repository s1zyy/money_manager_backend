package vlad.corp.money_manager_backend.infrastructure.persistence.expense;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseEntity {

    @Id
    private UUID id;

    @Column(nullable = false, name = "trip_id")
    private UUID tripId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "payer_id")
    private UUID payerId;

    @Column(name = "split_mode", nullable = false)
    private String splitMode;

    @ElementCollection
    @CollectionTable(
            name = "expense_participants",
            joinColumns = @JoinColumn(name = "expense_id")
    )
    @MapKeyColumn(name = "participant_id")
    @Column(name = "amount", nullable = true)
    private Map<UUID, BigDecimal> participantShares;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "description")
    private String description;

    @Column(name = "is_prepaid", nullable = false)
    private boolean isPrepaid;
}
