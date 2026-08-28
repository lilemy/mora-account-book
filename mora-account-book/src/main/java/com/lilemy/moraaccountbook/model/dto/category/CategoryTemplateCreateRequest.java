package com.lilemy.moraaccountbook.model.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分类模板创建请求
 *
 * @author lilemy
 * @date 2026-08-28
 */
@Data
@Schema(description = "分类模板创建请求")
public class CategoryTemplateCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 7298074611310324044L;

    /**
     * 模板编码
     */
    @Schema(description = "模板编码")
    @NotBlank(message = "模板编码不能为空")
    private String templateCode;

    /**
     * 模板名称
     */
    @Schema(description = "模板名称")
    @NotBlank(message = "模板名称不能为空")
    private String templateName;

    /**
     * 是否为默认模板
     */
    @Schema(description = "是否为默认模板")
    private Boolean isDefault;
}
