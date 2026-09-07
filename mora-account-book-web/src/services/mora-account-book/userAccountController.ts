// @ts-ignore
/* eslint-disable */
import { request } from "@umijs/max";

/** 获取用户账户 GET /userAccount/${param0} */
export async function getUserAccountVoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserAccountVOByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseUserAccountVO>(`/userAccount/${param0}`, {
    method: "GET",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 获取用户账户 GET /userAccount/admin/${param0} */
export async function getUserAccountManageVoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserAccountManageVOByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseUserAccountManageVO>(
    `/userAccount/admin/${param0}`,
    {
      method: "GET",
      params: { ...queryParams },
      ...(options || {}),
    }
  );
}

/** 获取用户账户分页列表（仅管理员） GET /userAccount/admin/page */
export async function getUserAccountManageVoPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserAccountManageVOPageParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageResultUserAccountManageVO>(
    "/userAccount/admin/page",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 创建用户账户 POST /userAccount/create */
export async function createUserAccount(
  body: API.UserAccountCreateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong>("/userAccount/create", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除用户账户 DELETE /userAccount/delete */
export async function deleteUserAccount(
  body: API.UserAccountDeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>("/userAccount/delete", {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取用户账户分页列表 GET /userAccount/page */
export async function getUserAccountVoPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserAccountVOPageParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageResultUserAccountVO>("/userAccount/page", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 更新用户账户 PUT /userAccount/update */
export async function updateUserAccount(
  body: API.UserAccountUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>("/userAccount/update", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
