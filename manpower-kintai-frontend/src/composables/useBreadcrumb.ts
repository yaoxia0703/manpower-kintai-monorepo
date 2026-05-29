import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { usePermissionStore } from '@/stores/permissionStore'
import { MenuType } from '@/types/enums'
import type { CurrentMenu } from '@/types/menu'

export function useBreadcrumb() {
  const route = useRoute()
  const permissionStore = usePermissionStore()

  const breadcrumbList = computed(() => permissionStore.findMenuPath(route.path))

  function getBreadcrumbTo(item: CurrentMenu, index: number) {
    const isLast = index === breadcrumbList.value.length - 1
    if (isLast || item.type === MenuType.DIRECTORY) return undefined

    return item.path || undefined
  }

  return {
    breadcrumbList,
    getBreadcrumbTo,
  }
}
