package com.lilemy.moraaccountbook.model.dto.user;

import com.lilemy.moraaccountbook.model.enums.user.UserAccountLoanDirectionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 用户账户更新请求
 *
 * @author lilemy
 * @date 2026-09-04
 */
@Data
@Schema(description = "用户账户更新请求")
public class UserAccountUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -6703644799022906326L;

    /**
     * 账户 ID
     */
    @Schema(description = "账户 ID")
    @NotNull(message = "账户 ID 不能为空")
    private Long id;

    /**
     * 账户名称
     */
    @Schema(description = "账户名称")
    @Size(max = 50, message = "账户名称过长")
    private String accountName;

    /**
     * 银行名称
     */
    @Schema(description = "银行名称")
    @Size(max = 50, message = "银行名称过长")
    private String bankName;

    /**
     * 信用额度，仅信用账户使用
     */
    @Schema(description = "信用额度，仅信用账户使用")
    @Min(value = 0, message = "信用额度不能为负")
    private BigDecimal creditLimit;

    /**
     * 信用账户出账日
     */
    @Schema(description = "信用账户出账日")
    @Min(value = 1, message = "信用账户出账日不能为零")
    @Max(value = 28, message = "信用账户出账日不能超过28日")
    private Integer creditStatementDay;

    /**
     * 信用账户还款日
     */
    @Schema(description = "信用账户还款日")
    @Min(value = 1, message = "信用账户还款日不能为零")
    @Max(value = 28, message = "信用账户还款日不能超过28日")
    private Integer creditRepaymentDay;

    /**
     * 借贷人姓名
     */
    @Schema(description = "借贷人姓名")
    @Size(max = 50, message = "借贷人姓名过长")
    private String loanUser;

    /**
     * 借贷方向：0借出，1借入，仅借贷账户使用
     */
    @Schema(description = "借贷方向，仅借贷账户使用")
    private UserAccountLoanDirectionEnum loanDirection;

    /**
     * 借贷发生日期，仅借贷账户使用
     */
    @Schema(description = "借贷发生日期，仅借贷账户使用")
    private LocalDate loanDate;

    /**
     * 计划还款日期，仅借贷账户使用
     */
    @Schema(description = "计划还款日期，仅借贷账户使用")
    private LocalDate repaymentDate;

    /**
     * 银行卡或信用卡卡号后四位
     */
    @Schema(description = "银行卡或信用卡卡号后四位")
    @Size(max = 4, message = "银行卡或信用卡卡号后四位过长")
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
