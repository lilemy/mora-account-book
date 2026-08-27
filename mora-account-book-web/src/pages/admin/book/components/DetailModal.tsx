import type { ProDescriptionsProps } from '@ant-design/pro-components';
import { ProDescriptions } from '@ant-design/pro-components';
import type { TableProps } from 'antd';
import { App, Modal, Space, Table } from 'antd';
import dayjs from 'dayjs';
import { useEffect, useRef, useState } from 'react';
import { getAccountBookById } from '@/services/mora-account-book/accountBookController';
import { getAccountBookIconConfig } from '@/utils/accountBookIcon';

type AccountBookDetailModalProps = {
  accountBookId?: number;
  open: boolean;
  onClose: () => void;
};

const { renderIcon } = getAccountBookIconConfig();

const accountBookColumns: ProDescriptionsProps<API.AccountBookManageVO>['columns'] =
  [
    {
      title: '账本名称',
      dataIndex: 'bookName',
    },
    {
      title: '账本所有者',
      dataIndex: 'ownerName',
    },
    {
      title: '账本类型',
      dataIndex: 'bookType',
      valueEnum: {
        PERSONAL: '个人账本',
        SHARE: '共享账本',
      },
    },
    {
      title: '所有者手机号',
      dataIndex: 'ownerPhone',
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
      title: '账本图标',
      dataIndex: 'icon',
      render: (_, record) => (
        <Space size="small">
          {renderIcon(record.icon, { style: { color: record.color } })}
          <span>{record.icon || '-'}</span>
        </Space>
      ),
    },
    {
      title: '主题色',
      dataIndex: 'color',
      valueType: 'color',
    },
    {
      title: '账本说明',
      dataIndex: 'description',
      span: 2,
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

const roleText: Record<string, string> = {
  OWNER: '所有者',
  ADMIN: '管理员',
  MEMBER: '普通成员',
};

const statusText: Record<string, string> = {
  NORMAL: '正常',
  REMOVED: '已移除',
};

const formatDateTime = (value?: string) =>
  value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '-';

const memberColumns: TableProps<API.AccountBookManageMemberVO>['columns'] = [
  {
    title: '用户名称',
    dataIndex: 'userName',
    render: (value?: string) => value || '-',
  },
  {
    title: '手机号',
    dataIndex: 'userPhone',
    render: (value?: string) => value || '-',
  },
  {
    title: '成员角色',
    dataIndex: 'memberRole',
    render: (value?: string) => (value ? roleText[value] || value : '-'),
  },
  {
    title: '默认账本',
    dataIndex: 'isDefault',
    render: (value?: boolean) => (value ? '是' : '否'),
  },
  {
    title: '成员状态',
    dataIndex: 'status',
    render: (value?: string) => (value ? statusText[value] || value : '-'),
  },
  {
    title: '加入时间',
    dataIndex: 'joinTime',
    render: formatDateTime,
  },
];

const DetailModal = ({
  accountBookId,
  open,
  onClose,
}: AccountBookDetailModalProps) => {
  const { message } = App.useApp();
  const [detail, setDetail] = useState<API.AccountBookManageVO>();
  const [loading, setLoading] = useState(false);
  const fetchRef = useRef(0);

  useEffect(() => {
    if (!open || accountBookId == null) {
      return;
    }

    fetchRef.current += 1;
    const fetchId = fetchRef.current;
    setDetail(undefined);
    setLoading(true);

    getAccountBookById({ id: accountBookId })
      .then(({ code, data, message: responseMessage }) => {
        if (fetchId !== fetchRef.current) {
          return;
        }

        if (code !== 0 || !data) {
          void message.error('获取账本详情失败' + responseMessage);
          return;
        }

        setDetail(data);
      })
      .catch((error: any) => {
        if (fetchId !== fetchRef.current) {
          return;
        }

        setDetail(undefined);
        void message.error('获取账本详情失败' + error.message);
      })
      .finally(() => {
        if (fetchId === fetchRef.current) {
          setLoading(false);
        }
      });

    return () => {
      fetchRef.current += 1;
    };
  }, [accountBookId, message, open]);

  return (
    <Modal
      open={open}
      title={detail?.bookName ? `账本详情 - ${detail.bookName}` : '账本详情'}
      width={900}
      footer={null}
      loading={loading}
      destroyOnHidden
      onCancel={onClose}
    >
      {detail ? (
        <div style={{ display: 'grid', gap: 24, minWidth: 0 }}>
          <section style={{ minWidth: 0 }}>
            <h3 style={{ fontSize: 16, margin: '0 0 16px' }}>账本信息</h3>
            <ProDescriptions<API.AccountBookManageVO>
              bordered
              column={2}
              size="small"
              dataSource={detail}
              columns={accountBookColumns}
            />
          </section>

          <section style={{ minWidth: 0 }}>
            <h3 style={{ fontSize: 16, margin: '0 0 16px' }}>
              账本成员（{detail.memberList?.length ?? 0}）
            </h3>
            <Table<API.AccountBookManageMemberVO>
              size="small"
              rowKey={(record) =>
                String(record.id ?? `${record.bookId}-${record.userId}`)
              }
              columns={memberColumns}
              dataSource={detail.memberList ?? []}
              pagination={{
                pageSize: 10,
                hideOnSinglePage: true,
                showSizeChanger: false,
              }}
            />
          </section>
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
