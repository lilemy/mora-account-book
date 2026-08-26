package com.lilemy.moraaccountbook.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账本成员角色枚举
 *
 * @author lilemy
 * @date 2026-08-26
 */
@Getter
@AllArgsConstructor
public enum AccountBookMemberRoleEnum {

    OWNER(0, "所有者"),
    ADMIN(1, "管理员"),
    MEMBER(2, "普通成员");

    @EnumValue
    private final int code;

    private final String desc;
}
