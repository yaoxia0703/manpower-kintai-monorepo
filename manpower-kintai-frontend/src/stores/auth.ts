import { ref } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'
import { fetchCurrentUser, login as loginApi, logout as logoutApi } from '@/api/auth'
import { usePermissionStore } from '@/stores/permissionStore'
import { useUserStore } from '@/stores/userStore'

export const useAuthStore = defineStore('auth', () => {
  const router = useRouter()
  const userStore = useUserStore()
  const permissionStore = usePermissionStore()

  const token = ref<string | null>(localStorage.getItem('token'))

  async function login(emailInput: string, password: string) {
    const res = await loginApi(emailInput, password)
    const data = res.data.data

    token.value = data.token
    localStorage.setItem('token', data.token)

    await loadCurrentUser()
  }

  async function loadCurrentUser() {
    const res = await fetchCurrentUser()
    const data = res.data.data

    userStore.setProfile(data.user)
    permissionStore.setAccess({
      roles: data.roles ?? [],
      permissions: data.permissions ?? [],
      menus: data.menus ?? [],
    })
  }

  function clearSession() {
    token.value = null

    localStorage.removeItem('token')
    userStore.clearProfile()
    permissionStore.clearAccess()
  }

  async function logout() {
    try {
      if (token.value) {
        await logoutApi()
      }
    } finally {
      clearSession()

      router.push('/login')
    }
  }

  return { token, login, loadCurrentUser, clearSession, logout }
})
