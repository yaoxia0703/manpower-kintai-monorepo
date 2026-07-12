import request from '@/api/common/request'
import type { ApiResponse } from '@/types/common'
import type { PageResult, SysNotification } from '@/types/system'

export function fetchUnreadNotificationCount() {
  return request.get<ApiResponse<number>>('/employee/notifications/unread-count')
}

export function fetchUnreadNotifications(params: { page?: number; size?: number } = {}) {
  return request.get<ApiResponse<PageResult<SysNotification>>>('/employee/notifications/unread', {
    params,
  })
}

export function markNotificationsAsRead(ids: number[]) {
  return request.put<ApiResponse<void>>('/employee/notifications/read', { ids })
}
