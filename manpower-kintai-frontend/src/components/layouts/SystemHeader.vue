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
        <el-popover
          placement="bottom-end"
          trigger="click"
          width="min(360px, calc(100vw - 24px))"
          :teleported="false"
          popper-class="notification-popper"
          @show="handleNotificationOpen"
        >
          <template #reference>
            <el-badge
              :value="unreadCount"
              :max="99"
              :hidden="unreadCount <= 0"
              class="notification-badge"
            >
              <el-button circle aria-label="通知" :icon="Bell" />
            </el-badge>
          </template>

          <div class="notification-panel">
            <div class="notification-header">
              <span class="notification-title">通知</span>
              <el-button
                text
                size="small"
                :icon="Refresh"
                :loading="notificationsLoading"
                @click="loadNotifications()"
              >
                更新
              </el-button>
            </div>

            <div v-if="notificationError" class="notification-state notification-error">
              {{ notificationError }}
            </div>
            <div v-else-if="notificationsLoading" class="notification-state">読み込み中...</div>
            <el-empty
              v-else-if="notifications.length === 0"
              description="未読通知はありません"
              :image-size="72"
            />
            <div v-else class="notification-list">
              <button
                v-for="notification in notifications"
                :key="notification.id"
                class="notification-item"
                type="button"
                @click="handleNotificationClick(notification)"
              >
                <span class="notification-item-title">{{ notification.title }}</span>
                <span class="notification-item-content">{{ notification.content }}</span>
                <span class="notification-item-time">
                  {{ formatNotificationTime(notification.createdAt) }}
                </span>
              </button>
            </div>
          </div>
        </el-popover>

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
      <template v-for="menu in displayMenus" :key="menu.code">
        <el-dropdown
          v-if="menu.children.length > 0"
          class="menu-dropdown"
          trigger="hover"
          placement="bottom-start"
          popper-class="header-menu-dropdown"
          @command="handleMenuCommand"
        >
          <MenuItem
            :label="menu.name"
            :chevron="true"
            :active="isMenuActive(menu)"
          />
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item
                v-for="child in menu.children"
                :key="child.code"
                :command="child.path"
                :disabled="!child.path"
                :class="{ 'is-active': isMenuActive(child) }"
              >
                {{ child.name }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <MenuItem
          v-else
          :label="menu.name"
          :to="menu.path || undefined"
          :active="isMenuActive(menu)"
        />
      </template>
    </nav>
  </div>
</template>

<script setup lang="ts">
import { Bell, Refresh } from '@element-plus/icons-vue'
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  fetchUnreadNotificationCount,
  fetchUnreadNotifications,
  markNotificationsAsRead,
} from '@/api/system/notification'
import MenuItem from '@/components/layouts/MenuItem.vue'
import { useAuthStore } from '@/stores/auth'
import { usePermissionStore } from '@/stores/permissionStore'
import { useUserStore } from '@/stores/userStore'
import type { SysNotification } from '@/types/system'
import { buildVisibleMenuTree } from '@/utils/currentMenuTree'
import type { CurrentMenuNode } from '@/utils/currentMenuTree'

const authStore = useAuthStore()
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const permissionStore = usePermissionStore()
const unreadCount = ref(0)
const notifications = ref<SysNotification[]>([])
const notificationsLoading = ref(false)
const notificationError = ref('')

const NOTIFICATION_PAGE_SIZE = 10

const userInitial = computed(() => {
  const name = userStore.displayName || userStore.email || 'U'
  return name.trim().charAt(0).toUpperCase()
})

const displayMenus = computed(() => buildVisibleMenuTree(permissionStore.menus))

function isMenuActive(menu: CurrentMenuNode): boolean {
  const matchesCurrentPath =
    Boolean(menu.path) &&
    (route.path === menu.path || route.path.startsWith(`${menu.path}/`))
  return matchesCurrentPath || menu.children.some(isMenuActive)
}

async function handleMenuCommand(path: string | null) {
  if (path && path !== route.path) {
    await router.push(path)
  }
}

async function handleUserCommand(command: string) {
  if (command === 'logout') {
    await authStore.logout()
  }
}

async function loadUnreadCount() {
  try {
    const response = await fetchUnreadNotificationCount()
    unreadCount.value = response.data.data ?? 0
  } catch {
    unreadCount.value = 0
  }
}

async function loadNotifications() {
  notificationsLoading.value = true
  notificationError.value = ''

  try {
    const response = await fetchUnreadNotifications({ page: 1, size: NOTIFICATION_PAGE_SIZE })
    notifications.value = response.data.data.records
    await loadUnreadCount()
  } catch {
    notificationError.value = '通知を取得できませんでした'
  } finally {
    notificationsLoading.value = false
  }
}

function handleNotificationOpen() {
  void loadNotifications()
}

async function handleNotificationClick(notification: SysNotification) {
  notifications.value = notifications.value.filter((item) => item.id !== notification.id)
  unreadCount.value = Math.max(0, unreadCount.value - 1)
  try {
    await markNotificationsAsRead([notification.id])
  } catch {
    void loadUnreadCount()
  }

  if (!notification.refId) return
  const path =
    notification.type === 'REQUEST_APPROVED' || notification.type === 'REQUEST_REJECTED'
      ? '/admin/requests'
      : '/admin/approvals'
  await router.push({ path, query: { requestId: String(notification.refId) } })
}

function formatNotificationTime(value?: string | null) {
  if (!value) {
    return ''
  }
  return value.replace('T', ' ').slice(0, 16)
}

onMounted(() => {
  void loadUnreadCount()
})
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

.notification-badge {
  display: inline-flex;
}

.notification-panel {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.notification-header {
  display: flex;
  min-height: 32px;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--el-border-color-lighter);
  padding-bottom: 8px;
}

.notification-title {
  color: var(--el-text-color-primary);
  font-size: 14px;
  font-weight: 700;
}

.notification-state {
  padding: 18px 4px;
  color: var(--el-text-color-secondary);
  font-size: 13px;
  text-align: center;
}

.notification-error {
  color: var(--el-color-danger);
}

.notification-list {
  display: flex;
  max-height: 320px;
  flex-direction: column;
  overflow-y: auto;
}

.notification-item {
  display: grid;
  width: 100%;
  grid-template-columns: minmax(0, 1fr);
  gap: 4px;
  border: 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
  background: transparent;
  padding: 10px 2px;
  color: inherit;
  cursor: pointer;
  font: inherit;
  text-align: left;
}

.notification-item:hover {
  background: var(--el-fill-color-light);
}

.notification-item:last-child {
  border-bottom: 0;
}

.notification-item-title {
  overflow: hidden;
  color: var(--el-text-color-primary);
  font-size: 13px;
  font-weight: 700;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.notification-item-content {
  display: -webkit-box;
  overflow: hidden;
  color: var(--el-text-color-regular);
  font-size: 13px;
  line-height: 1.4;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}

.notification-item-time {
  color: var(--el-text-color-secondary);
  font-size: 12px;
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

.menu-dropdown {
  display: inline-flex;
  align-items: stretch;
}

:global(.header-menu-dropdown .el-dropdown-menu__item.is-active) {
  background: var(--app-brand-color-light);
  color: var(--el-color-primary);
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
