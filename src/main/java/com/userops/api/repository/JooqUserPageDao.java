package com.userops.api.repository;

import com.userops.api.model.UserPage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JooqUserPageDao implements UserPageDao {

    private final JdbcTemplate jdbcTemplate;

    public JooqUserPageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<UserPage> getUserPages(String userId, String companyId) {
        String sql = "SELECT user_id, company_id, page_id, page_name, access_level, page_url " +
                    "FROM user_pages WHERE user_id = ? AND company_id = ?";
        return jdbcTemplate.query(sql, new UserPageRowMapper(), userId, companyId);
    }

    @Override
    public UserPage saveUserPage(UserPage userPage) {
        if (existsUserPage(userPage.getUserId(), userPage.getCompanyId(), userPage.getPageId())) {
            String updateSql = "UPDATE user_pages SET page_name = ?, access_level = ?, page_url = ? " +
                              "WHERE user_id = ? AND company_id = ? AND page_id = ?";
            jdbcTemplate.update(updateSql, 
                userPage.getPageName(), 
                userPage.getAccessLevel(), 
                userPage.getPageUrl(),
                userPage.getUserId(), 
                userPage.getCompanyId(), 
                userPage.getPageId());
        } else {
            String insertSql = "INSERT INTO user_pages (user_id, company_id, page_id, page_name, access_level, page_url) " +
                              "VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(insertSql, 
                userPage.getUserId(), 
                userPage.getCompanyId(), 
                userPage.getPageId(),
                userPage.getPageName(), 
                userPage.getAccessLevel(), 
                userPage.getPageUrl());
        }
        return userPage;
    }

    @Override
    public void deleteUserPage(String userId, String companyId, String pageId) {
        String sql = "DELETE FROM user_pages WHERE user_id = ? AND company_id = ? AND page_id = ?";
        jdbcTemplate.update(sql, userId, companyId, pageId);
    }

    @Override
    public boolean existsUserPage(String userId, String companyId, String pageId) {
        String sql = "SELECT COUNT(*) FROM user_pages WHERE user_id = ? AND company_id = ? AND page_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, companyId, pageId);
        return count != null && count > 0;
    }

    private static class UserPageRowMapper implements RowMapper<UserPage> {
        @Override
        public UserPage mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserPage userPage = new UserPage();
            userPage.setUserId(rs.getString("user_id"));
            userPage.setCompanyId(rs.getString("company_id"));
            userPage.setPageId(rs.getString("page_id"));
            userPage.setPageName(rs.getString("page_name"));
            userPage.setAccessLevel(rs.getString("access_level"));
            userPage.setPageUrl(rs.getString("page_url"));
            return userPage;
        }
    }
}
