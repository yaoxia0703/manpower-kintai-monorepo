import type { Directive, DirectiveBinding } from 'vue'
import { usePermissionStore } from '@/stores/permissionStore'

type PermissionBinding =
  | string
  | string[]
  | {
      value: string[]
      mode?: 'and' | 'or'
    }

function resolvePermission(value: PermissionBinding): boolean {
  const permissionStore = usePermissionStore()

  if (typeof value === 'string') {
    return permissionStore.hasPermission(value)
  }

  if (Array.isArray(value)) {
    return value.some((permission) => permissionStore.hasPermission(permission))
  }

  const mode = value.mode ?? 'or'
  return mode === 'and'
    ? value.value.every((permission) => permissionStore.hasPermission(permission))
    : value.value.some((permission) => permissionStore.hasPermission(permission))
}

function checkPermission(el: HTMLElement, binding: DirectiveBinding<PermissionBinding>) {
  const value = binding.value
  if (!value) return

  el.style.display = resolvePermission(value) ? '' : 'none'
}

export const permissionDirective: Directive<HTMLElement, PermissionBinding> = {
  mounted(el, binding) {
    checkPermission(el, binding)
  },
  updated(el, binding) {
    if (binding.value === binding.oldValue) return
    checkPermission(el, binding)
  },
}
