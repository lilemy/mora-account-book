package com.lilemy.moraaccountbook.model.enums.user;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户性别枚举
 *
 * @author lilemy
 * @date 2026-08-20
 */
@Getter
@AllArgsConstructor
public enum UserGenderEnum {

    UNKNOWN(0, "未填写"),
    MALE(1, "男"),
    FEMALE(2, "女");

    @EnumValue
    private final int code;

    private final String desc;
}
