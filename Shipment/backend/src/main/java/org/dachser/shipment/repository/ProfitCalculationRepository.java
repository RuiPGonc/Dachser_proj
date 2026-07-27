package org.dachser.shipment.repository;

import org.dachser.shipment.entities.ProfitCalculation;
import org.dachser.shipment.entities.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Data access layer for {@link ProfitCalculation} entities.
 * Basic CRUD operations are provided out of the box by {@link JpaRepository}.
 */
public interface ProfitCalculationRepository extends JpaRepository<ProfitCalculation, UUID> {

    /**
     * Retrieves every profit/loss calculation stored for the given shipment,
     * most recently inserted first.
     *
     * @param shipment the shipment to look up calculations for
     * @return the calculations recorded for that shipment, ordered by insertion date descending
     */
    List<ProfitCalculation> findAllByShipmentOrderByInsertedOnDesc(Shipment shipment);

}
