import request from '@/api/common/request'
import type { ApiResponse } from '@/types/common'
import type {
  PageResult,
  PermissionCreateRequest,
  PermissionUpdateRequest,
  SystemPermission,
} from '@/types/system'

export function fetchPermissions(params: { page?: number; size?: number } = {}) {
  return request.get<ApiResponse<PageResult<SystemPermission>>>('/admin/sys/permissions', { params })
}

export function fetchPermissionsByMenu(menuId: number) {
  return request.get<ApiResponse<SystemPermission[]>>(`/admin/sys/permissions/by-menu/${menuId}`)
}

export function createPermission(payload: PermissionCreateRequest) {
  return request.post<ApiResponse<SystemPermission>>('/admin/sys/permissions', payload)
}

export function updatePermission(id: number, payload: PermissionUpdateRequest) {
  return request.put<ApiResponse<SystemPermission>>(`/admin/sys/permissions/${id}`, payload)
}

export function enablePermission(id: number) {
  return request.put<ApiResponse<void>>(`/admin/sys/permissions/${id}/enable`)
}

export function disablePermission(id: number) {
  return request.put<ApiResponse<void>>(`/admin/sys/permissions/${id}/disable`)
}

export function deletePermission(id: number) {
  return request.delete<ApiResponse<void>>(`/admin/sys/permissions/${id}`)
}
