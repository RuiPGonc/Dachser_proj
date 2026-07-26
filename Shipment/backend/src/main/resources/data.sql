
INSERT INTO cost_type (cost_type_id,code,name, display_order,inserted_on) VALUES
(1,'BASE',      'Base service cost',1,CURRENT_TIMESTAMP),
(2,'ADDITIONAL','Additional cost', 2, CURRENT_TIMESTAMP);


INSERT INTO shipment (id,reference,inserted_on) VALUES
('dee0891b-a309-4954-b7fd-3dedde5a0001','0001',CURRENT_TIMESTAMP),
('dee0891b-a309-4954-b7fd-3dedde5a0002','0002',CURRENT_TIMESTAMP),
('dee0891b-a309-4954-b7fd-3dedde5a0003','0003',CURRENT_TIMESTAMP);

INSERT INTO income (id,shipment_id,income_value,inserted_on) VALUES
('dee0891b-a000-0000-b000-3dedde5a0001','dee0891b-a309-4954-b7fd-3dedde5a0001',2000,CURRENT_TIMESTAMP),
('dee0891b-a000-0000-b000-3dedde5a0002','dee0891b-a309-4954-b7fd-3dedde5a0001',2000,CURRENT_TIMESTAMP);


INSERT INTO cost (id,shipment_id,Cost_value,Cost_Type_id,inserted_on) VALUES
('dee0891b-a000-0000-b000-3dedde5a0011','dee0891b-a309-4954-b7fd-3dedde5a0001',500.00,1,CURRENT_TIMESTAMP),
('dee0891b-a000-0000-b000-3dedde5a0021','dee0891b-a309-4954-b7fd-3dedde5a0001',200.00,2,CURRENT_TIMESTAMP),
('dee0891b-a000-0000-b000-3dedde5a0031','dee0891b-a309-4954-b7fd-3dedde5a0001',50.00, 1,CURRENT_TIMESTAMP);

INSERT INTO profit_calculation (id,shipment_id,income,cost,profit,inserted_on) VALUES
('dee0891b-a000-0000-b000-3dedde5a0111', 'dee0891b-a309-4954-b7fd-3dedde5a0001',200.00,50.00,   150.00,  CURRENT_TIMESTAMP),
('e1e1e1e1-0000-0000-0000-000000000002', 'dee0891b-a309-4954-b7fd-3dedde5a0003',500.00,900.00,  -400.00, CURRENT_TIMESTAMP);
