package com.lilemy.moraaccountbook.service;

import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.model.dto.book.AccountBookCategoryQueryRequest;
import com.lilemy.moraaccountbook.model.dto.book.AccountBookManageQueryRequest;
import com.lilemy.moraaccountbook.model.dto.book.AccountBookUpdateRequest;
import com.lilemy.moraaccountbook.model.dto.book.AccountBookUserQueryRequest;
import com.lilemy.moraaccountbook.model.entity.AccountBook;
import com.lilemy.moraaccountbook.model.vo.book.AccountBookCategoryVO;
import com.lilemy.moraaccountbook.model.vo.book.AccountBookManageVO;
import com.lilemy.moraaccountbook.model.vo.book.AccountBookUserVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 账本 服务层。
 *
 * @author lilemy
 * @since 2026-08-26
 */
public interface AccountBookService extends IService<AccountBook> {

    /**
     * 初始化用户账本
     *
     * @param userId 用户ID
     */
    void initUserAccountBook(Long userId);

    /**
     * 获取用户账本分页
     *
     * @param request   查询请求
     * @param pageQuery 分页查询
     * @return 用户账本分页
     */
    PageResult<AccountBookUserVO> getUserAccountBookPage(AccountBookUserQueryRequest request, PageQuery pageQuery);

    /**
     * 获取账本管理封装信息分页
     *
     * @param request   查询请求
     * @param pageQuery 分页查询
     * @return 账本管理封装信息分页
     */
    PageResult<AccountBookManageVO> getAccountBookManageVOPage(AccountBookManageQueryRequest request, PageQuery pageQuery);

    /**
     * 获取查询条件
     *
     * @param request   查询请求
     * @param pageQuery 分页查询
     * @return 查询条件
     */
    QueryWrapper getQueryWrapper(AccountBookManageQueryRequest request, PageQuery pageQuery);

    /**
     * 获取账本管理封装信息
     *
     * @param accountBook 账本信息
     * @return 账本管理封装信息
     */
    AccountBookManageVO getAccountBookManageVO(AccountBook accountBook);

    /**
     * 获取账本分类封装信息列表
     *
     * @param request 查询请求
     * @return 账本分类封装信息列表
     */
    List<AccountBookCategoryVO> getAccountBookCategoryVOList(AccountBookCategoryQueryRequest request);

    /**
     * 更新账本
     *
     * @param request 账本更新请求
     * @return 是否更新成功
     */
    Boolean updateAccountBook(AccountBookUpdateRequest request);

    /**
     * 删除账本
     *
     * @param id 账本ID
     * @return 是否删除成功
     */
    Boolean deleteAccountBook(Long id);

    /**
     * 根据ID集合获取账本信息
     *
     * @param ids ID集合
     * @return 账本信息
     */
    Map<Long, AccountBook> getAccountBookMapByIds(Collection<Long> ids);

    /**
     * 检查账本权限
     *
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 账本信息
     */
    AccountBook checkBookPermission(Long bookId, Long userId);
}
