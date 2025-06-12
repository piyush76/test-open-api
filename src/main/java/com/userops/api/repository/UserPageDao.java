package com.userops.api.repository;

import com.userops.api.model.UserPage;
import java.util.List;

public interface UserPageDao {

    List<UserPage> getUserPages(String userId, String companyId);

    UserPage saveUserPage(UserPage userPage);

    void deleteUserPage(String userId, String companyId, String pageId);

    boolean existsUserPage(String userId, String companyId, String pageId);
}
