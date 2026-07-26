package org.dachser.shipment.exception;

import java.util.UUID;

/**
 * Thrown when no {@link org.dachser.shipment.entities.Shipment} matches the
 * requested reference. Mapped to HTTP 404 by {@link GlobalExceptionHandler}.
 */
public class ShipmentNotFoundException extends DomainException {

    /**
     * @param reference the shipment reference that could not be found
     */
    public ShipmentNotFoundException(String reference) {
        super("Shipment with reference: " + reference + " not found");
    }

}
