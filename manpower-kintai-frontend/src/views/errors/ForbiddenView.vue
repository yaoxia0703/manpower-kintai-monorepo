<template>
  <section class="empty-page">
    <el-empty description="このページを表示する権限がありません">
      <el-button type="primary" @click="router.push('/admin')">ホームへ戻る</el-button>
    </el-empty>
  </section>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<style scoped>
.empty-page {
  display: grid;
  min-height: 360px;
  place-items: center;
}
</style>
