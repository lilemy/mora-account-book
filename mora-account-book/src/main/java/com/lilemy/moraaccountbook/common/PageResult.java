package com.lilemy.moraaccountbook.common;

import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 *
 * @author lilemy
 * @date 2026-08-21
 */
@Data
@NoArgsConstructor
@Schema(description = "分页结果")
public class PageResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -5062728717277933248L;

    /**
     * 当前页数据。
     */
    @Schema(description = "当前页数据")
    private List<T> records;

    /**
     * 当前页码。
     */
    @Schema(description = "当前页码")
    private Long pageNum;

    /**
     * 每页数据数量。
     */
    @Schema(description = "每页数据数量")
    private Long pageSize;

    /**
     * 总页数。
     */
    @Schema(description = "总页数")
    private Long totalPage;

    /**
     * 总数据数量。
     */
    @Schema(description = "总数据数量")
    private Long totalRow;

    /**
     * 构建表格分页数据对象。
     *
     * @param pageData 分页数据
     * @param <T>      列表数据类型
     * @return 表格分页数据对象
     */
    public static <T> PageResult<T> build(Page<T> pageData) {
        PageResult<T> result = new PageResult<>();
        result.setRecords(pageData.getRecords());
        result.setPageNum(pageData.getPageNumber());
        result.setPageSize(pageData.getPageSize());
        result.setTotalPage(pageData.getTotalPage());
        result.setTotalRow(pageData.getTotalRow());
        return result;
    }

    /**
     * 构建空表格分页数据对象。
     *
     * @param <T> 列表数据类型
     * @return 表格分页数据对象
     */
    public static <T> PageResult<T> build() {
        return new PageResult<>();
    }
}
