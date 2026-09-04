package com.lilemy.moraaccountbook.service.impl;

import com.lilemy.moraaccountbook.mapper.UserAccountMapper;
import com.lilemy.moraaccountbook.model.entity.UserAccount;
import com.lilemy.moraaccountbook.service.UserAccountService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户账户 服务层实现。
 *
 * @author lilemy
 * @since 2026-09-04
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

}
