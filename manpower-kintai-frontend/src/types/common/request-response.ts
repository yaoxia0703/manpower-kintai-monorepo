export interface ApiResponse<T> {
  code: number
  message?: string
  data: T
  traceId?: string
  timestamp?: number
  detail?: string
}

export interface JoinPageResult<T> {
  records: T[]
  total: number
  pageNum: number
  pageSize: number
  pages: number
}
