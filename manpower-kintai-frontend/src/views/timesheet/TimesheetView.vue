<template>
  <div class="timesheet-view">
    <el-card class="toolbar-card" shadow="never">
      <div class="toolbar">
        <el-space>
          <el-button circle :icon="ArrowLeft" aria-label="前月" :disabled="loading" @click="moveMonth(-1)" />
          <el-text tag="strong" class="month-label">{{ currentYear }}年{{ currentMonth }}月</el-text>
          <el-button circle :icon="ArrowRight" aria-label="次月" :disabled="loading" @click="moveMonth(1)" />
          <el-button :disabled="isCurrentMonth || loading" @click="goToday">今月</el-button>
        </el-space>

        <el-space>
          <el-tag v-if="dirtyCount > 0" type="warning" effect="plain">
            {{ dirtyCount }}件 変更中
          </el-tag>
          <el-button type="primary" :loading="saving" :disabled="dirtyCount === 0 || hasValidationError" @click="saveAll">
            保存
          </el-button>
          <el-button type="success" plain disabled>提出</el-button>
        </el-space>
      </div>
    </el-card>

    <el-row :gutter="12" class="summary-row">
      <el-col v-for="item in summaryItems" :key="item.label" :xs="12" :md="6">
        <el-card shadow="never" class="summary-card">
          <el-statistic :title="item.label" :value="item.value" />
        </el-card>
      </el-col>
    </el-row>

    <el-card class="table-card" shadow="never" body-class="table-card-body">
      <el-table
        v-loading="loading"
        :data="tableRows"
        height="100%"
        stripe
        border
        class="timesheet-table"
        row-key="workDate"
        :row-class-name="rowClassName"
      >
        <el-table-column label="日付" width="82">
          <template #default="{ row }">
            <el-tag v-if="isToday(row.workDate)" size="small">{{ formatDate(row.workDate) }}</el-tag>
            <span v-else>{{ formatDate(row.workDate) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="dayOfWeek" label="曜" width="56" align="center">
          <template #default="{ row }">
            <el-text :type="dayTextType(row)">{{ row.dayOfWeek }}</el-text>
          </template>
        </el-table-column>

        <el-table-column label="出勤区分" min-width="150">
          <template #default="{ row }">
            <el-select
              v-model="row.edit.attendanceType"
              clearable
              placeholder="選択"
              size="small"
              :disabled="isLocked(row)"
              @change="markDirty(row.workDate)"
            >
              <el-option
                v-for="option in ATTENDANCE_OPTIONS"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              />
            </el-select>
          </template>
        </el-table-column>

        <el-table-column label="始業" width="128">
          <template #default="{ row }">
            <el-form-item class="cell-form-item" :error="timeInputError(row.edit)">
              <el-input
                :model-value="row.edit.clockIn ?? ''"
                class="time-input"
                placeholder="09:00"
                maxlength="5"
                inputmode="numeric"
                size="small"
                :disabled="isLocked(row)"
                @input="setTimeInput(row.workDate, 'clockIn', $event)"
              />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column label="終業" width="128">
          <template #default="{ row }">
            <el-form-item class="cell-form-item" :error="timeInputError(row.edit)">
              <el-input
                :model-value="row.edit.clockOut ?? ''"
                class="time-input"
                placeholder="18:00"
                maxlength="5"
                inputmode="numeric"
                size="small"
                :disabled="isLocked(row)"
                @input="setTimeInput(row.workDate, 'clockOut', $event)"
              />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column label="休憩" width="112" align="right">
          <template #default="{ row }">
            <el-form-item class="cell-form-item" :error="breakError(row.edit)">
              <el-input
                :model-value="formatBreakMinutes(row.edit.breakMinutes)"
                class="break-input"
                inputmode="numeric"
                maxlength="3"
                placeholder="60"
                size="small"
                :disabled="isLocked(row)"
                @input="setBreakInput(row.workDate, $event)"
              />
            </el-form-item>
          </template>
        </el-table-column>

        <el-table-column label="実働" width="92" align="center">
          <template #default="{ row }">
            {{ toHHmm(row.edit.calcWork) }}
          </template>
        </el-table-column>

        <el-table-column label="残業" width="92" align="center">
          <template #default="{ row }">
            <el-text :type="(row.edit.calcOvertime ?? 0) > 0 ? 'warning' : 'info'">
              {{ toHHmm(row.edit.calcOvertime) }}
            </el-text>
          </template>
        </el-table-column>

        <el-table-column label="備考" min-width="180">
          <template #default="{ row }">
            <el-input
              v-model="row.edit.remark"
              size="small"
              maxlength="100"
              clearable
              :disabled="isLocked(row)"
              @input="markDirty(row.workDate)"
            />
          </template>
        </el-table-column>

        <el-table-column label="状態" width="106" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.edit.dirty" size="small" type="warning" effect="plain">
              変更中
            </el-tag>
            <el-tag v-else :type="statusMeta(row).type" size="small" effect="plain">
              {{ statusMeta(row).label }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card class="footer-card" shadow="never">
      <div class="footer-bar">
        <el-text :type="dirtyCount > 0 ? 'warning' : 'info'">
          {{ dirtyCount > 0 ? `${dirtyCount}件の変更が未保存です` : '保存済み' }}
        </el-text>
        <el-space>
          <el-button type="primary" :loading="saving" :disabled="dirtyCount === 0 || hasValidationError" @click="saveAll">
            保存
          </el-button>
          <el-button type="success" plain disabled>提出</el-button>
        </el-space>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { fetchMonthlyTimesheet, saveTimesheetRecord, deleteTimesheetRecord } from '@/api/timesheet'
import type { AttendanceType, TimesheetDay } from '@/types/timesheet'

const ATTENDANCE_OPTIONS: Array<{ value: AttendanceType; label: string }> = [
  { value: 'OFFICE', label: '出社' },
  { value: 'REMOTE', label: '在宅勤務' },
  { value: 'BUSINESS_TRIP', label: '出張' },
  { value: 'HOLIDAY_WORK', label: '休日出勤' },
]

const STANDARD_WORK_MINUTES = 480
const JP_DAYS = ['日', '月', '火', '水', '木', '金', '土']
const today = new Date()

const currentYear = ref(today.getFullYear())
const currentMonth = ref(today.getMonth() + 1)
const loading = ref(false)
const saving = ref(false)
const days = ref<TimesheetDay[]>([])
const monthCache = new Map<string, TimesheetDay[]>()
let loadSequence = 0

interface EditState {
  attendanceType: AttendanceType | null
  clockIn: string | null
  clockOut: string | null
  breakMinutes: number | null
  remark: string | null
  dirty: boolean
  calcWork: number | null
  calcOvertime: number | null
}

interface TimesheetRow extends TimesheetDay {
  edit: EditState
}

const editMap = reactive<Record<string, EditState>>({})

const isCurrentMonth = computed(
  () => currentYear.value === today.getFullYear() && currentMonth.value === today.getMonth() + 1,
)

const dirtyCount = computed(() => Object.values(editMap).filter((state) => state.dirty).length)
const liveWorkDays = computed(
  () => Object.values(editMap).filter((state) => state.calcWork != null && state.calcWork > 0).length,
)
const liveTotalWork = computed(() =>
  Object.values(editMap).reduce((sum, state) => sum + (state.calcWork ?? 0), 0),
)
const liveTotalOvertime = computed(() =>
  Object.values(editMap).reduce((sum, state) => sum + (state.calcOvertime ?? 0), 0),
)
const hasValidationError = computed(() =>
  Object.values(editMap).some((state) => !!timeInputError(state) || !!breakError(state)),
)

const summaryItems = computed(() => [
  { label: '出勤日数', value: `${liveWorkDays.value}日` },
  { label: '実働時間', value: toHHmm(liveTotalWork.value) },
  { label: '残業時間', value: toHHmm(liveTotalOvertime.value) },
  { label: '有休残日数', value: '-' },
])

const tableRows = computed<TimesheetRow[]>(() =>
  days.value.map((day) => ({
    ...day,
    edit: editMap[day.workDate] ?? initEditState(day),
  })),
)

function toHHmm(minutes: number | null | undefined): string {
  if (!minutes || minutes <= 0) return '-'
  const hours = Math.floor(minutes / 60)
  const mins = minutes % 60
  return `${String(hours).padStart(2, '0')}:${String(mins).padStart(2, '0')}`
}

function formatDate(date: string): string {
  const [, month, day] = date.split('-')
  return `${Number(month)}/${Number(day)}`
}

function isToday(date: string): boolean {
  const year = today.getFullYear()
  const month = String(today.getMonth() + 1).padStart(2, '0')
  const day = String(today.getDate()).padStart(2, '0')
  return date === `${year}-${month}-${day}`
}

function dayTextType(day: TimesheetDay) {
  if (day.dayOfWeek === '土') return 'primary'
  if (day.dayOfWeek === '日') return 'danger'
  return 'info'
}

function rowClassName({ row }: { row: TimesheetRow }) {
  if (row.requestLocked) return 'timesheet-row-request-locked'
  if (row.holiday || row.dayOfWeek === '日') return 'timesheet-row-holiday'
  if (row.dayOfWeek === '土') return 'timesheet-row-saturday'
  return ''
}

function timeToMinutes(time: string | null): number | null {
  if (!time || !isValidTimeText(time)) return null
  const [hours, minutes] = time.split(':').map(Number)
  if (hours == null || minutes == null || Number.isNaN(hours) || Number.isNaN(minutes)) return null
  return hours * 60 + minutes
}

function isValidTimeText(time: string): boolean {
  if (!/^\d{2}:\d{2}$/.test(time)) return false

  const [hours, minutes] = time.split(':').map(Number)
  return hours != null && minutes != null && hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59
}

function sanitizeTimeInput(value: string | number): string {
  const digits = String(value).replace(/\D/g, '').slice(0, 4)
  if (digits.length <= 2) return digits

  return `${digits.slice(0, 2)}:${digits.slice(2)}`
}

function formatBreakMinutes(value: number | null): string {
  return value == null ? '' : String(value)
}

function workSpanMinutes(state: EditState): number | null {
  const clockIn = timeToMinutes(state.clockIn)
  const clockOut = timeToMinutes(state.clockOut)
  if (clockIn == null || clockOut == null || clockOut <= clockIn) return null

  return clockOut - clockIn
}

function timeRangeError(state: EditState): string {
  const clockIn = timeToMinutes(state.clockIn)
  const clockOut = timeToMinutes(state.clockOut)
  if (clockIn == null || clockOut == null) return ''

  return clockOut <= clockIn ? '終業は始業より後にしてください' : ''
}

function timeFormatError(time: string | null): string {
  if (!time) return ''

  return isValidTimeText(time) ? '' : 'HH:mm形式で入力してください'
}

function timeInputError(state: EditState): string {
  return timeFormatError(state.clockIn) || timeFormatError(state.clockOut) || timeRangeError(state)
}

function breakError(state: EditState): string {
  if (state.breakMinutes != null && state.breakMinutes > 480) {
    return '休憩時間は480分以内で入力してください'
  }

  const span = workSpanMinutes(state)
  if (span == null || state.breakMinutes == null) return ''

  return state.breakMinutes >= span ? '休憩時間は勤務時間より短くしてください' : ''
}

function recalc(workDate: string): void {
  const state = editMap[workDate]
  if (!state) return

  const span = workSpanMinutes(state)
  if (span != null && !breakError(state)) {
    const work = span - (state.breakMinutes ?? 0)
    state.calcWork = work
    state.calcOvertime = Math.max(0, work - STANDARD_WORK_MINUTES)
  } else {
    state.calcWork = null
    state.calcOvertime = null
  }

  state.dirty = true
}

function setTimeInput(workDate: string, field: 'clockIn' | 'clockOut', value: string | number): void {
  const state = editMap[workDate]
  if (!state) return

  state[field] = sanitizeTimeInput(value) || null
  recalc(workDate)
}

function setBreakInput(workDate: string, value: string | number): void {
  const state = editMap[workDate]
  if (!state) return

  const digits = String(value).replace(/\D/g, '').slice(0, 3)
  state.breakMinutes = digits ? Number(digits) : null
  recalc(workDate)
}

function markDirty(workDate: string): void {
  const state = editMap[workDate]
  if (state) state.dirty = true
}

function isLocked(day: TimesheetDay): boolean {
  return day.requestLocked || day.status === 1 || day.status === 2
}

function statusMeta(day: TimesheetDay): { label: string; type: 'success' | 'warning' | 'info' } {
  if (day.requestLocked) {
    const requestLabels = {
      PAID_LEAVE: '有給',
      SUBSTITUTE: '振替休暇',
      LEAVE_OF_ABSENCE: '休職',
    }
    const requestLabel = day.lockingRequestType ? requestLabels[day.lockingRequestType] : '休暇'
    return day.lockingRequestStatus === 'APPROVED'
      ? { label: `${requestLabel}承認済`, type: 'success' }
      : { label: `${requestLabel}申請中`, type: 'warning' }
  }
  if (day.status === 1) return { label: '提出済', type: 'success' }
  if (day.status === 2) return { label: 'ロック済', type: 'info' }
  if (day.recordId) return { label: '未提出', type: 'info' }
  return { label: '-', type: 'info' }
}

function generateEmptyDays(year: number, month: number): TimesheetDay[] {
  const count = new Date(year, month, 0).getDate()
  return Array.from({ length: count }, (_, index) => {
    const day = index + 1
    const date = new Date(year, month - 1, day)
    const workDate = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`

    return {
      workDate,
      dayOfWeek: JP_DAYS[date.getDay()] ?? '',
      weekend: date.getDay() === 0 || date.getDay() === 6,
      holiday: false,
      attendanceType: null,
      clockIn: null,
      clockOut: null,
      breakMinutes: null,
      workMinutes: null,
      overtimeMinutes: null,
      remark: null,
      status: null,
      recordId: null,
      requestLocked: false,
      lockingRequestType: null,
      lockingRequestStatus: null,
    }
  })
}

function initEditState(day: TimesheetDay): EditState {
  return {
    attendanceType: day.attendanceType,
    clockIn: day.clockIn ? day.clockIn.substring(0, 5) : null,
    clockOut: day.clockOut ? day.clockOut.substring(0, 5) : null,
    breakMinutes: day.breakMinutes,
    remark: day.remark,
    dirty: false,
    calcWork: day.workMinutes,
    calcOvertime: day.overtimeMinutes,
  }
}

function monthKey(year: number, month: number): string {
  return `${year}-${String(month).padStart(2, '0')}`
}

function resetDays(monthDays: TimesheetDay[]): void {
  days.value = monthDays
  Object.keys(editMap).forEach((key) => delete editMap[key])
  monthDays.forEach((day) => {
    editMap[day.workDate] = initEditState(day)
  })
}

async function loadTimesheet(year = currentYear.value, month = currentMonth.value): Promise<void> {
  const requestId = ++loadSequence
  const key = monthKey(year, month)
  const cachedDays = monthCache.get(key)

  if (cachedDays) {
    currentYear.value = year
    currentMonth.value = month
    resetDays(cachedDays)
    return
  }

  loading.value = true
  try {
    const res = await fetchMonthlyTimesheet(year, month)
    if (requestId !== loadSequence) return

    const emptyDays = generateEmptyDays(year, month)
    const apiDays = res.data.data.days ?? []
    const mergedDays = emptyDays.map((day) => {
      const apiDay = apiDays.find((item) => item.workDate === day.workDate)
      return apiDay ? { ...day, ...apiDay } : day
    })

    monthCache.set(key, mergedDays)
    currentYear.value = year
    currentMonth.value = month
    resetDays(mergedDays)
  } catch {
    if (requestId !== loadSequence) return
    const emptyDays = generateEmptyDays(year, month)
    currentYear.value = year
    currentMonth.value = month
    resetDays(emptyDays)
    ElMessage.warning('勤務データの取得に失敗しました。空の勤務表を表示しています。')
  } finally {
    if (requestId === loadSequence) {
      loading.value = false
    }
  }
}

function hasRecordData(state: EditState): boolean {
  return !!(state.attendanceType || state.clockIn || state.clockOut || state.remark)
}

async function saveAll(): Promise<void> {
  const dirtyEntries = Object.entries(editMap).filter(([, state]) => state.dirty)
  if (dirtyEntries.length === 0) {
    ElMessage.info('変更はありません')
    return
  }

  if (hasValidationError.value) {
    ElMessage.error('勤務時間と休憩時間を確認してください')
    return
  }

  saving.value = true
  let successCount = 0
  let failureCount = 0

  for (const [workDate, state] of dirtyEntries) {
    const day = days.value.find((item) => item.workDate === workDate)
    if (day && isLocked(day)) {
      state.dirty = false
      continue
    }

    if (!hasRecordData(state) && day?.recordId) {
      try {
        await deleteTimesheetRecord(day.recordId)
        state.dirty = false
        successCount++
      } catch {
        failureCount++
      }
      continue
    }

    if (!hasRecordData(state)) {
      state.dirty = false
      continue
    }

    try {
      await saveTimesheetRecord({
        workDate,
        attendanceType: state.attendanceType,
        clockIn: state.clockIn,
        clockOut: state.clockOut,
        breakMinutes: state.breakMinutes,
        remark: state.remark,
      })
      state.dirty = false
      successCount++
    } catch {
      failureCount++
    }
  }

  saving.value = false

  if (failureCount === 0) {
    ElMessage.success(`${successCount}件を保存しました`)
    monthCache.delete(monthKey(currentYear.value, currentMonth.value))
    await loadTimesheet(currentYear.value, currentMonth.value)
  } else {
    ElMessage.warning(`保存: ${successCount}件 / 失敗: ${failureCount}件`)
  }
}

async function confirmDiscardChanges(): Promise<boolean> {
  if (dirtyCount.value === 0) return true

  try {
    await ElMessageBox.confirm('未保存の変更があります。月を切り替えますか？', '確認', {
      confirmButtonText: '切り替える',
      cancelButtonText: 'キャンセル',
      type: 'warning',
    })
    return true
  } catch {
    return false
  }
}

async function switchMonth(year: number, month: number): Promise<void> {
  if (!(await confirmDiscardChanges())) return

  await loadTimesheet(year, month)
}

async function moveMonth(offset: number): Promise<void> {
  if (loading.value) return
  const next = new Date(currentYear.value, currentMonth.value - 1 + offset, 1)
  await switchMonth(next.getFullYear(), next.getMonth() + 1)
}

async function goToday(): Promise<void> {
  if (loading.value) return
  await switchMonth(today.getFullYear(), today.getMonth() + 1)
}

onMounted(() => loadTimesheet())
</script>

<style scoped>
.timesheet-view {
  display: flex;
  height: 100%;
  min-height: 0;
  flex-direction: column;
  gap: 12px;
  overflow: hidden;
  padding: 12px;
}

.toolbar-card,
.footer-card {
  flex-shrink: 0;
}

.toolbar,
.footer-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.month-label {
  min-width: 112px;
  text-align: center;
  font-size: 16px;
}

.summary-row {
  flex-shrink: 0;
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

.timesheet-table {
  height: 100%;
}

.timesheet-table :deep(.time-input) {
  width: 104px;
}

.timesheet-table :deep(.break-input) {
  width: 92px;
}

.timesheet-table :deep(.cell-form-item) {
  margin-bottom: 0;
}

.timesheet-table :deep(.cell-form-item .el-form-item__error) {
  position: static;
  padding-top: 2px;
  text-align: left;
  white-space: normal;
}

.timesheet-table :deep(.timesheet-row-saturday) {
  --el-table-tr-bg-color: var(--el-color-primary-light-9);
}

.timesheet-table :deep(.timesheet-row-holiday) {
  --el-table-tr-bg-color: var(--el-color-danger-light-9);
}

.timesheet-table :deep(.timesheet-row-request-locked) {
  --el-table-tr-bg-color: var(--el-color-warning-light-9);
}

@media (max-width: 900px) {
  .toolbar,
  .footer-bar {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
