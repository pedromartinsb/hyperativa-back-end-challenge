package com.hyperativa.visa.common.exception;

import com.hyperativa.visa.adapter.exception.IllegalCardRequestFormatException;
import com.hyperativa.visa.common.response.ErrorResponse;
import com.hyperativa.visa.domain.exception.CreditCardNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.context.request.WebRequest;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        ErrorResponse error = new ErrorResponse("Validation error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse("Bad Request", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchAlgorithmException(NoSuchAlgorithmException ex) {
        ErrorResponse error = new ErrorResponse("Bad Request", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put("timestamp", LocalDateTime.now());
            errors.put("message", "'" + error.getField() + "': " + error.getDefaultMessage());
            errors.put("description", request.getDescription(false));
            errors.put("statusCode", HttpStatus.BAD_REQUEST.value());
        });
        return errors;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleResourceBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Usuário e/ou Senha estão incorretos.");
        response.put("description", request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreditCardNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCreditCardNotFoundException(CreditCardNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("Not Found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalCardRequestFormatException.class)
    public ResponseEntity<ErrorResponse> handleIllegalCardRequestFormatException(IllegalCardRequestFormatException ex) {
        ErrorResponse error = new ErrorResponse("Bad Request", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ex.printStackTrace();
        ErrorResponse error = new ErrorResponse("Internal Server Error", "Ocorreu um erro inesperado. Tente novamente mais tarde.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 