package com.userops.api.service;

import com.userops.api.model.AdminRole;
import com.userops.api.model.UserOpsEntity;
import java.util.List;

public interface UserOpsEntityService {

    List<UserOpsEntity> getUserOpsEntity(Long personnelId, String companyId);

    UserOpsEntity updateUserOpsEntityAdminRole(Long personnelId, String companyId, AdminRole adminRole);
}
