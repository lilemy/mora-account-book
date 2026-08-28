package com.lilemy.moraaccountbook.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.lilemy.moraaccountbook.model.entity.AccountBookCategory;
import com.lilemy.moraaccountbook.mapper.AccountBookCategoryMapper;
import com.lilemy.moraaccountbook.service.AccountBookCategoryService;
import org.springframework.stereotype.Service;

/**
 * 账本分类 服务层实现。
 *
 * @author lilemy
 * @since 2026-08-28
 */
@Service
public class AccountBookCategoryServiceImpl extends ServiceImpl<AccountBookCategoryMapper, AccountBookCategory>  implements AccountBookCategoryService{

}
