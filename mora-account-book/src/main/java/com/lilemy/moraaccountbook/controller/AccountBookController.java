package com.lilemy.moraaccountbook.controller;

import com.lilemy.moraaccountbook.common.BaseResponse;
import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.common.ResultUtils;
import com.lilemy.moraaccountbook.model.dto.book.AccountBookManageQueryRequest;
import com.lilemy.moraaccountbook.model.dto.book.AccountBookUpdateRequest;
import com.lilemy.moraaccountbook.model.dto.book.AccountBookUserQueryRequest;
import com.lilemy.moraaccountbook.model.entity.AccountBook;
import com.lilemy.moraaccountbook.model.vo.book.AccountBookManageVO;
import com.lilemy.moraaccountbook.model.vo.book.AccountBookUserVO;
import com.lilemy.moraaccountbook.service.AccountBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 账本 控制层。
 *
 * @author lilemy
 * @since 2026-08-26
 */
@RestController
@RequestMapping("/accountBook")
@Tag(name = "AccountBookController", description = "账本接口")
public class AccountBookController {

    @Resource
    private AccountBookService accountBookService;

    @Operation(summary = "分页获取用户账本信息")
    @GetMapping("/page")
    public BaseResponse<PageResult<AccountBookUserVO>> getAccountBookPage(AccountBookUserQueryRequest request,
                                                                          PageQuery pageQuery) {
        return ResultUtils.success(accountBookService.getUserAccountBookPage(request, pageQuery));
    }

    @Operation(summary = "获取账本信息（管理员）")
    @GetMapping("/admin/{id}")
    public BaseResponse<AccountBookManageVO> getAccountBookById(@PathVariable Long id) {
        AccountBook accountBook = accountBookService.getById(id);
        return ResultUtils.success(accountBookService.getAccountBookManageVO(accountBook));
    }

    @Operation(summary = "分页获取账本信息（管理员）")
    @GetMapping("/admin/page")
    public BaseResponse<PageResult<AccountBookManageVO>> getAccountBookManageVOPage(AccountBookManageQueryRequest request,
                                                                                    PageQuery pageQuery) {
        return ResultUtils.success(accountBookService.getAccountBookManageVOPage(request, pageQuery));
    }

    @Operation(summary = "更新账本信息（管理员）")
    @PutMapping("/admin")
    public BaseResponse<Boolean> updateAccountBook(@Validated @RequestBody AccountBookUpdateRequest request) {
        return ResultUtils.success(accountBookService.updateAccountBook(request));
    }

    @Operation(summary = "删除账本信息（管理员）")
    @DeleteMapping("/admin/{id}")
    public BaseResponse<Boolean> deleteAccountBook(@PathVariable Long id) {
        return ResultUtils.success(accountBookService.deleteAccountBook(id));
    }
}
