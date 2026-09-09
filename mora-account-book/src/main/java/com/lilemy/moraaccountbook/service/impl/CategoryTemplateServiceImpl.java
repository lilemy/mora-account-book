package com.lilemy.moraaccountbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.exception.ErrorCode;
import com.lilemy.moraaccountbook.exception.ThrowUtils;
import com.lilemy.moraaccountbook.mapper.CategoryTemplateMapper;
import com.lilemy.moraaccountbook.model.dto.category.*;
import com.lilemy.moraaccountbook.model.entity.CategoryTemplate;
import com.lilemy.moraaccountbook.model.entity.CategoryTemplateItem;
import com.lilemy.moraaccountbook.model.enums.CategoryLevelEnum;
import com.lilemy.moraaccountbook.model.enums.TransactionTypeEnum;
import com.lilemy.moraaccountbook.model.enums.EnableStatusEnum;
import com.lilemy.moraaccountbook.model.vo.category.CategoryTemplateVO;
import com.lilemy.moraaccountbook.service.CategoryTemplateItemService;
import com.lilemy.moraaccountbook.service.CategoryTemplateService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 分类模板 服务层实现。
 *
 * @author lilemy
 * @since 2026-08-28
 */
@Service
public class CategoryTemplateServiceImpl extends ServiceImpl<CategoryTemplateMapper, CategoryTemplate> implements CategoryTemplateService {

    @Resource
    private CategoryTemplateItemService categoryTemplateItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createCategoryTemplate(CategoryTemplateCreateRequest request) {
        String templateCode = request.getTemplateCode();
        String templateName = request.getTemplateName();
        Boolean isDefault = request.getIsDefault();
        // 1.参数校验
        validCategoryTemplate(null, templateCode, templateName, isDefault);
        // 2.创建分类模板
        CategoryTemplate categoryTemplate = CategoryTemplate.builder()
                .templateCode(templateCode)
                .templateName(templateName)
                .isDefault(isDefault)
                .build();
        boolean save = this.save(categoryTemplate);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "分类模板创建失败");
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCategoryTemplate(CategoryTemplateUpdateRequest request) {
        Long id = request.getId();
        String templateCode = request.getTemplateCode();
        String templateName = request.getTemplateName();
        Boolean isDefault = request.getIsDefault();
        // 1.校验分类模板是否存在
        CategoryTemplate oldCategoryTemplate = this.getById(id);
        ThrowUtils.throwIf(oldCategoryTemplate == null, ErrorCode.PARAMS_ERROR, "分类模板不存在");
        // 2.参数校验
        validCategoryTemplate(id, templateCode, templateName, isDefault);
        // 3.更新分类模板
        CategoryTemplate categoryTemplate = CategoryTemplate.builder()
                .id(id)
                .templateCode(templateCode)
                .templateName(templateName)
                .isDefault(isDefault)
                .build();
        boolean update = this.updateById(categoryTemplate);
        ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR, "分类模板更新失败");
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteCategoryTemplate(Long id) {
        // 1.校验分类模板是否存在
        CategoryTemplate oldCategoryTemplate = this.getById(id);
        ThrowUtils.throwIf(oldCategoryTemplate == null, ErrorCode.PARAMS_ERROR, "分类模板不存在");
        // 2.获取分类模板对应分类项
        List<CategoryTemplateItem> itemList = categoryTemplateItemService.queryChain()
                .eq(CategoryTemplateItem::getTemplateId, id)
                .list();
        // 3.如果存在分类项，则一并删除
        if (CollUtil.isNotEmpty(itemList)) {
            List<Long> itemIds = itemList.stream().map(CategoryTemplateItem::getId).toList();
            boolean removed = categoryTemplateItemService.removeByIds(itemIds);
            ThrowUtils.throwIf(!removed, ErrorCode.OPERATION_ERROR, "分类模板删除失败");
        }
        // 4.删除分类模板
        boolean delete = this.removeById(id);
        ThrowUtils.throwIf(!delete, ErrorCode.OPERATION_ERROR, "分类模板删除失败");
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeCategoryTemplateStatus(Long id) {
        // 1.校验分类模板是否存在
        CategoryTemplate oldCategoryTemplate = this.getById(id);
        ThrowUtils.throwIf(oldCategoryTemplate == null, ErrorCode.PARAMS_ERROR, "分类模板不存在");
        EnableStatusEnum oldStatus = oldCategoryTemplate.getStatus();
        EnableStatusEnum newStatus = EnableStatusEnum.changeStatus(oldStatus);
        // 2.获取分类模板对应分类项
        List<CategoryTemplateItem> itemList = categoryTemplateItemService.queryChain()
                .eq(CategoryTemplateItem::getTemplateId, id)
                .eq(CategoryTemplateItem::getStatus, oldStatus)
                .list();
        // 3.如果存在分类项，则一并更新状态
        if (CollUtil.isNotEmpty(itemList)) {
            List<CategoryTemplateItem> updateItemList = itemList.stream()
                    .map(item -> CategoryTemplateItem.builder()
                            .id(item.getId())
                            .status(newStatus)
                            .build())
                    .toList();
            boolean updated = categoryTemplateItemService.updateBatch(updateItemList);
            ThrowUtils.throwIf(!updated, ErrorCode.OPERATION_ERROR, "分类模板状态切换失败");
        }
        // 4.更新分类模板状态
        CategoryTemplate categoryTemplate = CategoryTemplate.builder()
                .id(id)
                .status(newStatus)
                .build();
        boolean update = this.updateById(categoryTemplate);
        ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR, "分类模板状态切换失败");
        return null;
    }

    @Override
    public Boolean createCategoryTemplateItem(CategoryTemplateItemCreateRequest request) {
        // 1.参数校验
        Long templateId = request.getTemplateId();
        Long parentId = request.getParentId();
        String categoryName = request.getCategoryName();
        // 判断对应模板是否存在
        CategoryTemplate template = this.getById(templateId);
        ThrowUtils.throwIf(template == null, ErrorCode.PARAMS_ERROR, "分类模板不存在");
        // 获取创建项层级
        CategoryLevelEnum categoryLevel = CategoryLevelEnum.getLevelByParent(parentId);
        // 判断分类项父类是否存在以及是否重名
        validCategoryTemplateItem(null, null, templateId, parentId, categoryName, request.getCategoryType(), categoryLevel);
        // 2.创建分类项
        CategoryTemplateItem item = CategoryTemplateItem.builder()
                .templateId(templateId)
                .parentId(parentId)
                .categoryName(categoryName)
                .categoryType(request.getCategoryType())
                .categoryLevel(categoryLevel)
                .icon(request.getIcon())
                .color(request.getColor())
                .sortOrder(request.getSortOrder())
                .status(EnableStatusEnum.ENABLE)
                .build();
        boolean save = categoryTemplateItemService.save(item);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "分类项创建失败");
        return true;
    }

    @Override
    public Boolean updateCategoryTemplateItem(CategoryTemplateItemUpdateRequest request) {
        // 1.参数校验
        Long id = request.getId();
        Long templateId = request.getTemplateId();
        Long parentId = request.getParentId();
        String categoryName = request.getCategoryName();
        // 判断分类项是否存在
        CategoryTemplateItem oldItem = categoryTemplateItemService.getById(id);
        ThrowUtils.throwIf(oldItem == null, ErrorCode.PARAMS_ERROR, "分类项不存在");
        // 判断模板是否存在
        if (templateId != null && !templateId.equals(oldItem.getTemplateId())) {
            CategoryTemplate template = this.getById(templateId);
            ThrowUtils.throwIf(template == null, ErrorCode.PARAMS_ERROR, "分类模板不存在");
        } else {
            templateId = oldItem.getTemplateId();
        }
        // 获取创建项层级
        CategoryLevelEnum categoryLevel = CategoryLevelEnum.getLevelByParent(parentId);
        // 判断分类项父类是否存在以及是否重名
        validCategoryTemplateItem(id, oldItem, templateId, parentId, categoryName, request.getCategoryType(), categoryLevel);
        // 2.更新分类项
        CategoryTemplateItem updateItem = CategoryTemplateItem.builder()
                .id(id)
                .templateId(templateId)
                .parentId(parentId)
                .categoryName(categoryName)
                .categoryType(request.getCategoryType())
                .categoryLevel(categoryLevel)
                .icon(request.getIcon())
                .color(request.getColor())
                .sortOrder(request.getSortOrder())
                .build();
        boolean update = categoryTemplateItemService.updateById(updateItem);
        ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR, "分类项更新失败");
        return true;
    }

    @Override
    public Boolean deleteCategoryTemplateItem(Long id) {
        // 1.校验分类项是否存在
        CategoryTemplateItem item = categoryTemplateItemService.getById(id);
        ThrowUtils.throwIf(item == null, ErrorCode.PARAMS_ERROR, "分类项不存在");
        // 2.判断分类项下是否有子项
        if (item.getCategoryLevel().equals(CategoryLevelEnum.LEVEL_1)) {
            List<CategoryTemplateItem> children = categoryTemplateItemService.queryChain()
                    .eq(CategoryTemplateItem::getParentId, id)
                    .list();
            ThrowUtils.throwIf(CollUtil.isNotEmpty(children), ErrorCode.PARAMS_ERROR, "分类项下存在子项，请先删除子项");
        }
        // 3.删除分类项
        boolean delete = categoryTemplateItemService.removeById(id);
        ThrowUtils.throwIf(!delete, ErrorCode.OPERATION_ERROR, "分类项删除失败");
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeCategoryTemplateItemStatus(Long id) {
        // 1.校验分类项是否存在
        CategoryTemplateItem item = categoryTemplateItemService.getById(id);
        ThrowUtils.throwIf(item == null, ErrorCode.PARAMS_ERROR, "分类项不存在");
        // 2.更新分类项状态
        EnableStatusEnum changedStatus = EnableStatusEnum.changeStatus(item.getStatus());
        CategoryTemplateItem updateItem = CategoryTemplateItem.builder()
                .id(id)
                .status(changedStatus)
                .build();
        boolean update = categoryTemplateItemService.updateById(updateItem);
        ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR, "分类项状态切换失败");
        // 3.更新子项状态
        if (item.getCategoryLevel().equals(CategoryLevelEnum.LEVEL_1)) {
            List<CategoryTemplateItem> children = categoryTemplateItemService.queryChain()
                    .eq(CategoryTemplateItem::getParentId, id)
                    .list();
            if (CollUtil.isNotEmpty(children)) {
                List<CategoryTemplateItem> updateChildren = children.stream()
                        .map(child -> CategoryTemplateItem.builder()
                                .id(child.getId())
                                .status(changedStatus)
                                .build())
                        .toList();
                boolean updated = categoryTemplateItemService.updateBatch(updateChildren);
                ThrowUtils.throwIf(!updated, ErrorCode.OPERATION_ERROR, "分类项状态切换失败");
            }
        }
        return true;
    }

    @Override
    public CategoryTemplateVO getCategoryTemplateVO(Long id) {
        // 1.校验分类模板是否存在
        CategoryTemplate categoryTemplate = this.getById(id);
        ThrowUtils.throwIf(categoryTemplate == null, ErrorCode.PARAMS_ERROR, "分类模板不存在");
        // 2.转换成VO
        CategoryTemplateVO categoryTemplateVO = new CategoryTemplateVO();
        BeanUtil.copyProperties(categoryTemplate, categoryTemplateVO);
        // 3.获取分类模板对应分类项
        List<CategoryTemplateItem> children = categoryTemplateItemService.queryChain()
                .eq(CategoryTemplateItem::getTemplateId, id)
                .list();
        // 4.构建分类项树
        categoryTemplateVO.setItemList(categoryTemplateItemService.buildCategoryTemplateItemTreeList(children));
        return categoryTemplateVO;
    }

    @Override
    public PageResult<CategoryTemplateVO> getCategoryTemplateVOPage(CategoryTemplateQueryRequest request, PageQuery pageQuery) {
        // 1.构建查询条件
        QueryWrapper queryWrapper = this.getQueryWrapper(request, pageQuery);
        // 2.分页查询
        Page<CategoryTemplate> categoryTemplatePage = this.page(pageQuery.build(), queryWrapper);
        // 3.转换为封装类
        Page<CategoryTemplateVO> categoryTemplateVOPage = categoryTemplatePage.map(template -> {
            CategoryTemplateVO categoryTemplateVO = new CategoryTemplateVO();
            BeanUtil.copyProperties(template, categoryTemplateVO);
            return categoryTemplateVO;
        });
        // 4.返回结果
        return PageResult.build(categoryTemplateVOPage);
    }

    @Override
    public QueryWrapper getQueryWrapper(CategoryTemplateQueryRequest request, PageQuery pageQuery) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (pageQuery != null) {
            queryWrapper = pageQuery.buildOrderQuery();
        }
        if (request == null) {
            return queryWrapper;
        }
        String templateCode = request.getTemplateCode();
        String templateName = request.getTemplateName();
        Boolean isDefault = request.getIsDefault();
        EnableStatusEnum status = request.getStatus();
        queryWrapper.like(CategoryTemplate::getTemplateCode, templateCode, StringUtils::isNotBlank)
                .like(CategoryTemplate::getTemplateName, templateName, StringUtils::isNotBlank)
                .eq(CategoryTemplate::getIsDefault, isDefault, Objects::nonNull)
                .eq(CategoryTemplate::getStatus, status, Objects::nonNull);
        return queryWrapper;
    }

    /**
     * 校验分类模板是否存在，如果存在则抛出异常
     *
     * @param id           分类模板编号
     * @param templateCode 分类模板编码
     * @param templateName 分类模板名称
     * @param isDefault    是否默认模板
     */
    private void validCategoryTemplate(Long id, String templateCode, String templateName, Boolean isDefault) {
        // 1.校验是否存在相同编码或者名称的分类模板
        boolean codeExists = this.queryChain()
                .eq(CategoryTemplate::getTemplateCode, templateCode)
                .ne(CategoryTemplate::getId, id, id != null)
                .exists();
        ThrowUtils.throwIf(codeExists, ErrorCode.PARAMS_ERROR, "模板编码已存在");

        boolean nameExists = this.queryChain()
                .eq(CategoryTemplate::getTemplateName, templateName)
                .ne(CategoryTemplate::getId, id, id != null)
                .exists();
        ThrowUtils.throwIf(nameExists, ErrorCode.PARAMS_ERROR, "模板名称已存在");
        // 2.校验是否为默认模板
        if (isDefault != null && isDefault) {
            CategoryTemplate defaultCategoryTemplate = this.queryChain()
                    .eq(CategoryTemplate::getIsDefault, true)
                    .ne(CategoryTemplate::getId, id, id != null)
                    .one();
            // 如果存在默认模板，则取消其默认状态
            if (defaultCategoryTemplate != null) {
                defaultCategoryTemplate.setIsDefault(false);
                this.updateById(defaultCategoryTemplate);
            }
        }
    }

    /**
     * 校验分类项是否存在，如果存在则抛出异常
     *
     * @param id            分类项编号
     * @param templateId    分类模板编号
     * @param parentId      父分类项编号
     * @param categoryName  分类项名称
     * @param categoryType  分类项类型
     * @param categoryLevel 分类项等级
     */
    private void validCategoryTemplateItem(Long id, CategoryTemplateItem oldItem, Long templateId, Long parentId, String categoryName,
                                           TransactionTypeEnum categoryType, CategoryLevelEnum categoryLevel) {
        if (oldItem != null) {
            if (templateId == null) {
                templateId = oldItem.getTemplateId();
            }
            if (parentId == null) {
                parentId = oldItem.getParentId();
            }
            if (StringUtils.isBlank(categoryName)) {
                categoryName = oldItem.getCategoryName();
            }
            if (categoryType == null) {
                categoryType = oldItem.getCategoryType();
            }
        }
        // 判断分类项父类是否存在
        if (categoryLevel.equals(CategoryLevelEnum.LEVEL_2)) {
            CategoryTemplateItem parentItem = categoryTemplateItemService.getById(parentId);
            ThrowUtils.throwIf(parentItem == null, ErrorCode.PARAMS_ERROR, "分类项父类不存在");
            // 判断分类项父类是否属于当前分类模板
            ThrowUtils.throwIf(!templateId.equals(parentItem.getTemplateId()), ErrorCode.PARAMS_ERROR, "分类项父类不属于当前分类模板");
            // 判断父类是否为一级
            ThrowUtils.throwIf(!parentItem.getCategoryLevel().equals(CategoryLevelEnum.LEVEL_1), ErrorCode.PARAMS_ERROR, "分类项父类不属于一级分类");
            // 判断和父类类型是否一致
            ThrowUtils.throwIf(!parentItem.getCategoryType().equals(categoryType), ErrorCode.PARAMS_ERROR, "与父类类型不一致");
        }
        // 判断是否重名
        boolean nameExists = categoryTemplateItemService.queryChain()
                .eq(CategoryTemplateItem::getTemplateId, templateId)
                .eq(CategoryTemplateItem::getParentId, parentId)
                .eq(CategoryTemplateItem::getCategoryName, categoryName)
                .eq(CategoryTemplateItem::getCategoryType, categoryType)
                .eq(CategoryTemplateItem::getCategoryLevel, categoryLevel)
                .ne(CategoryTemplateItem::getId, id, Objects::nonNull)
                .exists();
        ThrowUtils.throwIf(nameExists, ErrorCode.PARAMS_ERROR, "分类项已存在");
    }
}
