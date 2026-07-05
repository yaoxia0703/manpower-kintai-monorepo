<template>
  <section class="page-section">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>部下管理</span>
          <el-button :loading="loading" @click="loadSubordinates">更新</el-button>
        </div>
      </template>

      <el-form :model="filters" class="query-form" label-width="90px">
        <el-row :gutter="16">
          <el-col :xs="24" :md="8">
            <el-form-item label="キーワード">
              <el-input
                v-model="filters.keyword"
                clearable
                placeholder="氏名・社員番号"
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="8">
            <el-form-item label="所属組織">
              <el-tree-select
                v-model="filters.nodeId"
                :data="nodeTreeOptions"
                :loading="optionLoading"
                clearable
                filterable
                node-key="id"
                :props="{ label: 'treeLabel', children: 'children' }"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="8">
            <el-form-item label="職級">
              <el-select v-model="filters.gradeId" :loading="optionLoading" clearable filterable>
                <el-option
                  v-for="grade in gradeOptions"
                  :key="grade.id"
                  :label="gradeLabel(grade)"
                  :value="grade.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16">
          <el-col :xs="24" :md="8">
            <el-form-item label="在職状態">
              <el-select v-model="filters.status">
                <el-option
                  v-for="option in COMMON_STATUS_OPTIONS"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="16">
            <el-form-item class="query-actions">
              <el-button type="primary" :loading="loading" @click="handleSearch">検索</el-button>
              <el-button @click="handleReset">リセット</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-table v-loading="loading" :data="rows" border row-key="employeeId">
        <el-table-column prop="employeeCode" label="社員番号" width="140" />
        <el-table-column prop="displayName" label="氏名" min-width="160" />
        <el-table-column prop="email" label="メール" min-width="220" />
        <el-table-column prop="nodeName" label="所属組織" min-width="180" />
        <el-table-column prop="gradeName" label="職級" width="140" />
      </el-table>

      <el-row justify="end" class="pager">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="size"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @current-change="loadSubordinates"
          @size-change="handleSizeChange"
        />
      </el-row>
    </el-card>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchSubordinateFilterOptions, fetchSubordinates } from '@/api/manager'
import { COMMON_STATUS_OPTIONS, CommonStatus } from '@/types/enums'
import type {
  ManagerGradeOption,
  ManagerOrgNodeOption,
  SubordinateEmployee,
  SubordinateQueryParams,
} from '@/types/manager'

interface FilterState {
  keyword: string
  nodeId?: number
  gradeId?: number
  status: CommonStatus
}

type NodeTreeOption = ManagerOrgNodeOption & {
  treeLabel: string
  children: NodeTreeOption[]
}

const rows = ref<SubordinateEmployee[]>([])
const loading = ref(false)
const optionLoading = ref(false)
const page = ref(1)
const size = ref(10)
const total = ref(0)
const nodeOptions = ref<ManagerOrgNodeOption[]>([])
const gradeOptions = ref<ManagerGradeOption[]>([])

const filters = reactive<FilterState>({
  keyword: '',
  status: CommonStatus.ENABLED,
})

const nodeTreeOptions = computed(() => buildNodeTree(nodeOptions.value))

function toQueryParams(): SubordinateQueryParams {
  const params: SubordinateQueryParams = {
    page: page.value,
    size: size.value,
    status: filters.status,
  }
  const keyword = filters.keyword.trim()
  if (keyword) params.keyword = keyword
  if (filters.nodeId) params.nodeId = filters.nodeId
  if (filters.gradeId) params.gradeId = filters.gradeId
  return params
}

async function loadSubordinates() {
  loading.value = true
  try {
    const res = await fetchSubordinates(toQueryParams())
    const result = res.data.data
    rows.value = result.records ?? []
    total.value = result.total ?? 0
    page.value = result.pageNum ? Number(result.pageNum) : page.value
    size.value = result.pageSize ? Number(result.pageSize) : size.value
  } catch {
    ElMessage.error('部下一覧を取得できませんでした')
  } finally {
    loading.value = false
  }
}

async function loadFilterOptions() {
  optionLoading.value = true
  try {
    const res = await fetchSubordinateFilterOptions()
    nodeOptions.value = res.data.data.nodes ?? []
    gradeOptions.value = res.data.data.grades ?? []
  } catch {
    ElMessage.error('検索条件を取得できませんでした')
  } finally {
    optionLoading.value = false
  }
}

async function handleSearch() {
  page.value = 1
  await loadSubordinates()
}

async function handleReset() {
  filters.keyword = ''
  filters.nodeId = undefined
  filters.gradeId = undefined
  filters.status = CommonStatus.ENABLED
  page.value = 1
  await loadSubordinates()
}

async function handleSizeChange() {
  page.value = 1
  await loadSubordinates()
}

function buildNodeTree(nodes: ManagerOrgNodeOption[]) {
  const map = new Map<number, NodeTreeOption>()
  const roots: NodeTreeOption[] = []

  nodes.forEach((node) => {
    map.set(node.id, {
      ...node,
      treeLabel: nodeLabel(node),
      children: [],
    })
  })

  map.forEach((node) => {
    if (node.parentId && map.has(node.parentId)) {
      map.get(node.parentId)?.children.push(node)
      return
    }
    roots.push(node)
  })

  return roots
}

function nodeLabel(node: ManagerOrgNodeOption) {
  const code = node.code ?? node.typeCode
  return code ? `${node.name} (${code})` : node.name
}

function gradeLabel(grade: ManagerGradeOption) {
  const code = grade.code ?? grade.gradeLevel
  return code ? `${grade.name} (${code})` : grade.name
}

onMounted(() => {
  loadFilterOptions()
  loadSubordinates()
})
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

.query-form {
  margin-bottom: 8px;
}

.query-actions :deep(.el-form-item__content) {
  justify-content: flex-end;
}

.pager {
  margin-top: 16px;
}
</style>
