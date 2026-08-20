drop database if exists mora_dev;
create database mora_dev;

comment on database mora_dev is '摩拉记账本本地';

-- 用户表
create table if not exists sys_user
(
    id            bigserial primary key,
    user_password varchar(512)                        not null,
    user_name     varchar(256)                        not null,
    user_avatar   varchar(512),
    user_profile  varchar(512),
    user_phone    varchar(11),
    user_email    varchar(256),
    user_gender   smallint  default 0                 not null,
    user_role     smallint  default 0                 not null,
    remark        varchar(512),
    edit_time     timestamp default CURRENT_TIMESTAMP not null,
    create_time   timestamp default CURRENT_TIMESTAMP not null,
    update_time   timestamp default CURRENT_TIMESTAMP not null,
    is_delete     smallint  default 0                 not null,
    constraint uk_user_email unique (user_email),
    constraint uk_user_phone unique (user_phone)
);

create index idx_sys_user_user_email on sys_user (user_email);
create index idx_sys_user_user_phone on sys_user (user_phone);
create index idx_sys_user_user_name on sys_user (user_name);

-- 注释
comment on table sys_user is '用户';
comment on column sys_user.id is 'id';
comment on column sys_user.user_password is '用户密码';
comment on column sys_user.user_name is '用户昵称';
comment on column sys_user.user_avatar is '用户头像';
comment on column sys_user.user_profile is '用户简介';
comment on column sys_user.user_phone is '用户手机号';
comment on column sys_user.user_email is '用户邮箱';
comment on column sys_user.user_gender is '用户性别(0未填写 1男 2女)';
comment on column sys_user.user_role is '用户角色(0用户 1管理员)';
comment on column sys_user.remark is '备注';
comment on column sys_user.edit_time is '编辑时间';
comment on column sys_user.create_time is '创建时间';
comment on column sys_user.update_time is '更新时间';
comment on column sys_user.is_delete is '是否删除';