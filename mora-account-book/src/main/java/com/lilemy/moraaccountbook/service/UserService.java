package com.lilemy.moraaccountbook.service;

import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.model.dto.user.UserLoginRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserQueryRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserRegisterRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserUpdateRequest;
import com.lilemy.moraaccountbook.model.entity.User;
import com.lilemy.moraaccountbook.model.vo.user.LoginUserVO;
import com.lilemy.moraaccountbook.model.vo.user.UserManageVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;

/**
 * 用户 服务层。
 *
 * @author lilemy
 * @since 2026-08-20
 */
public interface UserService extends IService<User> {

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
    LoginUserVO userLogin(UserLoginRequest request);

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
    User getLoginUserCache();

    /**
     * 获取已登录用户信息
     *
     * @return 登录用户信息
     */
    User getLoginUser();

    /**
     * 获取脱敏的已登录用户信息
     *
     * @param user 已登录用户
     * @return 登录用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 更新用户信息
     *
     * @param request 更新请求
     * @return 是否更新成功
     */
    Boolean updateUser(UserUpdateRequest request);

    /**
     * 上传并更新用户头像
     *
     * @param user 用户
     * @param file 头像文件
     * @return 是否上传成功
     */
    Boolean uploadAvatar(User user, MultipartFile file);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否删除成功
     */
    Boolean deleteUser(Long id);

    /**
     * 获取用户管理封装信息分页
     *
     * @param request   查询请求
     * @param pageQuery 分页查询
     * @return 用户管理封装信息分页
     */
    PageResult<UserManageVO> getUserManageVOPage(UserQueryRequest request, PageQuery pageQuery);

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
     * @param user 用户信息
     * @return 用户管理封装信息
     */
    UserManageVO getUserManageVO(User user);

    /**
     * 根据ID集合获取用户Map
     *
     * @param ids ID集合
     * @return 用户Map
     */
    Map<Long, User> getUserMapByIds(Collection<Long> ids);

    /**
     * 获取加密后的密码
     *
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String userPassword);
}
