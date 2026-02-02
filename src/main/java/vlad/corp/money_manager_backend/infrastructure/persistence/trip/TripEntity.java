package vlad.corp.money_manager_backend.infrastructure.persistence.trip;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class TripEntity {

    @Id
    private UUID id;


}
