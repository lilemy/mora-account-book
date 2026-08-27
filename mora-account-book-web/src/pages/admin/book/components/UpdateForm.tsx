import {
  ModalForm,
  ProForm,
  ProFormColorPicker,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useMutation } from '@tanstack/react-query';
import { Form, message, Space, Spin } from 'antd';
import React, { cloneElement, useCallback, useState } from 'react';
import {
  getAccountBookById,
  updateAccountBook,
} from '@/services/mora-account-book/accountBookController';
import { getAccountBookIconConfig } from '@/utils/accountBookIcon';

type UpdateFormProps = {
  trigger?: React.ReactElement<any>;
  onOk?: () => void;
  values: Partial<API.AccountBookManageVO>;
};

type UpdateFormValues = Omit<API.AccountBookUpdateRequest, 'id'>;
type AccountBookIconOption = ReturnType<
  typeof getAccountBookIconConfig
>['options'][number];

const { options: iconOptions } = getAccountBookIconConfig();

const UpdateForm: React.FC<UpdateFormProps> = ({ onOk, trigger, values }) => {
  const [form] = Form.useForm<UpdateFormValues>();
  const [open, setOpen] = useState(false);
  const [loadingDetail, setLoadingDetail] = useState(false);
  const [detail, setDetail] = useState<API.AccountBookManageVO>();
  const [messageApi, contextHolder] = message.useMessage();

  const { mutateAsync: saveAccountBook, isPending: saving } = useMutation({
    mutationFn: updateAccountBook,
  });

  const onCancel = useCallback(() => {
    setOpen(false);
    setDetail(undefined);
    form.resetFields();
  }, [form]);

  const onOpen = useCallback(async () => {
    if (values.id == null) {
      void messageApi.error('无法获取账本 ID');
      return;
    }

    setOpen(true);
    setLoadingDetail(true);
    setDetail(undefined);
    form.resetFields();

    try {
      const response = await getAccountBookById({ id: values.id });
      if (response.code !== 0 || !response.data) {
        void messageApi.error('获取账本信息失败' + response.message);
        return;
      }

      setDetail(response.data);
      form.setFieldsValue({
        bookName: response.data.bookName,
        icon: response.data.icon,
        color: response.data.color,
        description: response.data.description,
      });
    } catch (error: any) {
      void messageApi.error('获取账本信息失败' + error.message);
      setOpen(false);
    } finally {
      setLoadingDetail(false);
    }
  }, [form, messageApi, values.id]);

  const onFinish = useCallback(
    async (formValues: UpdateFormValues) => {
      const id = detail?.id ?? values.id;
      if (id == null) {
        void messageApi.error('无法获取账本 ID');
        return false;
      }

      try {
        const response = await saveAccountBook({
          id,
          bookName: formValues.bookName,
          icon: formValues.icon,
          color: formValues.color,
          description: formValues.description,
        });
        if (response.code !== 0 || response.data !== true) {
          void messageApi.error('账本信息更新失败' + response.message);
          return false;
        }

        void messageApi.success('账本信息更新成功');
        onOk?.();
        onCancel();
        return true;
      } catch (error: any) {
        void messageApi.error('账本信息更新失败' + error.message);
        return false;
      }
    },
    [detail?.id, messageApi, onCancel, onOk, saveAccountBook, values.id],
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
      <ModalForm<UpdateFormValues>
        form={form}
        open={open}
        title="修改账本"
        width={560}
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
            <ProFormText
              name="bookName"
              label="账本名称"
              rules={[{ required: true, message: '请输入账本名称' }]}
            />
            <ProForm.Group>
              <ProFormSelect<string, AccountBookIconOption>
                name="icon"
                label="账本图标"
                width="md"
                options={iconOptions}
                rules={[{ required: true, message: '请选择账本图标' }]}
                fieldProps={{
                  showSearch: { optionFilterProp: 'label' },
                  optionRender: (option) => (
                    <Space size="small">
                      {option.data.icon}
                      <span>{option.label}</span>
                    </Space>
                  ),
                }}
              />
              <ProFormColorPicker
                name="color"
                label="账本主题色"
                getValueFromEvent={(_color: unknown, cssColor: string) =>
                  cssColor
                }
              />
            </ProForm.Group>
            <ProFormTextArea name="description" label="账本说明" />
          </>
        )}
      </ModalForm>
    </>
  );
};

export default UpdateForm;
