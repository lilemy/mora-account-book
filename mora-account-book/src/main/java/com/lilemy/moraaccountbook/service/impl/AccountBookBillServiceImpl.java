package com.lilemy.moraaccountbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.exception.ErrorCode;
import com.lilemy.moraaccountbook.exception.ThrowUtils;
import com.lilemy.moraaccountbook.mapper.AccountBookBillMapper;
import com.lilemy.moraaccountbook.model.dto.bill.AccountBookBillQueryRequest;
import com.lilemy.moraaccountbook.model.entity.AccountBook;
import com.lilemy.moraaccountbook.model.entity.AccountBookBill;
import com.lilemy.moraaccountbook.model.entity.User;
import com.lilemy.moraaccountbook.model.enums.TransactionTypeEnum;
import com.lilemy.moraaccountbook.model.vo.bill.AccountBookBillManageVO;
import com.lilemy.moraaccountbook.service.AccountBookBillService;
import com.lilemy.moraaccountbook.service.AccountBookService;
import com.lilemy.moraaccountbook.service.UserService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 账本收入/支出流水 服务层实现。
 *
 * @author lilemy
 * @since 2026-09-09
 */
@Service
@RequiredArgsConstructor
public class AccountBookBillServiceImpl extends ServiceImpl<AccountBookBillMapper, AccountBookBill> implements AccountBookBillService {

    private final UserService userService;
    private final AccountBookService accountBookService;

    @Override
    public AccountBookBillManageVO getAccountBookBillManageVOById(Long id) {
        AccountBookBill bill = this.getById(id);
        ThrowUtils.throwIf(bill == null, ErrorCode.NOT_FOUND_ERROR, "账本流水不存在");
        AccountBookBillManageVO billManageVO = BeanUtil.copyProperties(bill, AccountBookBillManageVO.class);
        User user = userService.getById(bill.getUserId());
        if (user != null) {
            billManageVO.setUserPhone(user.getUserPhone());
            billManageVO.setUserName(user.getUserName());
        }
        AccountBook accountBook = accountBookService.getById(bill.getBookId());
        if (accountBook != null) {
            billManageVO.setBookName(accountBook.getBookName());
        }
        return billManageVO;
    }

    @Override
    public PageResult<AccountBookBillManageVO> getAccountBookBillManageVOPage(AccountBookBillQueryRequest request,
                                                                              PageQuery pageQuery) {
        // 1.构建查询条件
        QueryWrapper queryWrapper = this.buildQueryWrapper(request, pageQuery);
        // 2.分页查询
        Page<AccountBookBill> accountBookBillPage = this.page(pageQuery.build(), queryWrapper);
        List<AccountBookBill> accountBookBillList = accountBookBillPage.getRecords();
        if (CollUtil.isEmpty(accountBookBillList)) {
            return PageResult.build();
        }
        // 3.获取用户信息
        Set<Long> userIds = accountBookBillList.stream().map(AccountBookBill::getUserId).collect(Collectors.toSet());
        Map<Long, User> userMap = userService.getUserMapByIds(userIds);
        // 4.获取账本信息
        Set<Long> bookIds = accountBookBillList.stream().map(AccountBookBill::getBookId).collect(Collectors.toSet());
        Map<Long, AccountBook> bookMap = accountBookService.getAccountBookMapByIds(bookIds);
        // 5.转换为封装类
        Page<AccountBookBillManageVO> accountBookManageVOPage = accountBookBillPage.map(bill -> {
            AccountBookBillManageVO billManageVO = new AccountBookBillManageVO();
            BeanUtil.copyProperties(bill, billManageVO);
            // 设置用户信息
            if (CollUtil.isNotEmpty(userMap) && userMap.containsKey(bill.getUserId())) {
                User user = userMap.get(bill.getUserId());
                billManageVO.setUserPhone(user.getUserPhone());
                billManageVO.setUserName(user.getUserName());
            }
            // 设置账本信息
            if (CollUtil.isNotEmpty(bookMap) && bookMap.containsKey(bill.getBookId())) {
                AccountBook accountBook = bookMap.get(bill.getBookId());
                billManageVO.setBookName(accountBook.getBookName());
            }
            return billManageVO;
        });
        // 6.返回结果
        return PageResult.build(accountBookManageVOPage);
    }

    @Override
    public QueryWrapper buildQueryWrapper(AccountBookBillQueryRequest request, PageQuery pageQuery) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (pageQuery != null) {
            queryWrapper = pageQuery.buildOrderQuery();
        }
        if (request == null) {
            return queryWrapper;
        }
        Long bookId = request.getBookId();
        Long userId = request.getUserId();
        String categoryName = request.getCategoryName();
        TransactionTypeEnum billType = request.getBillType();
        LocalDateTime startBillTime = request.getStartBillTime();
        LocalDateTime endBillTime = request.getEndBillTime();
        queryWrapper.like(AccountBookBill::getCategoryName, categoryName, StringUtils::isNotBlank)
                .eq(AccountBookBill::getBookId, bookId, Objects::nonNull)
                .eq(AccountBookBill::getUserId, userId, Objects::nonNull)
                .eq(AccountBookBill::getBillType, billType, Objects::nonNull);
        if (startBillTime != null && endBillTime != null) {
            queryWrapper.between(AccountBookBill::getBillTime, startBillTime, endBillTime);
        } else if (startBillTime != null) {
            queryWrapper.ge(AccountBookBill::getBillTime, startBillTime);
        } else if (endBillTime != null) {
            queryWrapper.le(AccountBookBill::getBillTime, endBillTime);
        }
        return queryWrapper;
    }
}
