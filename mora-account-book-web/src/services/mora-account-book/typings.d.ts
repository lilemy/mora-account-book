declare namespace API {
  type AccountBookCategoryVO = {
    /** 账本分类 ID */
    id?: number;
    /** 账本 ID */
    bookId?: number;
    /** 父分类 ID，一级分类为 0 */
    parentId?: number;
    /** 分类名称 */
    categoryName?: string;
    /** 分类类型 */
    categoryType?: "INCOME" | "EXPENSE";
    /** 分类层级 */
    categoryLevel?: "LEVEL_1" | "LEVEL_2";
    /** 分类图标标识 */
    icon?: string;
    /** 分类颜色 */
    color?: string;
    /** 排序值，值越小越靠前 */
    sortOrder?: number;
    /** 子分类列表 */
    children?: AccountBookCategoryVO[];
  };

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
    /** 账本分类列表 */
    categoryList?: AccountBookCategoryVO[];
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

  type AccountBookUserVO = {
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
    /** 成员角色 */
    memberRole?: "OWNER" | "ADMIN" | "MEMBER";
    /** 是否为用户默认账本 */
    isDefault?: boolean;
    /** 加入账本时间 */
    joinTime?: string;
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

  type BaseResponseCategoryTemplateVO = {
    /** 状态码 */
    code?: number;
    data?: CategoryTemplateVO;
    /** 提示信息 */
    message?: string;
  };

  type BaseResponseLoginUserVO = {
    /** 状态码 */
    code?: number;
    data?: LoginUserVO;
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

  type BaseResponsePageResultAccountBookUserVO = {
    /** 状态码 */
    code?: number;
    data?: PageResultAccountBookUserVO;
    /** 提示信息 */
    message?: string;
  };

  type BaseResponsePageResultCategoryTemplateVO = {
    /** 状态码 */
    code?: number;
    data?: PageResultCategoryTemplateVO;
    /** 提示信息 */
    message?: string;
  };

  type BaseResponsePageResultUserAccountManageVO = {
    /** 状态码 */
    code?: number;
    data?: PageResultUserAccountManageVO;
    /** 提示信息 */
    message?: string;
  };

  type BaseResponsePageResultUserAccountVO = {
    /** 状态码 */
    code?: number;
    data?: PageResultUserAccountVO;
    /** 提示信息 */
    message?: string;
  };

  type BaseResponsePageResultUserManageVO = {
    /** 状态码 */
    code?: number;
    data?: PageResultUserManageVO;
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

  type BaseResponseUserAccountManageVO = {
    /** 状态码 */
    code?: number;
    data?: UserAccountManageVO;
    /** 提示信息 */
    message?: string;
  };

  type BaseResponseUserAccountVO = {
    /** 状态码 */
    code?: number;
    data?: UserAccountVO;
    /** 提示信息 */
    message?: string;
  };

  type BaseResponseUserManageVO = {
    /** 状态码 */
    code?: number;
    data?: UserManageVO;
    /** 提示信息 */
    message?: string;
  };

  type CategoryTemplateItemVO = {
    /** 分类模板项 ID */
    id?: number;
    /** 分类模板 ID */
    templateId?: number;
    /** 父分类模板项 ID，一级分类为 0 */
    parentId?: number;
    /** 分类名称 */
    categoryName?: string;
    /** 分类类型 */
    categoryType?: "INCOME" | "EXPENSE";
    /** 分类层级 */
    categoryLevel?: "LEVEL_1" | "LEVEL_2";
    /** 分类图标标识 */
    icon?: string;
    /** 分类颜色 */
    color?: string;
    /** 排序值，值越小越靠前 */
    sortOrder?: number;
    /** 模板项状态 */
    status?: "ENABLE" | "DISABLE";
    /** 子分类模板项 */
    children?: CategoryTemplateItemVO[];
  };

  type CategoryTemplateVO = {
    /** 分类模板 ID */
    id?: number;
    /** 模板编码 */
    templateCode?: string;
    /** 模板名称 */
    templateName?: string;
    /** 是否为默认模板 */
    isDefault?: boolean;
    /** 模板状态 */
    status?: "ENABLE" | "DISABLE";
    /** 创建时间 */
    createTime?: string;
    /** 更新时间 */
    updateTime?: string;
    /** 分类模板项 */
    itemList?: CategoryTemplateItemVO[];
  };

  type changeCategoryTemplateItemStatusParams = {
    id: number;
  };

  type changeCategoryTemplateStatusParams = {
    id: number;
  };

  type createCategoryTemplateItemParams = {
    /** 分类模板 ID */
    templateId: string;
    /** 父分类模板项 ID，一级分类为 0 */
    parentId?: string;
    /** 分类名称 */
    categoryName: string;
    /** 分类类型 */
    categoryType: string;
    /** 分类图标标识 */
    icon?: string;
    /** 分类颜色 */
    color?: string;
    /** 排序值，值越小越靠前 */
    sortOrder?: string;
  };

  type createCategoryTemplateParams = {
    /** 模板编码 */
    templateCode: string;
    /** 模板名称 */
    templateName: string;
    /** 是否为默认模板 */
    isDefault?: string;
  };

  type deleteAccountBookParams = {
    id: number;
  };

  type deleteCategoryTemplateItemParams = {
    id: number;
  };

  type deleteCategoryTemplateParams = {
    id: number;
  };

  type deleteUserParams = {
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

  type getAccountBookPageParams = {
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

  type getCategoryTemplateVOPageParams = {
    /** 模板编码 */
    templateCode?: string;
    /** 模板名称 */
    templateName?: string;
    /** 是否为默认模板 */
    isDefault?: string;
    /** 模板状态 */
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

  type getCategoryTemplateVOParams = {
    id: number;
  };

  type getUserAccountManageVOByIdParams = {
    id: number;
  };

  type getUserAccountManageVOPageParams = {
    /** 所属用户 ID */
    userId?: string;
    /** 账户名称 */
    accountName?: string;
    /** 账户类型 */
    accountType?: string;
    /** 是否为用户默认账户 */
    isDefault?: string;
    /** 分页大小 */
    pageSize?: string;
    /** 当前页数 */
    pageNum?: string;
    /** 排序字段 */
    sortField?: string;
    /** 排序顺序 */
    sortOrder?: string;
  };

  type getUserAccountVOByIdParams = {
    id: number;
  };

  type getUserAccountVOPageParams = {
    /** 所属用户 ID */
    userId?: string;
    /** 账户名称 */
    accountName?: string;
    /** 账户类型 */
    accountType?: string;
    /** 是否为用户默认账户 */
    isDefault?: string;
    /** 分页大小 */
    pageSize?: string;
    /** 当前页数 */
    pageNum?: string;
    /** 排序字段 */
    sortField?: string;
    /** 排序顺序 */
    sortOrder?: string;
  };

  type getUserByIdParams = {
    id: number;
  };

  type getUserManageVOPageParams = {
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

  type LoginUserVO = {
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

  type PageResultAccountBookUserVO = {
    /** 当前页数据 */
    records?: AccountBookUserVO[];
    /** 当前页码 */
    pageNum?: number;
    /** 每页数据数量 */
    pageSize?: number;
    /** 总页数 */
    totalPage?: number;
    /** 总数据数量 */
    totalRow?: number;
  };

  type PageResultCategoryTemplateVO = {
    /** 当前页数据 */
    records?: CategoryTemplateVO[];
    /** 当前页码 */
    pageNum?: number;
    /** 每页数据数量 */
    pageSize?: number;
    /** 总页数 */
    totalPage?: number;
    /** 总数据数量 */
    totalRow?: number;
  };

  type PageResultUserAccountManageVO = {
    /** 当前页数据 */
    records?: UserAccountManageVO[];
    /** 当前页码 */
    pageNum?: number;
    /** 每页数据数量 */
    pageSize?: number;
    /** 总页数 */
    totalPage?: number;
    /** 总数据数量 */
    totalRow?: number;
  };

  type PageResultUserAccountVO = {
    /** 当前页数据 */
    records?: UserAccountVO[];
    /** 当前页码 */
    pageNum?: number;
    /** 每页数据数量 */
    pageSize?: number;
    /** 总页数 */
    totalPage?: number;
    /** 总数据数量 */
    totalRow?: number;
  };

  type PageResultUserManageVO = {
    /** 当前页数据 */
    records?: UserManageVO[];
    /** 当前页码 */
    pageNum?: number;
    /** 每页数据数量 */
    pageSize?: number;
    /** 总页数 */
    totalPage?: number;
    /** 总数据数量 */
    totalRow?: number;
  };

  type updateCategoryTemplateItemParams = {
    /** 分类模板项 ID */
    id: string;
    /** 分类模板 ID */
    templateId?: string;
    /** 父分类模板项 ID，一级分类为 0 */
    parentId?: string;
    /** 分类名称 */
    categoryName?: string;
    /** 分类类型 */
    categoryType?: string;
    /** 分类图标标识 */
    icon?: string;
    /** 分类颜色 */
    color?: string;
    /** 排序值，值越小越靠前 */
    sortOrder?: string;
  };

  type updateCategoryTemplateParams = {
    /** 分类模板 ID */
    id: string;
    /** 模板编码 */
    templateCode?: string;
    /** 模板名称 */
    templateName?: string;
    /** 是否为默认模板 */
    isDefault?: string;
  };

  type uploadAvatarByAdminParams = {
    id: number;
    /** 头像文件 */
    file: Blob;
  };

  type UserAccountCreateRequest = {
    /** 账户名称 */
    accountName: string;
    /** 账户类型 */
    accountType: "CAPITAL" | "CREDIT" | "RECHARGE" | "INVEST" | "LOAN";
    /** 银行名称 */
    bankName?: string;
    /** 当前余额或欠款余额 */
    currentBalance: number;
    /** 信用额度 */
    creditLimit?: number;
    /** 信用账户出账日 */
    creditStatementDay?: number;
    /** 信用账户还款日 */
    creditRepaymentDay?: number;
    /** 借贷人姓名 */
    loanUser?: string;
    /** 借贷方向 */
    loanDirection?: "LEND" | "BORROW";
    /** 借贷发生日期 */
    loanDate?: string;
    /** 计划还款日期 */
    repaymentDate?: string;
    /** 银行卡或信用卡卡号后四位 */
    accountNoTail?: string;
    /** 账户图标标识 */
    icon?: string;
    /** 是否为用户默认账户 */
    isDefault?: boolean;
    /** 备注 */
    remark?: string;
  };

  type UserAccountDeleteRequest = {
    /** 账户 ID */
    id: number;
    /** 是否保留账单 */
    isKeepBill: boolean;
  };

  type UserAccountManageVO = {
    /** 账户 ID */
    id?: number;
    /** 所属用户 ID */
    userId?: number;
    /** 所属用户手机号 */
    userPhone?: string;
    /** 所属用户昵称 */
    userName?: string;
    /** 账户名称 */
    accountName?: string;
    /** 账户类型 */
    accountType?: "CAPITAL" | "CREDIT" | "RECHARGE" | "INVEST" | "LOAN";
    /** 银行名称 */
    bankName?: string;
    /** 信用账户出账日 */
    creditStatementDay?: number;
    /** 信用账户还款日 */
    creditRepaymentDay?: number;
    /** 借贷人姓名 */
    loanUser?: string;
    /** 借贷方向 */
    loanDirection?: "LEND" | "BORROW";
    /** 借贷发生日期 */
    loanDate?: string;
    /** 计划还款日期 */
    repaymentDate?: string;
    /** 银行卡或信用卡卡号后四位 */
    accountNoTail?: string;
    /** 账户图标标识 */
    icon?: string;
    /** 是否为用户默认账户 */
    isDefault?: boolean;
    /** 备注 */
    remark?: string;
    /** 创建时间 */
    createTime?: string;
    /** 更新时间 */
    updateTime?: string;
  };

  type UserAccountUpdateRequest = {
    /** 账户 ID */
    id: number;
    /** 账户名称 */
    accountName?: string;
    /** 银行名称 */
    bankName?: string;
    /** 信用额度，仅信用账户使用 */
    creditLimit?: number;
    /** 信用账户出账日 */
    creditStatementDay?: number;
    /** 信用账户还款日 */
    creditRepaymentDay?: number;
    /** 借贷人姓名 */
    loanUser?: string;
    /** 借贷方向，仅借贷账户使用 */
    loanDirection?: "LEND" | "BORROW";
    /** 借贷发生日期，仅借贷账户使用 */
    loanDate?: string;
    /** 计划还款日期，仅借贷账户使用 */
    repaymentDate?: string;
    /** 银行卡或信用卡卡号后四位 */
    accountNoTail?: string;
    /** 账户图标标识 */
    icon?: string;
    /** 是否为用户默认账户 */
    isDefault?: boolean;
    /** 备注 */
    remark?: string;
  };

  type UserAccountVO = {
    /** 账户 ID */
    id?: number;
    /** 所属用户 ID */
    userId?: number;
    /** 账户名称 */
    accountName?: string;
    /** 账户类型 */
    accountType?: "CAPITAL" | "CREDIT" | "RECHARGE" | "INVEST" | "LOAN";
    /** 银行名称 */
    bankName?: string;
    /** 当前余额或欠款余额 */
    currentBalance?: number;
    /** 信用额度 */
    creditLimit?: number;
    /** 信用账户出账日 */
    creditStatementDay?: number;
    /** 信用账户还款日 */
    creditRepaymentDay?: number;
    /** 借贷人姓名 */
    loanUser?: string;
    /** 借贷方向 */
    loanDirection?: "LEND" | "BORROW";
    /** 借贷发生日期 */
    loanDate?: string;
    /** 计划还款日期 */
    repaymentDate?: string;
    /** 银行卡或信用卡卡号后四位 */
    accountNoTail?: string;
    /** 账户图标标识 */
    icon?: string;
    /** 是否为用户默认账户 */
    isDefault?: boolean;
    /** 备注 */
    remark?: string;
    /** 创建时间 */
    createTime?: string;
    /** 更新时间 */
    updateTime?: string;
  };

  type UserLoginRequest = {
    /** 用户手机号 */
    userPhone: string;
    /** 用户密码 */
    userPassword: string;
  };

  type UserManageVO = {
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
