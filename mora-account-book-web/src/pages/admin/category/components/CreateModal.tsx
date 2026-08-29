import {
  ModalForm,
  ProFormSwitch,
  ProFormText,
} from '@ant-design/pro-components';
import { useMutation } from '@tanstack/react-query';
import { Form, message } from 'antd';
import React, { cloneElement, useCallback, useState } from 'react';
import { createCategoryTemplate } from '@/services/mora-account-book/categoryTemplateController';

type CreateModalProps = {
  trigger?: React.ReactElement<any>;
  onOk?: () => void;
};

type CreateFormValues = {
  templateCode: string;
  templateName: string;
  isDefault?: boolean;
};

const CreateModal: React.FC<CreateModalProps> = ({ trigger, onOk }) => {
  const [form] = Form.useForm<CreateFormValues>();
  const [open, setOpen] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();

  const { mutateAsync: saveCategoryTemplate, isPending: saving } = useMutation({
    mutationFn: createCategoryTemplate,
  });

  const onCancel = useCallback(() => {
    setOpen(false);
    form.resetFields();
  }, [form]);

  const onOpen = useCallback(() => {
    form.resetFields();
    setOpen(true);
  }, [form]);

  const onFinish = useCallback(
    async (formValues: CreateFormValues) => {
      try {
        const response = await saveCategoryTemplate({
          templateCode: formValues.templateCode,
          templateName: formValues.templateName,
          isDefault: formValues.isDefault ? 'true' : 'false',
        });
        if (response.code !== 0 || response.data !== true) {
          void messageApi.error(
            `模板创建失败${response.message ? `：${response.message}` : ''}`,
          );
          return false;
        }

        void messageApi.success('模板创建成功');
        onOk?.();
        onCancel();
        return true;
      } catch (error) {
        const errorMessage =
          error instanceof Error ? error.message : String(error);
        void messageApi.error(`模板创建失败：${errorMessage}`);
        return false;
      }
    },
    [messageApi, onCancel, onOk, saveCategoryTemplate],
  );

  const triggerElement = trigger
    ? cloneElement(trigger, {
        onClick: (event: React.MouseEvent<HTMLElement>) => {
          trigger.props.onClick?.(event);
          onOpen();
        },
      })
    : null;

  return (
    <>
      {contextHolder}
      {triggerElement}
      <ModalForm<CreateFormValues>
        form={form}
        open={open}
        title="新增分类模板"
        width={560}
        initialValues={{ isDefault: false }}
        onFinish={onFinish}
        onOpenChange={(nextOpen) => {
          if (!nextOpen) onCancel();
        }}
        modalProps={{
          destroyOnHidden: true,
          onCancel,
          confirmLoading: saving,
        }}
      >
        <ProFormText
          name="templateName"
          label="模板名称"
          rules={[{ required: true, message: '请输入模板名称' }]}
        />
        <ProFormText
          name="templateCode"
          label="模板编码"
          rules={[{ required: true, message: '请输入模板编码' }]}
        />
        <ProFormSwitch name="isDefault" label="默认模板" />
      </ModalForm>
    </>
  );
};

export default CreateModal;
