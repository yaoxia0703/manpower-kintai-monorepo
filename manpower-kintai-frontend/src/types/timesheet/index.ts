export type AttendanceType = 'OFFICE' | 'REMOTE' | 'BUSINESS_TRIP' | 'HOLIDAY_WORK'

export interface TimesheetDay {
  workDate: string
  dayOfWeek: string
  weekend: boolean
  holiday: boolean
  attendanceType: AttendanceType | null
  clockIn: string | null
  clockOut: string | null
  breakMinutes: number | null
  workMinutes: number | null
  overtimeMinutes: number | null
  remark: string | null
  status: number | null
  recordId: number | null
}

export interface TimesheetMonth {
  year: number
  month: number
  days: TimesheetDay[]
  workDays: number
  totalWorkMinutes: number
  totalOvertimeMinutes: number
}

export interface TimesheetSaveRequest {
  workDate: string
  attendanceType: AttendanceType | null
  clockIn: string | null
  clockOut: string | null
  breakMinutes: number | null
  remark: string | null
}
