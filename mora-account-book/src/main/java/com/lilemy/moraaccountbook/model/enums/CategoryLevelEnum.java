package com.lilemy.moraaccountbook.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账本分类层级枚举
 *
 * @author lilemy
 * @date 2026-08-28
 */
@Getter
@AllArgsConstructor
public enum CategoryLevelEnum {

    LEVEL_1(1, "一级分类"),
    LEVEL_2(2, "二级分类");

    @EnumValue
    private final int code;

    private final String desc;

    /**
     * 根据父级id获取分类层级
     *
     * @param parentId 父id
     * @return 分类分层
     */
    public static CategoryLevelEnum getLevelByParent(Long parentId) {
        if (parentId == null || parentId == 0) {
            return LEVEL_1;
        }
        return LEVEL_2;
    }
}
