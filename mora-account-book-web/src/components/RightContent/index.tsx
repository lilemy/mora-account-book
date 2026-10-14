import { LogoutOutlined } from '@ant-design/icons';
import { history, useModel } from '@umijs/max';
import type { MenuProps } from 'antd';
import { Select, Spin } from 'antd';
import React, { startTransition, useEffect, useState } from 'react';
import { getAccountBookPage } from '@/services/mora-account-book/accountBookController';
import { userLogout } from '@/services/mora-account-book/userController';
import HeaderDropdown from '../HeaderDropdown';

type GlobalHeaderRightProps = {
  children?: React.ReactNode;
};

const menuItems: MenuProps['items'] = [
  {
    type: 'divider' as const,
  },
  {
    key: 'logout',
    icon: <LogoutOutlined />,
    label: '退出登录',
  },
];

/**
 * 退出登录，并且将当前的 url 保存
 */
const loginOut = async () => {
  await userLogout();

  const { search, pathname } = window.location;
  const urlParams = new URL(window.location.href).searchParams;
  const searchParams = new URLSearchParams({
    redirect: pathname + search,
  });
  const redirect = urlParams.get('redirect');
  if (window.location.pathname !== '/user/login' && !redirect) {
    history.replace({
      pathname: '/user/login',
      search: searchParams.toString(),
    });
  }
};

const RightContent: React.FC<GlobalHeaderRightProps> = ({ children }) => {
  const { initialState, setInitialState } = useModel('@@initialState');

  const [books, setBooks] = useState<API.AccountBookUserVO[]>([]);
  const [bookId, setBookId] = useState<number>();

  useEffect(() => {
    if (!initialState?.currentUser) return;
    getAccountBookPage({ pageNum: '1', pageSize: '100' }).then((r) => {
      const list = r.data?.records || [];
      setBooks(list);
      const saved = localStorage.getItem('selectedBookId') as any;
      const id = list.find((b) => String(b.id) === saved)?.id ?? list[0]?.id;
      if (id) {
        setBookId(id);
        localStorage.setItem('selectedBookId', String(id));
        window.dispatchEvent(new Event('account-book-change'));
      }
    });
  }, [initialState?.currentUser]);

  const onMenuClick: MenuProps['onClick'] = (event) => {
    const { key } = event;
    if (key === 'logout') {
      startTransition(() => {
        setInitialState((s) => ({ ...s, currentUser: undefined }));
      });
      loginOut();
      return;
    }
    history.push(`/account/${key}`);
  };

  if (!initialState) {
    return <Spin size="small" />;
  }

  const { currentUser } = initialState;

  if (!currentUser) {
    return <Spin size="small" />;
  }

  return (
    <>
      <Select
        size="small"
        value={bookId}
        placeholder="选择账本"
        style={{ width: 150, marginRight: 12, maxHeight: 36 }}
        options={books.map((b) => ({ label: b.bookName, value: b.id }))}
        onChange={(id) => {
          setBookId(id);
          localStorage.setItem('selectedBookId', String(id));
          window.dispatchEvent(new Event('account-book-change'));
        }}
      />
      <HeaderDropdown
        placement="bottomRight"
        menu={{
          selectedKeys: [],
          onClick: onMenuClick,
          items: menuItems,
        }}
        arrow
      >
        {children}
      </HeaderDropdown>
    </>
  );
};

export default RightContent;
