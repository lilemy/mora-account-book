package com.lilemy.moraaccountbook.model.vo.user;

import com.lilemy.moraaccountbook.model.enums.user.UserGenderEnum;
import com.lilemy.moraaccountbook.model.enums.user.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户管理封装
 *
 * @author lilemy
 * @date 2026-08-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户管理封装")
public class UserManageVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6718303982414858849L;

    /**
     * id
     */
    @Schema(description = "id")
    private Long id;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String userName;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像")
    private String userAvatar;

    /**
     * 用户简介
     */
    @Schema(description = "用户简介")
    private String userProfile;

    /**
     * 用户手机号
     */
    @Schema(description = "用户手机号")
    private String userPhone;

    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱")
    private String userEmail;

    /**
     * 用户性别(0未填写 1男 2女)
     */
    @Schema(description = "用户性别(0未填写 1男 2女)")
    private UserGenderEnum userGender;

    /**
     * 用户角色(0用户 1管理员)
     */
    @Schema(description = "用户角色(0用户 1管理员)")
    private UserRoleEnum userRole;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 编辑时间
     */
    @Schema(description = "编辑时间")
    private LocalDateTime editTime;

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
}
