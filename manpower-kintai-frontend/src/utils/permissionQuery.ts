export interface PermissionFilterState {
  keyword: string
  menuId?: number
}

export interface PermissionQueryParams {
  page?: number
  size?: number
  menuId?: number
  keyword?: string
}

export function submitPermissionFilters(filters: PermissionFilterState): PermissionFilterState {
  return {
    keyword: filters.keyword.trim(),
    menuId: filters.menuId,
  }
}

export function resetPermissionFilters(): PermissionFilterState {
  return {
    keyword: '',
    menuId: undefined,
  }
}

export function buildPermissionQuery(
  filters: PermissionFilterState,
  page: number,
  size: number,
): PermissionQueryParams {
  const params: PermissionQueryParams = { page, size }
  const keyword = filters.keyword.trim()
  if (keyword) params.keyword = keyword
  if (filters.menuId != null) params.menuId = filters.menuId
  return params
}
