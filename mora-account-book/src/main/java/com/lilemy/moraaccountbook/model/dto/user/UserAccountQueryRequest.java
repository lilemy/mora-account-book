package com.lilemy.moraaccountbook.model.dto.user;

import com.lilemy.moraaccountbook.model.enums.user.UserAccountTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户账户查询请求
 *
 * @author lilemy
 * @date 2026-09-07
 */
@Data
@Schema(description = "用户账户查询请求")
public class UserAccountQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -7973897700745354896L;

    /**
     * 所属用户 ID
     */
    @Schema(description = "所属用户 ID")
    private Long userId;

    /**
     * 账户名称
     */
    @Schema(description = "账户名称")
    private String accountName;

    /**
     * 账户类型：0资金账户，1信用账户，2充值账户，3理财账户，4借贷账户
     */
    @Schema(description = "账户类型")
    private UserAccountTypeEnum accountType;

    /**
     * 是否为用户默认账户
     */
    @Schema(description = "是否为用户默认账户")
    private Boolean isDefault;
}
