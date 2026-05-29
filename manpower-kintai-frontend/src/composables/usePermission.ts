import { usePermissionStore } from '@/stores/permissionStore'

export function usePermission() {
  const permissionStore = usePermissionStore()

  function hasPermission(permission: string): boolean {
    return permissionStore.hasPermission(permission)
  }

  function hasAnyPermission(permissions: string[]): boolean {
    return permissions.some((permission) => permissionStore.hasPermission(permission))
  }

  function hasAllPermissions(permissions: string[]): boolean {
    return permissions.every((permission) => permissionStore.hasPermission(permission))
  }

  return {
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
  }
}
