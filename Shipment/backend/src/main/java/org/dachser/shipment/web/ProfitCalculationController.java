package org.dachser.shipment.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.dachser.shipment.dto.ProfitLossDto;
import org.dachser.shipment.dto.ShipmentDto;
import org.dachser.shipment.exception.ErrorResponse;
import org.dachser.shipment.service.ProfitLossCalculationService;
import org.dachser.shipment.service.ShipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for the "Calculate Profit" use case.
 * <p>
 * Exposes exactly one controller, as required by the assessment: it lets the
 * Finance Department submit a shipment's income/cost data to have its profit
 * or loss calculated and stored ({@link #profitLossCalculation}), and lets
 * previously calculated results be retrieved for a shipment ({@link #getAll}).
 */
@RestController
@RequestMapping("profitLoss")
@Validated
@Tag(name = "Profit/Loss", description = "Calculate and query the profit or loss of a shipment")
public class ProfitCalculationController {

    private final ProfitLossCalculationService profitLossCalculationService;

    private static final Logger log = LoggerFactory.getLogger(ProfitCalculationController.class);
    private final ShipmentService shipmentService;

    public ProfitCalculationController(ProfitLossCalculationService profitLossCalculationService,
                                       ShipmentService shipmentService) {
        this.profitLossCalculationService = profitLossCalculationService;
        this.shipmentService = shipmentService;
    }

    /**
     * Calculates the profit or loss for a shipment from the submitted income
     * and cost data, and stores both the raw data and the result.
     * <p>
     * No {@code Location} header is returned: there is no single-resource
     * endpoint for one calculation, only {@link #getAll} which lists every
     * calculation stored for a shipment.
     *
     * @param shipmentInfo the shipment reference plus income/cost data
     * @return the calculated profit or loss, with the totals used to compute it
     */
    @Operation(summary = "Calculate the profit or loss of a shipment",
            description = "Stores the income and cost data submitted for a shipment, computes profit = income - cost, and persists the result.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profit/loss calculated and stored",
                    content = @Content(schema = @Schema(implementation = ProfitLossDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body (validation error or all values are zero)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Shipment reference not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/calculation")
    public ResponseEntity<ProfitLossDto> profitLossCalculation(@Valid @RequestBody ShipmentDto shipmentInfo) {
        log.info("Profit and Loss calculation request received");
        ProfitLossDto calculation = this.profitLossCalculationService.profitLossCalculation(shipmentInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(calculation);
    }

    /**
     * Retrieves every profit/loss calculation previously stored for a shipment.
     *
     * @param shipmentReference the business reference of the shipment
     * @return the calculations recorded for that shipment (possibly empty)
     */
    @Operation(summary = "List the profit/loss calculations of a shipment",
            description = "Returns every profit/loss calculation previously stored for the given shipment reference.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Calculations retrieved"),
            @ApiResponse(responseCode = "400", description = "Missing or blank shipment reference",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Shipment reference not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProfitLossDto>> getAll(
            @Parameter(description = "Business reference of the shipment", example = "0001")
            @RequestParam @NotBlank(message = "Shipment reference is required") String shipmentReference) {
        log.info("Get all profit and loss registries by shipment request received");
        List<ProfitLossDto> response = this.profitLossCalculationService.getShipmentProfitLoss(shipmentReference);
        return ResponseEntity.ok()
                             .body(response);
    }

    /**
     * Retrieves the business reference of every shipment recorded in the system.
     *
     * @return all shipment references (e.g. {@code ["0001", "0002", "0003"]})
     */
    @Operation(summary = "List all shipment references",
            description = "Returns the business reference of every shipment recorded in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "References retrieved",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "The references could not be retrieved",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/references")
    public ResponseEntity<List<String>> getAllShipmentReferences() {
        log.info("Get all shipment references request received");
        return ResponseEntity.ok(shipmentService.getShipmentReferences());
    }
}
