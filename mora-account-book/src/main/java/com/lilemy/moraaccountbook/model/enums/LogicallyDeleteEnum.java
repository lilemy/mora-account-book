package com.lilemy.moraaccountbook.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lilemy
 * @date 2026-08-20
 */
@Getter
@AllArgsConstructor
public enum LogicallyDeleteEnum {

    NOT_DELETED(0, "未删除"),
    DELETED(1, "已删除");

    @EnumValue
    private final int code;

    private final String desc;
}
