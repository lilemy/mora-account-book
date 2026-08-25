package com.lilemy.moraaccountbook.manager.oss.entity;

import lombok.Data;

/**
 * 对象存储信息
 *
 * @author lilemy
 * @date 2026-08-25
 */
@Data
public class OssVO {

    /**
     * 对象存储地址
     */
    private String url;

    /**
     * 对象存储大小
     */
    private Long size;
}
