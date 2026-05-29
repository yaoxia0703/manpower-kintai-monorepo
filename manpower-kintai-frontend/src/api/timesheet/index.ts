import { request } from '@/api/common'
import type { ApiResponse } from '@/types/common'
import type { TimesheetMonth, TimesheetSaveRequest } from '@/types/timesheet'

export function fetchMonthlyTimesheet(year: number, month: number) {
  return request.get<ApiResponse<TimesheetMonth>>('/employee/att/timesheet', {
    params: { year, month },
  })
}

export function saveTimesheetRecord(data: TimesheetSaveRequest) {
  return request.put<ApiResponse<void>>('/employee/att/timesheet', data)
}

export function deleteTimesheetRecord(recordId: number) {
  return request.delete<ApiResponse<void>>(`/employee/att/timesheet/${recordId}`)
}
