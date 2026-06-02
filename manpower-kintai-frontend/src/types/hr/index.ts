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
