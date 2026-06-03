export interface MenuCreateRequest {
  parentId?: number | null
  name: string
  code: string
  path?: string | null
  component?: string | null
  icon?: string | null
  type: number
  sort: number
  visible?: number
}

export interface MenuUpdateRequest extends MenuCreateRequest {}

export interface PermissionCreateRequest {
  menuId?: number | null
  code: string
  name: string
  method: string
  path: string
  remark?: string | null
  sort: number
}

export interface PermissionUpdateRequest extends PermissionCreateRequest {}

export interface RoleCreateRequest {
  companyId?: number | null
  code: string
  name: string
  remark?: string | null
  sort: number
}

export interface RoleUpdateRequest extends RoleCreateRequest {}

export interface RoleAuthorizationSaveRequest {
  menuIds: number[]
  permissionIds: number[]
}
