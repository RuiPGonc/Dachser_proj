package org.dachser.shipment.repository;

import org.dachser.shipment.entities.CostType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Data access layer for the {@link CostType} lookup table.
 * Basic CRUD operations are provided out of the box by {@link JpaRepository}.
 */
public interface CostTypeRepository extends JpaRepository<CostType, Integer> {

    /**
     * Looks up a cost type by its primary key.
     *
     * @param id the cost type identifier
     * @return the matching {@link CostType}, if it exists
     */
    Optional<CostType> findCostTypeById (Integer id);
}
