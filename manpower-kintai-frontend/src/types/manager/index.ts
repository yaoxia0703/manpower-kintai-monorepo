import type { CommonStatus } from '@/types/enums'

export interface SubordinateEmployee {
  employeeId: number
  employeeCode: string
  displayName: string
  email: string
  companyId: number
  nodeId: number
  nodeName?: string
  gradeId: number
  gradeName?: string
}

export interface SubordinateQueryParams {
  keyword?: string
  nodeId?: number
  gradeId?: number
  status?: CommonStatus
  page?: number
  size?: number
}

export interface ManagerOrgNodeOption {
  id: number
  parentId: number | null
  name: string
  code?: string
  typeCode?: string
  level: number
  children?: ManagerOrgNodeOption[]
}

export interface ManagerGradeOption {
  id: number
  name: string
  code?: string
  gradeLevel?: string
}

export interface SubordinateFilterOptionsResponse {
  nodes: ManagerOrgNodeOption[]
  grades: ManagerGradeOption[]
}
