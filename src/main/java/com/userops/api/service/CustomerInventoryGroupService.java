package com.userops.api.service;

import com.userops.api.model.CustomerInventoryGroup;

public interface CustomerInventoryGroupService {

    CustomerInventoryGroup getCustomerInventoryGroup(String companyId, String inventoryGroup, String stockingMethod);

    CustomerInventoryGroup updateCustomerInventoryGroup(String companyId, String inventoryGroup, String stockingMethod, CustomerInventoryGroup entity);
}
