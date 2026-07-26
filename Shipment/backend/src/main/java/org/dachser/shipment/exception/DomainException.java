package org.dachser.shipment.exception;

/**
 * Basics for all domain exceptions
 * Allows the global handler to catch all business exception
 * with a single @ExceptionHandler.
 */
public abstract class DomainException extends RuntimeException {

    /**
     * @param message a human-readable description of the business rule that was violated
     */
    protected DomainException(String message) {
        super(message);
    }
}
