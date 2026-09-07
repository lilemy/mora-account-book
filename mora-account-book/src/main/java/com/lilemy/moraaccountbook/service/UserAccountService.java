package com.lilemy.moraaccountbook.service;

import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountCreateRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountDeleteRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountQueryRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountUpdateRequest;
import com.lilemy.moraaccountbook.model.entity.UserAccount;
import com.lilemy.moraaccountbook.model.vo.user.UserAccountManageVO;
import com.lilemy.moraaccountbook.model.vo.user.UserAccountVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;

/**
 * 用户账户 服务层。
 *
 * @author lilemy
 * @since 2026-09-04
 */
public interface UserAccountService extends IService<UserAccount> {

    /**
     * 创建用户账户
     *
     * @param request 请求参数
     * @return 账户ID
     */
    Long createUserAccount(UserAccountCreateRequest request);

    /**
     * 更新用户账户信息
     *
     * @param request 请求参数
     * @return 是否更新成功
     */
    Boolean updateUserAccount(UserAccountUpdateRequest request);

    /**
     * 删除用户账户
     *
     * @param request 请求参数
     * @return 是否删除成功
     */
    Boolean deleteUserAccount(UserAccountDeleteRequest request);

    /**
     * 根据ID获取用户账户信息
     *
     * @param id 账户ID
     * @return 用户账户信息
     */
    UserAccountVO getUserAccountVOById(Long id);

    /**
     * 根据ID获取用户账户管理信息
     *
     * @param id 账户ID
     * @return 用户账户管理信息
     */
    UserAccountManageVO getUserAccountManageVOById(Long id);

    /**
     * 获取用户账户管理列表
     *
     * @param request   请求参数
     * @param pageQuery 分页参数
     * @return 用户账户管理列表
     */
    PageResult<UserAccountVO> getUserAccountVOPage(UserAccountQueryRequest request, PageQuery pageQuery);

    /**
     * 获取用户账户管理列表
     *
     * @param request   请求参数
     * @param pageQuery 分页参数
     * @return 用户账户管理列表
     */
    PageResult<UserAccountManageVO> getUserAccountManageVOPage(UserAccountQueryRequest request, PageQuery pageQuery);

    /**
     * 构建查询条件
     *
     * @param request   请求参数
     * @param pageQuery 分页参数
     * @return 查询条件
     */
    QueryWrapper buildQueryWrapper(UserAccountQueryRequest request, PageQuery pageQuery);
}
