package vlad.corp.money_manager_backend.application.invite;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.exceptions.BusinessException;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.model.VirtualParticipantInvite;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.repository.VirtualParticipantInviteRepository;

import java.time.LocalDateTime;

public class ValidateInviteTokenUseCase {

    private final VirtualParticipantInviteRepository inviteRepository;
    private final TripRepository tripRepository;
    private final ParticipantRepository participantRepository;

    public ValidateInviteTokenUseCase(VirtualParticipantInviteRepository inviteRepository,
                                      TripRepository tripRepository,
                                      ParticipantRepository participantRepository) {
        this.inviteRepository = inviteRepository;
        this.tripRepository = tripRepository;
        this.participantRepository = participantRepository;
    }

    public InviteTokenInfo execute(String token) {
        VirtualParticipantInvite invite = inviteRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Invite not found"));

        if (invite.expiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Invite has expired");
        }

        Trip trip = tripRepository.findById(invite.tripId())
                .orElseThrow(() -> new NotFoundException("Trip not found"));

        Participant participant = participantRepository.findById(invite.virtualParticipantId())
                .orElseThrow(() -> new NotFoundException("Participant not found"));

        boolean requiresLogin = participantRepository.findByEmail(invite.invitedEmail())
                .map(real -> {
                    if (trip.getParticipantBudgets().containsKey(real.getId())) {
                        throw new BusinessException("You are already a participant in this trip");
                    }
                    return true;
                })
                .orElse(false);

        return new InviteTokenInfo(trip.getName(), participant.getName(), invite.invitedEmail(), requiresLogin);
    }
}
