package com.userops.api.service;

import com.userops.api.model.AdminRole;
import com.userops.api.model.UserOpsEntity;
import com.userops.api.repository.UserOpsEntityDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserOpsEntityServiceImpl implements UserOpsEntityService {

    private final UserOpsEntityDao userOpsEntityDao;

    public UserOpsEntityServiceImpl(UserOpsEntityDao userOpsEntityDao) {
        this.userOpsEntityDao = userOpsEntityDao;
    }

    @Override
    @Transactional(readOnly = true)
    public UserOpsEntity getUserOpsEntity(Long personnelId, String companyId) {
        return userOpsEntityDao.getUserOpsEntity(personnelId, companyId);
    }

    @Override
    public UserOpsEntity updateUserOpsEntityAdminRole(Long personnelId, String companyId, AdminRole adminRole) {
        return userOpsEntityDao.updateOpsEntityAdminRole(personnelId, companyId, adminRole);
    }
}
