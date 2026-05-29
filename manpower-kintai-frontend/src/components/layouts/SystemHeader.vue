<template>
  <div class="system-header">
    <div class="header-main">
      <el-button circle aria-label="メニュー" class="app-switcher">
        <span v-for="index in 9" :key="index"></span>
      </el-button>

      <div class="brand">
        <el-avatar :size="34" class="brand-mark">M</el-avatar>
        <span class="brand-name">Manpower</span>
      </div>

      <el-input class="global-search" placeholder="検索..." size="default" disabled />

      <div class="header-actions">
        <el-button circle aria-label="通知">!</el-button>

        <el-dropdown
          trigger="click"
          placement="bottom-end"
          :teleported="false"
          @command="handleUserCommand"
        >
          <el-avatar :size="38" class="user-avatar">{{ userInitial }}</el-avatar>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">ログアウト</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <nav class="menu-bar" aria-label="メインメニュー">
      <MenuItem
        v-for="menu in displayMenus"
        :key="menu.code"
        :label="menu.name"
        :to="menu.path || undefined"
        :chevron="menu.chevron"
      />
    </nav>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import MenuItem from '@/components/layouts/MenuItem.vue'
import { useAuthStore } from '@/stores/auth'
import { usePermissionStore } from '@/stores/permissionStore'
import { useUserStore } from '@/stores/userStore'
import type { CurrentMenu } from '@/types/menu'

interface HeaderMenu {
  name: string
  code: string
  path?: string | null
  chevron?: boolean
}

const fallbackMenus: HeaderMenu[] = [
  { name: 'ホーム', code: 'home', path: '/admin' },
  { name: '勤務表', code: 'timesheet' },
  { name: '事前申請', code: 'request' },
  { name: '経費精算', code: 'expense' },
  { name: '工数実績', code: 'worklog' },
  { name: '管理メニュー', code: 'admin-menu' },
  { name: '実績', code: 'actuals', chevron: true },
  { name: 'ファイル', code: 'files', chevron: true },
  { name: 'レポート', code: 'reports', chevron: true },
]

const authStore = useAuthStore()
const userStore = useUserStore()
const permissionStore = usePermissionStore()

const userInitial = computed(() => {
  const name = userStore.displayName || userStore.email || 'U'
  return name.trim().charAt(0).toUpperCase()
})

const displayMenus = computed<HeaderMenu[]>(() => {
  const menus = permissionStore.menus
  if (menus.length === 0) return fallbackMenus

  return menus
    .filter((menu: CurrentMenu) => menu.visible !== 0)
    .sort((a, b) => (a.sort ?? 0) - (b.sort ?? 0))
    .map((menu) => ({
      name: menu.name,
      code: menu.code,
      path: menu.path,
      chevron: menu.type === 1,
    }))
})

async function handleUserCommand(command: string) {
  if (command === 'logout') {
    await authStore.logout()
  }
}
</script>

<style scoped>
.system-header {
  height: 100%;
  background: var(--el-bg-color);
}

.header-main {
  display: grid;
  min-height: 56px;
  grid-template-columns: auto auto minmax(180px, 440px) minmax(0, 1fr);
  align-items: center;
  gap: 16px;
  padding: 8px 18px;
}

.app-switcher {
  display: grid;
  grid-template-columns: repeat(3, 4px);
  grid-template-rows: repeat(3, 4px);
  gap: 4px;
}

.app-switcher span {
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background: var(--el-text-color-secondary);
}

.brand {
  display: inline-flex;
  min-width: 168px;
  align-items: center;
  gap: 10px;
}

.brand-mark {
  background: var(--el-color-primary);
  color: var(--el-color-white);
  font-weight: 700;
}

.brand-name {
  color: var(--el-text-color-primary);
  font-weight: 700;
}

.global-search {
  width: 100%;
  min-width: 0;
}

.header-actions {
  display: inline-flex;
  min-width: 0;
  justify-self: end;
  align-items: center;
  gap: 8px;
}

.user-avatar {
  border: 2px solid var(--el-border-color-lighter);
  background: var(--el-color-primary);
  color: var(--el-color-white);
  cursor: pointer;
  font-weight: 700;
}

.menu-bar {
  display: flex;
  min-height: 42px;
  align-items: stretch;
  overflow-x: auto;
  overflow-y: hidden;
  border-top: 1px solid var(--el-border-color-lighter);
  padding: 0 18px 0 68px;
}

@media (max-width: 860px) {
  .header-main {
    grid-template-columns: auto auto minmax(0, 1fr) auto;
  }

  .brand-name {
    display: none;
  }

  .brand {
    min-width: auto;
  }

  .menu-bar {
    padding-left: 12px;
  }
}

@media (max-width: 640px) {
  .header-main {
    grid-template-columns: auto minmax(0, 1fr) auto;
    gap: 10px;
  }

  .brand {
    display: none;
  }
}
</style>
