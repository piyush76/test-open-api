package com.userops.api.service;

import com.userops.api.model.AdminRole;
import com.userops.api.model.UserOpsEntity;
import com.userops.api.repository.UserOpsEntityDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class UserOpsEntityServiceImpl implements UserOpsEntityService {

    private final UserOpsEntityDao userOpsEntityDao;

    public UserOpsEntityServiceImpl(UserOpsEntityDao userOpsEntityDao) {
        this.userOpsEntityDao = userOpsEntityDao;
    }

    @Override
    @Transactional(readOnly = true)
    public UserOpsEntity getUserOpsEntity(String userId, String companyId) {
        Long personnelId = Long.parseLong(userId);
        List<UserOpsEntity> entities = userOpsEntityDao.getUserOpsEntity(personnelId, companyId);
        return entities.isEmpty() ? null : entities.get(0);
    }

    @Override
    public UserOpsEntity createOrUpdateUserOpsEntity(String userId, String companyId, String opsEntityId, UserOpsEntity userOpsEntity) {
        Long personnelId = Long.parseLong(userId);
        userOpsEntity.setPersonnelId(personnelId);
        userOpsEntity.setCompanyId(companyId);
        userOpsEntity.setOpsEntityId(opsEntityId);
        return userOpsEntityDao.saveUserOpsEntity(userOpsEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserOpsEntity> getAllUserOpsEntities(Long personnelId, String companyId) {
        return userOpsEntityDao.getUserOpsEntity(personnelId, companyId);
    }

    @Override
    public UserOpsEntity updateUserOpsEntityAdminRole(Long personnelId, String companyId, AdminRole adminRole) {
        return userOpsEntityDao.updateOpsEntityAdminRole(personnelId, companyId, adminRole);
    }
}
