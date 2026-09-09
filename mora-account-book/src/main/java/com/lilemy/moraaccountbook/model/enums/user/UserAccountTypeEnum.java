package com.lilemy.moraaccountbook.model.enums.user;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户账户类型枚举
 *
 * @author lilemy
 * @date 2026-09-04
 */
@Getter
@AllArgsConstructor
public enum UserAccountTypeEnum {

    CAPITAL(0, "资金账户"),
    CREDIT(1, "信用账户"),
    RECHARGE(2, "充值账户"),
    INVEST(3, "理财账户"),
    LOAN(4, "借贷账户");

    @EnumValue
    private final int code;

    private final String message;
}
