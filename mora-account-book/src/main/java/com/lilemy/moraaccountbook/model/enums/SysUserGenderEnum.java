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
public enum SysUserGenderEnum {

    UNKNOWN(0, "未填写"),
    MALE(1, "男"),
    FEMALE(2, "女");

    @EnumValue
    private final int code;

    private final String desc;
}
