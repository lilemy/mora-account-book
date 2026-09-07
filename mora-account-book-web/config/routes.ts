export default [
  {
    path: '/user',
    layout: false,
    routes: [
      {
        name: '登录',
        path: '/user/login',
        component: './user/login',
      },
      {
        name: '注册',
        path: '/user/register',
        component: './user/register',
      },
    ],
  },
  {
    path: '/welcome',
    name: '欢迎',
    icon: 'smile',
    component: './Welcome',
  },
  {
    path: '/admin',
    name: '管理页',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      {
        path: '/admin',
        redirect: '/admin/user',
      },
      {
        path: '/admin/user',
        name: '用户管理',
        icon: 'user',
        component: './admin/user',
      },
      {
        path: '/admin/book',
        name: '账本管理',
        icon: 'wallet',
        component: './admin/book',
      },
      {
        path: '/admin/category-template',
        name: '分类模板管理',
        icon: 'book',
        component: './admin/category',
      },
      {
        path: '/admin/category-template/update/:id',
        name: '更新分类模板',
        component: './admin/category/update',
        hideInMenu: true,
      },
      {
        path: '/admin/account',
        name: '账户管理',
        icon: 'accountBook',
        component: './admin/account',
      },
    ],
  },
  {
    path: '/',
    redirect: '/welcome',
  },
  {
    component: './exception/404',
    layout: false,
    path: './*',
  },
];
