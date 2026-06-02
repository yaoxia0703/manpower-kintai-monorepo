-- V2 permission administration patch for MariaDB.
-- Initializes system-management menus/permissions and makes admin SUPER_ADMIN.

START TRANSACTION;

INSERT INTO sys_menu (parent_id, name, code, path, component, icon, type, sort, visible, status, created_by, created_at, updated_by, updated_at, is_deleted)
VALUES
(NULL, 'システム管理', 'system-management', NULL, NULL, 'Setting', 1, 900, 1, 1, 1, NOW(), 1, NOW(), 0)
ON DUPLICATE KEY UPDATE
name = VALUES(name),
path = VALUES(path),
component = VALUES(component),
icon = VALUES(icon),
type = VALUES(type),
sort = VALUES(sort),
visible = VALUES(visible),
status = VALUES(status),
updated_by = VALUES(updated_by),
updated_at = NOW(),
is_deleted = 0;

SET @system_menu_id = (SELECT id FROM sys_menu WHERE code = 'system-management' AND is_deleted = 0 LIMIT 1);

INSERT INTO sys_menu (parent_id, name, code, path, component, icon, type, sort, visible, status, created_by, created_at, updated_by, updated_at, is_deleted)
VALUES
(@system_menu_id, 'メニュー管理', 'system-menus', '/admin/system/menus', 'system/MenuManagementView', 'Menu', 2, 910, 1, 1, 1, NOW(), 1, NOW(), 0),
(@system_menu_id, '権限管理', 'system-permissions', '/admin/system/permissions', 'system/PermissionManagementView', 'Key', 2, 920, 1, 1, 1, NOW(), 1, NOW(), 0),
(@system_menu_id, 'ロール管理', 'system-roles', '/admin/system/roles', 'system/RoleManagementView', 'UserCog', 2, 930, 1, 1, 1, NOW(), 1, NOW(), 0)
ON DUPLICATE KEY UPDATE
parent_id = VALUES(parent_id),
name = VALUES(name),
path = VALUES(path),
component = VALUES(component),
icon = VALUES(icon),
type = VALUES(type),
sort = VALUES(sort),
visible = VALUES(visible),
status = VALUES(status),
updated_by = VALUES(updated_by),
updated_at = NOW(),
is_deleted = 0;

SET @menu_menus_id = (SELECT id FROM sys_menu WHERE code = 'system-menus' AND is_deleted = 0 LIMIT 1);
SET @menu_permissions_id = (SELECT id FROM sys_menu WHERE code = 'system-permissions' AND is_deleted = 0 LIMIT 1);
SET @menu_roles_id = (SELECT id FROM sys_menu WHERE code = 'system-roles' AND is_deleted = 0 LIMIT 1);

INSERT INTO sys_permission (menu_id, code, name, method, path, remark, sort, status, created_by, created_at, updated_by, updated_at, is_deleted)
VALUES
(@menu_menus_id, 'admin:menu:read', 'メニュー参照', 'GET', '/admin/sys/menus/**', 'メニュー管理参照', 200, 1, 1, NOW(), 1, NOW(), 0),
(@menu_menus_id, 'admin:menu:write', 'メニュー更新', 'POST', '/admin/sys/menus/**', 'メニュー管理更新', 210, 1, 1, NOW(), 1, NOW(), 0),
(@menu_permissions_id, 'admin:permission:read', '権限参照', 'GET', '/admin/sys/permissions/**', '権限管理参照', 250, 1, 1, NOW(), 1, NOW(), 0),
(@menu_permissions_id, 'admin:permission:write', '権限更新', 'POST', '/admin/sys/permissions/**', '権限管理更新', 260, 1, 1, NOW(), 1, NOW(), 0),
(@menu_roles_id, 'admin:role:read', 'ロール参照', 'GET', '/admin/sys/roles/**', 'ロール管理参照', 300, 1, 1, NOW(), 1, NOW(), 0),
(@menu_roles_id, 'admin:role:write', 'ロール更新', 'POST', '/admin/sys/roles/**', 'ロール管理更新', 310, 1, 1, NOW(), 1, NOW(), 0)
ON DUPLICATE KEY UPDATE
menu_id = VALUES(menu_id),
name = VALUES(name),
method = VALUES(method),
path = VALUES(path),
remark = VALUES(remark),
sort = VALUES(sort),
status = VALUES(status),
updated_by = VALUES(updated_by),
updated_at = NOW(),
is_deleted = 0;

INSERT INTO sys_role (company_id, code, name, remark, sort, status, created_by, created_at, updated_by, updated_at, is_deleted)
SELECT 1, 'SUPER_ADMIN', 'スーパー管理者', '全権限管理者', 0, 1, 1, NOW(), 1, NOW(), 0
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE code = 'SUPER_ADMIN' AND is_deleted = 0);

UPDATE sys_role
SET status = 1, updated_by = 1, updated_at = NOW(), is_deleted = 0
WHERE code = 'SUPER_ADMIN';

UPDATE emp_account
SET password = '$2a$10$Zg3pJHTR0S/ysCoHid4JcOVeeLidl6QaXkBLWd6o66zSRsahIncAe',
    status = 1,
    updated_by = 1,
    updated_at = NOW()
WHERE username = 'admin' AND is_deleted = 0;

SET @admin_employee_id = (SELECT employee_id FROM emp_account WHERE username = 'admin' AND is_deleted = 0 LIMIT 1);
SET @admin_company_id = (SELECT company_id FROM emp_employee WHERE id = @admin_employee_id AND is_deleted = 0 LIMIT 1);
SET @super_admin_role_id = (SELECT id FROM sys_role WHERE code = 'SUPER_ADMIN' AND is_deleted = 0 ORDER BY id LIMIT 1);

DELETE FROM sys_employee_role WHERE employee_id = @admin_employee_id;

INSERT INTO sys_employee_role (employee_id, role_id, company_id, start_date, end_date, created_by, created_at, updated_by, updated_at, is_deleted)
SELECT @admin_employee_id, @super_admin_role_id, @admin_company_id, CURRENT_DATE, NULL, 1, NOW(), 1, NOW(), 0
WHERE @admin_employee_id IS NOT NULL AND @super_admin_role_id IS NOT NULL;

DELETE rpm
FROM sys_role_permission rpm
JOIN sys_permission p ON p.id = rpm.permission_id
WHERE rpm.role_id = @super_admin_role_id
  AND p.code NOT IN (
    'admin:menu:read',
    'admin:menu:write',
    'admin:permission:read',
    'admin:permission:write',
    'admin:role:read',
    'admin:role:write'
  );

INSERT IGNORE INTO sys_role_permission (role_id, permission_id, created_by, created_at)
SELECT @super_admin_role_id, id, 1, NOW()
FROM sys_permission
WHERE code IN (
  'admin:menu:read',
  'admin:menu:write',
  'admin:permission:read',
  'admin:permission:write',
  'admin:role:read',
  'admin:role:write'
) AND is_deleted = 0;

INSERT IGNORE INTO sys_role_menu (role_id, menu_id, created_by, created_at)
SELECT @super_admin_role_id, id, 1, NOW()
FROM sys_menu
WHERE code IN ('home', 'system-management', 'system-menus', 'system-permissions', 'system-roles')
  AND is_deleted = 0;

COMMIT;
