import { useQuery } from '@tanstack/react-query';
import {
  Badge,
  type BadgeProps,
  Calendar,
  type CalendarProps,
  Spin,
} from 'antd';
import dayjs, { type Dayjs } from 'dayjs';
import React, { useState } from 'react';
import {
  getUserAccountBookBillTotalByMonth,
  getUserAccountBookBillVoListByMonth,
} from '@/services/mora-account-book/accountBookBillController';
import { formatYuan } from '@/utils/format';

export default function BillCalendar({ bookId }: { bookId: string }) {
  const [date, setDate] = useState(() => dayjs());
  const [mode, setMode] = useState<'month' | 'year'>('month');
  const month = date.format('YYYY-MM');
  const year = date.format('YYYY');

  const monthQuery = useQuery({
    queryKey: ['account-book-calendar-month', bookId, month],
    enabled: !!bookId && mode === 'month',
    queryFn: async () => {
      const response = await getUserAccountBookBillVoListByMonth({
        bookId,
        calendarDate: `${month}-01`,
      });
      if (response.code !== 0)
        throw new Error(response.message || '获取月度流水失败');
      return response.data ?? [];
    },
  });

  const yearQuery = useQuery({
    queryKey: ['account-book-calendar-year', bookId, year],
    enabled: !!bookId && mode === 'year',
    queryFn: async () => {
      const response = await getUserAccountBookBillTotalByMonth({
        bookId,
        calendarDate: `${year}-01-01`,
      });
      if (response.code !== 0)
        throw new Error(response.message || '获取年度汇总失败');
      return response.data ?? [];
    },
  });

  const activeQuery = mode === 'month' ? monthQuery : yearQuery;

  const onPanelChange = (value: Dayjs, mode: CalendarProps<Dayjs>['mode']) => {
    setDate(value);
    setMode(mode ?? 'month');
  };

  const monthCellRender = (value: Dayjs) => {
    const total = yearQuery.data?.find(
      (item) => item.month === value.month() + 1,
    );
    return bookId && total ? (
      <div>
        <div>收入：{formatYuan(total.totalIncome ?? 0)}</div>
        <div>支出：{formatYuan(total.totalExpense ?? 0)}</div>
        <div>
          结余：
          {formatYuan((total.totalIncome ?? 0) - (total.totalExpense ?? 0))}
        </div>
      </div>
    ) : null;
  };

  const dateCellRender = (value: Dayjs) => {
    if (!bookId || value.format('YYYY-MM') !== month) return null;
    const daily = monthQuery.data?.find(
      (item) => item.date === value.format('YYYY-MM-DD'),
    );
    const listData = (daily?.bills ?? []).map((bill, index) => ({
      key: bill.id ?? index,
      type: bill.billType === 'INCOME' ? 'success' : 'error',
      content:
        (bill.categoryName ?? '未分类') +
        ' ' +
        (bill.billType === 'INCOME' ? '+' : '-') +
        formatYuan(bill.amount ?? 0) +
        (bill.remark ? ` · ${bill.remark}` : ''),
    }));
    return (
      <ul>
        {listData.map((item) => (
          <li key={item.key}>
            <Badge
              status={item.type as BadgeProps['status']}
              text={item.content}
            />
          </li>
        ))}
      </ul>
    );
  };

  const cellRender: CalendarProps<Dayjs>['cellRender'] = (current, info) => {
    if (info.type === 'date') {
      return dateCellRender(current);
    }
    if (info.type === 'month') {
      return monthCellRender(current);
    }
    return info.originNode;
  };

  return (
    <Spin spinning={activeQuery.isFetching}>
      {activeQuery.isError && (
        <div role="alert">{activeQuery.error.message}</div>
      )}
      <Calendar
        value={date}
        mode={mode}
        onChange={setDate}
        cellRender={cellRender}
        onPanelChange={onPanelChange}
      />
    </Spin>
  );
}
