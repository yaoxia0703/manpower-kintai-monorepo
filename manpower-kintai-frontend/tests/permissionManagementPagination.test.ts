import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'
import test from 'node:test'
import {
  buildPermissionQuery,
  resetPermissionFilters,
  submitPermissionFilters,
} from '../src/utils/permissionQuery.ts'

const permissionApi = readFileSync(
  new URL('../src/api/system/permission.ts', import.meta.url),
  'utf8',
)
const permissionQuery = readFileSync(
  new URL('../src/utils/permissionQuery.ts', import.meta.url),
  'utf8',
)
const permissionView = readFileSync(
  new URL('../src/views/system/PermissionManagementView.vue', import.meta.url),
  'utf8',
)

test('permission API exposes one typed filtered page request', () => {
  assert.match(permissionQuery, /menuId\?: number/)
  assert.match(permissionQuery, /keyword\?: string/)
  assert.match(permissionApi, /PermissionQueryParams/)
  assert.doesNotMatch(permissionApi, /fetchPermissionsByMenu/)
})

test('submitted filters are independent from later draft edits', () => {
  const draft = { keyword: ' ADMIN ', menuId: 6 }
  const applied = submitPermissionFilters(draft)

  draft.keyword = 'changed'
  draft.menuId = 9

  assert.deepEqual(buildPermissionQuery(applied, 2, 20), {
    page: 2,
    size: 20,
    menuId: 6,
    keyword: 'ADMIN',
  })
})

test('reset removes menu and whitespace-only keyword from the request', () => {
  assert.deepEqual(buildPermissionQuery(resetPermissionFilters(), 1, 50), {
    page: 1,
    size: 50,
  })
})

test('menu loading and permission loading start independently', () => {
  assert.match(permissionView, /void loadMenus\(\)/)
  assert.match(permissionView, /void loadData\(\)/)
  assert.doesNotMatch(permissionView, /await loadMenus\(\)[\s\S]*await loadData\(\)/)
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
