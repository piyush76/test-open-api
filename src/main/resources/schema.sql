CREATE TABLE IF NOT EXISTS user_ops_entity (
    personnel_id BIGINT NOT NULL,
    company_id VARCHAR(255) NOT NULL,
    ops_entity_id VARCHAR(255),
    ops_company_id VARCHAR(255),
    admin_role VARCHAR(50),
    PRIMARY KEY (personnel_id, company_id)
);

INSERT INTO user_ops_entity (personnel_id, company_id, ops_entity_id, ops_company_id, admin_role) VALUES
(1, 'COMP1', 'OPS1', 'OPSCOMP1', 'Admin'),
(2, 'COMP1', 'OPS2', 'OPSCOMP1', 'Grant Admin'),
(1, 'COMP2', 'OPS3', 'OPSCOMP2', 'Admin');
