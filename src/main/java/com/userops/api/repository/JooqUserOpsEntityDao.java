package com.userops.api.repository;

import com.userops.api.model.AdminRole;
import com.userops.api.model.UserOpsEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public List<UserOpsEntity> getUserOpsEntity(final Long personnelId, final String companyId) {
        try {
            return jdbcTemplate.query(getUserOpsEntitySql, new UserOpsEntityRowMapper(), personnelId, companyId);
        } catch (Exception e) {
            return new ArrayList<>();
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
            List<UserOpsEntity> entities = getUserOpsEntity(personnelId, companyId);
            return entities.isEmpty() ? null : entities.get(0);
        }
        return null;
    }

    @Override
    public UserOpsEntity saveUserOpsEntity(UserOpsEntity userOpsEntity) {
        if (existsUserOpsEntity(userOpsEntity.getPersonnelId(), userOpsEntity.getCompanyId(), userOpsEntity.getOpsEntityId())) {
            String updateSql = "UPDATE user_ops_entity SET ops_company_id = ?, admin_role = ? " +
                              "WHERE personnel_id = ? AND company_id = ? AND ops_entity_id = ?";
            jdbcTemplate.update(updateSql, 
                userOpsEntity.getOpsCompanyId(),
                userOpsEntity.getAdminRole() != null ? userOpsEntity.getAdminRole().getValue() : null,
                userOpsEntity.getPersonnelId(), 
                userOpsEntity.getCompanyId(),
                userOpsEntity.getOpsEntityId());
        } else {
            String insertSql = "INSERT INTO user_ops_entity (personnel_id, company_id, ops_entity_id, ops_company_id, admin_role) " +
                              "VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(insertSql, 
                userOpsEntity.getPersonnelId(), 
                userOpsEntity.getCompanyId(),
                userOpsEntity.getOpsEntityId(),
                userOpsEntity.getOpsCompanyId(),
                userOpsEntity.getAdminRole() != null ? userOpsEntity.getAdminRole().getValue() : null);
        }
        return userOpsEntity;
    }

    @Override
    public boolean existsUserOpsEntity(Long personnelId, String companyId, String opsEntityId) {
        String sql = "SELECT COUNT(*) FROM user_ops_entity WHERE personnel_id = ? AND company_id = ? AND ops_entity_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, personnelId, companyId, opsEntityId);
        return count != null && count > 0;
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
