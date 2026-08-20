package com.lilemy.moraaccountbook.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author lilemy
 * @date 2026-08-20
 */
@Data
@Schema(description = "用户注册请求")
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 4217845687689895998L;

    /**
     * 用户手机号
     */
    @NotBlank(message = "用户手机号不能为空")
    @Schema(description = "用户手机号")
    private String userPhone;

    /**
     * 用户密码
     */
    @NotBlank(message = "用户密码不能为空")
    @Size(min = 8, message = "用户密码长度不能少于8位")
    @Schema(description = "用户密码")
    private String userPassword;

    /**
     * 用户确认密码
     */
    @NotBlank(message = "用户确认密码不能为空")
    @Schema(description = "用户确认密码")
    private String checkPassword;
}
