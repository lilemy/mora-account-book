package com.lilemy.moraaccountbook.model.vo.user;

import com.lilemy.moraaccountbook.model.enums.SysUserGenderEnum;
import com.lilemy.moraaccountbook.model.enums.SysUserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录用户信息
 *
 * @author lilemy
 * @date 2026-08-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录用户信息")
public class SysLoginUserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4213094836691102522L;

    /**
     * id
     */
    @Schema(defaultValue = "id")
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
    @Schema(description = "用户性别")
    private SysUserGenderEnum userGender;

    /**
     * 用户角色(0用户 1管理员)
     */
    @Schema(description = "用户角色")
    private SysUserRoleEnum userRole;

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
