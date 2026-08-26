package com.lilemy.moraaccountbook.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.lilemy.moraaccountbook.model.entity.AccountBook;
import com.lilemy.moraaccountbook.mapper.AccountBookMapper;
import com.lilemy.moraaccountbook.service.AccountBookService;
import org.springframework.stereotype.Service;

/**
 * 账本 服务层实现。
 *
 * @author lilemy
 * @since 2026-08-26
 */
@Service
public class AccountBookServiceImpl extends ServiceImpl<AccountBookMapper, AccountBook>  implements AccountBookService{

}
