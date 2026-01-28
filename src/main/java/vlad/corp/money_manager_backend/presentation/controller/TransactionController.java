package vlad.corp.money_manager_backend.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vlad.corp.money_manager_backend.application.transaction.AddTransactionUseCase;
import vlad.corp.money_manager_backend.application.transaction.UpdateTransactionUseCase;
import vlad.corp.money_manager_backend.domain.model.Transaction;
import vlad.corp.money_manager_backend.application.transaction.ListTransactionsByWalletUseCase;
import vlad.corp.money_manager_backend.presentation.dto.transaction.CreateTransactionRequest;
import vlad.corp.money_manager_backend.presentation.dto.transaction.TransactionDto;
import vlad.corp.money_manager_backend.presentation.dto.transaction.UpdateTransactionRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final AddTransactionUseCase addTransactionUseCase;
    private final UpdateTransactionUseCase updateTransactionUseCase;
    private final ListTransactionsByWalletUseCase listTransactionsByWalletUseCase;

    public TransactionController(AddTransactionUseCase addTransactionUseCase,
                                 UpdateTransactionUseCase updateTransactionUseCase,
                                 ListTransactionsByWalletUseCase listTransactionsByWalletUseCase) {
        this.addTransactionUseCase = addTransactionUseCase;
        this.updateTransactionUseCase = updateTransactionUseCase;
        this.listTransactionsByWalletUseCase = listTransactionsByWalletUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDto create(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestBody CreateTransactionRequest request
    ){
        Transaction tx = addTransactionUseCase.execute(
                request.walletId(),
                userId,
                request.amount(),
                request.category()
        );
        return toDto(tx);
    }

    @PutMapping("/{transactionId}")
    public TransactionDto update(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable UUID transactionId,
            @RequestBody UpdateTransactionRequest request
    ){
        Transaction tx = updateTransactionUseCase.execute(
                userId,
                transactionId,
                request.amount(),
                request.category(),
                request.expectedVersion()
        );
        return toDto(tx);
    }

    @GetMapping
    public List<TransactionDto> listByWallet(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam UUID walletId
    ) {
        return listTransactionsByWalletUseCase.execute(userId,walletId)
                .stream()
                .map(this::toDto)
                .toList();
    }


    private TransactionDto toDto(Transaction tx){
        return new TransactionDto(
                tx.getTransactionId(),
                tx.getWalletId(),
                tx.getAmount().amount(),
                tx.getAmount().currency().getCurrencyCode(),
                tx.getCategory(),
                tx.getVersion(),
                tx.getUpdatedBy(),
                tx.getUpdatedAt()
        );
    }

}
