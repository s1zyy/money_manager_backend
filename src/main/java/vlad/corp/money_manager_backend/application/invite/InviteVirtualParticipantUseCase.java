package vlad.corp.money_manager_backend.application.invite;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import vlad.corp.money_manager_backend.application.exception.NotFoundException;
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

public class InviteVirtualParticipantUseCase {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TOKEN_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final TripRepository tripRepository;
    private final ParticipantRepository participantRepository;
    private final VirtualParticipantInviteRepository inviteRepository;
    private final JavaMailSender mailSender;
    private final String fromEmail;

    public InviteVirtualParticipantUseCase(TripRepository tripRepository,
                                           ParticipantRepository participantRepository,
                                           VirtualParticipantInviteRepository inviteRepository,
                                           JavaMailSender mailSender,
                                           String fromEmail) {
        this.tripRepository = tripRepository;
        this.participantRepository = participantRepository;
        this.inviteRepository = inviteRepository;
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    public void execute(UUID tripId, UUID ownerId, UUID virtualParticipantId, String email) {
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

        sendInviteEmail(email, virtual.getName(), trip.getName(), token);
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
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("You were invited on a trip «" + tripName + "»");
        message.setText(
                "Hi, " + participantName + "!\n\n" +
                "You have been invited to join the trip «" + tripName + "» in Budgi app.\n\n" +
                "Download the app and enter this code when registering:\n\n" +
                "  " + token + "\n\n" +
                "The code is valid for 7 days.\n\n" +
                "After logging in, you'll see your spending, your daily limit, and you can add expenses yourself."
        );
        mailSender.send(message);
    }
}
