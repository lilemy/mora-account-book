// @ts-ignore
/* eslint-disable */
import { request } from "@umijs/max";

/** 更新分类模板 PUT /categoryTemplate/admin */
export async function updateCategoryTemplate(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateCategoryTemplateParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>("/categoryTemplate/admin", {
    method: "PUT",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 创建分类模板 POST /categoryTemplate/admin */
export async function createCategoryTemplate(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.createCategoryTemplateParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>("/categoryTemplate/admin", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取分类模板详情 GET /categoryTemplate/admin/${param0} */
export async function getCategoryTemplateVo(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCategoryTemplateVOParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseCategoryTemplateVO>(
    `/categoryTemplate/admin/${param0}`,
    {
      method: "GET",
      params: { ...queryParams },
      ...(options || {}),
    }
  );
}

/** 删除分类模板 DELETE /categoryTemplate/admin/${param0} */
export async function deleteCategoryTemplate(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteCategoryTemplateParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseBoolean>(`/categoryTemplate/admin/${param0}`, {
    method: "DELETE",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 更新分类模板项 PUT /categoryTemplate/admin/item */
export async function updateCategoryTemplateItem(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateCategoryTemplateItemParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>("/categoryTemplate/admin/item", {
    method: "PUT",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 创建分类模板项 POST /categoryTemplate/admin/item */
export async function createCategoryTemplateItem(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.createCategoryTemplateItemParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>("/categoryTemplate/admin/item", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 删除分类模板项 DELETE /categoryTemplate/admin/item/${param0} */
export async function deleteCategoryTemplateItem(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteCategoryTemplateItemParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseBoolean>(
    `/categoryTemplate/admin/item/${param0}`,
    {
      method: "DELETE",
      params: { ...queryParams },
      ...(options || {}),
    }
  );
}

/** 切换分类模板项状态 PUT /categoryTemplate/admin/item/status/${param0} */
export async function changeCategoryTemplateItemStatus(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.changeCategoryTemplateItemStatusParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseBoolean>(
    `/categoryTemplate/admin/item/status/${param0}`,
    {
      method: "PUT",
      params: { ...queryParams },
      ...(options || {}),
    }
  );
}

/** 获取分类模板分页列表 GET /categoryTemplate/admin/page */
export async function getCategoryTemplateVoPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCategoryTemplateVOPageParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageResultCategoryTemplateVO>(
    "/categoryTemplate/admin/page",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 切换分类模板状态 PUT /categoryTemplate/admin/status/${param0} */
export async function changeCategoryTemplateStatus(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.changeCategoryTemplateStatusParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseBoolean>(
    `/categoryTemplate/admin/status/${param0}`,
    {
      method: "PUT",
      params: { ...queryParams },
      ...(options || {}),
    }
  );
}
