package com.lilemy.moraaccountbook.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.lilemy.moraaccountbook.common.BaseResponse;
import com.lilemy.moraaccountbook.common.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查控制器
 *
 * @author lilemy
 * @date 2026-08-17
 */
@SaIgnore
@RestController
@RequestMapping("/health")
@Tag(name = "HealthController", description = "健康检查接口")
public class HealthController {

    @GetMapping()
    @Operation(summary = "健康检查")
    public BaseResponse<String> healthCheck() {
        return ResultUtils.success("ok");
    }
}
