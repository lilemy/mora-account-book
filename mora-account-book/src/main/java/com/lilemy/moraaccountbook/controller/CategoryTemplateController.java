package com.lilemy.moraaccountbook.controller;

import com.lilemy.moraaccountbook.common.BaseResponse;
import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.common.ResultUtils;
import com.lilemy.moraaccountbook.model.dto.category.*;
import com.lilemy.moraaccountbook.model.vo.category.CategoryTemplateVO;
import com.lilemy.moraaccountbook.service.CategoryTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "创建分类模板")
    @PostMapping("/admin")
    public BaseResponse<Boolean> createCategoryTemplate(@Validated CategoryTemplateCreateRequest request) {
        return ResultUtils.success(categoryTemplateService.createCategoryTemplate(request));
    }

    @Operation(summary = "更新分类模板")
    @PutMapping("/admin")
    public BaseResponse<Boolean> updateCategoryTemplate(@Validated CategoryTemplateUpdateRequest request) {
        return ResultUtils.success(categoryTemplateService.updateCategoryTemplate(request));
    }

    @Operation(summary = "删除分类模板")
    @DeleteMapping("/admin/{id}")
    public BaseResponse<Boolean> deleteCategoryTemplate(@PathVariable Long id) {
        return ResultUtils.success(categoryTemplateService.deleteCategoryTemplate(id));
    }

    @Operation(summary = "切换分类模板状态")
    @PutMapping("/admin/status/{id}")
    public BaseResponse<Boolean> changeCategoryTemplateStatus(@PathVariable Long id) {
        return ResultUtils.success(categoryTemplateService.changeCategoryTemplateStatus(id));
    }

    @Operation(summary = "创建分类模板项")
    @PostMapping("/admin/item")
    public BaseResponse<Boolean> createCategoryTemplateItem(@Validated CategoryTemplateItemCreateRequest request) {
        return ResultUtils.success(categoryTemplateService.createCategoryTemplateItem(request));
    }

    @Operation(summary = "更新分类模板项")
    @PutMapping("/admin/item")
    public BaseResponse<Boolean> updateCategoryTemplateItem(@Validated CategoryTemplateItemUpdateRequest request) {
        return ResultUtils.success(categoryTemplateService.updateCategoryTemplateItem(request));
    }

    @Operation(summary = "删除分类模板项")
    @DeleteMapping("/admin/item/{id}")
    public BaseResponse<Boolean> deleteCategoryTemplateItem(@PathVariable Long id) {
        return ResultUtils.success(categoryTemplateService.deleteCategoryTemplateItem(id));
    }

    @Operation(summary = "切换分类模板项状态")
    @PutMapping("/admin/item/status/{id}")
    public BaseResponse<Boolean> changeCategoryTemplateItemStatus(@PathVariable Long id) {
        return ResultUtils.success(categoryTemplateService.changeCategoryTemplateItemStatus(id));
    }

    @Operation(summary = "获取分类模板详情")
    @GetMapping("/admin/{id}")
    public BaseResponse<CategoryTemplateVO> getCategoryTemplateVO(@PathVariable Long id) {
        return ResultUtils.success(categoryTemplateService.getCategoryTemplateVO(id));
    }

    @Operation(summary = "获取分类模板分页列表")
    @GetMapping("/admin/page")
    public BaseResponse<PageResult<CategoryTemplateVO>> getCategoryTemplateVOPage(CategoryTemplateQueryRequest request,
                                                                                  PageQuery pageQuery) {
        return ResultUtils.success(categoryTemplateService.getCategoryTemplateVOPage(request, pageQuery));
    }
}
