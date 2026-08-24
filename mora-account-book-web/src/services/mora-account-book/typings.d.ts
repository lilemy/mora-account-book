declare namespace API {
  type BaseResponseBoolean = {
    /** 状态码 */
    code?: number;
    /** 数据 */
    data?: boolean;
    /** 提示信息 */
    message?: string;
  };

  type BaseResponseLong = {
    /** 状态码 */
    code?: number;
    /** 数据 */
    data?: number;
    /** 提示信息 */
    message?: string;
  };

  type BaseResponsePageResultSysUserManageVO = {
    /** 状态码 */
    code?: number;
    data?: PageResultSysUserManageVO;
    /** 提示信息 */
    message?: string;
  };

  type BaseResponseString = {
    /** 状态码 */
    code?: number;
    /** 数据 */
    data?: string;
    /** 提示信息 */
    message?: string;
  };

  type BaseResponseSysLoginUserVO = {
    /** 状态码 */
    code?: number;
    data?: SysLoginUserVO;
    /** 提示信息 */
    message?: string;
  };

  type BaseResponseSysUserManageVO = {
    /** 状态码 */
    code?: number;
    data?: SysUserManageVO;
    /** 提示信息 */
    message?: string;
  };

  type deleteSysUserParams = {
    id: number;
  };

  type getSysUserByIdParams = {
    id: number;
  };

  type getSysUserManageVOPageParams = {
    /** 用户昵称 */
    userName?: string;
    /** 用户手机号 */
    userPhone?: string;
    /** 用户邮箱 */
    userEmail?: string;
    /** 用户性别 */
    userGender?: string;
    /** 用户角色 */
    userRole?: string;
    /** 分页大小 */
    pageSize?: string;
    /** 当前页数 */
    pageNum?: string;
    /** 排序字段 */
    sortField?: string;
    /** 排序顺序 */
    sortOrder?: string;
  };

  type PageResultSysUserManageVO = {
    /** 当前页数据 */
    records?: SysUserManageVO[];
    /** 当前页码 */
    pageNum?: number;
    /** 每页数据数量 */
    pageSize?: number;
    /** 总页数 */
    totalPage?: number;
    /** 总数据数量 */
    totalRow?: number;
  };

  type SysLoginUserVO = {
    id?: number;
    /** 用户昵称 */
    userName?: string;
    /** 用户头像 */
    userAvatar?: string;
    /** 用户简介 */
    userProfile?: string;
    /** 用户手机号 */
    userPhone?: string;
    /** 用户邮箱 */
    userEmail?: string;
    /** 用户性别 */
    userGender?: "UNKNOWN" | "MALE" | "FEMALE";
    /** 用户角色 */
    userRole?: "USER" | "ADMIN";
    /** 创建时间 */
    createTime?: string;
    /** 更新时间 */
    updateTime?: string;
  };

  type SysUserManageVO = {
    id?: number;
    userName?: string;
    userAvatar?: string;
    userProfile?: string;
    userPhone?: string;
    userEmail?: string;
    userGender?: "UNKNOWN" | "MALE" | "FEMALE";
    userRole?: "USER" | "ADMIN";
    remark?: string;
    editTime?: string;
    createTime?: string;
    updateTime?: string;
  };

  type UserLoginRequest = {
    /** 用户手机号 */
    userPhone: string;
    /** 用户密码 */
    userPassword: string;
  };

  type UserRegisterRequest = {
    /** 用户手机号 */
    userPhone: string;
    /** 用户密码 */
    userPassword: string;
    /** 用户确认密码 */
    checkPassword: string;
  };

  type UserUpdateRequest = {
    /** id */
    id: number;
    /** 用户昵称 */
    userName?: string;
    /** 用户头像 */
    userAvatar?: string;
    /** 用户简介 */
    userProfile?: string;
    /** 用户手机号 */
    userPhone?: string;
    /** 用户邮箱 */
    userEmail?: string;
    /** 用户性别(0未填写 1男 2女) */
    userGender?: "UNKNOWN" | "MALE" | "FEMALE";
    /** 用户角色(0用户 1管理员) */
    userRole?: "USER" | "ADMIN";
    /** 备注 */
    remark?: string;
  };
}
