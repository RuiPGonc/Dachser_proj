package org.dachser.shipment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Cost data submitted as part of a "Calculate Profit" request.
 * <p>
 * The current use case only distinguishes two cost buckets: a base service
 * cost and an additional cost. Both are mapped internally to their matching
 * {@link org.dachser.shipment.entities.CostType} row.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Cost information for a shipment, split into base and additional cost.")
public class CostDto {

    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 10, fraction = 2)
    @Schema(description = "Base service cost for the shipment.", example = "200")
    private BigDecimal costValue;

    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 10, fraction = 2)
    @Schema(description = "Additional cost for the shipment (0 if not applicable).", example = "0")
    private BigDecimal additionalCostValue;
}
