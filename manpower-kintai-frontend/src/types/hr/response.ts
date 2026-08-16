import type { ManagerGradeOption, ManagerOrgNodeOption } from "../manager"

export interface CompanyOption {
  id: number
  parentId: number | null
  name: string
  companyCode: string
}

export interface NodeOption {
  id: number
  parentId: number | null
  name: string
  code: string
  typeCode: string
  level: number
  children?: NodeOption[]
}

export interface GradeOption {
  id: number
  name: string
  code: string
  gradeLevel: string
}

export interface RoleOption {
  id: number
  code: string
  name: string
}

export interface EmployeeOnboardingOptionsResponse {
  selectedCompanyId: number
  companies: CompanyOption[]
  nodes: NodeOption[]
  grades: GradeOption[]
  roles: RoleOption[]
}

export interface EmployeeOnboardingResponse {
  employeeId: number
  accountId: number
  positionId: number
  employeeCode: string
  displayName: string
  email: string
}


export interface EmployeeDirectoryResponse {
  employeeId: number
  employeeCode: string
  displayName: string
  displayNameKana: string
  email: string
  companyId: number,
  companyName:string,
  nodeId:number,
  nodeName:string,
  gradeId:number,
  gradeName:string,
  phone:string,
  gender:number
}

export interface EmployeeDirectoryOptionsResponse {
  nodes: ManagerOrgNodeOption[]
  grades: ManagerGradeOption[]
}
