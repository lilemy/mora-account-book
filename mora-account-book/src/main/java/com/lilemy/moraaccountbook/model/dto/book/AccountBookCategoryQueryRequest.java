package com.lilemy.moraaccountbook.model.dto.book;

import com.lilemy.moraaccountbook.model.enums.TransactionTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 账本分类查询请求
 *
 * @author lilemy
 * @date 2026-09-14
 */
@Data
@Schema(description = "账本分类查询请求")
public class AccountBookCategoryQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 178090778164349734L;

    /**
     * 账本ID
     */
    @NotNull(message = "账本ID不能为空")
    @Schema(description = "账本ID")
    private Long bookId;

    /**
     * 分类类型
     */
    @NotNull(message = "分类类型不能为空")
    @Schema(description = "分类类型")
    private TransactionTypeEnum categoryType;
}
