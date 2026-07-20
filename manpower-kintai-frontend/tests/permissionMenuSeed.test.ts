import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'
import test from 'node:test'

const backendSqlRoot = new URL('../../manpower-kintai-backend/docs/sql/', import.meta.url)
const dataFiles = ['data/V1_init_MYSQL_data.sql', 'data/V1_init_MARIADB_data.sql']
const ddlFiles = ['ddl/V1_init_MYSQL_ddl.sql', 'ddl/V1_init_MARIADB_ddl.sql']

function section(sql: string, start: string, end: string) {
  const startIndex = sql.indexOf(start)
  const endIndex = sql.indexOf(end, startIndex)
  assert.notEqual(startIndex, -1)
  assert.notEqual(endIndex, -1)
  return sql.slice(startIndex, endIndex)
}

for (const dataFile of dataFiles) {
  test(`${dataFile} keeps only permissions linked to current menus`, () => {
    const sql = readFileSync(new URL(dataFile, backendSqlRoot), 'utf8')
    const menuSql = section(sql, 'insert  into `sys_menu`', '/*Data for the table `sys_permission`')
    const permissionSql = section(
      sql,
      'insert  into `sys_permission`',
      '/*Data for the table `sys_role`',
    )
    const rolePermissionSql = section(
      sql,
      'insert  into `sys_role_permission`',
      '/*Data for the table `wf_approval`',
    )

    const menuIds = new Set(
      [...menuSql.matchAll(/^\((\d+),/gm)].map((match) => Number(match[1])),
    )
    const permissions = [...permissionSql.matchAll(/^\((\d+),(\d+),'([^']+)'/gm)].map(
      (match) => ({
        id: Number(match[1]),
        menuId: Number(match[2]),
        code: match[3],
      }),
    )
    const permissionIds = new Set(permissions.map((permission) => permission.id))
    const assignedPermissionIds = [...rolePermissionSql.matchAll(/\(\d+,(\d+),/g)].map(
      (match) => Number(match[1]),
    )

    assert.equal(permissions.length, 36)
    assert.ok(permissions.every((permission) => menuIds.has(permission.menuId)))
    assert.ok(assignedPermissionIds.every((permissionId) => permissionIds.has(permissionId)))
    assert.ok(!permissions.some((permission) => permission.code.startsWith('auth:')))
    assert.ok(!permissions.some((permission) => permission.code === 'admin:role:menus'))
    assert.ok(!permissions.some((permission) => permission.code === 'admin:role:permissions'))
  })
}

for (const ddlFile of ddlFiles) {
  test(`${ddlFile} requires every permission to reference a menu`, () => {
    const sql = readFileSync(new URL(ddlFile, backendSqlRoot), 'utf8')
    const permissionDdl = section(
      sql,
      'CREATE TABLE `sys_permission`',
      '/*Table structure for table `sys_role`',
    )
    assert.match(permissionDdl, /`menu_id` bigint NOT NULL COMMENT 'メニューID'/)
  })
}
