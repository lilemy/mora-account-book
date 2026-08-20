package com.lilemy.moraaccountbook.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.lilemy.moraaccountbook.common.BaseResponse;
import com.lilemy.moraaccountbook.common.ResultUtils;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author lilemy
 * @date 2026-08-20
 */
@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e, HttpServletRequest request) {
        // 获取请求地址
        String path = request.getRequestURI();
        String method = request.getMethod();
        String url = method + " " + path;
        String remoteHost = request.getRemoteHost();
        // 生成请求唯一 id
        String requestId = IdUtil.fastSimpleUUID();
        log.warn("业务异常 ID:[{}], URL[{}], IP:[{}]: {} {}", requestId, url, remoteHost, e.getCode(), e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e,
                                                                  HttpServletRequest request) {
        // 获取 BindingResult
        BindingResult bindingResult = e.getBindingResult();
        // 获取请求参数
        Object targetObject = bindingResult.getTarget();
        String reqParam = JSONUtil.toJsonStr(targetObject);
        // 获取错误信息
        String msg = bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("，"));
        // 生成请求唯一 id
        String requestId = IdUtil.fastSimpleUUID();
        String path = request.getRequestURI();
        String method = request.getMethod();
        String url = method + " " + path;
        String remoteHost = request.getRemoteHost();
        log.warn("请求参数异常 ID:[{}], URL[{}], IP:[{}], 参数:[{}], 异常信息:[{} {}]",
                requestId, url, remoteHost, reqParam, ErrorCode.PARAMS_ERROR.getCode(), msg);
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, msg);
    }

    @ExceptionHandler(NotRoleException.class)
    public BaseResponse<?> notRoleExceptionHandler(RuntimeException e, HttpServletRequest request) {
        // 获取请求地址
        String path = request.getRequestURI();
        String method = request.getMethod();
        String url = method + " " + path;
        String remoteHost = request.getRemoteHost();
        // 生成请求唯一 id
        String requestId = IdUtil.fastSimpleUUID();
        log.warn("无权限异常 ID:[{}], URL[{}], IP:[{}], 错误信息:[{}]",
                requestId, url, remoteHost, e.getMessage(), e);
        return ResultUtils.error(ErrorCode.NO_AUTH_ERROR, "无权限");
    }

    @ExceptionHandler(NotLoginException.class)
    public BaseResponse<?> notLoginExceptionHandler(RuntimeException e, HttpServletRequest request) {
        // 获取请求地址
        String path = request.getRequestURI();
        String method = request.getMethod();
        String url = method + " " + path;
        String remoteHost = request.getRemoteHost();
        // 生成请求唯一 id
        String requestId = IdUtil.fastSimpleUUID();
        log.warn("未登录异常 ID:[{}], URL[{}], IP:[{}], 错误信息:[{}]",
                requestId, url, remoteHost, e.getMessage());
        return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("运行时异常", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<?> exceptionHandler(Exception e) {
        log.error("系统异常", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}

