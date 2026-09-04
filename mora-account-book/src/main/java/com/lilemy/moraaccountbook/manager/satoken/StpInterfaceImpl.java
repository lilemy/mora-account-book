package com.lilemy.moraaccountbook.manager.satoken;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.lilemy.moraaccountbook.constant.UserConstant;
import com.lilemy.moraaccountbook.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sa-Toke 权限验证接口实现
 *
 * @author lilemy
 * @date 2026-08-21
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return new ArrayList<>();
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 从当前登录用户信息中获取角色
        User user = (User) StpUtil.getTokenSession().get(UserConstant.USER_LOGIN_STATE);
        return Collections.singletonList(user.getUserRole().toString());
    }
}
