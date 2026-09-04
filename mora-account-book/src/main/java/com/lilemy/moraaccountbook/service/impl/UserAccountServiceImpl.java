package com.lilemy.moraaccountbook.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.lilemy.moraaccountbook.exception.ErrorCode;
import com.lilemy.moraaccountbook.exception.ThrowUtils;
import com.lilemy.moraaccountbook.mapper.UserAccountMapper;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountCreateRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountDeleteRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountUpdateRequest;
import com.lilemy.moraaccountbook.model.entity.User;
import com.lilemy.moraaccountbook.model.entity.UserAccount;
import com.lilemy.moraaccountbook.model.enums.UserAccountTypeEnum;
import com.lilemy.moraaccountbook.model.enums.UserRoleEnum;
import com.lilemy.moraaccountbook.service.UserAccountService;
import com.lilemy.moraaccountbook.service.UserService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
    public Boolean deleteUserAccount(UserAccountDeleteRequest request) {
        Long id = request.getId();
        Boolean isKeepBill = request.getIsKeepBill();
        // 1.查询账户是否存在
        UserAccount account = this.getById(id);
        ThrowUtils.throwIf(account == null, ErrorCode.NOT_FOUND_ERROR, "账户不存在");
        // 2.判断是否有操作权限
        ThrowUtils.throwIf(!hasOperationPermission(account), ErrorCode.NO_AUTH_ERROR, "无操作权限");
        // TODO 3.判断账户下是否有账单，如果存在则根据isKeepBill决定是否删除账单
        if (isKeepBill) {
            // 如果保留，则移除账单与账户的关联
        } else {
            // 如果不保留，则删除账单
        }
        // 4.删除账户
        boolean result = this.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "删除账户失败");
        return true;
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
                .subAccountName(request.getSubAccountName())
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
                .subAccountName(request.getSubAccountName())
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
        ThrowUtils.throwIf(repaymentDate.isBefore(loanDate), ErrorCode.PARAMS_ERROR, "还款日期不能早于发生日期");
        return UserAccount.builder()
                .accountName(request.getAccountName())
                .accountType(request.getAccountType())
                .subAccountName(request.getSubAccountName())
                .currentBalance(request.getCurrentBalance())
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
                .subAccountName(request.getSubAccountName())
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
        // 1.参数校验
        BigDecimal currentBalance = account.getCurrentBalance();
        ThrowUtils.throwIf(creditLimit.compareTo(currentBalance) < 0, ErrorCode.PARAMS_ERROR, "信用额度不能小于欠款余额");
        // 2.设置更新的字段
        return UserAccount.builder()
                .id(request.getId())
                .accountName(request.getAccountName())
                .subAccountName(request.getSubAccountName())
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
                .subAccountName(request.getSubAccountName())
                .loanDate(loanDate)
                .repaymentDate(repaymentDate)
                .icon(request.getIcon())
                .isDefault(request.getIsDefault())
                .remark(request.getRemark())
                .build();
    }
}
