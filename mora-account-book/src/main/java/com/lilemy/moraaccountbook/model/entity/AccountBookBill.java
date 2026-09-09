package com.lilemy.moraaccountbook.model.entity;

import com.lilemy.moraaccountbook.model.enums.TransactionTypeEnum;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账本收入/支出流水 实体类。
 *
 * @author lilemy
 * @since 2026-09-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table("account_book_bill")
public class AccountBookBill extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 流水 ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 所属账本 ID
     */
    private Long bookId;

    /**
     * 记账人用户 ID
     */
    private Long userId;

    /**
     * 关联账户 ID
     */
    private Long accountId;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 关联账本分类 ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 流水类型：1收入，2支出
     */
    private TransactionTypeEnum billType;

    /**
     * 流水金额
     */
    private BigDecimal amount;

    /**
     * 流水发生时间
     */
    private LocalDateTime billTime;

    /**
     * 流水备注
     */
    private String remark;

}
