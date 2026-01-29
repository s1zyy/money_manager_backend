package vlad.corp.money_manager_backend.presentation;

import org.junit.jupiter.api.Test;
import vlad.corp.money_manager_backend.domain.exception.VersionConflictException;
import vlad.corp.money_manager_backend.domain.model.ClientAttemptSnapshot;
import vlad.corp.money_manager_backend.domain.model.TransactionSnapshot;
import vlad.corp.money_manager_backend.presentation.dto.conflict.VersionConflictResponse;
import vlad.corp.money_manager_backend.presentation.mapper.TransactionConflictMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler(
            new TransactionConflictMapper()
    );



    @Test
    void handleVersionConflictException_returnsCorrectPayload() {
        UUID transactionId = UUID.randomUUID();
        int currentVersion = 3;
        int expectedVersion = 2;

        VersionConflictException ex =
                new VersionConflictException(transactionId, currentVersion, expectedVersion);

        TransactionSnapshot serverSnapshot = new TransactionSnapshot(
                transactionId,
                UUID.randomUUID(), // walletId
                UUID.randomUUID(), // updatedBy
                currentVersion,
                BigDecimal.valueOf(100),
                "USD",
                "Food",
                Instant.now()
        );
        ClientAttemptSnapshot clientSnapshot = new ClientAttemptSnapshot(
                BigDecimal.valueOf(120),
                "Groceries",
                expectedVersion
        );

        ex.attachTransactionSnapshot(serverSnapshot);
        ex.attachClientAttemptSnapshot(clientSnapshot);

        VersionConflictResponse response = handler.handleVersionConflictException(ex);

        assertNotNull(response);
        assertEquals(transactionId, response.transactionId());
        assertEquals(currentVersion, response.currentVersion());
        assertEquals(expectedVersion, response.expectedVersion());
        assertEquals("Groceries", response.clientTransaction().category());
        assertEquals(BigDecimal.valueOf(120), response.clientTransaction().amount());
        assertEquals("Food", response.serverTransaction().category());
        assertEquals(BigDecimal.valueOf(100), response.serverTransaction().amount());

    }

}
