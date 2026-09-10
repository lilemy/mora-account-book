package com.lilemy.moraaccountbook.model.vo.bill;

import com.lilemy.moraaccountbook.model.enums.TransactionTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账本收入/支出流水封装
 *
 * @author lilemy
 * @date 2026-09-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "账本收入/支出流水封装")
public class AccountBookBillManageVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7438114368261114975L;

    /**
     * 流水 ID
     */
    @Schema(description = "流水 ID")
    private Long id;

    /**
     * 所属账本 ID
     */
    @Schema(description = "所属账本 ID")
    private Long bookId;

    /**
     * 所属账本名称
     */
    @Schema(description = "所属账本名称")
    private String bookName;

    /**
     * 记账人用户 ID
     */
    @Schema(description = "记账人用户 ID")
    private Long userId;

    /**
     * 记账人用户手机号
     */
    @Schema(description = "记账人用户手机号")
    private String userPhone;

    /**
     * 记账人用户名称
     */
    @Schema(description = "记账人用户名称")
    private String userName;

    /**
     * 关联账户 ID
     */
    @Schema(description = "关联账户 ID")
    private Long accountId;

    /**
     * 账户名称
     */
    @Schema(description = "账户名称")
    private String accountName;

    /**
     * 关联账本分类 ID
     */
    @Schema(description = "关联账本分类 ID")
    private Long categoryId;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String categoryName;

    /**
     * 流水类型：1收入，2支出
     */
    @Schema(description = "流水类型")
    private TransactionTypeEnum billType;

    /**
     * 流水金额
     */
    @Schema(description = "流水金额")
    private BigDecimal amount;

    /**
     * 流水发生时间
     */
    @Schema(description = "流水发生时间")
    private LocalDateTime billTime;

    /**
     * 流水备注
     */
    @Schema(description = "流水备注")
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
