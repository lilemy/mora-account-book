package com.lilemy.moraaccountbook.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户账户删除请求
 *
 * @author lilemy
 * @date 2026-09-04
 */
@Data
@Schema(description = "用户账户删除请求")
public class UserAccountDeleteRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 6144527210022113839L;

    /**
     * 账户 ID
     */
    @Schema(description = "账户 ID")
    @NotNull(message = "账户 ID 不能为空")
    private Long id;

    /**
     * 是否保留账单
     */
    @Schema(description = "是否保留账单")
    @NotNull(message = "请选择是否保留账单")
    private Boolean isKeepBill;
}
