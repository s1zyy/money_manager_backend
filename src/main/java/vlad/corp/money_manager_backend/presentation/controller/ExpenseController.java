package vlad.corp.money_manager_backend.presentation.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vlad.corp.money_manager_backend.application.expense.*;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.infrastructure.security.AuthenticatedParticipant;
import vlad.corp.money_manager_backend.presentation.dto.expense.CreateExpenseDto;
import vlad.corp.money_manager_backend.presentation.dto.expense.ExpenseDto;
import vlad.corp.money_manager_backend.presentation.dto.expense.UpdateExpenseDto;
import vlad.corp.money_manager_backend.presentation.mapper.expense_mapper.ExpenseMapperDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trips/{tripId}/expenses")
public class ExpenseController {
    private final AddExpenseUseCase addExpenseUseCase;
    private final DeleteExpenseUseCase deleteExpenseUseCase;
    private final GetExpenseUseCase getExpenseUseCase;
    private final UpdateExpenseUseCase updateExpenseUseCase;
    private final ListExpensesUseCase listExpensesUseCase;
    private final ExpenseMapperDto expenseMapper;

    public ExpenseController(AddExpenseUseCase addExpenseUseCase, DeleteExpenseUseCase deleteExpenseUseCase,
                             GetExpenseUseCase getExpenseUseCase, UpdateExpenseUseCase updateExpenseUseCase,
                             ListExpensesUseCase listExpensesUseCase, ExpenseMapperDto expenseMapper) {
        this.addExpenseUseCase = addExpenseUseCase;
        this.deleteExpenseUseCase = deleteExpenseUseCase;
        this.getExpenseUseCase = getExpenseUseCase;
        this.updateExpenseUseCase = updateExpenseUseCase;
        this.listExpensesUseCase = listExpensesUseCase;
        this.expenseMapper = expenseMapper;
    }

    @PostMapping
    public boolean addExpense(
            @PathVariable UUID tripId,
            @AuthenticationPrincipal AuthenticatedParticipant participant,
            @Valid @RequestBody CreateExpenseDto expenseDto) {
        addExpenseUseCase.execute(
                tripId,
                expenseDto.payerId(),
                expenseDto.amount(),
                expenseDto.date(),
                expenseDto.splitMode(),
                expenseDto.participantIds(),
                expenseDto.customShares(),
                expenseDto.description(),
                expenseDto.isPrepaid());
        return true;
    }

    @GetMapping("/{expenseId}")
    public ExpenseDto getExpense(
            @PathVariable UUID tripId,
            @PathVariable UUID expenseId,
            @AuthenticationPrincipal AuthenticatedParticipant participant) {
        Expense expense = getExpenseUseCase.execute(participant.participantId(), expenseId, tripId);
        return expenseMapper.toDto(expense);
    }

    @GetMapping
    public List<ExpenseDto> listExpenses(
            @PathVariable UUID tripId,
            @AuthenticationPrincipal AuthenticatedParticipant participant) {
        List<Expense> expenses = listExpensesUseCase.execute(tripId, participant.participantId());
        return expenses.stream().map(expenseMapper::toDto).toList();
    }

    @PutMapping("/{expenseId}")
    public ExpenseDto updateExpense(
            @PathVariable UUID tripId,
            @PathVariable UUID expenseId,
            @AuthenticationPrincipal AuthenticatedParticipant participant,
            @Valid @RequestBody UpdateExpenseDto expenseDto) {
        Expense expense = updateExpenseUseCase.execute(
                participant.participantId(),
                expenseId,
                tripId,
                expenseDto.date(),
                expenseDto.amount(),
                expenseDto.splitMode(),
                expenseDto.newParticipantIds(),
                expenseDto.customShares(),
                expenseDto.description());
        return expenseMapper.toDto(expense);
    }

    @DeleteMapping("/{expenseId}")
    public void deleteExpense(
            @PathVariable UUID tripId,
            @PathVariable UUID expenseId,
            @AuthenticationPrincipal AuthenticatedParticipant participant) {
        deleteExpenseUseCase.execute(participant.participantId(), expenseId, tripId);
    }
}
