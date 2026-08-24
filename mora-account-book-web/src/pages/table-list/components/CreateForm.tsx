import { PlusOutlined } from '@ant-design/icons';
import {
  type ActionType,
  ModalForm,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useMutation, useQueryClient } from '@tanstack/react-query';

import { Button, message } from 'antd';
import type { FC } from 'react';
import { addRule } from '@/services/ant-design-pro/api';

interface CreateFormProps {
  reload?: ActionType['reload'];
}

const CreateForm: FC<CreateFormProps> = (props) => {
  const { reload } = props;

  const [messageApi, contextHolder] = message.useMessage();
  const queryClient = useQueryClient();

  const { mutateAsync: run, isPending: loading } = useMutation({
    mutationFn: addRule,
    onSuccess: () => {
      messageApi.success('Added successfully');
      queryClient.invalidateQueries({ queryKey: ['rule'] });
      reload?.();
    },
    onError: () => {
      messageApi.error('Adding failed, please try again!');
    },
  });

  return (
    <>
      {contextHolder}
      <ModalForm
        title={'新建规则'}
        trigger={
          <Button type="primary" icon={<PlusOutlined />}>
            新建
          </Button>
        }
        width="400px"
        modalProps={{ okButtonProps: { loading } }}
        onFinish={async (value) => {
          try {
            await run({ data: value as API.RuleListItem });
            return true;
          } catch {
            return false;
          }
        }}
      >
        <ProFormText
          rules={[
            {
              required: true,
              message: '规则名称为必填项',
            },
          ]}
          width="md"
          name="name"
        />
        <ProFormTextArea width="md" name="desc" />
      </ModalForm>
    </>
  );
};

export default CreateForm;
