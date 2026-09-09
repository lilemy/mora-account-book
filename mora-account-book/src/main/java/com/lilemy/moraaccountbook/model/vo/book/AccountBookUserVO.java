package com.lilemy.moraaccountbook.model.vo.book;

import com.lilemy.moraaccountbook.model.enums.book.AccountBookMemberRoleEnum;
import com.lilemy.moraaccountbook.model.enums.book.AccountBookStatusEnum;
import com.lilemy.moraaccountbook.model.enums.book.AccountBookTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账本封装
 *
 * @author lilemy
 * @date 2026-08-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "账本封装")
public class AccountBookUserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5878977227082967201L;

    /**
     * 账本 ID
     */
    @Schema(description = "账本 ID")
    private Long id;

    /**
     * 账本所有者用户 ID
     */
    @Schema(description = "账本所有者用户 ID")
    private Long ownerId;

    /**
     * 账本所有者用户手机号
     */
    @Schema(description = "账本所有者用户手机号")
    private String ownerPhone;

    /**
     * 账本所有者用户昵称
     */
    @Schema(description = "账本所有者用户昵称")
    private String ownerName;

    /**
     * 账本名称
     */
    @Schema(description = "账本名称")
    private String bookName;

    /**
     * 账本类型(0个人账本 1共享账本)
     */
    @Schema(description = "账本类型")
    private AccountBookTypeEnum bookType;

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

    /**
     * 账本状态(0正常 1已归档)
     */
    @Schema(description = "账本状态")
    private AccountBookStatusEnum status;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

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
     * 加入账本时间
     */
    @Schema(description = "加入账本时间")
    private LocalDateTime joinTime;
}
