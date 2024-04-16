package com.lgblog.controller;

import com.lgblog.entity.LoginUser;
import com.lgblog.entity.Menu;
import com.lgblog.entity.User;
import com.lgblog.entity.vo.adminVo.AdminUserVo;
import com.lgblog.entity.vo.adminVo.RoutersVo;
import com.lgblog.entity.vo.UserInfoVo;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.AdminLoginService;
import com.lgblog.service.MenuService;
import com.lgblog.service.RoleService;
import com.lgblog.util.SecurityUtil.SecurityUtil;
import com.lgblog.util.copyBeanUtil.BeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class adminLoginController {
    @Autowired
    private AdminLoginService  adminLoginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @PostMapping("/user/login")
    public ResponseResult adminLogin(@RequestBody User user)
    {
        return adminLoginService.login(user);
    }

    /**
     * 该方法是对用户所操控的控件的数据封装
     * @return
     */
    @GetMapping("getInfo")
    public ResponseResult getInfo()
    {
        LoginUser loginUser= SecurityUtil.getLoginUser();
        Long id = loginUser.getUser().getId();
        //根据用户查询权限信息
        List<String> perms=menuService.getPermsForAdminUser(id);
        //根据用户id查询角色信息
        List<String> roleKeys=roleService.getRoleKeyForAdminUser(id);
        User user=loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopy.copyByBean(user, UserInfoVo.class);
        //封装
        AdminUserVo adminUserVo=new AdminUserVo(perms,roleKeys,userInfoVo);
        return ResponseResult.okResult(adminUserVo);
    }
    @GetMapping("getRouters")
    public ResponseResult getRouters()
    {
        //获取用户id
        LoginUser loginUser= SecurityUtil.getLoginUser();
        Long id = loginUser.getUser().getId();
        List<Menu> routers=menuService.getRoutersForAdminUser(id);
        RoutersVo routersVo=new RoutersVo(routers);
        return ResponseResult.okResult(routersVo);
    }

    /**
     * 管理员退出登录接口
     * @return
     */
    @PostMapping("/user/logout")
    public ResponseResult logout()
    {
        return adminLoginService.logout();
    }
}
