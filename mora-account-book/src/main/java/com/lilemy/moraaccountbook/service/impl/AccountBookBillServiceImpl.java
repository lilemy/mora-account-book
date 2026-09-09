package com.lilemy.moraaccountbook.service.impl;

import com.lilemy.moraaccountbook.mapper.AccountBookBillMapper;
import com.lilemy.moraaccountbook.model.entity.AccountBookBill;
import com.lilemy.moraaccountbook.service.AccountBookBillService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 账本收入/支出流水 服务层实现。
 *
 * @author lilemy
 * @since 2026-09-09
 */
@Service
public class AccountBookBillServiceImpl extends ServiceImpl<AccountBookBillMapper, AccountBookBill> implements AccountBookBillService {

}
