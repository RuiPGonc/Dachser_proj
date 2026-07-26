package org.dachser.shipment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response payload returned after calculating (or listing) the profit/loss
 * of a shipment: {@code profitLossValue = totalIncome - totalCost}.
 */
@Builder
@Getter
@Schema(description = "Result of a profit/loss calculation for a shipment.")
public class ProfitLossDto {

    @Schema(description = "Shimpment reference.", example = "0001")
    private final String shipmentReference;

    @Schema(description = "Profit (positive) or loss (negative) for the shipment.", example = "700")
    @Digits(integer = 10, fraction = 2)
    private final BigDecimal profitLossValue;

    @Schema(description = "Total income considered in the calculation.", example = "1000")
    @Digits(integer = 10, fraction = 2)
    private final BigDecimal totalIncome;

    @Schema(description = "Total cost considered in the calculation.", example = "300")
    @Digits(integer = 10, fraction = 2)
    private final BigDecimal totalCost;

    @Schema(description = "Timestamp when the calculation was performed.", example = "2026-07-24T10:30:00")
    private LocalDateTime calculatedOn;
}
