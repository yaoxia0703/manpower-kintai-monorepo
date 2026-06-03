<template>
  <section>
    <el-card shadow="never">
      <template #header>
        <el-row align="middle" justify="space-between">
          <el-col :span="12">ロール管理</el-col>
          <el-col :span="12">
            <el-row justify="end">
              <el-button :loading="loading" @click="loadRoles">更新</el-button>
              <el-button type="primary" @click="openCreate">新規作成</el-button>
            </el-row>
          </el-col>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="rows">
        <el-table-column prop="name" label="名称" min-width="160" />
        <el-table-column prop="code" label="コード" min-width="180" />
        <el-table-column prop="companyId" label="会社ID" width="110" />
        <el-table-column prop="sort" label="表示順" width="90" />
        <el-table-column prop="remark" label="備考" min-width="180" />
        <el-table-column label="状態" width="90">
          <template #default="{ row }">
            <el-switch
              :model-value="row.status === CommonStatus.ENABLED"
              :loading="switchingId === row.id"
              @change="(value: string | number | boolean) => toggleStatus(row, Boolean(value))"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="210" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openAuthorization(row)">権限設定</el-button>
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
          @current-change="loadRoles"
          @size-change="loadRoles"
        />
      </el-row>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="editingId ? 'ロール編集' : 'ロール新規作成'" width="560px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="会社ID">
          <el-input-number v-model="form.companyId" :min="1" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="コード" prop="code">
          <el-input v-model="form.code" />
        </el-form-item>
        <el-form-item label="表示順" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
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

    <el-drawer v-model="authorizationVisible" :title="`ロール権限設定：${authorizingRole?.name ?? ''}`" size="78%">
      <el-alert
        v-if="authorizationWarnings.length > 0"
        type="warning"
        :closable="false"
        show-icon
        class="auth-warning"
      >
        <template #title>
          {{ authorizationWarnings.join('；') }}
        </template>
      </el-alert>

      <el-row :gutter="20" class="authorization-body">
        <el-col :xs="24" :md="10">
          <h3>メニュー付与</h3>
          <el-tree
            ref="menuTreeRef"
            :data="authorizationMenuTree"
            show-checkbox
            node-key="id"
            default-expand-all
            :props="{ label: 'name', children: 'children' }"
          />
        </el-col>
        <el-col :xs="24" :md="14">
          <h3>権限付与</h3>
          <div v-loading="authorizationLoading" class="permission-groups">
            <section v-for="group in permissionGroups" :key="group.key" class="permission-group">
              <h4>{{ group.name }}</h4>
              <el-checkbox-group v-model="selectedPermissionIds">
                <el-row>
                  <el-col v-for="permission in group.permissions" :key="permission.id" :xs="24" :md="12">
                    <el-checkbox :label="permission.id">
                      {{ permission.name }} / {{ permission.code }}
                    </el-checkbox>
                  </el-col>
                </el-row>
              </el-checkbox-group>
            </section>
          </div>
        </el-col>
      </el-row>

      <template #footer>
        <el-button @click="authorizationVisible = false">キャンセル</el-button>
        <el-button type="primary" :loading="authorizationSaving" @click="submitAuthorization">権限設定を保存</el-button>
      </template>
    </el-drawer>
  </section>
</template>

<script setup lang="ts">
import { computed, nextTick, onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  createRole,
  deleteRole,
  disableRole,
  enableRole,
  fetchRoleAuthorization,
  fetchRoles,
  saveRoleAuthorization,
  updateRole,
} from '@/api/system/role'
import { CommonStatus } from '@/types/enums'
import type { RoleCreateRequest, SystemMenu, SystemPermission, SystemRole } from '@/types/system'

interface RoleFormState {
  companyId: number | null
  code: string
  name: string
  remark: string
  sort: number
}

interface PermissionGroup {
  key: string
  name: string
  permissions: SystemPermission[]
}

const loading = ref(false)
const saving = ref(false)
const switchingId = ref<number>()
const dialogVisible = ref(false)
const editingId = ref<number>()
const formRef = ref<FormInstance>()
const rows = ref<SystemRole[]>([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

const authorizationVisible = ref(false)
const authorizationLoading = ref(false)
const authorizationSaving = ref(false)
const authorizingRole = ref<SystemRole>()
const authorizationMenus = ref<SystemMenu[]>([])
const authorizationPermissions = ref<SystemPermission[]>([])
const selectedPermissionIds = ref<number[]>([])
const menuTreeRef = ref()

const form = reactive<RoleFormState>({
  companyId: null,
  code: '',
  name: '',
  remark: '',
  sort: 0,
})

const rules: FormRules<RoleFormState> = {
  name: [{ required: true, message: '名称を入力してください', trigger: 'blur' }],
  code: [{ required: true, message: 'コードを入力してください', trigger: 'blur' }],
  sort: [{ required: true, message: '表示順を入力してください', trigger: 'blur' }],
}

const authorizationMenuTree = computed(() => buildMenuTree(authorizationMenus.value))

const permissionGroups = computed<PermissionGroup[]>(() => {
  const menuById = new Map(authorizationMenus.value.map((menu) => [menu.id, menu]))
  const groups = new Map<string, PermissionGroup>()
  authorizationPermissions.value.forEach((permission) => {
    const key = permission.menuId ? String(permission.menuId) : 'ungrouped'
    const menu = permission.menuId ? menuById.get(permission.menuId) : undefined
    if (!groups.has(key)) {
      groups.set(key, {
        key,
        name: menu ? menu.name : '所属メニューなし',
        permissions: [],
      })
    }
    groups.get(key)?.permissions.push(permission)
  })
  return Array.from(groups.values())
})

const authorizationWarnings = computed(() => {
  const checkedMenus = new Set(getSelectedMenuIds())
  const selectedPermissions = authorizationPermissions.value.filter((permission) =>
    selectedPermissionIds.value.includes(permission.id),
  )
  const missingMenuNames = selectedPermissions
    .filter((permission) => permission.menuId && !checkedMenus.has(permission.menuId))
    .map((permission) => permission.name)
  if (missingMenuNames.length === 0) return []
  return [`対応メニューが選択されていない権限があります：${Array.from(new Set(missingMenuNames)).join('、')}`]
})

function buildMenuTree(list: SystemMenu[]) {
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

async function loadRoles() {
  loading.value = true
  try {
    const { data } = await fetchRoles({ page: page.value, size: size.value })
    rows.value = data.data.records
    total.value = data.data.total
  } finally {
    loading.value = false
  }
}

function resetForm() {
  editingId.value = undefined
  Object.assign(form, {
    companyId: null,
    code: '',
    name: '',
    remark: '',
    sort: 0,
  })
}

function openCreate() {
  resetForm()
  dialogVisible.value = true
}

function openEdit(row: SystemRole) {
  editingId.value = row.id
  Object.assign(form, {
    companyId: row.companyId ?? null,
    code: row.code,
    name: row.name,
    remark: row.remark ?? '',
    sort: row.sort,
  })
  dialogVisible.value = true
}

async function submit() {
  await formRef.value?.validate()
  saving.value = true
  try {
    const request = toRoleRequest()
    if (editingId.value) {
      await updateRole(editingId.value, request)
    } else {
      await createRole(request)
    }
    ElMessage.success('保存しました')
    dialogVisible.value = false
    await loadRoles()
  } finally {
    saving.value = false
  }
}

function toRoleRequest(): RoleCreateRequest {
  return {
    companyId: form.companyId,
    code: form.code,
    name: form.name,
    remark: form.remark,
    sort: form.sort,
  }
}

async function toggleStatus(row: SystemRole, enabled: boolean) {
  switchingId.value = row.id
  try {
    enabled ? await enableRole(row.id) : await disableRole(row.id)
    await loadRoles()
  } finally {
    switchingId.value = undefined
  }
}

async function confirmDelete(row: SystemRole) {
  await ElMessageBox.confirm('社員に割り当て済みのロールは削除できません。削除しますか？', 'ロール削除', {
    type: 'warning',
  })
  await deleteRole(row.id)
  ElMessage.success('削除しました')
  await loadRoles()
}

async function openAuthorization(row: SystemRole) {
  authorizingRole.value = row
  authorizationVisible.value = true
  authorizationLoading.value = true
  try {
    const { data } = await fetchRoleAuthorization(row.id)
    authorizationMenus.value = data.data.menus
    authorizationPermissions.value = data.data.permissions
    selectedPermissionIds.value = [...data.data.selectedPermissionIds]
    await nextTick()
    menuTreeRef.value?.setCheckedKeys(data.data.selectedMenuIds)
  } finally {
    authorizationLoading.value = false
  }
}

function getSelectedMenuIds() {
  const checked = (menuTreeRef.value?.getCheckedKeys(false) ?? []) as number[]
  const halfChecked = (menuTreeRef.value?.getHalfCheckedKeys() ?? []) as number[]
  return Array.from(new Set([...checked, ...halfChecked]))
}

async function submitAuthorization() {
  if (!authorizingRole.value) return
  authorizationSaving.value = true
  try {
    await saveRoleAuthorization(authorizingRole.value.id, {
      menuIds: getSelectedMenuIds(),
      permissionIds: selectedPermissionIds.value,
    })
    ElMessage.success('権限設定を保存しました')
    authorizationVisible.value = false
  } finally {
    authorizationSaving.value = false
  }
}

onMounted(loadRoles)
</script>

<style scoped>
.pager {
  margin-top: 16px;
}

.authorization-body {
  min-height: 520px;
}

.permission-groups {
  min-height: 320px;
}

.permission-group {
  margin-bottom: 18px;
}

.permission-group h4,
.authorization-body h3 {
  margin: 0 0 12px;
}

.auth-warning {
  margin-bottom: 16px;
}
</style>
