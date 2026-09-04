package com.lilemy.moraaccountbook.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.lilemy.moraaccountbook.common.BaseResponse;
import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.common.ResultUtils;
import com.lilemy.moraaccountbook.exception.ErrorCode;
import com.lilemy.moraaccountbook.exception.ThrowUtils;
import com.lilemy.moraaccountbook.model.dto.user.UserLoginRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserQueryRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserRegisterRequest;
import com.lilemy.moraaccountbook.model.dto.user.UserUpdateRequest;
import com.lilemy.moraaccountbook.model.entity.User;
import com.lilemy.moraaccountbook.model.vo.user.LoginUserVO;
import com.lilemy.moraaccountbook.model.vo.user.UserManageVO;
import com.lilemy.moraaccountbook.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户 控制层。
 *
 * @author lilemy
 * @since 2026-08-20
 */
@RestController
@RequestMapping("/user")
@Tag(name = "UserController", description = "用户接口")
public class UserController {

    @Resource
    private UserService userService;

    @SaIgnore
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@Validated @RequestBody UserRegisterRequest request) {
        return ResultUtils.success(userService.userRegister(request));
    }

    @SaIgnore
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@Validated @RequestBody UserLoginRequest request) {
        return ResultUtils.success(userService.userLogin(request));
    }

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout() {
        return ResultUtils.success(userService.userLogout());
    }

    @Operation(summary = "获取当前登录用户")
    @GetMapping("/getLoginUser")
    public BaseResponse<LoginUserVO> getLoginUser() {
        User loginUser = userService.getLoginUserCache();
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    @Operation(summary = "上传并修改用户头像")
    @PostMapping(value = "/uploadAvatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<Boolean> uploadAvatar(@RequestParam("file") MultipartFile file) {
        // 1.文件校验
        ThrowUtils.throwIf(file.isEmpty(), ErrorCode.PARAMS_ERROR, "上传的文件为空");
        // 2.获取登录用户
        User loginUser = userService.getLoginUser();
        return ResultUtils.success(userService.uploadAvatar(loginUser, file));
    }

    @Operation(summary = "上传并修改用户头像（管理员）")
    @PostMapping(value = "/admin/uploadAvatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<Boolean> uploadAvatarByAdmin(Long id, @RequestParam("file") MultipartFile file) {
        // 1.参数校验
        ThrowUtils.throwIf(id == null || file.isEmpty(), ErrorCode.PARAMS_ERROR);
        // 2.查询用户
        User user = userService.getById(id);
        return ResultUtils.success(userService.uploadAvatar(user, file));
    }

    @Operation(summary = "获取用户信息（管理员）")
    @GetMapping("/admin/{id}")
    public BaseResponse<UserManageVO> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResultUtils.success(userService.getUserManageVO(user));
    }

    @Operation(summary = "分页获取用户信息（管理员）")
    @GetMapping("/admin/page")
    public BaseResponse<PageResult<UserManageVO>> getUserManageVOPage(UserQueryRequest request, PageQuery pageQuery) {
        return ResultUtils.success(userService.getUserManageVOPage(request, pageQuery));
    }

    @Operation(summary = "更新用户信息（管理员）")
    @PutMapping("/admin")
    public BaseResponse<Boolean> updateUser(@Validated @RequestBody UserUpdateRequest request) {
        return ResultUtils.success(userService.updateUser(request));
    }

    @Operation(summary = "删除用户信息（管理员）")
    @DeleteMapping("/admin/{id}")
    public BaseResponse<Boolean> deleteUser(@PathVariable Long id) {
        return ResultUtils.success(userService.deleteUser(id));
    }
}
