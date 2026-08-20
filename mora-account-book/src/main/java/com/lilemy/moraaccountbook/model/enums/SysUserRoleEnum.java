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
public enum SysUserRoleEnum {

    USER(0, "用户"),
    ADMIN(1, "管理员");

    @EnumValue
    private final int code;

    private final String message;
}
