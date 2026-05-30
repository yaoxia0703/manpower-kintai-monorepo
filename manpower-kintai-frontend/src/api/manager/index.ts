import request from '@/api/common/request'
import type { ApiResponse } from '@/types/common'
import type { SubordinateEmployee } from '@/types/manager'

export function fetchSubordinates() {
  return request.get<ApiResponse<SubordinateEmployee[]>>('/manager/emp/subordinates')
}
