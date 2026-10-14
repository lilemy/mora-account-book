package com.lilemy.moraaccountbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.constant.CommonConstant;
import com.lilemy.moraaccountbook.exception.ErrorCode;
import com.lilemy.moraaccountbook.exception.ThrowUtils;
import com.lilemy.moraaccountbook.mapper.AccountBookBillMapper;
import com.lilemy.moraaccountbook.model.dto.bill.AccountBookBillCalendarQueryRequest;
import com.lilemy.moraaccountbook.model.dto.bill.AccountBookBillCreateRequest;
import com.lilemy.moraaccountbook.model.dto.bill.AccountBookBillQueryRequest;
import com.lilemy.moraaccountbook.model.dto.bill.AccountBookBillUpdateRequest;
import com.lilemy.moraaccountbook.model.entity.*;
import com.lilemy.moraaccountbook.model.enums.TransactionTypeEnum;
import com.lilemy.moraaccountbook.model.enums.book.AccountBookStatusEnum;
import com.lilemy.moraaccountbook.model.vo.bill.AccountBookBillManageVO;
import com.lilemy.moraaccountbook.model.vo.bill.AccountBookBillMonthListVO;
import com.lilemy.moraaccountbook.model.vo.bill.AccountBookBillMonthTotalVO;
import com.lilemy.moraaccountbook.model.vo.bill.AccountBookBillVO;
import com.lilemy.moraaccountbook.service.*;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
    private final UserAccountService userAccountService;
    private final AccountBookCategoryService accountBookCategoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAccountBookBill(AccountBookBillCreateRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR, "请求不能为空");
        Long bookId = request.getBookId();
        Long accountId = request.getAccountId();
        Long categoryId = request.getCategoryId();
        BigDecimal amount = request.getAmount();
        LocalDateTime billTime = request.getBillTime();
        String remark = request.getRemark();
        validateBillFields(amount, billTime, remark, accountId);
        ThrowUtils.throwIf(bookId == null || bookId <= 0, ErrorCode.PARAMS_ERROR, "账本 ID 必须为正数");
        ThrowUtils.throwIf(categoryId == null || categoryId <= 0, ErrorCode.PARAMS_ERROR, "分类 ID 必须为正数");
        // 1.参数校验
        if (billTime == null) {
            billTime = LocalDateTime.now();
        }
        ThrowUtils.throwIf(billTime.isAfter(LocalDateTime.now()), ErrorCode.PARAMS_ERROR, "时间不能晚于当前时间");
        // 账本校验
        User loginUser = userService.getLoginUser();
        Long userId = loginUser.getId();
        checkBookPermission(bookId, userId);
        // 分类校验
        AccountBookCategory category = getValidCategory(categoryId, bookId);
        // 2.创建账本流水
        AccountBookBill accountBookBill = AccountBookBill.builder()
                .bookId(bookId)
                .userId(userId)
                .categoryId(categoryId)
                .categoryName(category.getCategoryName())
                .billType(category.getCategoryType())
                .amount(amount)
                .billTime(billTime)
                .remark(remark)
                .build();
        if (accountId != null && accountId != 0) {
            // 更新账户余额信息
            UserAccount userAccount = userAccountService.updateBalanceByBill(accountId, userId, amount, category.getCategoryType());
            accountBookBill.setAccountId(accountId);
            accountBookBill.setAccountName(userAccount.getAccountName());
        }
        // 3.保存账本流水
        boolean save = this.save(accountBookBill);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "账本流水创建失败");
        return accountBookBill.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAccountBookBill(AccountBookBillUpdateRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR, "请求不能为空");
        // 1.锁定流水并校验归属，避免重复更新/删除导致余额重复撤销。
        AccountBookBill bill = getBillForOperation(request.getId());
        BigDecimal amount = request.getAmount() == null ? bill.getAmount() : request.getAmount();
        LocalDateTime billTime = request.getBillTime() == null ? bill.getBillTime() : request.getBillTime();
        String remark = request.getRemark() == null ? bill.getRemark() : request.getRemark();
        Long oldAccountId = normalizeAccountId(bill.getAccountId());
        Long newAccountId = request.getAccountId() == null ? oldAccountId : normalizeAccountId(request.getAccountId());
        validateBillFields(amount, billTime, remark, newAccountId);
        BigDecimal oldImpact = incomeImpact(bill.getAmount(), bill.getBillType());
        // 2.更换分类时同步刷新分类名称和收支类型；未更换时保留历史快照。
        if (request.getCategoryId() != null) {
            AccountBookCategory category = getValidCategory(request.getCategoryId(), bill.getBookId());
            bill.setCategoryId(category.getId());
            bill.setCategoryName(category.getCategoryName());
            bill.setBillType(category.getCategoryType());
        }
        BigDecimal newImpact = incomeImpact(amount, bill.getBillType());
        // 3.按账户合并净变化，并按 ID 升序加锁，避免两个请求交叉换账户产生死锁。
        Map<Long, BigDecimal> changes = new java.util.TreeMap<>();
        if (oldAccountId != null) {
            changes.put(oldAccountId, oldImpact.negate());
        }
        if (newAccountId != null) {
            changes.merge(newAccountId, newImpact, BigDecimal::add);
        }
        for (Map.Entry<Long, BigDecimal> change : changes.entrySet()) {
            UserAccount account = userAccountService.adjustBalanceByBill(change.getKey(), bill.getUserId(), change.getValue());
            if (Objects.equals(change.getKey(), newAccountId)) {
                bill.setAccountName(account.getAccountName());
            }
        }
        bill.setAccountId(newAccountId);
        if (newAccountId == null) {
            bill.setAccountName(null);
        }
        bill.setAmount(amount);
        bill.setBillTime(billTime);
        bill.setRemark(remark);
        // 4.显式写入 null，保证解除账户关联时同时清空账户 ID 和名称。
        ThrowUtils.throwIf(!this.updateById(bill, false), ErrorCode.OPERATION_ERROR, "账本流水更新失败");
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAccountBookBillById(Long id) {
        AccountBookBill bill = getBillForOperation(id);
        // 撤销原流水对账户的影响；余额不满足约束时整个删除事务回滚。
        Long accountId = normalizeAccountId(bill.getAccountId());
        if (accountId != null) {
            userAccountService.adjustBalanceByBill(accountId, bill.getUserId(),
                    incomeImpact(bill.getAmount(), bill.getBillType()).negate());
        }
        ThrowUtils.throwIf(!this.removeById(id), ErrorCode.OPERATION_ERROR, "账本流水删除失败");
        return true;
    }

    @Override
    public List<AccountBookBillMonthListVO> getAccountBookBillVOListByMonth(AccountBookBillCalendarQueryRequest request) {
        User loginUser = userService.getLoginUserCache();
        LocalDate calendarDate = request.getCalendarDate();
        Long bookId = request.getBookId();
        // 校验账本权限
        accountBookService.checkBookPermission(bookId, loginUser.getId());
        // 获取指定月份范围内的账本流水列表
        int monthLength = calendarDate.lengthOfMonth();
        LocalDate firstDay = calendarDate.withDayOfMonth(1);
        LocalDateTime start = firstDay.atStartOfDay();
        LocalDateTime end = calendarDate.withDayOfMonth(monthLength).plusDays(1).atStartOfDay();
        List<AccountBookBill> list = getBillListByDateRange(bookId, loginUser.getId(), start, end);
        if (CollUtil.isEmpty(list)) {
            return List.of();
        }
        // 按照日期进行分组
        Map<LocalDate, List<AccountBookBill>> map = list.stream().collect(Collectors.groupingBy(
                e -> e.getBillTime().toLocalDate(),
                TreeMap::new,
                Collectors.toList()));
        // 遍历指定月份的每一天
        return firstDay.datesUntil(firstDay.plusMonths(1)).map(date -> {
            BigDecimal income = BigDecimal.ZERO;
            BigDecimal expense = BigDecimal.ZERO;
            List<AccountBookBillVO> accountBookBills = List.of();
            if (CollUtil.isNotEmpty(map) && map.containsKey(date)) {
                List<AccountBookBill> bills = map.get(date);

                for (AccountBookBill bill : bills) {
                    if (bill.getBillType().equals(TransactionTypeEnum.INCOME)) {
                        income = income.add(bill.getAmount());
                    } else if (bill.getBillType().equals(TransactionTypeEnum.EXPENSE)) {
                        expense = expense.add(bill.getAmount());
                    }
                }

                accountBookBills = BeanUtil.copyToList(bills, AccountBookBillVO.class);
            }
            return AccountBookBillMonthListVO.builder()
                    .order(date.getDayOfMonth())
                    .date(date)
                    .income(income)
                    .expense(expense)
                    .bills(accountBookBills)
                    .build();
        }).toList();
    }

    @Override
    public List<AccountBookBillMonthTotalVO> getAccountBookBillTotalByMonth(AccountBookBillCalendarQueryRequest request) {
        User loginUser = userService.getLoginUserCache();
        LocalDate calendarDate = request.getCalendarDate();
        Long bookId = request.getBookId();
        // 校验账本权限
        accountBookService.checkBookPermission(bookId, loginUser.getId());
        // 获取指定年份范围内的账本流水列表
        int year = calendarDate.getYear();
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(year + 1, 1, 1, 0, 0, 0);
        List<AccountBookBill> list = getBillListByDateRange(bookId, loginUser.getId(), start, end);
        if (CollUtil.isEmpty(list)) {
            return List.of();
        }
        // 按照日期进行分组
        Map<Integer, List<AccountBookBill>> map = list.stream().collect(Collectors.groupingBy(
                e -> e.getBillTime().getMonthValue(),
                TreeMap::new,
                Collectors.toList()));
        // 遍历每个月
        List<AccountBookBillMonthTotalVO> totalList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            AccountBookBillMonthTotalVO totalVO = AccountBookBillMonthTotalVO.builder()
                    .month(i)
                    .totalIncome(BigDecimal.ZERO)
                    .totalExpense(BigDecimal.ZERO)
                    .build();
            if (map.containsKey(i)) {
                List<AccountBookBill> bills = map.get(i);
                for (AccountBookBill bill : bills) {
                    if (bill.getBillType().equals(TransactionTypeEnum.INCOME)) {
                        totalVO.setTotalIncome(totalVO.getTotalIncome().add(bill.getAmount()));
                    } else if (bill.getBillType().equals(TransactionTypeEnum.EXPENSE)) {
                        totalVO.setTotalExpense(totalVO.getTotalExpense().add(bill.getAmount()));
                    }
                }
            }

            totalList.add(totalVO);
        }
        return totalList;
    }

    @Override
    public AccountBookBillVO getAccountBookBillVOById(Long id) {
        User loginUser = userService.getLoginUserCache();
        AccountBookBill bill = this.getById(id);
        ThrowUtils.throwIf(bill == null, ErrorCode.NOT_FOUND_ERROR, "账本流水不存在");
        ThrowUtils.throwIf(!bill.getUserId().equals(loginUser.getId()), ErrorCode.NO_AUTH_ERROR);
        AccountBookBillVO billVO = BeanUtil.copyProperties(bill, AccountBookBillVO.class);
        AccountBook accountBook = accountBookService.getById(bill.getBookId());
        if (accountBook != null) {
            billVO.setBookName(accountBook.getBookName());
        }
        return billVO;
    }

    @Override
    public PageResult<AccountBookBillVO> getAccountBookBillVOPage(AccountBookBillQueryRequest request, PageQuery pageQuery) {
        // 1.获取当前登录用户
        User loginUser = userService.getLoginUserCache();
        // 2.构建查询条件
        QueryWrapper queryWrapper = this.buildQueryWrapper(request, pageQuery);
        queryWrapper.eq(AccountBookBill::getUserId, loginUser.getId());
        // 3.分页查询
        Page<AccountBookBill> accountBookBillPage = this.page(pageQuery.build(), queryWrapper);
        List<AccountBookBill> accountBookBillList = accountBookBillPage.getRecords();
        if (CollUtil.isEmpty(accountBookBillList)) {
            return PageResult.build();
        }
        // 4.获取账本信息
        Map<Long, AccountBook> bookMap = accountBookService.getAccountBookMapByIds(accountBookBillList.stream()
                .map(AccountBookBill::getBookId).collect(Collectors.toSet()));
        // 5.转换为封装类
        Page<AccountBookBillVO> accountBookVOPage = accountBookBillPage.map(bill -> {
            AccountBookBillVO billVO = new AccountBookBillVO();
            BeanUtil.copyProperties(bill, billVO);
            // 设置账本信息
            if (CollUtil.isNotEmpty(bookMap) && bookMap.containsKey(bill.getBookId())) {
                AccountBook accountBook = bookMap.get(bill.getBookId());
                billVO.setBookName(accountBook.getBookName());
            }
            return billVO;
        });
        // 6.返回结果
        return PageResult.build(accountBookVOPage);
    }

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

    /**
     * 获取并锁定本人流水；共享账本成员也只能修改自己的记账记录。
     */
    private AccountBookBill getBillForOperation(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "ID 必须为正数");
        Long userId = userService.getLoginUser().getId();
        AccountBookBill bill = this.getOne(QueryWrapper.create().eq(AccountBookBill::getId, id).forUpdate());
        ThrowUtils.throwIf(bill == null, ErrorCode.NOT_FOUND_ERROR, "账本流水不存在");
        ThrowUtils.throwIf(!Objects.equals(bill.getUserId(), userId), ErrorCode.NO_AUTH_ERROR, "只能操作本人流水");
        checkBookPermission(bill.getBookId(), userId);
        return bill;
    }

    /**
     * 个人账本仅所有者可记账，共享账本要求所有者或有效成员，归档后不可变更。
     */
    private void checkBookPermission(Long bookId, Long userId) {
        AccountBook book = accountBookService.checkBookPermission(bookId, userId);
        ThrowUtils.throwIf(!AccountBookStatusEnum.NORMAL.equals(book.getStatus()),
                ErrorCode.PARAMS_ERROR, "账本已归档或状态异常，不能变更流水");
    }

    /**
     * 分类必须属于当前账本，流水类型由服务端分类决定。
     */
    private AccountBookCategory getValidCategory(Long categoryId, Long bookId) {
        ThrowUtils.throwIf(categoryId == null || categoryId <= 0, ErrorCode.PARAMS_ERROR, "ID 必须为正数");
        AccountBookCategory category = accountBookCategoryService.getById(categoryId);
        ThrowUtils.throwIf(category == null, ErrorCode.NOT_FOUND_ERROR, "账本分类不存在");
        ThrowUtils.throwIf(!Objects.equals(category.getBookId(), bookId), ErrorCode.NO_AUTH_ERROR, "账本分类不属于该账本");
        ThrowUtils.throwIf(category.getCategoryType() == null, ErrorCode.PARAMS_ERROR, "分类收支类型不能为空");
        return category;
    }

    /**
     * 服务层再次校验，保证非 HTTP 调用也不能绕过金额精度和字段约束。
     */
    private void validateBillFields(BigDecimal amount, LocalDateTime time, String remark, Long accountId) {
        ThrowUtils.throwIf(amount == null || amount.signum() <= 0, ErrorCode.PARAMS_ERROR, "流水金额必须大于 0");
        ThrowUtils.throwIf(amount.scale() > 2 || amount.compareTo(CommonConstant.MAX_MONEY_DECIMAL) > 0,
                ErrorCode.PARAMS_ERROR, "流水金额最多 16 位整数、2 位小数");
        ThrowUtils.throwIf(time != null && time.isAfter(LocalDateTime.now()), ErrorCode.PARAMS_ERROR, "时间不能晚于当前时间");
        ThrowUtils.throwIf(remark != null && remark.length() > 512, ErrorCode.PARAMS_ERROR, "流水备注不能超过 512 个字符");
        ThrowUtils.throwIf(accountId != null && accountId < 0, ErrorCode.PARAMS_ERROR, "账户 ID 不能为负数");
    }

    /**
     * 兼容创建接口以 0 表示不关联账户的约定。
     */
    private Long normalizeAccountId(Long id) {
        return id == null || id == 0 ? null : id;
    }

    /**
     * 统一用收入为正、支出为负描述影响，便于计算更新差额和删除冲销。
     */
    private BigDecimal incomeImpact(BigDecimal amount, TransactionTypeEnum type) {
        ThrowUtils.throwIf(type == null, ErrorCode.PARAMS_ERROR, "流水收支类型不能为空");
        return TransactionTypeEnum.INCOME.equals(type) ? amount : amount.negate();
    }

    /**
     * 根据账本 ID、用户 ID 和时间范围获取账本流水列表（左闭右开）
     *
     * @param bookId 账本 ID
     * @param userId 用户 ID
     * @param start  开始时间
     * @param end    结束时间
     * @return 账本流水列表
     */
    private List<AccountBookBill> getBillListByDateRange(Long bookId, Long userId, LocalDateTime start, LocalDateTime end) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(AccountBookBill::getBookId, bookId, Objects::nonNull)
                .eq(AccountBookBill::getUserId, userId, Objects::nonNull)
                .ge(AccountBookBill::getBillTime, start, Objects::nonNull)
                .lt(AccountBookBill::getBillTime, end, Objects::nonNull);
        return this.list(queryWrapper);
    }
}
