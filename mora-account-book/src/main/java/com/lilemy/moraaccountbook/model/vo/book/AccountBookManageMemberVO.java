package com.lilemy.moraaccountbook.model.vo.book;

import com.lilemy.moraaccountbook.model.enums.AccountBookMemberRoleEnum;
import com.lilemy.moraaccountbook.model.enums.AccountBookMemberStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账本管理成员封装
 *
 * @author lilemy
 * @date 2026-08-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "账本管理成员封装")
public class AccountBookManageMemberVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8660348299535901086L;

    /**
     * 账本成员 ID
     */
    @Schema(description = "账本成员 ID")
    private Long id;

    /**
     * 账本 ID
     */
    @Schema(description = "账本 ID")
    private Long bookId;

    /**
     * 用户 ID
     */
    @Schema(description = "用户 ID")
    private Long userId;

    /**
     * 用户手机号
     */
    @Schema(description = "用户手机号")
    private String userPhone;

    /**
     * 用户名称
     */
    @Schema(description = "用户名称")
    private String userName;

    /**
     * 成员角色(0所有者 1管理员 2普通成员)
     */
    @Schema(description = "成员角色")
    private AccountBookMemberRoleEnum memberRole;

    /**
     * 是否为用户默认账本
     */
    @Schema(description = "是否为用户默认账本")
    private Boolean isDefault;

    /**
     * 成员状态(0正常 1已移除)
     */
    @Schema(description = "成员状态")
    private AccountBookMemberStatusEnum status;

    /**
     * 加入账本时间
     */
    @Schema(description = "加入账本时间")
    private LocalDateTime joinTime;
}
