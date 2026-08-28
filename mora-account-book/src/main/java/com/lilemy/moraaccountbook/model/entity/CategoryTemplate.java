package com.lilemy.moraaccountbook.model.entity;

import com.lilemy.moraaccountbook.model.enums.EnableStatusEnum;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分类模板 实体类。
 *
 * @author lilemy
 * @since 2026-08-28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table("category_template")
public class CategoryTemplate extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类模板 ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 是否为默认模板
     */
    private Boolean isDefault;

    /**
     * 模板状态(0启用 1停用)
     */
    private EnableStatusEnum status;

}
