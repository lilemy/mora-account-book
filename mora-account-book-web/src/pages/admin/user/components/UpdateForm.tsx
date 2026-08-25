import {
  ModalForm,
  ProForm,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useMutation } from '@tanstack/react-query';
import { Form, message, Spin } from 'antd';
import React, { cloneElement, useCallback, useState } from 'react';
import {
  getSysUserById,
  updateSysUser,
} from '@/services/mora-account-book/sysUserController';

type UpdateFormProps = {
  trigger?: React.ReactElement<any>;
  onOk?: () => void;
  values: Partial<API.SysUserManageVO>;
};

/**
 * 用户编辑弹窗
 */
const UpdateForm: React.FC<UpdateFormProps> = ({ onOk, values, trigger }) => {
  const [form] = Form.useForm<API.UserUpdateRequest>();
  const [open, setOpen] = useState(false);
  const [loadingDetail, setLoadingDetail] = useState(false);
  const [detail, setDetail] = useState<API.SysUserManageVO>();
  const [messageApi, contextHolder] = message.useMessage();

  const { mutateAsync: saveUser, isPending: saving } = useMutation({
    mutationFn: updateSysUser,
  });

  const onCancel = useCallback(() => {
    setOpen(false);
    setDetail(undefined);
    form.resetFields();
  }, [form]);

  const onOpen = useCallback(async () => {
    if (!values.id) {
      messageApi.error('无法获取用户 ID').then();
      return;
    }

    setOpen(true);
    setLoadingDetail(true);
    setDetail(undefined);
    form.resetFields();

    try {
      const response = await getSysUserById({ id: values.id });
      if (response.code !== 0 || !response.data) {
        messageApi.error('获取用户信息失败' + response.message).then();
      }

      setDetail(response.data);
      form.setFieldsValue(response.data ?? {});
    } catch (error: any) {
      messageApi.error('获取用户信息失败' + error.message).then();
      setOpen(false);
    } finally {
      setLoadingDetail(false);
    }
  }, [form, messageApi, values.id]);

  const onFinish = useCallback(
    async (formValues: API.UserUpdateRequest) => {
      const id = detail?.id ?? values.id;
      if (!id) {
        messageApi.error('无法获取用户 ID').then();
        return false;
      }

      try {
        const response = await saveUser({ ...formValues, id });
        if (response.code !== 0) {
          messageApi.error('用户信息更新失败' + response.message).then();
          return false;
        }

        messageApi.success('用户信息更新成功').then();
        onOk?.();
        onCancel();
        return true;
      } catch (error: any) {
        messageApi.error('用户信息更新失败' + error.message).then();
        return false;
      }
    },
    [detail?.id, messageApi, onCancel, onOk, saveUser, values.id],
  );

  const triggerElement = trigger
    ? cloneElement(trigger, {
        onClick: (event: React.MouseEvent<HTMLElement>) => {
          trigger.props.onClick?.(event);
          void onOpen();
        },
      })
    : null;

  return (
    <>
      {contextHolder}
      {triggerElement}
      <ModalForm<API.UserUpdateRequest>
        form={form}
        open={open}
        title="修改用户"
        width={640}
        initialValues={detail}
        onFinish={onFinish}
        onOpenChange={(nextOpen) => {
          if (!nextOpen) onCancel();
        }}
        submitter={loadingDetail ? false : undefined}
        modalProps={{
          destroyOnHidden: true,
          onCancel,
          confirmLoading: saving,
        }}
      >
        {loadingDetail || !detail ? (
          <Spin style={{ display: 'block', padding: '32px 0' }} />
        ) : (
          <>
            <ProForm.Group>
              <ProFormText
                name="userName"
                label="昵称"
                rules={[{ required: true, message: '请输入昵称' }]}
              />
              <ProFormText
                name="userPhone"
                label="手机号"
                rules={[{ required: true, message: '请输入手机号' }]}
              />
            </ProForm.Group>
            <ProFormText name="userAvatar" label="头像地址" />
            <ProFormTextArea name="userProfile" label="简介" />
            <ProForm.Group>
              <ProFormText
                name="userEmail"
                label="邮箱"
                rules={[{ type: 'email', message: '请输入有效的邮箱地址' }]}
              />
              <ProFormSelect
                name="userGender"
                label="性别"
                valueEnum={{
                  UNKNOWN: '未填写',
                  MALE: '男',
                  FEMALE: '女',
                }}
              />
              <ProFormSelect
                name="userRole"
                label="角色"
                valueEnum={{
                  USER: '用户',
                  ADMIN: '管理员',
                }}
              />
            </ProForm.Group>
            <ProFormTextArea name="remark" label="备注" />
          </>
        )}
      </ModalForm>
    </>
  );
};

export default UpdateForm;
