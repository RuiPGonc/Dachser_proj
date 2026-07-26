package org.dachser.shipment.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a single shipment (main carriage) handled by the logistics company.
 * <p>
 * A shipment aggregates all the {@link Income} and {@link Cost} records that
 * are used to compute its profit or loss (see {@link ProfitCalculation}).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Business/customer-facing reference used to look up a shipment (e.g. "0001"). Must be unique.
     */
    @Column(nullable = false, unique = true)
    @Size(max = 40)
    private String reference;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shipment")
    private List<Cost> cost = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shipment")
    private List<Income> income = new ArrayList<>();

    private LocalDateTime insertedOn;
}
