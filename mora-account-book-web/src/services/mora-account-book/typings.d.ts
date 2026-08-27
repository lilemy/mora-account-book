declare namespace API {
  type AccountBookManageMemberVO = {
    /** 账本成员 ID */
    id?: number;
    /** 账本 ID */
    bookId?: number;
    /** 用户 ID */
    userId?: number;
    /** 用户手机号 */
    userPhone?: string;
    /** 用户名称 */
    userName?: string;
    /** 成员角色 */
    memberRole?: "OWNER" | "ADMIN" | "MEMBER";
    /** 是否为用户默认账本 */
    isDefault?: boolean;
    /** 成员状态 */
    status?: "NORMAL" | "REMOVED";
    /** 加入账本时间 */
    joinTime?: string;
  };

  type AccountBookManageVO = {
    /** 账本 ID */
    id?: number;
    /** 账本所有者用户 ID */
    ownerId?: number;
    /** 账本所有者用户手机号 */
    ownerPhone?: string;
    /** 账本所有者用户昵称 */
    ownerName?: string;
    /** 账本名称 */
    bookName?: string;
    /** 账本类型 */
    bookType?: "PERSONAL" | "SHARE";
    /** 账本图标标识 */
    icon?: string;
    /** 账本主题色 */
    color?: string;
    /** 账本说明 */
    description?: string;
    /** 账本状态 */
    status?: "NORMAL" | "ARCHIVED";
    /** 创建时间 */
    createTime?: string;
    /** 更新时间 */
    updateTime?: string;
    /** 账本成员列表 */
    memberList?: AccountBookManageMemberVO[];
  };

  type AccountBookUpdateRequest = {
    /** 账本 ID */
    id: number;
    /** 账本名称 */
    bookName?: string;
    /** 账本图标标识 */
    icon?: string;
    /** 账本主题色 */
    color?: string;
    /** 账本说明 */
    description?: string;
  };

  type BaseResponseAccountBookManageVO = {
    /** 状态码 */
    code?: number;
    data?: AccountBookManageVO;
    /** 提示信息 */
    message?: string;
  };

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

  type BaseResponsePageResultAccountBookManageVO = {
    /** 状态码 */
    code?: number;
    data?: PageResultAccountBookManageVO;
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

  type deleteAccountBookParams = {
    id: number;
  };

  type deleteSysUserParams = {
    id: number;
  };

  type getAccountBookByIdParams = {
    id: number;
  };

  type getAccountBookManageVOPageParams = {
    /** 账本所有者用户 ID */
    ownerId?: string;
    /** 账本名称 */
    bookName?: string;
    /** 账本类型 */
    bookType?: string;
    /** 账本状态 */
    status?: string;
    /** 分页大小 */
    pageSize?: string;
    /** 当前页数 */
    pageNum?: string;
    /** 排序字段 */
    sortField?: string;
    /** 排序顺序 */
    sortOrder?: string;
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

  type PageResultAccountBookManageVO = {
    /** 当前页数据 */
    records?: AccountBookManageVO[];
    /** 当前页码 */
    pageNum?: number;
    /** 每页数据数量 */
    pageSize?: number;
    /** 总页数 */
    totalPage?: number;
    /** 总数据数量 */
    totalRow?: number;
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
    /** id */
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
    /** 用户性别(0未填写 1男 2女) */
    userGender?: "UNKNOWN" | "MALE" | "FEMALE";
    /** 用户角色(0用户 1管理员) */
    userRole?: "USER" | "ADMIN";
    /** 备注 */
    remark?: string;
    /** 编辑时间 */
    editTime?: string;
    /** 创建时间 */
    createTime?: string;
    /** 更新时间 */
    updateTime?: string;
  };

  type uploadAvatarByAdminParams = {
    id: number;
    /** 头像文件 */
    file: Blob;
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
