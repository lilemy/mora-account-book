package com.lilemy.moraaccountbook.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.lilemy.moraaccountbook.model.entity.AccountBookMember;
import com.lilemy.moraaccountbook.mapper.AccountBookMemberMapper;
import com.lilemy.moraaccountbook.service.AccountBookMemberService;
import org.springframework.stereotype.Service;

/**
 * 账本成员 服务层实现。
 *
 * @author lilemy
 * @since 2026-08-26
 */
@Service
public class AccountBookMemberServiceImpl extends ServiceImpl<AccountBookMemberMapper, AccountBookMember>  implements AccountBookMemberService{

}
