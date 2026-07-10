import axios from 'axios'
import router from '@/router'
import { API_BASE_URL } from '@/config/env'
import type { ApiResponse } from '@/types/common'

const request = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
})

request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

request.interceptors.response.use(
  (response) => {
    const data = response.data as ApiErrorResponse
    if (data?.code && data.code !== 200) {
      return Promise.reject(new Error(resolveErrorMessage(data)))
    }
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('employeeId')
      localStorage.removeItem('displayName')
      localStorage.removeItem('email')
      router.push({
        path: '/login',
        query: { redirect: router.currentRoute.value.fullPath },
      })
    }
    return Promise.reject(error)
  },
)

// バリデーションエラー1件分の形
interface ValidationErrorItem {
  key?: string
  message?: string
}

// resolveErrorMessage / interceptor 共通で使うエラーレスポンスの型
type ApiErrorResponse = ApiResponse<{ errors?: ValidationErrorItem[] }>

// バックエンドのエラーレスポンスからユーザー向けメッセージを組み立てる
function resolveErrorMessage(response: ApiErrorResponse): string {
  const errors = response?.data?.errors
  if (Array.isArray(errors) && errors.length > 0) {
    return errors
      .map((item) => item?.message || item?.key)
      .filter((msg): msg is string => Boolean(msg))
      .slice(0, 3)
      .join('\n')
  }
  return response?.message || 'Request failed'
}

export default request
