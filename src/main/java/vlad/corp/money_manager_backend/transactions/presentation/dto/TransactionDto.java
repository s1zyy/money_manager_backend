package vlad.corp.money_manager_backend.transactions.presentation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDto(UUID id, Double amount, String category, LocalDateTime dateTime){}
