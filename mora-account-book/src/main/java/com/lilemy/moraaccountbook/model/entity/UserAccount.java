package com.lilemy.moraaccountbook.model.entity;

import com.lilemy.moraaccountbook.model.enums.UserAccountLoanDirection;
import com.lilemy.moraaccountbook.model.enums.UserAccountTypeEnum;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 用户账户 实体类。
 *
 * @author lilemy
 * @since 2026-09-04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table("user_account")
public class UserAccount extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 账户 ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 所属用户 ID
     */
    private Long userId;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 账户类型：0资金账户，1信用账户，2充值账户，3理财账户，4借贷账户
     */
    private UserAccountTypeEnum accountType;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 当前余额或欠款余额
     */
    private BigDecimal currentBalance;

    /**
     * 信用额度，仅信用账户使用
     */
    private BigDecimal creditLimit;

    /**
     * 信用账户出账日
     */
    private Integer creditStatementDay;

    /**
     * 信用账户还款日
     */
    private Integer creditRepaymentDay;

    /**
     * 借贷人姓名
     */
    private String loanUser;

    /**
     * 借贷方向：0借出，1借入，仅借贷账户使用
     */
    private UserAccountLoanDirection loanDirection;

    /**
     * 借贷发生日期，仅借贷账户使用
     */
    private LocalDate loanDate;

    /**
     * 计划还款日期，仅借贷账户使用
     */
    private LocalDate repaymentDate;

    /**
     * 银行卡或信用卡卡号后四位
     */
    private String accountNoTail;

    /**
     * 账户图标标识
     */
    private String icon;

    /**
     * 是否为用户默认账户
     */
    private Boolean isDefault;

    /**
     * 备注
     */
    private String remark;

}
