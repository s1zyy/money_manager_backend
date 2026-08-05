package vlad.corp.money_manager_backend.application.participant;

import com.cloudinary.Cloudinary;
import vlad.corp.money_manager_backend.application.exceptions.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class UploadAvatarUseCase {

    private final ParticipantRepository participantRepository;
    private final Cloudinary cloudinary;

    public UploadAvatarUseCase(ParticipantRepository participantRepository, Cloudinary cloudinary) {
        this.participantRepository = participantRepository;
        this.cloudinary = cloudinary;
    }

    public String execute(UUID participantId, byte[] fileBytes) {
        Participant existing = participantRepository.findById(participantId)
                .orElseThrow(() -> new NotFoundException("Participant not found"));

        try {
            var result = cloudinary.uploader().upload(fileBytes, Map.of(
                    "public_id", "avatars/" + participantId,
                    "overwrite", true
                    //"transformation", Map.of("width", 256, "height", 256, "crop", "fill", "gravity", "face")
            ));
            String url = (String) result.get("secure_url");

            Participant updated = new Participant(
                    existing.getId(),
                    existing.getName(),
                    existing.getEmail(),
                    existing.getPasswordHash(),
                    existing.isVirtual(),
                    url
            );
            participantRepository.save(updated);
            return url;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload avatar", e);
        }
    }
}
