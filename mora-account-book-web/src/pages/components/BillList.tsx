import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import {
  Badge,
  Button,
  Card,
  Listy,
  message,
  Pagination,
  Popconfirm,
  Spin,
} from 'antd';
import dayjs from 'dayjs';
import React, { useState } from 'react';
import BillDetailModal from '@/pages/components/BillDetailModal';
import CreateBillModal from '@/pages/components/CreateBillModal';
import {
  deleteAccountBookBill,
  getUserAccountBookBillVoPage,
} from '@/services/mora-account-book/accountBookBillController';
import { formatYuan } from '@/utils/format';

function BillListColumn({
  bookId,
  billType,
}: {
  bookId: string;
  billType: 'EXPENSE' | 'INCOME';
}) {
  const title = billType === 'EXPENSE' ? '支出' : '收入';
  const [pageNum, setPageNum] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [detailId, setDetailId] = useState<number>();
  const [api, holder] = message.useMessage();
  const queryClient = useQueryClient();
  const deletion = useMutation({ mutationFn: deleteAccountBookBill });
  const query = useQuery({
    queryKey: ['account-book-bill-list', bookId, billType, pageNum, pageSize],
    queryFn: async () => {
      const response = await getUserAccountBookBillVoPage({
        bookId,
        billType,
        pageNum: String(pageNum),
        pageSize: String(pageSize),
        sortField: 'billTime,id',
        sortOrder: 'desc',
      });
      if (response.code !== 0)
        throw new Error(response.message || '获取流水列表失败');
      return response.data;
    },
  });
  const bills = query.data?.records ?? [];
  const deleteBill = async (id: number) => {
    try {
      const response = await deletion.mutateAsync({ id });
      if (response.code !== 0 || response.data !== true)
        throw new Error(response.message || '删除流水失败');
      void api.success('删除流水成功');
      if (bills.length === 1 && pageNum > 1) setPageNum(pageNum - 1);
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: ['account-book-bill-list'] }),
        queryClient.invalidateQueries({
          queryKey: ['account-book-calendar-month'],
        }),
        queryClient.invalidateQueries({
          queryKey: ['account-book-calendar-year'],
        }),
        queryClient.invalidateQueries({
          queryKey: ['account-book-bill-detail', id],
        }),
      ]);
    } catch (error) {
      void api.error(error instanceof Error ? error.message : '删除流水失败');
    }
  };

  return (
    <Spin spinning={query.isFetching}>
      <Card styles={{ body: { minHeight: 120 } }}>
        {holder}
        {detailId != null && (
          <BillDetailModal
            billId={detailId}
            onClose={() => setDetailId(undefined)}
          />
        )}
        {query.isError && (
          <div role="alert">
            {query.error.message}
            <Button type="link" onClick={() => void query.refetch()}>
              重试
            </Button>
          </div>
        )}
        <h3
          style={{
            margin: '0 0 8px',
            color:
              billType === 'EXPENSE'
                ? 'var(--ant-color-error)'
                : 'var(--ant-color-success)',
          }}
        >
          {title}
        </h3>
        {!query.isFetching && !query.isError && bills.length === 0 && (
          <div>当前暂无{title}流水</div>
        )}
        <Listy<API.AccountBookBillVO>
          items={bills}
          rowKey="id"
          styles={{
            item: {
              padding: '16px 0',
              borderBottom: '1px solid var(--ant-color-border-secondary)',
            },
          }}
          itemRender={(bill) => (
            <div
              style={{
                display: 'flex',
                justifyContent: 'space-between',
                gap: 16,
              }}
            >
              <div style={{ minWidth: 0 }}>
                <Badge
                  status={bill.billType === 'INCOME' ? 'success' : 'error'}
                  text={bill.categoryName ?? '未分类'}
                />
                <div style={{ marginTop: 4 }}>
                  {bill.billTime
                    ? dayjs(bill.billTime).format('YYYY-MM-DD HH:mm')
                    : '未记录时间'}
                  {bill.accountName ? ` · ${bill.accountName}` : ''}
                </div>
                {bill.remark && (
                  <div style={{ marginTop: 4, overflowWrap: 'anywhere' }}>
                    {bill.remark}
                  </div>
                )}
              </div>
              <div>
                <div
                  style={{
                    textAlign: 'right',
                    whiteSpace: 'nowrap',
                    fontWeight: 600,
                    color:
                      bill.billType === 'INCOME'
                        ? 'var(--ant-color-success)'
                        : 'var(--ant-color-error)',
                  }}
                >
                  {bill.billType === 'INCOME' ? '+' : '-'}
                  {formatYuan(bill.amount ?? 0)}
                </div>
                {bill.id != null && (
                  <div
                    style={{
                      display: 'flex',
                      flexWrap: 'wrap',
                      justifyContent: 'flex-end',
                    }}
                  >
                    <Button type="link" onClick={() => setDetailId(bill.id)}>
                      详情
                    </Button>
                    <CreateBillModal
                      billId={bill.id}
                      trigger={<Button type="link">编辑</Button>}
                    />
                    <Popconfirm
                      title="确定删除这笔流水？"
                      description="删除后将撤销流水对关联账户余额的影响。"
                      okText="删除"
                      cancelText="取消"
                      okButtonProps={{ danger: true }}
                      disabled={deletion.isPending}
                      onConfirm={() =>
                        bill.id == null ? undefined : deleteBill(bill.id)
                      }
                    >
                      <Button
                        type="link"
                        danger
                        disabled={deletion.isPending}
                        loading={
                          deletion.isPending &&
                          deletion.variables?.id === bill.id
                        }
                      >
                        删除
                      </Button>
                    </Popconfirm>
                  </div>
                )}
              </div>
            </div>
          )}
        />
        <Pagination
          style={{ marginTop: 16 }}
          current={pageNum}
          pageSize={pageSize}
          total={query.data?.totalRow ?? 0}
          showSizeChanger
          disabled={query.isFetching}
          showTotal={(total) => `共 ${total} 条流水`}
          onChange={(page, size) => {
            setPageNum(size === pageSize ? page : 1);
            setPageSize(size);
          }}
        />
      </Card>
    </Spin>
  );
}

export default function BillList({ bookId }: { bookId: string }) {
  return (
    <div
      style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(2, minmax(0, 1fr))',
        gap: 24,
      }}
    >
      {(['EXPENSE', 'INCOME'] as const).map((billType) => (
        <section key={bookId + billType} style={{ minWidth: 0 }}>
          <BillListColumn bookId={bookId} billType={billType} />
        </section>
      ))}
    </div>
  );
}
