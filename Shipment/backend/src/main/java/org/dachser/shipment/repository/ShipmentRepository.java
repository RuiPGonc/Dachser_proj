package org.dachser.shipment.repository;

import org.dachser.shipment.entities.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Data access layer for {@link Shipment} entities.
 * Basic CRUD operations are provided out of the box by {@link JpaRepository}.
 */
public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {

    /**
     * Looks up a shipment by its business reference (e.g. "0001").
     *
     * @param reference the shipment reference
     * @return the matching {@link Shipment}, if it exists
     */
    Optional<Shipment> findByReference(String reference);

    /**
     * Returns the business reference of every shipment, ordered alphabetically.
     *
     * @return all shipment references (e.g. {@code ["0001", "0002", "0003"]})
     */
    @Query("SELECT s.reference FROM Shipment s ORDER BY s.reference")
    List<String> findAllReferences();

}
