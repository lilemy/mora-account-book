package com.lilemy.moraaccountbook.model.enums.book;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账本状态枚举
 *
 * @author lilemy
 * @date 2026-08-26
 */
@Getter
@AllArgsConstructor
public enum AccountBookStatusEnum {

    NORMAL(0, "正常"),
    ARCHIVED(1, "已归档");

    @EnumValue
    private final int code;

    private final String desc;
}
