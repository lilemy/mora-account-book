package com.lilemy.moraaccountbook.service;

import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.model.dto.user.UserLoginRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserQueryRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserRegisterRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserUpdateRequest;
import com.lilemy.moraaccountbook.model.entity.SysUser;
import com.lilemy.moraaccountbook.model.vo.user.SysLoginUserVO;
import com.lilemy.moraaccountbook.model.vo.user.SysUserManageVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import org.springframework.web.multipart.MultipartFile;

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
     * 获取已登录用户信息（登录缓存）
     *
     * @return 登录用户信息
     */
    SysUser getLoginUserCache();

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
     * 更新用户信息
     *
     * @param request 更新请求
     * @return 是否更新成功
     */
    Boolean updateSysUser(UserUpdateRequest request);

    /**
     * 上传并更新用户头像
     *
     * @param sysUser 用户
     * @param file    头像文件
     * @return 是否上传成功
     */
    Boolean uploadAvatar(SysUser sysUser, MultipartFile file);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否删除成功
     */
    Boolean deleteSysUser(Long id);

    /**
     * 获取用户管理封装信息分页
     *
     * @param request   查询请求
     * @param pageQuery 分页查询
     * @return 用户管理封装信息分页
     */
    PageResult<SysUserManageVO> getSysUserManageVOPage(UserQueryRequest request, PageQuery pageQuery);

    /**
     * 获取查询条件
     *
     * @param request   查询请求
     * @param pageQuery 分页查询
     * @return 查询条件
     */
    QueryWrapper getQueryWrapper(UserQueryRequest request, PageQuery pageQuery);

    /**
     * 获取用户管理封装信息
     *
     * @param sysUser 用户信息
     * @return 用户管理封装信息
     */
    SysUserManageVO getSysUserManageVO(SysUser sysUser);

    /**
     * 获取加密后的密码
     *
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String userPassword);
}
