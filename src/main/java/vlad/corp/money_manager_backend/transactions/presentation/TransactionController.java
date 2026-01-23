package vlad.corp.money_manager_backend.transactions.presentation;

import org.springframework.web.bind.annotation.*;
import vlad.corp.money_manager_backend.transactions.application.TransactionService;
import vlad.corp.money_manager_backend.transactions.domain.Transaction;
import vlad.corp.money_manager_backend.transactions.presentation.dto.TransactionDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<TransactionDto> getAll() {
        return transactionService.getAllTransactions()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @PostMapping
    public void create(@RequestBody TransactionDto dto) {
        transactionService.createTransaction(
                new Transaction(
                        dto.id() != null ? dto.id() : UUID.randomUUID(),
                        dto.amount(),
                        dto.category(),
                        dto.dateTime()
                )
        );
    }
    public TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCategory(),
                transaction.getDateTime());
    }
}
