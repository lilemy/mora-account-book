// @ts-ignore
/* eslint-disable */
import { request } from "@umijs/max";

/** 更新账本信息（管理员） PUT /accountBook/admin */
export async function updateAccountBook(
  body: API.AccountBookUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>("/accountBook/admin", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取账本信息（管理员） GET /accountBook/admin/${param0} */
export async function getAccountBookById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getAccountBookByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseAccountBookManageVO>(
    `/accountBook/admin/${param0}`,
    {
      method: "GET",
      params: { ...queryParams },
      ...(options || {}),
    }
  );
}

/** 删除账本信息（管理员） DELETE /accountBook/admin/${param0} */
export async function deleteAccountBook(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteAccountBookParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseBoolean>(`/accountBook/admin/${param0}`, {
    method: "DELETE",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 分页获取账本信息（管理员） GET /accountBook/admin/page */
export async function getAccountBookManageVoPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getAccountBookManageVOPageParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageResultAccountBookManageVO>(
    "/accountBook/admin/page",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
