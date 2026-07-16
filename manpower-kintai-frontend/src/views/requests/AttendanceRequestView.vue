<template>
  <div class="request-page">
    <el-card class="toolbar-card" shadow="never">
      <div class="toolbar">
        <div>
          <h1>勤怠申請</h1>
          <el-text type="info">有給休暇・残業・振替休暇の申請状況</el-text>
        </div>
        <el-space>
          <el-button
            :icon="Refresh"
            :loading="loading"
            circle
            aria-label="更新"
            @click="loadRequests"
          />
          <el-button type="primary" :icon="Plus" @click="openCreate">新規申請</el-button>
        </el-space>
      </div>
    </el-card>

    <el-row :gutter="12" class="summary-row">
      <el-col v-for="item in summary" :key="item.label" :xs="12" :md="6">
        <el-card class="summary-card" shadow="never">
          <el-statistic :title="item.label" :value="item.value" />
        </el-card>
      </el-col>
    </el-row>

    <el-card class="table-card" shadow="never" body-class="table-card-body">
      <el-table
        v-loading="loading"
        :data="requests"
        row-key="id"
        stripe
        border
        height="100%"
        class="request-table"
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
    </el-card>

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

          <el-form-item
            :label="form.requestType === 'OVERTIME' ? '申請日' : '申請期間'"
            prop="startDate"
          >
            <el-date-picker
              v-if="form.requestType === 'OVERTIME'"
              v-model="form.startDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="申請日"
              class="full-width"
              @change="syncOvertimeDate"
            />
            <el-date-picker
              v-else
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
            <el-form-item label="申請時間">
              <div class="derived-amount">{{ derivedAmountText }}</div>
            </el-form-item>
          </template>
          <el-form-item v-else label="申請日数">
            <div class="derived-amount">{{ derivedAmountText }}</div>
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
import type { ApprovalStatus, AttRequest, AttRequestPayload, RequestType } from '@/types/attendance'
import { calculateOvertimeMinutes, countWholeLeaveDays } from '@/utils/attendanceRequestAmount'

const REQUEST_TYPE_OPTIONS: Array<{ value: RequestType; label: string }> = [
  { value: 'PAID_LEAVE', label: '有給休暇' },
  { value: 'OVERTIME', label: '残業' },
  { value: 'SUBSTITUTE', label: '振替休暇' },
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

const derivedAmountText = computed(() => {
  if (form.requestType === 'OVERTIME') {
    return form.minutes == null ? '開始・終了時刻を選択してください' : `${form.minutes}分`
  }
  return form.days == null || form.days === 0 ? '申請期間を選択してください' : `${form.days}日`
})

function emptyForm(): AttRequestPayload {
  return {
    requestType: 'PAID_LEAVE',
    startDate: '',
    endDate: '',
    startTime: null,
    endTime: null,
    days: null,
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
  if (request.requestType === 'OVERTIME') form.endDate = request.startDate
  dateRange.value = [request.startDate, request.endDate]
  recalculateDerivedAmount()
  dialogVisible.value = true
}

function syncDateRange(value: [string, string] | null) {
  form.startDate = value?.[0] ?? ''
  form.endDate = value?.[1] ?? ''
  recalculateDerivedAmount()
}

function syncOvertimeDate(value: string | null) {
  form.startDate = value ?? ''
  form.endDate = form.startDate
}

function resetAmountFields() {
  if (form.requestType === 'OVERTIME') {
    form.days = null
    form.endDate = form.startDate
  } else {
    form.startTime = null
    form.endTime = null
    form.minutes = null
    dateRange.value = form.startDate ? [form.startDate, form.endDate || form.startDate] : null
  }
  recalculateDerivedAmount()
}

function calculateMinutes() {
  form.minutes = calculateOvertimeMinutes(form.startTime, form.endTime)
}

function recalculateDerivedAmount() {
  if (form.requestType === 'OVERTIME') {
    form.days = null
    calculateMinutes()
    return
  }
  form.days = countWholeLeaveDays(form.startDate, form.endDate)
  form.minutes = null
}

async function submitForm() {
  recalculateDerivedAmount()
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  if (form.requestType === 'OVERTIME' && (!form.startTime || !form.endTime || !form.minutes)) {
    ElMessage.warning('開始・終了時刻を確認してください')
    return
  }
  if (form.requestType !== 'OVERTIME' && (!form.days || form.days < 1)) {
    ElMessage.warning('申請期間には平日を含めてください')
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
  display: flex;
  height: 100%;
  min-height: 0;
  flex-direction: column;
  gap: 12px;
  overflow: hidden;
  padding: 12px;
}

.toolbar-card,
.summary-row {
  flex-shrink: 0;
}

.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

h1 {
  margin: 0 0 4px;
  font-size: 22px;
  letter-spacing: 0;
}

.summary-card :deep(.el-card__body) {
  padding: 14px 16px;
}

.table-card {
  flex: 1 1 auto;
  min-height: 0;
}

.table-card :deep(.table-card-body) {
  height: 100%;
  min-height: 0;
  padding: 0;
}

.request-table {
  height: 100%;
}

.request-table :deep(.selected-request-row > td) {
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

.derived-amount {
  width: 100%;
  min-height: 32px;
  padding: 0 12px;
  border: 1px solid var(--el-border-color-light);
  border-radius: var(--el-border-radius-base);
  background: var(--el-fill-color-light);
  color: var(--el-text-color-regular);
  line-height: 30px;
}

@media (max-width: 760px) {
  .request-page {
    padding: 12px;
  }

  .toolbar {
    align-items: flex-start;
    flex-direction: column;
  }

  .summary-row {
    row-gap: 12px;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>
