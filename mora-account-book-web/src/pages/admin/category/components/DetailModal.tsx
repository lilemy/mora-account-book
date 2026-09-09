import {
  type ProColumns,
  ProDescriptions,
  type ProDescriptionsProps,
  ProTable,
} from '@ant-design/pro-components';
import { App, Modal, Space } from 'antd';
import React, { useEffect, useRef, useState } from 'react';
import { getCategoryTemplateVo } from '@/services/mora-account-book/categoryTemplateController';
import { getAccountBookIconConfig } from '@/utils/accountBookIcon';

type CategoryTemplateDetailModalProps = {
  categoryTemplateId?: number;
  open: boolean;
  onClose: () => void;
};

const { renderIcon } = getAccountBookIconConfig();

const categoryTemplateColumns: ProDescriptionsProps<API.CategoryTemplateVO>['columns'] =
  [
    {
      title: '模板名称',
      dataIndex: 'templateName',
    },
    {
      title: '模板编码',
      dataIndex: 'templateCode',
    },
    {
      title: '是否为默认模板',
      dataIndex: 'isDefault',
      valueEnum: {
        true: '是',
        false: '否',
      },
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
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      valueType: 'dateTime',
    },
  ];

const itemColumns: ProColumns<API.CategoryTemplateItemVO>[] = [
  {
    title: '分类名称',
    dataIndex: 'categoryName',
    key: 'categoryName',
    render: (dom, entity) => {
      return (
        <Space>
          {renderIcon(entity.icon, { style: { color: entity.color } })}
          <div>{dom}</div>
        </Space>
      );
    },
  },
  {
    title: '分类类型',
    dataIndex: 'categoryType',
    key: 'categoryType',
    valueEnum: {
      INCOME: {
        text: '收入',
        status: 'Success',
      },
      EXPENSE: {
        text: '支出',
        status: 'Error',
      },
    },
  },
  {
    title: '排序值',
    dataIndex: 'sortOrder',
    valueType: 'digit',
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
];

const DetailModal = ({
  categoryTemplateId,
  open,
  onClose,
}: CategoryTemplateDetailModalProps) => {
  const { message } = App.useApp();
  const [detail, setDetail] = useState<API.CategoryTemplateVO>();
  const [items, setItems] = useState<API.CategoryTemplateItemVO[]>([]);
  const [loading, setLoading] = useState(false);
  const fetchRef = useRef(0);

  useEffect(() => {
    if (!open || categoryTemplateId == null) {
      return;
    }

    fetchRef.current += 1;
    const fetchId = fetchRef.current;
    setDetail(undefined);
    setLoading(true);

    getCategoryTemplateVo({ id: categoryTemplateId })
      .then(({ code, data, message: responseMessage }) => {
        if (fetchId !== fetchRef.current) {
          return;
        }

        if (code !== 0 || !data) {
          void message.error(`获取模板详情失败${responseMessage}`);
          return;
        }

        setDetail(data);
        setItems(data.itemList || []);
      })
      .catch((error: any) => {
        if (fetchId !== fetchRef.current) {
          return;
        }

        setDetail(undefined);
        void message.error(`获取模板详情失败${error.message}`);
      })
      .finally(() => {
        if (fetchId === fetchRef.current) {
          setLoading(false);
        }
      });

    return () => {
      fetchRef.current += 1;
    };
  }, [categoryTemplateId, message, open]);

  return (
    <Modal
      open={open}
      title={
        detail?.templateName ? `模板详情 - ${detail.templateName}` : '模板详情'
      }
      width={900}
      footer={null}
      loading={loading}
      destroyOnHidden
      onCancel={onClose}
    >
      {detail ? (
        <div style={{ display: 'grid', gap: 24, minWidth: 0 }}>
          <section style={{ minWidth: 0 }}>
            <h3 style={{ fontSize: 16, margin: '0 0 16px' }}>模板信息</h3>
            <ProDescriptions<API.CategoryTemplateVO>
              bordered
              column={2}
              size="small"
              dataSource={detail}
              columns={categoryTemplateColumns}
            />
          </section>

          <section style={{ minWidth: 0 }}>
            <h3 style={{ fontSize: 16, margin: '0 0 16px' }}>
              模板分类项（{items.length}）
            </h3>
            <ProTable<API.CategoryTemplateItemVO>
              rowKey="id"
              columns={itemColumns}
              dataSource={items}
              pagination={false}
              options={false}
              search={false}
            />
          </section>
        </div>
      ) : (
        !loading && (
          <div style={{ padding: 32, textAlign: 'center' }}>暂无详情数据</div>
        )
      )}
    </Modal>
  );
};

export default DetailModal;
