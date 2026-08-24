// @ts-ignore
/* eslint-disable */
import { request } from "@umijs/max";

/** 更新用户信息（管理员） PUT /sysUser/admin */
export async function updateSysUser(
  body: API.UserUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>("/sysUser/admin", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取用户信息（管理员） GET /sysUser/admin/${param0} */
export async function getSysUserById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSysUserByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseSysUserManageVO>(`/sysUser/admin/${param0}`, {
    method: "GET",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 删除用户信息（管理员） DELETE /sysUser/admin/${param0} */
export async function deleteSysUser(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteSysUserParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseBoolean>(`/sysUser/admin/${param0}`, {
    method: "DELETE",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 分页获取用户信息（管理员） GET /sysUser/admin/page */
export async function getSysUserManageVoPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSysUserManageVOPageParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageResultSysUserManageVO>(
    "/sysUser/admin/page",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 获取当前登录用户 GET /sysUser/getLoginUser */
export async function getLoginUser(options?: { [key: string]: any }) {
  return request<API.BaseResponseSysLoginUserVO>("/sysUser/getLoginUser", {
    method: "GET",
    ...(options || {}),
  });
}

/** 用户登录 POST /sysUser/login */
export async function userLogin(
  body: API.UserLoginRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseSysLoginUserVO>("/sysUser/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 用户登出 POST /sysUser/logout */
export async function userLogout(options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>("/sysUser/logout", {
    method: "POST",
    ...(options || {}),
  });
}

/** 用户注册 POST /sysUser/register */
export async function userRegister(
  body: API.UserRegisterRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong>("/sysUser/register", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
