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
  min-height: 100vh;
  background:
    linear-gradient(rgba(255, 255, 255, 0.18), rgba(255, 255, 255, 0.18)),
    #b8cde5;
  color: #172033;
}

.layout-header {
  height: auto;
  border-bottom: 1px solid #c7d2e0;
  background: #ffffff;
  box-shadow: 0 1px 2px rgba(16, 24, 40, 0.08);
  padding: 0;
}

.layout-body {
  min-height: 0;
  padding: 0;
}

.layout-footer {
  height: 48px;
  border-top: 1px solid #c7d2e0;
  padding: 0;
}
</style>
