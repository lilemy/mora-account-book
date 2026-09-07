import {
  AlipayCircleOutlined,
  BankOutlined,
  CarOutlined,
  CoffeeOutlined,
  CreditCardOutlined,
  DollarOutlined,
  EditOutlined,
  FundOutlined,
  GiftOutlined,
  LoginOutlined,
  LogoutOutlined,
  MoneyCollectOutlined,
  PayCircleOutlined,
  QuestionCircleOutlined,
  ShoppingCartOutlined,
  StockOutlined,
  WalletOutlined,
  WechatOutlined,
} from '@ant-design/icons';
import type { ComponentProps, ComponentType, ReactNode } from 'react';

/** 后端 UserAccountTypeEnum 对应的账户类型。 */
export type UserAccountType =
  | 'CAPITAL'
  | 'CREDIT'
  | 'RECHARGE'
  | 'INVEST'
  | 'LOAN';

export type UserAccountIconProps = ComponentProps<typeof WalletOutlined>;
export type UserAccountIconComponent = ComponentType<UserAccountIconProps>;

export type UserAccountIconItem = {
  /** 保存到 user_account.icon 的唯一标识。 */
  value: string;
  /** 展示名称。 */
  label: string;
  /** 图标组件。 */
  component: UserAccountIconComponent;
  /** 账户类型；父级和子级都提供，便于筛选。 */
  accountType: UserAccountType;
  /** 用于在图标选择器中保持一致的主题色。 */
  color: string;
  /** 子图标所属的父级标识；父级本身没有该字段。 */
  parentValue?: UserAccountType;
};

export type UserAccountIconGroup = UserAccountIconItem & {
  children: UserAccountIconItem[];
};

export type UserAccountIconOption = {
  value: string;
  label: string;
  color?: string;
  icon: ReactNode;
  children?: UserAccountIconOption[];
};

const customIcon = EditOutlined;

/**
 * 用户账户图标的父子目录。
 *
 * 父级 value 使用后端账户类型枚举，子级 value 使用命名空间前缀，
 * 因此既能直接用于账户类型筛选，也能安全地将子图标标识持久化到后端。
 */
export const userAccountIconGroups: readonly UserAccountIconGroup[] = [
  {
    value: 'CAPITAL',
    label: '资金账户',
    component: BankOutlined,
    accountType: 'CAPITAL',
    color: '#3b82f6',
    children: [
      {
        value: 'capital-bank-card',
        label: '储蓄卡',
        component: BankOutlined,
        accountType: 'CAPITAL',
        parentValue: 'CAPITAL',
        color: '#3b82f6',
      },
      {
        value: 'capital-wechat',
        label: '微信',
        component: WechatOutlined,
        accountType: 'CAPITAL',
        parentValue: 'CAPITAL',
        color: '#10b981',
      },
      {
        value: 'capital-alipay',
        label: '支付宝',
        component: AlipayCircleOutlined,
        accountType: 'CAPITAL',
        parentValue: 'CAPITAL',
        color: '#1677ff',
      },
      {
        value: 'capital-cash',
        label: '现金',
        component: DollarOutlined,
        accountType: 'CAPITAL',
        parentValue: 'CAPITAL',
        color: '#f59e0b',
      },
      {
        value: 'capital-custom',
        label: '自定义',
        component: customIcon,
        accountType: 'CAPITAL',
        parentValue: 'CAPITAL',
        color: '#94a3b8',
      },
    ],
  },
  {
    value: 'CREDIT',
    label: '信用账户',
    component: CreditCardOutlined,
    accountType: 'CREDIT',
    color: '#ef4444',
    children: [
      {
        value: 'credit-card',
        label: '信用卡',
        component: CreditCardOutlined,
        accountType: 'CREDIT',
        parentValue: 'CREDIT',
        color: '#ef4444',
      },
      {
        value: 'credit-huabei',
        label: '花呗',
        component: PayCircleOutlined,
        accountType: 'CREDIT',
        parentValue: 'CREDIT',
        color: '#2563eb',
      },
      {
        value: 'credit-jiebei',
        label: '借呗',
        component: WalletOutlined,
        accountType: 'CREDIT',
        parentValue: 'CREDIT',
        color: '#2563eb',
      },
      {
        value: 'credit-jd-baitiao',
        label: '京东白条',
        component: ShoppingCartOutlined,
        accountType: 'CREDIT',
        parentValue: 'CREDIT',
        color: '#ef4444',
      },
      {
        value: 'credit-custom',
        label: '自定义',
        component: customIcon,
        accountType: 'CREDIT',
        parentValue: 'CREDIT',
        color: '#94a3b8',
      },
    ],
  },
  {
    value: 'RECHARGE',
    label: '充值账户',
    component: WalletOutlined,
    accountType: 'RECHARGE',
    color: '#f59e0b',
    children: [
      {
        value: 'recharge-meal-card',
        label: '饭卡',
        component: CoffeeOutlined,
        accountType: 'RECHARGE',
        parentValue: 'RECHARGE',
        color: '#f59e0b',
      },
      {
        value: 'recharge-transit-card',
        label: '公交卡',
        component: CarOutlined,
        accountType: 'RECHARGE',
        parentValue: 'RECHARGE',
        color: '#3b82f6',
      },
      {
        value: 'recharge-membership-card',
        label: '会员卡',
        component: GiftOutlined,
        accountType: 'RECHARGE',
        parentValue: 'RECHARGE',
        color: '#fbbf24',
      },
      {
        value: 'recharge-custom',
        label: '自定义',
        component: customIcon,
        accountType: 'RECHARGE',
        parentValue: 'RECHARGE',
        color: '#94a3b8',
      },
    ],
  },
  {
    value: 'INVEST',
    label: '理财账户',
    component: StockOutlined,
    accountType: 'INVEST',
    color: '#10b981',
    children: [
      {
        value: 'invest-stock',
        label: '股票',
        component: StockOutlined,
        accountType: 'INVEST',
        parentValue: 'INVEST',
        color: '#3b82f6',
      },
      {
        value: 'invest-fund',
        label: '基金',
        component: FundOutlined,
        accountType: 'INVEST',
        parentValue: 'INVEST',
        color: '#3b82f6',
      },
      {
        value: 'invest-yuebao',
        label: '余额宝',
        component: MoneyCollectOutlined,
        accountType: 'INVEST',
        parentValue: 'INVEST',
        color: '#ef4444',
      },
      {
        value: 'invest-lingqiantong',
        label: '零钱通',
        component: WalletOutlined,
        accountType: 'INVEST',
        parentValue: 'INVEST',
        color: '#10b981',
      },
      {
        value: 'invest-fixed-deposit',
        label: '定期存款',
        component: BankOutlined,
        accountType: 'INVEST',
        parentValue: 'INVEST',
        color: '#14b8a6',
      },
      {
        value: 'invest-custom',
        label: '自定义',
        component: customIcon,
        accountType: 'INVEST',
        parentValue: 'INVEST',
        color: '#94a3b8',
      },
    ],
  },
  {
    value: 'LOAN',
    label: '借贷账户',
    component: MoneyCollectOutlined,
    accountType: 'LOAN',
    color: '#8b5cf6',
    children: [
      {
        value: 'loan-lend',
        label: '借出',
        component: LogoutOutlined,
        accountType: 'LOAN',
        parentValue: 'LOAN',
        color: '#10b981',
      },
      {
        value: 'loan-borrow',
        label: '借入',
        component: LoginOutlined,
        accountType: 'LOAN',
        parentValue: 'LOAN',
        color: '#ef4444',
      },
      {
        value: 'loan-custom',
        label: '自定义',
        component: customIcon,
        accountType: 'LOAN',
        parentValue: 'LOAN',
        color: '#94a3b8',
      },
    ],
  },
] as const;

/** `userAccountIcons` 作为更短的别名，方便在组件中直接导入目录数据。 */
export const userAccountIcons = userAccountIconGroups;

const flattenIcons = (): UserAccountIconItem[] =>
  userAccountIconGroups.flatMap(({ children }) => children);

/** 返回账户图标目录及常用的渲染、筛选辅助方法。 */
export const getUserAccountIconConfig = () => {
  const icons = flattenIcons();
  const allIcons = userAccountIconGroups.flatMap((group) => [
    group,
    ...group.children,
  ]);
  const iconMap = new Map<string, UserAccountIconItem>(
    allIcons.map((item) => [item.value, item]),
  );

  const renderIcon = (value?: string, props?: UserAccountIconProps) => {
    const IconComponent = value ? iconMap.get(value)?.component : undefined;

    return IconComponent ? (
      <IconComponent {...props} />
    ) : (
      <QuestionCircleOutlined {...props} />
    );
  };

  const iconOptions = icons.map(
    ({ value, label, component: IconComponent, color }) => ({
      value,
      label,
      color,
      icon: <IconComponent />,
    }),
  );

  const accountTypeOptions = userAccountIconGroups.map(
    ({ value, label, component: IconComponent, color }) => ({
      value,
      label,
      color,
      icon: <IconComponent />,
    }),
  );

  const options: UserAccountIconOption[] = userAccountIconGroups.map(
    ({ value, label, component: IconComponent, children, color }) => ({
      value,
      label,
      color,
      icon: <IconComponent />,
      children: children.map(
        ({
          value: childValue,
          label: childLabel,
          component: ChildIcon,
          color: childColor,
        }) => ({
          value: childValue,
          label: childLabel,
          color: childColor,
          icon: <ChildIcon />,
        }),
      ),
    }),
  );

  return {
    /** 分组后的父子目录，适合直接渲染分组选择器。 */
    groups: userAccountIconGroups,
    /** 扁平子图标列表，适合表单 Select 或图标网格。 */
    icons,
    /** 嵌套选项，兼容 Ant Design Cascader/TreeSelect。 */
    options,
    /** 扁平选项，保留图标选择器常用的 value/label/icon 结构。 */
    iconOptions,
    /** 仅包含父级账户类型，适合用于列表筛选。 */
    accountTypeOptions,
    getIcon: (value?: string) => (value ? iconMap.get(value) : undefined),
    getGroup: (accountType?: UserAccountType) =>
      accountType
        ? userAccountIconGroups.find((group) => group.value === accountType)
        : undefined,
    getIconsByAccountType: (accountType?: UserAccountType) =>
      accountType
        ? icons.filter((icon) => icon.accountType === accountType)
        : icons,
    getAccountType: (value?: string) =>
      value ? iconMap.get(value)?.accountType : undefined,
    renderIcon,
  };
};
