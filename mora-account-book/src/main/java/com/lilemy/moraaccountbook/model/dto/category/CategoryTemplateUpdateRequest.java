package com.lilemy.moraaccountbook.model.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新分类模板请求
 *
 * @author lilemy
 * @date 2026-08-28
 */
@Data
@Schema(description = "更新分类模板请求")
public class CategoryTemplateUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 7704381042704206379L;

    /**
     * 分类模板 ID
     */
    @Schema(description = "分类模板 ID")
    @NotNull(message = "分类模板 ID 不能为空")
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
}
