import { useQuery } from '@tanstack/react-query';
import { Button, Descriptions, Modal, Spin } from 'antd';
import React from 'react';
import { getUserAccountBookBillVoById } from '@/services/mora-account-book/accountBookBillController';
import { formatYuan } from '@/utils/format';

export default function BillDetailModal({
  billId,
  onClose,
}: {
  billId: number;
  onClose: () => void;
}) {
  const query = useQuery({
    queryKey: ['account-book-bill-detail', billId],
    queryFn: async () => {
      const response = await getUserAccountBookBillVoById({ id: billId });
      if (response.code !== 0 || !response.data)
        throw new Error(response.message || '获取流水详情失败');
      return response.data;
    },
  });
  const detail = query.data;
  return (
    <Modal
      open
      title="流水详情"
      footer={null}
      onCancel={onClose}
      destroyOnHidden
    >
      <Spin spinning={query.isFetching}>
        <div style={{ minHeight: 120 }}>
          {query.isError && (
            <div role="alert">
              {query.error.message}
              <Button type="link" onClick={() => void query.refetch()}>
                重试
              </Button>
            </div>
          )}
          {detail && (
            <Descriptions
              column={1}
              items={[
                {
                  key: 'book',
                  label: '账本',
                  children: detail.bookName ?? '-',
                },
                {
                  key: 'type',
                  label: '收支类型',
                  children: detail.billType === 'INCOME' ? '收入' : '支出',
                },
                {
                  key: 'category',
                  label: '分类',
                  children: detail.categoryName ?? '-',
                },
                {
                  key: 'amount',
                  label: '金额',
                  children: formatYuan(detail.amount ?? 0),
                },
                {
                  key: 'account',
                  label: '账户',
                  children: detail.accountName ?? '未关联账户',
                },
                {
                  key: 'time',
                  label: '发生时间',
                  children: detail.billTime ?? '-',
                },
                {
                  key: 'remark',
                  label: '备注',
                  children: detail.remark || '-',
                },
                {
                  key: 'created',
                  label: '创建时间',
                  children: detail.createTime ?? '-',
                },
                {
                  key: 'updated',
                  label: '更新时间',
                  children: detail.updateTime ?? '-',
                },
              ]}
            />
          )}
        </div>
      </Spin>
    </Modal>
  );
}
