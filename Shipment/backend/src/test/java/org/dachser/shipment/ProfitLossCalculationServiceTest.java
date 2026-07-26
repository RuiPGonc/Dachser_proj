package org.dachser.shipment;

import org.dachser.shipment.dto.CostDto;
import org.dachser.shipment.dto.ProfitLossDto;
import org.dachser.shipment.dto.ShipmentDto;
import org.dachser.shipment.entities.ProfitCalculation;
import org.dachser.shipment.entities.Shipment;
import org.dachser.shipment.mapper.ProfitCalculationMapper;
import org.dachser.shipment.repository.ProfitCalculationRepository;
import org.dachser.shipment.repository.ShipmentRepository;
import org.dachser.shipment.service.ProfitLossCalculationService;
import org.dachser.shipment.service.ShipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfitLossCalculationServiceTest {

    @Mock
    private ProfitCalculationRepository profitCalculationRepository;

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private ShipmentService shipmentService;
    @Mock
    private ProfitCalculationMapper profitCalculationMapper;

    @InjectMocks
    private ProfitLossCalculationService profitLossCalculationService;

    private ShipmentDto shipmentDto;
    private Shipment shipment;
    private ProfitCalculation profitCalculation;
    private ProfitLossDto profitLossDto;

    @BeforeEach
    void setup(){
        // Setup CostDto
        CostDto costDto = new CostDto();
        costDto.setCostValue(new BigDecimal(100));
        costDto.setAdditionalCostValue(new BigDecimal(200));

        // Setup ShipmentDto
        shipmentDto = new ShipmentDto();
        shipmentDto.setIncomeValue(new BigDecimal(1000));
        shipmentDto.setShipmentReference("00025R");
        shipmentDto.setCosts(costDto);

        // Setup Shipment entity
        shipment = new Shipment();
        shipment.setReference("00025R");
        shipment.setId(UUID.randomUUID());

        // Setup ProfitCalculation entity
        profitCalculation = new ProfitCalculation();
        profitCalculation.setShipment(shipment);
        profitCalculation.setIncome(new BigDecimal(1000));
        profitCalculation.setCost(new BigDecimal(300));
        profitCalculation.setProfit(new BigDecimal(700));
        profitCalculation.setInsertedOn(LocalDateTime.now());

        // Setup ProfitLossDto
        profitLossDto = ProfitLossDto.builder()
                .profitLossValue(new BigDecimal(700))
                .totalIncome(new BigDecimal(1000))
                .totalCost(new BigDecimal(300))
                .build();
    }

    @Test
    void profitLossCalculationTest() {
        // Arrange
        when(shipmentRepository.findByReference("00025R")).thenReturn(Optional.of(shipment));
        when(profitCalculationRepository.save(any(ProfitCalculation.class))).thenReturn(profitCalculation);
        when(profitCalculationMapper.toDto(profitCalculation)).thenReturn(profitLossDto);

        // Act
        ProfitLossDto result = profitLossCalculationService.profitLossCalculation(shipmentDto);

        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal(700), result.getProfitLossValue());
        assertEquals(new BigDecimal(1000), result.getTotalIncome());
        assertEquals(new BigDecimal(300), result.getTotalCost());
        verify(shipmentRepository, times(1)).findByReference("00025R");
        verify(profitCalculationRepository, times(1)).save(any(ProfitCalculation.class));
    }
}
