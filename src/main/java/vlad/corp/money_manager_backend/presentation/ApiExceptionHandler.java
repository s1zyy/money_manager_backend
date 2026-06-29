package vlad.corp.money_manager_backend.presentation;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vlad.corp.money_manager_backend.application.exception.*;
import vlad.corp.money_manager_backend.domain.exceptions.*;

@RestControllerAdvice
public class ApiExceptionHandler {

    public record ErrorResponse(String message) {}


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex){
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(InvalidExpenseDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidExpenseDateException(InvalidExpenseDateException ex){
        return new ErrorResponse(ex.getMessage()); }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBusinessException(BusinessException ex){
        return new ErrorResponse(ex.getMessage());
    }


    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidCredentialsException(InvalidCredentialsException ex){
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(ParticipantAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleParticipantAlreadyExistException(ParticipantAlreadyExistException ex){
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(ArchivedTripException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse archivedTripException(Exception ex){
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(OwnerCannotLeaveTripException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse ownerCannotLeaveTripException(OwnerCannotLeaveTripException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(ForbiddenException ex){
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(Exception ex){
        return new ErrorResponse("Internal server error");
    }



}

