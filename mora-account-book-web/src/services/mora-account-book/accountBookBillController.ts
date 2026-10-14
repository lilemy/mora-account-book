// @ts-ignore
/* eslint-disable */
import { request } from "@umijs/max";

/** 获取账本流水 GET /accountBookBill/${param0} */
export async function getUserAccountBookBillVoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserAccountBookBillVOByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseAccountBookBillVO>(
    `/accountBookBill/${param0}`,
    {
      method: "GET",
      params: { ...queryParams },
      ...(options || {}),
    }
  );
}

/** 获取账本流水（仅管理员） GET /accountBookBill/admin/${param0} */
export async function getUserAccountBookBillManageVoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserAccountBookBillManageVOByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseAccountBookBillManageVO>(
    `/accountBookBill/admin/${param0}`,
    {
      method: "GET",
      params: { ...queryParams },
      ...(options || {}),
    }
  );
}

/** 获取账本流水分页列表（仅管理员） GET /accountBookBill/admin/page */
export async function getUserAccountBookBillManageVoPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserAccountBookBillManageVOPageParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageResultAccountBookBillManageVO>(
    "/accountBookBill/admin/page",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 创建账本流水 POST /accountBookBill/create */
export async function createAccountBookBill(
  body: API.AccountBookBillCreateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong>("/accountBookBill/create", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除账本流水 DELETE /accountBookBill/delete/${param0} */
export async function deleteAccountBookBill(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteAccountBookBillParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseBoolean>(`/accountBookBill/delete/${param0}`, {
    method: "DELETE",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 根据月份获取账本流水列表 GET /accountBookBill/month/list */
export async function getUserAccountBookBillVoListByMonth(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserAccountBookBillVOListByMonthParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListAccountBookBillMonthListVO>(
    "/accountBookBill/month/list",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 根据年份获取账本流水汇总 GET /accountBookBill/month/total */
export async function getUserAccountBookBillTotalByMonth(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserAccountBookBillTotalByMonthParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListAccountBookBillMonthTotalVO>(
    "/accountBookBill/month/total",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 获取账本流水分页列表 GET /accountBookBill/page */
export async function getUserAccountBookBillVoPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserAccountBookBillVOPageParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageResultAccountBookBillVO>(
    "/accountBookBill/page",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 更新账本流水 PUT /accountBookBill/update */
export async function updateAccountBookBill(
  body: API.AccountBookBillUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>("/accountBookBill/update", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
