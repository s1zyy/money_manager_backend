package vlad.corp.money_manager_backend.presentation;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vlad.corp.money_manager_backend.application.exception.AccessDeniedException;
import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.exception.VersionConflictException;
import vlad.corp.money_manager_backend.presentation.dto.VersionConflictResponse;

@RestControllerAdvice
public class ApiExceptionHandler {

    public record ErrorResponse(String message) {}


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex){
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException ex){
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(VersionConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public VersionConflictResponse handleVersionConflictException(VersionConflictException ex){
        return new VersionConflictResponse(
                ex.getTransactionId(),
                ex.getCurrentVersion(),
                ex.getExpectedVersion()
        );
    }
}
