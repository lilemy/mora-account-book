import { Button, Card, Segmented } from 'antd';
import React, { useEffect, useState } from 'react';
import BillCalendar from '@/pages/components/BillCalendar';
import BillList from '@/pages/components/BillList';
import CreateBillModal from '@/pages/components/CreateBillModal';

const Welcome: React.FC = () => {
  const [bookId, setBookId] = useState(() =>
    localStorage.getItem('selectedBookId'),
  );
  const [view, setView] = useState<'calendar' | 'list'>('calendar');

  useEffect(() => {
    const syncBook = () => setBookId(localStorage.getItem('selectedBookId'));
    window.addEventListener('account-book-change', syncBook);
    window.addEventListener('storage', syncBook);
    syncBook();
    return () => {
      window.removeEventListener('account-book-change', syncBook);
      window.removeEventListener('storage', syncBook);
    };
  }, []);

  return (
    <div className="max-width-content">
      <Card
        title="欢迎使用摩拉记账本"
        extra={
          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: 12,
              flexWrap: 'wrap',
            }}
          >
            <Segmented<'calendar' | 'list'>
              value={view}
              onChange={setView}
              options={[
                { label: '日历', value: 'calendar' },
                { label: '列表', value: 'list' },
              ]}
            />
            <CreateBillModal trigger={<Button type="primary">记一笔</Button>} />
          </div>
        }
      >
        {!bookId ? (
          <div>请先选择账本</div>
        ) : view === 'calendar' ? (
          <BillCalendar bookId={bookId} />
        ) : (
          <BillList key={bookId} bookId={bookId} />
        )}
      </Card>
    </div>
  );
};

export default Welcome;
