package hr.hrproduct.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(CategoryDeleteException.class)
    public ResponseEntity handleCategoryDeleteException(CategoryDeleteException ex) {
        logger.log(Level.WARNING, "Exception in category service. Message: {0}", ex.getMessage());
        return ResponseEntity.unprocessableEntity().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleException(Exception ex) {
        logger.log(Level.SEVERE, "Exception in category service. Message: {0}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleAuthenticationException(AuthenticationException ex) {
        logger.log(Level.SEVERE, "error when trying to login ", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}

