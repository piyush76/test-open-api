package com.userops.api.repository;

import com.userops.api.model.User;
import com.userops.api.model.UserGroupBy;
import java.util.List;

public interface UserDao {

    User getCurrentUser();

    List<UserGroupBy> getUsersByRole(String role);
}
