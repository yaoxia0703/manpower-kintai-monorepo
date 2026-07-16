import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'
import test from 'node:test'

const permissionApi = readFileSync(
  new URL('../src/api/system/permission.ts', import.meta.url),
  'utf8',
)
const permissionView = readFileSync(
  new URL('../src/views/system/PermissionManagementView.vue', import.meta.url),
  'utf8',
)

test('permission API exposes one typed filtered page request', () => {
  assert.match(permissionApi, /menuId\?: number/)
  assert.match(permissionApi, /keyword\?: string/)
  assert.doesNotMatch(permissionApi, /fetchPermissionsByMenu/)
})

test('permission page uses submitted filters and server pagination', () => {
  assert.match(permissionView, /class="query-form"/)
  assert.match(permissionView, /@keyup\.enter="handleSearch"/)
  assert.match(permissionView, /@click="handleSearch"/)
  assert.match(permissionView, /@click="handleReset"/)
  assert.match(permissionView, /const appliedFilters = reactive/)
  assert.match(permissionView, /records \?\? \[\]/)
  assert.match(permissionView, /result\.total \?\? 0/)
  assert.doesNotMatch(permissionView, /fetchPermissionsByMenu/)
  assert.doesNotMatch(permissionView, />鏇存柊<\/el-button>/)
})
