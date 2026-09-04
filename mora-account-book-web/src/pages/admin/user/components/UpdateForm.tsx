import { LoadingOutlined, PlusOutlined } from '@ant-design/icons';
import {
  ModalForm,
  ProForm,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useMutation } from '@tanstack/react-query';
import type { GetProp, UploadProps } from 'antd';
import { Form, message, Spin, Upload } from 'antd';
import React, { cloneElement, useCallback, useState } from 'react';
import {
  getUserById,
  updateUser,
  uploadAvatarByAdmin,
} from '@/services/mora-account-book/userController';

type FileType = Parameters<GetProp<UploadProps, 'beforeUpload'>>[0];

const getBase64 = (file: FileType) =>
  new Promise<string>((resolve, reject) => {
    const reader = new FileReader();
    reader.addEventListener('load', () => resolve(reader.result as string));
    reader.addEventListener('error', () => reject(reader.error));
    reader.readAsDataURL(file);
  });

type UpdateFormProps = {
  trigger?: React.ReactElement<any>;
  onOk?: () => void;
  values: Partial<API.UserManageVO>;
};

/**
 * 用户编辑弹窗
 */
const UpdateForm: React.FC<UpdateFormProps> = ({ onOk, values, trigger }) => {
  const [form] = Form.useForm<API.UserUpdateRequest>();
  const [open, setOpen] = useState(false);
  const [loadingDetail, setLoadingDetail] = useState(false);
  const [detail, setDetail] = useState<API.UserManageVO>();
  const [avatarUrl, setAvatarUrl] = useState<string>();
  const [avatarUploading, setAvatarUploading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();

  const { mutateAsync: saveUser, isPending: saving } = useMutation({
    mutationFn: updateUser,
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
    setAvatarUrl(undefined);
    form.resetFields();

    try {
      const response = await getUserById({ id: values.id });
      if (response.code !== 0 || !response.data) {
        messageApi.error('获取用户信息失败' + response.message).then();
      }

      setDetail(response.data);
      setAvatarUrl(response.data?.userAvatar);
      form.setFieldsValue(response.data ?? {});
    } catch (error: any) {
      messageApi.error('获取用户信息失败' + error.message).then();
      setOpen(false);
    } finally {
      setLoadingDetail(false);
    }
  }, [form, messageApi, values.id]);

  const beforeAvatarUpload: UploadProps['beforeUpload'] = (file) => {
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng) {
      messageApi.error('头像只能上传 JPG 或 PNG 格式').then();
    }
    const isLt5M = file.size / 1024 / 1024 < 5;
    if (!isLt5M) {
      messageApi.error('头像大小不能超过 5MB').then();
    }
    return isJpgOrPng && isLt5M;
  };

  const customAvatarRequest: UploadProps['customRequest'] = async ({
    file,
    onError,
    onSuccess,
  }) => {
    const id = detail?.id ?? values.id;
    if (!id || typeof file === 'string') {
      const error = new Error('无法获取用户 ID 或头像文件');
      onError?.(error);
      return;
    }

    setAvatarUploading(true);
    try {
      const response = await uploadAvatarByAdmin({
        id,
        file: file as Blob,
      });
      if (response.code !== 0 || response.data !== true) {
        messageApi.error('头像上传失败' + response.message).then();
      }

      setAvatarUrl(await getBase64(file as FileType));
      onSuccess?.(response);
      messageApi.success('头像上传成功').then();
      onOk?.();
    } catch (error: any) {
      onError?.(error instanceof Error ? error : new Error(error?.message));
      messageApi
        .error(`头像上传失败${error?.message ? `：${error.message}` : ''}`)
        .then();
    } finally {
      setAvatarUploading(false);
    }
  };

  const onFinish = useCallback(
    async (formValues: API.UserUpdateRequest) => {
      const id = detail?.id ?? values.id;
      if (!id) {
        messageApi.error('无法获取用户 ID').then();
        return false;
      }

      try {
        const userValues = { ...formValues };
        delete userValues.userAvatar;
        const response = await saveUser({ ...userValues, id });
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
            <Form.Item label="头像">
              <Upload
                name="file"
                listType="picture-card"
                showUploadList={false}
                accept="image/jpeg,image/png"
                maxCount={1}
                beforeUpload={beforeAvatarUpload}
                customRequest={customAvatarRequest}
                disabled={avatarUploading}
              >
                {avatarUrl ? (
                  <img
                    draggable={false}
                    src={avatarUrl}
                    alt="avatar"
                    style={{ width: '100%' }}
                  />
                ) : (
                  <button
                    style={{ border: 0, background: 'none' }}
                    type="button"
                  >
                    {avatarUploading ? <LoadingOutlined /> : <PlusOutlined />}
                    <div style={{ marginTop: 8 }}>上传头像</div>
                  </button>
                )}
              </Upload>
            </Form.Item>
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
