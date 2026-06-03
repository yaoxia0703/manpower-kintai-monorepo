<template>
  <section>
    <el-card shadow="never">
      <template #header>
        <el-row align="middle" justify="space-between">
          <el-col :span="12">権限管理</el-col>
          <el-col :span="12">
            <el-row justify="end">
              <el-button :loading="loading" @click="loadData">更新</el-button>
              <el-button type="primary" @click="openCreate">新規作成</el-button>
            </el-row>
          </el-col>
        </el-row>
      </template>

      <el-row :gutter="12" class="toolbar">
        <el-col :xs="24" :md="8">
          <el-tree-select
            v-model="selectedMenuId"
            :data="menuTree"
            clearable
            check-strictly
            node-key="id"
            placeholder="メニューで絞り込み"
            :props="{ label: 'name', children: 'children' }"
            @change="loadData"
          />
        </el-col>
      </el-row>

      <el-table v-loading="loading" :data="rows">
        <el-table-column prop="name" label="名称" min-width="150" />
        <el-table-column prop="code" label="コード" min-width="220" />
        <el-table-column prop="method" label="メソッド" width="100" />
        <el-table-column prop="path" label="APIパス" min-width="240" />
        <el-table-column prop="sort" label="表示順" width="90" />
        <el-table-column label="状態" width="90">
          <template #default="{ row }">
            <el-switch
              :model-value="row.status === CommonStatus.ENABLED"
              :loading="switchingId === row.id"
              @change="(value: string | number | boolean) => toggleStatus(row, Boolean(value))"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">編集</el-button>
            <el-button link type="danger" @click="confirmDelete(row)">削除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-row justify="end" class="pager">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="size"
          layout="total, sizes, prev, pager, next"
          :total="total"
          @current-change="loadData"
          @size-change="loadData"
        />
      </el-row>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="editingId ? '権限編集' : '権限新規作成'" width="620px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="所属メニュー">
          <el-tree-select
            v-model="form.menuId"
            :data="menuTree"
            clearable
            check-strictly
            node-key="id"
            :props="{ label: 'name', children: 'children' }"
          />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="コード" prop="code">
          <el-input v-model="form.code" />
        </el-form-item>
        <el-row :gutter="12">
          <el-col :span="8">
            <el-form-item label="メソッド" prop="method">
              <el-select v-model="form.method">
                <el-option label="GET" value="GET" />
                <el-option label="POST" value="POST" />
                <el-option label="PUT" value="PUT" />
                <el-option label="DELETE" value="DELETE" />
                <el-option label="PATCH" value="PATCH" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="表示順" prop="sort">
              <el-input-number v-model="form.sort" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="APIパス" prop="path">
          <el-input v-model="form.path" />
        </el-form-item>
        <el-form-item label="備考">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">キャンセル</el-button>
        <el-button type="primary" :loading="saving" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import { fetchMenus } from '@/api/system/menu'
import {
  createPermission,
  deletePermission,
  disablePermission,
  enablePermission,
  fetchPermissions,
  fetchPermissionsByMenu,
  updatePermission,
} from '@/api/system/permission'
import { CommonStatus } from '@/types/enums'
import type { PermissionCreateRequest, SystemMenu, SystemPermission } from '@/types/system'

interface PermissionFormState {
  menuId: number | null
  code: string
  name: string
  method: string
  path: string
  remark: string
  sort: number
}

const loading = ref(false)
const saving = ref(false)
const switchingId = ref<number>()
const dialogVisible = ref(false)
const editingId = ref<number>()
const formRef = ref<FormInstance>()
const rows = ref<SystemPermission[]>([])
const menus = ref<SystemMenu[]>([])
const selectedMenuId = ref<number>()
const page = ref(1)
const size = ref(10)
const total = ref(0)

const form = reactive<PermissionFormState>({
  menuId: null,
  code: '',
  name: '',
  method: 'GET',
  path: '',
  remark: '',
  sort: 0,
})

const rules: FormRules<PermissionFormState> = {
  name: [{ required: true, message: '名称を入力してください', trigger: 'blur' }],
  code: [{ required: true, message: 'コードを入力してください', trigger: 'blur' }],
  method: [{ required: true, message: 'メソッドを選択してください', trigger: 'change' }],
  path: [{ required: true, message: 'APIパスを入力してください', trigger: 'blur' }],
  sort: [{ required: true, message: '表示順を入力してください', trigger: 'blur' }],
}

const menuTree = computed(() => buildTree(menus.value))

function buildTree(list: SystemMenu[]) {
  const map = new Map<number, SystemMenu>()
  const roots: SystemMenu[] = []
  list.forEach((item) => map.set(item.id, { ...item, children: [] }))
  map.forEach((item) => {
    if (item.parentId && map.has(item.parentId)) {
      map.get(item.parentId)?.children?.push(item)
      return
    }
    roots.push(item)
  })
  return roots
}

async function loadMenus() {
  const { data } = await fetchMenus()
  menus.value = data.data
}

async function loadData() {
  loading.value = true
  try {
    if (selectedMenuId.value) {
      const { data } = await fetchPermissionsByMenu(selectedMenuId.value)
      rows.value = data.data
      total.value = data.data.length
      return
    }
    const { data } = await fetchPermissions({ page: page.value, size: size.value })
    rows.value = data.data.records
    total.value = data.data.total
  } finally {
    loading.value = false
  }
}

function resetForm() {
  editingId.value = undefined
  Object.assign(form, {
    menuId: selectedMenuId.value ?? null,
    code: '',
    name: '',
    method: 'GET',
    path: '',
    remark: '',
    sort: 0,
  })
}

function openCreate() {
  resetForm()
  dialogVisible.value = true
}

function openEdit(row: SystemPermission) {
  editingId.value = row.id
  Object.assign(form, {
    menuId: row.menuId ?? null,
    code: row.code,
    name: row.name,
    method: row.method,
    path: row.path,
    remark: row.remark ?? '',
    sort: row.sort,
  })
  dialogVisible.value = true
}

async function submit() {
  await formRef.value?.validate()
  saving.value = true
  try {
    const request = toPermissionRequest()
    if (editingId.value) {
      await updatePermission(editingId.value, request)
    } else {
      await createPermission(request)
    }
    ElMessage.success('保存しました')
    dialogVisible.value = false
    await loadData()
  } finally {
    saving.value = false
  }
}

function toPermissionRequest(): PermissionCreateRequest {
  return {
    menuId: form.menuId,
    code: form.code,
    name: form.name,
    method: form.method,
    path: form.path,
    remark: form.remark,
    sort: form.sort,
  }
}

async function toggleStatus(row: SystemPermission, enabled: boolean) {
  switchingId.value = row.id
  try {
    enabled ? await enablePermission(row.id) : await disablePermission(row.id)
    await loadData()
  } finally {
    switchingId.value = undefined
  }
}

async function confirmDelete(row: SystemPermission) {
  await ElMessageBox.confirm('ロールに割り当て済みの権限は削除できません。削除しますか？', '権限削除', {
    type: 'warning',
  })
  await deletePermission(row.id)
  ElMessage.success('削除しました')
  await loadData()
}

onMounted(async () => {
  await loadMenus()
  await loadData()
})
</script>

<style scoped>
.toolbar {
  margin-bottom: 16px;
}

.pager {
  margin-top: 16px;
}
</style>
