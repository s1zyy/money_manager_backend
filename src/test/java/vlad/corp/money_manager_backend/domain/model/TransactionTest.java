package vlad.corp.money_manager_backend.domain.model;


import vlad.corp.money_manager_backend.domain.exception.VersionConflictException;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionTest {

    @Test
    void update_throwsVersionConflict_and_exception_containsVersions() {
        UUID id = UUID.randomUUID();
        Transaction tx = new Transaction(
                id,
                UUID.randomUUID(),
                null,
                null,
                LocalDateTime.now(),
                2,
                UUID.randomUUID()
        );

        VersionConflictException ex = assertThrows(VersionConflictException.class, () -> {
            tx.update(null,null, UUID.randomUUID(), 1);
        });

        assertEquals(id, ex.getTransactionId());
        assertEquals(2, ex.getCurrentVersion());
        assertEquals(1, ex.getExpectedVersion());

    }
}
