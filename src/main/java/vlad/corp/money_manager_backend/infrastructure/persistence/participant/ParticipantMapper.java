package vlad.corp.money_manager_backend.infrastructure.persistence.participant;

import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.domain.model.Participant;

@Component
public class ParticipantMapper {

    public ParticipantEntity toEntity(Participant participant) {
        return new ParticipantEntity(
                participant.getId(),
                participant.getName(),
                participant.getEmail(),
                participant.getPasswordHash(),
                participant.isVirtual()
        );
    }

    public Participant toDomain(ParticipantEntity participantEntity) {
        return new Participant(
                participantEntity.getId(),
                participantEntity.getName(),
                participantEntity.getEmail(),
                participantEntity.getPasswordHash(),
                participantEntity.isVirtual()
        );
    }
}
