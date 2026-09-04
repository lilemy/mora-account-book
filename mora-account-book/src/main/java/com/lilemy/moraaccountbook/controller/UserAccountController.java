package com.lilemy.moraaccountbook.controller;

import com.lilemy.moraaccountbook.service.UserAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户账户 控制层。
 *
 * @author lilemy
 * @since 2026-09-04
 */
@RestController
@RequestMapping("/userAccount")
@Tag(name = "UserAccountController", description = "用户账户接口")
public class UserAccountController {

    @Resource
    private UserAccountService userAccountService;
}
