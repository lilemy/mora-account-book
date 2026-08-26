package com.lilemy.moraaccountbook.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账本成员状态枚举
 *
 * @author lilemy
 * @date 2026-08-26
 */
@Getter
@AllArgsConstructor
public enum AccountBookMemberStatusEnum {

    NORMAL(0, "正常"),
    REMOVED(1, "已移除");

    @EnumValue
    private final int code;

    private final String desc;
}
