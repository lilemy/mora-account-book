package com.lilemy.moraaccountbook.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户账户借贷方向枚举
 *
 * @author lilemy
 * @date 2026-09-04
 */
@Getter
@AllArgsConstructor
public enum UserAccountLoanDirection {

    LEND(0, "借出"),
    BORROW(1, "借入");

    @EnumValue
    private final int code;

    private final String message;
}
