package org.dachser.shipment.service;

import org.dachser.shipment.dto.CostDto;
import org.dachser.shipment.dto.ShipmentDto;
import org.dachser.shipment.entities.Cost;
import org.dachser.shipment.entities.Income;
import org.dachser.shipment.entities.Shipment;
import org.dachser.shipment.exception.CostTypeNotFoundException;
import org.dachser.shipment.exception.ShipmentReferenceNotFoundException;
import org.dachser.shipment.repository.CostRepository;
import org.dachser.shipment.repository.CostTypeRepository;
import org.dachser.shipment.repository.IncomeRepository;
import org.dachser.shipment.repository.ShipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of the income and cost data entered for a shipment
 * (customer payment administration and operational cost administration, as
 * described in the use case document).
 */
@Service
public class ShipmentService {

    private final CostTypeRepository costTypeRepository;
    private final CostRepository costRepository;
    private final IncomeRepository incomeRepository;
    private static final Logger log = LoggerFactory.getLogger(ShipmentService.class);
    private final ShipmentRepository shipmentRepository;

    public ShipmentService(CostRepository costRepository, CostTypeRepository costTypeRepository, IncomeRepository incomeRepository,
                           ShipmentRepository shipmentRepository) {
        this.costTypeRepository = costTypeRepository;
        this.costRepository = costRepository;
        this.incomeRepository = incomeRepository;
        this.shipmentRepository = shipmentRepository;
    }

    /**
     * Records the income and cost entries submitted for a shipment, appending
     * them to the shipment's existing history.
     *
     * @param shipment     the shipment being updated
     * @param shipmentInfo the income/cost data entered by the user
     */
    public void updateShipmentInfo(Shipment shipment,
                                   ShipmentDto shipmentInfo) {

        Income income = createIncome(shipment,
                                     shipmentInfo.getIncomeValue());
        List<Income> newIncomelist = shipment.getIncome();
        newIncomelist.add(income);
        shipment.setIncome(newIncomelist);
        log.info("Updating shipment income for {}",shipmentInfo.getShipmentReference());

        List<Cost> cost = createCost(shipment,
                                     shipmentInfo.getCosts());
        if (!cost.isEmpty()) {
            List<Cost> newCostList = shipment.getCost();
            newCostList.addAll(cost);
            log.info("Updating shipment cost for {}",shipmentInfo.getShipmentReference());

        }

    }

    /**
     * Creates and persists the {@link Cost} entries (base and/or additional)
     * described in the given {@link CostDto}. A cost entry is only created
     * when its value is non-zero.
     *
     * @param shipment the shipment the costs belong to
     * @param costs    the base/additional cost values entered by the user
     * @return the cost entries that were created
     * @throws org.dachser.shipment.exception.CostTypeNotFoundException if the base or additional cost type is not seeded in the database
     */
    public List<Cost> createCost(Shipment shipment,
                                 CostDto costs) {
        List<Cost> costList = new ArrayList<>();

            if (costs.getCostValue().compareTo(BigDecimal.ZERO) != 0) {
                Cost cost = new Cost();
                cost.setValue(costs.getCostValue());
                cost.setCostType(costTypeRepository.findCostTypeById(1).orElseThrow(() -> new CostTypeNotFoundException("1")));
                cost.setInsertedOn(LocalDateTime.now());
                cost.setShipment(shipment);
                costRepository.save(cost);
                costList.add(cost);
            }
            if (costs.getAdditionalCostValue().compareTo(BigDecimal.ZERO) != 0) {
                Cost cost = new Cost();
                cost.setValue(costs.getAdditionalCostValue());
                cost.setCostType(costTypeRepository.findCostTypeById(2).orElseThrow(() -> new CostTypeNotFoundException("2")));
                cost.setInsertedOn(LocalDateTime.now());
                cost.setShipment(shipment);
                costRepository.save(cost);
                costList.add(cost);
            }

        return costList;
    }

    /**
     * Creates and persists a single {@link Income} entry for the shipment.
     *
     * @param shipment the shipment the income belongs to
     * @param value    the customer payment amount
     * @return the persisted income entity
     */
    public Income createIncome(Shipment shipment,
                               BigDecimal value) {
        Income income = new Income();
        income.setValue(value);
        income.setInsertedOn(LocalDateTime.now());
        income.setShipment(shipment);

        incomeRepository.save(income);

        return income;
    }

    /**
     * Retrieves the business reference of every shipment recorded in the system.
     *
     * @return all shipment references (e.g. {@code ["0001", "0002", "0003"]})
     * @throws ShipmentReferenceNotFoundException if the references cannot be retrieved
     */
    public List<String> getShipmentReferences() {
        try {
            return shipmentRepository.findAllReferences();
        } catch (Exception e) {
            log.error("Error retrieving shipment references", e);
            throw new ShipmentReferenceNotFoundException();
        }
    }
}
