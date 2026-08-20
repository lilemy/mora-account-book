package com.lilemy.moraaccountbook.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录请求
 *
 * @author lilemy
 * @date 2026-08-20
 */
@Data
@Schema(description = "用户登录请求")
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -2505996921194556381L;

    /**
     * 用户手机号
     */
    @Schema(description = "用户手机号")
    @NotBlank(message = "用户手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号码错误")
    private String userPhone;

    /**
     * 用户密码
     */
    @Schema(description = "用户密码")
    @NotBlank(message = "用户密码不能为空")
    @Size(min = 8, max = 20, message = "密码错误")
    private String userPassword;
}
