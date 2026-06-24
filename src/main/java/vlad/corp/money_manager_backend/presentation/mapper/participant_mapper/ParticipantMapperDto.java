package vlad.corp.money_manager_backend.presentation.mapper.participant_mapper;

import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.presentation.dto.participant.ParticipantDto;

@Component
public class ParticipantMapperDto {
    public ParticipantDto toDto(Participant participant) {
        return new ParticipantDto(
                participant.getId(),
                participant.getName(),
                participant.getEmail(),
                participant.isVirtual()
        );
    }
}
