package com.incora.api.iam.service;

import com.incora.api.iam.exception.EntityNotFoundException;
import com.incora.api.iam.model.UserOpsEntity;
import com.incora.api.iam.repository.UserOpsEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserOpsEntityServiceImpl implements UserOpsEntityService {

    private final UserOpsEntityDao userOpsEntityDao;

    @Autowired
    public UserOpsEntityServiceImpl(UserOpsEntityDao userOpsEntityDao) {
        this.userOpsEntityDao = userOpsEntityDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserOpsEntity> getAllUserOpsEntities() {
        return userOpsEntityDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserOpsEntity getUserOpsEntity(Long personnelId, String companyId, String opsEntityId) {
        return userOpsEntityDao.findById(personnelId, companyId, opsEntityId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("UserOpsEntity not found with personnelId: %d, companyId: %s, opsEntityId: %s", 
                                personnelId, companyId, opsEntityId)));
    }

    @Override
    public UserOpsEntity createUserOpsEntity(UserOpsEntity userOpsEntity) {
        if (userOpsEntityDao.existsById(userOpsEntity.getPersonnelId(), 
                userOpsEntity.getCompanyId(), userOpsEntity.getOpsEntityId())) {
            throw new IllegalArgumentException(
                    String.format("UserOpsEntity already exists with personnelId: %d, companyId: %s, opsEntityId: %s", 
                            userOpsEntity.getPersonnelId(), userOpsEntity.getCompanyId(), userOpsEntity.getOpsEntityId()));
        }
        return userOpsEntityDao.save(userOpsEntity);
    }

    @Override
    public UserOpsEntity updateUserOpsEntity(Long personnelId, String companyId, String opsEntityId, UserOpsEntity userOpsEntity) {
        if (!userOpsEntityDao.existsById(personnelId, companyId, opsEntityId)) {
            throw new EntityNotFoundException(
                    String.format("UserOpsEntity not found with personnelId: %d, companyId: %s, opsEntityId: %s", 
                            personnelId, companyId, opsEntityId));
        }
        
        userOpsEntity.setPersonnelId(personnelId);
        userOpsEntity.setCompanyId(companyId);
        userOpsEntity.setOpsEntityId(opsEntityId);
        
        return userOpsEntityDao.save(userOpsEntity);
    }

    @Override
    public void deleteUserOpsEntity(Long personnelId, String companyId, String opsEntityId) {
        if (!userOpsEntityDao.existsById(personnelId, companyId, opsEntityId)) {
            throw new EntityNotFoundException(
                    String.format("UserOpsEntity not found with personnelId: %d, companyId: %s, opsEntityId: %s", 
                            personnelId, companyId, opsEntityId));
        }
        userOpsEntityDao.deleteById(personnelId, companyId, opsEntityId);
    }
}
