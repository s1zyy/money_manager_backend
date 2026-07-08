package vlad.corp.money_manager_backend.infrastructure.persistence.invite;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VirtualParticipantInviteJpaRepository extends JpaRepository<VirtualParticipantInviteEntity, String> {
}
