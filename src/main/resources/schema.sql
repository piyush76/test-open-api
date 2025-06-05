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
(1, 'COMP1', 'OPS1', 'OPSCOMP1', 'Admin'),
(2, 'COMP1', 'OPS2', 'OPSCOMP1', 'Grant Admin'),
(1, 'COMP2', 'OPS3', 'OPSCOMP2', 'Admin')
) AS tmp(personnel_id, company_id, ops_entity_id, ops_company_id, admin_role)
WHERE NOT EXISTS (
    SELECT 1 FROM user_ops_entity 
    WHERE user_ops_entity.personnel_id = tmp.personnel_id 
    AND user_ops_entity.company_id = tmp.company_id
);
