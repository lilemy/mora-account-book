package com.lilemy.moraaccountbook.model.dto.category;

import com.lilemy.moraaccountbook.model.enums.EnableStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分类模板管理查询请求
 *
 * @author lilemy
 * @date 2026-08-28
 */
@Data
@Schema(description = "分类模板管理查询请求")
public class CategoryTemplateQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -5349752095440560404L;

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
}
