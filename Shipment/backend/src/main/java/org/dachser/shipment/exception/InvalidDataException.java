package org.dachser.shipment.exception;

/**
 * Thrown when a "Calculate Profit" request is rejected by a business rule,
 * e.g. income, cost and additional cost are all zero.
 * Mapped to HTTP 400 by {@link GlobalExceptionHandler}.
 */
public class InvalidDataException extends DomainException {

    /**
     * @param reference the shipment reference the invalid request refers to
     */
    public InvalidDataException( String reference) {
        super("Invalid Request for reference "+reference+". All received data are zero.");
    }
}
