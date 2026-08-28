package com.lilemy.moraaccountbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.lilemy.moraaccountbook.mapper.CategoryTemplateItemMapper;
import com.lilemy.moraaccountbook.model.entity.CategoryTemplateItem;
import com.lilemy.moraaccountbook.model.enums.AccountBookCategoryLevelEnum;
import com.lilemy.moraaccountbook.model.vo.category.CategoryTemplateItemVO;
import com.lilemy.moraaccountbook.service.CategoryTemplateItemService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * 分类模板项 服务层实现。
 *
 * @author lilemy
 * @since 2026-08-28
 */
@Service
public class CategoryTemplateItemServiceImpl extends ServiceImpl<CategoryTemplateItemMapper, CategoryTemplateItem> implements CategoryTemplateItemService {

    @Override
    public List<CategoryTemplateItemVO> buildCategoryTemplateItemTreeList(List<CategoryTemplateItem> categoryTemplateItemList) {
        if (CollUtil.isEmpty(categoryTemplateItemList)) {
            return CollUtil.newArrayList();
        }
        // 获取第一层分类模版项
        return categoryTemplateItemList.stream().filter(item ->
                        item.getCategoryLevel().equals(AccountBookCategoryLevelEnum.LEVEL_1) && item.getParentId().equals(0L))
                .map(item -> {
                    // 构建分类模版项 VO
                    CategoryTemplateItemVO itemVO = new CategoryTemplateItemVO();
                    BeanUtil.copyProperties(item, itemVO);
                    // 设置子分类模版项
                    List<CategoryTemplateItem> children = categoryTemplateItemList.stream().filter(subItem ->
                            subItem.getParentId().equals(item.getId())).toList();
                    if (CollUtil.isNotEmpty(children)) {
                        List<CategoryTemplateItemVO> childrenVO = BeanUtil.copyToList(children, CategoryTemplateItemVO.class);
                        // 按状态和排序值排序
                        childrenVO.sort(Comparator.comparing(CategoryTemplateItemVO::getStatus)
                                .thenComparing(CategoryTemplateItemVO::getSortOrder));
                        itemVO.setChildren(childrenVO);
                    } else {
                        itemVO.setChildren(CollUtil.newArrayList());
                    }
                    return itemVO;
                })
                .sorted(Comparator.comparing(CategoryTemplateItemVO::getSortOrder))
                .toList();
    }
}
