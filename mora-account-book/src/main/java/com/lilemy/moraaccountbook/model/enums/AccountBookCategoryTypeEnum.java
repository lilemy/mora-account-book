package com.lilemy.moraaccountbook.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账本分类类型枚举
 *
 * @author lilemy
 * @date 2026-08-28
 */
@Getter
@AllArgsConstructor
public enum AccountBookCategoryTypeEnum {

    INCOME(1, "收入"),
    EXPENSE(2, "支出");

    @EnumValue
    private final int code;

    private final String desc;
}
