package com.lilemy.moraaccountbook.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.lilemy.moraaccountbook.common.BaseResponse;
import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.common.ResultUtils;
import com.lilemy.moraaccountbook.model.dto.user.UserLoginRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserQueryRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserRegisterRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserUpdateRequest;
import com.lilemy.moraaccountbook.model.entity.SysUser;
import com.lilemy.moraaccountbook.model.vo.user.SysLoginUserVO;
import com.lilemy.moraaccountbook.model.vo.user.SysUserManageVO;
import com.lilemy.moraaccountbook.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户 控制层。
 *
 * @author lilemy
 * @since 2026-08-20
 */
@RestController
@Tag(name = "用户接口")
@RequestMapping("/sysUser")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @SaIgnore
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@Validated @RequestBody UserRegisterRequest request) {
        return ResultUtils.success(sysUserService.userRegister(request));
    }

    @SaIgnore
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public BaseResponse<SysLoginUserVO> userLogin(@Validated @RequestBody UserLoginRequest request) {
        return ResultUtils.success(sysUserService.userLogin(request));
    }

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout() {
        return ResultUtils.success(sysUserService.userLogout());
    }

    @Operation(summary = "获取当前登录用户")
    @GetMapping("/getLoginUser")
    public BaseResponse<SysLoginUserVO> getLoginUser() {
        SysUser loginUser = sysUserService.getLoginUser();
        return ResultUtils.success(sysUserService.getLoginUserVO(loginUser));
    }

    @Operation(summary = "获取用户信息（管理员）")
    @GetMapping("/admin/{id}")
    public BaseResponse<SysUserManageVO> getSysUserById(@PathVariable Long id) {
        SysUser sysUser = sysUserService.getById(id);
        return ResultUtils.success(sysUserService.getSysUserManageVO(sysUser));
    }

    @Operation(summary = "分页获取用户信息（管理员）")
    @GetMapping("/admin/page")
    public BaseResponse<PageResult<SysUserManageVO>> getSysUserManageVOPage(UserQueryRequest request, PageQuery pageQuery) {
        return ResultUtils.success(sysUserService.getSysUserManageVOPage(request, pageQuery));
    }

    @Operation(summary = "更新用户信息（管理员）")
    @PutMapping("/admin")
    public BaseResponse<Boolean> updateSysUser(@Validated @RequestBody UserUpdateRequest request) {
        return ResultUtils.success(sysUserService.updateSysUser(request));
    }

    @Operation(summary = "删除用户信息（管理员）")
    @DeleteMapping("/admin/{id}")
    public BaseResponse<Boolean> deleteSysUser(@PathVariable Long id) {
        return ResultUtils.success(sysUserService.deleteSysUser(id));
    }
}
