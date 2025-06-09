package com.userops.api.repository;

import com.userops.api.model.AdminRole;
import com.userops.api.model.UserInventoryGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JooqUserInventoryGroupDao implements UserInventoryGroupDao {

    private final JdbcTemplate jdbcTemplate;
    
    @Value("${userinventory.sql.getUserInventoryGroup}")
    private String getUserInventoryGroupSql;
    
    @Value("${userinventory.sql.updateUserInventoryGroupAdminRole}")
    private String updateUserInventoryGroupAdminRoleSql;

    public JooqUserInventoryGroupDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserInventoryGroup getUserInventoryGroup(final String companyId, final Long personnelId, final String inventoryGroup) {
        try {
            return jdbcTemplate.queryForObject(getUserInventoryGroupSql, new UserInventoryGroupRowMapper(), companyId, personnelId, inventoryGroup);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserInventoryGroup updateUserInventoryGroupAdminRole(
        final String companyId,
        final Long personnelId,
        final String inventoryGroup,
        final AdminRole adminRole
    ) {
        int updated = jdbcTemplate.update(updateUserInventoryGroupAdminRoleSql, adminRole.getValue(), companyId, personnelId, inventoryGroup);
        
        if (updated > 0) {
            return getUserInventoryGroup(companyId, personnelId, inventoryGroup);
        }
        return null;
    }

    private static class UserInventoryGroupRowMapper implements RowMapper<UserInventoryGroup> {
        @Override
        public UserInventoryGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserInventoryGroup entity = new UserInventoryGroup();
            entity.setCompanyId(rs.getString("company_id"));
            entity.setPersonnelId(rs.getLong("personnel_id"));
            entity.setInventoryGroup(rs.getString("inventory_group"));
            entity.setHub(rs.getString("hub"));
            entity.setOpsEntityId(rs.getString("ops_entity_id"));
            entity.setOpsCompanyId(rs.getString("ops_company_id"));
            
            String adminRoleValue = rs.getString("admin_role");
            if (adminRoleValue != null) {
                entity.setAdminRole(AdminRole.fromValue(adminRoleValue));
            }
            
            return entity;
        }
    }
}
