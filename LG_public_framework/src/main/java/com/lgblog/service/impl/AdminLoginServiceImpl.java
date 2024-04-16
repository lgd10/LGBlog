package com.lgblog.service.impl;

import com.lgblog.entity.LoginUser;
import com.lgblog.entity.User;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.AdminLoginService;
import com.lgblog.util.JWTUtil.JwtUtil;
import com.lgblog.util.SecurityUtil.SecurityUtil;
import com.lgblog.util.redisUtil.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 管理员登录接口实现层
 */
@Service
public class AdminLoginServiceImpl implements AdminLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(token);
        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String id = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(id);
        //把用户信息存入redis
        redisCache.setCacheObject("login:"+id,loginUser);
        //把token和userinfo封装返回
        Map<String ,String > adminUserMap=new HashMap<>();
        adminUserMap.put("token",jwt);
        return ResponseResult.okResult(adminUserMap);
    }

    /**
     * 管理员退出登录接口
     * @return
     */
    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtil.getUserId();
        redisCache.deleteObject("login"+userId);
        return ResponseResult.okResult();
    }
}
