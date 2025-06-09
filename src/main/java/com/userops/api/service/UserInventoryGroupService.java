package com.userops.api.service;

import com.userops.api.model.AdminRole;
import com.userops.api.model.UserInventoryGroup;

public interface UserInventoryGroupService {

    UserInventoryGroup getUserInventoryGroup(String companyId, Long personnelId, String inventoryGroup);

    UserInventoryGroup updateUserInventoryGroupAdminRole(String companyId, Long personnelId, String inventoryGroup, AdminRole adminRole);
}
