/*
  既存データベース向け権限整理。
  現在の画面メニューから利用する権限だけを残し、全権限を所属メニューへ関連付ける。
  MySQL 8.0 / MariaDB 11.4 共通。
*/

START TRANSACTION;

UPDATE `sys_permission`
SET `menu_id` = CASE
    WHEN `code` IN (
        'employee:timesheet:read',
        'employee:timesheet:write',
        'employee:timesheet:delete'
    ) THEN 2
    WHEN `code` = 'manager:subordinate:read' THEN 3
    WHEN `code` IN (
        'hr:employee:onboard',
        'hr:onboarding:options'
    ) THEN 4
    WHEN `code` IN (
        'admin:menu:read',
        'admin:menu:write',
        'admin:menu:update',
        'admin:menu:show',
        'admin:menu:hide',
        'admin:menu:enable',
        'admin:menu:disable',
        'admin:menu:delete'
    ) THEN 7
    WHEN `code` IN (
        'admin:permission:read',
        'admin:permission:write',
        'admin:permission:update',
        'admin:permission:enable',
        'admin:permission:disable',
        'admin:permission:delete'
    ) THEN 8
    WHEN `code` IN (
        'admin:role:read',
        'admin:role:write',
        'admin:role:update',
        'admin:role:authorization',
        'admin:role:enable',
        'admin:role:disable',
        'admin:role:delete'
    ) THEN 9
    WHEN `code` IN (
        'employee:request:read',
        'employee:request:create',
        'employee:request:update',
        'employee:request:cancel'
    ) THEN 10
    WHEN `code` IN (
        'manager:approval:read',
        'manager:approval:approve',
        'manager:approval:reject',
        'manager:approval:delegate',
        'manager:approval:delegate-search'
    ) THEN 11
    ELSE `menu_id`
END
WHERE `code` IN (
    'employee:timesheet:read',
    'employee:timesheet:write',
    'employee:timesheet:delete',
    'manager:subordinate:read',
    'hr:employee:onboard',
    'hr:onboarding:options',
    'admin:menu:read',
    'admin:menu:write',
    'admin:menu:update',
    'admin:menu:show',
    'admin:menu:hide',
    'admin:menu:enable',
    'admin:menu:disable',
    'admin:menu:delete',
    'admin:permission:read',
    'admin:permission:write',
    'admin:permission:update',
    'admin:permission:enable',
    'admin:permission:disable',
    'admin:permission:delete',
    'admin:role:read',
    'admin:role:write',
    'admin:role:update',
    'admin:role:authorization',
    'admin:role:enable',
    'admin:role:disable',
    'admin:role:delete',
    'employee:request:read',
    'employee:request:create',
    'employee:request:update',
    'employee:request:cancel',
    'manager:approval:read',
    'manager:approval:approve',
    'manager:approval:reject',
    'manager:approval:delegate',
    'manager:approval:delegate-search'
);

DELETE role_permission
FROM `sys_role_permission` role_permission
LEFT JOIN `sys_permission` permission
    ON permission.`id` = role_permission.`permission_id`
WHERE permission.`id` IS NULL
   OR permission.`code` NOT IN (
       'employee:timesheet:read',
       'employee:timesheet:write',
       'employee:timesheet:delete',
       'manager:subordinate:read',
       'hr:employee:onboard',
       'hr:onboarding:options',
       'admin:menu:read',
       'admin:menu:write',
       'admin:menu:update',
       'admin:menu:show',
       'admin:menu:hide',
       'admin:menu:enable',
       'admin:menu:disable',
       'admin:menu:delete',
       'admin:permission:read',
       'admin:permission:write',
       'admin:permission:update',
       'admin:permission:enable',
       'admin:permission:disable',
       'admin:permission:delete',
       'admin:role:read',
       'admin:role:write',
       'admin:role:update',
       'admin:role:authorization',
       'admin:role:enable',
       'admin:role:disable',
       'admin:role:delete',
       'employee:request:read',
       'employee:request:create',
       'employee:request:update',
       'employee:request:cancel',
       'manager:approval:read',
       'manager:approval:approve',
       'manager:approval:reject',
       'manager:approval:delegate',
       'manager:approval:delegate-search'
   );

DELETE FROM `sys_permission`
WHERE `code` NOT IN (
    'employee:timesheet:read',
    'employee:timesheet:write',
    'employee:timesheet:delete',
    'manager:subordinate:read',
    'hr:employee:onboard',
    'hr:onboarding:options',
    'admin:menu:read',
    'admin:menu:write',
    'admin:menu:update',
    'admin:menu:show',
    'admin:menu:hide',
    'admin:menu:enable',
    'admin:menu:disable',
    'admin:menu:delete',
    'admin:permission:read',
    'admin:permission:write',
    'admin:permission:update',
    'admin:permission:enable',
    'admin:permission:disable',
    'admin:permission:delete',
    'admin:role:read',
    'admin:role:write',
    'admin:role:update',
    'admin:role:authorization',
    'admin:role:enable',
    'admin:role:disable',
    'admin:role:delete',
    'employee:request:read',
    'employee:request:create',
    'employee:request:update',
    'employee:request:cancel',
    'manager:approval:read',
    'manager:approval:approve',
    'manager:approval:reject',
    'manager:approval:delegate',
    'manager:approval:delegate-search'
);

COMMIT;

ALTER TABLE `sys_permission`
    MODIFY COLUMN `menu_id` bigint NOT NULL COMMENT 'メニューID';

