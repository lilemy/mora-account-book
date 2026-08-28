package com.lilemy.moraaccountbook.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.lilemy.moraaccountbook.model.entity.CategoryTemplateItem;
import com.lilemy.moraaccountbook.mapper.CategoryTemplateItemMapper;
import com.lilemy.moraaccountbook.service.CategoryTemplateItemService;
import org.springframework.stereotype.Service;

/**
 * 分类模板项 服务层实现。
 *
 * @author lilemy
 * @since 2026-08-28
 */
@Service
public class CategoryTemplateItemServiceImpl extends ServiceImpl<CategoryTemplateItemMapper, CategoryTemplateItem>  implements CategoryTemplateItemService{

}
