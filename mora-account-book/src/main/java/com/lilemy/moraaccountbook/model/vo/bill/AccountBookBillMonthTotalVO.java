package com.lilemy.moraaccountbook.model.vo.bill;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账本流水按月统计封装
 *
 * @author lilemy
 * @date 2026-10-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "账本流水按月统计封装")
public class AccountBookBillMonthTotalVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3557225486069346657L;

    /**
     * 月份
     */
    @Schema(description = "月份")
    private Integer month;

    /**
     * 总收入
     */
    @Schema(description = "总收入")
    private BigDecimal totalIncome;

    /**
     * 总支出
     */
    @Schema(description = "总支出")
    private BigDecimal totalExpense;
}
