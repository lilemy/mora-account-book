package com.lilemy.moraaccountbook.common;

import com.lilemy.moraaccountbook.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用响应类
 *
 * @author lilemy
 * @date 2026-08-20
 */
@Data
@Schema(description = "通用响应类")
public class BaseResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 2575188626020212712L;

    /**
     * 状态码
     */
    @Schema(description = "状态码")
    private int code;

    /**
     * 数据
     */
    @Schema(description = "数据")
    private T data;

    /**
     * 提示信息
     */
    @Schema(description = "提示信息")
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}

