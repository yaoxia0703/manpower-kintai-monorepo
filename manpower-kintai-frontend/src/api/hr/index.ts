import request from '@/api/common/request'
import type { ApiResponse } from '@/types/common'
import type {
  EmployeeOnboardingOptionsResponse,
  EmployeeOnboardingRequest,
  EmployeeOnboardingResponse,
} from '@/types/hr'

export function fetchOnboardingOptions(companyId?: number) {
  return request.get<ApiResponse<EmployeeOnboardingOptionsResponse>>('/admin/hr/onboarding/options', {
    params: { companyId },
  })
}

export function onboardEmployee(payload: EmployeeOnboardingRequest) {
  return request.post<ApiResponse<EmployeeOnboardingResponse>>('/admin/hr/onboarding/employees', payload)
}
