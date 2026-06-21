package vlad.corp.money_manager_backend.infrastructure.persistence.trip;

import jakarta.persistence.*;
import lombok.*;
import vlad.corp.money_manager_backend.infrastructure.persistence.trip.status.TripStatusEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripEntity {

    @Id
    private UUID id;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_budget")
    private BigDecimal totalBudget;

    @Column(name = "prepaid_expenses")
    private BigDecimal prepaidExpenses;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;





    @ElementCollection
    @CollectionTable(
            name = "trip_participants",
            joinColumns = @JoinColumn(name = "trip_id"))
    @Column(name = "participant_id", nullable = false)
    private Set<UUID> participantIds;

    @Column(name = "join_code", nullable = false, unique = true, length = 8)
    private String joinCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private TripStatusEntity tripStatus;


}
