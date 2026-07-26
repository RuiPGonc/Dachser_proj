package org.dachser.shipment;

import org.dachser.shipment.dto.CostDto;
import org.dachser.shipment.dto.ProfitLossDto;
import org.dachser.shipment.dto.ShipmentDto;
import org.dachser.shipment.service.ProfitLossCalculationService;
import org.dachser.shipment.service.ShipmentService;
import org.dachser.shipment.web.ProfitCalculationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProfitCalculationController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfitCalculationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfitLossCalculationService profitLossCalculationService;

    @MockBean
    private ShipmentService shipmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private ShipmentDto shipmentDto;
    private ProfitLossDto profitLossDto;
    private List<ProfitLossDto> profitLossList;

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

        // Setup ProfitLossDto
        profitLossDto = ProfitLossDto.builder()
                .shipmentReference(shipmentDto.getShipmentReference())
                .profitLossValue(new BigDecimal(700))
                .totalIncome(new BigDecimal(1000))
                .totalCost(new BigDecimal(300))
                .build();

        // Setup list for GET test
        profitLossList = List.of(profitLossDto);
    }

    @Test
    void testProfitLossCalculationPost() throws Exception {
        // Arrange
        when(profitLossCalculationService.profitLossCalculation(any(ShipmentDto.class)))
                .thenReturn(profitLossDto);

        String requestBody = objectMapper.writeValueAsString(shipmentDto);

        // Act & Assert
        mockMvc.perform(post("/profitLoss/calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.profitLossValue").value(700));
    }

    @Test
    void testGetAllProfitLoss() throws Exception {
        // Arrange
        when(profitLossCalculationService.getShipmentProfitLoss("00025R"))
                .thenReturn(profitLossList);

        // Act & Assert
        mockMvc.perform(get("/profitLoss")
                        .param("shipmentReference", "00025R")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].profitLossValue").value(700));
    }
}
