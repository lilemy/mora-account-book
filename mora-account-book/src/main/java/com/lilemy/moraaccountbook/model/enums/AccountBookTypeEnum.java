package com.lilemy.moraaccountbook.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账本类型枚举
 *
 * @author lilemy
 * @date 2026-08-26
 */
@Getter
@AllArgsConstructor
public enum AccountBookTypeEnum {

    PERSONAL(0, "个人账本"),
    SHARE(1, "共享账本");

    @EnumValue
    private final int code;

    private final String desc;
}

