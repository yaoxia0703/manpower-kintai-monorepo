<template>
  <div class="approval-page">
    <header class="page-toolbar">
      <div>
        <h1>承認ワークスペース</h1>
        <el-text type="info">勤怠申請の確認と承認履歴</el-text>
      </div>
      <el-button :icon="Refresh" :loading="loading" circle aria-label="更新" @click="loadAll" />
    </header>

    <div class="approval-tabs">
      <el-segmented v-model="activeView" :options="viewOptions" />
      <el-tag v-if="pending.length > 0" type="warning" effect="plain">
        {{ pending.length }}件の承認待ち
      </el-tag>
    </div>

    <section class="table-surface">
      <el-table
        v-if="activeView === 'pending'"
        v-loading="loading"
        :data="pending"
        row-key="approvalId"
        stripe
        height="100%"
      >
        <el-table-column prop="approvalId" label="承認ID" width="90" />
        <el-table-column prop="applicantId" label="申請者ID" width="105" />
        <el-table-column label="種別" min-width="130">
          <template #default="{ row }">{{ requestTypeLabel(row.requestType) }}</template>
        </el-table-column>
        <el-table-column label="期間" min-width="190">
          <template #default="{ row }">{{ formatPeriod(row.startDate, row.endDate) }}</template>
        </el-table-column>
        <el-table-column prop="reason" label="理由" min-width="220" show-overflow-tooltip />
        <el-table-column label="進捗" width="110" align="center">
          <template #default="{ row }">
            <el-tag type="warning" effect="plain">{{ row.currentStep }} / {{ row.totalSteps }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="申請日時" width="155">
          <template #default="{ row }">{{ formatDateTime(row.submittedAt) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="238" fixed="right" align="center">
          <template #default="{ row }">
            <el-space :size="6">
              <el-tooltip content="詳細">
                <el-button circle size="small" :icon="View" aria-label="詳細" @click="openDetail(row.approvalId)" />
              </el-tooltip>
              <el-button size="small" type="success" :icon="Check" @click="openDecision(row, 'approve')">
                承認
              </el-button>
              <el-button size="small" type="danger" plain :icon="Close" @click="openDecision(row, 'reject')">
                否認
              </el-button>
              <el-tooltip content="委譲">
                <el-button circle size="small" :icon="UserFilled" aria-label="委譲" @click="openDelegate(row)" />
              </el-tooltip>
            </el-space>
          </template>
        </el-table-column>
        <template #empty><el-empty description="承認待ちはありません" /></template>
      </el-table>

      <el-table
        v-else
        v-loading="loading"
        :data="history"
        row-key="approvalId"
        stripe
        height="100%"
        @row-click="handleHistoryRowClick"
      >
        <el-table-column prop="approvalId" label="承認ID" width="90" />
        <el-table-column prop="applicantId" label="申請者ID" width="105" />
        <el-table-column label="種別" min-width="140">
          <template #default="{ row }">{{ requestTypeLabel(row.requestType) }}</template>
        </el-table-column>
        <el-table-column label="状態" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="statusMeta(row.status).type" effect="plain">
              {{ statusMeta(row.status).label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="申請日時" min-width="155">
          <template #default="{ row }">{{ formatDateTime(row.submittedAt) }}</template>
        </el-table-column>
        <el-table-column label="完了日時" min-width="155">
          <template #default="{ row }">{{ formatDateTime(row.completedAt) }}</template>
        </el-table-column>
        <el-table-column label="詳細" width="76" align="center">
          <template #default="{ row }">
            <el-button circle size="small" :icon="View" aria-label="詳細" @click.stop="openDetail(row.approvalId)" />
          </template>
        </el-table-column>
        <template #empty><el-empty description="承認履歴はありません" /></template>
      </el-table>
    </section>

    <el-dialog
      v-model="decisionVisible"
      :title="decisionAction === 'approve' ? '勤怠申請を承認' : '勤怠申請を否認'"
      width="min(500px, calc(100vw - 24px))"
    >
      <el-alert
        :type="decisionAction === 'approve' ? 'success' : 'warning'"
        :closable="false"
        show-icon
        :title="decisionTarget ? `${requestTypeLabel(decisionTarget.requestType)} / 申請者 #${decisionTarget.applicantId}` : ''"
      />
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="コメント">
          <el-input v-model="decisionComment" type="textarea" :rows="4" maxlength="500" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="decisionVisible = false">キャンセル</el-button>
        <el-button
          :type="decisionAction === 'approve' ? 'success' : 'danger'"
          :loading="submitting"
          @click="submitDecision"
        >
          {{ decisionAction === 'approve' ? '承認する' : '否認する' }}
        </el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="delegateVisible"
      title="承認を委譲"
      width="min(460px, calc(100vw - 24px))"
    >
      <el-form label-position="top">
        <el-form-item label="委譲先社員ID" required>
          <el-input-number v-model="delegateEmployeeId" :min="1" class="full-width" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="delegateVisible = false">キャンセル</el-button>
        <el-button type="primary" :loading="submitting" :disabled="delegateEmployeeId == null" @click="submitDelegate">
          委譲する
        </el-button>
      </template>
    </el-dialog>

    <el-drawer
      v-model="detailVisible"
      title="承認詳細"
      size="min(620px, 100vw)"
      destroy-on-close
    >
      <div v-loading="detailLoading" class="detail-content">
        <template v-if="detail">
          <div class="detail-header">
            <div>
              <el-text type="info">承認 #{{ detail.approvalId }}</el-text>
              <h2>{{ requestTypeLabel(detail.requestType) }}</h2>
            </div>
            <el-tag :type="statusMeta(detail.status).type" effect="plain">
              {{ statusMeta(detail.status).label }}
            </el-tag>
          </div>

          <el-descriptions :column="2" border>
            <el-descriptions-item label="申請者">#{{ detail.applicantId }}</el-descriptions-item>
            <el-descriptions-item label="申請ID">#{{ detail.requestId }}</el-descriptions-item>
            <el-descriptions-item label="期間" :span="2">
              {{ formatPeriod(detail.startDate, detail.endDate) }}
            </el-descriptions-item>
            <el-descriptions-item label="理由" :span="2">{{ detail.reason || '-' }}</el-descriptions-item>
          </el-descriptions>

          <h3>承認経路</h3>
          <el-timeline>
            <el-timeline-item
              v-for="step in detail.steps"
              :key="step.step"
              :type="statusMeta(step.status).type"
              :timestamp="formatDateTime(step.decidedAt)"
            >
              <div class="step-row">
                <strong>ステップ {{ step.step }}</strong>
                <span>承認者 #{{ step.approverId }}</span>
                <el-tag size="small" :type="statusMeta(step.status).type" effect="plain">
                  {{ statusMeta(step.status).label }}
                </el-tag>
              </div>
              <el-text v-if="step.comment" type="info">{{ step.comment }}</el-text>
            </el-timeline-item>
          </el-timeline>
        </template>
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { Check, Close, Refresh, UserFilled, View } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import {
  approveRequest,
  delegateRequest,
  fetchApprovalDetail,
  fetchApprovalHistory,
  fetchPendingApprovals,
  rejectRequest,
} from '@/api/attendance/approvals'
import type {
  ApprovalDetail,
  ApprovalHistoryItem,
  ApprovalInboxItem,
  ApprovalStatus,
  RequestType,
} from '@/types/attendance'

type DecisionAction = 'approve' | 'reject'

const route = useRoute()
const activeView = ref<'pending' | 'history'>('pending')
const viewOptions = [
  { label: '承認待ち', value: 'pending' },
  { label: '承認履歴', value: 'history' },
]
const loading = ref(false)
const submitting = ref(false)
const pending = ref<ApprovalInboxItem[]>([])
const history = ref<ApprovalHistoryItem[]>([])
const decisionVisible = ref(false)
const decisionAction = ref<DecisionAction>('approve')
const decisionTarget = ref<ApprovalInboxItem | null>(null)
const decisionComment = ref('')
const delegateVisible = ref(false)
const delegateTarget = ref<ApprovalInboxItem | null>(null)
const delegateEmployeeId = ref<number | null>(null)
const detailVisible = ref(false)
const detailLoading = ref(false)
const detail = ref<ApprovalDetail | null>(null)

async function loadAll() {
  loading.value = true
  try {
    const [pendingResponse, historyResponse] = await Promise.all([
      fetchPendingApprovals(),
      fetchApprovalHistory(),
    ])
    pending.value = pendingResponse.data.data ?? []
    history.value = historyResponse.data.data ?? []
    await openRequestedRecord()
  } catch {
    ElMessage.error('承認データを取得できませんでした')
  } finally {
    loading.value = false
  }
}

function openDecision(target: ApprovalInboxItem, action: DecisionAction) {
  decisionTarget.value = target
  decisionAction.value = action
  decisionComment.value = ''
  decisionVisible.value = true
}

async function submitDecision() {
  if (!decisionTarget.value) return
  submitting.value = true
  try {
    if (decisionAction.value === 'approve') {
      await approveRequest(decisionTarget.value.approvalId, decisionComment.value || null)
      ElMessage.success('勤怠申請を承認しました')
    } else {
      await rejectRequest(decisionTarget.value.approvalId, decisionComment.value || null)
      ElMessage.success('勤怠申請を否認しました')
    }
    decisionVisible.value = false
    await loadAll()
  } catch {
    ElMessage.error(decisionAction.value === 'approve' ? '承認に失敗しました' : '否認に失敗しました')
  } finally {
    submitting.value = false
  }
}

function openDelegate(target: ApprovalInboxItem) {
  delegateTarget.value = target
  delegateEmployeeId.value = null
  delegateVisible.value = true
}

async function submitDelegate() {
  if (!delegateTarget.value || delegateEmployeeId.value == null) return
  submitting.value = true
  try {
    await delegateRequest(delegateTarget.value.approvalId, delegateEmployeeId.value)
    ElMessage.success('承認を委譲しました')
    delegateVisible.value = false
    await loadAll()
  } catch {
    ElMessage.error('委譲に失敗しました')
  } finally {
    submitting.value = false
  }
}

async function openDetail(approvalId: number) {
  detailVisible.value = true
  detailLoading.value = true
  try {
    const response = await fetchApprovalDetail(approvalId)
    detail.value = response.data.data
  } catch {
    detailVisible.value = false
    ElMessage.error('承認詳細を取得できませんでした')
  } finally {
    detailLoading.value = false
  }
}

function handleHistoryRowClick(row: ApprovalHistoryItem) {
  void openDetail(row.approvalId)
}

async function openRequestedRecord() {
  const requestId = Number(route.query.requestId)
  if (!Number.isFinite(requestId) || requestId <= 0) return
  const pendingTarget = pending.value.find((item) => item.requestId === requestId)
  if (pendingTarget) {
    activeView.value = 'pending'
    await openDetail(pendingTarget.approvalId)
    return
  }
  const historyTarget = history.value.find((item) => item.requestId === requestId)
  if (historyTarget) {
    activeView.value = 'history'
    await openDetail(historyTarget.approvalId)
  }
}

function requestTypeLabel(type: RequestType) {
  const labels: Record<RequestType, string> = {
    PAID_LEAVE: '有給休暇',
    OVERTIME: '残業',
    BUSINESS_TRIP: '出張',
    SUBSTITUTE: '振替休暇',
    LEAVE_OF_ABSENCE: '休職',
  }
  return labels[type] ?? type
}

function statusMeta(status: ApprovalStatus) {
  const values = {
    PENDING: { label: '承認待ち', type: 'warning' as const },
    APPROVED: { label: '承認済', type: 'success' as const },
    REJECTED: { label: '否認', type: 'danger' as const },
    CANCELLED: { label: '取消', type: 'info' as const },
  }
  return values[status]
}

function formatPeriod(startDate: string, endDate: string) {
  return startDate === endDate ? startDate : `${startDate} - ${endDate}`
}

function formatDateTime(value?: string | null) {
  return value ? value.replace('T', ' ').slice(0, 16) : '-'
}

watch(() => route.query.requestId, () => void openRequestedRecord())
onMounted(loadAll)
</script>

<style scoped>
.approval-page {
  display: grid;
  min-height: 100%;
  grid-template-rows: auto auto minmax(460px, 1fr);
  gap: 12px;
  padding: 16px;
}

.page-toolbar,
.approval-tabs,
.detail-header,
.step-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

h1,
h2 {
  margin: 0 0 4px;
  letter-spacing: 0;
}

h1 {
  font-size: 22px;
}

h2 {
  font-size: 20px;
}

h3 {
  margin: 24px 0 16px;
  font-size: 16px;
}

.approval-tabs {
  justify-content: flex-start;
}

.table-surface {
  min-height: 0;
  overflow: hidden;
  border: 1px solid var(--el-border-color-light);
  background: var(--el-bg-color);
}

.dialog-form {
  margin-top: 18px;
}

.full-width {
  width: 100%;
}

.detail-content {
  min-height: 240px;
}

.detail-header {
  margin-bottom: 18px;
}

.step-row {
  justify-content: flex-start;
  margin-bottom: 4px;
}

@media (max-width: 760px) {
  .approval-page {
    grid-template-rows: auto auto minmax(560px, 1fr);
    padding: 12px;
  }

  .page-toolbar {
    align-items: flex-start;
  }

  .detail-header,
  .step-row {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
