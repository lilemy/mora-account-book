package com.lilemy.moraaccountbook.service;

import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.model.dto.bill.AccountBookBillQueryRequest;
import com.lilemy.moraaccountbook.model.entity.AccountBookBill;
import com.lilemy.moraaccountbook.model.vo.bill.AccountBookBillManageVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;

/**
 * 账本收入/支出流水 服务层。
 *
 * @author lilemy
 * @since 2026-09-09
 */
public interface AccountBookBillService extends IService<AccountBookBill> {

    /**
     * 根据主键获取账本收入/支出流水VO。
     *
     * @param id 主键。
     * @return 账本收入/支出流水VO。
     */
    AccountBookBillManageVO getAccountBookBillManageVOById(Long id);

    /**
     * 根据查询条件获取账本收入/支出流水分页列表。
     *
     * @param request   查询条件。
     * @param pageQuery 分页查询条件。
     * @return 账本收入/支出流水分页列表。
     */
    PageResult<AccountBookBillManageVO> getAccountBookBillManageVOPage(AccountBookBillQueryRequest request,
                                                                       PageQuery pageQuery);

    /**
     * 根据查询条件构建查询条件。
     *
     * @param request   查询条件。
     * @param pageQuery 分页查询条件。
     * @return 查询条件。
     */
    QueryWrapper buildQueryWrapper(AccountBookBillQueryRequest request, PageQuery pageQuery);
}
