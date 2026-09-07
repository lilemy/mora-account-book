import {
  ProDescriptions,
  type ProDescriptionsProps,
} from '@ant-design/pro-components';
import { App, Divider, Modal, Space } from 'antd';
import React, { useEffect, useRef, useState } from 'react';
import { getUserAccountManageVoById } from '@/services/mora-account-book/userAccountController';
import { getUserAccountIconConfig } from '@/utils/userAccountIcon';

type UserAccountDetailModalProps = {
  userAccountId?: number;
  open: boolean;
  onClose: () => void;
};

const { renderIcon } = getUserAccountIconConfig();

const DetailModal = ({
  userAccountId,
  open,
  onClose,
}: UserAccountDetailModalProps) => {
  const { message } = App.useApp();
  const [detail, setDetail] = useState<API.UserAccountManageVO>();
  const [accountType, setAccountType] = useState<
    'CAPITAL' | 'CREDIT' | 'RECHARGE' | 'INVEST' | 'LOAN'
  >();
  const [loading, setLoading] = useState(false);
  const fetchRef = useRef(0);

  useEffect(() => {
    if (!open || userAccountId == null) {
      return;
    }

    fetchRef.current += 1;
    const fetchId = fetchRef.current;
    setDetail(undefined);
    setLoading(true);
    setAccountType(undefined);

    getUserAccountManageVoById({ id: userAccountId })
      .then(({ code, data, message: responseMessage }) => {
        if (fetchId !== fetchRef.current) {
          return;
        }

        if (code !== 0 || !data) {
          void message.error('获取账户详情失败' + responseMessage);
          return;
        }

        setDetail(data);
        setAccountType(data.accountType);
      })
      .catch((error: any) => {
        if (fetchId !== fetchRef.current) {
          return;
        }

        setDetail(undefined);
        void message.error('获取账户详情失败' + error.message);
      })
      .finally(() => {
        if (fetchId === fetchRef.current) {
          setLoading(false);
        }
      });

    return () => {
      fetchRef.current += 1;
    };
  }, [userAccountId, message, open]);

  /**
   * 基础信息
   */
  const userAccountColumns: ProDescriptionsProps<API.UserAccountManageVO>['columns'] =
    [
      {
        title: '账户名称',
        dataIndex: 'accountName',
      },
      {
        title: '所属用户',
        dataIndex: 'userId',
        renderText: (_, record) => (
          <div>
            {record.userName}-{record.userPhone}
          </div>
        ),
      },
      {
        title: '银行名称',
        dataIndex: 'bankName',
        renderText: (_, record) => (
          <div>
            {record.bankName || '-'}
            {record.accountNoTail && `(${record.accountNoTail})`}
          </div>
        ),
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
        title: '账本图标',
        dataIndex: 'icon',
        render: (_, record) => (
          <Space size="small">
            {renderIcon(record.icon)}
            <span>{record.icon || '-'}</span>
          </Space>
        ),
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
        title: '备注',
        dataIndex: 'remark',
        valueType: 'textarea',
        span: 3,
      },
      {
        title: '创建时间',
        dataIndex: 'createTime',
        valueType: 'dateTime',
      },
      {
        title: '更新时间',
        dataIndex: 'updateTime',
        valueType: 'dateTime',
      },
    ];

  /**
   * 信用账户信息
   */
  const creditColumns: ProDescriptionsProps<API.UserAccountManageVO>['columns'] =
    [
      {
        title: '信用账户出账日',
        dataIndex: 'creditStatementDay',
        renderText: (_, record) => (
          <div>每月 {record.creditStatementDay || '-'} 日</div>
        ),
      },
      {
        title: '信用账户还款日',
        dataIndex: 'creditRepaymentDay',
        renderText: (_, record) => (
          <div>每月 {record.creditRepaymentDay || '-'} 日</div>
        ),
      },
    ];

  /**
   * 借贷账户信息
   */
  const loanColumns: ProDescriptionsProps<API.UserAccountManageVO>['columns'] =
    [
      {
        title: '借贷人姓名',
        dataIndex: 'loanUser',
      },
      {
        title: '借贷方向',
        dataIndex: 'loanDirection',
        valueEnum: {
          LEND: {
            text: '借出',
          },
          BORROW: {
            text: '借入',
          },
        },
      },
      {
        title: '借贷发生日期',
        dataIndex: 'loanDate',
        valueType: 'date',
      },
      {
        title: '计划还款日期',
        dataIndex: 'repaymentDate',
        valueType: 'date',
      },
    ];

  return (
    <Modal
      open={open}
      title={
        detail?.accountName ? `账户详情 - ${detail.accountName}` : '账户详情'
      }
      width={900}
      footer={null}
      loading={loading}
      destroyOnHidden
      onCancel={onClose}
    >
      {detail ? (
        <div>
          <Divider>基础信息</Divider>
          <ProDescriptions<API.UserAccountManageVO>
            bordered
            column={3}
            dataSource={detail}
            columns={userAccountColumns}
          ></ProDescriptions>
          {accountType && accountType === 'CREDIT' && (
            <div>
              <Divider>信用账户信息</Divider>
              <ProDescriptions<API.UserAccountManageVO>
                bordered
                column={2}
                dataSource={detail}
                columns={creditColumns}
              ></ProDescriptions>
            </div>
          )}
          {accountType && accountType === 'LOAN' && (
            <div>
              <Divider>借贷账户信息</Divider>
              <ProDescriptions<API.UserAccountManageVO>
                bordered
                column={2}
                dataSource={detail}
                columns={loanColumns}
              ></ProDescriptions>
            </div>
          )}
        </div>
      ) : (
        !loading && (
          <div style={{ padding: 32, textAlign: 'center' }}>暂无详情数据</div>
        )
      )}
    </Modal>
  );
};

export default DetailModal;
