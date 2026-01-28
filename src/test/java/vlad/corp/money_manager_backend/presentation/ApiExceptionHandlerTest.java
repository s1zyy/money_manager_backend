package vlad.corp.money_manager_backend.presentation;

import org.junit.jupiter.api.Test;
import vlad.corp.money_manager_backend.domain.exception.VersionConflictException;
import vlad.corp.money_manager_backend.presentation.dto.VersionConflictResponse;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void handleVersionConflictException_returnsCorrectPayload() {
        UUID transactionId = UUID.randomUUID();
        int currentVersion = 3;
        int expectedVersion = 2;

        VersionConflictException ex =
                new VersionConflictException(transactionId, currentVersion, expectedVersion);

        VersionConflictResponse response = handler.handleVersionConflictException(ex);

        assertNotNull(response);
        assertEquals(transactionId, response.getTransactionId());
        assertEquals(currentVersion, response.getCurrentVersion());
        assertEquals(expectedVersion, response.getExpectedVersion());
    }

}
