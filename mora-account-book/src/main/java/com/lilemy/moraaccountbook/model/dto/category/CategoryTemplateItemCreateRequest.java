package com.lilemy.moraaccountbook.model.dto.category;

import com.lilemy.moraaccountbook.model.enums.AccountBookCategoryTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分类模板项创建请求
 *
 * @author lilemy
 * @date 2026-08-28
 */
@Data
@Schema(description = "分类模板项创建请求")
public class CategoryTemplateItemCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 3263015325628544411L;

    /**
     * 分类模板 ID
     */
    @NotNull(message = "分类模板 ID 不能为空")
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
    @NotBlank(message = "分类名称不能为空")
    @Schema(description = "分类名称")
    private String categoryName;

    /**
     * 分类类型(1支出 2收入)
     */
    @NotNull(message = "分类类型不能为空")
    @Schema(description = "分类类型")
    private AccountBookCategoryTypeEnum categoryType;

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
}
