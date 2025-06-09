package com.userops.api.service;

import com.userops.api.model.AdminRole;
import com.userops.api.model.UserInventoryGroup;
import com.userops.api.repository.UserInventoryGroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserInventoryGroupServiceImpl implements UserInventoryGroupService {

    private final UserInventoryGroupDao userInventoryGroupDao;

    public UserInventoryGroupServiceImpl(UserInventoryGroupDao userInventoryGroupDao) {
        this.userInventoryGroupDao = userInventoryGroupDao;
    }

    @Override
    @Transactional(readOnly = true)
    public UserInventoryGroup getUserInventoryGroup(String companyId, Long personnelId, String inventoryGroup) {
        return userInventoryGroupDao.getUserInventoryGroup(companyId, personnelId, inventoryGroup);
    }

    @Override
    public UserInventoryGroup updateUserInventoryGroupAdminRole(String companyId, Long personnelId, String inventoryGroup, AdminRole adminRole) {
        return userInventoryGroupDao.updateUserInventoryGroupAdminRole(companyId, personnelId, inventoryGroup, adminRole);
    }
}
