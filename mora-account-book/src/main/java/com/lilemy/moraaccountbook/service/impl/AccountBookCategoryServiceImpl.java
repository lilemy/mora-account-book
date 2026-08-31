package com.lilemy.moraaccountbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.lilemy.moraaccountbook.exception.ErrorCode;
import com.lilemy.moraaccountbook.exception.ThrowUtils;
import com.lilemy.moraaccountbook.mapper.AccountBookCategoryMapper;
import com.lilemy.moraaccountbook.model.entity.AccountBookCategory;
import com.lilemy.moraaccountbook.model.entity.CategoryTemplate;
import com.lilemy.moraaccountbook.model.entity.CategoryTemplateItem;
import com.lilemy.moraaccountbook.model.enums.AccountBookCategoryLevelEnum;
import com.lilemy.moraaccountbook.model.enums.EnableStatusEnum;
import com.lilemy.moraaccountbook.model.vo.book.AccountBookCategoryVO;
import com.lilemy.moraaccountbook.service.AccountBookCategoryService;
import com.lilemy.moraaccountbook.service.CategoryTemplateItemService;
import com.lilemy.moraaccountbook.service.CategoryTemplateService;
import com.mybatisflex.core.keygen.impl.SnowFlakeIDKeyGenerator;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 账本分类 服务层实现。
 *
 * @author lilemy
 * @since 2026-08-28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBookCategoryServiceImpl extends ServiceImpl<AccountBookCategoryMapper, AccountBookCategory> implements AccountBookCategoryService {

    private final CategoryTemplateService categoryTemplateService;
    private final CategoryTemplateItemService categoryTemplateItemService;

    @Override
    public void initCategoryByTemplate(Long accountBookId) {
        // 1.获取默认分类模板
        CategoryTemplate template = categoryTemplateService.queryChain()
                .eq(CategoryTemplate::getIsDefault, true)
                .eq(CategoryTemplate::getStatus, EnableStatusEnum.ENABLE)
                .limit(1)
                .one();
        if (template == null) {
            log.error("未设置启用的默认分类模板");
            return;
        }
        // 2.获取默认分类模板下的所有分类
        List<CategoryTemplateItem> itemList = categoryTemplateItemService.queryChain()
                .eq(CategoryTemplateItem::getTemplateId, template.getId())
                .eq(CategoryTemplateItem::getStatus, EnableStatusEnum.ENABLE)
                .list();
        if (CollUtil.isEmpty(itemList)) {
            log.error("默认分类模板下未设置分类");
            return;
        }
        // 3.记录需要新增的账本分类
        List<AccountBookCategory> accountBookCategoryList = new ArrayList<>();
        // 4.获取一级分类
        SnowFlakeIDKeyGenerator generator = new SnowFlakeIDKeyGenerator();
        itemList.stream().filter(item -> item.getCategoryLevel().equals(AccountBookCategoryLevelEnum.LEVEL_1))
                .forEach(item -> {
                    long id = generator.nextId();
                    AccountBookCategory parentCategory = AccountBookCategory.builder()
                            .id(id)
                            .bookId(accountBookId)
                            .parentId(item.getParentId())
                            .categoryName(item.getCategoryName())
                            .categoryType(item.getCategoryType())
                            .categoryLevel(item.getCategoryLevel())
                            .icon(item.getIcon())
                            .color(item.getColor())
                            .sortOrder(item.getSortOrder())
                            .build();
                    accountBookCategoryList.add(parentCategory);
                    // 5.获取二级分类
                    itemList.stream().filter(subItem -> subItem.getParentId().equals(item.getId()))
                            .forEach(subItem -> {
                                AccountBookCategory childCategory = AccountBookCategory.builder()
                                        .bookId(accountBookId)
                                        .parentId(id)
                                        .categoryName(subItem.getCategoryName())
                                        .categoryType(subItem.getCategoryType())
                                        .categoryLevel(subItem.getCategoryLevel())
                                        .icon(subItem.getIcon())
                                        .color(subItem.getColor())
                                        .sortOrder(subItem.getSortOrder())
                                        .build();
                                accountBookCategoryList.add(childCategory);
                            });
                });
        // 6.批量新增账本分类
        boolean saved = this.saveBatch(accountBookCategoryList);
        ThrowUtils.throwIf(!saved, ErrorCode.OPERATION_ERROR, "初始化账本分类失败");
    }

    @Override
    public List<AccountBookCategoryVO> getListByBook(Long accountBookId) {
        if (accountBookId == null) {
            return List.of();
        }
        List<AccountBookCategory> categoryList = this.queryChain()
                .eq(AccountBookCategory::getBookId, accountBookId)
                .list();
        if (CollUtil.isEmpty(categoryList)) {
            return List.of();
        }
        return this.buildCategoryTreeList(categoryList);
    }

    @Override
    public List<AccountBookCategoryVO> buildCategoryTreeList(List<AccountBookCategory> categoryList) {
        if (CollUtil.isEmpty(categoryList)) {
            return CollUtil.newArrayList();
        }
        // 获取第一层分类模版项
        return categoryList.stream().filter(item ->
                        item.getCategoryLevel().equals(AccountBookCategoryLevelEnum.LEVEL_1) && item.getParentId().equals(0L))
                .map(item -> {
                    // 构建分类模版项 VO
                    AccountBookCategoryVO itemVO = new AccountBookCategoryVO();
                    BeanUtil.copyProperties(item, itemVO);
                    // 设置子分类模版项
                    List<AccountBookCategory> children = categoryList.stream().filter(sub ->
                            sub.getParentId().equals(item.getId())).toList();
                    if (CollUtil.isNotEmpty(children)) {
                        List<AccountBookCategoryVO> childrenVO = BeanUtil.copyToList(children, AccountBookCategoryVO.class);
                        // 按排序值排序
                        childrenVO.sort(Comparator.comparing(AccountBookCategoryVO::getSortOrder));
                        itemVO.setChildren(childrenVO);
                    } else {
                        itemVO.setChildren(CollUtil.newArrayList());
                    }
                    return itemVO;
                })
                .sorted(Comparator.comparing(AccountBookCategoryVO::getSortOrder))
                .toList();
    }
}
