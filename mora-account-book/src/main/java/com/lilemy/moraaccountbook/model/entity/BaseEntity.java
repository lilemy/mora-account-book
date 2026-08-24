package com.lilemy.moraaccountbook.model.entity;

import com.mybatisflex.annotation.Column;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体公共字段。
 *
 * @author lilemy
 * @since 2026-08-25
 */
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 创建时间，仅在新增时由数据库时间填充。
     */
    @Column(updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间，新增和更新时均由数据库时间填充。
     */
    private LocalDateTime updateTime;
}
