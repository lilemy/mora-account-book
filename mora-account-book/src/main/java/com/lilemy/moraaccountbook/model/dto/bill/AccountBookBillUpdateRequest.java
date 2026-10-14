package com.lilemy.moraaccountbook.model.dto.bill;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账本流水更新请求
 *
 * @author lilemy
 * @date 2026-09-11
 */
@Data
@Schema(description = "账本流水更新请求")
public class AccountBookBillUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 3264739966168171832L;

    /**
     * 待更新流水 ID；不允许修改所属账本和记账人。
     */
    @NotNull(message = "流水 ID 不能为空")
    @Positive(message = "流水 ID 必须为正数")
    private Long id;

    /**
     * 账户 ID：null 保留原账户，0 解除关联，正数切换账户。
     */
    @Schema(description = "账户 ID：不传保留，0 解除关联，正数切换账户")
    @PositiveOrZero(message = "账户 ID 不能为负数")
    private Long accountId;

    /**
     * 分类 ID；不传保留原分类，收支类型由新分类确定。
     */
    @Positive(message = "分类 ID 必须为正数")
    private Long categoryId;

    /**
     * 流水金额；不传保留原金额。
     */
    @DecimalMin(value = "0", inclusive = false, message = "流水金额必须大于 0")
    @Digits(integer = 16, fraction = 2, message = "流水金额最多 16 位整数、2 位小数")
    private BigDecimal amount;

    /**
     * 发生时间；不传保留原时间。
     */
    @PastOrPresent(message = "时间不能晚于当前时间")
    private LocalDateTime billTime;

    /**
     * 备注；null 保留原备注，空字符串清空备注。
     */
    @Size(max = 512, message = "流水备注不能超过 512 个字符")
    private String remark;

}
