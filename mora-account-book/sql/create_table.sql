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

-- 分类模板表
drop table if exists category_template;
create table if not exists category_template
(
    id            bigint primary key,
    template_code varchar(32)                         not null,
    template_name varchar(64)                         not null,
    is_default    boolean   default false             not null,
    status        smallint  default 0                 not null,
    create_time   timestamp default CURRENT_TIMESTAMP not null,
    update_time   timestamp default CURRENT_TIMESTAMP not null,
    constraint uk_category_template_code unique (template_code),
    constraint uk_category_template_name unique (template_name),
    constraint ck_category_template_status check (status in (0, 1))
);

create unique index uk_category_template_default_active
    on category_template (is_default)
    where is_default and status = 0;

-- 分类模板项表
drop table if exists category_template_item;
create table if not exists category_template_item
(
    id             bigint primary key,
    template_id    bigint                                not null,
    parent_id      bigint      default 0                 not null,
    category_name  varchar(64)                           not null,
    category_type  smallint                              not null,
    category_level smallint                              not null,
    icon           varchar(64) default 'container'       not null,
    color          varchar(16) default '#64bbcf'         not null,
    sort_order     integer     default 0                 not null,
    status         smallint    default 0                 not null,
    create_time    timestamp   default CURRENT_TIMESTAMP not null,
    update_time    timestamp   default CURRENT_TIMESTAMP not null,
    constraint ck_category_template_item_level check (
        (category_level = 1 and parent_id = 0) or
        (category_level = 2 and parent_id > 0)
        ),
    constraint ck_category_template_item_status check (status in (0, 1))
);

create index idx_category_template_item_tree
    on category_template_item (template_id, category_type, status, parent_id, sort_order);
create unique index uk_category_template_item_root_name
    on category_template_item (template_id, category_type, category_name)
    where category_level = 1 and status = 0;
create unique index uk_category_template_item_child_name
    on category_template_item (template_id, parent_id, category_name)
    where category_level = 2 and status = 0;

-- 账本分类表
drop table if exists account_book_category;
create table if not exists account_book_category
(
    id             bigint primary key,
    book_id        bigint                                not null,
    parent_id      bigint      default 0                 not null,
    category_name  varchar(64)                           not null,
    category_type  smallint                              not null,
    category_level smallint                              not null,
    icon           varchar(64) default 'container'       not null,
    color          varchar(16) default '#64bbcf'         not null,
    sort_order     integer     default 0                 not null,
    create_time    timestamp   default CURRENT_TIMESTAMP not null,
    update_time    timestamp   default CURRENT_TIMESTAMP not null,
    constraint ck_account_book_category_level check (
        (category_level = 1 and parent_id = 0) or
        (category_level = 2 and parent_id > 0)
        )
);

create index idx_account_book_category_tree
    on account_book_category (book_id, category_type, parent_id, sort_order);

-- 分类模板表注释
comment on table category_template is '分类模板';
comment on column category_template.id is '分类模板 ID';
comment on column category_template.template_code is '模板编码';
comment on column category_template.template_name is '模板名称';
comment on column category_template.is_default is '是否为默认模板';
comment on column category_template.status is '模板状态(0启用 1停用)';
comment on column category_template.create_time is '创建时间';
comment on column category_template.update_time is '更新时间';

-- 分类模板项表注释
comment on table category_template_item is '分类模板项';
comment on column category_template_item.id is '分类模板项 ID';
comment on column category_template_item.template_id is '分类模板 ID';
comment on column category_template_item.parent_id is '父分类模板项 ID，一级分类为 0';
comment on column category_template_item.category_name is '分类名称';
comment on column category_template_item.category_type is '分类类型(1收入 2支出 3转账)';
comment on column category_template_item.category_level is '分类层级(1一级 2二级)';
comment on column category_template_item.icon is '分类图标标识';
comment on column category_template_item.color is '分类颜色';
comment on column category_template_item.sort_order is '排序值，值越小越靠前';
comment on column category_template_item.status is '模板项状态(0启用 1停用)';
comment on column category_template_item.create_time is '创建时间';
comment on column category_template_item.update_time is '更新时间';

-- 账本分类表注释
comment on table account_book_category is '账本分类';
comment on column account_book_category.id is '账本分类 ID';
comment on column account_book_category.book_id is '账本 ID';
comment on column account_book_category.parent_id is '父分类 ID，一级分类为 0';
comment on column account_book_category.category_name is '分类名称';
comment on column account_book_category.category_type is '分类类型(1收入 2支出 3转账)';
comment on column account_book_category.category_level is '分类层级(1一级 2二级)';
comment on column account_book_category.icon is '分类图标标识';
comment on column account_book_category.color is '分类颜色';
comment on column account_book_category.sort_order is '排序值，值越小越靠前';
comment on column account_book_category.create_time is '创建时间';
comment on column account_book_category.update_time is '更新时间';
