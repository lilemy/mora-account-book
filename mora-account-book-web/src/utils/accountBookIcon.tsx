import {
  AccountBookOutlined,
  BankOutlined,
  CarOutlined,
  CoffeeOutlined,
  ContainerOutlined,
  CreditCardOutlined,
  FundOutlined,
  GiftOutlined,
  HomeOutlined,
  InsuranceOutlined,
  MedicineBoxOutlined,
  MobileOutlined,
  PayCircleOutlined,
  PieChartOutlined,
  QuestionCircleOutlined,
  ReadOutlined,
  ShopOutlined,
  ShoppingCartOutlined,
  TeamOutlined,
  WalletOutlined,
} from '@ant-design/icons';
import type { ComponentProps } from 'react';

type AccountBookIconProps = ComponentProps<typeof WalletOutlined>;

export const getAccountBookIconConfig = () => {
  const icons = [
    {
      value: 'account-book',
      label: '综合账本',
      component: AccountBookOutlined,
    },
    { value: 'wallet', label: '钱包', component: WalletOutlined },
    { value: 'pay-circle', label: '日常收支', component: PayCircleOutlined },
    { value: 'bank', label: '银行账户', component: BankOutlined },
    { value: 'credit-card', label: '信用卡', component: CreditCardOutlined },
    {
      value: 'shopping-cart',
      label: '购物消费',
      component: ShoppingCartOutlined,
    },
    { value: 'home', label: '家庭账本', component: HomeOutlined },
    { value: 'car', label: '交通出行', component: CarOutlined },
    { value: 'coffee', label: '餐饮消费', component: CoffeeOutlined },
    {
      value: 'medicine-box',
      label: '医疗健康',
      component: MedicineBoxOutlined,
    },
    { value: 'read', label: '教育学习', component: ReadOutlined },
    { value: 'gift', label: '人情礼物', component: GiftOutlined },
    { value: 'team', label: '共享账本', component: TeamOutlined },
    { value: 'shop', label: '经营账本', component: ShopOutlined },
    { value: 'mobile', label: '数码通讯', component: MobileOutlined },
    { value: 'insurance', label: '保险保障', component: InsuranceOutlined },
    { value: 'fund', label: '投资理财', component: FundOutlined },
    { value: 'pie-chart', label: '财务分析', component: PieChartOutlined },
    { value: 'container', label: '财务分析', component: ContainerOutlined },
  ] as const;

  const iconMap = new Map<string, (typeof icons)[number]>(
    icons.map((item) => [item.value, item]),
  );

  const renderIcon = (value?: string, props?: AccountBookIconProps) => {
    const IconComponent = value ? iconMap.get(value)?.component : undefined;

    return IconComponent ? (
      <IconComponent {...props} />
    ) : (
      <QuestionCircleOutlined {...props} />
    );
  };

  const options = icons.map(({ value, label, component: IconComponent }) => ({
    value,
    label,
    icon: <IconComponent />,
  }));

  return {
    icons,
    options,
    getIcon: (value?: string) => (value ? iconMap.get(value) : undefined),
    renderIcon,
  };
};
