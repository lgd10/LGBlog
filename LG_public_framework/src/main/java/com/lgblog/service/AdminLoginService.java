package com.lgblog.service;

import com.lgblog.entity.User;
import com.lgblog.result.ResponseResult;

public interface AdminLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
