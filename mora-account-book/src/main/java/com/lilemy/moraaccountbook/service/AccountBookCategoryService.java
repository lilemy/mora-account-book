package com.lilemy.moraaccountbook.service;

import com.lilemy.moraaccountbook.model.entity.AccountBookCategory;
import com.lilemy.moraaccountbook.model.enums.TransactionTypeEnum;
import com.lilemy.moraaccountbook.model.vo.book.AccountBookCategoryVO;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 账本分类 服务层。
 *
 * @author lilemy
 * @since 2026-08-28
 */
public interface AccountBookCategoryService extends IService<AccountBookCategory> {

    /**
     * 根据模板初始化分类
     *
     * @param accountBookId 账本ID
     */
    void initCategoryByTemplate(Long accountBookId);

    /**
     * 根据账本ID获取分类列表
     *
     * @param accountBookId 账本ID
     * @param type          分类类型
     * @return 分类列表
     */
    List<AccountBookCategoryVO> getCategoryListByBook(Long accountBookId, TransactionTypeEnum type);

    /**
     * 构建分类树列表
     *
     * @param categoryList 分类列表
     * @return 分类树列表
     */
    List<AccountBookCategoryVO> buildCategoryTreeList(List<AccountBookCategory> categoryList);
}
