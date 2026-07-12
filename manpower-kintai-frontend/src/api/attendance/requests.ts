import request from '@/api/common/request'
import type { ApiResponse } from '@/types/common'
import type { AttRequest, AttRequestPayload } from '@/types/attendance'

export function fetchAttendanceRequests() {
  return request.get<ApiResponse<AttRequest[]>>('/employee/att/requests')
}

export function createAttendanceRequest(payload: AttRequestPayload) {
  return request.post<ApiResponse<AttRequest>>('/employee/att/requests', payload)
}

export function updateAttendanceRequest(requestId: number, payload: AttRequestPayload) {
  return request.put<ApiResponse<AttRequest>>(`/employee/att/requests/${requestId}`, payload)
}

export function cancelAttendanceRequest(requestId: number) {
  return request.post<ApiResponse<void>>(`/employee/att/requests/${requestId}/cancel`)
}
