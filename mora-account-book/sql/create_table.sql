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

-- 账本表
drop table if exists account_book;
create table if not exists account_book
(
    id          bigint primary key,
    owner_id    bigint                                not null,
    book_name   varchar(64)                           not null,
    book_type   smallint    default 0                 not null,
    icon        varchar(64) default 'wallet'          not null,
    color       varchar(16) default '#64bbcf'         not null,
    description varchar(256),
    status      smallint    default 0                 not null,
    create_time timestamp   default CURRENT_TIMESTAMP not null,
    update_time timestamp   default CURRENT_TIMESTAMP not null
);

create index idx_account_book_owner_status
    on account_book (owner_id, status);

-- 账本成员表
drop table if exists account_book_member;
create table if not exists account_book_member
(
    id          bigint primary key,
    book_id     bigint                              not null,
    user_id     bigint                              not null,
    member_role smallint  default 2                 not null,
    is_default  boolean   default false             not null,
    status      smallint  default 0                 not null,
    join_time   timestamp default CURRENT_TIMESTAMP not null,
    create_time timestamp default CURRENT_TIMESTAMP not null,
    update_time timestamp default CURRENT_TIMESTAMP not null,
    constraint uk_account_book_member unique (book_id, user_id)
);

create index idx_account_book_member_user_status
    on account_book_member (user_id, status);
create index idx_account_book_member_book_status
    on account_book_member (book_id, status);
create unique index uk_account_book_member_owner_active
    on account_book_member (book_id)
    where member_role = 0 and status = 0;
create unique index uk_account_book_member_default_active
    on account_book_member (user_id)
    where is_default and status = 0;

-- 账本表注释
comment on table account_book is '账本';
comment on column account_book.id is '账本 ID';
comment on column account_book.owner_id is '账本所有者用户 ID';
comment on column account_book.book_name is '账本名称';
comment on column account_book.book_type is '账本类型(0个人账本 1共享账本)';
comment on column account_book.icon is '账本图标标识';
comment on column account_book.color is '账本主题色';
comment on column account_book.description is '账本说明';
comment on column account_book.status is '账本状态(0正常 1已归档)';
comment on column account_book.create_time is '创建时间';
comment on column account_book.update_time is '更新时间';

-- 账本成员表注释
comment on table account_book_member is '账本成员';
comment on column account_book_member.id is '账本成员 ID';
comment on column account_book_member.book_id is '账本 ID';
comment on column account_book_member.user_id is '用户 ID';
comment on column account_book_member.member_role is '成员角色(0所有者 1管理员 2普通成员)';
comment on column account_book_member.is_default is '是否为用户默认账本';
comment on column account_book_member.status is '成员状态(0正常 1已移除)';
comment on column account_book_member.join_time is '加入账本时间';
comment on column account_book_member.create_time is '创建时间';
comment on column account_book_member.update_time is '更新时间';
