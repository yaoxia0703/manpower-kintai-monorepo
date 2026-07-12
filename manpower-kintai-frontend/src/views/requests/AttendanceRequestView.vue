<template>
  <div class="request-page">
    <header class="page-toolbar">
      <div>
        <h1>勤怠申請</h1>
        <el-text type="info">休暇・残業・出張などの申請状況</el-text>
      </div>
      <el-space>
        <el-button :icon="Refresh" :loading="loading" circle aria-label="更新" @click="loadRequests" />
        <el-button type="primary" :icon="Plus" @click="openCreate">新規申請</el-button>
      </el-space>
    </header>

    <div class="status-summary">
      <div v-for="item in summary" :key="item.label" class="summary-item">
        <span>{{ item.label }}</span>
        <strong>{{ item.value }}</strong>
      </div>
    </div>

    <section class="table-surface">
      <el-table
        v-loading="loading"
        :data="requests"
        row-key="id"
        stripe
        height="100%"
        :row-class-name="requestRowClass"
      >
        <el-table-column prop="id" label="申請ID" width="90" />
        <el-table-column label="種別" min-width="130">
          <template #default="{ row }">{{ requestTypeLabel(row.requestType) }}</template>
        </el-table-column>
        <el-table-column label="期間" min-width="190">
          <template #default="{ row }">{{ formatPeriod(row) }}</template>
        </el-table-column>
        <el-table-column label="申請量" width="120">
          <template #default="{ row }">{{ formatAmount(row) }}</template>
        </el-table-column>
        <el-table-column prop="reason" label="理由" min-width="220" show-overflow-tooltip />
        <el-table-column label="状態" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="statusMeta(row.status).type" effect="plain">
              {{ statusMeta(row.status).label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="申請日時" width="155">
          <template #default="{ row }">{{ formatDateTime(row.createdAt) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="132" fixed="right" align="center">
          <template #default="{ row }">
            <el-space :size="4">
              <el-tooltip content="編集">
                <el-button
                  circle
                  size="small"
                  :icon="Edit"
                  :disabled="row.status !== 'PENDING'"
                  aria-label="編集"
                  @click="openEdit(row)"
                />
              </el-tooltip>
              <el-tooltip content="取消">
                <el-button
                  circle
                  size="small"
                  type="danger"
                  plain
                  :icon="Close"
                  :disabled="row.status !== 'PENDING'"
                  aria-label="取消"
                  @click="cancelRequest(row)"
                />
              </el-tooltip>
            </el-space>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="申請はありません" />
        </template>
      </el-table>
    </section>

    <el-dialog
      v-model="dialogVisible"
      :title="editingId == null ? '新規勤怠申請' : '勤怠申請を編集'"
      width="min(620px, calc(100vw - 24px))"
      destroy-on-close
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <div class="form-grid">
          <el-form-item label="申請種別" prop="requestType">
            <el-select v-model="form.requestType" class="full-width" @change="resetAmountFields">
              <el-option
                v-for="option in REQUEST_TYPE_OPTIONS"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="申請期間" prop="startDate">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              value-format="YYYY-MM-DD"
              start-placeholder="開始日"
              end-placeholder="終了日"
              class="full-width"
              @change="syncDateRange"
            />
          </el-form-item>

          <template v-if="form.requestType === 'OVERTIME'">
            <el-form-item label="開始時刻" prop="startTime">
              <el-time-select
                v-model="form.startTime"
                start="00:00"
                step="00:30"
                end="23:30"
                class="full-width"
                @change="calculateMinutes"
              />
            </el-form-item>
            <el-form-item label="終了時刻" prop="endTime">
              <el-time-select
                v-model="form.endTime"
                start="00:00"
                step="00:30"
                end="23:30"
                class="full-width"
                @change="calculateMinutes"
              />
            </el-form-item>
            <el-form-item label="申請時間（分）" prop="minutes">
              <el-input-number v-model="form.minutes" :min="1" :max="1440" class="full-width" />
            </el-form-item>
          </template>
          <el-form-item v-else label="申請日数" prop="days">
            <el-input-number
              v-model="form.days"
              :min="0.5"
              :max="365"
              :step="0.5"
              :precision="1"
              class="full-width"
            />
          </el-form-item>
        </div>

        <el-form-item label="理由" prop="reason">
          <el-input
            v-model="form.reason"
            type="textarea"
            :rows="4"
            maxlength="500"
            show-word-limit
            placeholder="申請理由を入力してください"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">キャンセル</el-button>
        <el-button type="primary" :loading="saving" @click="submitForm">
          {{ editingId == null ? '申請する' : '更新する' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import { Close, Edit, Plus, Refresh } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  cancelAttendanceRequest,
  createAttendanceRequest,
  fetchAttendanceRequests,
  updateAttendanceRequest,
} from '@/api/attendance/requests'
import type {
  ApprovalStatus,
  AttRequest,
  AttRequestPayload,
  RequestType,
} from '@/types/attendance'

const REQUEST_TYPE_OPTIONS: Array<{ value: RequestType; label: string }> = [
  { value: 'PAID_LEAVE', label: '有給休暇' },
  { value: 'OVERTIME', label: '残業' },
  { value: 'BUSINESS_TRIP', label: '出張' },
  { value: 'SUBSTITUTE', label: '振替休暇' },
  { value: 'LEAVE_OF_ABSENCE', label: '休職' },
]

const route = useRoute()
const loading = ref(false)
const saving = ref(false)
const requests = ref<AttRequest[]>([])
const dialogVisible = ref(false)
const editingId = ref<number | null>(null)
const formRef = ref<FormInstance>()
const dateRange = ref<[string, string] | null>(null)

const form = reactive<AttRequestPayload>(emptyForm())

const rules: FormRules<AttRequestPayload> = {
  requestType: [{ required: true, message: '申請種別を選択してください', trigger: 'change' }],
  startDate: [{ required: true, message: '申請期間を選択してください', trigger: 'change' }],
  reason: [{ required: true, message: '申請理由を入力してください', trigger: 'blur' }],
}

const summary = computed(() => [
  { label: '申請中', value: countStatus('PENDING') },
  { label: '承認済', value: countStatus('APPROVED') },
  { label: '否認', value: countStatus('REJECTED') },
  { label: '取消', value: countStatus('CANCELLED') },
])

function emptyForm(): AttRequestPayload {
  return {
    requestType: 'PAID_LEAVE',
    startDate: '',
    endDate: '',
    startTime: null,
    endTime: null,
    days: 1,
    minutes: null,
    reason: '',
  }
}

async function loadRequests() {
  loading.value = true
  try {
    const response = await fetchAttendanceRequests()
    requests.value = response.data.data ?? []
  } catch {
    ElMessage.error('勤怠申請を取得できませんでした')
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editingId.value = null
  Object.assign(form, emptyForm())
  dateRange.value = null
  dialogVisible.value = true
}

function openEdit(request: AttRequest) {
  editingId.value = request.id
  Object.assign(form, {
    requestType: request.requestType,
    startDate: request.startDate,
    endDate: request.endDate,
    startTime: request.startTime,
    endTime: request.endTime,
    days: request.days,
    minutes: request.minutes,
    reason: request.reason,
  })
  dateRange.value = [request.startDate, request.endDate]
  dialogVisible.value = true
}

function syncDateRange(value: [string, string] | null) {
  form.startDate = value?.[0] ?? ''
  form.endDate = value?.[1] ?? ''
}

function resetAmountFields() {
  if (form.requestType === 'OVERTIME') {
    form.days = null
    form.minutes = null
  } else {
    form.startTime = null
    form.endTime = null
    form.minutes = null
    form.days = 1
  }
}

function calculateMinutes() {
  if (!form.startTime || !form.endTime) return
  const [startHour = 0, startMinute = 0] = form.startTime.split(':').map(Number)
  const [endHour = 0, endMinute = 0] = form.endTime.split(':').map(Number)
  const minutes = endHour * 60 + endMinute - (startHour * 60 + startMinute)
  form.minutes = minutes > 0 ? minutes : null
}

async function submitForm() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  if (form.requestType === 'OVERTIME' && (!form.startTime || !form.endTime || !form.minutes)) {
    ElMessage.warning('残業時間を入力してください')
    return
  }

  saving.value = true
  try {
    if (editingId.value == null) {
      await createAttendanceRequest({ ...form })
      ElMessage.success('勤怠申請を提出しました')
    } else {
      await updateAttendanceRequest(editingId.value, { ...form })
      ElMessage.success('勤怠申請を更新しました')
    }
    dialogVisible.value = false
    await loadRequests()
  } catch {
    ElMessage.error(editingId.value == null ? '申請に失敗しました' : '更新に失敗しました')
  } finally {
    saving.value = false
  }
}

async function cancelRequest(request: AttRequest) {
  try {
    await ElMessageBox.confirm(`申請 #${request.id} を取り消しますか？`, '申請取消', {
      confirmButtonText: '取り消す',
      cancelButtonText: '戻る',
      type: 'warning',
    })
    await cancelAttendanceRequest(request.id)
    ElMessage.success('勤怠申請を取り消しました')
    await loadRequests()
  } catch (error) {
    if (error instanceof Error) ElMessage.error('取消に失敗しました')
  }
}

function countStatus(status: ApprovalStatus) {
  return requests.value.filter((request) => request.status === status).length
}

function requestTypeLabel(type: RequestType) {
  return REQUEST_TYPE_OPTIONS.find((option) => option.value === type)?.label ?? type
}

function statusMeta(status: ApprovalStatus) {
  const values = {
    PENDING: { label: '申請中', type: 'warning' as const },
    APPROVED: { label: '承認済', type: 'success' as const },
    REJECTED: { label: '否認', type: 'danger' as const },
    CANCELLED: { label: '取消', type: 'info' as const },
  }
  return values[status]
}

function formatPeriod(request: AttRequest) {
  if (request.startDate === request.endDate) return request.startDate
  return `${request.startDate} - ${request.endDate}`
}

function formatAmount(request: AttRequest) {
  if (request.minutes != null) return `${request.minutes}分`
  if (request.days != null) return `${request.days}日`
  return '-'
}

function formatDateTime(value?: string | null) {
  return value ? value.replace('T', ' ').slice(0, 16) : '-'
}

function requestRowClass({ row }: { row: AttRequest }) {
  return Number(route.query.requestId) === row.id ? 'selected-request-row' : ''
}

onMounted(loadRequests)
</script>

<style scoped>
.request-page {
  display: grid;
  min-height: 100%;
  grid-template-rows: auto auto minmax(420px, 1fr);
  gap: 12px;
  padding: 16px;
}

.page-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

h1 {
  margin: 0 0 4px;
  font-size: 22px;
  letter-spacing: 0;
}

.status-summary {
  display: grid;
  grid-template-columns: repeat(4, minmax(120px, 1fr));
  border: 1px solid var(--el-border-color-light);
  background: var(--el-bg-color);
}

.summary-item {
  display: flex;
  min-height: 68px;
  align-items: center;
  justify-content: space-between;
  border-right: 1px solid var(--el-border-color-light);
  padding: 12px 16px;
  color: var(--el-text-color-secondary);
}

.summary-item:last-child {
  border-right: 0;
}

.summary-item strong {
  color: var(--el-text-color-primary);
  font-size: 24px;
}

.table-surface {
  min-height: 0;
  overflow: hidden;
  border: 1px solid var(--el-border-color-light);
  background: var(--el-bg-color);
}

.table-surface :deep(.selected-request-row > td) {
  background: var(--el-color-primary-light-9) !important;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 0 16px;
}

.full-width {
  width: 100%;
}

@media (max-width: 760px) {
  .request-page {
    grid-template-rows: auto auto minmax(520px, 1fr);
    padding: 12px;
  }

  .page-toolbar {
    align-items: flex-start;
  }

  .status-summary {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .summary-item:nth-child(2) {
    border-right: 0;
  }

  .summary-item:nth-child(-n + 2) {
    border-bottom: 1px solid var(--el-border-color-light);
  }

  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>
