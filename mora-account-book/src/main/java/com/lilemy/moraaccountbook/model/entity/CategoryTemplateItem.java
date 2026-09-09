package com.lilemy.moraaccountbook.model.entity;

import com.lilemy.moraaccountbook.model.enums.AccountBookCategoryLevelEnum;
import com.lilemy.moraaccountbook.model.enums.AccountBookCategoryTypeEnum;
import com.lilemy.moraaccountbook.model.enums.EnableStatusEnum;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分类模板项 实体类。
 *
 * @author lilemy
 * @since 2026-08-28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table("category_template_item")
public class CategoryTemplateItem extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类模板项 ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 分类模板 ID
     */
    private Long templateId;

    /**
     * 父分类模板项 ID，一级分类为 0
     */
    private Long parentId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类类型(1支出 2收入)
     */
    private AccountBookCategoryTypeEnum categoryType;

    /**
     * 分类层级(1一级 2二级)
     */
    private AccountBookCategoryLevelEnum categoryLevel;

    /**
     * 分类图标标识
     */
    private String icon;

    /**
     * 分类颜色
     */
    private String color;

    /**
     * 排序值，值越小越靠前
     */
    private Integer sortOrder;

    /**
     * 模板项状态(0启用 1停用)
     */
    private EnableStatusEnum status;

}
