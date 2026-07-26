package org.dachser.shipment.exception;

/**
 * Thrown when a referenced {@link org.dachser.shipment.entities.CostType} cannot be found.
 * Mapped to HTTP 404 by {@link GlobalExceptionHandler}.
 */
public class CostTypeNotFoundException extends DomainException {
    /**
     * @param code the cost type code that could not be found
     */
    public CostTypeNotFoundException(String code) {
        super("Cost type with code: " + code + " not found");
    }
}
