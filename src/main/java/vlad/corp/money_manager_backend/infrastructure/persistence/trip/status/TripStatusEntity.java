package vlad.corp.money_manager_backend.infrastructure.persistence.trip.status;

import jakarta.persistence.*;
import lombok.*;
import vlad.corp.money_manager_backend.domain.model.TripStatus;

@Entity
@Table(name = "trip_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private TripStatus code;
}
