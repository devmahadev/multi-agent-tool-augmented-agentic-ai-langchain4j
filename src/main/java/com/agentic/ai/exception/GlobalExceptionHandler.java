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

        Map<String, Object> details = validationDetails(ex);
        return respond(HttpStatus.BAD_REQUEST, "Validation error", req, details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(
            ConstraintViolationException ex, HttpServletRequest req) {

        Map<String, Object> details = constraintDetails(ex);
        return respond(HttpStatus.BAD_REQUEST, "Constraint violation", req, details);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest req) {

        return respond(HttpStatus.BAD_REQUEST, ex.getMessage(), req);
    }

    @ExceptionHandler(SchipholApiException.class)
    public ResponseEntity<ErrorResponse> handleSchipholApi(
            SchipholApiException ex, HttpServletRequest req) {

        HttpStatus status = HttpStatus.BAD_GATEWAY;
        Map<String, Object> details = Map.of("upstreamStatus", ex.getStatusCode());
        return respond(status, ex.getMessage(), req, details);
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ErrorResponse> handleSpringError(
            ErrorResponseException ex, HttpServletRequest req) {

        HttpStatus status = (HttpStatus) ex.getStatusCode();
        String message = ex.getBody() != null ? ex.getBody().getDetail() : status.getReasonPhrase();
        return respond(status, message, req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAny(
            Exception ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Map<String, Object> details = Map.of("cause", ex.getClass().getSimpleName());
        return respond(status, "Unexpected error", req, details);
    }


    private ResponseEntity<ErrorResponse> respond(HttpStatus status, String message, HttpServletRequest req) {
        return respond(status, message, req, Map.of());
    }

    private ResponseEntity<ErrorResponse> respond(HttpStatus status, String message, HttpServletRequest req, Map<String, Object> details) {
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                req.getRequestURI(),
                details
        );
        return ResponseEntity.status(status).body(body);
    }

    private Map<String, Object> validationDetails(MethodArgumentNotValidException ex) {
        Map<String, Object> details = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(err -> details.put(err.getField(), err.getDefaultMessage()));
        return details;
    }

    private Map<String, Object> constraintDetails(ConstraintViolationException ex) {
        Map<String, Object> details = new HashMap<>();
        ex.getConstraintViolations()
                .forEach(v -> details.put(v.getPropertyPath().toString(), v.getMessage()));
        return details;
    }
}