package org.dachser.shipment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Reference/lookup table describing the kinds of cost that can be recorded
 * against a shipment (e.g. {@code BASE}, {@code ADDITIONAL}).
 * <p>
 * Rows are seeded once via {@code data.sql} and are not expected to change at runtime.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cost_type")
public class CostType {

    @Id
    @Column(name = "cost_type_id", nullable = false)
    private Integer id;

    @Column
    private String name;

    @Column
    private String code;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "inserted_on", updatable = false, nullable = false)
    private LocalDateTime insertedOn;

}
