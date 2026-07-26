package org.dachser.shipment.repository;

import org.dachser.shipment.entities.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Data access layer for the {@link Income} lookup table.
 * Basic CRUD operations are provided out of the box by {@link JpaRepository}.
 */
public interface IncomeRepository extends JpaRepository<Income, UUID> {

}
