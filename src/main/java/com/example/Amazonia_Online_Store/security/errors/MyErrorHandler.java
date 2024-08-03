package com.example.Amazonia_Online_Store.security.errors;

import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyErrorHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> validationError(ValidationException e){
        return ResponseEntity.badRequest().body("{\"error\":\""+ e.getMessage() +"\"}");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> errorIntegridad(DataIntegrityViolationException e){
        return ResponseEntity.badRequest().body("{\"error\":\""+ e.getRootCause() +"\"}");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle400(MethodArgumentNotValidException e){
        var errors = e.getFieldErrors().stream().map(getIndividualErrors::new).toList();
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> userNotFound(UsernameNotFoundException e){
        return ResponseEntity.badRequest().body("{\"error\":\""+ e.getMessage() +"\"}");
    }

    public record getIndividualErrors(String campo, String error){
        public getIndividualErrors(FieldError error){
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
