package com.lilemy.moraaccountbook.model.dto.user;

import com.lilemy.moraaccountbook.model.enums.UserAccountLoanDirection;
import com.lilemy.moraaccountbook.model.enums.UserAccountTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 用户账户创建请求
 *
 * @author lilemy
 * @date 2026-09-04
 */
@Data
@Schema(description = "用户账户创建请求")
public class UserAccountCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 7930108857520935189L;

    /**
     * 账户名称
     */
    @Schema(description = "账户名称")
    @NotBlank(message = "账户名称不能为空")
    @Size(max = 50, message = "账户名称不能超过50个字符")
    private String accountName;

    /**
     * 账户类型：0资金账户，1信用账户，2充值账户，3理财账户，4借贷账户
     */
    @Schema(description = "账户类型")
    @NotNull(message = "账户类型不能为空")
    private UserAccountTypeEnum accountType;

    /**
     * 银行名称
     */
    @Schema(description = "银行名称")
    @Size(max = 50, message = "银行名称不能超过50个字符")
    private String bankName;

    /**
     * 当前余额或欠款余额
     */
    @Schema(description = "当前余额或欠款余额")
    @NotNull(message = "当前余额不能为空")
    @Min(value = 0, message = "当前余额不能小于0")
    private BigDecimal currentBalance;

    /**
     * 信用额度，仅信用账户使用
     */
    @Schema(description = "信用额度")
    @Min(value = 0, message = "信用额度不能小于0")
    private BigDecimal creditLimit;

    /**
     * 信用账户出账日
     */
    @Schema(description = "信用账户出账日")
    @Min(value = 0, message = "信用账户出账日不能小于0")
    @Max(value = 28, message = "信用账户出账日不能大于28")
    private Integer creditStatementDay;

    /**
     * 信用账户还款日
     */
    @Schema(description = "信用账户还款日")
    @Min(value = 0, message = "信用账户还款日不能小于0")
    @Max(value = 28, message = "信用账户还款日不能大于28")
    private Integer creditRepaymentDay;

    /**
     * 借贷人姓名
     */
    @Schema(description = "借贷人姓名")
    @Size(max = 50, message = "借贷人姓名不能超过50个字符")
    private String loanUser;

    /**
     * 借贷方向：0借出，1借入，仅借贷账户使用
     */
    @Schema(description = "借贷方向")
    private UserAccountLoanDirection loanDirection;

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
    @Size(max = 4, message = "银行卡或信用卡卡号后四位不能超过4个字符")
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
}
