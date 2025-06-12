package com.incora.api.iam.repository;

import com.incora.api.iam.model.UserOpsEntity;

import java.util.List;
import java.util.Optional;

public interface UserOpsEntityDao {
    
    List<UserOpsEntity> findAll();
    
    Optional<UserOpsEntity> findById(Long personnelId, String companyId, String opsEntityId);
    
    UserOpsEntity save(UserOpsEntity userOpsEntity);
    
    void deleteById(Long personnelId, String companyId, String opsEntityId);
    
    boolean existsById(Long personnelId, String companyId, String opsEntityId);
}
