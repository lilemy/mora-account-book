package com.lilemy.moraaccountbook.model.vo.book;

import com.lilemy.moraaccountbook.model.enums.book.AccountBookStatusEnum;
import com.lilemy.moraaccountbook.model.enums.book.AccountBookTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 账本管理封装
 *
 * @author lilemy
 * @date 2026-08-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "账本管理封装")
public class AccountBookManageVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7800609575416176558L;

    /**
     * 账本 ID
     */
    @Schema(description = "账本 ID")
    private Long id;

    /**
     * 账本所有者用户 ID
     */
    @Schema(description = "账本所有者用户 ID")
    private Long ownerId;

    /**
     * 账本所有者用户手机号
     */
    @Schema(description = "账本所有者用户手机号")
    private String ownerPhone;

    /**
     * 账本所有者用户昵称
     */
    @Schema(description = "账本所有者用户昵称")
    private String ownerName;

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
     * 账本图标标识
     */
    @Schema(description = "账本图标标识")
    private String icon;

    /**
     * 账本主题色
     */
    @Schema(description = "账本主题色")
    private String color;

    /**
     * 账本说明
     */
    @Schema(description = "账本说明")
    private String description;

    /**
     * 账本状态(0正常 1已归档)
     */
    @Schema(description = "账本状态")
    private AccountBookStatusEnum status;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 账本成员列表
     */
    @Schema(description = "账本成员列表")
    private List<AccountBookManageMemberVO> memberList;

    /**
     * 账本分类列表
     */
    @Schema(description = "账本分类列表")
    private List<AccountBookCategoryVO> categoryList;
}
