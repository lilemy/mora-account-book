package com.lilemy.moraaccountbook.service;

import com.lilemy.moraaccountbook.model.entity.CategoryTemplateItem;
import com.lilemy.moraaccountbook.model.vo.category.CategoryTemplateItemVO;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 分类模板项 服务层。
 *
 * @author lilemy
 * @since 2026-08-28
 */
public interface CategoryTemplateItemService extends IService<CategoryTemplateItem> {


    /**
     * 构建分类模板项树列表
     *
     * @param categoryTemplateItemList 分类模板项列表
     * @return 分类模板项树列表
     */
    List<CategoryTemplateItemVO> buildCategoryTemplateItemTreeList(List<CategoryTemplateItem> categoryTemplateItemList);
}
