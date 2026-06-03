import type { CommonStatus } from '@/types/enums'

export interface PageResult<T> {
  records: T[]
  total: number
  page: number
  size: number
}

export interface SystemMenu {
  id: number
  parentId?: number | null
  name: string
  code: string
  path?: string | null
  component?: string | null
  icon?: string | null
  type: number
  sort: number
  visible: number
  status: CommonStatus
  children?: SystemMenu[]
}

export interface SystemPermission {
  id: number
  menuId?: number | null
  code: string
  name: string
  method: string
  path: string
  remark?: string | null
  sort: number
  status: CommonStatus
}

export interface SystemRole {
  id: number
  companyId?: number | null
  code: string
  name: string
  remark?: string | null
  sort: number
  status: CommonStatus
}

export interface RoleAuthorization {
  menus: SystemMenu[]
  permissions: SystemPermission[]
  selectedMenuIds: number[]
  selectedPermissionIds: number[]
}
