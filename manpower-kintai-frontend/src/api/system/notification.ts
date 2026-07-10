import request from '@/api/common/request'
import type { ApiResponse } from '@/types/common'
import type { PageResult, SysNotification } from '@/types/system'

export function fetchUnreadNotificationCount() {
  return request.get<ApiResponse<number>>('/admin/sys/notification/unread-count')
}

export function fetchUnreadNotifications(params: { page?: number; size?: number } = {}) {
  return request.get<ApiResponse<PageResult<SysNotification>>>('/admin/sys/notification/unread', {
    params,
  })
}
