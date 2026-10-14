package com.lilemy.moraaccountbook.model.dto.bill;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账本流水创建请求参数
 *
 * @author lilemy
 * @date 2026-09-11
 */
@Data
@Schema(description = "账本流水创建请求参数")
public class AccountBookBillCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -6877343789757325235L;

    /**
     * 所属账本 ID
     */
    @Schema(description = "所属账本 ID")
    @NotNull(message = "所属账本不能为空")
    @Positive(message = "账本 ID 必须为正数")
    private Long bookId;

    /**
     * 关联账户 ID
     */
    @Schema(description = "关联账户 ID")
    @PositiveOrZero(message = "账户 ID 不能为负数")
    private Long accountId;

    /**
     * 关联账本分类 ID
     */
    @Schema(description = "关联账本分类 ID")
    @NotNull(message = "账本分类不能为空")
    @Positive(message = "分类 ID 必须为正数")
    private Long categoryId;

    /**
     * 流水金额
     */
    @Schema(description = "流水金额")
    @NotNull(message = "流水金额不能为空")
    @DecimalMin(value = "0", inclusive = false, message = "流水金额必须大于 0")
    @Digits(integer = 16, fraction = 2, message = "流水金额最多 16 位整数、2 位小数")
    private BigDecimal amount;

    /**
     * 流水发生时间；不传时使用当前时间。
     */
    @Schema(description = "流水发生时间")
    @PastOrPresent(message = "时间不能晚于当前时间")
    private LocalDateTime billTime;

    /**
     * 流水备注
     */
    @Schema(description = "流水备注")
    @Size(max = 512, message = "流水备注不能超过 512 个字符")
    private String remark;
}
