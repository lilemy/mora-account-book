package com.lilemy.moraaccountbook.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.PhoneUtil;
import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.constant.UserConstant;
import com.lilemy.moraaccountbook.exception.ErrorCode;
import com.lilemy.moraaccountbook.exception.ThrowUtils;
import com.lilemy.moraaccountbook.manager.oss.OssService;
import com.lilemy.moraaccountbook.mapper.UserMapper;
import com.lilemy.moraaccountbook.model.dto.user.UserLoginRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserQueryRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserRegisterRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserUpdateRequest;
import com.lilemy.moraaccountbook.model.entity.User;
import com.lilemy.moraaccountbook.model.enums.UserGenderEnum;
import com.lilemy.moraaccountbook.model.enums.UserRoleEnum;
import com.lilemy.moraaccountbook.model.vo.user.LoginUserVO;
import com.lilemy.moraaccountbook.model.vo.user.UserManageVO;
import com.lilemy.moraaccountbook.service.AccountBookService;
import com.lilemy.moraaccountbook.service.UserService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户 服务层实现。
 *
 * @author lilemy
 * @since 2026-08-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private OssService ossService;

    @Lazy
    @Resource
    private AccountBookService accountBookService;

    @Override
    public Long userRegister(UserRegisterRequest request) {
        // 1.校验
        String userPhone = request.getUserPhone();
        String userPassword = request.getUserPassword();
        String checkPassword = request.getCheckPassword();
        ThrowUtils.throwIf(!PhoneUtil.isMobile(userPhone), ErrorCode.PARAMS_ERROR, "手机号格式不正确");
        ThrowUtils.throwIf(!userPassword.equals(checkPassword), ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        // 2.查询手机号是否已存在
        long count = this.queryChain()
                .eq(User::getUserPhone, userPhone)
                .count();
        ThrowUtils.throwIf(count > 0, ErrorCode.PARAMS_ERROR, "手机号已存在");
        // 3.加密密码
        String encodePassword = getEncryptPassword(userPassword);
        // 4.保存
        User user = new User();
        user.setUserPhone(userPhone);
        user.setUserPassword(encodePassword);
        user.setUserName(UserConstant.USER_DEFAULT_NAME);
        user.setUserAvatar(UserConstant.USER_DEFAULT_AVATAR_URL);
        boolean save = this.save(user);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "注册失败");
        // 5.创建默认账本
        Long userId = user.getId();
        accountBookService.initUserAccountBook(userId);
        // 6.返回结果
        return userId;
    }

    @Override
    public LoginUserVO userLogin(UserLoginRequest request) {
        // 1.校验
        String userPhone = request.getUserPhone();
        String userPassword = request.getUserPassword();
        ThrowUtils.throwIf(!PhoneUtil.isMobile(userPhone), ErrorCode.PARAMS_ERROR, "手机号格式不正确");
        // 2.加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 3.查询用户
        User user = this.queryChain()
                .eq(User::getUserPhone, userPhone)
                .eq(User::getUserPassword, encryptPassword)
                .one();
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "手机号码不存在或密码错误");
        // 4.记录用户登录态
        StpUtil.login(user.getId());
        StpUtil.getTokenSession().set(UserConstant.USER_LOGIN_STATE, user);
        // 5.校验是否至少有一个账本，没有则初始化
        accountBookService.initUserAccountBook(user.getId());
        // 6.获取脱敏后的用户信息
        return this.getLoginUserVO(user);
    }

    @Override
    public Boolean userLogout() {
        StpUtil.checkLogin();
        // 移除登录态
        StpUtil.logout();
        return true;
    }

    @Override
    public User getLoginUserCache() {
        // 先判断是否已登录
        StpUtil.checkLogin();
        // 获取登录用户
        Object userObj = StpUtil.getTokenSession().get(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_LOGIN_ERROR);
        return user;
    }

    @Override
    public User getLoginUser() {
        // 获取登录用户
        User loginUserCache = this.getLoginUserCache();
        ThrowUtils.throwIf(loginUserCache == null, ErrorCode.NOT_LOGIN_ERROR);
        // 获取登录用户最新数据
        User user = this.getById(loginUserCache.getId());
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_LOGIN_ERROR);
        return user;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public Boolean updateUser(UserUpdateRequest request) {
        // 1.查询修改用户
        Long id = request.getId();
        User user = this.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        // 2.更新用户
        User updateUser = new User();
        BeanUtil.copyProperties(request, updateUser);
        boolean result = this.updateById(updateUser);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "更新失败");
        // 3.踢出登录
        StpUtil.kickout(id);
        return true;
    }

    @Override
    public Boolean uploadAvatar(User user, MultipartFile file) {
        // 1.参数校验
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        ThrowUtils.throwIf(file.isEmpty(), ErrorCode.PARAMS_ERROR, "上传的文件为空");
        // 2.记录旧图片地址
        String oldUserAvatar = user.getUserAvatar();
        // 3.上传文件
        String userAvatar = ossService.uploadFile(UserConstant.USER_AVATAR_PREFIX, file);
        // 4.更新用户数据
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setUserAvatar(userAvatar);
        boolean result = this.updateById(updateUser);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "上传失败");
        // 5.删除旧头像
        if (StringUtils.isNotBlank(oldUserAvatar) && !oldUserAvatar.equals(UserConstant.USER_DEFAULT_AVATAR_URL)) {
            ossService.deleteFileByUrl(oldUserAvatar);
        }
        // 6.返回结果
        return true;
    }

    @Override
    public Boolean deleteUser(Long id) {
        // 1.查询用户
        User user = this.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        // 2.删除用户
        boolean result = this.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "删除失败");
        // 3.踢出登录
        StpUtil.kickout(id);
        return true;
    }

    @Override
    public PageResult<UserManageVO> getUserManageVOPage(UserQueryRequest request, PageQuery pageQuery) {
        // 1.构建查询条件
        QueryWrapper queryWrapper = this.getQueryWrapper(request, pageQuery);
        // 2.分页查询
        Page<User> userPage = this.page(pageQuery.build(), queryWrapper);
        // 3.转换为封装类
        Page<UserManageVO> userManageVOPage = userPage.map(this::getUserManageVO);
        // 4.返回结果
        return PageResult.build(userManageVOPage);
    }

    @Override
    public QueryWrapper getQueryWrapper(UserQueryRequest request, PageQuery pageQuery) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (pageQuery != null) {
            queryWrapper = pageQuery.buildOrderQuery();
        }
        if (request == null) {
            return queryWrapper;
        }
        String userName = request.getUserName();
        String userPhone = request.getUserPhone();
        String userEmail = request.getUserEmail();
        UserGenderEnum userGender = request.getUserGender();
        UserRoleEnum userRole = request.getUserRole();
        queryWrapper.like(User::getUserName, userName, StringUtils::isNotBlank)
                .like(User::getUserPhone, userPhone, StringUtils::isNotBlank)
                .like(User::getUserEmail, userEmail, StringUtils::isNotBlank)
                .eq(User::getUserGender, userGender, Objects::nonNull)
                .eq(User::getUserRole, userRole, Objects::nonNull);
        return queryWrapper;
    }

    @Override
    public UserManageVO getUserManageVO(User user) {
        if (user == null) {
            return null;
        }
        UserManageVO userManageVO = new UserManageVO();
        BeanUtil.copyProperties(user, userManageVO);
        return userManageVO;
    }

    @Override
    public Map<Long, User> getUserMapByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<User> userList = this.listByIds(ids);
        if (CollUtil.isEmpty(userList)) {
            return Collections.emptyMap();
        }
        return userList.stream().collect(Collectors.toMap(User::getId, Function.identity(), (v1, v2) -> v1));
    }

    @Override
    public String getEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = "lilemy";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }
}
