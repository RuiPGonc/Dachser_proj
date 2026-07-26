package org.dachser.shipment.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Stores the outcome of a single "Calculate Profit" request: the income and
 * total cost that were used, and the resulting profit or loss
 * ({@code profit = income - cost}) for a {@link Shipment}.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profit_calculation")
public class ProfitCalculation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    @Digits(integer = 10, fraction = 2)
    private BigDecimal profit;

    /** Total cost (base + additional) used in this calculation. */
    @Column
    @Digits(integer = 10, fraction = 2)
    private BigDecimal cost;

    @Column
    @Digits(integer = 10, fraction = 2)
    private BigDecimal income;

    /** Shipment this calculation belongs to. */
    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    private LocalDateTime insertedOn;

}
