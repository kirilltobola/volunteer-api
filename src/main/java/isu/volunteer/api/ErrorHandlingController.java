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

@ControllerAdvice
public class ErrorHandlingController extends ResponseEntityExceptionHandler{
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status, 
        WebRequest request) {
            
            List<String> details = new ArrayList<>();
            details.add(ex.getMessage());
            String message = "Validation failed.";
            ErrorResponse response = new ErrorResponse(message, details);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
        MissingPathVariableException ex, 
        HttpHeaders headers,
        HttpStatus status, 
        WebRequest request) {
            
            List<String> details = new ArrayList<>();
            details.add(ex.getMessage());
            String message = "Path variable is not valid.";
            ErrorResponse response = new ErrorResponse(message, details);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    
    // TODO: get default message;
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
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        String message = "Authentication exception.";
        ErrorResponse response = new ErrorResponse(message, details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        String message = "Access denied exception.";
        ErrorResponse response = new ErrorResponse(message, details);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ResponseBody
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handeUsernameNotFoundException(UsernameNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        String message = "Username not found.";
        ErrorResponse response = new ErrorResponse(message, details);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
