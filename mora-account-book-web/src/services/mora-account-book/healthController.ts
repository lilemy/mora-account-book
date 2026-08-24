// @ts-ignore
/* eslint-disable */
import { request } from "@umijs/max";

/** 健康检查 GET /health */
export async function healthCheck(options?: { [key: string]: any }) {
  return request<API.BaseResponseString>("/health", {
    method: "GET",
    ...(options || {}),
  });
}
