package com.lilemy.moraaccountbook.controller;

import com.lilemy.moraaccountbook.common.BaseResponse;
import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.common.ResultUtils;
import com.lilemy.moraaccountbook.model.dto.bill.AccountBookBillCalendarQueryRequest;
import com.lilemy.moraaccountbook.model.dto.bill.AccountBookBillCreateRequest;
import com.lilemy.moraaccountbook.model.dto.bill.AccountBookBillQueryRequest;
import com.lilemy.moraaccountbook.model.dto.bill.AccountBookBillUpdateRequest;
import com.lilemy.moraaccountbook.model.vo.bill.AccountBookBillManageVO;
import com.lilemy.moraaccountbook.model.vo.bill.AccountBookBillMonthListVO;
import com.lilemy.moraaccountbook.model.vo.bill.AccountBookBillMonthTotalVO;
import com.lilemy.moraaccountbook.model.vo.bill.AccountBookBillVO;
import com.lilemy.moraaccountbook.service.AccountBookBillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 账本收入/支出流水 控制层。
 *
 * @author lilemy
 * @since 2026-09-09
 */
@RestController
@RequestMapping("/accountBookBill")
@Tag(name = "AccountBookBillController", description = "账本收入/支出流水接口")
public class AccountBookBillController {

    @Resource
    private AccountBookBillService accountBookBillService;

    @Operation(summary = "创建账本流水")
    @PostMapping("/create")
    public BaseResponse<Long> createAccountBookBill(@Validated @RequestBody AccountBookBillCreateRequest request) {
        return ResultUtils.success(accountBookBillService.createAccountBookBill(request));
    }

    @Operation(summary = "更新账本流水")
    @PutMapping("/update")
    public BaseResponse<Boolean> updateAccountBookBill(@Validated @RequestBody AccountBookBillUpdateRequest request) {
        return ResultUtils.success(accountBookBillService.updateAccountBookBill(request));
    }

    @Operation(summary = "删除账本流水")
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteAccountBookBill(@PathVariable Long id) {
        return ResultUtils.success(accountBookBillService.deleteAccountBookBillById(id));
    }

    @Operation(summary = "获取账本流水")
    @GetMapping("/{id}")
    public BaseResponse<AccountBookBillVO> getUserAccountBookBillVOById(@PathVariable Long id) {
        return ResultUtils.success(accountBookBillService.getAccountBookBillVOById(id));
    }

    @Operation(summary = "获取账本流水分页列表")
    @GetMapping("/page")
    public BaseResponse<PageResult<AccountBookBillVO>> getUserAccountBookBillVOPage(AccountBookBillQueryRequest request,
                                                                                    PageQuery pageQuery) {
        return ResultUtils.success(accountBookBillService.getAccountBookBillVOPage(request, pageQuery));
    }

    @Operation(summary = "根据月份获取账本流水列表")
    @GetMapping("/month/list")
    public BaseResponse<List<AccountBookBillMonthListVO>> getUserAccountBookBillVOListByMonth(@Validated AccountBookBillCalendarQueryRequest
                                                                                                      request) {
        return ResultUtils.success(accountBookBillService.getAccountBookBillVOListByMonth(request));
    }

    @Operation(summary = "根据年份获取账本流水汇总")
    @GetMapping("/month/total")
    public BaseResponse<List<AccountBookBillMonthTotalVO>> getUserAccountBookBillTotalByMonth(@Validated AccountBookBillCalendarQueryRequest
                                                                                                      request) {
        return ResultUtils.success(accountBookBillService.getAccountBookBillTotalByMonth(request));
    }

    @Operation(summary = "获取账本流水（仅管理员）")
    @GetMapping("/admin/{id}")
    public BaseResponse<AccountBookBillManageVO> getUserAccountBookBillManageVOById(@PathVariable Long id) {
        return ResultUtils.success(accountBookBillService.getAccountBookBillManageVOById(id));
    }

    @Operation(summary = "获取账本流水分页列表（仅管理员）")
    @GetMapping("/admin/page")
    public BaseResponse<PageResult<AccountBookBillManageVO>> getUserAccountBookBillManageVOPage(AccountBookBillQueryRequest request,
                                                                                                PageQuery pageQuery) {
        return ResultUtils.success(accountBookBillService.getAccountBookBillManageVOPage(request, pageQuery));
    }
}
