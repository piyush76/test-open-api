package com.userops.api.repository;

import com.userops.api.model.AdminRole;
import com.userops.api.model.User;
import com.userops.api.model.UserGroupBy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class JooqUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public JooqUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getCurrentUser() {
        User user = new User();
        user.setPersonnelId(3L);
        user.setCompanyId("HostCompany");
        user.setUserType("Customer");
        user.setWorkContext("Facility");
        user.setContextId("FAC001");
        user.setAdminRole(AdminRole.ADMIN);
        return user;
    }

    @Override
    public List<UserGroupBy> getUsersByRole(String role) {
        String sql = "SELECT personnel_id, company_id, ops_entity_id, ops_company_id, admin_role " +
                    "FROM user_ops_entity WHERE admin_role = ?";
        
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), role);
        
        Map<String, List<User>> groupedUsers = users.stream()
            .collect(Collectors.groupingBy(user -> user.getAdminRole().getValue()));
        
        return groupedUsers.entrySet().stream()
            .map(entry -> {
                UserGroupBy group = new UserGroupBy();
                group.setRole(entry.getKey());
                group.setUsers(entry.getValue());
                group.setCount(entry.getValue().size());
                return group;
            })
            .collect(Collectors.toList());
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setPersonnelId(rs.getLong("personnel_id"));
            user.setCompanyId(rs.getString("company_id"));
            user.setUserType("Customer");
            user.setWorkContext("Facility");
            user.setContextId(rs.getString("ops_entity_id"));
            
            String adminRoleValue = rs.getString("admin_role");
            if (adminRoleValue != null) {
                user.setAdminRole(AdminRole.fromValue(adminRoleValue));
            }
            
            return user;
        }
    }
}
