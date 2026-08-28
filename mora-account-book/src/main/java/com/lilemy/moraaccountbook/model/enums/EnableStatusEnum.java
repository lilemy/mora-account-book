package com.lilemy.moraaccountbook.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 启用状态枚举
 *
 * @author lilemy
 * @date 2026-08-28
 */
@Getter
@AllArgsConstructor
public enum EnableStatusEnum {

    ENABLE(0, "启用"),
    DISABLE(1, "禁用");

    @EnumValue
    private final int code;

    private final String desc;
}
