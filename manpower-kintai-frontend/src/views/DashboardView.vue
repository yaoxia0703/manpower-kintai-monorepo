<template>
  <main class="dashboard">
    <header class="dashboard-header">
      <div>
        <p class="eyebrow">ManpowerGroup</p>
        <h1>勤怠管理</h1>
      </div>
      <el-button @click="authStore.logout()">ログアウト</el-button>
    </header>

    <section class="welcome">
      <h2>{{ authStore.displayName || 'ユーザー' }}</h2>
      <p>{{ authStore.email }}</p>
    </section>
  </main>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
  background: #f6f8fb;
  padding: 32px;
}

.dashboard-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  margin-bottom: 32px;
}

.eyebrow {
  margin: 0 0 4px;
  color: #667085;
  font-size: 0.85rem;
}

.dashboard-header h1 {
  margin: 0;
  color: #172033;
  font-size: 1.6rem;
}

.welcome {
  max-width: 720px;
  border: 1px solid #e4e7ec;
  border-radius: 8px;
  background: #ffffff;
  padding: 24px;
}

.welcome h2 {
  margin: 0 0 8px;
  color: #172033;
  font-size: 1.25rem;
}

.welcome p {
  margin: 0;
  color: #667085;
}
</style>
