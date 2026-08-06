package vlad.corp.money_manager_backend.application.invite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vlad.corp.money_manager_backend.application.exceptions.NotFoundException;
import vlad.corp.money_manager_backend.application.port.EmailSender;
import vlad.corp.money_manager_backend.domain.exceptions.BusinessException;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.model.VirtualParticipantInvite;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.repository.VirtualParticipantInviteRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class InviteVirtualParticipantUseCase {

    private static final Logger log = LoggerFactory.getLogger(InviteVirtualParticipantUseCase.class);
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TOKEN_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final TripRepository tripRepository;
    private final ParticipantRepository participantRepository;
    private final VirtualParticipantInviteRepository inviteRepository;
    private final EmailSender emailSender;

    public InviteVirtualParticipantUseCase(TripRepository tripRepository,
                                           ParticipantRepository participantRepository,
                                           VirtualParticipantInviteRepository inviteRepository,
                                           EmailSender emailSender) {
        this.tripRepository = tripRepository;
        this.participantRepository = participantRepository;
        this.inviteRepository = inviteRepository;
        this.emailSender = emailSender;
    }

    public void execute(UUID tripId, UUID ownerId, UUID virtualParticipantId, String email, boolean force) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found"));

        trip.ensureNotArchived();
        trip.ensureOwner(ownerId);

        participantRepository.findByEmail(email).ifPresent(existing -> {
            if (trip.getParticipantBudgets().containsKey(existing.getId())) {
                throw new BusinessException("This user is already in the trip");
            }
            throw new BusinessException("Email already registered. Share the trip join code instead");
        });

        Participant virtual = participantRepository.findById(virtualParticipantId)
                .orElseThrow(() -> new NotFoundException("Participant not found"));

        if (!virtual.isVirtual()) {
            throw new BusinessException("Participant is not virtual");
        }

        if (!trip.getParticipantIds().contains(virtualParticipantId)) {
            throw new BusinessException("Participant is not in this trip");
        }

        inviteRepository.findByVirtualParticipantId(virtualParticipantId).ifPresent(existing -> {
            if (!force) {
                throw new BusinessException("Invite already sent");
            }
            inviteRepository.deleteByToken(existing.token());
        });

        String token = generateUniqueToken();

        VirtualParticipantInvite invite = new VirtualParticipantInvite(
                token,
                virtualParticipantId,
                tripId,
                email,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7)
        );
        inviteRepository.save(invite);

        String participantName = virtual.getName();
        String tripName = trip.getName();
        CompletableFuture.runAsync(() -> {
            try {
                sendInviteEmail(email, participantName, tripName, token);
                log.info("Invite email sent to {}", email);
            } catch (Exception e) {
                log.error("Failed to send invite email to {}: {}", email, e.getMessage());
            }
        });
    }

    private String generateUniqueToken() {
        String token;
        do {
            token = generateToken();
        } while (inviteRepository.findByToken(token).isPresent());
        return token;
    }

    private String generateToken() {
        StringBuilder sb = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    private void sendInviteEmail(String toEmail, String participantName, String tripName, String token) {
        String deepLink = "trippace://invite?token=" + token;
        String subject = "You were invited to the trip «" + tripName + "»";
        String html = "<p>Hi, <b>" + participantName + "</b>!</p>" +
                "<p>You have been invited to join the trip <b>«" + tripName + "»</b> in TripPace.</p>" +
                "<p>If you already have TripPace installed, tap the button below:</p>" +
                "<p><a href=\"" + deepLink + "\" style=\"background:#6C63FF;color:white;padding:12px 24px;border-radius:8px;text-decoration:none;font-weight:bold;\">Open in TripPace</a></p>" +
                "<p>Or enter this code manually in the app:</p>" +
                "<p style=\"font-size:24px;font-weight:bold;letter-spacing:6px;\">" + token + "</p>" +
                "<p style=\"color:#888;\">The invite is valid for 7 days.</p>";
        emailSender.send(toEmail, subject, html);
    }
}
