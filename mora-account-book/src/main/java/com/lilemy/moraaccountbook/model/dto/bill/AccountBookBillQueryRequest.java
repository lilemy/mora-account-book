package com.lilemy.moraaccountbook.model.dto.bill;

import com.lilemy.moraaccountbook.model.enums.TransactionTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账本收入/支出流水查询请求参数
 *
 * @author lilemy
 * @date 2026-09-09
 */
@Data
@Schema(description = "账本流水查询请求参数")
public class AccountBookBillQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -4287619274796740063L;

    /**
     * 所属账本 ID
     */
    @Schema(description = "所属账本 ID")
    private Long bookId;

    /**
     * 记账人用户 ID
     */
    @Schema(description = "记账人用户 ID")
    private Long userId;

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
     * 流水开始时间
     */
    @Schema(description = "流水开始时间")
    private LocalDateTime startBillTime;

    /**
     * 流水结束时间
     */
    @Schema(description = "流水结束时间")
    private LocalDateTime endBillTime;
}
