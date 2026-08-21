package com.lilemy.moraaccountbook.model.dto.user;

import com.lilemy.moraaccountbook.model.enums.SysUserGenderEnum;
import com.lilemy.moraaccountbook.model.enums.SysUserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户查询请求
 *
 * @author lilemy
 * @date 2026-08-21
 */
@Data
@Schema(description = "用户查询请求")
public class UserQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 839203578054059858L;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String userName;

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
}
