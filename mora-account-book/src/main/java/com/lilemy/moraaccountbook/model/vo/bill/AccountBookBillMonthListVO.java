package com.lilemy.moraaccountbook.model.vo.bill;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author lilemy
 * @date 2026-10-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "账本收入/支出流水封装")
public class AccountBookBillMonthListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5694383477207153917L;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer order;

    /**
     * 日期
     */
    @Schema(description = "日期")
    private LocalDate date;

    /**
     * 收入金额
     */
    @Schema(description = "收入金额")
    private BigDecimal income;

    /**
     * 支出金额
     */
    @Schema(description = "支出金额")
    private BigDecimal expense;

    /**
     * 流水列表
     */
    @Schema(description = "流水列表")
    private List<AccountBookBillVO> bills;
}
