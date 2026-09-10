// @ts-ignore
/* eslint-disable */
import { request } from "@umijs/max";

/** 获取账本流水（仅管理员） GET /accountBookBill/admin/${param0} */
export async function getUserAccountManageVoById1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserAccountManageVOById1Params,
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
export async function getUserAccountManageVoPage1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserAccountManageVOPage1Params,
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
