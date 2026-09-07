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
import { App, Form, Space, Spin } from 'antd';
import dayjs, { type Dayjs } from 'dayjs';
import React, { cloneElement, useCallback, useState } from 'react';
import {
  getUserAccountManageVoById,
  updateUserAccount,
} from '@/services/mora-account-book/userAccountController';
import { getUserAccountIconConfig } from '@/utils/userAccountIcon';

type UpdateFormProps = {
  trigger?: React.ReactElement<any>;
  onOk?: () => void;
  values: Partial<API.UserAccountManageVO>;
};

type UpdateFormValues = Omit<
  API.UserAccountUpdateRequest,
  'id' | 'loanDate' | 'repaymentDate'
> & {
  loanDate?: Dayjs;
  repaymentDate?: Dayjs;
};

type AccountIconOption = ReturnType<
  typeof getUserAccountIconConfig
>['iconOptions'][number];

const { getIconsByAccountType } = getUserAccountIconConfig();

const getIconOptions = (
  accountType?: API.UserAccountManageVO['accountType'],
): AccountIconOption[] => {
  if (!accountType) {
    return [];
  }

  return getIconsByAccountType(accountType).map(
    ({ value, label, component: IconComponent, color }) => ({
      value,
      label,
      color,
      icon: <IconComponent />,
    }),
  );
};

const UpdateForm: React.FC<UpdateFormProps> = ({ onOk, trigger, values }) => {
  const [form] = Form.useForm<UpdateFormValues>();
  const { message } = App.useApp();
  const [open, setOpen] = useState(false);
  const [loadingDetail, setLoadingDetail] = useState(false);
  const [detail, setDetail] = useState<API.UserAccountManageVO>();

  const { mutateAsync: saveAccount, isPending: saving } = useMutation({
    mutationFn: updateUserAccount,
  });

  const onCancel = useCallback(() => {
    setOpen(false);
    setDetail(undefined);
    form.resetFields();
  }, [form]);

  const onOpen = useCallback(async () => {
    if (values.id == null) {
      void message.error('无法获取账户 ID');
      return;
    }

    setOpen(true);
    setLoadingDetail(true);
    setDetail(undefined);
    form.resetFields();

    try {
      const response = await getUserAccountManageVoById({ id: values.id });
      if (response.code !== 0 || !response.data) {
        void message.error(`获取账户信息失败${response.message ?? ''}`);
        setOpen(false);
        return;
      }

      setDetail(response.data);
      form.setFieldsValue({
        accountName: response.data.accountName,
        bankName: response.data.bankName,
        creditStatementDay: response.data.creditStatementDay,
        creditRepaymentDay: response.data.creditRepaymentDay,
        loanUser: response.data.loanUser,
        loanDirection: response.data.loanDirection,
        loanDate: response.data.loanDate
          ? dayjs(response.data.loanDate)
          : undefined,
        repaymentDate: response.data.repaymentDate
          ? dayjs(response.data.repaymentDate)
          : undefined,
        accountNoTail: response.data.accountNoTail,
        icon: response.data.icon,
        isDefault: response.data.isDefault,
        remark: response.data.remark,
      });
    } catch (error: any) {
      void message.error(`获取账户信息失败${error.message}`);
      setOpen(false);
    } finally {
      setLoadingDetail(false);
    }
  }, [form, message, values.id]);

  const onFinish = useCallback(
    async (formValues: UpdateFormValues) => {
      const id = detail?.id ?? values.id;
      if (id == null) {
        void message.error('无法获取账户 ID');
        return false;
      }

      const request: API.UserAccountUpdateRequest = {
        id,
        accountName: formValues.accountName,
        icon: formValues.icon,
        isDefault: formValues.isDefault,
        remark: formValues.remark,
      };

      if (detail?.accountType !== 'LOAN') {
        request.bankName = formValues.bankName;
        request.accountNoTail = formValues.accountNoTail;
      }

      if (detail?.accountType === 'CREDIT') {
        request.creditStatementDay = formValues.creditStatementDay;
        request.creditRepaymentDay = formValues.creditRepaymentDay;
      }

      if (detail?.accountType === 'LOAN') {
        request.loanUser = formValues.loanUser;
        request.loanDirection = formValues.loanDirection;
        request.loanDate = formValues.loanDate?.format('YYYY-MM-DD');
        request.repaymentDate = formValues.repaymentDate?.format('YYYY-MM-DD');
      }

      try {
        const response = await saveAccount(request);
        if (response.code !== 0 || response.data !== true) {
          void message.error(`账户信息更新失败${response.message ?? ''}`);
          return false;
        }

        void message.success('账户信息更新成功');
        onOk?.();
        onCancel();
        return true;
      } catch (error: any) {
        void message.error(`账户信息更新失败${error.message}`);
        return false;
      }
    },
    [detail, message, onCancel, onOk, saveAccount, values.id],
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
      {triggerElement}
      <ModalForm<UpdateFormValues>
        form={form}
        open={open}
        title="修改账户"
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
              name="accountName"
              label="账户名称"
              rules={[{ required: true, message: '请输入账户名称' }]}
            />
            {detail.accountType !== 'LOAN' && (
              <ProForm.Group>
                <ProFormText name="bankName" label="银行名称" />
                <ProFormText
                  name="accountNoTail"
                  label="卡号后四位"
                  fieldProps={{ maxLength: 4 }}
                />
              </ProForm.Group>
            )}
            {detail.accountType === 'CREDIT' && (
              <ProForm.Group>
                <ProFormDigit
                  name="creditStatementDay"
                  label="出账日"
                  min={1}
                  max={28}
                  fieldProps={{ precision: 0 }}
                  rules={[{ required: true, message: '请输入出账日' }]}
                />
                <ProFormDigit
                  name="creditRepaymentDay"
                  label="还款日"
                  min={1}
                  max={28}
                  fieldProps={{ precision: 0 }}
                  rules={[{ required: true, message: '请输入还款日' }]}
                />
              </ProForm.Group>
            )}
            {detail.accountType === 'LOAN' && (
              <>
                <ProFormText
                  name="loanUser"
                  label="借贷人姓名"
                  rules={[{ required: true, message: '请输入借贷人姓名' }]}
                />
                <ProFormSelect
                  name="loanDirection"
                  label="借贷方向"
                  options={[
                    { label: '借出', value: 'LEND' },
                    { label: '借入', value: 'BORROW' },
                  ]}
                  rules={[{ required: true, message: '请选择借贷方向' }]}
                />
                <ProForm.Group>
                  <ProFormDatePicker
                    name="loanDate"
                    label="发生日期"
                    fieldProps={{ format: 'YYYY-MM-DD' }}
                    rules={[{ required: true, message: '请选择发生日期' }]}
                  />
                  <ProFormDatePicker
                    name="repaymentDate"
                    label="计划还款日期"
                    fieldProps={{ format: 'YYYY-MM-DD' }}
                    rules={[{ required: true, message: '请选择计划还款日期' }]}
                  />
                </ProForm.Group>
              </>
            )}
            <ProFormSelect<string, AccountIconOption>
              name="icon"
              label="账户图标"
              options={getIconOptions(detail.accountType)}
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
            <ProFormTextArea name="remark" label="备注" />
          </>
        )}
      </ModalForm>
    </>
  );
};

export default UpdateForm;
