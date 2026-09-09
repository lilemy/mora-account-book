package com.lilemy.moraaccountbook.controller;

import com.lilemy.moraaccountbook.service.AccountBookBillService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
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
}
