package com.lilemy.moraaccountbook.model.listener;

import com.lilemy.moraaccountbook.model.entity.BaseEntity;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;

import java.time.LocalDateTime;

/**
 * 实体时间字段自动填充监听器。
 */
public class EntityTimestampListener implements InsertListener, UpdateListener {

    @Override
    public void onInsert(Object entity) {
        BaseEntity baseEntity = (BaseEntity) entity;
        LocalDateTime now = LocalDateTime.now();
        if (baseEntity.getCreateTime() == null) {
            baseEntity.setCreateTime(now);
        }
        baseEntity.setUpdateTime(now);
    }

    @Override
    public void onUpdate(Object entity) {
        BaseEntity baseEntity = (BaseEntity) entity;
        baseEntity.setUpdateTime(LocalDateTime.now());
    }
}
