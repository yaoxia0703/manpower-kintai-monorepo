import type { CurrentMenu } from '@/types/menu'

export interface LoginResponse {
  token: string
  employeeId: number
  accountId: number
  displayName: string
  email: string
}

export interface CurrentUser {
  employeeId: number
  accountId: number
  companyId: number
  employeeCode: string
  displayName: string
  email: string
}

export interface CurrentUserResponse {
  user: CurrentUser
  roles: string[]
  permissions: string[]
  menus: CurrentMenu[]
}
