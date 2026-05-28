<template>
  <div class="system-header">
    <div class="header-main">
      <button class="icon-button app-switcher" type="button" aria-label="メニュー">
        <span v-for="index in 9" :key="index"></span>
      </button>

      <div class="brand">
        <div class="brand-mark" aria-hidden="true">M</div>
        <span class="brand-name">Manpower</span>
      </div>

      <label class="global-search" aria-label="グローバル検索">
        <span class="search-icon" aria-hidden="true"></span>
        <input type="search" placeholder="検索..." disabled />
      </label>

      <div class="header-actions">
        <button class="icon-button" type="button" aria-label="通知">
          <span class="bell-icon" aria-hidden="true"></span>
        </button>

        <el-dropdown trigger="click" @command="handleUserCommand">
          <button class="user-button" type="button" aria-label="ユーザー">
            {{ userInitial }}
          </button>
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
  background: #ffffff;
}

.header-main {
  display: grid;
  min-height: 56px;
  grid-template-columns: auto auto minmax(220px, 440px) 1fr;
  align-items: center;
  gap: 16px;
  padding: 8px 18px;
}

.icon-button {
  display: grid;
  width: 34px;
  height: 34px;
  place-items: center;
  border: 1px solid transparent;
  border-radius: 6px;
  background: transparent;
  color: #475467;
  cursor: pointer;
}

.icon-button:hover {
  border-color: #d0d5dd;
  background: #f6f8fb;
}

.app-switcher {
  grid-template-columns: repeat(3, 4px);
  grid-template-rows: repeat(3, 4px);
  gap: 4px;
}

.app-switcher span {
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background: #667085;
}

.brand {
  display: inline-flex;
  min-width: 168px;
  align-items: center;
  gap: 10px;
}

.brand-mark {
  display: grid;
  width: 34px;
  height: 34px;
  place-items: center;
  border-radius: 6px;
  background: #0f5f9e;
  color: #ffffff;
  font-size: 1.05rem;
  font-weight: 700;
}

.brand-name {
  color: #172033;
  font-size: 1rem;
  font-weight: 700;
}

.global-search {
  position: relative;
  display: block;
  width: 100%;
}

.global-search input {
  width: 100%;
  height: 34px;
  border: 1px solid #aeb7c3;
  border-radius: 5px;
  background: #ffffff;
  color: #475467;
  cursor: not-allowed;
  font-size: 0.92rem;
  opacity: 1;
  padding: 0 12px 0 36px;
}

.search-icon {
  position: absolute;
  top: 50%;
  left: 13px;
  width: 12px;
  height: 12px;
  border: 2px solid #667085;
  border-radius: 50%;
  transform: translateY(-50%);
}

.search-icon::after {
  position: absolute;
  right: -6px;
  bottom: -5px;
  width: 7px;
  height: 2px;
  border-radius: 2px;
  background: #667085;
  content: '';
  transform: rotate(45deg);
}

.header-actions {
  display: inline-flex;
  justify-self: end;
  align-items: center;
  gap: 8px;
}

.bell-icon {
  position: relative;
  width: 15px;
  height: 16px;
  border: 2px solid #667085;
  border-bottom: 0;
  border-radius: 9px 9px 4px 4px;
}

.bell-icon::before {
  position: absolute;
  right: -4px;
  bottom: -3px;
  left: -4px;
  height: 2px;
  border-radius: 2px;
  background: #667085;
  content: '';
}

.bell-icon::after {
  position: absolute;
  bottom: -7px;
  left: 50%;
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background: #667085;
  content: '';
  transform: translateX(-50%);
}

.user-button {
  display: grid;
  width: 38px;
  height: 38px;
  place-items: center;
  border: 2px solid #d9e4ef;
  border-radius: 50%;
  background: #355270;
  color: #ffffff;
  cursor: pointer;
  font-weight: 700;
}

.menu-bar {
  display: flex;
  min-height: 42px;
  align-items: stretch;
  overflow-x: auto;
  border-top: 1px solid #eef2f6;
  padding: 0 18px 0 68px;
}

@media (max-width: 860px) {
  .header-main {
    grid-template-columns: auto auto 1fr auto;
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
    grid-template-columns: auto 1fr auto;
    gap: 10px;
  }

  .brand {
    display: none;
  }
}
</style>
