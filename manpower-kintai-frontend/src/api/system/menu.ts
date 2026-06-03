import request from '@/api/common/request'
import type { ApiResponse } from '@/types/common'
import type { MenuCreateRequest, MenuUpdateRequest, SystemMenu } from '@/types/system'

export function fetchMenus() {
  return request.get<ApiResponse<SystemMenu[]>>('/admin/sys/menus')
}

export function createMenu(payload: MenuCreateRequest) {
  return request.post<ApiResponse<SystemMenu>>('/admin/sys/menus', payload)
}

export function updateMenu(id: number, payload: MenuUpdateRequest) {
  return request.put<ApiResponse<SystemMenu>>(`/admin/sys/menus/${id}`, payload)
}

export function showMenu(id: number) {
  return request.put<ApiResponse<void>>(`/admin/sys/menus/${id}/show`)
}

export function hideMenu(id: number) {
  return request.put<ApiResponse<void>>(`/admin/sys/menus/${id}/hide`)
}

export function enableMenu(id: number) {
  return request.put<ApiResponse<void>>(`/admin/sys/menus/${id}/enable`)
}

export function disableMenu(id: number) {
  return request.put<ApiResponse<void>>(`/admin/sys/menus/${id}/disable`)
}

export function deleteMenu(id: number) {
  return request.delete<ApiResponse<void>>(`/admin/sys/menus/${id}`)
}
