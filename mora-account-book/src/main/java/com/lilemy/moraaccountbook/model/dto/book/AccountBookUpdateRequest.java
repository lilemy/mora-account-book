package com.lilemy.moraaccountbook.model.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 账本更新请求
 *
 * @author lilemy
 * @date 2026-08-26
 */
@Data
@Schema(description = "账本更新请求")
public class AccountBookUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -8324146785217384515L;

    /**
     * 账本 ID
     */
    @NotNull(message = "账本 ID 不能为空")
    @Schema(description = "账本 ID")
    private Long id;

    /**
     * 账本名称
     */
    @Schema(description = "账本名称")
    private String bookName;

    /**
     * 账本图标标识
     */
    @Schema(description = "账本图标标识")
    private String icon;

    /**
     * 账本主题色
     */
    @Schema(description = "账本主题色")
    private String color;

    /**
     * 账本说明
     */
    @Schema(description = "账本说明")
    private String description;
}
