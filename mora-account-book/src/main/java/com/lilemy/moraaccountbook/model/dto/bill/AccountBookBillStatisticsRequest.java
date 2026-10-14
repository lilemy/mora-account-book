package com.lilemy.moraaccountbook.model.dto.bill;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 账本统计请求参数
 *
 * @author lilemy
 * @date 2026-09-14
 */
@Data
@Schema(description = "账本统计请求参数")
public class AccountBookBillStatisticsRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -7550115860513190168L;


}
