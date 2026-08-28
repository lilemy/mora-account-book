package com.lilemy.moraaccountbook.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.lilemy.moraaccountbook.model.entity.CategoryTemplate;
import com.lilemy.moraaccountbook.mapper.CategoryTemplateMapper;
import com.lilemy.moraaccountbook.service.CategoryTemplateService;
import org.springframework.stereotype.Service;

/**
 * 分类模板 服务层实现。
 *
 * @author lilemy
 * @since 2026-08-28
 */
@Service
public class CategoryTemplateServiceImpl extends ServiceImpl<CategoryTemplateMapper, CategoryTemplate>  implements CategoryTemplateService{

}
