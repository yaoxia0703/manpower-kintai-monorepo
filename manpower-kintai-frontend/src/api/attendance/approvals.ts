import request from '@/api/common/request'
import type { ApiResponse } from '@/types/common'
import type {
  ApprovalDelegateCandidate,
  ApprovalDetail,
  ApprovalHistoryItem,
  ApprovalInboxItem,
} from '@/types/attendance'
import type { PageResult } from '@/types/system'

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

export function searchApprovalDelegates(keyword: string) {
  return request.get<ApiResponse<PageResult<ApprovalDelegateCandidate>>>('/employee/approval-delegates', {
    params: { keyword, page: 1, size: 20 },
  })
}
