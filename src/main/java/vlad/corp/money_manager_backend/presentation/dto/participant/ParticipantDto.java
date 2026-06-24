package vlad.corp.money_manager_backend.presentation.dto.participant;

import java.util.UUID;

public record ParticipantDto(
         UUID id,
         String name,
         String email,
         boolean isVirtual) {
}
