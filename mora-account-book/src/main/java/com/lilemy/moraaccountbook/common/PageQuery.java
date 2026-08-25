package com.lilemy.moraaccountbook.common;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.lilemy.moraaccountbook.exception.BusinessException;
import com.lilemy.moraaccountbook.exception.ErrorCode;
import com.lilemy.moraaccountbook.utils.SqlUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页查询参数
 *
 * @author lilemy
 * @date 2026-08-21
 */
@Data
@NoArgsConstructor
public class PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = -2521788335830900272L;

    /**
     * 分页大小
     */
    @Schema(description = "分页大小")
    private Integer pageSize;

    /**
     * 当前页数
     */
    @Schema(description = "当前页数")
    private Integer pageNum;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private String sortField;

    /**
     * 排序顺序
     */
    @Schema(description = "排序顺序")
    private String sortOrder;

    /**
     * 当前记录起始索引 默认值
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 每页显示记录数 默认值 默认 10 条
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 构建分页对象。
     *
     * @param <T> 分页记录类型
     * @return MyBatis-Plus 分页对象
     */
    public <T> Page<T> build() {
        Integer pageNum = ObjectUtil.defaultIfNull(getPageNum(), DEFAULT_PAGE_NUM);
        Integer pageSize = ObjectUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
        if (pageNum <= 0) {
            pageNum = DEFAULT_PAGE_NUM;
        }
        return new Page<>(pageNum, pageSize);
    }

    /**
     * 构建排序
     * <p>
     * 支持的用法如下:
     * {sortOrder:"asc",sortField:"id"} order by id asc
     * {sortOrder:"asc",sortField:"id,createTime"} order by id asc,create_time asc
     * {sortOrder:"desc",sortField:"id,createTime"} order by id desc,create_time desc
     * {sortOrder:"asc,desc",sortField:"id,createTime"} order by id asc,create_time desc
     */
    public QueryWrapper buildOrderQuery() {
        if (StringUtils.isBlank(sortField) || StringUtils.isBlank(sortOrder)) {
            return QueryWrapper.create();
        }
        String sortFieldBy = SqlUtil.escapeOrderBySql(sortField);
        sortFieldBy = StrUtil.toUnderlineCase(sortFieldBy);

        // 兼容前端排序类型
        String orderDirection = StringUtils.replaceEach(sortOrder, new String[]{"ascend", "descend"}, new String[]{"asc", "desc"});

        String[] sortFieldArr = sortFieldBy.split(",");
        String[] sortOrderArr = orderDirection.split(",");
        if (sortOrderArr.length != 1 && sortOrderArr.length != sortFieldArr.length) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "排序参数错误");
        }
        return createQueryWrapper(sortFieldArr, sortOrderArr);
    }

    private static QueryWrapper createQueryWrapper(String[] sortFieldArr, String[] sortOrderArr) {
        QueryWrapper queryWrapper = new QueryWrapper();
        // 每个字段各自排序
        for (int i = 0; i < sortFieldArr.length; i++) {
            String sortFieldByStr = sortFieldArr[i];
            String sortOrderStr = sortOrderArr.length == 1 ? sortOrderArr[0] : sortOrderArr[i];
            if ("asc".equals(sortOrderStr)) {
                queryWrapper.orderBy(sortFieldByStr, true);
            } else if ("desc".equals(sortOrderStr)) {
                queryWrapper.orderBy(sortFieldByStr, false);
            } else {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "排序参数错误");
            }
        }
        return queryWrapper;
    }

    /**
     * 构造分页查询对象。
     *
     * @param pageSize 分页大小
     * @param pageNum  当前页码
     */
    public PageQuery(Integer pageSize, Integer pageNum) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }
}
