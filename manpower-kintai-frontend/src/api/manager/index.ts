import request from '@/api/common/request'
import type { ApiResponse, JoinPageResult } from '@/types/common'
import type {
  SubordinateEmployee,
  SubordinateFilterOptionsResponse,
  SubordinateQueryParams,
} from '@/types/manager'

export function fetchSubordinates(params: SubordinateQueryParams = {}) {
  return request.get<ApiResponse<JoinPageResult<SubordinateEmployee>>>('/manager/emp/subordinates', {
    params,
  })
}

export function fetchSubordinateFilterOptions() {
  return request.get<ApiResponse<SubordinateFilterOptionsResponse>>('/manager/emp/subordinates/options')
}
