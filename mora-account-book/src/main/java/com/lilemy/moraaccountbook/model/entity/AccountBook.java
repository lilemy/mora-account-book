package com.lilemy.moraaccountbook.model.entity;

import com.lilemy.moraaccountbook.model.enums.book.AccountBookStatusEnum;
import com.lilemy.moraaccountbook.model.enums.book.AccountBookTypeEnum;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 账本 实体类。
 *
 * @author lilemy
 * @since 2026-08-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table("account_book")
public class AccountBook extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 账本 ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 账本所有者用户 ID
     */
    private Long ownerId;

    /**
     * 账本名称
     */
    private String bookName;

    /**
     * 账本类型(0个人账本 1共享账本)
     */
    private AccountBookTypeEnum bookType;

    /**
     * 账本图标标识
     */
    private String icon;

    /**
     * 账本主题色
     */
    private String color;

    /**
     * 账本说明
     */
    private String description;

    /**
     * 账本状态(0正常 1已归档)
     */
    private AccountBookStatusEnum status;

}
