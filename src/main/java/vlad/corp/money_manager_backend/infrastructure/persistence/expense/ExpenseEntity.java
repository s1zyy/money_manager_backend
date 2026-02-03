package vlad.corp.money_manager_backend.infrastructure.persistence.expense;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "expenses")
@Data
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

    @ElementCollection
    @CollectionTable(
            name = "expense_participants",
            joinColumns = @JoinColumn(name = "expense_id")
    )
    @Column(name = "participant_id", nullable = false)
    private Set<UUID> participantIds;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "description")
    private String description;

}
