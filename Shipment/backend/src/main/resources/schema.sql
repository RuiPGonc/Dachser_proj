-- Table for Cost Types
CREATE TABLE cost_type (
                           cost_type_id  INT           NOT NULL PRIMARY KEY,
                           code          VARCHAR(40)   NOT NULL UNIQUE,
                           name          VARCHAR(100)  NOT NULL,
                           display_order INT           NOT NULL,
                           inserted_on   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Shipment Table
CREATE TABLE shipment (
                          id          UUID        NOT NULL PRIMARY KEY,
                          reference   VARCHAR(40) NOT NULL UNIQUE,
                          inserted_on TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Income Table
CREATE TABLE income (
                        id            UUID          NOT NULL PRIMARY KEY,
                        shipment_id   UUID          NOT NULL,
                        income_value  DECIMAL(12,2) NOT NULL CHECK (income_value >= 0),
                        inserted_on   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_income_shipment
                            FOREIGN KEY (shipment_id) REFERENCES shipment(id)
);

-- Table Cost
CREATE TABLE cost (
                      id           UUID          NOT NULL PRIMARY KEY,
                      shipment_id  UUID          NOT NULL,
                      cost_type_id INT           NOT NULL,
                      cost_value   DECIMAL(12,2) NOT NULL CHECK (cost_value >= 0),
                      inserted_on  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      CONSTRAINT fk_cost_shipment
                          FOREIGN KEY (shipment_id) REFERENCES shipment(id),
                      CONSTRAINT fk_cost_type
                          FOREIGN KEY (cost_type_id) REFERENCES cost_type(cost_type_id)
);

-- Profit Calculation Table
CREATE TABLE profit_calculation (
                                    id            UUID          NOT NULL PRIMARY KEY,
                                    shipment_id   UUID          NOT NULL,
                                    income        DECIMAL(12,2) NOT NULL,
                                    cost          DECIMAL(12,2) NOT NULL,
                                    profit        DECIMAL(12,2) NOT NULL,
                                    inserted_on   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    CONSTRAINT fk_calc_shipment
                                        FOREIGN KEY (shipment_id) REFERENCES shipment(id)
);

-- FKs Index
CREATE INDEX idx_income_shipment ON income(shipment_id);
CREATE INDEX idx_cost_shipment   ON cost(shipment_id);
CREATE INDEX idx_cost_type       ON cost(cost_type_id);
CREATE INDEX idx_calc_shipment   ON profit_calculation(shipment_id);

