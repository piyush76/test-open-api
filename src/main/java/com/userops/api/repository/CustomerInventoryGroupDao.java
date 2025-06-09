package com.userops.api.repository;

import com.userops.api.model.CustomerInventoryGroup;

public interface CustomerInventoryGroupDao {

    CustomerInventoryGroup getCustomerInventoryGroup(String companyId, String inventoryGroup, String stockingMethod);

    CustomerInventoryGroup updateCustomerInventoryGroup(String companyId, String inventoryGroup, String stockingMethod, CustomerInventoryGroup entity);
}
