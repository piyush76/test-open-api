package com.userops.api.service;

import com.userops.api.model.UserPage;
import com.userops.api.repository.UserPageDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class UserPageServiceImpl implements UserPageService {

    private final UserPageDao userPageDao;

    public UserPageServiceImpl(UserPageDao userPageDao) {
        this.userPageDao = userPageDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserPage> getUserPages(String userId, String companyId) {
        return userPageDao.getUserPages(userId, companyId);
    }

    @Override
    public UserPage createOrUpdateUserPage(String userId, String companyId, String pageId, UserPage userPage) {
        userPage.setUserId(userId);
        userPage.setCompanyId(companyId);
        userPage.setPageId(pageId);
        return userPageDao.saveUserPage(userPage);
    }

    @Override
    public void deleteUserPage(String userId, String companyId, String pageId) {
        userPageDao.deleteUserPage(userId, companyId, pageId);
    }
}
