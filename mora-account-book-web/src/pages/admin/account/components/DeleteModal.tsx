import { App, Button, Flex, Modal, Radio, Space, Typography } from 'antd';
import React, { useState } from 'react';
import { deleteUserAccount } from '@/services/mora-account-book/userAccountController';

type DeleteModalProps = {
  values: API.UserAccountManageVO;
  onOk?: () => void;
};

const DeleteModal: React.FC<DeleteModalProps> = ({ values, onOk }) => {
  const { message } = App.useApp();
  const [open, setOpen] = useState(false);
  const [isKeepBill, setIsKeepBill] = useState(true);
  const [step, setStep] = useState<'options' | 'confirm'>('options');
  const [loading, setLoading] = useState(false);

  const handleRemove = async () => {
    if (!values.id) {
      return;
    }

    setLoading(true);
    const hide = message.loading('正在删除');
    try {
      await deleteUserAccount({ id: values.id, isKeepBill });
      message.success('删除成功').then();
      setOpen(false);
      onOk?.();
    } catch (error: any) {
      message.error(`删除失败，${error.message}`).then();
    } finally {
      hide();
      setLoading(false);
    }
  };

  const handleNext = async () => {
    if (step === 'options') {
      setStep('confirm');
      return;
    }
    await handleRemove();
  };

  return (
    <>
      <Button type="link" danger className="p-0!" onClick={() => setOpen(true)}>
        删除
      </Button>
      <Modal
        open={open}
        title={step === 'options' ? '选择删除方式' : '确认删除账户'}
        width={520}
        centered
        destroyOnHidden
        closable={!loading}
        footer={
          <Flex gap="middle">
            <Button
              block
              size="large"
              disabled={loading}
              onClick={() => setOpen(false)}
            >
              取消
            </Button>
            <Button
              block
              size="large"
              type="primary"
              danger={step === 'confirm'}
              loading={loading}
              onClick={handleNext}
            >
              {step === 'options' ? '下一步' : '确认删除'}
            </Button>
          </Flex>
        }
        onCancel={() => setOpen(false)}
        afterClose={() => {
          setIsKeepBill(true);
          setStep('options');
        }}
      >
        {step === 'options' ? (
          <Radio.Group
            block
            value={isKeepBill}
            onChange={(event) => setIsKeepBill(event.target.value as boolean)}
          >
            <Space vertical>
              <Radio value={true}>仅删除资产账户，保留账单</Radio>
              <Radio value={false}>删除资产账户和账单</Radio>
            </Space>
          </Radio.Group>
        ) : (
          <Space vertical>
            <Typography.Text>
              确定删除账户“{values.accountName ?? '未命名账户'}”吗？
            </Typography.Text>
            <Typography.Text type="secondary">
              {isKeepBill
                ? '账户删除后，相关账单将被保留。'
                : '账户及其相关账单将被同时删除，此操作不可恢复。'}
            </Typography.Text>
          </Space>
        )}
      </Modal>
    </>
  );
};

export default DeleteModal;
