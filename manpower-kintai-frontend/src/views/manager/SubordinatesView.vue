<template>
  <section class="page-section">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>部下管理</span>
          <el-button :loading="loading" @click="loadSubordinates">更新</el-button>
        </div>
      </template>

      <el-table v-loading="loading" :data="rows" border>
        <el-table-column prop="employeeCode" label="社員番号" width="140" />
        <el-table-column prop="displayName" label="氏名" min-width="160" />
        <el-table-column prop="email" label="メール" min-width="220" />
        <el-table-column prop="nodeName" label="所属組織" min-width="180" />
        <el-table-column prop="gradeName" label="職級" width="140" />
      </el-table>
    </el-card>
  </section>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchSubordinates } from '@/api/manager'
import type { SubordinateEmployee } from '@/types/manager'

const rows = ref<SubordinateEmployee[]>([])
const loading = ref(false)

async function loadSubordinates() {
  loading.value = true
  try {
    const res = await fetchSubordinates()
    rows.value = res.data.data ?? []
  } catch {
    ElMessage.error('部下一覧を取得できませんでした')
  } finally {
    loading.value = false
  }
}

onMounted(loadSubordinates)
</script>

<style scoped>
.page-section {
  padding: 16px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
