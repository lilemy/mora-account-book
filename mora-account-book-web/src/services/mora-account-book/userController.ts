// @ts-ignore
/* eslint-disable */
import { request } from "@umijs/max";

/** 更新用户信息（管理员） PUT /user/admin */
export async function updateUser(
  body: API.UserUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>("/user/admin", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取用户信息（管理员） GET /user/admin/${param0} */
export async function getUserById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseUserManageVO>(`/user/admin/${param0}`, {
    method: "GET",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 删除用户信息（管理员） DELETE /user/admin/${param0} */
export async function deleteUser(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteUserParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseBoolean>(`/user/admin/${param0}`, {
    method: "DELETE",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 分页获取用户信息（管理员） GET /user/admin/page */
export async function getUserManageVoPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserManageVOPageParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageResultUserManageVO>("/user/admin/page", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 上传并修改用户头像（管理员） POST /user/admin/uploadAvatar */
export async function uploadAvatarByAdmin(
// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
params: API.uploadAvatarByAdminParams,
options?: { [key: string]: any }
) {
  const { id, file, ...queryParams } = params;
  const formData = new FormData();
  formData.append("file", file);

  return request<API.BaseResponseBoolean>("/user/admin/uploadAvatar", {
    method: "POST",
    params: {
      id,
      ...queryParams,
    },
    data: formData,
    ...(options || {}),
  });
}

/** 获取当前登录用户 GET /user/getLoginUser */
export async function getLoginUser(options?: { [key: string]: any }) {
  return request<API.BaseResponseLoginUserVO>("/user/getLoginUser", {
    method: "GET",
    ...(options || {}),
  });
}

/** 用户登录 POST /user/login */
export async function userLogin(
  body: API.UserLoginRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLoginUserVO>("/user/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 用户登出 POST /user/logout */
export async function userLogout(options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>("/user/logout", {
    method: "POST",
    ...(options || {}),
  });
}

/** 用户注册 POST /user/register */
export async function userRegister(
  body: API.UserRegisterRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong>("/user/register", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 上传并修改用户头像 POST /user/uploadAvatar */
export async function uploadAvatar(options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>("/user/uploadAvatar", {
    method: "POST",
    ...(options || {}),
  });
}
