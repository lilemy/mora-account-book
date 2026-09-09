package com.lilemy.moraaccountbook.model.vo.category;

import com.lilemy.moraaccountbook.model.enums.CategoryLevelEnum;
import com.lilemy.moraaccountbook.model.enums.TransactionTypeEnum;
import com.lilemy.moraaccountbook.model.enums.EnableStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author lilemy
 * @date 2026-08-28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类模板封装")
public class CategoryTemplateItemVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3852990181776051874L;

    /**
     * 分类模板项 ID
     */
    @Schema(description = "分类模板项 ID")
    private Long id;

    /**
     * 分类模板 ID
     */
    @Schema(description = "分类模板 ID")
    private Long templateId;

    /**
     * 父分类模板项 ID，一级分类为 0
     */
    @Schema(description = "父分类模板项 ID，一级分类为 0")
    private Long parentId;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String categoryName;

    /**
     * 分类类型(1支出 2收入)
     */
    @Schema(description = "分类类型")
    private TransactionTypeEnum categoryType;

    /**
     * 分类层级(1一级 2二级)
     */
    @Schema(description = "分类层级")
    private CategoryLevelEnum categoryLevel;

    /**
     * 分类图标标识
     */
    @Schema(description = "分类图标标识")
    private String icon;

    /**
     * 分类颜色
     */
    @Schema(description = "分类颜色")
    private String color;

    /**
     * 排序值，值越小越靠前
     */
    @Schema(description = "排序值，值越小越靠前")
    private Integer sortOrder;

    /**
     * 模板项状态(0启用 1停用)
     */
    @Schema(description = "模板项状态")
    private EnableStatusEnum status;

    /**
     * 子分类模板项
     */
    @Schema(description = "子分类模板项")
    private List<CategoryTemplateItemVO> children;
}
