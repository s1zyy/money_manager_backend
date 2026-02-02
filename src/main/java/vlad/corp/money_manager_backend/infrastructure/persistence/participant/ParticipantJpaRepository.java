package vlad.corp.money_manager_backend.infrastructure.persistence.participant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParticipantJpaRepository extends JpaRepository<ParticipantEntity, UUID> {
}
