package org.dachser.shipment.exception;

/**
 * Thrown when it's not possible to get the shipment references list. Mapped to HTTP 500 by {@link GlobalExceptionHandler}.
 */
public class ShipmentReferenceNotFoundException extends DomainException {

    public ShipmentReferenceNotFoundException() {
        super("Shipment reference list not found");
    }

}
