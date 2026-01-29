package vlad.corp.money_manager_backend.presentation.mapper;

import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.domain.exception.VersionConflictException;
import vlad.corp.money_manager_backend.presentation.dto.conflict.TransactionClientDto;
import vlad.corp.money_manager_backend.presentation.dto.conflict.TransactionServerDto;
import vlad.corp.money_manager_backend.presentation.dto.conflict.VersionConflictResponse;

@Component
public class TransactionConflictMapper {
    public VersionConflictResponse toResponse(VersionConflictException exception) {

        TransactionClientDto clientTransaction = new TransactionClientDto(
                exception.getClientAttemptSnapshot().amount(),
                exception.getClientAttemptSnapshot().category(),
                exception.getClientAttemptSnapshot().expectedVersion()
        );
        TransactionServerDto serverTransaction = new TransactionServerDto(
                exception.getTransactionSnapshot().transactionId(),
                exception.getTransactionSnapshot().walletId(),
                exception.getTransactionSnapshot().amount(),
                exception.getTransactionSnapshot().category(),
                exception.getTransactionSnapshot().version(),
                exception.getTransactionSnapshot().updatedBy(),
                exception.getTransactionSnapshot().updatedAt()
        );
        return new VersionConflictResponse(
                exception.getTransactionId(),
                exception.getCurrentVersion(),
                exception.getExpectedVersion(),
                serverTransaction,
                clientTransaction,
                exception.getMessage()
        );
    }
}
