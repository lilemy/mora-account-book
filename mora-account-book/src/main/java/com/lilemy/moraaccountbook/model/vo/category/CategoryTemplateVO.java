package com.lilemy.moraaccountbook.model.vo.category;

import com.lilemy.moraaccountbook.model.enums.EnableStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 分类模板封装
 *
 * @author lilemy
 * @date 2026-08-28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类模板封装")
public class CategoryTemplateVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6048351088820132720L;

    /**
     * 分类模板 ID
     */
    @Schema(description = "分类模板 ID")
    private Long id;

    /**
     * 模板编码
     */
    @Schema(description = "模板编码")
    private String templateCode;

    /**
     * 模板名称
     */
    @Schema(description = "模板名称")
    private String templateName;

    /**
     * 是否为默认模板
     */
    @Schema(description = "是否为默认模板")
    private Boolean isDefault;

    /**
     * 模板状态(0启用 1停用)
     */
    @Schema(description = "模板状态")
    private EnableStatusEnum status;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 分类模板项
     */
    @Schema(description = "分类模板项")
    private List<CategoryTemplateItemVO> itemList;
}
