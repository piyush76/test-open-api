CREATE TABLE IF NOT EXISTS user_ops_entity (
    personnel_id BIGINT NOT NULL,
    company_id VARCHAR(255) NOT NULL,
    ops_entity_id VARCHAR(255),
    ops_company_id VARCHAR(255),
    admin_role VARCHAR(50),
    PRIMARY KEY (personnel_id, company_id)
);

CREATE TABLE IF NOT EXISTS customer_inventory_group (
    company_id VARCHAR(30) NOT NULL,
    inventory_group VARCHAR(30) NOT NULL,
    stocking_method VARCHAR(8) NOT NULL,
    min_shelf_life INTEGER,
    min_shelf_life_method VARCHAR(8),
    source_hub VARCHAR(12),
    dropship VARCHAR(1),
    relax_shelf_life INTEGER,
    notused1 VARCHAR(30),
    short_shelf_life_days INTEGER,
    PRIMARY KEY (company_id, inventory_group, stocking_method)
);

INSERT INTO user_ops_entity (personnel_id, company_id, ops_entity_id, ops_company_id, admin_role) 
SELECT * FROM (VALUES 
(3, 'HostCompany', 'TestOpsEntity', 'TestOperatingCompany', 'Grant Admin'),
(2, 'HostCompany', 'TestOpsEntity', 'TestOperatingCompany', 'Grant Admin'),
(1, 'COMP1', 'OPS1', 'OPSCOMP1', 'Admin')
) AS tmp(personnel_id, company_id, ops_entity_id, ops_company_id, admin_role)
WHERE NOT EXISTS (
    SELECT 1 FROM user_ops_entity 
    WHERE user_ops_entity.personnel_id = tmp.personnel_id 
    AND user_ops_entity.company_id = tmp.company_id
);

INSERT INTO customer_inventory_group (company_id, inventory_group, stocking_method, min_shelf_life, min_shelf_life_method, source_hub, dropship, relax_shelf_life, notused1, short_shelf_life_days) 
SELECT * FROM (VALUES 
('HostCompany', 'ELECTRONICS', 'FIFO', 30, 'DAYS', 'HUB001', 'N', 7, NULL, 5),
('HostCompany', 'FOOD', 'FEFO', 14, 'DAYS', 'HUB002', 'Y', 3, NULL, 2),
('COMP1', 'GENERAL', 'LIFO', 60, 'DAYS', 'HUB003', 'N', 10, NULL, 7)
) AS tmp(company_id, inventory_group, stocking_method, min_shelf_life, min_shelf_life_method, source_hub, dropship, relax_shelf_life, notused1, short_shelf_life_days)
WHERE NOT EXISTS (
    SELECT 1 FROM customer_inventory_group 
    WHERE customer_inventory_group.company_id = tmp.company_id 
    AND customer_inventory_group.inventory_group = tmp.inventory_group
    AND customer_inventory_group.stocking_method = tmp.stocking_method
);
