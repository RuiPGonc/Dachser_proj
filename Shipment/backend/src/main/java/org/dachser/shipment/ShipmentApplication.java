package org.dachser.shipment;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Shipment Profit/Loss calculation service.
 * <p>
 * Exposes a REST API (see {@link org.dachser.shipment.web.ProfitCalculationController})
 * implementing the "Calculate Profit" use case: given the income and costs of
 * a shipment, compute and store its profit or loss.
 * <p>
 * Once the application is running, the interactive Swagger UI is available at
 * {@code /swagger-ui.html} and the raw OpenAPI spec at {@code /v3/api-docs}.
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Shipment Profit/Loss Calculation API",
        version = "0.0.1",
        description = "Calculates and stores the profit or loss (income - costs) for a shipment."
))
public class ShipmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShipmentApplication.class,
                              args);
    }

}
