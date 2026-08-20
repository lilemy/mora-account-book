package com.lilemy.moraaccountbook.service;

import com.lilemy.moraaccountbook.model.dto.user.UserRegisterRequest;
import com.lilemy.moraaccountbook.model.entity.SysUser;
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
     * 获取加密后的密码
     *
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String userPassword);
}
