/*
 * Japan organization seed data.
 *
 * Test account password: 12345678
 */

USE `manpower_kintai`;

START TRANSACTION;

SET @operator_id = 1;
SET @today = CURDATE();
SET @default_password_hash = '$2a$10$JU8LQtgNkeiljdkCVTLO5.dYc5ivOYhz5BumavSA58RcbSyB0..my';

/* -------------------------------------------------------------------------- */
/* Company                                                                    */
/* -------------------------------------------------------------------------- */

INSERT INTO org_company (
    parent_id,
    name,
    company_code,
    level,
    sort,
    status,
    created_by,
    updated_by,
    is_deleted
)
VALUES (
    NULL,
    'ManpowerGroup',
    'MPG',
    1,
    0,
    1,
    @operator_id,
    @operator_id,
    0
)
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    level = VALUES(level),
    sort = VALUES(sort),
    status = VALUES(status),
    updated_by = VALUES(updated_by),
    is_deleted = 0;

SELECT @global_company_id := id
FROM org_company
WHERE company_code = 'MPG'
  AND is_deleted = 0
LIMIT 1;

INSERT INTO org_company (
    parent_id,
    name,
    company_code,
    level,
    sort,
    status,
    created_by,
    updated_by,
    is_deleted
)
VALUES (
    @global_company_id,
    'マンパワーグループ株式会社',
    'MPG_JP',
    2,
    10,
    1,
    @operator_id,
    @operator_id,
    0
)
ON DUPLICATE KEY UPDATE
    parent_id = VALUES(parent_id),
    name = VALUES(name),
    level = VALUES(level),
    sort = VALUES(sort),
    status = VALUES(status),
    updated_by = VALUES(updated_by),
    is_deleted = 0;

SELECT @jp_company_id := id
FROM org_company
WHERE company_code = 'MPG_JP'
  AND is_deleted = 0
LIMIT 1;

/* -------------------------------------------------------------------------- */
/* Grade                                                                      */
/* -------------------------------------------------------------------------- */

INSERT INTO org_grade (company_id, name, code, grade_level, sort, status, created_by, updated_by, is_deleted)
VALUES (@jp_company_id, '部長', 'JP_MANAGER', 'L2', 20, 1, @operator_id, @operator_id, 0)
ON DUPLICATE KEY UPDATE name = '部長', grade_level = 'L2', sort = 20, status = 1, updated_by = @operator_id, is_deleted = 0;

INSERT INTO org_grade (company_id, name, code, grade_level, sort, status, created_by, updated_by, is_deleted)
VALUES (@jp_company_id, 'リーダー', 'JP_LEADER', 'L3', 30, 1, @operator_id, @operator_id, 0)
ON DUPLICATE KEY UPDATE name = 'リーダー', grade_level = 'L3', sort = 30, status = 1, updated_by = @operator_id, is_deleted = 0;

INSERT INTO org_grade (company_id, name, code, grade_level, sort, status, created_by, updated_by, is_deleted)
VALUES (@jp_company_id, '一般社員', 'JP_STAFF', 'L5', 50, 1, @operator_id, @operator_id, 0)
ON DUPLICATE KEY UPDATE name = '一般社員', grade_level = 'L5', sort = 50, status = 1, updated_by = @operator_id, is_deleted = 0;

SELECT @manager_grade_id := id
FROM org_grade
WHERE company_id = @jp_company_id
  AND code = 'JP_MANAGER'
  AND is_deleted = 0
LIMIT 1;

SELECT @leader_grade_id := id
FROM org_grade
WHERE company_id = @jp_company_id
  AND code = 'JP_LEADER'
  AND is_deleted = 0
LIMIT 1;

SELECT @staff_grade_id := id
FROM org_grade
WHERE company_id = @jp_company_id
  AND code = 'JP_STAFF'
  AND is_deleted = 0
LIMIT 1;

/* -------------------------------------------------------------------------- */
/* Organization nodes                                                         */
/* -------------------------------------------------------------------------- */

INSERT INTO org_node (
    company_id,
    parent_id,
    manager_id,
    name,
    type_code,
    dept_function,
    code,
    level,
    sort,
    status,
    created_by,
    updated_by,
    is_deleted
)
VALUES
    (@jp_company_id, NULL, NULL, 'エクスペリス事業本部', 'DIVISION', 'GENERAL', 'JP_EXP_DIV', 1, 10, 1, @operator_id, @operator_id, 0),
    (@jp_company_id, NULL, NULL, '人事部', 'DEPT', 'HR', 'JP_HR_DEPT', 1, 20, 1, @operator_id, @operator_id, 0)
ON DUPLICATE KEY UPDATE
    parent_id = VALUES(parent_id),
    name = VALUES(name),
    type_code = VALUES(type_code),
    dept_function = VALUES(dept_function),
    level = VALUES(level),
    sort = VALUES(sort),
    status = VALUES(status),
    updated_by = VALUES(updated_by),
    is_deleted = 0;

SELECT @exp_node_id := id
FROM org_node
WHERE company_id = @jp_company_id
  AND code = 'JP_EXP_DIV'
  AND is_deleted = 0
LIMIT 1;

SELECT @hr_node_id := id
FROM org_node
WHERE company_id = @jp_company_id
  AND code = 'JP_HR_DEPT'
  AND is_deleted = 0
LIMIT 1;

INSERT INTO org_node (
    company_id,
    parent_id,
    manager_id,
    name,
    type_code,
    dept_function,
    code,
    level,
    sort,
    status,
    created_by,
    updated_by,
    is_deleted
)
VALUES (
    @jp_company_id,
    @exp_node_id,
    NULL,
    'サービスデリバリー',
    'DEPT',
    'GENERAL',
    'JP_EXP_SERVICE_DELIVERY',
    2,
    10,
    1,
    @operator_id,
    @operator_id,
    0
)
ON DUPLICATE KEY UPDATE
    parent_id = VALUES(parent_id),
    name = VALUES(name),
    type_code = VALUES(type_code),
    dept_function = VALUES(dept_function),
    level = VALUES(level),
    sort = VALUES(sort),
    status = VALUES(status),
    updated_by = VALUES(updated_by),
    is_deleted = 0;

SELECT @service_delivery_node_id := id
FROM org_node
WHERE company_id = @jp_company_id
  AND code = 'JP_EXP_SERVICE_DELIVERY'
  AND is_deleted = 0
LIMIT 1;

INSERT INTO org_node (
    company_id,
    parent_id,
    manager_id,
    name,
    type_code,
    dept_function,
    code,
    level,
    sort,
    status,
    created_by,
    updated_by,
    is_deleted
)
VALUES (
    @jp_company_id,
    @service_delivery_node_id,
    NULL,
    '西日本テクニカル',
    'SECTION',
    'GENERAL',
    'JP_EXP_WEST_TECH',
    3,
    10,
    1,
    @operator_id,
    @operator_id,
    0
)
ON DUPLICATE KEY UPDATE
    parent_id = VALUES(parent_id),
    name = VALUES(name),
    type_code = VALUES(type_code),
    dept_function = VALUES(dept_function),
    level = VALUES(level),
    sort = VALUES(sort),
    status = VALUES(status),
    updated_by = VALUES(updated_by),
    is_deleted = 0;

SELECT @west_tech_node_id := id
FROM org_node
WHERE company_id = @jp_company_id
  AND code = 'JP_EXP_WEST_TECH'
  AND is_deleted = 0
LIMIT 1;

INSERT INTO org_node (
    company_id,
    parent_id,
    manager_id,
    name,
    type_code,
    dept_function,
    code,
    level,
    sort,
    status,
    created_by,
    updated_by,
    is_deleted
)
VALUES (
    @jp_company_id,
    @west_tech_node_id,
    NULL,
    '第2ソリューション',
    'SECTION',
    'GENERAL',
    'JP_EXP_SOLUTION_2',
    4,
    10,
    1,
    @operator_id,
    @operator_id,
    0
)
ON DUPLICATE KEY UPDATE
    parent_id = VALUES(parent_id),
    name = VALUES(name),
    type_code = VALUES(type_code),
    dept_function = VALUES(dept_function),
    level = VALUES(level),
    sort = VALUES(sort),
    status = VALUES(status),
    updated_by = VALUES(updated_by),
    is_deleted = 0;

SELECT @solution2_node_id := id
FROM org_node
WHERE company_id = @jp_company_id
  AND code = 'JP_EXP_SOLUTION_2'
  AND is_deleted = 0
LIMIT 1;

INSERT INTO org_node (
    company_id,
    parent_id,
    manager_id,
    name,
    type_code,
    dept_function,
    code,
    level,
    sort,
    status,
    created_by,
    updated_by,
    is_deleted
)
VALUES (
    @jp_company_id,
    @solution2_node_id,
    NULL,
    'ITエンジニアリング',
    'TEAM',
    'GENERAL',
    'JP_EXP_IT_ENGINEERING',
    5,
    10,
    1,
    @operator_id,
    @operator_id,
    0
)
ON DUPLICATE KEY UPDATE
    parent_id = VALUES(parent_id),
    name = VALUES(name),
    type_code = VALUES(type_code),
    dept_function = VALUES(dept_function),
    level = VALUES(level),
    sort = VALUES(sort),
    status = VALUES(status),
    updated_by = VALUES(updated_by),
    is_deleted = 0;

SELECT @it_engineering_node_id := id
FROM org_node
WHERE company_id = @jp_company_id
  AND code = 'JP_EXP_IT_ENGINEERING'
  AND is_deleted = 0
LIMIT 1;

/* Refresh closure rows for this seed tree. */
DELETE FROM org_node_closure
WHERE descendant_id IN (
    @exp_node_id,
    @hr_node_id,
    @service_delivery_node_id,
    @west_tech_node_id,
    @solution2_node_id,
    @it_engineering_node_id
);

INSERT IGNORE INTO org_node_closure (ancestor_id, descendant_id, depth)
VALUES
    (@exp_node_id, @exp_node_id, 0),
    (@hr_node_id, @hr_node_id, 0);

INSERT IGNORE INTO org_node_closure (ancestor_id, descendant_id, depth)
SELECT ancestor_id, @service_delivery_node_id, depth + 1
FROM org_node_closure
WHERE descendant_id = @exp_node_id
UNION ALL
SELECT @service_delivery_node_id, @service_delivery_node_id, 0;

INSERT IGNORE INTO org_node_closure (ancestor_id, descendant_id, depth)
SELECT ancestor_id, @west_tech_node_id, depth + 1
FROM org_node_closure
WHERE descendant_id = @service_delivery_node_id
UNION ALL
SELECT @west_tech_node_id, @west_tech_node_id, 0;

INSERT IGNORE INTO org_node_closure (ancestor_id, descendant_id, depth)
SELECT ancestor_id, @solution2_node_id, depth + 1
FROM org_node_closure
WHERE descendant_id = @west_tech_node_id
UNION ALL
SELECT @solution2_node_id, @solution2_node_id, 0;

INSERT IGNORE INTO org_node_closure (ancestor_id, descendant_id, depth)
SELECT ancestor_id, @it_engineering_node_id, depth + 1
FROM org_node_closure
WHERE descendant_id = @solution2_node_id
UNION ALL
SELECT @it_engineering_node_id, @it_engineering_node_id, 0;

/* -------------------------------------------------------------------------- */
/* RBAC for Japan company                                                     */
/* -------------------------------------------------------------------------- */

INSERT INTO sys_role (
    company_id,
    code,
    name,
    remark,
    sort,
    status,
    created_by,
    updated_by,
    is_deleted
)
VALUES
    (@jp_company_id, 'EMPLOYEE', '一般社員', '日本会社一般社員ロール', 10, 1, @operator_id, @operator_id, 0),
    (@jp_company_id, 'DEPT_MANAGER', '部門管理者', '日本会社部門管理者ロール', 20, 1, @operator_id, @operator_id, 0),
    (@jp_company_id, 'HR_ADMIN', '人事管理者', '日本会社人事管理者ロール', 30, 1, @operator_id, @operator_id, 0)
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    remark = VALUES(remark),
    sort = VALUES(sort),
    status = VALUES(status),
    updated_by = VALUES(updated_by),
    is_deleted = 0;

SELECT @jp_employee_role_id := id
FROM sys_role
WHERE company_id = @jp_company_id
  AND code = 'EMPLOYEE'
  AND is_deleted = 0
LIMIT 1;

SELECT @jp_manager_role_id := id
FROM sys_role
WHERE company_id = @jp_company_id
  AND code = 'DEPT_MANAGER'
  AND is_deleted = 0
LIMIT 1;

SELECT @jp_hr_role_id := id
FROM sys_role
WHERE company_id = @jp_company_id
  AND code = 'HR_ADMIN'
  AND is_deleted = 0
LIMIT 1;

INSERT IGNORE INTO sys_role_menu (role_id, menu_id, created_by)
SELECT role_id, menu_id, @operator_id
FROM (
    SELECT @jp_employee_role_id AS role_id, id AS menu_id
    FROM sys_menu
    WHERE code IN ('home', 'timesheet')
      AND is_deleted = 0
    UNION ALL
    SELECT @jp_manager_role_id AS role_id, id AS menu_id
    FROM sys_menu
    WHERE code IN ('home', 'timesheet', 'manager-subordinates')
      AND is_deleted = 0
    UNION ALL
    SELECT @jp_hr_role_id AS role_id, id AS menu_id
    FROM sys_menu
    WHERE code IN ('home', 'timesheet', 'hr-onboarding')
      AND is_deleted = 0
    UNION ALL
    SELECT role.id AS role_id, menu.id AS menu_id
    FROM sys_role role
    CROSS JOIN sys_menu menu
    WHERE role.code = 'SUPER_ADMIN'
      AND role.is_deleted = 0
      AND menu.is_deleted = 0
) role_menu_seed;

INSERT IGNORE INTO sys_role_permission (role_id, permission_id, created_by)
SELECT role_id, permission_id, @operator_id
FROM (
    SELECT @jp_employee_role_id AS role_id, id AS permission_id
    FROM sys_permission
    WHERE code IN ('employee:timesheet:read', 'employee:timesheet:write')
      AND is_deleted = 0
    UNION ALL
    SELECT @jp_manager_role_id AS role_id, id AS permission_id
    FROM sys_permission
    WHERE code IN ('employee:timesheet:read', 'employee:timesheet:write', 'manager:subordinate:read')
      AND is_deleted = 0
    UNION ALL
    SELECT @jp_hr_role_id AS role_id, id AS permission_id
    FROM sys_permission
    WHERE code IN ('employee:timesheet:read', 'employee:timesheet:write', 'hr:employee:onboard')
      AND is_deleted = 0
    UNION ALL
    SELECT role.id AS role_id, permission.id AS permission_id
    FROM sys_role role
    CROSS JOIN sys_permission permission
    WHERE role.code = 'SUPER_ADMIN'
      AND role.is_deleted = 0
      AND permission.is_deleted = 0
) role_permission_seed;

/* -------------------------------------------------------------------------- */
/* Employees, accounts and positions                                          */
/* -------------------------------------------------------------------------- */

INSERT INTO emp_employee (
    company_id,
    employee_code,
    last_name,
    first_name,
    last_name_kana,
    first_name_kana,
    email,
    phone,
    gender,
    birth_date,
    hire_date,
    leave_date,
    status,
    created_by,
    updated_by,
    is_deleted
)
VALUES
    (@jp_company_id, 'JP-HR-001', '佐藤', '花子', 'サトウ', 'ハナコ', 'testHR@manpower.jp', NULL, 2, NULL, @today, NULL, 1, @operator_id, @operator_id, 0),
    (@jp_company_id, 'JP-MGR-EXP', '田中', '太郎', 'タナカ', 'タロウ', 'testManagerEXP@manpower.jp', NULL, 1, NULL, @today, NULL, 1, @operator_id, @operator_id, 0),
    (@jp_company_id, 'JP-MGR-SD', '鈴木', '一郎', 'スズキ', 'イチロウ', 'testManagerSD@manpower.jp', NULL, 1, NULL, @today, NULL, 1, @operator_id, @operator_id, 0),
    (@jp_company_id, 'JP-MGR-WT', '高橋', '健', 'タカハシ', 'ケン', 'testManagerWT@manpower.jp', NULL, 1, NULL, @today, NULL, 1, @operator_id, @operator_id, 0),
    (@jp_company_id, 'JP-MGR-SOL2', '伊藤', '誠', 'イトウ', 'マコト', 'testManagerSOL2@manpower.jp', NULL, 1, NULL, @today, NULL, 1, @operator_id, @operator_id, 0),
    (@jp_company_id, 'JP-MGR-ITENG', '山本', '拓也', 'ヤマモト', 'タクヤ', 'testManagerIT@manpower.jp', NULL, 1, NULL, @today, NULL, 1, @operator_id, @operator_id, 0),
    (@jp_company_id, 'JP-EMP-HR-001', '小林', '優子', 'コバヤシ', 'ユウコ', 'testEmployeeHR@manpower.jp', NULL, 2, NULL, @today, NULL, 1, @operator_id, @operator_id, 0),
    (@jp_company_id, 'JP-EMP-SD-001', '加藤', '翔', 'カトウ', 'ショウ', 'testEmployeeSD@manpower.jp', NULL, 1, NULL, @today, NULL, 1, @operator_id, @operator_id, 0),
    (@jp_company_id, 'JP-EMP-WT-001', '吉田', '直樹', 'ヨシダ', 'ナオキ', 'testEmployeeWT@manpower.jp', NULL, 1, NULL, @today, NULL, 1, @operator_id, @operator_id, 0),
    (@jp_company_id, 'JP-EMP-SOL2-001', '松本', '彩', 'マツモト', 'アヤ', 'testEmployeeSOL2@manpower.jp', NULL, 2, NULL, @today, NULL, 1, @operator_id, @operator_id, 0),
    (@jp_company_id, 'JP-EMP-ITENG-001', '中村', '美咲', 'ナカムラ', 'ミサキ', 'testEmployeeIT1@manpower.jp', NULL, 2, NULL, @today, NULL, 1, @operator_id, @operator_id, 0),
    (@jp_company_id, 'JP-EMP-ITENG-002', '森', '大輔', 'モリ', 'ダイスケ', 'testEmployeeIT2@manpower.jp', NULL, 1, NULL, @today, NULL, 1, @operator_id, @operator_id, 0)
ON DUPLICATE KEY UPDATE
    company_id = VALUES(company_id),
    last_name = VALUES(last_name),
    first_name = VALUES(first_name),
    last_name_kana = VALUES(last_name_kana),
    first_name_kana = VALUES(first_name_kana),
    email = VALUES(email),
    gender = VALUES(gender),
    hire_date = VALUES(hire_date),
    status = VALUES(status),
    updated_by = VALUES(updated_by),
    is_deleted = 0;

SELECT @hr_employee_id := id FROM emp_employee WHERE company_id = @jp_company_id AND employee_code = 'JP-HR-001' AND is_deleted = 0 LIMIT 1;
SELECT @exp_manager_id := id FROM emp_employee WHERE company_id = @jp_company_id AND employee_code = 'JP-MGR-EXP' AND is_deleted = 0 LIMIT 1;
SELECT @sd_manager_id := id FROM emp_employee WHERE company_id = @jp_company_id AND employee_code = 'JP-MGR-SD' AND is_deleted = 0 LIMIT 1;
SELECT @west_tech_manager_id := id FROM emp_employee WHERE company_id = @jp_company_id AND employee_code = 'JP-MGR-WT' AND is_deleted = 0 LIMIT 1;
SELECT @solution2_manager_id := id FROM emp_employee WHERE company_id = @jp_company_id AND employee_code = 'JP-MGR-SOL2' AND is_deleted = 0 LIMIT 1;
SELECT @iteng_manager_id := id FROM emp_employee WHERE company_id = @jp_company_id AND employee_code = 'JP-MGR-ITENG' AND is_deleted = 0 LIMIT 1;
SELECT @hr_staff_employee_id := id FROM emp_employee WHERE company_id = @jp_company_id AND employee_code = 'JP-EMP-HR-001' AND is_deleted = 0 LIMIT 1;
SELECT @sd_staff_employee_id := id FROM emp_employee WHERE company_id = @jp_company_id AND employee_code = 'JP-EMP-SD-001' AND is_deleted = 0 LIMIT 1;
SELECT @west_tech_staff_employee_id := id FROM emp_employee WHERE company_id = @jp_company_id AND employee_code = 'JP-EMP-WT-001' AND is_deleted = 0 LIMIT 1;
SELECT @solution2_staff_employee_id := id FROM emp_employee WHERE company_id = @jp_company_id AND employee_code = 'JP-EMP-SOL2-001' AND is_deleted = 0 LIMIT 1;
SELECT @iteng_staff_employee_id := id FROM emp_employee WHERE company_id = @jp_company_id AND employee_code = 'JP-EMP-ITENG-001' AND is_deleted = 0 LIMIT 1;
SELECT @iteng_staff2_employee_id := id FROM emp_employee WHERE company_id = @jp_company_id AND employee_code = 'JP-EMP-ITENG-002' AND is_deleted = 0 LIMIT 1;

INSERT INTO emp_account (
    employee_id,
    username,
    password,
    last_login,
    status,
    created_by,
    updated_by,
    is_deleted
)
VALUES
    (@hr_employee_id, 'testHR@manpower.jp', @default_password_hash, NULL, 1, @operator_id, @operator_id, 0),
    (@exp_manager_id, 'testManagerEXP@manpower.jp', @default_password_hash, NULL, 1, @operator_id, @operator_id, 0),
    (@sd_manager_id, 'testManagerSD@manpower.jp', @default_password_hash, NULL, 1, @operator_id, @operator_id, 0),
    (@west_tech_manager_id, 'testManagerWT@manpower.jp', @default_password_hash, NULL, 1, @operator_id, @operator_id, 0),
    (@solution2_manager_id, 'testManagerSOL2@manpower.jp', @default_password_hash, NULL, 1, @operator_id, @operator_id, 0),
    (@iteng_manager_id, 'testManagerIT@manpower.jp', @default_password_hash, NULL, 1, @operator_id, @operator_id, 0),
    (@hr_staff_employee_id, 'testEmployeeHR@manpower.jp', @default_password_hash, NULL, 1, @operator_id, @operator_id, 0),
    (@sd_staff_employee_id, 'testEmployeeSD@manpower.jp', @default_password_hash, NULL, 1, @operator_id, @operator_id, 0),
    (@west_tech_staff_employee_id, 'testEmployeeWT@manpower.jp', @default_password_hash, NULL, 1, @operator_id, @operator_id, 0),
    (@solution2_staff_employee_id, 'testEmployeeSOL2@manpower.jp', @default_password_hash, NULL, 1, @operator_id, @operator_id, 0),
    (@iteng_staff_employee_id, 'testEmployeeIT1@manpower.jp', @default_password_hash, NULL, 1, @operator_id, @operator_id, 0),
    (@iteng_staff2_employee_id, 'testEmployeeIT2@manpower.jp', @default_password_hash, NULL, 1, @operator_id, @operator_id, 0)
ON DUPLICATE KEY UPDATE
    username = VALUES(username),
    password = VALUES(password),
    status = VALUES(status),
    updated_by = VALUES(updated_by),
    is_deleted = 0;

INSERT INTO emp_employee_position (
    employee_id,
    company_id,
    node_id,
    grade_id,
    is_primary,
    start_date,
    end_date,
    status,
    created_by,
    updated_by,
    is_deleted
)
SELECT employee_id, @jp_company_id, node_id, grade_id, 1, @today, NULL, 1, @operator_id, @operator_id, 0
FROM (
    SELECT @hr_employee_id AS employee_id, @hr_node_id AS node_id, @manager_grade_id AS grade_id
    UNION ALL SELECT @exp_manager_id, @exp_node_id, @manager_grade_id
    UNION ALL SELECT @sd_manager_id, @service_delivery_node_id, @manager_grade_id
    UNION ALL SELECT @west_tech_manager_id, @west_tech_node_id, @leader_grade_id
    UNION ALL SELECT @solution2_manager_id, @solution2_node_id, @leader_grade_id
    UNION ALL SELECT @iteng_manager_id, @it_engineering_node_id, @leader_grade_id
    UNION ALL SELECT @hr_staff_employee_id, @hr_node_id, @staff_grade_id
    UNION ALL SELECT @sd_staff_employee_id, @service_delivery_node_id, @staff_grade_id
    UNION ALL SELECT @west_tech_staff_employee_id, @west_tech_node_id, @staff_grade_id
    UNION ALL SELECT @solution2_staff_employee_id, @solution2_node_id, @staff_grade_id
    UNION ALL SELECT @iteng_staff_employee_id, @it_engineering_node_id, @staff_grade_id
    UNION ALL SELECT @iteng_staff2_employee_id, @it_engineering_node_id, @staff_grade_id
) position_seed
WHERE NOT EXISTS (
    SELECT 1
    FROM emp_employee_position position
    WHERE position.employee_id = position_seed.employee_id
      AND position.company_id = @jp_company_id
      AND position.node_id = position_seed.node_id
      AND position.is_deleted = 0
);

UPDATE org_node
SET manager_id = CASE code
    WHEN 'JP_HR_DEPT' THEN @hr_employee_id
    WHEN 'JP_EXP_DIV' THEN @exp_manager_id
    WHEN 'JP_EXP_SERVICE_DELIVERY' THEN @sd_manager_id
    WHEN 'JP_EXP_WEST_TECH' THEN @west_tech_manager_id
    WHEN 'JP_EXP_SOLUTION_2' THEN @solution2_manager_id
    WHEN 'JP_EXP_IT_ENGINEERING' THEN @iteng_manager_id
    ELSE manager_id
END,
updated_by = @operator_id
WHERE company_id = @jp_company_id
  AND code IN (
      'JP_HR_DEPT',
      'JP_EXP_DIV',
      'JP_EXP_SERVICE_DELIVERY',
      'JP_EXP_WEST_TECH',
      'JP_EXP_SOLUTION_2',
      'JP_EXP_IT_ENGINEERING'
  )
  AND is_deleted = 0;

INSERT IGNORE INTO sys_employee_role (
    employee_id,
    role_id,
    company_id,
    start_date,
    end_date,
    created_by,
    updated_by,
    is_deleted
)
SELECT employee_id, role_id, @jp_company_id, @today, NULL, @operator_id, @operator_id, 0
FROM (
    SELECT @hr_employee_id AS employee_id, @jp_hr_role_id AS role_id
    UNION ALL SELECT @exp_manager_id, @jp_manager_role_id
    UNION ALL SELECT @sd_manager_id, @jp_manager_role_id
    UNION ALL SELECT @west_tech_manager_id, @jp_manager_role_id
    UNION ALL SELECT @solution2_manager_id, @jp_manager_role_id
    UNION ALL SELECT @iteng_manager_id, @jp_manager_role_id
    UNION ALL SELECT @hr_staff_employee_id, @jp_employee_role_id
    UNION ALL SELECT @sd_staff_employee_id, @jp_employee_role_id
    UNION ALL SELECT @west_tech_staff_employee_id, @jp_employee_role_id
    UNION ALL SELECT @solution2_staff_employee_id, @jp_employee_role_id
    UNION ALL SELECT @iteng_staff_employee_id, @jp_employee_role_id
    UNION ALL SELECT @iteng_staff2_employee_id, @jp_employee_role_id
) employee_role_seed;

COMMIT;

/* -------------------------------------------------------------------------- */
/* Smoke check queries                                                        */
/* -------------------------------------------------------------------------- */

SELECT
    company.id,
    company.parent_id,
    company.name,
    company.company_code,
    company.level
FROM org_company company
WHERE company.company_code IN ('MPG', 'MPG_JP')
  AND company.is_deleted = 0
ORDER BY company.level, company.sort;

SELECT
    node.id,
    node.parent_id,
    node.manager_id,
    node.name,
    node.type_code,
    node.dept_function,
    node.code,
    node.level
FROM org_node node
WHERE node.company_id = @jp_company_id
  AND node.is_deleted = 0
ORDER BY node.level, node.sort;

SELECT
    employee.employee_code,
    employee.email,
    account.username,
    role.code AS role_code
FROM emp_employee employee
JOIN emp_account account
  ON account.employee_id = employee.id
 AND account.is_deleted = 0
JOIN sys_employee_role employee_role
  ON employee_role.employee_id = employee.id
 AND employee_role.is_deleted = 0
JOIN sys_role role
  ON role.id = employee_role.role_id
 AND role.is_deleted = 0
WHERE employee.company_id = @jp_company_id
  AND employee.is_deleted = 0
ORDER BY employee.employee_code;
