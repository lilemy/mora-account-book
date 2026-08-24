import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { LoginForm, ProFormText } from '@ant-design/pro-components';
import { Helmet, history } from '@umijs/max';
import { App, Button } from 'antd';
import { createStyles } from 'antd-style';
import React from 'react';
import { Footer } from '@/components';
import { userRegister } from '@/services/mora-account-book/sysUserController';
import Settings from '../../../../config/defaultSettings';

const useStyles = createStyles(() => ({
  container: {
    display: 'flex',
    flexDirection: 'column',
    height: '100vh',
    overflow: 'auto',
    backgroundImage:
      "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
    backgroundSize: '100% 100%',
  },
}));

const Register: React.FC = () => {
  const { styles } = useStyles();
  const { message } = App.useApp();

  const handleSubmit = async (values: API.UserRegisterRequest) => {
    try {
      const res = await userRegister(values);
      if (res.code === 0) {
        await message.success('注册成功，请登录！');
        history.push('/user/login');
      }
    } catch (error: any) {
      message.error(`注册失败，${error.message}`).then();
    }
  };

  return (
    <div className={styles.container}>
      <Helmet>
        <title>
          注册
          {Settings.title && ` - ${Settings.title}`}
        </title>
      </Helmet>
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
        <LoginForm<API.UserRegisterRequest>
          contentStyle={{
            minWidth: 280,
            maxWidth: '75vw',
          }}
          logo={<img alt="logo" src="/logo.svg" />}
          title="摩拉记账本"
          subTitle="创建账号，开始管理你的摩拉"
          submitter={{
            searchConfig: {
              submitText: '注册',
            },
          }}
          onFinish={handleSubmit}
        >
          <ProFormText
            name="userPhone"
            fieldProps={{
              size: 'large',
              prefix: <UserOutlined />,
              maxLength: 11,
            }}
            placeholder="请输入手机号"
            rules={[
              {
                required: true,
                message: '手机号是必填项！',
              },
              {
                pattern: /^1[3-9]\d{9}$/,
                message: '请输入正确的手机号！',
              },
            ]}
          />
          <ProFormText.Password
            name="userPassword"
            fieldProps={{
              size: 'large',
              prefix: <LockOutlined />,
              maxLength: 20,
            }}
            placeholder="请输入密码"
            rules={[
              {
                required: true,
                message: '密码是必填项！',
              },
              {
                min: 8,
                max: 20,
                message: '密码长度应为 8–20 位！',
              },
            ]}
          />
          <ProFormText.Password
            name="checkPassword"
            dependencies={['userPassword']}
            fieldProps={{
              size: 'large',
              prefix: <LockOutlined />,
              maxLength: 20,
            }}
            placeholder="请再次输入密码"
            rules={[
              {
                required: true,
                message: '请再次输入密码！',
              },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue('userPassword') === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(new Error('两次输入的密码不一致！'));
                },
              }),
            ]}
          />
          <div
            style={{
              marginBottom: 24,
              textAlign: 'right',
            }}
          >
            <span>已有账号？</span>
            <Button
              type="link"
              style={{ padding: 0 }}
              onClick={() => history.push('/user/login')}
            >
              返回登录
            </Button>
          </div>
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};

export default Register;
