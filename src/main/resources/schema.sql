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

CREATE TABLE IF NOT EXISTS user_pages (
    user_id VARCHAR(255) NOT NULL,
    company_id VARCHAR(255) NOT NULL,
    page_id VARCHAR(255) NOT NULL,
    page_name VARCHAR(255),
    access_level VARCHAR(50),
    page_url VARCHAR(500),
    PRIMARY KEY (user_id, company_id, page_id)
);

INSERT INTO user_pages (user_id, company_id, page_id, page_name, access_level, page_url) 
SELECT * FROM (VALUES 
('3', 'HostCompany', 'dashboard', 'Dashboard', 'READ_WRITE', '/dashboard'),
('3', 'HostCompany', 'reports', 'Reports', 'READ_ONLY', '/reports'),
('2', 'HostCompany', 'reports', 'Reports', 'READ_ONLY', '/reports'),
('2', 'HostCompany', 'inventory', 'Inventory', 'READ_WRITE', '/inventory'),
('1', 'COMP1', 'admin', 'Admin Panel', 'READ_WRITE', '/admin'),
('1', 'COMP1', 'dashboard', 'Dashboard', 'READ_WRITE', '/dashboard')
) AS tmp(user_id, company_id, page_id, page_name, access_level, page_url)
WHERE NOT EXISTS (
    SELECT 1 FROM user_pages 
    WHERE user_pages.user_id = tmp.user_id 
    AND user_pages.company_id = tmp.company_id
    AND user_pages.page_id = tmp.page_id
);
