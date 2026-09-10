package com.lilemy.moraaccountbook.controller;

import com.lilemy.moraaccountbook.common.BaseResponse;
import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.common.ResultUtils;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountCreateRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountDeleteRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountQueryRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserAccountUpdateRequest;
import com.lilemy.moraaccountbook.model.vo.user.UserAccountManageVO;
import com.lilemy.moraaccountbook.model.vo.user.UserAccountVO;
import com.lilemy.moraaccountbook.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "创建用户账户")
    @PostMapping("/create")
    public BaseResponse<Long> createUserAccount(@Validated @RequestBody UserAccountCreateRequest request) {
        return ResultUtils.success(userAccountService.createUserAccount(request));
    }

    @Operation(summary = "更新用户账户")
    @PutMapping("/update")
    public BaseResponse<Boolean> updateUserAccount(@Validated @RequestBody UserAccountUpdateRequest request) {
        return ResultUtils.success(userAccountService.updateUserAccount(request));
    }

    @Operation(summary = "删除用户账户")
    @DeleteMapping("/delete")
    public BaseResponse<Boolean> deleteUserAccount(@Validated @RequestBody UserAccountDeleteRequest request) {
        return ResultUtils.success(userAccountService.deleteUserAccount(request));
    }

    @Operation(summary = "获取用户账户")
    @GetMapping("/{id}")
    public BaseResponse<UserAccountVO> getUserAccountVOById(@PathVariable Long id) {
        return ResultUtils.success(userAccountService.getUserAccountVOById(id));
    }

    @Operation(summary = "获取用户账户（仅管理员）")
    @GetMapping("/admin/{id}")
    public BaseResponse<UserAccountManageVO> getUserAccountManageVOById(@PathVariable Long id) {
        return ResultUtils.success(userAccountService.getUserAccountManageVOById(id));
    }

    @Operation(summary = "获取用户账户分页列表")
    @GetMapping("/page")
    public BaseResponse<PageResult<UserAccountVO>> getUserAccountVOPage(UserAccountQueryRequest request, PageQuery pageQuery) {
        return ResultUtils.success(userAccountService.getUserAccountVOPage(request, pageQuery));
    }

    @Operation(summary = "获取用户账户分页列表（仅管理员）")
    @GetMapping("/admin/page")
    public BaseResponse<PageResult<UserAccountManageVO>> getUserAccountManageVOPage(UserAccountQueryRequest request, PageQuery pageQuery) {
        return ResultUtils.success(userAccountService.getUserAccountManageVOPage(request, pageQuery));
    }
}
