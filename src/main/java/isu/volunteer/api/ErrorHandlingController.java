package isu.volunteer.api;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import isu.volunteer.api.user.PhoneInUseException;
import isu.volunteer.api.user.UserNameInUseException;

@ControllerAdvice
public class ErrorHandlingController extends ResponseEntityExceptionHandler{

    private ErrorResponse makeResponse(Exception ex, String msg) {
        List<String> errorDetails = new ArrayList<>();
        errorDetails.add(ex.getMessage());
        ErrorResponse response = new ErrorResponse(msg, errorDetails);
        return response;
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status, 
        WebRequest request) {
            ErrorResponse response = makeResponse(ex, "Validation failed.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
        MissingPathVariableException ex, 
        HttpHeaders headers,
        HttpStatus status, 
        WebRequest request) {
            ErrorResponse response = makeResponse(ex, "Path variable is not valid.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintValidationException(ConstraintViolationException ex) {
        List<String> details = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            details.add(violation.getMessage());
        }
        String message = "Validation failed.";
        ErrorResponse response = new ErrorResponse(message, details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleUsernameNotFountException(AuthenticationException ex) {
        ErrorResponse response = makeResponse(ex, "Authentication exception.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse response = makeResponse(ex, "Access denied exception.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ResponseBody
    @ExceptionHandler(UserNameInUseException.class)
    public ResponseEntity<Object> handleUsernameInUseException(UserNameInUseException ex) {
        ErrorResponse response = makeResponse(ex, "Given username is in use.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ResponseBody
    @ExceptionHandler(PhoneInUseException.class)
    public ResponseEntity<Object> handlePhoneInUseException(PhoneInUseException ex) {
        ErrorResponse response = makeResponse(ex, "Given phone is in use.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ResponseBody
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handeUsernameNotFoundException(UsernameNotFoundException ex) {
        ErrorResponse response = makeResponse(ex, "Username not found.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
