package com.lilemy.moraaccountbook.model.dto.bill;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 账本流水日历查询请求
 *
 * @author lilemy
 * @date 2026-10-13
 */
@Data
@Schema(description = "账本流水日历查询请求")
public class AccountBookBillCalendarQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -2953419754206785053L;

    /**
     * 所属账本 ID
     */
    @Schema(description = "所属账本 ID")
    @NotNull(message = "所属账本不能为空")
    private Long bookId;

    /**
     * 日历日期
     */
    @Schema(description = "日历日期")
    @NotNull(message = "日历日期不能为空")
    private LocalDate calendarDate;
}
