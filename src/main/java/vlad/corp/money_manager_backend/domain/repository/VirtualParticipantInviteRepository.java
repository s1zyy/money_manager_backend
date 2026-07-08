package vlad.corp.money_manager_backend.domain.repository;

import vlad.corp.money_manager_backend.domain.model.VirtualParticipantInvite;

import java.util.Optional;

public interface VirtualParticipantInviteRepository {
    void save(VirtualParticipantInvite invite);
    Optional<VirtualParticipantInvite> findByToken(String token);
    void deleteByToken(String token);
}
