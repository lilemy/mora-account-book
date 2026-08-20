package com.lilemy.moraaccountbook.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.PhoneUtil;
import com.lilemy.moraaccountbook.constant.UserConstant;
import com.lilemy.moraaccountbook.exception.ErrorCode;
import com.lilemy.moraaccountbook.exception.ThrowUtils;
import com.lilemy.moraaccountbook.mapper.SysUserMapper;
import com.lilemy.moraaccountbook.model.dto.user.UserLoginRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserRegisterRequest;
import com.lilemy.moraaccountbook.model.entity.SysUser;
import com.lilemy.moraaccountbook.model.vo.user.SysLoginUserVO;
import com.lilemy.moraaccountbook.service.SysUserService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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
        sysUser.setUserName("小摩拉");
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
    public String getEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = "lilemy";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }
}
