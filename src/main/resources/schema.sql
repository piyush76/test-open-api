CREATE TABLE IF NOT EXISTS user_ops_entity (
    personnel_id BIGINT NOT NULL,
    company_id VARCHAR(255) NOT NULL,
    ops_entity_id VARCHAR(255),
    ops_company_id VARCHAR(255),
    admin_role VARCHAR(50),
    PRIMARY KEY (personnel_id, company_id)
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

CREATE TABLE IF NOT EXISTS user_inventory_group (
    company_id VARCHAR(255) NOT NULL,
    personnel_id BIGINT NOT NULL,
    inventory_group VARCHAR(255) NOT NULL,
    hub VARCHAR(255),
    ops_entity_id VARCHAR(255),
    ops_company_id VARCHAR(255),
    admin_role VARCHAR(50),
    PRIMARY KEY (company_id, personnel_id, inventory_group)
);

INSERT INTO user_inventory_group (company_id, personnel_id, inventory_group, hub, ops_entity_id, ops_company_id, admin_role) 
SELECT * FROM (VALUES 
('HostCompany', 3, 'ELECTRONICS', 'HUB001', 'TestOpsEntity', 'TestOperatingCompany', 'Grant Admin'),
('HostCompany', 2, 'FOOD', 'HUB002', 'TestOpsEntity', 'TestOperatingCompany', 'Admin'),
('COMP1', 1, 'GENERAL', 'HUB003', 'OPS1', 'OPSCOMP1', 'Admin')
) AS tmp(company_id, personnel_id, inventory_group, hub, ops_entity_id, ops_company_id, admin_role)
WHERE NOT EXISTS (
    SELECT 1 FROM user_inventory_group 
    WHERE user_inventory_group.company_id = tmp.company_id 
    AND user_inventory_group.personnel_id = tmp.personnel_id
    AND user_inventory_group.inventory_group = tmp.inventory_group
);
