import assert from 'node:assert/strict'
import test from 'node:test'

import { buildVisibleMenuTree } from '../src/utils/currentMenuTree.ts'

function menu(
  id: number,
  parentId: number | null,
  name: string,
  sort: number,
  path: string | null = null,
) {
  return {
    id,
    parentId,
    name,
    code: `menu-${id}`,
    path,
    component: null,
    icon: null,
    type: path ? 2 : 1,
    sort,
    visible: 1,
  }
}

test('system management children are nested under the dropdown parent', () => {
  const tree = buildVisibleMenuTree([
    menu(9, 6, 'ロール管理', 930, '/admin/system/roles'),
    menu(1, null, 'ホーム', 10, '/admin'),
    menu(7, 6, 'メニュー管理', 910, '/admin/system/menus'),
    menu(6, null, 'システム管理', 900),
    menu(8, 6, '権限管理', 920, '/admin/system/permissions'),
  ])

  assert.deepEqual(
    tree.map((item) => item.name),
    ['ホーム', 'システム管理'],
  )
  assert.deepEqual(
    tree[1]?.children.map((item) => item.name),
    ['メニュー管理', '権限管理', 'ロール管理'],
  )
})

test('hidden menus are excluded before the hierarchy is built', () => {
  const hidden = { ...menu(7, 6, 'メニュー管理', 910, '/admin/system/menus'), visible: 0 }
  const tree = buildVisibleMenuTree([menu(6, null, 'システム管理', 900), hidden])

  assert.equal(tree.length, 1)
  assert.deepEqual(tree[0]?.children, [])
})
