package org.dachser.shipment.service;

import org.dachser.shipment.dto.CostDto;
import org.dachser.shipment.dto.ProfitLossDto;
import org.dachser.shipment.dto.ShipmentDto;
import org.dachser.shipment.entities.ProfitCalculation;
import org.dachser.shipment.entities.Shipment;
import org.dachser.shipment.exception.InvalidDataException;
import org.dachser.shipment.exception.ShipmentNotFoundException;
import org.dachser.shipment.mapper.ProfitCalculationMapper;
import org.dachser.shipment.repository.ProfitCalculationRepository;
import org.dachser.shipment.repository.ShipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Business logic for the "Calculate Profit" use case: records the income and
 * cost submitted for a shipment, computes {@code profit = income - cost},
 * persists the result, and lets it be looked up afterwards.
 */
@Service
public class ProfitLossCalculationService {
    private final ProfitCalculationRepository profitCalculationRepository;
    private final ShipmentRepository shipmentRepository;
    private final ShipmentService shipmentService;
    private final ProfitCalculationMapper profitCalculationMapper;

    private static final Logger log = LoggerFactory.getLogger(ProfitLossCalculationService.class);


    public ProfitLossCalculationService(ProfitCalculationRepository profitCalculationRepository,
                                        ShipmentRepository shipmentRepository,
                                        ShipmentService shipmentService,
                                        ProfitCalculationMapper profitCalculationMapper) {
        this.profitCalculationRepository = profitCalculationRepository;
        this.shipmentRepository = shipmentRepository;
        this.shipmentService = shipmentService;
        this.profitCalculationMapper = profitCalculationMapper;
    }

    /**
     * Retrieves every profit/loss calculation previously stored for a shipment,
     * most recent first.
     *
     * @param shipmentReference the business reference of the shipment
     * @return the calculations recorded for that shipment, as DTOs, ordered by
     * insertion date descending (an empty list if none were found)
     * @throws org.dachser.shipment.exception.ShipmentNotFoundException if no shipment matches the reference
     */
    public List<ProfitLossDto> getShipmentProfitLoss(String shipmentReference) {

        Shipment shipment = shipmentRepository.findByReference(shipmentReference)
                                                       .orElseThrow(() -> new ShipmentNotFoundException(shipmentReference));

        List<ProfitCalculation> profitLossList = profitCalculationRepository.findAllByShipmentOrderByInsertedOnDesc(shipment);
        log.info("Found {} profit/loss registries for shipment {}", profitLossList.size(), shipment.getReference());

        return profitLossList.stream().map(profitCalculationMapper::toDto).toList();
    }
    /**
     * Executes the "Calculate Profit" use case main flow: stores the income
     * and cost data submitted for the shipment, computes the profit or loss,
     * and persists the resulting {@link org.dachser.shipment.entities.ProfitCalculation}.
     *
     * @param shipmentInfo the shipment reference plus the income/cost data entered by the user
     * @return the calculated profit or loss, with the totals used to compute it
     * @throws org.dachser.shipment.exception.InvalidDataException if income, cost and additional cost are all zero
     * @throws org.dachser.shipment.exception.ShipmentNotFoundException if no shipment matches the reference
     */
    @Transactional
    public ProfitLossDto profitLossCalculation(ShipmentDto shipmentInfo) {
        if(shipmentInfo.getIncomeValue().compareTo(BigDecimal.ZERO) == 0 && shipmentInfo.getCosts().getCostValue().compareTo(BigDecimal.ZERO) == 0
           && shipmentInfo.getCosts().getAdditionalCostValue().compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidDataException(shipmentInfo.getShipmentReference());
}
        Shipment shipment = shipmentRepository.findByReference(shipmentInfo.getShipmentReference())
                .orElseThrow(() -> new ShipmentNotFoundException(shipmentInfo.getShipmentReference()));

        shipmentService.updateShipmentInfo(shipment,
                                           shipmentInfo);

        CostDto costs = shipmentInfo.getCosts();
        BigDecimal cost = costs.getCostValue();
        BigDecimal additionalCost = costs.getAdditionalCostValue();

        BigDecimal totalCost = totalCostCalculation(cost,
                                                    additionalCost);

        BigDecimal profitLossValue = calculationOperation(shipmentInfo.getIncomeValue(),
                                                          totalCost);

        ProfitCalculation profitCalculation = new ProfitCalculation();
        profitCalculation.setShipment(shipment);
        profitCalculation.setIncome(shipmentInfo.getIncomeValue());
        profitCalculation.setCost(totalCost);
        profitCalculation.setProfit(profitLossValue);
        profitCalculation.setInsertedOn(LocalDateTime.now());
        profitCalculation = profitCalculationRepository.save(profitCalculation);

        log.info("New profit calculation created");

        return profitCalculationMapper.toDto(profitCalculation);

    }

    /**
     * Sums the base and additional cost into a single total cost figure.
     *
     * @param cost           base service cost
     * @param additionalCost additional cost
     * @return the total cost ({@code cost + additionalCost})
     */
    public BigDecimal totalCostCalculation(BigDecimal cost,
                                           BigDecimal additionalCost) {
        return cost.add(additionalCost);
    }

    /**
     * Computes the profit or loss for a shipment.
     *
     * @param income    total income (customer payment)
     * @param costValue total cost
     * @return the profit (positive) or loss (negative), i.e. {@code income - costValue}
     */
    public BigDecimal calculationOperation(BigDecimal income,
                                           BigDecimal costValue) {
        return income.subtract(costValue);
    }

}
