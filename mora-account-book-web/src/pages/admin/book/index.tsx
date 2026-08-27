import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';

import { Button, message, Popconfirm, Space } from 'antd';
import React, { useRef, useState } from 'react';
import { useUserSelectProps } from '@/components';
import {
  deleteAccountBook,
  getAccountBookManageVoPage,
} from '@/services/mora-account-book/accountBookController';
import { getAccountBookIconConfig } from '@/utils/accountBookIcon';
import DetailModal from './components/DetailModal';
import UpdateForm from './components/UpdateForm';

const { renderIcon } = getAccountBookIconConfig();

const AccountBookTableList: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const accountBookOwnerSelectProps = useUserSelectProps();

  const [showDetail, setShowDetail] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.AccountBookManageVO>();

  const columns: ProColumns<API.AccountBookManageVO>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
      hideInTable: true,
      hideInForm: true,
      search: false,
    },
    {
      title: '账本名称',
      dataIndex: 'bookName',
      render: (dom, entity) => {
        return (
          <a
            type="link"
            onClick={() => {
              setCurrentRow(entity);
              setShowDetail(true);
            }}
          >
            <Space>
              {renderIcon(entity.icon)}
              <div>{dom}</div>
            </Space>
          </a>
        );
      },
    },
    {
      title: '账本所有者',
      dataIndex: 'ownerId',
      valueType: 'select',
      renderText: (_, entity) => {
        return `${entity.ownerName} - ${entity.ownerPhone}`;
      },
      fieldProps: accountBookOwnerSelectProps,
    },
    {
      title: '账本类型',
      dataIndex: 'bookType',
      valueEnum: {
        PERSONAL: {
          text: '个人账本',
          status: 'Processing',
        },
        SHARE: {
          text: '共享账本',
          status: 'Success',
        },
      },
    },
    {
      title: '账本状态',
      dataIndex: 'status',
      valueEnum: {
        NORMAL: {
          text: '正常',
          status: 'Success',
        },
        ARCHIVED: {
          text: '已归档',
          status: 'Default',
        },
      },
    },
    {
      title: '账本主题色',
      dataIndex: 'color',
      valueType: 'color',
      search: false,
    },
    {
      title: '账本说明',
      dataIndex: 'description',
      valueType: 'textarea',
      hideInTable: true,
      search: false,
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
            title="该操作不可能，确定删除这个账本吗?"
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
  const handleRemove = async (selectedRow: API.AccountBookManageVO) => {
    const hide = message.loading('正在删除');
    if (!selectedRow || !selectedRow.id) return;
    try {
      await deleteAccountBook({ id: selectedRow.id });
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
      <ProTable<API.AccountBookManageVO, API.PageResultAccountBookManageVO>
        headerTitle={'账本管理'}
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        pagination={{
          pageSize: 10,
        }}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;
          const { data, code } = await getAccountBookManageVoPage({
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.getAccountBookManageVOPageParams);
          return {
            success: code === 0,
            data: data?.records || [],
            total: Number(data?.totalRow) || 0,
          };
        }}
        columns={columns}
      />
      <DetailModal
        open={showDetail}
        accountBookId={currentRow?.id}
        onClose={() => {
          setShowDetail(false);
          setCurrentRow(undefined);
        }}
      />
    </PageContainer>
  );
};

export default AccountBookTableList;
