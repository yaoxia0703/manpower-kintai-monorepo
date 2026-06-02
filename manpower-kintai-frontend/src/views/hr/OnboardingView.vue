<template>
  <section>
    <el-card shadow="never">
      <template #header>
        <el-row align="middle" justify="space-between">
          <el-col :span="12">入社登録</el-col>
          <el-col :span="12">
            <el-row justify="end">
              <el-button :loading="loadingOptions" @click="loadOptions(form.companyId)">更新</el-button>
            </el-row>
          </el-col>
        </el-row>
      </template>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="130px">
        <el-row :gutter="16">
          <el-col :xs="24" :md="12">
            <el-form-item label="会社" prop="companyId">
              <el-select
                v-model="form.companyId"
                filterable
                :disabled="companyOptions.length <= 1"
                :loading="loadingOptions"
                @change="handleCompanyChange"
              >
                <el-option
                  v-for="company in companyOptions"
                  :key="company.id"
                  :label="`${company.name}（${company.companyCode}）`"
                  :value="company.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12">
            <el-form-item label="社員番号" prop="employeeCode">
              <el-input v-model="form.employeeCode" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16">
          <el-col :xs="24" :md="12">
            <el-form-item label="姓" prop="lastName">
              <el-input v-model="form.lastName" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12">
            <el-form-item label="名" prop="firstName">
              <el-input v-model="form.firstName" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16">
          <el-col :xs="24" :md="12">
            <el-form-item label="姓（カナ）">
              <el-input v-model="form.lastNameKana" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12">
            <el-form-item label="名（カナ）">
              <el-input v-model="form.firstNameKana" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16">
          <el-col :xs="24" :md="12">
            <el-form-item label="メール" prop="email">
              <el-input v-model="form.email" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12">
            <el-form-item label="電話番号">
              <el-input v-model="form.phone" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16">
          <el-col :xs="24" :md="12">
            <el-form-item label="性別">
              <el-select v-model="form.gender" clearable>
                <el-option label="その他" :value="0" />
                <el-option label="男性" :value="1" />
                <el-option label="女性" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12">
            <el-form-item label="入社日" prop="hireDate">
              <el-date-picker v-model="form.hireDate" value-format="YYYY-MM-DD" type="date" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16">
          <el-col :xs="24" :md="12">
            <el-form-item label="所属組織" prop="nodeId">
              <el-tree-select
                v-model="form.nodeId"
                :data="nodeTreeOptions"
                filterable
                :loading="loadingOptions"
                node-key="id"
                :props="{ label: 'treeLabel', children: 'children' }"
                check-strictly
                default-expand-all
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12">
            <el-form-item label="職級" prop="gradeId">
              <el-select v-model="form.gradeId" filterable :loading="loadingOptions">
                <el-option
                  v-for="grade in gradeOptions"
                  :key="grade.id"
                  :label="`${grade.name}（${grade.gradeLevel}）`"
                  :value="grade.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16">
          <el-col :xs="24" :md="12">
            <el-form-item label="ユーザー名" prop="username">
              <el-input v-model="form.username" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12">
            <el-form-item label="初期パスワード" prop="password">
              <el-input v-model="form.password" type="password" show-password />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="ロール" prop="roleIds">
          <el-select v-model="form.roleIds" multiple filterable :loading="loadingOptions">
            <el-option
              v-for="role in roleOptions"
              :key="role.id"
              :label="`${role.name}（${role.code}）`"
              :value="role.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="saving" @click="submit">登録</el-button>
          <el-button @click="resetForm">クリア</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { fetchOnboardingOptions, onboardEmployee } from '@/api/hr'
import type {
  EmployeeOnboardingOptionsResponse,
  EmployeeOnboardingRequest,
  NodeOption,
} from '@/types/hr'

const formRef = ref<FormInstance>()
const saving = ref(false)
const loadingOptions = ref(false)
const options = ref<EmployeeOnboardingOptionsResponse>({
  selectedCompanyId: 0,
  companies: [],
  nodes: [],
  grades: [],
  roles: [],
})

const form = reactive<EmployeeOnboardingRequest>({
  companyId: 0,
  employeeCode: '',
  lastName: '',
  firstName: '',
  lastNameKana: '',
  firstNameKana: '',
  email: '',
  phone: '',
  gender: undefined,
  hireDate: '',
  nodeId: 0,
  gradeId: 0,
  roleIds: [],
  username: '',
  password: '',
})

const companyOptions = computed(() => options.value.companies)
const nodeOptions = computed(() => options.value.nodes)
const nodeTreeOptions = computed(() => buildNodeTree(options.value.nodes))
const gradeOptions = computed(() => options.value.grades)
const roleOptions = computed(() => options.value.roles)

const positiveSelectRule = {
  validator: (_rule: unknown, value: number, callback: (error?: Error) => void) => {
    if (value > 0) {
      callback()
      return
    }
    callback(new Error('選択してください'))
  },
  trigger: 'change',
}

const rules: FormRules<EmployeeOnboardingRequest> = {
  companyId: [positiveSelectRule],
  employeeCode: [{ required: true, message: '社員番号は必須です', trigger: 'blur' }],
  lastName: [{ required: true, message: '姓は必須です', trigger: 'blur' }],
  firstName: [{ required: true, message: '名は必須です', trigger: 'blur' }],
  email: [
    { required: true, message: 'メールは必須です', trigger: 'blur' },
    { type: 'email', message: 'メール形式を確認してください', trigger: 'blur' },
  ],
  hireDate: [{ required: true, message: '入社日は必須です', trigger: 'change' }],
  nodeId: [positiveSelectRule],
  gradeId: [positiveSelectRule],
  roleIds: [{ required: true, type: 'array', min: 1, message: 'ロールは必須です', trigger: 'change' }],
  username: [{ required: true, message: 'ユーザー名は必須です', trigger: 'blur' }],
  password: [{ required: true, min: 8, message: '初期パスワードは8文字以上です', trigger: 'blur' }],
}

function nodeLabel(node: NodeOption) {
  return `${'　'.repeat(Math.max(node.level - 1, 0))}${node.name}（${node.typeCode}）`
}

function buildNodeTree(nodes: NodeOption[]) {
  const map = new Map<number, NodeOption & { treeLabel: string; children: NodeOption[] }>()
  const roots: Array<NodeOption & { treeLabel: string; children: NodeOption[] }> = []

  nodes.forEach((node) => {
    map.set(node.id, {
      ...node,
      treeLabel: `${node.name} (${node.typeCode})`,
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

async function loadOptions(companyId?: number) {
  loadingOptions.value = true
  try {
    const res = await fetchOnboardingOptions(companyId && companyId > 0 ? companyId : undefined)
    options.value = res.data.data
    applyOptionDefaults()
  } catch {
    ElMessage.error('入社登録の選択肢を取得できませんでした')
  } finally {
    loadingOptions.value = false
  }
}

async function handleCompanyChange(companyId: number) {
  await loadOptions(companyId)
}

function applyOptionDefaults() {
  form.companyId = options.value.selectedCompanyId
  form.nodeId = nodeOptions.value.some((node) => node.id === form.nodeId)
    ? form.nodeId
    : (nodeOptions.value.at(-1)?.id ?? 0)
  form.gradeId = gradeOptions.value.some((grade) => grade.id === form.gradeId)
    ? form.gradeId
    : (gradeOptions.value.find((grade) => grade.code.includes('STAFF'))?.id ?? gradeOptions.value[0]?.id ?? 0)

  if (!form.roleIds.every((roleId) => roleOptions.value.some((role) => role.id === roleId))) {
    const employeeRoleId = roleOptions.value.find((role) => role.code === 'EMPLOYEE')?.id
    form.roleIds = employeeRoleId ? [employeeRoleId] : []
  }
}

async function submit() {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  saving.value = true
  try {
    await onboardEmployee(form)
    ElMessage.success('入社社員を登録しました')
    resetForm()
  } catch {
    ElMessage.error('入社登録に失敗しました')
  } finally {
    saving.value = false
  }
}

function resetForm() {
  const companyId = form.companyId
  const nodeId = form.nodeId
  const gradeId = form.gradeId
  const roleIds = [...form.roleIds]

  Object.assign(form, {
    companyId,
    employeeCode: '',
    lastName: '',
    firstName: '',
    lastNameKana: '',
    firstNameKana: '',
    email: '',
    phone: '',
    gender: undefined,
    hireDate: '',
    nodeId,
    gradeId,
    roleIds,
    username: '',
    password: '',
  })
  formRef.value?.clearValidate()
}

onMounted(() => loadOptions())
</script>
