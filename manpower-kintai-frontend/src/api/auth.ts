import request from './request'

export interface LoginResponse {
  token: string
  employeeId: number
  accountId: number
  displayName: string
  email: string
}

export function login(email: string, password: string) {
  return request.post<{ code: number; data: LoginResponse }>('/api/system/auth/login', {
    email,
    password,
  })
}
