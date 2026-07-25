package vlad.corp.money_manager_backend.infrastructure.persistence.invite;

import org.springframework.stereotype.Repository;
import vlad.corp.money_manager_backend.domain.model.VirtualParticipantInvite;
import vlad.corp.money_manager_backend.domain.repository.VirtualParticipantInviteRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class VirtualParticipantInviteRepositoryImpl implements VirtualParticipantInviteRepository {

    private final VirtualParticipantInviteJpaRepository jpa;

    public VirtualParticipantInviteRepositoryImpl(VirtualParticipantInviteJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void save(VirtualParticipantInvite invite) {
        jpa.save(VirtualParticipantInviteEntity.builder()
                .token(invite.token())
                .virtualParticipantId(invite.virtualParticipantId())
                .tripId(invite.tripId())
                .invitedEmail(invite.invitedEmail())
                .createdAt(invite.createdAt())
                .expiresAt(invite.expiresAt())
                .build());
    }

    @Override
    public Optional<VirtualParticipantInvite> findByToken(String token) {
        return jpa.findById(token)
                .map(e -> new VirtualParticipantInvite(
                        e.getToken(),
                        e.getVirtualParticipantId(),
                        e.getTripId(),
                        e.getInvitedEmail(),
                        e.getCreatedAt(),
                        e.getExpiresAt()
                ));
    }

    @Override
    public Optional<VirtualParticipantInvite> findByVirtualParticipantId(UUID virtualParticipantId) {
        return jpa.findByVirtualParticipantId(virtualParticipantId)
                .map(e -> new VirtualParticipantInvite(
                        e.getToken(),
                        e.getVirtualParticipantId(),
                        e.getTripId(),
                        e.getInvitedEmail(),
                        e.getCreatedAt(),
                        e.getExpiresAt()
                ));
    }

    @Override
    public void deleteByToken(String token) {
        jpa.deleteById(token);
    }
}
