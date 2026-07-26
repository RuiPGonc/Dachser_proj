package org.dachser.shipment.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Central place that translates exceptions raised anywhere in the application
 * into a consistent {@link ErrorResponse} payload, and logs them.
 * <p>
 * This is what satisfies the use case's "Data Retrieval Error" alternative
 * flow: every error is logged server-side and a message is returned so the UI
 * can notify the user.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Returns 404 when the requested shipment reference does not exist. */
    @ExceptionHandler(ShipmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleShipmentNotFound(ShipmentNotFoundException ex) {
        log.warn("Shipment not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** Returns 404 when a referenced cost type does not exist. */
    @ExceptionHandler(CostTypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCostTypeNotFound(CostTypeNotFoundException ex) {
        log.warn("Cost type not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** Returns 500 when the list of shipment references cannot be retrieved. */
    @ExceptionHandler(ShipmentReferenceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleShipmentReferenceNotFound(ShipmentReferenceNotFoundException ex) {
        log.error("Shipment reference list not found: {}", ex.getMessage());
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /** Returns 400 when the request violates a database constraint. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Data integrity violation", ex);
        return build(HttpStatus.BAD_REQUEST, "The submitted data violates a database constraint (e.g. a negative monetary value).");
    }

    /** Returns 400 when {@code @Valid} bean validation fails on a request body. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                           .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                           .reduce((a, b) -> a + "; " + b)
                           .orElse("Validation failed");
        log.warn("Validation error: {}", message);
        return build(HttpStatus.BAD_REQUEST, message);
    }

    /** Returns 400 when the submitted income/cost data fails a business rule (e.g. all values are zero). */
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidData(InvalidDataException ex) {
        log.warn("Invalid data sent: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST,  ex.getMessage());
    }

    /** Returns 400 when {@code @Validated} constraints fail on method/path/query parameters. */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                           .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                           .reduce((a, b) -> a + "; " + b)
                           .orElse("Validation failed");
        log.warn("Constraint violation: {}", message);
        return build(HttpStatus.BAD_REQUEST, message);
    }

    /** Fallback for any exception not handled above; logs the full stack trace but hides internal details from the client. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    /**
     * Builds the standard {@link ErrorResponse} body for a given status/message.
     */
    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ErrorResponse.of(status.value(), message));
    }
}
