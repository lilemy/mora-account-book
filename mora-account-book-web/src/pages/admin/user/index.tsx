import type {
  ActionType,
  ProColumns,
  ProDescriptionsColumn,
} from '@ant-design/pro-components';
import {
  PageContainer,
  ProDescriptions,
  ProTable,
} from '@ant-design/pro-components';

import { Button, Drawer, message, Popconfirm, Space } from 'antd';
import React, { useRef, useState } from 'react';
import {
  deleteSysUser,
  getSysUserManageVoPage,
} from '@/services/mora-account-book/sysUserController';
import UpdateForm from './components/UpdateForm';

const UserTableList: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);

  const [showDetail, setShowDetail] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.SysUserManageVO>();

  const columns: ProColumns<API.SysUserManageVO>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
      hideInTable: true,
      hideInForm: true,
      search: false,
    },
    {
      title: '昵称',
      dataIndex: 'userName',
      render: (dom, entity) => {
        return (
          <Button
            type="link"
            onClick={() => {
              setCurrentRow(entity);
              setShowDetail(true);
            }}
          >
            {dom}
          </Button>
        );
      },
    },
    {
      title: '头像',
      dataIndex: 'userAvatar',
      valueType: 'image',
      search: false,
      fieldProps: {
        width: 36,
      },
    },
    {
      title: '简介',
      dataIndex: 'userProfile',
      valueType: 'textarea',
      hideInTable: true,
      search: false,
    },
    {
      title: '手机号',
      dataIndex: 'userPhone',
      valueType: 'text',
    },
    {
      title: '邮箱',
      dataIndex: 'userEmail',
      valueType: 'text',
    },
    {
      title: '性别',
      dataIndex: 'userGender',
      valueEnum: {
        UNKNOWN: {
          text: '未填写',
          status: 'Default',
        },
        MALE: {
          text: '男',
          status: 'Processing',
        },
        FEMALE: {
          text: '女',
          status: 'Success',
        },
      },
    },
    {
      title: '角色',
      dataIndex: 'userRole',
      valueEnum: {
        USER: {
          text: '用户',
          status: 'Default',
        },
        ADMIN: {
          text: '管理员',
          status: 'Processing',
        },
      },
    },
    {
      title: '备注',
      dataIndex: 'remark',
      valueType: 'textarea',
      hideInTable: true,
      search: false,
    },
    {
      title: '编辑时间',
      sorter: true,
      search: false,
      hideInForm: true,
      dataIndex: 'editTime',
      valueType: 'dateTime',
    },
    {
      title: '创建时间',
      sorter: true,
      search: false,
      hideInForm: true,
      dataIndex: 'createTime',
      valueType: 'dateTime',
    },
    {
      title: '更新时间',
      sorter: true,
      search: false,
      hideInForm: true,
      dataIndex: 'updateTime',
      valueType: 'dateTime',
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => (
        <Space size="small">
          <UpdateForm
            trigger={
              <Button type="link" className="p-0!">
                修改
              </Button>
            }
            key="config"
            onOk={actionRef.current?.reload}
            values={record}
          />
          <Popconfirm
            title="确定删除这个用户?"
            onConfirm={() => handleRemove(record)}
            okText="确定"
            cancelText="取消"
          >
            <Button type="link" danger className="p-0!">
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  /**
   *  Delete node
   * @zh-CN 删除节点
   *
   * @param selectedRow
   */
  const handleRemove = async (selectedRow: API.SysUserManageVO) => {
    const hide = message.loading('正在删除');
    if (!selectedRow || !selectedRow.id) return;
    try {
      await deleteSysUser({ id: selectedRow.id });
      hide();
      message.success('删除成功').then();
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      message.error('删除失败，' + error.message).then();
      return false;
    }
  };

  return (
    <PageContainer>
      <ProTable<API.SysUserManageVO, API.PageResultSysUserManageVO>
        headerTitle={'用户管理'}
        actionRef={actionRef}
        rowKey="key"
        search={{
          labelWidth: 120,
        }}
        pagination={{
          pageSize: 10,
        }}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;
          const { data, code } = await getSysUserManageVoPage({
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.getSysUserManageVOPageParams);
          return {
            success: code === 0,
            data: data?.records || [],
            total: Number(data?.totalRow) || 0,
          };
        }}
        columns={columns}
      />

      <Drawer
        size={600}
        open={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={false}
      >
        {currentRow?.userPhone && (
          <ProDescriptions<API.SysUserManageVO>
            column={2}
            title={currentRow?.userName}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.id,
            }}
            columns={columns as ProDescriptionsColumn<API.SysUserManageVO>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};

export default UserTableList;
