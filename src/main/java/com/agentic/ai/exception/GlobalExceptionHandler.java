package com.agentic.ai.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Consistent JSON error responses for API consumers and for easier frontend handling.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest req) {

        Map<String, Object> details = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                details.put(err.getField(), err.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
                new ErrorResponse(Instant.now(), 400, "Bad Request", "Validation error", req.getRequestURI(), details
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(
            ConstraintViolationException ex, HttpServletRequest req) {

        Map<String, Object> details = new HashMap<>();
        ex.getConstraintViolations()
                .forEach(v -> details.put(v.getPropertyPath().toString(), v.getMessage()));

        return ResponseEntity.badRequest().body(new ErrorResponse(
                Instant.now(), 400, "Bad Request", "Constraint violation", req.getRequestURI(), details
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest req) {

        return ResponseEntity.badRequest().body(new ErrorResponse(
                Instant.now(), 400, "Bad Request", ex.getMessage(), req.getRequestURI(), Map.of()
        ));
    }

    @ExceptionHandler(SchipholApiException.class)
    public ResponseEntity<ErrorResponse> handleSchipholApi(
            SchipholApiException ex, HttpServletRequest req) {

        HttpStatus status = HttpStatus.BAD_GATEWAY;
        return ResponseEntity.status(status).body(new ErrorResponse(
                Instant.now(), status.value(), status.getReasonPhrase(),
                ex.getMessage(), req.getRequestURI(), Map.of("upstreamStatus", ex.getStatusCode())
        ));
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ErrorResponse> handleSpringError(
            ErrorResponseException ex, HttpServletRequest req) {

        HttpStatus status = (HttpStatus) ex.getStatusCode();
        return ResponseEntity.status(status).body(new ErrorResponse(
                Instant.now(), status.value(), status.getReasonPhrase(),
                ex.getBody().getDetail(), req.getRequestURI(), Map.of()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAny(
            Exception ex, HttpServletRequest req) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(new ErrorResponse(
                Instant.now(), status.value(), status.getReasonPhrase(),
                "Unexpected error", req.getRequestURI(), Map.of("cause", ex.getClass().getSimpleName())
        ));
    }
}