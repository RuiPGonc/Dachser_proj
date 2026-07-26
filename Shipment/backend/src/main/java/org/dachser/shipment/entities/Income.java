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
 * Represents a single customer payment (income) recorded against a {@link Shipment}.
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "income")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "income_value")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    private LocalDateTime insertedOn;
}
