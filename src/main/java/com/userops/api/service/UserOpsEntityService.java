package com.userops.api.service;

import com.userops.api.model.AdminRole;
import com.userops.api.model.UserOpsEntity;
import java.util.List;

public interface UserOpsEntityService {

    UserOpsEntity getUserOpsEntity(String userId, String companyId);

    UserOpsEntity createOrUpdateUserOpsEntity(String userId, String companyId, String opsEntityId, UserOpsEntity userOpsEntity);

    List<UserOpsEntity> getAllUserOpsEntities(Long personnelId, String companyId);

    UserOpsEntity updateUserOpsEntityAdminRole(Long personnelId, String companyId, AdminRole adminRole);
}
