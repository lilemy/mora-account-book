package com.lilemy.moraaccountbook.config;

import com.lilemy.moraaccountbook.model.entity.BaseEntity;
import com.lilemy.moraaccountbook.model.listener.EntityTimestampListener;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * MyBatisFlex 配置类
 *
 * @author lilemy
 * @date 2026-08-20
 */
@Configuration
public class MybatisFlexConfig {

    @PostConstruct
    public void init() {
        // 为所有 BaseEntity 子类注册新增、更新时的公共字段填充。
        EntityTimestampListener timestampListener = new EntityTimestampListener();
        FlexGlobalConfig globalConfig = FlexGlobalConfig.getDefaultConfig();
        globalConfig.registerInsertListener(timestampListener, BaseEntity.class);
        globalConfig.registerUpdateListener(timestampListener, BaseEntity.class);

        // 1. 开启审计功能
        AuditManager.setAuditEnable(true);

        // 2. 设置 SQL 审计收集器
        AuditManager.setMessageCollector(auditMessage -> {
                    String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    System.err.println(" Consume Time：" + auditMessage.getElapsedTime() + " ms " + now);
                    System.err.println(" Execute SQL：" + auditMessage.getFullSql());
                    System.out.println(); // 加个空行方便阅读
                }
        );
    }
}
