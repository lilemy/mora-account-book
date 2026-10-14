import {
  ModalForm,
  ProFormDatePicker,
  ProFormDigit,
  ProFormSelect,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { Form, message, TreeSelect } from 'antd';
import dayjs, { type Dayjs } from 'dayjs';
import React, { cloneElement, useState } from 'react';
import {
  createAccountBookBill,
  getUserAccountBookBillVoById,
  updateAccountBookBill,
} from '@/services/mora-account-book/accountBookBillController';
import { getAccountBookCategoryList } from '@/services/mora-account-book/accountBookController';
import { getUserAccountVoPage } from '@/services/mora-account-book/userAccountController';
import { getAccountBookIconConfig } from '@/utils/accountBookIcon';

type BillFormValues = {
  bookId?: number;
  categoryType: 'INCOME' | 'EXPENSE';
  categoryId?: number;
  accountId?: number;
  amount?: number;
  billTime?: Dayjs | string;
  remark?: string;
};

type CategoryTreeNode = {
  title: React.ReactNode;
  value: number;
  children?: CategoryTreeNode[];
};

const { renderIcon } = getAccountBookIconConfig();

const formatBillTime = (value?: Dayjs | string) => {
  if (!value) return undefined;
  const parsed = dayjs(value);
  return parsed.isValid() ? parsed.format('YYYY-MM-DD HH:mm:ss') : undefined;
};

const toTreeData = (
  categories: API.AccountBookCategoryVO[],
): CategoryTreeNode[] =>
  categories.flatMap((category) => {
    if (category.id == null) return [];
    const children = toTreeData(category.children ?? []);
    return [
      {
        title: (
          <span
            style={{ display: 'inline-flex', alignItems: 'center', gap: 8 }}
          >
            {renderIcon(category.icon, { style: { color: category.color } })}
            <span>{category.categoryName ?? '未命名分类'}</span>
          </span>
        ),
        value: category.id,
        children: children.length > 0 ? children : undefined,
      },
    ];
  });

export default function CreateBillModal({
  trigger,
  billId,
}: {
  trigger: React.ReactElement<any>;
  billId?: number;
}) {
  const [open, setOpen] = useState(false);
  const [loadingDetail, setLoadingDetail] = useState(false);
  const queryClient = useQueryClient();
  const [bookId, setBookId] = useState<string>();
  const [form] = Form.useForm<BillFormValues>();
  const [api, holder] = message.useMessage();
  const categoryType = Form.useWatch('categoryType', form) ?? 'EXPENSE';

  const categoriesQuery = useQuery({
    queryKey: ['account-book-categories', bookId, categoryType],
    enabled: open && bookId != null,
    queryFn: async () => {
      const response = await getAccountBookCategoryList({
        bookId: String(bookId),
        categoryType,
      });
      if (response.code !== 0) {
        throw new Error(response.message || '获取分类失败');
      }
      return response.data ?? [];
    },
  });

  const accountsQuery = useQuery({
    queryKey: ['user-accounts-for-bill'],
    enabled: open,
    queryFn: async () => {
      const response = await getUserAccountVoPage({
        pageNum: '1',
        pageSize: '100',
      });
      if (response.code !== 0) {
        throw new Error(response.message || '获取账户失败');
      }
      return response.data?.records ?? [];
    },
  });

  const { mutateAsync, isPending: creating } = useMutation({
    mutationFn: createAccountBookBill,
  });
  const { mutateAsync: updateAsync, isPending: updating } = useMutation({
    mutationFn: updateAccountBookBill,
  });
  const isPending = creating || updating;

  return (
    <>
      {holder}
      {cloneElement(trigger, {
        loading: loadingDetail,
        onClick: async () => {
          if (billId != null) {
            setLoadingDetail(true);
            try {
              const response = await getUserAccountBookBillVoById({
                id: billId,
              });
              if (response.code !== 0 || !response.data)
                throw new Error(response.message || '获取流水详情失败');
              const bill = response.data;
              setBookId(String(bill.bookId));
              form.resetFields();
              form.setFieldsValue({
                categoryType: bill.billType ?? 'EXPENSE',
                categoryId: bill.categoryId,
                accountId: bill.accountId,
                amount: bill.amount,
                billTime: bill.billTime ? dayjs(bill.billTime) : undefined,
                remark: bill.remark,
              });
              setOpen(true);
            } catch (error) {
              void api.error(
                error instanceof Error ? error.message : '获取流水详情失败',
              );
            } finally {
              setLoadingDetail(false);
            }
            return;
          }
          const selectedBookId = localStorage.getItem('selectedBookId') ?? '';
          setBookId(selectedBookId);
          form.resetFields();
          form.setFieldsValue({
            bookId: selectedBookId as any,
            categoryType: 'EXPENSE',
            billTime: dayjs(),
          });
          setOpen(true);
        },
      })}
      <ModalForm<BillFormValues>
        form={form}
        open={open}
        title={billId == null ? '新增流水' : '编辑流水'}
        dateFormatter={false}
        initialValues={{ categoryType: 'EXPENSE', billTime: dayjs() }}
        onOpenChange={setOpen}
        modalProps={{ destroyOnHidden: true, confirmLoading: isPending }}
        onFinish={async (values) => {
          if (values.categoryId == null || values.amount == null) {
            return false;
          }

          try {
            const fields = {
              categoryId: values.categoryId,
              amount: values.amount,
              billTime: formatBillTime(values.billTime),
              remark: values.remark ?? '',
            };
            const response =
              billId == null
                ? await mutateAsync({
                    bookId: bookId as any,
                    accountId: values.accountId,
                    ...fields,
                  })
                : await updateAsync({
                    id: billId,
                    accountId: values.accountId ?? 0,
                    ...fields,
                  });
            if (response.code !== 0) {
              void api.error(`${response.message ?? ''}保存失败`);
              return false;
            }
            void api.success(billId == null ? '新增流水成功' : '编辑流水成功');
            void queryClient.invalidateQueries({
              queryKey: ['account-book-calendar-month'],
            });
            void queryClient.invalidateQueries({
              queryKey: ['account-book-calendar-year'],
            });
            void queryClient.invalidateQueries({
              queryKey: ['account-book-bill-list'],
            });
            if (billId != null) {
              void queryClient.invalidateQueries({
                queryKey: ['account-book-bill-detail', billId],
              });
            }
            setOpen(false);
            return true;
          } catch (error) {
            void api.error(error instanceof Error ? error.message : '保存失败');
            return false;
          }
        }}
        onValuesChange={(changedValues) => {
          if ('categoryType' in changedValues) {
            form.setFieldValue('categoryId', undefined);
          }
        }}
      >
        <ProFormSelect
          name="categoryType"
          label="收支类型"
          options={[
            { label: '支出', value: 'EXPENSE' },
            { label: '收入', value: 'INCOME' },
          ]}
          fieldProps={{ allowClear: false }}
          rules={[{ required: true, message: '请选择收支类型' }]}
        />
        <Form.Item
          name="categoryId"
          label="分类"
          rules={[{ required: true, message: '请选择分类' }]}
        >
          <TreeSelect
            style={{ width: '100%' }}
            placeholder="请选择分类"
            treeData={toTreeData(categoriesQuery.data ?? [])}
            treeDefaultExpandAll
            loading={categoriesQuery.isFetching}
            notFoundContent={bookId == null ? '请先选择账本' : '暂无分类'}
          />
        </Form.Item>
        <ProFormSelect
          name="accountId"
          label="账户"
          placeholder="可不选择账户"
          options={(accountsQuery.data ?? []).flatMap((account) =>
            account.id == null
              ? []
              : [
                  {
                    label: account.accountName ?? `账户 ${account.id}`,
                    value: account.id,
                  },
                ],
          )}
          fieldProps={{
            allowClear: true,
            showSearch: true,
            optionFilterProp: 'label',
            loading: accountsQuery.isFetching,
          }}
        />
        <ProFormDigit
          name="amount"
          label="金额"
          min={0.01}
          precision={2}
          rules={[{ required: true, message: '请输入金额' }]}
        />
        <ProFormDatePicker name="billTime" label="发生时间" showTime />
        <ProFormTextArea name="remark" label="备注" />
      </ModalForm>
    </>
  );
}
