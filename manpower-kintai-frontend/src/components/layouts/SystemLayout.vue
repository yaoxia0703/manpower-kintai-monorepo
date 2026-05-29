<template>
  <el-container class="layout">
    <el-header class="layout-header">
      <SystemHeader />
    </el-header>

    <el-main class="layout-body">
      <SystemMain />
    </el-main>

    <el-footer class="layout-footer">
      <SystemFooter />
    </el-footer>
  </el-container>
</template>

<script setup lang="ts">
import SystemFooter from '@/components/layouts/SystemFooter.vue'
import SystemHeader from '@/components/layouts/SystemHeader.vue'
import SystemMain from '@/components/layouts/SystemMain.vue'
</script>

<style scoped>
.layout {
  width: 100%;
  height: 100dvh;
  overflow: hidden;
  background: var(--el-bg-color-page);
  color: var(--el-text-color-primary);
}

.layout-header {
  height: auto;
  border-bottom: 1px solid var(--el-border-color-light);
  background: var(--el-bg-color);
  box-shadow: var(--el-box-shadow-light);
  padding: 0;
}

.layout-body {
  flex: 1 1 auto;
  min-height: 0;
  overflow: auto;
  padding: 0;
}

.layout-footer {
  flex-shrink: 0;
  height: 48px;
  border-top: 1px solid var(--el-border-color-light);
  padding: 0;
}
</style>
