import type { ProLayoutProps } from '@ant-design/pro-components';

/**
 * @name
 */
const Settings: ProLayoutProps & {
  logo?: string;
} = {
  navTheme: 'light',
  colorPrimary: '#64bbcf',
  layout: 'top',
  contentWidth: 'Fixed',
  fixedHeader: true,
  fixSiderbar: true,
  colorWeak: false,
  title: '摩拉记账本',
  logo: 'https://pic.lilemy.cn/public/mora-logo.svg',
  iconfontUrl: '',
  stylish: {
    header: (token) => ({
      [`${token.antCls}-menu-horizontal > ${token.antCls}-menu-item-selected, ${token.antCls}-menu-horizontal > ${token.antCls}-menu-submenu-selected`]:
        {
          color: token.colorPrimary,
          '&::after': {
            borderBottomWidth: 2,
            borderBottomColor: token.colorPrimary,
          },
        },
    }),
  },
  token: {
    // 参见ts声明，demo 见文档，通过token 修改样式
    //https://procomponents.ant.design/components/layout#%E9%80%9A%E8%BF%87-token-%E4%BF%AE%E6%94%B9%E6%A0%B7%E5%BC%8F
  },
};

export default Settings;
