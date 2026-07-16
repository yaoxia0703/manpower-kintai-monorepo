import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'
import test from 'node:test'

const attendanceRequestView = readFileSync(
  new URL('../src/views/requests/AttendanceRequestView.vue', import.meta.url),
  'utf8',
)
const approvalWorkspaceView = readFileSync(
  new URL('../src/views/approvals/ApprovalWorkspaceView.vue', import.meta.url),
  'utf8',
)

test('attendance request uses the same dashboard card structure as timesheet', () => {
  assert.match(attendanceRequestView, /<el-card class="toolbar-card" shadow="never">/)
  assert.match(attendanceRequestView, /class="summary-row"/)
  assert.match(attendanceRequestView, /class="summary-card"/)
  assert.match(attendanceRequestView, /<el-card class="table-card" shadow="never"/)
  assert.doesNotMatch(attendanceRequestView, /class="page-toolbar"/)
  assert.doesNotMatch(attendanceRequestView, /class="table-surface"/)
})

test('approval workspace uses the same single-card structure as subordinate management', () => {
  assert.match(approvalWorkspaceView, /<section class="page-section">/)
  assert.match(approvalWorkspaceView, /<el-card class="approval-card" shadow="never"/)
  assert.match(approvalWorkspaceView, /<template #header>/)
  assert.match(approvalWorkspaceView, /class="card-header"/)
  assert.match(approvalWorkspaceView, /<el-table[\s\S]*?border/)
  assert.doesNotMatch(approvalWorkspaceView, /class="page-toolbar"/)
  assert.doesNotMatch(approvalWorkspaceView, /class="table-surface"/)
})
