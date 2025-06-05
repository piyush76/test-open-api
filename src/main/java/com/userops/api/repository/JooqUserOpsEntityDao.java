package com.userops.api.repository;

import com.userops.api.model.AdminRole;
import com.userops.api.model.UserOpsEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JooqUserOpsEntityDao implements UserOpsEntityDao {

    private final JdbcTemplate jdbcTemplate;
    
    @Value("${userops.sql.getUserOpsEntity}")
    private String getUserOpsEntitySql;
    
    @Value("${userops.sql.updateOpsEntityAdminRole}")
    private String updateOpsEntityAdminRoleSql;

    public JooqUserOpsEntityDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserOpsEntity getUserOpsEntity(final Long personnelId, final String companyId) {
        try {
            return jdbcTemplate.queryForObject(getUserOpsEntitySql, new UserOpsEntityRowMapper(), personnelId, companyId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserOpsEntity updateOpsEntityAdminRole(
        final Long personnelId,
        final String companyId,
        final AdminRole adminRole
    ) {
        int updated = jdbcTemplate.update(updateOpsEntityAdminRoleSql, adminRole.getValue(), personnelId, companyId);
        
        if (updated > 0) {
            return getUserOpsEntity(personnelId, companyId);
        }
        return null;
    }

    private static class UserOpsEntityRowMapper implements RowMapper<UserOpsEntity> {
        @Override
        public UserOpsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserOpsEntity entity = new UserOpsEntity();
            entity.setPersonnelId(rs.getLong("personnel_id"));
            entity.setCompanyId(rs.getString("company_id"));
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
