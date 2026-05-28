import { request } from '@/api/common'
import type { ApiResponse } from '@/types/common'
import type { CurrentUserResponse, LoginResponse } from '@/types/employee'

export function login(email: string, password: string) {
  return request.post<ApiResponse<LoginResponse>>('/api/system/auth/login', {
    email,
    password,
  })
}

export function fetchCurrentUser() {
  return request.get<ApiResponse<CurrentUserResponse>>('/api/system/auth/me')
}

export function logout() {
  return request.post<ApiResponse<void>>('/api/system/auth/logout')
}
