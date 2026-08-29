import type { ProColumns } from '@ant-design/pro-components';
import {
  ModalForm,
  PageContainer,
  ProForm,
  ProFormColorPicker,
  ProFormDigit,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProTable,
} from '@ant-design/pro-components';
import { useParams } from '@umijs/max';
import { App, Button, Flex, Space, Spin, Tag } from 'antd';
import { useCallback, useEffect, useMemo, useState } from 'react';
import {
  changeCategoryTemplateItemStatus,
  createCategoryTemplateItem,
  getCategoryTemplateVo,
  updateCategoryTemplate,
  updateCategoryTemplateItem,
} from '@/services/mora-account-book/categoryTemplateController';
import { getAccountBookIconConfig } from '@/utils/accountBookIcon';

type TemplateFormValues = {
  templateCode?: string;
  templateName?: string;
  isDefault?: boolean;
};

type CategoryItemFormValues = {
  parentId?: string | number;
  categoryName?: string;
  categoryType?: API.CategoryTemplateItemVO['categoryType'];
  icon?: string;
  color?: string;
  sortOrder?: number;
};

const { options: iconOptions } = getAccountBookIconConfig();

const categoryTypeText: Record<string, string> = {
  INCOME: '收入',
  EXPENSE: '支出',
  TRANSFER: '转账',
};

const categoryTypeColor: Record<string, string> = {
  INCOME: 'green',
  EXPENSE: 'red',
  TRANSFER: 'blue',
};

const flattenCategoryItems = (
  items: API.CategoryTemplateItemVO[],
): API.CategoryTemplateItemVO[] =>
  items.flatMap((item) => [item, ...flattenCategoryItems(item.children ?? [])]);

const CategoryTemplateUpdatePage = () => {
  const { id } = useParams<{ id?: string }>();
  const { message } = App.useApp();
  const [templateForm] = ProForm.useForm<TemplateFormValues>();
  const [itemForm] = ProForm.useForm<CategoryItemFormValues>();
  const [detail, setDetail] = useState<API.CategoryTemplateVO>();
  const [loading, setLoading] = useState(true);
  const [itemModalOpen, setItemModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<API.CategoryTemplateItemVO>();
  const [statusUpdatingId, setStatusUpdatingId] = useState<number>();

  const loadDetail = useCallback(async () => {
    if (id == null) {
      setDetail(undefined);
      setLoading(false);
      return;
    }

    setLoading(true);
    try {
      const response = await getCategoryTemplateVo({ id: id as any });
      if (response.code !== 0 || !response.data) {
        void message.error(`获取模板详情失败${response.message ?? ''}`);
        setDetail(undefined);
        return;
      }

      setDetail(response.data);
      templateForm.setFieldsValue({
        templateCode: response.data.templateCode,
        templateName: response.data.templateName,
        isDefault: response.data.isDefault,
      });
    } catch (error) {
      const errorMessage =
        error instanceof Error ? error.message : String(error);
      void message.error(`获取模板详情失败${errorMessage}`);
      setDetail(undefined);
    } finally {
      setLoading(false);
    }
  }, [message, templateForm, id]);

  useEffect(() => {
    void loadDetail();
  }, [loadDetail]);

  const parentOptions = useMemo(
    () => [
      { label: '顶级', value: '0' },
      ...(detail?.itemList ?? [])
        .filter((item) => item.id !== editingItem?.id)
        .map((item) => ({
          label: item.categoryName || `分类项 ${item.id ?? ''}`,
          value: item.id,
        })),
    ],
    [detail?.itemList, editingItem?.id],
  );

  const openCreateItem = useCallback(() => {
    setEditingItem(undefined);
    itemForm.resetFields();
    itemForm.setFieldsValue({ parentId: '0', sortOrder: 0 });
    setItemModalOpen(true);
  }, [itemForm]);

  const openCreateChildItem = useCallback(
    (parent: API.CategoryTemplateItemVO) => {
      if (parent.id == null) {
        void message.error('无法获取父级分类 ID');
        return;
      }

      setEditingItem(undefined);
      itemForm.resetFields();
      itemForm.setFieldsValue({
        parentId: parent.id,
        categoryType: parent.categoryType,
        sortOrder: 0,
      });
      setItemModalOpen(true);
    },
    [itemForm, message],
  );

  const openEditItem = useCallback(
    (item: API.CategoryTemplateItemVO) => {
      setEditingItem(item);
      itemForm.setFieldsValue({
        parentId: item.parentId ?? '0',
        categoryName: item.categoryName,
        categoryType: item.categoryType,
        icon: item.icon,
        color: item.color,
        sortOrder: item.sortOrder,
      });
      setItemModalOpen(true);
    },
    [itemForm],
  );

  const handleTemplateSubmit = useCallback(
    async (values: TemplateFormValues) => {
      if (!detail?.id) {
        void message.error('无法获取模板 ID');
        return false;
      }

      try {
        const response = await updateCategoryTemplate({
          id: String(detail.id),
          templateCode: values.templateCode,
          templateName: values.templateName,
          isDefault: values.isDefault ? 'true' : 'false',
        });
        if (response.code !== 0 || response.data !== true) {
          void message.error(`模板更新失败${response.message ?? ''}`);
          return false;
        }

        void message.success('模板更新成功');
        await loadDetail();
        return true;
      } catch (error) {
        const errorMessage =
          error instanceof Error ? error.message : String(error);
        void message.error(`模板更新失败${errorMessage}`);
        return false;
      }
    },
    [detail?.id, loadDetail, message],
  );

  const handleItemSubmit = useCallback(
    async (values: CategoryItemFormValues) => {
      if (!detail?.id) {
        void message.error('无法获取模板 ID');
        return false;
      }

      try {
        const commonValues = {
          parentId: String(values.parentId ?? 0),
          categoryName: values.categoryName ?? '',
          categoryType: values.categoryType ?? 'EXPENSE',
          icon: values.icon,
          color: values.color,
          sortOrder: String(values.sortOrder ?? 0),
        };
        const response = editingItem?.id
          ? await updateCategoryTemplateItem({
              ...commonValues,
              id: String(editingItem.id),
              templateId: String(detail.id),
            })
          : await createCategoryTemplateItem({
              ...commonValues,
              templateId: String(detail.id),
            });

        if (response.code !== 0 || response.data !== true) {
          void message.error(
            `${editingItem?.id ? '分类项更新' : '分类项新增'}失败${response.message ?? ''}`,
          );
          return false;
        }

        void message.success(
          `${editingItem?.id ? '分类项更新' : '分类项新增'}成功`,
        );
        setItemModalOpen(false);
        setEditingItem(undefined);
        await loadDetail();
        return true;
      } catch (error) {
        const errorMessage =
          error instanceof Error ? error.message : String(error);
        void message.error(
          `${editingItem?.id ? '分类项更新' : '分类项新增'}失败${errorMessage}`,
        );
        return false;
      }
    },
    [detail?.id, editingItem?.id, loadDetail, message],
  );

  const handleToggleItemStatus = useCallback(
    async (item: API.CategoryTemplateItemVO) => {
      if (item.id == null) {
        void message.error('无法获取分类项 ID');
        return;
      }

      setStatusUpdatingId(item.id);
      try {
        const response = await changeCategoryTemplateItemStatus({
          id: item.id,
        });
        if (response.code !== 0 || response.data !== true) {
          void message.error(`分类项状态更新失败${response.message ?? ''}`);
          return;
        }

        void message.success(
          item.status === 'ENABLE' ? '分类项已禁用' : '分类项已启用',
        );
        await loadDetail();
      } catch (error) {
        const errorMessage =
          error instanceof Error ? error.message : String(error);
        void message.error(`分类项状态更新失败${errorMessage}`);
      } finally {
        setStatusUpdatingId(undefined);
      }
    },
    [loadDetail, message],
  );

  const columns: ProColumns<API.CategoryTemplateItemVO>[] = [
    {
      title: '分类名称',
      dataIndex: 'categoryName',
      render: (_, record) => record.categoryName || '-',
    },
    {
      title: '分类类型',
      dataIndex: 'categoryType',
      render: (_, record) => {
        const type = record.categoryType;
        return type ? (
          <Tag color={categoryTypeColor[type]}>
            {categoryTypeText[type] || type}
          </Tag>
        ) : (
          '-'
        );
      },
    },
    {
      title: '层级',
      dataIndex: 'categoryLevel',
      render: (_, record) =>
        record.categoryLevel === 'LEVEL_1' ? '一级' : '二级',
    },
    {
      title: '排序',
      dataIndex: 'sortOrder',
      width: 90,
    },
    {
      title: '状态',
      dataIndex: 'status',
      valueEnum: {
        ENABLE: { text: '正常', status: 'Success' },
        DISABLE: { text: '停用', status: 'Default' },
      },
    },
    {
      title: '操作',
      valueType: 'option',
      render: (_, record) => (
        <Space size="small">
          {record.categoryLevel === 'LEVEL_1' ? (
            <Button
              type="link"
              className="p-0!"
              onClick={() => openCreateChildItem(record)}
            >
              新增子分类
            </Button>
          ) : null}
          <Button
            type="link"
            className="p-0!"
            onClick={() => openEditItem(record)}
          >
            修改
          </Button>
          <Button
            type="link"
            className="p-0!"
            danger={record.status === 'ENABLE'}
            loading={statusUpdatingId === record.id}
            onClick={() => void handleToggleItemStatus(record)}
          >
            {record.status === 'ENABLE' ? '禁用' : '启用'}
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <PageContainer
      title={
        detail?.templateName
          ? `更新模板：${detail.templateName}`
          : '更新分类模板'
      }
    >
      <div className="mx-auto w-full max-w-400">
        {loading ? (
          <Spin style={{ display: 'block', padding: 48 }} />
        ) : detail ? (
          <Flex vertical gap={24}>
            <ProForm<TemplateFormValues>
              form={templateForm}
              layout="horizontal"
              onFinish={handleTemplateSubmit}
              submitter={{ searchConfig: { submitText: '保存模板' } }}
            >
              <ProForm.Group>
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
              </ProForm.Group>
            </ProForm>

            <ProTable<API.CategoryTemplateItemVO>
              rowKey={(record) => String(record.id)}
              headerTitle={`模板分类项（${flattenCategoryItems(detail.itemList ?? []).length}）`}
              toolBarRender={() => [
                <Button key="create" type="primary" onClick={openCreateItem}>
                  新增分类项
                </Button>,
              ]}
              search={false}
              options={false}
              pagination={false}
              expandable={{ defaultExpandAllRows: true }}
              dataSource={detail.itemList ?? []}
              columns={columns}
            />

            <ModalForm<CategoryItemFormValues>
              form={itemForm}
              open={itemModalOpen}
              title={editingItem ? '修改分类项' : '新增分类项'}
              width={560}
              onOpenChange={(open) => {
                setItemModalOpen(open);
                if (!open) {
                  setEditingItem(undefined);
                  itemForm.resetFields();
                }
              }}
              onFinish={handleItemSubmit}
              modalProps={{ destroyOnHidden: true }}
            >
              <ProFormSelect
                name="parentId"
                label="父级分类"
                options={parentOptions}
                fieldProps={{ showSearch: true }}
              />
              <ProFormText
                name="categoryName"
                label="分类名称"
                rules={[{ required: true, message: '请输入分类名称' }]}
              />
              <ProFormSelect
                name="categoryType"
                label="分类类型"
                options={Object.entries(categoryTypeText).map(
                  ([value, label]) => ({
                    value,
                    label,
                  }),
                )}
                rules={[{ required: true, message: '请选择分类类型' }]}
              />
              <ProFormSelect
                name="icon"
                label="分类图标"
                options={iconOptions}
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
                label="分类颜色"
                getValueFromEvent={(_color: unknown, cssColor: string) =>
                  cssColor
                }
              />
              <ProFormDigit
                name="sortOrder"
                label="排序值"
                min={0}
                fieldProps={{ precision: 0 }}
              />
            </ModalForm>
          </Flex>
        ) : (
          <div style={{ padding: 48, textAlign: 'center' }}>暂无模板详情</div>
        )}
      </div>
    </PageContainer>
  );
};

export default CategoryTemplateUpdatePage;
