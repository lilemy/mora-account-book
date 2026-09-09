package com.lilemy.moraaccountbook.model.vo.book;

import com.lilemy.moraaccountbook.model.enums.AccountBookCategoryLevelEnum;
import com.lilemy.moraaccountbook.model.enums.AccountBookCategoryTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 账本分类封装
 *
 * @author lilemy
 * @date 2026-08-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "账本分类封装")
public class AccountBookCategoryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 333088403881076445L;

    /**
     * 账本分类 ID
     */
    @Schema(description = "账本分类 ID")
    private Long id;

    /**
     * 账本 ID
     */
    @Schema(description = "账本 ID")
    private Long bookId;

    /**
     * 父分类 ID，一级分类为 0
     */
    @Schema(description = "父分类 ID，一级分类为 0")
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
    private AccountBookCategoryTypeEnum categoryType;

    /**
     * 分类层级(1一级 2二级)
     */
    @Schema(description = "分类层级")
    private AccountBookCategoryLevelEnum categoryLevel;

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
     * 子分类列表
     */
    @Schema(description = "子分类列表")
    private List<AccountBookCategoryVO> children;
}
