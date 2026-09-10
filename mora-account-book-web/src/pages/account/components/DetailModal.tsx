import {
  ProDescriptions,
  type ProDescriptionsProps,
} from '@ant-design/pro-components';
import { App, Divider, Modal, Space } from 'antd';
import React, { useEffect, useState } from 'react';
import { getUserAccountVoById } from '@/services/mora-account-book/userAccountController';
import { getUserAccountIconConfig } from '@/utils/userAccountIcon';

type DetailModalProps = {
  userAccountId?: number;
  open: boolean;
  onClose: () => void;
};

const { renderIcon, getIcon, getGroup } = getUserAccountIconConfig();
const moneyFormatter = new Intl.NumberFormat('zh-CN', {
  minimumFractionDigits: 2,
  maximumFractionDigits: 2,
});
const columnsPerRow = { xs: 1, sm: 2, md: 2, lg: 2, xl: 2, xxl: 2 };

const DetailModal = ({ userAccountId, open, onClose }: DetailModalProps) => {
  const { message } = App.useApp();
  const [detail, setDetail] = useState<API.UserAccountVO>();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setDetail(undefined);
    if (!open || userAccountId == null) {
      setLoading(false);
      return;
    }

    let active = true;
    setLoading(true);
    getUserAccountVoById({ id: userAccountId })
      .then(({ code, data, message: responseMessage }) => {
        if (!active) return;
        if (code !== 0 || !data) {
          void message.error(
            `获取账户详情失败${responseMessage ? `：${responseMessage}` : ''}`,
          );
          return;
        }
        setDetail(data);
      })
      .catch((error: unknown) => {
        if (!active) return;
        const errorMessage =
          error instanceof Error ? error.message : String(error);
        void message.error(`获取账户详情失败：${errorMessage}`);
      })
      .finally(() => {
        if (active) setLoading(false);
      });

    return () => {
      active = false;
    };
  }, [userAccountId, open, message]);

  const accountType = detail?.accountType;
  const baseColumns: ProDescriptionsProps<API.UserAccountVO>['columns'] = [
    { title: '账户名称', dataIndex: 'accountName' },
    {
      title: '账户类型',
      dataIndex: 'accountType',
      renderText: (_, record) => getGroup(record.accountType)?.label ?? '-',
    },
    ...(accountType !== 'LOAN'
      ? [
          { title: '银行名称', dataIndex: 'bankName' },
          { title: '卡号后四位', dataIndex: 'accountNoTail' },
        ]
      : []),
    {
      title: '账户图标',
      dataIndex: 'icon',
      render: (_, record) => (
        <Space size="small">
          {renderIcon(record.icon)}
          <span>{getIcon(record.icon)?.label ?? '-'}</span>
        </Space>
      ),
    },
    {
      title: '默认账户',
      dataIndex: 'isDefault',
      valueEnum: {
        true: { text: '是', status: 'Success' },
        false: { text: '否', status: 'Default' },
      },
    },
    { title: '创建时间', dataIndex: 'createTime', valueType: 'dateTime' },
    { title: '更新时间', dataIndex: 'updateTime', valueType: 'dateTime' },
    { title: '备注', dataIndex: 'remark', valueType: 'textarea', span: 2 },
  ];
  const creditColumns: ProDescriptionsProps<API.UserAccountVO>['columns'] = [
    {
      title: '出账日',
      dataIndex: 'creditStatementDay',
      renderText: (_, record) =>
        record.creditStatementDay
          ? `每月 ${record.creditStatementDay} 日`
          : '-',
    },
    {
      title: '还款日',
      dataIndex: 'creditRepaymentDay',
      renderText: (_, record) =>
        record.creditRepaymentDay
          ? `每月 ${record.creditRepaymentDay} 日`
          : '-',
    },
  ];
  const loanColumns: ProDescriptionsProps<API.UserAccountVO>['columns'] = [
    { title: '借贷人姓名', dataIndex: 'loanUser' },
    {
      title: '借贷方向',
      dataIndex: 'loanDirection',
      valueEnum: { LEND: { text: '借出' }, BORROW: { text: '借入' } },
    },
    { title: '发生日期', dataIndex: 'loanDate', valueType: 'date' },
    { title: '计划还款日期', dataIndex: 'repaymentDate', valueType: 'date' },
  ];
  const balanceLabel =
    accountType === 'CREDIT'
      ? '欠款余额'
      : accountType === 'LOAN'
        ? detail?.loanDirection === 'LEND'
          ? '待收金额'
          : '待还金额'
        : '当前余额';
  const amounts = [
    { label: balanceLabel, value: detail?.currentBalance },
    ...(accountType === 'CREDIT'
      ? [
          { label: '信用额度', value: detail?.creditLimit },
          {
            label: '可用额度',
            value:
              detail?.creditLimit != null && detail.currentBalance != null
                ? detail.creditLimit - detail.currentBalance
                : undefined,
          },
        ]
      : []),
  ];

  return (
    <Modal
      open={open}
      title="账户详情"
      width={900}
      footer={null}
      loading={loading}
      destroyOnHidden
      onCancel={onClose}
    >
      {detail ? (
        <div className="wrap-break-word">
          <dl className="m-0 grid grid-cols-1 gap-x-6 gap-y-4 py-4 sm:grid-cols-3">
            {amounts.map(({ label, value }) => (
              <div key={label} className="min-w-0">
                <dt className="mb-2">{label}（元）</dt>
                <dd className="m-0 wrap-break-word text-2xl font-semibold tabular-nums">
                  {value == null ? '-' : moneyFormatter.format(value)}
                </dd>
              </div>
            ))}
          </dl>
          <Divider>基础信息</Divider>
          <ProDescriptions<API.UserAccountVO>
            bordered
            column={columnsPerRow}
            dataSource={detail}
            columns={baseColumns}
          />
          {accountType === 'CREDIT' && (
            <>
              <Divider>信用账户信息</Divider>
              <ProDescriptions<API.UserAccountVO>
                bordered
                column={columnsPerRow}
                dataSource={detail}
                columns={creditColumns}
              />
            </>
          )}
          {accountType === 'LOAN' && (
            <>
              <Divider>借贷账户信息</Divider>
              <ProDescriptions<API.UserAccountVO>
                bordered
                column={columnsPerRow}
                dataSource={detail}
                columns={loanColumns}
              />
            </>
          )}
        </div>
      ) : (
        !loading && <div className="p-8 text-center">暂无详情数据</div>
      )}
    </Modal>
  );
};

export default DetailModal;
