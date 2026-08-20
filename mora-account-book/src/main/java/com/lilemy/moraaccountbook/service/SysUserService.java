package com.lilemy.moraaccountbook.service;

import com.lilemy.moraaccountbook.model.dto.user.UserLoginRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserRegisterRequest;
import com.lilemy.moraaccountbook.model.entity.SysUser;
import com.lilemy.moraaccountbook.model.vo.user.SysLoginUserVO;
import com.mybatisflex.core.service.IService;

/**
 * 用户 服务层。
 *
 * @author lilemy
 * @since 2026-08-20
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 新用户ID
     */
    Long userRegister(UserRegisterRequest request);

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录用户信息
     */
    SysLoginUserVO userLogin(UserLoginRequest request);

    /**
     * 用户登出
     *
     * @return 是否登出成功
     */
    Boolean userLogout();

    /**
     * 获取已登录用户信息
     *
     * @return 登录用户信息
     */
    SysUser getLoginUser();

    /**
     * 获取脱敏的已登录用户信息
     *
     * @param sysUser 已登录用户
     * @return 登录用户信息
     */
    SysLoginUserVO getLoginUserVO(SysUser sysUser);

    /**
     * 获取加密后的密码
     *
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String userPassword);
}
