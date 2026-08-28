package com.lilemy.moraaccountbook.controller;

import com.lilemy.moraaccountbook.service.CategoryTemplateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分类模板 控制层。
 *
 * @author lilemy
 * @since 2026-08-28
 */
@RestController
@RequestMapping("/categoryTemplate")
@Tag(name = "CategoryTemplateController", description = "分类模板接口")
public class CategoryTemplateController {

    @Resource
    private CategoryTemplateService categoryTemplateService;
}
