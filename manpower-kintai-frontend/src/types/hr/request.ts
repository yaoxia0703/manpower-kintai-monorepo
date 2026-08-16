import type { CommonStatus } from "../enums"

export interface EmployeeOnboardingRequest {
  companyId: number
  employeeCode: string
  lastName: string
  firstName: string
  email: string
  hireDate: string
  nodeId: number
  gradeId: number
  roleIds: number[]
  username: string
  password: string
  lastNameKana?: string
  firstNameKana?: string
  phone?: string
  gender?: number
}


export interface EnployeeDirectoryQueryParams {
  keyword?: string
  nodeId?: number
  gradeId?: number
  status?: CommonStatus
  page?: number
  size?: number
}
