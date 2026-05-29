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

  function findMenuPath(path: string) {
    const byId = new Map(menus.value.map((menu) => [menu.id, menu]))
    const current = menus.value.find((menu) => menu.path === path)
    if (!current) return []

    const pathMenus = []
    let cursor: CurrentMenu | undefined = current

    while (cursor) {
      pathMenus.unshift(cursor)
      cursor = cursor.parentId == null ? undefined : byId.get(cursor.parentId)
    }

    return pathMenus
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
    findMenuPath,
    clearAccess,
  }
})
