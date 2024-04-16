package com.lgblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lgblog.MyException.SystemException;
import com.lgblog.entity.LoginUser;
import com.lgblog.entity.Role;
import com.lgblog.entity.SysUserRole;
import com.lgblog.entity.dto.UserAndRoleDto;
import com.lgblog.entity.dto.UserDto;
import com.lgblog.entity.dto.UserStatusDto;
import com.lgblog.entity.vo.PageVo;
import com.lgblog.entity.vo.adminVo.UpdateUserDataBackVo;
import com.lgblog.entity.vo.adminVo.UserListVo;
import com.lgblog.entity.vo.adminVo.UserVo;
import com.lgblog.result.AppHttpCodeEnum;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.RoleService;
import com.lgblog.service.SysUserRoleService;
import com.lgblog.service.UserService;
import com.lgblog.util.JWTUtil.JwtUtil;
import com.lgblog.util.SecurityUtil.SecurityUtil;
import com.lgblog.util.copyBeanUtil.BeanCopy;
import com.lgblog.util.redisUtil.RedisCache;
import com.lgblog.entity.vo.LoginUserVo;
import com.lgblog.entity.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.lgblog.entity.User;
import com.lgblog.dao.UserDao;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author gyd
 * @since 2023-11-30 14:12:15
 */

/**
 * 登录步骤
 * 1、自定义登录接口
 * 调用providerManager的方法进行认证，如果认证通过生成jwt把用户信息存入redis中
 * 2、自定义UserDetailService
 * 在这个实现类中去查询数据库
 * 注意配置passwordEncoder为BcryptPasswordEncoder
 *
 * 校验：
 * 	①定义Jwt认证过滤器
 * 				获取token
 * 				解析token获取其中的userid
 * 				从redis中获取用户信息
 * 				存入SecurityContextHolder
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private RoleService roleService;
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
        redisCache.setCacheObject("bloglogin:"+id,loginUser);
        //把token和userinfo封装返回
        UserInfoVo userInfoVo = BeanCopy.copyByBean(loginUser.getUser(), UserInfoVo.class);
        LoginUserVo loginUserVo=new LoginUserVo(jwt,userInfoVo);
        return ResponseResult.okResult(loginUserVo);
    }


    /**
     * 退出登录
     * 找到redis中存储的相应的用户的信息缓存，删除即可
     */
    @Override
    public ResponseResult logout() {
        //获取token 解析获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userId
        Long userId=loginUser.getUser().getId();
        //删除redis中的信息
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }

    /**
     * 获取用户信息(个人中心功能的实现)
     * @return
     */
    @Override
    public ResponseResult userInfo() {
        //获取用户id
        Long userId = SecurityUtil.getUserId();
        //根据id查询用户信息并封装
        User user = getById(userId);
        UserInfoVo userInfoVo = BeanCopy.copyByBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    /**
     * 更新用户信息
     * @return 返回值是一个封装的对象
     */
    @Override
    public ResponseResult updateUserInfo(User user) {
        /**
         * 这样写有一个弊端，就是前端传递过来的修改值不知道是不是用户传递的--假设有个黑客他伪造了一个请求体的json数据，
         * 里面包含了password这个字段，那么这个简单的update方法将直接把黑客提供的用户数据的密码实施篡改
         */
        updateById(user);
        return ResponseResult.okResult();
    }

    /**
     * 注册功能的实现
     * @param user 前端传递的注册用户
     * @return
     */
    @Override
    public ResponseResult register(User user) {
        //非空判断
        ElementNullAndExit(user);
        //对明文密码进行加密
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult UserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(userName),User::getUserName,userName);
        wrapper.eq(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        wrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        List<User> userList = list(wrapper);
        List<UserListVo> userListVos = BeanCopy.copyListByBean(userList, UserListVo.class);
        Page<User> userPage=new Page<>(pageNum,pageSize);
        page(userPage,wrapper);
        PageVo pageVo=new PageVo(userListVos,userPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addUser(UserDto userDto) {
        User user = BeanCopy.copyByBean(userDto, User.class);
        //判断字段是否为空，字段是否异常 并且回显异常
        ElementNullAndExit(user);
        //向数据库中插入用户信息
        save(user);
        Long userId = user.getId();
        //向数据库中插入用户以及关联的角色信息
        List<SysUserRole> sysUserRoles = userDto.getRoleIds()
                .stream()
                .map(roleId -> new SysUserRole(userId, roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(sysUserRoles);

        /**
         *  userDto.getRoleIds()
         *                 .stream()
         *                 .map(new Function<Long, SysUserRole>() {
         *                     @Override
         *                     public SysUserRole apply(Long roleId) {
         *                         return new SysUserRole(userId,roleId);
         *                     }
         *                 }).collect(Collectors.toList());
         */
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUser(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult UserDataBack(Long id) {
        //分别对返回数据的类进行数据查询
        //先进行对用户数据的查询和封装
        User user = getById(id);
        UserVo userVo = BeanCopy.copyByBean(user, UserVo.class);
        //再将所有的角色信息查询出来
        List<Role> roles = roleService.list();
        //再获取角色和用户关系表的信息
        LambdaQueryWrapper<SysUserRole> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,id);
        List<Long> roleIds = userRoleService.list(wrapper)
                .stream()
                .map(sysUserRole -> sysUserRole.getRoleId())
                .collect(Collectors.toList());
        /**
         * List<Long> roleIds = userRoleService.list(wrapper).stream()
         *                 .map(new Function<SysUserRole, Long>() {
         *                     @Override
         *                     public Long apply(SysUserRole sysUserRole) {
         *                         return sysUserRole.getRoleId();
         *                     }
         *                 }).collect(Collectors.toList());
         */
        //构造总的封装类
        UpdateUserDataBackVo vo=new UpdateUserDataBackVo(roleIds,roles,userVo);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult subUserData(UserAndRoleDto dto) {
        User user = BeanCopy.copyByBean(dto, User.class);
        System.out.println("=======执行上传任务，获取电话号码信息========"+dto.getPhonenumber());
        updateById(user);
        //先删除有关该用户的角色信息
        userRoleService.removeById(user.getId());
        //再存入用户和角色信息
        List<SysUserRole> sysUserRoles = dto.getRoleIds().stream()
                .map(roleId -> new SysUserRole(dto.getId(), roleId))
                .collect(Collectors.toList());
        /**
         * List<SysUserRole> sysUserRoles = dto.getRoleIds().stream()
         *                 .map(new Function<Long, SysUserRole>() {
         *                     @Override
         *                     public SysUserRole apply(Long roleId) {
         *                         return new SysUserRole(dto.getId(), roleId);
         *                     }
         *                 })
         *                 .collect(Collectors.toList());
         */
        userRoleService.saveBatch(sysUserRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(UserStatusDto dto) {
        LambdaUpdateWrapper<User> wrapper=new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId,dto.getUserId());
        wrapper.set(User::getStatus,dto.getStatus());
        update(wrapper);
        return ResponseResult.okResult();
    }


    /**
     * //存在判断(比如判断用户名是否已经存在)
     * @param element 去数据库中寻找是否存在该值
     * @return
     */
    private boolean elementsExist(String element,int code)
    {
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper();
        if (code==AppHttpCodeEnum.USERNAME_EXIST.getCode())
        {
            wrapper.eq(User::getUserName,element);
        }
        if(code==AppHttpCodeEnum.EMAIL_EXIST.getCode())
        {
            wrapper.eq(User::getEmail,element);
        }
        if(code==AppHttpCodeEnum.PHONENUMBER_EXIST.getCode())
        {
            wrapper.eq(User::getPhonenumber,element);
        }
        Integer ct = count(wrapper);
        return ct>0;
    }

    /**
     * 判断传入的用户必须为非空的字段值 以及用户是否存在 以及密码的加密和上传
     * @param user 传入的用户信息
     */
    public void ElementNullAndExit(User user)
    {
        if(!StringUtils.hasText(user.getUserName()))
        {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword()))
        {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail()))
        {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName()))
        {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //存在判断(比如判断用户名是否已经存在)
        if (elementsExist(user.getUserName(),AppHttpCodeEnum.USERNAME_EXIST.getCode()))
        {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (elementsExist(user.getEmail(),AppHttpCodeEnum.EMAIL_EXIST.getCode()))
        {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        if (elementsExist(user.getPhonenumber(),AppHttpCodeEnum.PHONENUMBER_EXIST.getCode()))
        {
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        String encodingPassword = passwordEncoder.encode(user.getPassword());//加密过后的密码
        user.setPassword(encodingPassword);//将加密过后的密码存入数据库
    }
}
