package com.userops.api.repository;

import com.userops.api.model.CustomerInventoryGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JooqCustomerInventoryGroupDao implements CustomerInventoryGroupDao {

    private final JdbcTemplate jdbcTemplate;
    
    @Value("${inventory.sql.getCustomerInventoryGroup}")
    private String getCustomerInventoryGroupSql;
    
    @Value("${inventory.sql.updateCustomerInventoryGroup}")
    private String updateCustomerInventoryGroupSql;

    public JooqCustomerInventoryGroupDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CustomerInventoryGroup getCustomerInventoryGroup(final String companyId, final String inventoryGroup, final String stockingMethod) {
        try {
            return jdbcTemplate.queryForObject(getCustomerInventoryGroupSql, new CustomerInventoryGroupRowMapper(), companyId, inventoryGroup, stockingMethod);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public CustomerInventoryGroup updateCustomerInventoryGroup(
        final String companyId,
        final String inventoryGroup,
        final String stockingMethod,
        final CustomerInventoryGroup entity
    ) {
        int updated = jdbcTemplate.update(updateCustomerInventoryGroupSql, 
            entity.getMinShelfLife(),
            entity.getMinShelfLifeMethod(),
            entity.getSourceHub(),
            entity.getDropship(),
            entity.getRelaxShelfLife(),
            entity.getNotused1(),
            entity.getShortShelfLifeDays(),
            companyId, 
            inventoryGroup, 
            stockingMethod);
        
        if (updated > 0) {
            return getCustomerInventoryGroup(companyId, inventoryGroup, stockingMethod);
        }
        return null;
    }

    private static class CustomerInventoryGroupRowMapper implements RowMapper<CustomerInventoryGroup> {
        @Override
        public CustomerInventoryGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
            CustomerInventoryGroup entity = new CustomerInventoryGroup();
            entity.setCompanyId(rs.getString("company_id"));
            entity.setInventoryGroup(rs.getString("inventory_group"));
            entity.setStockingMethod(rs.getString("stocking_method"));
            entity.setMinShelfLife(rs.getObject("min_shelf_life", Integer.class));
            entity.setMinShelfLifeMethod(rs.getString("min_shelf_life_method"));
            entity.setSourceHub(rs.getString("source_hub"));
            entity.setDropship(rs.getString("dropship"));
            entity.setRelaxShelfLife(rs.getObject("relax_shelf_life", Integer.class));
            entity.setNotused1(rs.getString("notused1"));
            entity.setShortShelfLifeDays(rs.getObject("short_shelf_life_days", Integer.class));
            
            return entity;
        }
    }
}
