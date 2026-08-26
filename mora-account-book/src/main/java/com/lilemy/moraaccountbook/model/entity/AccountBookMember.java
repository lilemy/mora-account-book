package com.lilemy.moraaccountbook.model.entity;

import com.lilemy.moraaccountbook.model.enums.AccountBookMemberRoleEnum;
import com.lilemy.moraaccountbook.model.enums.AccountBookMemberStatusEnum;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账本成员 实体类。
 *
 * @author lilemy
 * @since 2026-08-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table("account_book_member")
public class AccountBookMember extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 账本成员 ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 账本 ID
     */
    private Long bookId;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 成员角色(0所有者 1管理员 2普通成员)
     */
    private AccountBookMemberRoleEnum memberRole;

    /**
     * 是否为用户默认账本
     */
    private Boolean isDefault;

    /**
     * 成员状态(0正常 1已移除)
     */
    private AccountBookMemberStatusEnum status;

    /**
     * 加入账本时间
     */
    private LocalDateTime joinTime;

}
