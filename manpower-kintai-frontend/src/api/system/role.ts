import request from '@/api/common/request'
import type { ApiResponse } from '@/types/common'
import type {
  PageResult,
  RoleAuthorization,
  RoleAuthorizationSaveRequest,
  RoleCreateRequest,
  RoleUpdateRequest,
  SystemRole,
} from '@/types/system'

export function fetchRoles(params: { companyId?: number; page?: number; size?: number } = {}) {
  return request.get<ApiResponse<PageResult<SystemRole>>>('/admin/sys/roles', { params })
}

export function createRole(payload: RoleCreateRequest) {
  return request.post<ApiResponse<SystemRole>>('/admin/sys/roles', payload)
}

export function updateRole(id: number, payload: RoleUpdateRequest) {
  return request.put<ApiResponse<SystemRole>>(`/admin/sys/roles/${id}`, payload)
}

export function enableRole(id: number) {
  return request.put<ApiResponse<void>>(`/admin/sys/roles/${id}/enable`)
}

export function disableRole(id: number) {
  return request.put<ApiResponse<void>>(`/admin/sys/roles/${id}/disable`)
}

export function deleteRole(id: number) {
  return request.delete<ApiResponse<void>>(`/admin/sys/roles/${id}`)
}

export function fetchRoleAuthorization(id: number) {
  return request.get<ApiResponse<RoleAuthorization>>(`/admin/sys/roles/${id}/authorization`)
}

export function saveRoleAuthorization(id: number, payload: RoleAuthorizationSaveRequest) {
  return request.put<ApiResponse<void>>(`/admin/sys/roles/${id}/authorization`, payload)
}
