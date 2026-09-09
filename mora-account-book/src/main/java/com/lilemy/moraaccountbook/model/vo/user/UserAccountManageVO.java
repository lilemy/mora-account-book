package com.lilemy.moraaccountbook.model.vo.user;

import com.lilemy.moraaccountbook.model.enums.UserAccountLoanDirectionEnum;
import com.lilemy.moraaccountbook.model.enums.UserAccountTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户账户管理封装
 *
 * @author lilemy
 * @date 2026-09-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户账户管理封装")
public class UserAccountManageVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5049609281033115905L;

    /**
     * 账户 ID
     */
    @Schema(description = "账户 ID")
    private Long id;

    /**
     * 所属用户 ID
     */
    @Schema(description = "所属用户 ID")
    private Long userId;

    /**
     * 所属用户手机号
     */
    @Schema(description = "所属用户手机号")
    private String userPhone;

    /**
     * 所属用户昵称
     */
    @Schema(description = "所属用户昵称")
    private String userName;

    /**
     * 账户名称
     */
    @Schema(description = "账户名称")
    private String accountName;

    /**
     * 账户类型：0资金账户，1信用账户，2充值账户，3理财账户，4借贷账户
     */
    @Schema(description = "账户类型")
    private UserAccountTypeEnum accountType;

    /**
     * 银行名称
     */
    @Schema(description = "银行名称")
    private String bankName;

    /**
     * 信用账户出账日
     */
    @Schema(description = "信用账户出账日")
    private Integer creditStatementDay;

    /**
     * 信用账户还款日
     */
    @Schema(description = "信用账户还款日")
    private Integer creditRepaymentDay;

    /**
     * 借贷人姓名
     */
    @Schema(description = "借贷人姓名")
    private String loanUser;

    /**
     * 借贷方向：0借出，1借入，仅借贷账户使用
     */
    @Schema(description = "借贷方向")
    private UserAccountLoanDirectionEnum loanDirection;

    /**
     * 借贷发生日期，仅借贷账户使用
     */
    @Schema(description = "借贷发生日期")
    private LocalDate loanDate;

    /**
     * 计划还款日期，仅借贷账户使用
     */
    @Schema(description = "计划还款日期")
    private LocalDate repaymentDate;

    /**
     * 银行卡或信用卡卡号后四位
     */
    @Schema(description = "银行卡或信用卡卡号后四位")
    private String accountNoTail;

    /**
     * 账户图标标识
     */
    @Schema(description = "账户图标标识")
    private String icon;

    /**
     * 是否为用户默认账户
     */
    @Schema(description = "是否为用户默认账户")
    private Boolean isDefault;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
