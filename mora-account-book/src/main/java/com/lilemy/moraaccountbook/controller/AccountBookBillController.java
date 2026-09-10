package com.lilemy.moraaccountbook.controller;

import com.lilemy.moraaccountbook.common.BaseResponse;
import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.common.ResultUtils;
import com.lilemy.moraaccountbook.model.dto.bill.AccountBookBillQueryRequest;
import com.lilemy.moraaccountbook.model.vo.bill.AccountBookBillManageVO;
import com.lilemy.moraaccountbook.service.AccountBookBillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "获取账本流水（仅管理员）")
    @GetMapping("/admin/{id}")
    public BaseResponse<AccountBookBillManageVO> getUserAccountManageVOById(@PathVariable Long id) {
        return ResultUtils.success(accountBookBillService.getAccountBookBillManageVOById(id));
    }

    @Operation(summary = "获取账本流水分页列表（仅管理员）")
    @GetMapping("/admin/page")
    public BaseResponse<PageResult<AccountBookBillManageVO>> getUserAccountManageVOPage(AccountBookBillQueryRequest request,
                                                                                        PageQuery pageQuery) {
        return ResultUtils.success(accountBookBillService.getAccountBookBillManageVOPage(request, pageQuery));
    }
}
