import request from '@/api/common/request'
import type { ApiResponse } from '@/types/common'
import type {
  ApprovalDetail,
  ApprovalHistoryItem,
  ApprovalInboxItem,
} from '@/types/attendance'

export function fetchPendingApprovals() {
  return request.get<ApiResponse<ApprovalInboxItem[]>>('/employee/approvals/pending')
}

export function fetchApprovalHistory() {
  return request.get<ApiResponse<ApprovalHistoryItem[]>>('/employee/approvals/history')
}

export function fetchApprovalDetail(approvalId: number) {
  return request.get<ApiResponse<ApprovalDetail>>(`/employee/approvals/${approvalId}`)
}

export function approveRequest(approvalId: number, comment: string | null) {
  return request.post<ApiResponse<void>>(`/employee/approvals/${approvalId}/approve`, { comment })
}

export function rejectRequest(approvalId: number, comment: string | null) {
  return request.post<ApiResponse<void>>(`/employee/approvals/${approvalId}/reject`, { comment })
}

export function delegateRequest(approvalId: number, targetApproverId: number) {
  return request.post<ApiResponse<void>>(`/employee/approvals/${approvalId}/delegate`, {
    targetApproverId,
  })
}
