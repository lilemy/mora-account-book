package com.lilemy.moraaccountbook.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.PhoneUtil;
import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.constant.UserConstant;
import com.lilemy.moraaccountbook.exception.ErrorCode;
import com.lilemy.moraaccountbook.exception.ThrowUtils;
import com.lilemy.moraaccountbook.mapper.SysUserMapper;
import com.lilemy.moraaccountbook.model.dto.user.UserLoginRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserQueryRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserRegisterRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserUpdateRequest;
import com.lilemy.moraaccountbook.model.entity.SysUser;
import com.lilemy.moraaccountbook.model.enums.SysUserGenderEnum;
import com.lilemy.moraaccountbook.model.enums.SysUserRoleEnum;
import com.lilemy.moraaccountbook.model.vo.user.SysLoginUserVO;
import com.lilemy.moraaccountbook.model.vo.user.SysUserManageVO;
import com.lilemy.moraaccountbook.service.SysUserService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Objects;

/**
 * 用户 服务层实现。
 *
 * @author lilemy
 * @since 2026-08-20
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

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
                .eq(SysUser::getUserPhone, userPhone)
                .count();
        ThrowUtils.throwIf(count > 0, ErrorCode.PARAMS_ERROR, "手机号已存在");
        // 3.加密密码
        String encodePassword = getEncryptPassword(userPassword);
        // 4.保存
        SysUser sysUser = new SysUser();
        sysUser.setUserPhone(userPhone);
        sysUser.setUserPassword(encodePassword);
        sysUser.setUserName(UserConstant.USER_DEFAULT_NAME);
        sysUser.setUserAvatar(UserConstant.USER_DEFAULT_AVATAR_URL);
        boolean save = this.save(sysUser);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "注册失败");
        // 5.返回结果
        return sysUser.getId();
    }

    @Override
    public SysLoginUserVO userLogin(UserLoginRequest request) {
        // 1.校验
        String userPhone = request.getUserPhone();
        String userPassword = request.getUserPassword();
        ThrowUtils.throwIf(!PhoneUtil.isMobile(userPhone), ErrorCode.PARAMS_ERROR, "手机号格式不正确");
        // 2.加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 3.查询用户
        SysUser sysUser = this.queryChain()
                .eq(SysUser::getUserPhone, userPhone)
                .eq(SysUser::getUserPassword, encryptPassword)
                .one();
        ThrowUtils.throwIf(sysUser == null, ErrorCode.PARAMS_ERROR, "手机号码不存在或密码错误");
        // 4.记录用户登录态
        StpUtil.login(sysUser.getId());
        StpUtil.getTokenSession().set(UserConstant.USER_LOGIN_STATE, sysUser);
        // 5.获取脱敏后的用户信息
        return this.getLoginUserVO(sysUser);
    }

    @Override
    public Boolean userLogout() {
        StpUtil.checkLogin();
        // 移除登录态
        StpUtil.logout();
        return true;
    }

    @Override
    public SysUser getLoginUser() {
        // 先判断是否已登录
        StpUtil.checkLogin();
        // 获取登录用户
        Object userObj = StpUtil.getTokenSession().get(UserConstant.USER_LOGIN_STATE);
        SysUser sysUser = (SysUser) userObj;
        ThrowUtils.throwIf(sysUser == null, ErrorCode.NOT_LOGIN_ERROR);
        return sysUser;
    }

    @Override
    public SysLoginUserVO getLoginUserVO(SysUser sysUser) {
        if (sysUser == null) {
            return null;
        }
        SysLoginUserVO sysLoginUserVO = new SysLoginUserVO();
        BeanUtil.copyProperties(sysUser, sysLoginUserVO);
        return sysLoginUserVO;
    }

    @Override
    public Boolean updateSysUser(UserUpdateRequest request) {
        // 1.查询修改用户
        Long id = request.getId();
        SysUser sysUser = this.getById(id);
        ThrowUtils.throwIf(sysUser == null, ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        // 2.更新用户
        SysUser updateUser = new SysUser();
        BeanUtil.copyProperties(request, updateUser);
        boolean result = this.updateById(updateUser);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "更新失败");
        // 3.踢出登录
        StpUtil.kickout(id);
        return true;
    }

    @Override
    public Boolean deleteSysUser(Long id) {
        // 1.查询用户
        SysUser sysUser = this.getById(id);
        ThrowUtils.throwIf(sysUser == null, ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        // 2.删除用户
        boolean result = this.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "删除失败");
        // 3.踢出登录
        StpUtil.kickout(id);
        return true;
    }

    @Override
    public PageResult<SysUserManageVO> getSysUserManageVOPage(UserQueryRequest request, PageQuery pageQuery) {
        // 1.构建查询条件
        QueryWrapper queryWrapper = this.getQueryWrapper(request, pageQuery);
        // 2.分页查询
        Page<SysUser> sysUserPage = this.page(pageQuery.build(), queryWrapper);
        // 3.转换为封装类
        Page<SysUserManageVO> sysUserManageVOPage = sysUserPage.map(this::getSysUserManageVO);
        // 4.返回结果
        return PageResult.build(sysUserManageVOPage);
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
        SysUserGenderEnum userGender = request.getUserGender();
        SysUserRoleEnum userRole = request.getUserRole();
        queryWrapper.like(SysUser::getUserName, userName, StringUtils::isNotBlank)
                .like(SysUser::getUserPhone, userPhone, StringUtils::isNotBlank)
                .like(SysUser::getUserEmail, userEmail, StringUtils::isNotBlank)
                .eq(SysUser::getUserGender, userGender, Objects::nonNull)
                .eq(SysUser::getUserRole, userRole, Objects::nonNull);
        return queryWrapper;
    }

    @Override
    public SysUserManageVO getSysUserManageVO(SysUser sysUser) {
        if (sysUser == null) {
            return null;
        }
        SysUserManageVO sysUserManageVO = new SysUserManageVO();
        BeanUtil.copyProperties(sysUser, sysUserManageVO);
        return sysUserManageVO;
    }

    @Override
    public String getEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = "lilemy";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }
}
