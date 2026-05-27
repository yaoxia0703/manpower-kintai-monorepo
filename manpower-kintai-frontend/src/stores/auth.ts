import { ref } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'
import { login as loginApi } from '@/api/auth'

export const useAuthStore = defineStore('auth', () => {
  const router = useRouter()

  const token = ref<string | null>(localStorage.getItem('token'))
  const employeeId = ref<number | null>(
    localStorage.getItem('employeeId') ? Number(localStorage.getItem('employeeId')) : null,
  )
  const displayName = ref<string | null>(localStorage.getItem('displayName'))
  const email = ref<string | null>(localStorage.getItem('email'))

  async function login(emailInput: string, password: string) {
    const res = await loginApi(emailInput, password)
    const data = res.data.data

    token.value = data.token
    employeeId.value = data.employeeId
    displayName.value = data.displayName
    email.value = data.email

    localStorage.setItem('token', data.token)
    localStorage.setItem('employeeId', String(data.employeeId))
    localStorage.setItem('displayName', data.displayName)
    localStorage.setItem('email', data.email)
  }

  function logout() {
    token.value = null
    employeeId.value = null
    displayName.value = null
    email.value = null

    localStorage.removeItem('token')
    localStorage.removeItem('employeeId')
    localStorage.removeItem('displayName')
    localStorage.removeItem('email')

    router.push('/login')
  }

  return { token, employeeId, displayName, email, login, logout }
})
