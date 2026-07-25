package vlad.corp.money_manager_backend.domain.repository;

import vlad.corp.money_manager_backend.domain.model.VirtualParticipantInvite;

import java.util.Optional;
import java.util.UUID;

public interface VirtualParticipantInviteRepository {
    void save(VirtualParticipantInvite invite);
    Optional<VirtualParticipantInvite> findByToken(String token);
    Optional<VirtualParticipantInvite> findByVirtualParticipantId(UUID virtualParticipantId);
    void deleteByToken(String token);
}
