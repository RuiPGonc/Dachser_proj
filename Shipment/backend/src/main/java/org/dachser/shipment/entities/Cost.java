package org.dachser.shipment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a single operational cost incurred while providing the service
 * for a {@link Shipment} (e.g. base service cost or an additional cost).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cost")
public class Cost {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "cost_value")
    private BigDecimal value;

    /** Classification of this cost (e.g. base service cost, additional cost). */
    @ManyToOne
    @JoinColumn(name = "cost_type_id")
    private CostType costType;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @Column(name = "inserted_on", updatable = false, nullable = false)
    private LocalDateTime insertedOn;
}
