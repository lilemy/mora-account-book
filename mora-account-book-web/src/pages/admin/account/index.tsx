import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';

import { Button, Space } from 'antd';
import React, { useRef, useState } from 'react';
import { useUserSelectProps } from '@/components';
import { getUserAccountManageVoPage } from '@/services/mora-account-book/userAccountController';
import { getUserAccountIconConfig } from '@/utils/userAccountIcon';
import DeleteModal from './components/DeleteModal';
import DetailModal from './components/DetailModal';
import UpdateForm from './components/UpdateForm';

const { renderIcon } = getUserAccountIconConfig();

const UserAccountTableList: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const userSelectProps = useUserSelectProps();

  const [showDetail, setShowDetail] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.UserAccountManageVO>();

  const columns: ProColumns<API.UserAccountManageVO>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
      hideInTable: true,
      hideInForm: true,
      search: false,
    },
    {
      title: '账户名称',
      dataIndex: 'accountName',
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
      title: '账户所有者',
      dataIndex: 'userId',
      valueType: 'select',
      renderText: (_, entity) => {
        return `${entity.userName} - ${entity.userPhone}`;
      },
      fieldProps: userSelectProps,
    },
    {
      title: '账户类型',
      dataIndex: 'accountType',
      valueEnum: {
        CAPITAL: {
          text: '资金账户',
        },
        CREDIT: {
          text: '信用账户',
        },
        RECHARGE: {
          text: '充值账户',
        },
        INVEST: {
          text: '理财账户',
        },
        LOAN: {
          text: '借贷账户',
        },
      },
    },
    {
      title: '银行名称',
      dataIndex: 'bankName',
      valueType: 'text',
      search: false,
    },
    {
      title: '默认账户',
      dataIndex: 'isDefault',
      valueEnum: {
        true: {
          text: '是',
          status: 'Success',
        },
        false: {
          text: '否',
          status: 'Default',
        },
      },
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
            onOk={() => actionRef.current?.reload()}
            values={record}
          />
          <DeleteModal
            values={record}
            onOk={() => actionRef.current?.reload()}
          />
        </Space>
      ),
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.UserAccountManageVO, API.PageResultUserAccountManageVO>
        headerTitle={'账户管理'}
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
          const { data, code } = await getUserAccountManageVoPage({
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.getUserAccountManageVOPageParams);
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
        userAccountId={currentRow?.id}
        onClose={() => {
          setShowDetail(false);
          setCurrentRow(undefined);
        }}
      />
    </PageContainer>
  );
};

export default UserAccountTableList;
