package com.lilemy.moraaccountbook.service;

import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.model.dto.bill.AccountBookBillCalendarQueryRequest;
import com.lilemy.moraaccountbook.model.dto.bill.AccountBookBillCreateRequest;
import com.lilemy.moraaccountbook.model.dto.bill.AccountBookBillQueryRequest;
import com.lilemy.moraaccountbook.model.dto.bill.AccountBookBillUpdateRequest;
import com.lilemy.moraaccountbook.model.entity.AccountBookBill;
import com.lilemy.moraaccountbook.model.vo.bill.AccountBookBillManageVO;
import com.lilemy.moraaccountbook.model.vo.bill.AccountBookBillMonthListVO;
import com.lilemy.moraaccountbook.model.vo.bill.AccountBookBillMonthTotalVO;
import com.lilemy.moraaccountbook.model.vo.bill.AccountBookBillVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 账本收入/支出流水 服务层。
 *
 * @author lilemy
 * @since 2026-09-09
 */
public interface AccountBookBillService extends IService<AccountBookBill> {

    /**
     * 创建账本收入/支出流水。
     *
     * @param request 创建参数。
     * @return 新建流水主键。
     */
    Long createAccountBookBill(AccountBookBillCreateRequest request);

    /**
     * 部分更新本人流水，并在同一事务中调整账户余额；不允许更换账本和记账人。
     * 未传字段保留原值，账户 ID 为 0 时解除关联，备注为空字符串时清空。
     *
     * @param request 更新参数。
     * @return 是否更新成功。
     */
    Boolean updateAccountBookBill(AccountBookBillUpdateRequest request);

    /**
     * 根据主键删除本人流水，并在同一事务中撤销账户余额影响。
     * 若撤销后余额为负或欠款超过信用额度，则拒绝删除。
     *
     * @param id 主键。
     * @return 是否删除成功。
     */
    Boolean deleteAccountBookBillById(Long id);

    /**
     * 按照指定月份获取分组列表。
     *
     * @param request 查询条件。
     * @return 账本收入/支出流水分组列表。
     */
    List<AccountBookBillMonthListVO> getAccountBookBillVOListByMonth(AccountBookBillCalendarQueryRequest request);

    /**
     * 获取指定年份的各月汇总数据。
     *
     * @param request 查询条件。
     * @return 各月汇总数据。
     */
    List<AccountBookBillMonthTotalVO> getAccountBookBillTotalByMonth(AccountBookBillCalendarQueryRequest request);

    /**
     * 根据主键获取账本收入/支出流水VO。
     *
     * @param id 主键。
     * @return 账本收入/支出流水VO。
     */
    AccountBookBillVO getAccountBookBillVOById(Long id);

    /**
     * 根据查询条件获取账本收入/支出流水分页列表。
     *
     * @param request   查询条件。
     * @param pageQuery 分页查询条件。
     * @return 账本收入/支出流水分页列表。
     */
    PageResult<AccountBookBillVO> getAccountBookBillVOPage(AccountBookBillQueryRequest request, PageQuery pageQuery);

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
