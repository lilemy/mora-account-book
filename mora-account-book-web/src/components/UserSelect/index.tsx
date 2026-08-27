import type { SelectProps } from 'antd';
import { Avatar, Select, Spin } from 'antd';
import { useCallback, useEffect, useRef, useState } from 'react';
import { getSysUserManageVoPage } from '@/services/mora-account-book/sysUserController';

export type UserSelectOption = {
  label: string;
  value: string;
  avatar?: string;
  phone?: string;
};

type ManagedSelectProp =
  | 'autoClearSearchValue'
  | 'filterOption'
  | 'loading'
  | 'notFoundContent'
  | 'onOpenChange'
  | 'onSearch'
  | 'optionRender'
  | 'options'
  | 'showSearch';

export type UserSelectProps = Omit<
  SelectProps<string, UserSelectOption>,
  ManagedSelectProp
> & {
  debounceTimeout?: number;
};

const fetchUserOptions = async (
  search: string,
): Promise<UserSelectOption[]> => {
  const keyword = search.trim();
  const isPhone = /^\d+$/.test(keyword);
  const { code, data } = await getSysUserManageVoPage({
    pageNum: '1',
    pageSize: '20',
    userName: keyword && !isPhone ? keyword : undefined,
    userPhone: keyword && isPhone ? keyword : undefined,
  });

  if (code !== 0) {
    return [];
  }

  return (data?.records ?? []).flatMap((user) => {
    if (user.id == null) {
      return [];
    }

    return [
      {
        label: user.userName || `用户 ${user.id}`,
        value: String(user.id),
        avatar: user.userAvatar,
        phone: user.userPhone,
      },
    ];
  });
};

export const useUserSelectProps = (
  debounceTimeout = 300,
): SelectProps<string, UserSelectOption> => {
  const [fetching, setFetching] = useState(false);
  const [options, setOptions] = useState<UserSelectOption[]>([]);
  const fetchRef = useRef(0);
  const timerRef = useRef<ReturnType<typeof setTimeout>>(undefined);

  const loadOptions = useCallback(
    (search: string) => {
      fetchRef.current += 1;
      const fetchId = fetchRef.current;

      clearTimeout(timerRef.current);
      setOptions([]);
      setFetching(true);

      timerRef.current = setTimeout(async () => {
        try {
          const newOptions = await fetchUserOptions(search);
          if (fetchId === fetchRef.current) {
            setOptions(newOptions);
          }
        } catch {
          if (fetchId === fetchRef.current) {
            setOptions([]);
          }
        } finally {
          if (fetchId === fetchRef.current) {
            setFetching(false);
          }
        }
      }, debounceTimeout);
    },
    [debounceTimeout],
  );

  useEffect(
    () => () => {
      clearTimeout(timerRef.current);
      fetchRef.current += 1;
    },
    [],
  );

  return {
    allowClear: true,
    placeholder: '请输入用户昵称或手机号',
    options,
    loading: fetching,
    showSearch: true,
    autoClearSearchValue: false,
    filterOption: false,
    onSearch: loadOptions,
    notFoundContent: fetching ? <Spin size="small" /> : '未找到用户',
    onOpenChange: (open) => {
      if (open && options.length === 0 && !fetching) {
        loadOptions('');
      }
    },
    optionRender: (option) => (
      <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
        <Avatar size={24} src={option.data.avatar}>
          {option.data.label.slice(0, 1)}
        </Avatar>
        <div>
          <div>{option.data.label}</div>
          {option.data.phone && (
            <div style={{ color: 'rgba(0, 0, 0, 0.45)', fontSize: 12 }}>
              {option.data.phone}
            </div>
          )}
        </div>
      </div>
    ),
  };
};

const UserSelect = ({ debounceTimeout = 300, ...props }: UserSelectProps) => {
  const selectProps = useUserSelectProps(debounceTimeout);

  return <Select<string, UserSelectOption> {...selectProps} {...props} />;
};

export default UserSelect;
