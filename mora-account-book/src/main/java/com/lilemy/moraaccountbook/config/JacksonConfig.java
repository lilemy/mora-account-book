package com.lilemy.moraaccountbook.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalTimeDeserializer;
import tools.jackson.databind.ext.javatime.deser.YearMonthDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.YearMonthSerializer;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Jackson 序列化配置
 * <p>
 * Spring Boot 4 默认使用 Jackson 3（tools.jackson），HTTP 消息转换由
 * {@code JacksonJsonHttpMessageConverter} 完成，其 JsonMapper 通过
 * {@link JsonMapperBuilderCustomizer} 定制。
 *
 * @author lilemy
 * @date 2026-08-20
 */
@Slf4j
@Configuration
public class JacksonConfig {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * 定制 Web 层实际使用的 JsonMapper。
     * <p>
     * 注意：必须用 Builder 定制，直接声明 {@code com.fasterxml.jackson.databind.ObjectMapper}
     * （Jackson 2）的 Bean 不会被 Spring Boot 4 的 HTTP 消息转换器采用。
     */
    @Bean
    public JsonMapperBuilderCustomizer jsonMapperBuilderCustomizer() {
        return builder -> {
            // 忽略未知字段（前端传入后端未定义的字段时一律忽略）
            builder.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // Long 转字符串，防止 JS 精度丢失（超过 Number.MAX_SAFE_INTEGER）
            SimpleModule longToStringModule = new SimpleModule("LongToString");
            longToStringModule.addSerializer(Long.class, ToStringSerializer.instance);
            longToStringModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
            // 时间类型的序列化/反序列化格式
            SimpleModule javaTimeModule = new SimpleModule("JavaTimeFormat");
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));
            javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER));
            javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));
            javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(TIME_FORMATTER));
            javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(TIME_FORMATTER));
            javaTimeModule.addSerializer(YearMonth.class, new YearMonthSerializer(YEAR_MONTH_FORMATTER));
            javaTimeModule.addDeserializer(YearMonth.class, new YearMonthDeserializer(YEAR_MONTH_FORMATTER));
            builder.addModules(longToStringModule, javaTimeModule);
            // 设置时区
            builder.defaultTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            log.info("初始化 Jackson 配置");
        };
    }
}
