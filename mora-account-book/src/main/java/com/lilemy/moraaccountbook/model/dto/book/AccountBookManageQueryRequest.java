package com.lilemy.moraaccountbook.model.dto.book;

import com.lilemy.moraaccountbook.model.enums.book.AccountBookStatusEnum;
import com.lilemy.moraaccountbook.model.enums.book.AccountBookTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 账本管理查询请求
 *
 * @author lilemy
 * @date 2026-08-27
 */
@Data
@Schema(description = "账本管理查询请求")
public class AccountBookManageQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -6796141321264833406L;

    /**
     * 账本所有者用户 ID
     */
    @Schema(description = "账本所有者用户 ID")
    private Long ownerId;

    /**
     * 账本名称
     */
    @Schema(description = "账本名称")
    private String bookName;

    /**
     * 账本类型(0个人账本 1共享账本)
     */
    @Schema(description = "账本类型")
    private AccountBookTypeEnum bookType;

    /**
     * 账本状态(0正常 1已归档)
     */
    @Schema(description = "账本状态")
    private AccountBookStatusEnum status;
}
