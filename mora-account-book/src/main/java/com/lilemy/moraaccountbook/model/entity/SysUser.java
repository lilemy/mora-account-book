package com.lilemy.moraaccountbook.model.entity;

import com.lilemy.moraaccountbook.model.enums.LogicallyDeleteEnum;
import com.lilemy.moraaccountbook.model.enums.SysUserGenderEnum;
import com.lilemy.moraaccountbook.model.enums.SysUserRoleEnum;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户 实体类。
 *
 * @author lilemy
 * @since 2026-08-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("sys_user")
public class SysUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户性别(0未填写 1男 2女)
     */
    private SysUserGenderEnum userGender;

    /**
     * 用户角色(0用户 1管理员)
     */
    private SysUserRoleEnum userRole;

    /**
     * 备注
     */
    private String remark;

    /**
     * 编辑时间
     */
    private Timestamp editTime;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 是否删除
     */
    @Column(isLogicDelete = true)
    private LogicallyDeleteEnum isDelete;

}
