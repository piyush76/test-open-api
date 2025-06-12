package com.userops.api.service;

import com.userops.api.model.User;
import com.userops.api.model.UserGroupBy;
import java.util.List;

public interface UserService {

    User getCurrentUserContext();

    List<UserGroupBy> getUsersByRole(String role);
}
