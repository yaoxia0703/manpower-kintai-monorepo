import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import type { CurrentMenu } from '@/types/menu'

export const usePermissionStore = defineStore('permission', () => {
  const roles = ref<string[]>([])
  const permissions = ref<string[]>([])
  const menus = ref<CurrentMenu[]>([])

  const isLoaded = computed(() => roles.value.length > 0 || permissions.value.length > 0 || menus.value.length > 0)

  function setAccess(payload: { roles: string[]; permissions: string[]; menus: CurrentMenu[] }) {
    roles.value = payload.roles
    permissions.value = payload.permissions
    menus.value = payload.menus
  }

  function hasRole(role: string) {
    return roles.value.includes(role)
  }

  function hasPermission(permission: string) {
    return permissions.value.includes(permission)
  }

  function clearAccess() {
    roles.value = []
    permissions.value = []
    menus.value = []
  }

  return {
    roles,
    permissions,
    menus,
    isLoaded,
    setAccess,
    hasRole,
    hasPermission,
    clearAccess,
  }
})
