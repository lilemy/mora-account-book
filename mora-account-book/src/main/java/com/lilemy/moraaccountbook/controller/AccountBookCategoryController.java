package com.lilemy.moraaccountbook.controller;

import com.lilemy.moraaccountbook.service.AccountBookCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账本分类 控制层。
 *
 * @author lilemy
 * @since 2026-08-28
 */
@RestController
@RequestMapping("/accountBookCategory")
@Tag(name = "CategoryTemplateController", description = "账本分类接口")
public class AccountBookCategoryController {

    @Resource
    private AccountBookCategoryService accountBookCategoryService;
}
