package org.dachser.shipment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Request payload for the "Calculate Profit" use case (see
 * {@link org.dachser.shipment.web.ProfitCalculationController}).
 * <p>
 * Carries the shipment reference together with the income and cost data
 * entered by the Finance Department for that shipment.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Income and cost data submitted to calculate the profit or loss of a shipment.")
public class ShipmentDto {

    @NotBlank(message = "Shipment reference is required")
    @Schema(description = "Business reference of the shipment.", example = "0001")
    private String shipmentReference;

    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 10, fraction = 2)
    @Schema(description = "Customer payment (income) for the shipment.", example = "1000")
    private BigDecimal incomeValue;

    @NotNull
    @Valid
    @Schema(description = "Cost breakdown for the shipment.")
    private CostDto costs;

}
