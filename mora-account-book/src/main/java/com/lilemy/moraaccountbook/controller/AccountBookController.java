package com.lilemy.moraaccountbook.controller;

import com.lilemy.moraaccountbook.service.AccountBookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
