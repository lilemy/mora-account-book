import {
  ModalForm,
  ProForm,
  ProFormDatePicker,
  ProFormDigit,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useMutation } from '@tanstack/react-query';
import { Form, type FormInstance, message, Space } from 'antd';
import type { Dayjs } from 'dayjs';
import React, { cloneElement, useCallback, useState } from 'react';
import { createUserAccount } from '@/services/mora-account-book/userAccountController';
import { getUserAccountIconConfig } from '@/utils/userAccountIcon';

type CreateModalProps = {
  trigger?: React.ReactElement<any>;
  onOk?: () => void;
};

type CreateFormValues = Omit<
  API.UserAccountCreateRequest,
  'loanDate' | 'repaymentDate'
> & {
  loanDate?: Dayjs;
  repaymentDate?: Dayjs;
};

const { accountTypeOptions, getIconsByAccountType } =
  getUserAccountIconConfig();

const CreateModal: React.FC<CreateModalProps> = ({ trigger, onOk }) => {
  const [form] = Form.useForm<CreateFormValues>();
  const [open, setOpen] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();
  const accountType = Form.useWatch('accountType', form);
  const iconOptions = getIconsByAccountType(accountType ?? 'CAPITAL').map(
    ({ value, label, component: IconComponent }) => ({
      value,
      label,
      icon: <IconComponent />,
    }),
  );

  const { mutateAsync: saveAccount, isPending: saving } = useMutation({
    mutationFn: createUserAccount,
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
    async (values: CreateFormValues) => {
      const request: API.UserAccountCreateRequest = {
        accountName: values.accountName.trim(),
        accountType: values.accountType,
        currentBalance: values.currentBalance,
        icon: values.icon,
        isDefault: values.isDefault ?? false,
        remark: values.remark,
      };

      if (values.accountType !== 'LOAN') {
        request.bankName = values.bankName;
        request.accountNoTail = values.accountNoTail;
      }
      if (values.accountType === 'CREDIT') {
        request.creditLimit = values.creditLimit;
        request.creditStatementDay = values.creditStatementDay;
        request.creditRepaymentDay = values.creditRepaymentDay;
      }
      if (values.accountType === 'LOAN') {
        request.loanUser = values.loanUser?.trim();
        request.loanDirection = values.loanDirection;
        request.loanDate = values.loanDate?.format('YYYY-MM-DD');
        request.repaymentDate = values.repaymentDate?.format('YYYY-MM-DD');
      }

      try {
        const response = await saveAccount(request);
        if (response.code !== 0 || response.data == null) {
          void messageApi.error(
            `账户创建失败${response.message ? `：${response.message}` : ''}`,
          );
          return false;
        }
        void messageApi.success('账户创建成功');
        onOk?.();
        onCancel();
        return true;
      } catch (error) {
        const errorMessage =
          error instanceof Error ? error.message : String(error);
        void messageApi.error(`账户创建失败：${errorMessage}`);
        return false;
      }
    },
    [messageApi, onCancel, onOk, saveAccount],
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
        title="新增账户"
        width={640}
        dateFormatter={false}
        initialValues={{
          accountType: 'CAPITAL',
          currentBalance: 0,
          isDefault: false,
        }}
        onFinish={onFinish}
        onValuesChange={(changedValues) => {
          if ('accountType' in changedValues)
            form.setFieldValue('icon', undefined);
        }}
        onOpenChange={(nextOpen) => {
          if (!nextOpen) onCancel();
        }}
        modalProps={{ destroyOnHidden: true, onCancel, confirmLoading: saving }}
      >
        <ProForm.Group>
          <ProFormSelect
            name="accountType"
            label="账户类型"
            options={accountTypeOptions}
            fieldProps={{ allowClear: false }}
            rules={[{ required: true, message: '请选择账户类型' }]}
          />
          <ProFormSelect<string, (typeof iconOptions)[number]>
            name="icon"
            label="账户图标"
            options={iconOptions}
            rules={[{ required: true, message: '请选择账户图标' }]}
            fieldProps={{
              showSearch: { optionFilterProp: 'label' },
              optionRender: (option) => (
                <Space size="small">
                  {option.data.icon}
                  <span>{option.data.label}</span>
                </Space>
              ),
            }}
          />
          <ProFormSwitch name="isDefault" label="默认账户" />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormText
            name="accountName"
            label="账户名称"
            fieldProps={{ maxLength: 50 }}
            rules={[
              { required: true, whitespace: true, message: '请输入账户名称' },
            ]}
          />
          <ProFormDigit
            name="currentBalance"
            label={
              accountType === 'CREDIT'
                ? '欠款余额'
                : accountType === 'LOAN'
                  ? '待还金额'
                  : '当前余额'
            }
            min={0}
            fieldProps={{ precision: 2 }}
            rules={[
              { required: true, message: '请输入金额' },
              { type: 'number', min: 0 },
            ]}
          />
        </ProForm.Group>
        {accountType !== 'LOAN' && (
          <ProForm.Group>
            <ProFormText
              name="bankName"
              label="银行名称"
              preserve={false}
              fieldProps={{ maxLength: 50 }}
            />
            <ProFormText
              name="accountNoTail"
              label="卡号后四位"
              preserve={false}
              fieldProps={{ maxLength: 4 }}
              rules={[{ pattern: /^\d{4}$/, message: '请输入四位数字' }]}
            />
          </ProForm.Group>
        )}
        {accountType === 'CREDIT' && (
          <>
            <ProFormDigit
              name="creditLimit"
              label="信用额度"
              preserve={false}
              min={0}
              fieldProps={{ precision: 2 }}
              dependencies={['currentBalance']}
              rules={[
                { required: true, message: '请输入信用额度' },
                { type: 'number', min: 0 },
                ({ getFieldValue }: FormInstance<CreateFormValues>) => ({
                  validator: (_: unknown, value: number | undefined) =>
                    value == null || value >= getFieldValue('currentBalance')
                      ? Promise.resolve()
                      : Promise.reject(new Error('信用额度不能小于欠款余额')),
                }),
              ]}
            />
            <ProForm.Group>
              <ProFormDigit
                name="creditStatementDay"
                label="出账日"
                preserve={false}
                min={1}
                max={28}
                fieldProps={{ precision: 0 }}
                rules={[
                  { required: true, message: '请输入出账日' },
                  { type: 'integer', min: 1, max: 28 },
                ]}
              />
              <ProFormDigit
                name="creditRepaymentDay"
                label="还款日"
                preserve={false}
                min={1}
                max={28}
                fieldProps={{ precision: 0 }}
                rules={[
                  { required: true, message: '请输入还款日' },
                  { type: 'integer', min: 1, max: 28 },
                ]}
              />
            </ProForm.Group>
          </>
        )}
        {accountType === 'LOAN' && (
          <>
            <ProForm.Group>
              <ProFormText
                name="loanUser"
                label="借贷人姓名"
                preserve={false}
                fieldProps={{ maxLength: 50 }}
                rules={[
                  {
                    required: true,
                    whitespace: true,
                    message: '请输入借贷人姓名',
                  },
                ]}
              />
              <ProFormSelect
                name="loanDirection"
                label="借贷方向"
                preserve={false}
                options={[
                  { label: '借出', value: 'LEND' },
                  { label: '借入', value: 'BORROW' },
                ]}
                rules={[{ required: true, message: '请选择借贷方向' }]}
              />
            </ProForm.Group>
            <ProForm.Group>
              <ProFormDatePicker
                name="loanDate"
                label="发生日期"
                preserve={false}
                fieldProps={{ format: 'YYYY-MM-DD' }}
              />
              <ProFormDatePicker
                name="repaymentDate"
                label="计划还款日期"
                preserve={false}
                fieldProps={{ format: 'YYYY-MM-DD' }}
                dependencies={['loanDate']}
                rules={[
                  ({ getFieldValue }: FormInstance<CreateFormValues>) => ({
                    validator: (_: unknown, value: Dayjs | undefined) =>
                      !value ||
                      !getFieldValue('loanDate') ||
                      !value.isBefore(getFieldValue('loanDate'), 'day')
                        ? Promise.resolve()
                        : Promise.reject(new Error('还款日期不能早于发生日期')),
                  }),
                ]}
              />
            </ProForm.Group>
          </>
        )}
        <ProFormTextArea name="remark" label="备注" />
      </ModalForm>
    </>
  );
};

export default CreateModal;
