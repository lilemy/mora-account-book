import {
  type ActionType,
  type ProColumns,
  ProTable,
} from '@ant-design/pro-components';
import { Button, Space } from 'antd';
import React, { useRef, useState } from 'react';
import CreateModal from '@/pages/account/components/CreateModal';
import DetailModal from '@/pages/account/components/DetailModal';
import DeleteModal from '@/pages/admin/account/components/DeleteModal';
import UpdateForm from '@/pages/admin/account/components/UpdateForm';
import { getUserAccountVoPage } from '@/services/mora-account-book/userAccountController';
import { getUserAccountIconConfig } from '@/utils/userAccountIcon';

const { renderIcon } = getUserAccountIconConfig();

const UserAccountPage: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);

  const [showDetail, setShowDetail] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.UserAccountVO>();

  const columns: ProColumns<API.UserAccountVO>[] = [
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
              {entity.accountNoTail}
            </Space>
          </a>
        );
      },
    },
    {
      title: '当前余额',
      dataIndex: 'currentBalance',
      valueType: 'text',
      hideInForm: true,
      search: false,
      render: (dom, entity) => {
        return (
          <div>
            {entity.accountType === 'CREDIT' ||
            (entity.accountType === 'LOAN' &&
              entity.loanDirection === 'BORROW') ? (
              <div>-{dom}</div>
            ) : (
              <div>{dom}</div>
            )}
          </div>
        );
      },
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
    <div className="max-width-content">
      <ProTable<API.UserAccountVO, API.PageResultUserAccountVO>
        headerTitle={'账户管理'}
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        pagination={{
          pageSize: 10,
        }}
        toolBarRender={() => [
          <CreateModal
            key="create"
            trigger={<Button type="primary">新增账户</Button>}
            onOk={() => actionRef.current?.reload()}
          />,
        ]}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;
          const { data, code } = await getUserAccountVoPage({
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.getUserAccountVOPageParams);
          return {
            success: code === 0,
            data: data?.records || [],
            total: Number(data?.totalRow) || 0,
          };
        }}
        columns={columns}
      />
      <DetailModal
        userAccountId={currentRow?.id}
        open={showDetail}
        onClose={() => {
          setShowDetail(false);
          setCurrentRow(undefined);
        }}
      />
    </div>
  );
};

export default UserAccountPage;
