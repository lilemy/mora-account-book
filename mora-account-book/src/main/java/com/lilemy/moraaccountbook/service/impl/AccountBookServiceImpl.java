package com.lilemy.moraaccountbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.lilemy.moraaccountbook.common.PageQuery;
import com.lilemy.moraaccountbook.common.PageResult;
import com.lilemy.moraaccountbook.constant.AccountBookConstant;
import com.lilemy.moraaccountbook.exception.BusinessException;
import com.lilemy.moraaccountbook.exception.ErrorCode;
import com.lilemy.moraaccountbook.exception.ThrowUtils;
import com.lilemy.moraaccountbook.mapper.AccountBookMapper;
import com.lilemy.moraaccountbook.model.dto.book.AccountBookManageQueryRequest;
import com.lilemy.moraaccountbook.model.dto.book.AccountBookUpdateRequest;
import com.lilemy.moraaccountbook.model.dto.book.AccountBookUserQueryRequest;
import com.lilemy.moraaccountbook.model.entity.AccountBook;
import com.lilemy.moraaccountbook.model.entity.AccountBookCategory;
import com.lilemy.moraaccountbook.model.entity.AccountBookMember;
import com.lilemy.moraaccountbook.model.entity.SysUser;
import com.lilemy.moraaccountbook.model.enums.AccountBookMemberRoleEnum;
import com.lilemy.moraaccountbook.model.enums.AccountBookMemberStatusEnum;
import com.lilemy.moraaccountbook.model.enums.AccountBookStatusEnum;
import com.lilemy.moraaccountbook.model.enums.AccountBookTypeEnum;
import com.lilemy.moraaccountbook.model.vo.book.AccountBookCategoryVO;
import com.lilemy.moraaccountbook.model.vo.book.AccountBookManageMemberVO;
import com.lilemy.moraaccountbook.model.vo.book.AccountBookManageVO;
import com.lilemy.moraaccountbook.model.vo.book.AccountBookUserVO;
import com.lilemy.moraaccountbook.service.AccountBookCategoryService;
import com.lilemy.moraaccountbook.service.AccountBookMemberService;
import com.lilemy.moraaccountbook.service.AccountBookService;
import com.lilemy.moraaccountbook.service.SysUserService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mybatisflex.core.query.QueryMethods.column;

/**
 * 账本 服务层实现。
 *
 * @author lilemy
 * @since 2026-08-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBookServiceImpl extends ServiceImpl<AccountBookMapper, AccountBook> implements AccountBookService {

    private final SysUserService sysUserService;
    private final AccountBookMemberService accountBookMemberService;
    private final AccountBookCategoryService accountBookCategoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initUserAccountBook(Long userId) {
        // 1.参数校验
        if (userId == null) {
            return;
        }
        // 2.判断用户是否已有账本
        boolean exists = this.queryChain()
                .eq(AccountBook::getOwnerId, userId)
                .exists();
        if (exists) {
            return;
        }
        // 3.初始化账本
        AccountBook accountBook = AccountBook.builder()
                .ownerId(userId)
                .bookName(AccountBookConstant.DEFAULT_BOOK_NAME)
                .bookType(AccountBookTypeEnum.PERSONAL)
                .icon(AccountBookConstant.DEFAULT_BOOK_ICON)
                .color(AccountBookConstant.DEFAULT_BOOK_COLOR)
                .status(AccountBookStatusEnum.NORMAL)
                .build();
        // 4.保存账本
        boolean save = this.save(accountBook);
        if (!save) {
            log.error("用户：{}，初始化账本失败", userId);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "初始化账本失败");
        }
        // 5.添加用户账本关联
        AccountBookMember accountBookMember = AccountBookMember.builder()
                .bookId(accountBook.getId())
                .userId(userId)
                .memberRole(AccountBookMemberRoleEnum.OWNER)
                .isDefault(true)
                .status(AccountBookMemberStatusEnum.NORMAL)
                .joinTime(LocalDateTime.now())
                .build();
        // 6.保存用户账本关联
        boolean saveMember = accountBookMemberService.save(accountBookMember);
        if (!saveMember) {
            log.error("用户：{}，初始化账本关联失败", userId);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "初始化账本关联失败");
        }
        // 7.添加默认分类
        accountBookCategoryService.initCategoryByTemplate(accountBook.getId());
    }

    @Override
    public PageResult<AccountBookUserVO> getUserAccountBookPage(AccountBookUserQueryRequest request, PageQuery pageQuery) {
        // 1.获取当前登录用户
        SysUser loginUser = sysUserService.getLoginUser();
        // 2.构建用户账本联表查询
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(
                        column(AccountBook::getId),
                        column(AccountBook::getOwnerId),
                        column(SysUser::getUserPhone).as(AccountBookUserVO::getOwnerPhone),
                        column(SysUser::getUserName).as(AccountBookUserVO::getOwnerName),
                        column(AccountBook::getBookName),
                        column(AccountBook::getBookType),
                        column(AccountBook::getIcon),
                        column(AccountBook::getColor),
                        column(AccountBook::getDescription),
                        column(AccountBook::getStatus),
                        column(AccountBook::getCreateTime),
                        column(AccountBookMember::getMemberRole),
                        column(AccountBookMember::getIsDefault),
                        column(AccountBookMember::getJoinTime)
                )
                .from(AccountBook.class)
                .innerJoin(AccountBookMember.class)
                .on(AccountBookMember::getBookId, AccountBook::getId)
                .leftJoin(SysUser.class)
                .on(SysUser::getId, AccountBook::getOwnerId)
                .where(AccountBookMember::getUserId).eq(loginUser.getId())
                .and(AccountBookMember::getStatus).eq(AccountBookMemberStatusEnum.NORMAL);

        // 3.添加筛选条件
        if (request != null) {
            queryWrapper.and(AccountBook::getBookName).like(request.getBookName(), StringUtils::isNotBlank)
                    .and(AccountBook::getBookType).eq(request.getBookType(), Objects::nonNull)
                    .and(AccountBook::getStatus).eq(request.getStatus(), Objects::nonNull);
        }

        // 4.默认账本优先，其次正常账本优先；加入时间和账本 ID 用于保证分页顺序稳定
        queryWrapper.orderBy(AccountBookMember::getIsDefault).desc()
                .orderBy(AccountBook::getStatus).asc()
                .orderBy(AccountBookMember::getJoinTime).desc()
                .orderBy(AccountBook::getId).desc();

        // 5.分页查询并返回封装结果
        PageQuery actualPageQuery = pageQuery == null ? new PageQuery() : pageQuery;
        Page<AccountBookUserVO> accountBookPage = getMapper().paginateAs(
                actualPageQuery.build(), queryWrapper, AccountBookUserVO.class
        );
        return PageResult.build(accountBookPage);
    }

    @Override
    public PageResult<AccountBookManageVO> getAccountBookManageVOPage(AccountBookManageQueryRequest request, PageQuery pageQuery) {
        // 1.构建查询条件
        QueryWrapper queryWrapper = this.getQueryWrapper(request, pageQuery);
        // 2.分页查询
        Page<AccountBook> accountBookPage = this.page(pageQuery.build(), queryWrapper);
        List<AccountBook> accountBookList = accountBookPage.getRecords();
        if (CollUtil.isEmpty(accountBookList)) {
            return PageResult.build();
        }
        // 3.获取用户信息
        Set<Long> userIds = accountBookList.stream().map(AccountBook::getOwnerId).collect(Collectors.toSet());
        List<SysUser> userList = sysUserService.listByIds(userIds);
        Map<Long, SysUser> userMap = userList.stream()
                .collect(Collectors.toMap(SysUser::getId, Function.identity(), (v1, v2) -> v1));
        // 4.转换为封装类
        Page<AccountBookManageVO> accountBookManageVOPage = accountBookPage.map(accountBook -> {
            AccountBookManageVO accountBookManageVO = new AccountBookManageVO();
            BeanUtil.copyProperties(accountBook, accountBookManageVO);
            // 设置用户信息
            if (userMap.containsKey(accountBook.getOwnerId())) {
                SysUser sysUser = userMap.get(accountBook.getOwnerId());
                accountBookManageVO.setOwnerPhone(sysUser.getUserPhone());
                accountBookManageVO.setOwnerName(sysUser.getUserName());
            }
            return accountBookManageVO;
        });
        // 5.返回结果
        return PageResult.build(accountBookManageVOPage);
    }

    @Override
    public QueryWrapper getQueryWrapper(AccountBookManageQueryRequest request, PageQuery pageQuery) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (pageQuery != null) {
            queryWrapper = pageQuery.buildOrderQuery();
        }
        if (request == null) {
            return queryWrapper;
        }
        Long ownerId = request.getOwnerId();
        String bookName = request.getBookName();
        AccountBookTypeEnum bookType = request.getBookType();
        AccountBookStatusEnum status = request.getStatus();
        queryWrapper.like(AccountBook::getBookName, bookName, StringUtils::isNotBlank)
                .eq(AccountBook::getOwnerId, ownerId, Objects::nonNull)
                .eq(AccountBook::getBookType, bookType, Objects::nonNull)
                .eq(AccountBook::getStatus, status, Objects::nonNull);
        return queryWrapper;
    }

    @Override
    public AccountBookManageVO getAccountBookManageVO(AccountBook accountBook) {
        if (accountBook == null) {
            return null;
        }
        // 1.创建封装对象
        AccountBookManageVO accountBookManageVO = new AccountBookManageVO();
        BeanUtil.copyProperties(accountBook, accountBookManageVO);
        // 2.获取账本成员列表
        List<AccountBookMember> bookMemberList = accountBookMemberService.queryChain()
                .eq(AccountBookMember::getBookId, accountBook.getId())
                .list();
        // 3.获取需查询用户信息列表
        Set<Long> userIds = bookMemberList.stream().map(AccountBookMember::getUserId).collect(Collectors.toSet());
        userIds.add(accountBook.getOwnerId());
        // 4.获取用户信息
        List<SysUser> userList = sysUserService.listByIds(userIds);
        Map<Long, SysUser> userMap = userList.stream()
                .collect(Collectors.toMap(SysUser::getId, Function.identity(), (v1, v2) -> v1));
        // 5.封装创建用户信息
        if (userMap.containsKey(accountBook.getOwnerId())) {
            SysUser sysUser = userMap.get(accountBook.getOwnerId());
            accountBookManageVO.setOwnerPhone(sysUser.getUserPhone());
            accountBookManageVO.setOwnerName(sysUser.getUserName());
        }
        // 6.封装账本成员列表
        List<AccountBookManageMemberVO> memberVOList = bookMemberList.stream()
                .map(member -> {
                    AccountBookManageMemberVO memberVO = new AccountBookManageMemberVO();
                    BeanUtil.copyProperties(member, memberVO);
                    if (userMap.containsKey(member.getUserId())) {
                        SysUser sysUser = userMap.get(member.getUserId());
                        memberVO.setUserPhone(sysUser.getUserPhone());
                        memberVO.setUserName(sysUser.getUserName());
                    }
                    return memberVO;
                })
                .sorted(Comparator.comparing(AccountBookManageMemberVO::getStatus)
                        .thenComparing(AccountBookManageMemberVO::getMemberRole)
                        .thenComparing(AccountBookManageMemberVO::getJoinTime))
                .toList();
        accountBookManageVO.setMemberList(memberVOList);
        // 7.封装账本分类列表
        List<AccountBookCategoryVO> categoryVOList = accountBookCategoryService.getListByBook(accountBook.getId());
        accountBookManageVO.setCategoryList(categoryVOList);
        // 8.返回封装对象
        return accountBookManageVO;
    }

    @Override
    public Boolean updateAccountBook(AccountBookUpdateRequest request) {
        // 1.查询修改账本
        Long id = request.getId();
        AccountBook accountBook = this.getById(id);
        ThrowUtils.throwIf(accountBook == null, ErrorCode.NOT_FOUND_ERROR, "账本不存在");
        // 2.更新账本
        AccountBook updateAccountBook = new AccountBook();
        BeanUtil.copyProperties(request, updateAccountBook);
        boolean result = this.updateById(updateAccountBook);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "更新失败");
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAccountBook(Long id) {
        // 1.查询账本
        AccountBook accountBook = this.getById(id);
        ThrowUtils.throwIf(accountBook == null, ErrorCode.NOT_FOUND_ERROR, "账本不存在");
        // 2.删除账本
        boolean result = this.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "账本删除失败");
        // 3.查询账本用户关联
        List<AccountBookMember> bookMemberList = accountBookMemberService.queryChain()
                .eq(AccountBookMember::getBookId, id)
                .list();
        if (CollUtil.isNotEmpty(bookMemberList)) {
            // 4.删除账本用户关联
            List<Long> memberIds = bookMemberList.stream().map(AccountBookMember::getId).toList();
            boolean remove = accountBookMemberService.removeByIds(memberIds);
            ThrowUtils.throwIf(!remove, ErrorCode.OPERATION_ERROR, "账本关联删除失败");
        }
        // 5.查询账本分类关联
        List<AccountBookCategory> bookCategoryList = accountBookCategoryService.queryChain()
                .eq(AccountBookCategory::getBookId, id)
                .list();
        if (CollUtil.isNotEmpty(bookCategoryList)) {
            // 6.删除账本分类关联
            List<Long> categoryIds = bookCategoryList.stream().map(AccountBookCategory::getId).toList();
            boolean remove = accountBookCategoryService.removeByIds(categoryIds);
            ThrowUtils.throwIf(!remove, ErrorCode.OPERATION_ERROR, "账本分类关联删除失败");
        }
        return true;
    }
}
