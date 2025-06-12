package com.userops.api.service;

import com.userops.api.model.UserPage;
import java.util.List;

public interface UserPageService {

    List<UserPage> getUserPages(String userId, String companyId);

    UserPage createOrUpdateUserPage(String userId, String companyId, String pageId, UserPage userPage);

    void deleteUserPage(String userId, String companyId, String pageId);
}
