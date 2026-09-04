package com.lilemy.moraaccountbook.service;

import com.lilemy.moraaccountbook.model.dto.user.UserAccountCreateRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountDeleteRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountUpdateRequest;
import com.lilemy.moraaccountbook.model.entity.UserAccount;
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
}
