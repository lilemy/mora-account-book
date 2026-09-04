package com.lilemy.moraaccountbook.model.dto.user;

import com.lilemy.moraaccountbook.model.enums.UserGenderEnum;
import com.lilemy.moraaccountbook.model.enums.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户更新请求
 *
 * @author lilemy
 * @date 2026-08-21
 */
@Data
@Schema(description = "用户更新请求")
public class UserUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -6843838759215708973L;

    /**
     * id
     */
    @Schema(description = "id")
    @NotNull(message = "id不能为空")
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
}
