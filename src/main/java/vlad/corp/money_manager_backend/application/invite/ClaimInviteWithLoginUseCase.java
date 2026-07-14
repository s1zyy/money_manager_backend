package vlad.corp.money_manager_backend.application.invite;

import org.springframework.security.crypto.password.PasswordEncoder;
import vlad.corp.money_manager_backend.application.auth.port.TokenGenerator;
import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.exceptions.BusinessException;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.model.VirtualParticipantInvite;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.repository.VirtualParticipantInviteRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ClaimInviteWithLoginUseCase {

    private final VirtualParticipantInviteRepository inviteRepository;
    private final ParticipantRepository participantRepository;
    private final TripRepository tripRepository;
    private final ExpenseRepository expenseRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    public ClaimInviteWithLoginUseCase(VirtualParticipantInviteRepository inviteRepository,
                                       ParticipantRepository participantRepository,
                                       TripRepository tripRepository,
                                       ExpenseRepository expenseRepository,
                                       PasswordEncoder passwordEncoder,
                                       TokenGenerator tokenGenerator) {
        this.inviteRepository = inviteRepository;
        this.participantRepository = participantRepository;
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
    }

    public String execute(String token, String password) {
        VirtualParticipantInvite invite = inviteRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Invite not found"));

        if (invite.expiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Invite has expired");
        }

        Participant real = participantRepository.findByEmail(invite.invitedEmail())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        if (!passwordEncoder.matches(password, real.getPasswordHash())) {
            throw new BusinessException("Invalid password");
        }

        Participant virtual = participantRepository.findById(invite.virtualParticipantId())
                .orElseThrow(() -> new NotFoundException("Virtual participant not found"));

        Trip trip = tripRepository.findById(invite.tripId())
                .orElseThrow(() -> new NotFoundException("Trip not found"));

        if (trip.getParticipantBudgets().containsKey(real.getId())) {
            throw new BusinessException("You are already a participant in this trip");
        }

        Money virtualBudget = trip.getParticipantBudgets().getOrDefault(virtual.getId(), Money.of(BigDecimal.ZERO));

        expenseRepository.reassignParticipant(trip.getId(), virtual.getId(), real.getId());

        trip.getParticipantBudgets().remove(virtual.getId());
        trip.addParticipant(real.getId(), virtualBudget);
        tripRepository.save(trip);

        participantRepository.deleteById(virtual.getId());
        inviteRepository.deleteByToken(token);

        return tokenGenerator.generateToken(real, List.of("ROLE_USER"));
    }
}
