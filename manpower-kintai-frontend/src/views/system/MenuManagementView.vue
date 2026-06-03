<template>
  <section>
    <el-card shadow="never">
      <template #header>
        <el-row align="middle" justify="space-between">
          <el-col :span="12">メニュー管理</el-col>
          <el-col :span="12">
            <el-row justify="end">
              <el-button :loading="loading" @click="loadMenus">更新</el-button>
              <el-button type="primary" @click="openCreate()">新規作成</el-button>
            </el-row>
          </el-col>
        </el-row>
      </template>

      <el-table
        v-loading="loading"
        :data="menuTree"
        row-key="id"
        :tree-props="{ children: 'children' }"
        default-expand-all
      >
        <el-table-column prop="name" label="名称" min-width="180" />
        <el-table-column prop="code" label="コード" min-width="180" />
        <el-table-column prop="path" label="パス" min-width="180" />
        <el-table-column prop="component" label="コンポーネント" min-width="180" />
        <el-table-column prop="sort" label="表示順" width="90" />
        <el-table-column label="表示" width="90">
          <template #default="{ row }">
            <el-switch
              :model-value="row.visible === 1"
              :loading="switchingId === row.id"
              @change="(value: string | number | boolean) => toggleVisible(row, Boolean(value))"
            />
          </template>
        </el-table-column>
        <el-table-column label="状態" width="90">
          <template #default="{ row }">
            <el-switch
              :model-value="row.status === CommonStatus.ENABLED"
              :loading="switchingId === row.id"
              @change="(value: string | number | boolean) => toggleStatus(row, Boolean(value))"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openCreate(row.id)">子メニュー追加</el-button>
            <el-button link type="primary" @click="openEdit(row)">編集</el-button>
            <el-button link type="danger" @click="confirmDelete(row)">削除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="editingId ? 'メニュー編集' : 'メニュー新規作成'" width="560px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="親メニュー">
          <el-tree-select
            v-model="form.parentId"
            :data="parentOptions"
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
        <el-form-item label="パス">
          <el-input v-model="form.path" />
        </el-form-item>
        <el-form-item label="コンポーネント">
          <el-input v-model="form.component" />
        </el-form-item>
        <el-form-item label="アイコン">
          <el-input v-model="form.icon" />
        </el-form-item>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="種別" prop="type">
              <el-select v-model="form.type">
                <el-option label="ディレクトリ" :value="1" />
                <el-option label="メニュー" :value="2" />
                <el-option label="ボタン" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="表示順" prop="sort">
              <el-input-number v-model="form.sort" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
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
import {
  createMenu,
  deleteMenu,
  disableMenu,
  enableMenu,
  fetchMenus,
  hideMenu,
  showMenu,
  updateMenu,
} from '@/api/system/menu'
import { CommonStatus } from '@/types/enums'
import type { MenuCreateRequest, SystemMenu } from '@/types/system'

interface MenuFormState {
  parentId: number | null
  name: string
  code: string
  path: string
  component: string
  icon: string
  type: number
  sort: number
  visible: number
}

const loading = ref(false)
const saving = ref(false)
const switchingId = ref<number>()
const dialogVisible = ref(false)
const editingId = ref<number>()
const formRef = ref<FormInstance>()
const rows = ref<SystemMenu[]>([])

const form = reactive<MenuFormState>({
  parentId: null,
  name: '',
  code: '',
  path: '',
  component: '',
  icon: '',
  type: 2,
  sort: 0,
  visible: 1,
})

const rules: FormRules<MenuFormState> = {
  name: [{ required: true, message: '名称を入力してください', trigger: 'blur' }],
  code: [{ required: true, message: 'コードを入力してください', trigger: 'blur' }],
  type: [{ required: true, message: '種別を選択してください', trigger: 'change' }],
  sort: [{ required: true, message: '表示順を入力してください', trigger: 'blur' }],
}

const menuTree = computed(() => buildTree(rows.value))
const parentOptions = computed(() =>
  buildTree(rows.value.filter((menu) => menu.id !== editingId.value && menu.type !== 3)),
)

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
  loading.value = true
  try {
    const { data } = await fetchMenus()
    rows.value = data.data
  } finally {
    loading.value = false
  }
}

function resetForm(parentId: number | null = null) {
  editingId.value = undefined
  Object.assign(form, {
    parentId,
    name: '',
    code: '',
    path: '',
    component: '',
    icon: '',
    type: 2,
    sort: 0,
    visible: 1,
  })
}

function openCreate(parentId: number | null = null) {
  resetForm(parentId)
  dialogVisible.value = true
}

function openEdit(row: SystemMenu) {
  editingId.value = row.id
  Object.assign(form, {
    parentId: row.parentId ?? null,
    name: row.name,
    code: row.code,
    path: row.path ?? '',
    component: row.component ?? '',
    icon: row.icon ?? '',
    type: row.type,
    sort: row.sort,
    visible: row.visible,
  })
  dialogVisible.value = true
}

async function submit() {
  await formRef.value?.validate()
  saving.value = true
  try {
    const request = toMenuRequest()
    if (editingId.value) {
      await updateMenu(editingId.value, request)
    } else {
      await createMenu(request)
    }
    ElMessage.success('保存しました')
    dialogVisible.value = false
    await loadMenus()
  } finally {
    saving.value = false
  }
}

function toMenuRequest(): MenuCreateRequest {
  return {
    parentId: form.parentId,
    name: form.name,
    code: form.code,
    path: form.path,
    component: form.component,
    icon: form.icon,
    type: form.type,
    sort: form.sort,
    visible: form.visible,
  }
}

async function toggleVisible(row: SystemMenu, visible: boolean) {
  switchingId.value = row.id
  try {
    visible ? await showMenu(row.id) : await hideMenu(row.id)
    await loadMenus()
  } finally {
    switchingId.value = undefined
  }
}

async function toggleStatus(row: SystemMenu, enabled: boolean) {
  switchingId.value = row.id
  try {
    enabled ? await enableMenu(row.id) : await disableMenu(row.id)
    await loadMenus()
  } finally {
    switchingId.value = undefined
  }
}

async function confirmDelete(row: SystemMenu) {
  await ElMessageBox.confirm('親メニューを削除すると子メニューも削除されます。関連権限がある場合は削除できません。削除しますか？', 'メニュー削除', {
    type: 'warning',
  })
  await deleteMenu(row.id)
  ElMessage.success('削除しました')
  await loadMenus()
}

onMounted(loadMenus)
</script>
