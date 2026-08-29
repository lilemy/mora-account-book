import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import { Link } from '@umijs/max';
import { Button, message, Popconfirm, Space } from 'antd';
import React, { useRef, useState } from 'react';
import CreateModal from '@/pages/admin/category/components/CreateModal';
import DetailModal from '@/pages/admin/category/components/DetailModal';
import {
  changeCategoryTemplateStatus,
  deleteCategoryTemplate,
  getCategoryTemplateVoPage,
} from '@/services/mora-account-book/categoryTemplateController';

const CategoryTemplateTableList: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [messageApi, contextHolder] = message.useMessage();

  const [showDetail, setShowDetail] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.CategoryTemplateVO>();
  const [statusUpdatingId, setStatusUpdatingId] = useState<number>();

  const columns: ProColumns<API.CategoryTemplateVO>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
      hideInTable: true,
      hideInForm: true,
      search: false,
    },
    {
      title: '模板名称',
      dataIndex: 'templateName',
      render: (dom, entity) => {
        return (
          <a
            type="link"
            onClick={() => {
              setCurrentRow(entity);
              setShowDetail(true);
            }}
          >
            <div>{dom}</div>
          </a>
        );
      },
    },
    {
      title: '模板编码',
      dataIndex: 'templateCode',
      valueType: 'text',
    },
    {
      title: '模板状态',
      dataIndex: 'status',
      valueEnum: {
        ENABLE: {
          text: '正常',
          status: 'Success',
        },
        DISABLE: {
          text: '停用',
          status: 'Default',
        },
      },
    },
    {
      title: '默认模板',
      dataIndex: 'isDefault',
      valueEnum: {
        true: {
          text: '是',
          status: 'Success',
        },
        false: {
          text: '否',
          status: 'Default',
        },
      },
    },
    {
      title: '创建时间',
      sorter: true,
      search: false,
      hideInForm: true,
      dataIndex: 'createTime',
      valueType: 'dateTime',
    },
    {
      title: '更新时间',
      sorter: true,
      search: false,
      hideInForm: true,
      dataIndex: 'updateTime',
      valueType: 'dateTime',
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => (
        <Space size="small">
          <Link to={`/admin/category-template/update/${record.id}`}>修改</Link>
          <Popconfirm
            title={`确定${record.status === 'ENABLE' ? '禁用' : '启用'}这个分类模板吗?`}
            onConfirm={() => handleToggleStatus(record)}
            okText="确定"
            cancelText="取消"
          >
            <Button
              type="link"
              danger={record.status === 'ENABLE'}
              loading={statusUpdatingId === record.id}
              className="p-0!"
            >
              {record.status === 'ENABLE' ? '禁用' : '启用'}
            </Button>
          </Popconfirm>
          <Popconfirm
            title="该操作不可能，确定删除这个分类模板吗?"
            onConfirm={() => handleRemove(record)}
            okText="确定"
            cancelText="取消"
          >
            <Button type="link" danger className="p-0!">
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  /**
   *  Delete node
   * @zh-CN 删除节点
   *
   * @param selectedRow
   */
  const handleRemove = async (selectedRow: API.CategoryTemplateVO) => {
    const hide = messageApi.loading('正在删除');
    if (!selectedRow?.id) return;
    try {
      await deleteCategoryTemplate({ id: selectedRow.id });
      hide();
      messageApi.success('删除成功').then();
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      messageApi.error(`删除失败，${error.message}`).then();
      return false;
    }
  };

  const handleToggleStatus = async (selectedRow: API.CategoryTemplateVO) => {
    if (selectedRow.id == null) {
      void messageApi.error('无法获取分类模板 ID');
      return false;
    }

    setStatusUpdatingId(selectedRow.id);
    try {
      const response = await changeCategoryTemplateStatus({
        id: selectedRow.id,
      });
      if (response.code !== 0) {
        void messageApi.error(
          `分类模板状态更新失败${response.message ? `：${response.message}` : ''}`,
        );
        return false;
      }

      void messageApi.success(
        selectedRow.status === 'ENABLE' ? '分类模板已禁用' : '分类模板已启用',
      );
      actionRef.current?.reload();
      return true;
    } catch (error) {
      const errorMessage =
        error instanceof Error ? error.message : String(error);
      void messageApi.error(`分类模板状态更新失败：${errorMessage}`);
      return false;
    } finally {
      setStatusUpdatingId(undefined);
    }
  };

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<API.CategoryTemplateVO, API.PageResultCategoryTemplateVO>
        headerTitle={'分类模板管理'}
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        pagination={{
          pageSize: 10,
        }}
        toolBarRender={() => [
          <CreateModal
            key="create"
            trigger={<Button type="primary">新增模板</Button>}
            onOk={() => actionRef.current?.reload()}
          />,
        ]}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;
          const { data, code } = await getCategoryTemplateVoPage({
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.getCategoryTemplateVOPageParams);
          return {
            success: code === 0,
            data: data?.records || [],
            total: Number(data?.totalRow) || 0,
          };
        }}
        columns={columns}
      />
      <DetailModal
        open={showDetail}
        categoryTemplateId={currentRow?.id}
        onClose={() => {
          setShowDetail(false);
          setCurrentRow(undefined);
        }}
      />
    </PageContainer>
  );
};

export default CategoryTemplateTableList;
