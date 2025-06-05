package com.userops.api.repository;

import com.userops.api.model.AdminRole;
import com.userops.api.model.UserOpsEntity;

public interface UserOpsEntityDao {

    UserOpsEntity getUserOpsEntity(Long personnelId, String companyId);

    UserOpsEntity updateOpsEntityAdminRole(Long personnelId, String companyId, AdminRole adminRole);
}
