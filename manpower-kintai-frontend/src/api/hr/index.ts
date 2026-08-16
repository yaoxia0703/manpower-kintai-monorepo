import request from '@/api/common/request'
import type { ApiResponse, JoinPageResult } from '@/types/common'
import type {
  EmployeeDirectoryOptionsResponse,
  EmployeeDirectoryResponse,
  EmployeeOnboardingOptionsResponse,
  EmployeeOnboardingRequest,
  EmployeeOnboardingResponse,
  EnployeeDirectoryQueryParams,
} from '@/types/hr'

export function fetchOnboardingOptions(companyId?: number) {
  return request.get<ApiResponse<EmployeeOnboardingOptionsResponse>>('/admin/hr/onboarding/options', {
    params: { companyId },
  })
}

export function onboardEmployee(payload: EmployeeOnboardingRequest) {
  return request.post<ApiResponse<EmployeeOnboardingResponse>>('/hr/emp/employee/register', payload)
}


export function fetchEmployeeDirectory(params: EnployeeDirectoryQueryParams = {}) {
  return request.get<ApiResponse<JoinPageResult<EmployeeDirectoryResponse>>>('/hr/emp/employee', {
    params,
  })
}

export function fetchEmployeeDirectoryOptions() {
  return request.get<ApiResponse<EmployeeDirectoryOptionsResponse>>(
    '/hr/emp/employee/options',
  )
}
