export type ApprovalStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'CANCELLED'

export type RequestType =
  | 'PAID_LEAVE'
  | 'OVERTIME'
  | 'BUSINESS_TRIP'
  | 'SUBSTITUTE'
  | 'LEAVE_OF_ABSENCE'

export interface AttRequestPayload {
  requestType: RequestType
  startDate: string
  endDate: string
  startTime: string | null
  endTime: string | null
  days: number | null
  minutes: number | null
  reason: string | null
}

export interface AttRequest extends AttRequestPayload {
  id: number
  status: ApprovalStatus
  createdAt?: string | null
  updatedAt?: string | null
}

export interface ApprovalInboxItem {
  approvalId: number
  requestId: number
  requestType: RequestType
  applicantId: number
  applicantEmployeeCode: string
  applicantName: string
  currentStep: number
  totalSteps: number
  startDate: string
  endDate: string
  reason?: string | null
  submittedAt?: string | null
}

export interface ApprovalHistoryItem {
  approvalId: number
  requestId: number
  requestType: RequestType
  applicantId: number
  applicantEmployeeCode: string
  applicantName: string
  status: ApprovalStatus
  submittedAt?: string | null
  completedAt?: string | null
}

export interface ApprovalStep {
  step: number
  approverId: number
  approverEmployeeCode: string
  approverName: string
  status: ApprovalStatus
  comment?: string | null
  decidedAt?: string | null
}

export interface ApprovalDetail {
  approvalId: number
  requestId: number
  requestType: RequestType
  applicantId: number
  applicantEmployeeCode: string
  applicantName: string
  currentStep: number
  totalSteps: number
  status: ApprovalStatus
  startDate: string
  endDate: string
  reason?: string | null
  submittedAt?: string | null
  completedAt?: string | null
  steps: ApprovalStep[]
}

export interface ApprovalDelegateCandidate {
  employeeId: number
  employeeCode: string
  displayName: string
}
