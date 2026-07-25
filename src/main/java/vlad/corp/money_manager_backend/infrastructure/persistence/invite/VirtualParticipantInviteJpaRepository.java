package vlad.corp.money_manager_backend.infrastructure.persistence.invite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VirtualParticipantInviteJpaRepository extends JpaRepository<VirtualParticipantInviteEntity, String> {
    Optional<VirtualParticipantInviteEntity> findByVirtualParticipantId(UUID virtualParticipantId);
}
