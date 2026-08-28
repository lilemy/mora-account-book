package com.lilemy.moraaccountbook.service;

import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.model.dto.category.*;
import com.lilemy.moraaccountbook.model.entity.CategoryTemplate;
import com.lilemy.moraaccountbook.model.vo.category.CategoryTemplateVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;

/**
 * 分类模板 服务层。
 *
 * @author lilemy
 * @since 2026-08-28
 */
public interface CategoryTemplateService extends IService<CategoryTemplate> {

    /**
     * 创建分类模板
     *
     * @param request 创建分类模板请求
     * @return 是否创建成功
     */
    Boolean createCategoryTemplate(CategoryTemplateCreateRequest request);

    /**
     * 更新分类模板
     *
     * @param request 更新分类模板请求
     * @return 是否更新成功
     */
    Boolean updateCategoryTemplate(CategoryTemplateUpdateRequest request);

    /**
     * 删除分类模板
     *
     * @param id 分类模板 ID
     * @return 是否删除成功
     */
    Boolean deleteCategoryTemplate(Long id);

    /**
     * 修改分类模板状态
     *
     * @param id 分类模板 ID
     * @return 是否修改成功
     */
    Boolean changeCategoryTemplateStatus(Long id);

    /**
     * 创建分类模板项
     *
     * @param request 创建分类模板项请求
     * @return 是否创建成功
     */
    Boolean createCategoryTemplateItem(CategoryTemplateItemCreateRequest request);

    /**
     * 更新分类模板项
     *
     * @param request 更新分类模板项请求
     * @return 是否更新成功
     */
    Boolean updateCategoryTemplateItem(CategoryTemplateItemUpdateRequest request);

    /**
     * 删除分类模板项
     *
     * @param id 分类模板项 ID
     * @return 是否删除成功
     */
    Boolean deleteCategoryTemplateItem(Long id);

    /**
     * 修改分类模板项状态
     *
     * @param id 分类模板项 ID
     * @return 是否修改成功
     */
    Boolean changeCategoryTemplateItemStatus(Long id);

    /**
     * 获取分类模板详情
     *
     * @param id 分类模板 ID
     * @return 分类模板详情
     */
    CategoryTemplateVO getCategoryTemplateVO(Long id);

    /**
     * 获取分类模板分页列表
     *
     * @param request   查询请求
     * @param pageQuery 分页查询
     * @return 分类模板分页列表
     */
    PageResult<CategoryTemplateVO> getCategoryTemplateVOPage(CategoryTemplateQueryRequest request, PageQuery pageQuery);

    /**
     * 获取查询条件
     *
     * @param request   查询请求
     * @param pageQuery 分页查询
     * @return 查询条件
     */
    QueryWrapper getQueryWrapper(CategoryTemplateQueryRequest request, PageQuery pageQuery);
}
