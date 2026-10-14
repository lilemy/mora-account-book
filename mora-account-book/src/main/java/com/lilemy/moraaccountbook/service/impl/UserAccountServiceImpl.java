package com.lilemy.moraaccountbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.constant.CommonConstant;
import com.lilemy.moraaccountbook.exception.ErrorCode;
import com.lilemy.moraaccountbook.exception.ThrowUtils;
import com.lilemy.moraaccountbook.mapper.AccountBookBillMapper;
import com.lilemy.moraaccountbook.mapper.UserAccountMapper;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountCreateRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountDeleteRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountQueryRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountUpdateRequest;
import com.lilemy.moraaccountbook.model.entity.AccountBookBill;
import com.lilemy.moraaccountbook.model.entity.User;
import com.lilemy.moraaccountbook.model.entity.UserAccount;
import com.lilemy.moraaccountbook.model.enums.TransactionTypeEnum;
import com.lilemy.moraaccountbook.model.enums.user.UserAccountLoanDirectionEnum;
import com.lilemy.moraaccountbook.model.enums.user.UserAccountTypeEnum;
import com.lilemy.moraaccountbook.model.enums.user.UserRoleEnum;
import com.lilemy.moraaccountbook.model.vo.user.UserAccountManageVO;
import com.lilemy.moraaccountbook.model.vo.user.UserAccountVO;
import com.lilemy.moraaccountbook.service.UserAccountService;
import com.lilemy.moraaccountbook.service.UserService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户账户 服务层实现。
 *
 * @author lilemy
 * @since 2026-09-04
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

    @Resource
    private UserService userService;

    @Resource
    private AccountBookBillMapper accountBookBillMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAccount updateBalanceByBill(Long accountId, Long userId, BigDecimal amount, TransactionTypeEnum billType) {
        ThrowUtils.throwIf(amount == null || amount.signum() <= 0 || billType == null,
                ErrorCode.PARAMS_ERROR, "流水金额和收支类型不合法");
        return adjustBalanceByBill(accountId, userId, TransactionTypeEnum.INCOME.equals(billType) ? amount : amount.negate());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAccount adjustBalanceByBill(Long accountId, Long userId, BigDecimal incomeDelta) {
        ThrowUtils.throwIf(accountId == null || accountId <= 0 || userId == null || incomeDelta == null,
                ErrorCode.PARAMS_ERROR, "余额调整参数不合法");
        // 锁住账户直至事务提交，避免并发记账读取相同余额后互相覆盖。
        UserAccount account = this.getOne(QueryWrapper.create().eq(UserAccount::getId, accountId).forUpdate());
        ThrowUtils.throwIf(account == null, ErrorCode.NOT_FOUND_ERROR, "账户不存在");
        ThrowUtils.throwIf(!Objects.equals(account.getUserId(), userId), ErrorCode.NO_AUTH_ERROR, "账户不属于记账人");
        // 信用账户和借入账户记录欠款，收入减少欠款；其他账户记录资产，收入增加资产。
        boolean debt = UserAccountTypeEnum.CREDIT.equals(account.getAccountType())
                || (UserAccountTypeEnum.LOAN.equals(account.getAccountType())
                && UserAccountLoanDirectionEnum.BORROW.equals(account.getLoanDirection()));
        BigDecimal balance = account.getCurrentBalance().add(debt ? incomeDelta.negate() : incomeDelta);
        // 只校验最终余额，避免更新流水时因中间撤销状态而误报余额不足。
        ThrowUtils.throwIf(balance.signum() < 0, ErrorCode.PARAMS_ERROR, "调整后余额或欠款不能为负数");
        ThrowUtils.throwIf(balance.compareTo(CommonConstant.MAX_MONEY_DECIMAL) > 0,
                ErrorCode.PARAMS_ERROR, "账户余额超出允许范围");
        if (UserAccountTypeEnum.CREDIT.equals(account.getAccountType()) && account.getCreditLimit() != null) {
            ThrowUtils.throwIf(balance.compareTo(account.getCreditLimit()) > 0,
                    ErrorCode.PARAMS_ERROR, "调整后欠款超过信用额度");
        }
        if (incomeDelta.signum() != 0) {
            // 仅更新余额字段，避免覆盖账户名称等信息。
            boolean updated = this.updateById(UserAccount.builder().id(accountId).currentBalance(balance).build());
            ThrowUtils.throwIf(!updated, ErrorCode.OPERATION_ERROR, "更新账户余额失败");
        }
        account.setCurrentBalance(balance);
        return account;
    }

    @Override
    public Long createUserAccount(UserAccountCreateRequest request) {
        // 1.获取当前登录用户
        User loginUser = userService.getLoginUser();
        // 2.根据账户类型创建不同类型的账户
        UserAccount createAccount;
        if (request.getAccountType().equals(UserAccountTypeEnum.CREDIT)) {
            // 创建信用账户
            createAccount = createCreditUserAccount(request);
        } else if (request.getAccountType().equals(UserAccountTypeEnum.LOAN)) {
            // 创建借贷账户
            createAccount = createLoanUserAccount(request);
        } else {
            // 创建普通账户
            createAccount = createNormalUserAccount(request);
        }
        // 3.添加用户信息
        createAccount.setUserId(loginUser.getId());
        // 4.保存账户信息
        boolean save = this.save(createAccount);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "创建账户失败");
        return createAccount.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserAccount(UserAccountUpdateRequest request) {
        // 1.查询账户是否存在
        Long id = request.getId();
        UserAccount account = this.getById(id);
        ThrowUtils.throwIf(account == null, ErrorCode.NOT_FOUND_ERROR, "账户不存在");
        // 2.判断是否有操作权限
        ThrowUtils.throwIf(!hasOperationPermission(account), ErrorCode.NO_AUTH_ERROR, "无操作权限");
        // 3.根据账户类型调用不同的更新方法
        UserAccountTypeEnum accountType = account.getAccountType();
        UserAccount updated;
        if (accountType.equals(UserAccountTypeEnum.CREDIT)) {
            updated = updateCreditUserAccount(account, request);
        } else if (accountType.equals(UserAccountTypeEnum.LOAN)) {
            updated = updateLoanUserAccount(account, request);
        } else {
            updated = updateNormalUserAccount(request);
        }
        // 4.判断是否需要更新默认账户
        Boolean isDefault = request.getIsDefault();
        if (isDefault != null && isDefault && !account.getIsDefault()) {
            // 更新其他账户的默认账户为false
            List<UserAccount> defaultAccount = this.queryChain()
                    .eq(UserAccount::getUserId, account.getUserId())
                    .eq(UserAccount::getIsDefault, true)
                    .list();
            if (CollUtil.isNotEmpty(defaultAccount)) {
                Set<UserAccount> changeDefault = defaultAccount.stream()
                        .map(userAccount -> UserAccount.builder()
                                .id(userAccount.getId())
                                .isDefault(false)
                                .build())
                        .collect(Collectors.toSet());
                boolean result = this.updateBatch(changeDefault);
                ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "更新默认账户失败");
            }
        }
        // 5.更新账户信息
        boolean update = this.updateById(updated);
        ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR, "更新账户失败");
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUserAccount(UserAccountDeleteRequest request) {
        Long id = request.getId();
        Boolean isKeepBill = request.getIsKeepBill();
        // 1.查询账户是否存在
        UserAccount account = this.getOne(QueryWrapper.create().eq(UserAccount::getId, id).forUpdate());
        ThrowUtils.throwIf(account == null, ErrorCode.NOT_FOUND_ERROR, "账户不存在");
        // 2.判断是否有操作权限
        ThrowUtils.throwIf(!hasOperationPermission(account), ErrorCode.NO_AUTH_ERROR, "无操作权限");
        QueryWrapper billQuery = QueryWrapper.create().eq(AccountBookBill::getAccountId, id);
        if (accountBookBillMapper.selectCountByQuery(billQuery) > 0) {
            int affectedRows;
            if (isKeepBill) {
                AccountBookBill updated = UpdateEntity.of(AccountBookBill.class);
                updated.setAccountId(null);
                affectedRows = accountBookBillMapper.updateByQuery(updated, billQuery);
            } else {
                affectedRows = accountBookBillMapper.deleteByQuery(billQuery);
            }
            ThrowUtils.throwIf(affectedRows <= 0, ErrorCode.OPERATION_ERROR, "处理账户账单失败");
        }
        // 4.删除账户
        boolean result = this.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "删除账户失败");
        return true;
    }

    @Override
    public UserAccountVO getUserAccountVOById(Long id) {
        User loginUser = userService.getLoginUser();
        UserAccount userAccount = this.getById(id);
        ThrowUtils.throwIf(userAccount == null, ErrorCode.NOT_FOUND_ERROR, "账户不存在");
        ThrowUtils.throwIf(!loginUser.getId().equals(userAccount.getUserId()), ErrorCode.NO_AUTH_ERROR, "无权限访问");
        return BeanUtil.copyProperties(userAccount, UserAccountVO.class);
    }

    @Override
    public UserAccountManageVO getUserAccountManageVOById(Long id) {
        UserAccount userAccount = this.getById(id);
        ThrowUtils.throwIf(userAccount == null, ErrorCode.NOT_FOUND_ERROR, "账户不存在");
        UserAccountManageVO userAccountManageVO = BeanUtil.copyProperties(userAccount, UserAccountManageVO.class);
        User user = userService.getById(userAccount.getUserId());
        if (user != null) {
            userAccountManageVO.setUserPhone(user.getUserPhone());
            userAccountManageVO.setUserName(user.getUserName());
        }
        return userAccountManageVO;
    }

    @Override
    public PageResult<UserAccountVO> getUserAccountVOPage(UserAccountQueryRequest request, PageQuery pageQuery) {
        // 1.获取当前用户
        User loginUser = userService.getLoginUser();
        // 2.构建查询条件
        QueryWrapper queryWrapper = this.buildQueryWrapper(request, pageQuery);
        queryWrapper.eq(UserAccount::getUserId, loginUser.getId());
        // 3.分页查询
        Page<UserAccount> userAccountPage = this.page(pageQuery.build(), queryWrapper);
        // 4.转换为封装类
        Page<UserAccountVO> userAccountVOPage = userAccountPage.map(userAccount ->
                BeanUtil.copyProperties(userAccount, UserAccountVO.class));
        // 5.返回结果
        return PageResult.build(userAccountVOPage);
    }

    @Override
    public PageResult<UserAccountManageVO> getUserAccountManageVOPage(UserAccountQueryRequest request, PageQuery pageQuery) {
        // 1.构建查询条件
        QueryWrapper queryWrapper = this.buildQueryWrapper(request, pageQuery);
        // 2.分页查询
        Page<UserAccount> userAccountPage = this.page(pageQuery.build(), queryWrapper);
        List<UserAccount> records = userAccountPage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return PageResult.build();
        }
        // 3.获取用户信息
        Set<Long> userIds = records.stream().map(UserAccount::getUserId).collect(Collectors.toSet());
        Map<Long, User> userMap = userService.getUserMapByIds(userIds);
        // 4.转换为封装类
        Page<UserAccountManageVO> userAccountManageVOPage = userAccountPage.map(userAccount -> {
            UserAccountManageVO userAccountManageVO = new UserAccountManageVO();
            BeanUtil.copyProperties(userAccount, userAccountManageVO);
            // 设置用户信息
            Long userId = userAccount.getUserId();
            if (CollUtil.isNotEmpty(userMap) && userMap.containsKey(userId)) {
                User user = userMap.get(userId);
                userAccountManageVO.setUserPhone(user.getUserPhone());
                userAccountManageVO.setUserName(user.getUserName());
            }
            return userAccountManageVO;
        });
        // 5.返回结果
        return PageResult.build(userAccountManageVOPage);
    }

    @Override
    public QueryWrapper buildQueryWrapper(UserAccountQueryRequest request, PageQuery pageQuery) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (pageQuery != null) {
            queryWrapper = pageQuery.buildOrderQuery();
        }
        if (request == null) {
            return queryWrapper;
        }
        Long userId = request.getUserId();
        String accountName = request.getAccountName();
        UserAccountTypeEnum accountType = request.getAccountType();
        Boolean isDefault = request.getIsDefault();
        queryWrapper.like(UserAccount::getAccountName, accountName, StringUtils::isNotBlank)
                .eq(UserAccount::getAccountType, accountType, Objects::nonNull)
                .eq(UserAccount::getUserId, userId, Objects::nonNull)
                .eq(UserAccount::getIsDefault, isDefault, Objects::nonNull);
        return queryWrapper;
    }

    /**
     * 判断当前用户是否有操作该账户的权限。
     *
     * @param account 账户信息
     * @return 是否有操作权限
     */
    private Boolean hasOperationPermission(UserAccount account) {
        // 判断当前用户是否有操作该账户的权限
        Long userId = account.getUserId();
        User loginUser = userService.getLoginUser();
        if (loginUser.getUserRole().equals(UserRoleEnum.ADMIN)) {
            return true;
        }
        Long currentUserId = loginUser.getId();
        return userId.equals(currentUserId);
    }

    /**
     * 创建普通账户。
     *
     * @param request 创建账户请求
     * @return 账户信息
     */
    private UserAccount createNormalUserAccount(UserAccountCreateRequest request) {
        return UserAccount.builder()
                .accountName(request.getAccountName())
                .accountType(request.getAccountType())
                .bankName(request.getBankName())
                .currentBalance(request.getCurrentBalance())
                .accountNoTail(request.getAccountNoTail())
                .icon(request.getIcon())
                .isDefault(request.getIsDefault())
                .remark(request.getRemark())
                .build();
    }

    /**
     * 创建信用账户。
     *
     * @param request 创建账户请求
     * @return 账户信息
     */
    private UserAccount createCreditUserAccount(UserAccountCreateRequest request) {
        BigDecimal currentBalance = request.getCurrentBalance();
        BigDecimal creditLimit = request.getCreditLimit();
        ThrowUtils.throwIf(creditLimit.compareTo(currentBalance) < 0, ErrorCode.PARAMS_ERROR, "信用额度不能小于欠款余额");
        return UserAccount.builder()
                .accountName(request.getAccountName())
                .accountType(request.getAccountType())
                .bankName(request.getBankName())
                .currentBalance(currentBalance)
                .creditLimit(creditLimit)
                .creditStatementDay(request.getCreditStatementDay())
                .creditRepaymentDay(request.getCreditRepaymentDay())
                .accountNoTail(request.getAccountNoTail())
                .icon(request.getIcon())
                .isDefault(request.getIsDefault())
                .remark(request.getRemark())
                .build();
    }

    /**
     * 创建借贷账户。
     *
     * @param request 创建账户请求
     * @return 账户信息
     */
    private UserAccount createLoanUserAccount(UserAccountCreateRequest request) {
        LocalDate loanDate = request.getLoanDate();
        LocalDate repaymentDate = request.getRepaymentDate();
        if (loanDate != null && repaymentDate != null) {
            ThrowUtils.throwIf(repaymentDate.isBefore(loanDate), ErrorCode.PARAMS_ERROR, "还款日期不能早于发生日期");
        }
        return UserAccount.builder()
                .accountName(request.getAccountName())
                .accountType(request.getAccountType())
                .currentBalance(request.getCurrentBalance())
                .loanUser(request.getLoanUser())
                .loanDirection(request.getLoanDirection())
                .loanDate(loanDate)
                .repaymentDate(repaymentDate)
                .accountNoTail(request.getAccountNoTail())
                .icon(request.getIcon())
                .isDefault(request.getIsDefault())
                .remark(request.getRemark())
                .build();
    }

    /**
     * 更新普通用户账户信息。
     *
     * @param request 更新请求
     * @return 更新后的账户信息
     */
    private UserAccount updateNormalUserAccount(UserAccountUpdateRequest request) {
        // 1.设置更新的字段
        return UserAccount.builder()
                .id(request.getId())
                .accountName(request.getAccountName())
                .bankName(request.getBankName())
                .accountNoTail(request.getAccountNoTail())
                .icon(request.getIcon())
                .isDefault(request.getIsDefault())
                .remark(request.getRemark())
                .build();
    }

    /**
     * 更新信用用户账户信息。
     *
     * @param account 账户信息
     * @param request 更新请求
     * @return 更新后的账户信息
     */
    private UserAccount updateCreditUserAccount(UserAccount account, UserAccountUpdateRequest request) {
        BigDecimal creditLimit = request.getCreditLimit();
        if (creditLimit == null) {
            creditLimit = account.getCreditLimit();
        }
        // 1.参数校验
        BigDecimal currentBalance = account.getCurrentBalance();
        ThrowUtils.throwIf(creditLimit.compareTo(currentBalance) < 0, ErrorCode.PARAMS_ERROR, "信用额度不能小于欠款余额");
        // 2.设置更新的字段
        return UserAccount.builder()
                .id(request.getId())
                .accountName(request.getAccountName())
                .bankName(request.getBankName())
                .creditLimit(creditLimit)
                .creditStatementDay(request.getCreditStatementDay())
                .creditRepaymentDay(request.getCreditRepaymentDay())
                .accountNoTail(request.getAccountNoTail())
                .icon(request.getIcon())
                .isDefault(request.getIsDefault())
                .remark(request.getRemark())
                .build();
    }

    /**
     * 更新借款用户账户信息。
     *
     * @param account 账户信息
     * @param request 更新请求
     * @return 更新后的账户信息
     */
    private UserAccount updateLoanUserAccount(UserAccount account, UserAccountUpdateRequest request) {
        LocalDate loanDate = request.getLoanDate();
        LocalDate repaymentDate = request.getRepaymentDate();
        if (loanDate == null) {
            loanDate = account.getLoanDate();
        }
        if (repaymentDate == null) {
            repaymentDate = account.getRepaymentDate();
        }
        // 1.参数校验
        ThrowUtils.throwIf(repaymentDate.isBefore(loanDate), ErrorCode.PARAMS_ERROR, "还款日期不能早于发生日期");
        // 2.设置更新的字段
        return UserAccount.builder()
                .id(request.getId())
                .accountName(request.getAccountName())
                .loanUser(request.getLoanUser())
                .loanDate(loanDate)
                .repaymentDate(repaymentDate)
                .icon(request.getIcon())
                .isDefault(request.getIsDefault())
                .remark(request.getRemark())
                .build();
    }
}
