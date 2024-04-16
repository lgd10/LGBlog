package com.lgblog.controller;

import com.lgblog.MyException.SystemException;
import com.lgblog.entity.User;
import com.lgblog.result.AppHttpCodeEnum;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.lgblog.annotation.mySystemlog;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user)
    {
        //userName如果为空
        if(!StringUtils.hasText(user.getUserName())){//StringUtils.hasText(user.getUserName()) 如果userName是空则返回false
            //抛出异常 使用自定义异常类
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return  userService.login(user);
    }
    @PostMapping("/logout")
    public ResponseResult logout()
    {
        return userService.logout();
    }
    @GetMapping("/user/userInfo")
    public ResponseResult userInfo()
    {
        return userService.userInfo();
    }

    /**
     * @RequestBody 注解通常用于在后端控制器中接收 HTTP 请求中的请求体（body）部分，
     * 并将其转换成需要的对象。比如，当客户端以 JSON 格式向后端发送请求时，
     * 我们可以使用 @RequestBody 将 JSON 中的数据自动转换成 Java 对象
     * @param user 这是前端传过来的数据 使用user类去接收前端的数据
     * @return
     */
    @PutMapping("user/userInfo")
    @mySystemlog(businessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user)
    {
        return userService.updateUserInfo(user);
    }

    @PostMapping("user/register")
    public ResponseResult register(@RequestBody User user)
    {
        return userService.register(user);
    }
}
