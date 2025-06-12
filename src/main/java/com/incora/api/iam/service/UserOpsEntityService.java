package com.incora.api.iam.service;

import com.incora.api.iam.model.UserOpsEntity;

import java.util.List;

public interface UserOpsEntityService {
    
    List<UserOpsEntity> getAllUserOpsEntities();
    
    UserOpsEntity getUserOpsEntity(Long personnelId, String companyId, String opsEntityId);
    
    UserOpsEntity createUserOpsEntity(UserOpsEntity userOpsEntity);
    
    UserOpsEntity updateUserOpsEntity(Long personnelId, String companyId, String opsEntityId, UserOpsEntity userOpsEntity);
    
    void deleteUserOpsEntity(Long personnelId, String companyId, String opsEntityId);
}
