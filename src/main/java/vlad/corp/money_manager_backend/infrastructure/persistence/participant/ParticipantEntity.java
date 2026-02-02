package vlad.corp.money_manager_backend.infrastructure.persistence.participant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class ParticipantEntity {

    @Id
    private UUID id;
}
