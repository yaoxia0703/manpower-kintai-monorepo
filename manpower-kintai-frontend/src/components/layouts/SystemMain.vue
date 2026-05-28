<template>
  <div class="system-main">
    <RouterView />
  </div>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
</script>

<style scoped>
.system-main {
  min-height: 100%;
}
</style>
