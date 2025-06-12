package com.userops.api.service;

import com.userops.api.model.AdminRole;
import com.userops.api.model.User;
import com.userops.api.model.UserGroupBy;
import com.userops.api.repository.UserDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUserContext() {
        User user = new User();
        user.setPersonnelId(3L);
        user.setCompanyId("HostCompany");
        user.setUserType("Customer");
        user.setWorkContext("Facility");
        user.setContextId("FAC001");
        user.setAdminRole(AdminRole.ADMIN);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGroupBy> getUsersByRole(String role) {
        return userDao.getUsersByRole(role);
    }
}
