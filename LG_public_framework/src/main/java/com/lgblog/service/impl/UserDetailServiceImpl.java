package com.lgblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lgblog.dao.MenuDao;
import com.lgblog.statement.statementVal;
import com.lgblog.dao.UserDao;
import com.lgblog.entity.LoginUser;
import com.lgblog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserDao dao;

    @Autowired
    private MenuDao menuDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,username);
        User user = dao.selectOne(wrapper);
        //判断是否查询到用户，没有查询到抛出异常
        if (Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //返回用户信息
        //获取用户类型(权限) 如果是管理员 则进行管理员相关的操作
        if( statementVal.ADMIN_TYPE.equals(user.getType()))
        {
            List<String> perms = menuDao.getPermsByUserType(user.getId());
            return new LoginUser(user,perms);
        }
        return new LoginUser(user,null);
    }
}
