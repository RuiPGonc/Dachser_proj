package org.dachser.shipment.mapper;

import org.dachser.shipment.dto.ProfitLossDto;
import org.dachser.shipment.entities.ProfitCalculation;
import org.springframework.stereotype.Component;

/**
 * Maps {@link ProfitCalculation} entities to their API representation,
 * {@link ProfitLossDto}, so the persistence model is never exposed directly
 * to controller consumers.
 */
@Component
public class ProfitCalculationMapper {

    /**
     * Converts a persisted profit/loss calculation into its DTO representation.
     *
     * @param profitCalculation the entity to convert
     * @return the corresponding {@link ProfitLossDto}
     */
    public ProfitLossDto toDto(ProfitCalculation profitCalculation) {
        return ProfitLossDto.builder()
                .shipmentReference(profitCalculation.getShipment().getReference())
                .profitLossValue(profitCalculation.getProfit())
                .totalIncome(profitCalculation.getIncome())
                .totalCost(profitCalculation.getCost())
                .calculatedOn(profitCalculation.getInsertedOn())
                .build();
    }
}
