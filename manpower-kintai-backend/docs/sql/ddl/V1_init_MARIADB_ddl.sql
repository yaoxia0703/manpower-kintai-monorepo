/*
MariaDB 11.4 compatible schema generated from MySQL schema.
Database - manpower_kintai
*/

/*!40101 SET NAMES utf8mb4 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE IF NOT EXISTS `manpower_kintai` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `manpower_kintai`;

DROP TABLE IF EXISTS `wf_approval_step`;
DROP TABLE IF EXISTS `wf_approval_rule`;
DROP TABLE IF EXISTS `wf_approval`;
DROP TABLE IF EXISTS `sys_role_permission`;
DROP TABLE IF EXISTS `sys_role_menu`;
DROP TABLE IF EXISTS `sys_role`;
DROP TABLE IF EXISTS `sys_permission`;
DROP TABLE IF EXISTS `sys_notification`;
DROP TABLE IF EXISTS `sys_menu`;
DROP TABLE IF EXISTS `sys_i18n`;
DROP TABLE IF EXISTS `sys_grade_role`;
DROP TABLE IF EXISTS `sys_enum_value`;
DROP TABLE IF EXISTS `sys_enum_type`;
DROP TABLE IF EXISTS `sys_employee_role`;
DROP TABLE IF EXISTS `org_node_closure`;
DROP TABLE IF EXISTS `org_node`;
DROP TABLE IF EXISTS `org_grade`;
DROP TABLE IF EXISTS `org_company`;
DROP TABLE IF EXISTS `emp_employee_position`;
DROP TABLE IF EXISTS `emp_employee`;
DROP TABLE IF EXISTS `emp_account`;
DROP TABLE IF EXISTS `att_request`;
DROP TABLE IF EXISTS `att_record`;
DROP TABLE IF EXISTS `att_paid_leave_balance`;
DROP TABLE IF EXISTS `att_monthly_summary`;

/*Table structure for table `att_monthly_summary` */

CREATE TABLE `att_monthly_summary` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '月次集計ID',
  `employee_id` bigint NOT NULL COMMENT '社員ID',
  `company_id` bigint NOT NULL COMMENT '会社ID',
  `summary_month` char(7) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '対象年月（YYYY-MM）',
  `work_days` int NOT NULL DEFAULT '0' COMMENT '出勤日数',
  `absent_days` int NOT NULL DEFAULT '0' COMMENT '欠勤日数',
  `paid_leave_days` decimal(4,1) NOT NULL DEFAULT '0.0' COMMENT '有給取得日数',
  `total_work_minutes` int NOT NULL DEFAULT '0' COMMENT '総労働時間（分）',
  `total_overtime_minutes` int NOT NULL DEFAULT '0' COMMENT '総残業時間（分）',
  `late_count` int NOT NULL DEFAULT '0' COMMENT '遅刻回数',
  `early_leave_count` int NOT NULL DEFAULT '0' COMMENT '早退回数',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT 'ステータス（0=未確定 1=確定済）',
  `confirmed_at` datetime DEFAULT NULL COMMENT '確定日時',
  `confirmed_by` bigint DEFAULT NULL COMMENT '確定者ID',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_summary_month` (`employee_id`,`summary_month`,`active_deleted`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_summary_month` (`summary_month`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='月次勤怠集計';

/*Table structure for table `att_paid_leave_balance` */

CREATE TABLE `att_paid_leave_balance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '有給残数ID',
  `employee_id` bigint NOT NULL COMMENT '社員ID',
  `company_id` bigint NOT NULL COMMENT '会社ID',
  `fiscal_year` char(4) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '対象年度（YYYY）',
  `granted_days` decimal(4,1) NOT NULL DEFAULT '0.0' COMMENT '付与日数',
  `used_days` decimal(4,1) NOT NULL DEFAULT '0.0' COMMENT '取得済日数',
  `expired_days` decimal(4,1) NOT NULL DEFAULT '0.0' COMMENT '失効日数',
  `balance_days` decimal(4,1) NOT NULL DEFAULT '0.0' COMMENT '残日数',
  `expire_date` date NOT NULL COMMENT '有効期限',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_fiscal_year` (`employee_id`,`fiscal_year`,`active_deleted`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_expire_date` (`expire_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='有給休暇残数管理';

/*Table structure for table `att_record` */

CREATE TABLE `att_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '打刻記録ID',
  `employee_id` bigint NOT NULL COMMENT '社員ID',
  `company_id` bigint NOT NULL COMMENT '会社ID',
  `work_date` date NOT NULL COMMENT '勤務日',
  `clock_in` time DEFAULT NULL COMMENT '出勤時刻',
  `clock_out` time DEFAULT NULL COMMENT '退勤時刻',
  `attendance_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OFFICE' COMMENT '出勤区分（ATTENDANCE_TYPE参照）',
  `work_minutes` int DEFAULT NULL COMMENT '実労働時間（分）',
  `overtime_minutes` int DEFAULT NULL COMMENT '残業時間（分）',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '備考',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT 'ステータス（0=未承認 1=承認済 2=否認）',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_work_date` (`employee_id`,`work_date`,`active_deleted`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_work_date` (`work_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='打刻記録';

/*Table structure for table `att_request` */

CREATE TABLE `att_request` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '申請ID',
  `employee_id` bigint NOT NULL COMMENT '申請者社員ID',
  `company_id` bigint NOT NULL COMMENT '会社ID',
  `request_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申請タイプ（REQUEST_TYPE参照）',
  `start_date` date NOT NULL COMMENT '開始日',
  `end_date` date NOT NULL COMMENT '終了日',
  `start_time` time DEFAULT NULL COMMENT '開始時刻（残業申請等）',
  `end_time` time DEFAULT NULL COMMENT '終了時刻（残業申請等）',
  `days` decimal(4,1) DEFAULT NULL COMMENT '申請日数',
  `minutes` int DEFAULT NULL COMMENT '申請時間（分）',
  `reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '申請理由',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING' COMMENT '承認ステータス（APPROVAL_STATUS参照）',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  PRIMARY KEY (`id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_request_type` (`request_type`),
  KEY `idx_status` (`status`),
  KEY `idx_start_date` (`start_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='各種申請';

/*Table structure for table `emp_account` */

CREATE TABLE `emp_account` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'アカウントID',
  `employee_id` bigint NOT NULL COMMENT '社員ID',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ユーザー名',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'パスワード（BCrypt）',
  `last_login` datetime DEFAULT NULL COMMENT '最終ログイン日時',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT 'ステータス（1=有効 0=無効）',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`,`active_deleted`),
  UNIQUE KEY `uk_employee_id` (`employee_id`,`active_deleted`),
  KEY `idx_employee_id` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社員アカウント';

/*Table structure for table `emp_employee` */

CREATE TABLE `emp_employee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '社員ID',
  `company_id` bigint NOT NULL COMMENT '所属会社ID',
  `employee_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '社員番号',
  `last_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '姓',
  `first_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名',
  `last_name_kana` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓（カナ）',
  `first_name_kana` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名（カナ）',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'メールアドレス',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '電話番号',
  `gender` tinyint DEFAULT NULL COMMENT '性別（1=男 2=女 0=その他）',
  `birth_date` date DEFAULT NULL COMMENT '生年月日',
  `hire_date` date NOT NULL COMMENT '入社日',
  `leave_date` date DEFAULT NULL COMMENT '退職日',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT 'ステータス（1=在職 0=退職）',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_code` (`company_id`,`employee_code`,`active_deleted`),
  UNIQUE KEY `uk_email` (`email`,`active_deleted`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社員マスタ';

/*Table structure for table `emp_employee_position` */

CREATE TABLE `emp_employee_position` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '社員職位ID',
  `employee_id` bigint NOT NULL COMMENT '社員ID',
  `company_id` bigint NOT NULL COMMENT '所属会社ID',
  `node_id` bigint NOT NULL COMMENT '組織ノードID',
  `grade_id` bigint NOT NULL COMMENT '職級ID',
  `is_primary` tinyint NOT NULL DEFAULT '1' COMMENT '主務フラグ（1=主務 0=兼務）',
  `start_date` date NOT NULL COMMENT '着任日',
  `end_date` date DEFAULT NULL COMMENT '離任日（NULLは現在も在任）',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT 'ステータス（1=有効 0=無効）',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  PRIMARY KEY (`id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_node_id` (`node_id`),
  KEY `idx_grade_id` (`grade_id`),
  KEY `idx_is_primary` (`is_primary`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社員職位関連（兼任対応）';

/*Table structure for table `org_company` */

CREATE TABLE `org_company` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会社ID',
  `parent_id` bigint DEFAULT NULL COMMENT '親会社ID（NULLが最上位）',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会社名',
  `company_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会社コード',
  `level` int NOT NULL DEFAULT '1' COMMENT '階層レベル（1=総本社）',
  `sort` int NOT NULL DEFAULT '0' COMMENT '表示順',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT 'ステータス（1=有効 0=無効）',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_company_code` (`company_code`,`active_deleted`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会社マスタ';

/*Table structure for table `org_grade` */

CREATE TABLE `org_grade` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '職級ID',
  `company_id` bigint NOT NULL COMMENT '所属会社ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '職級名',
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '職級コード',
  `grade_level` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '集団統一職級レベル（GRADE_LEVEL参照：L1〜L5）',
  `sort` int NOT NULL DEFAULT '0' COMMENT '表示順',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT 'ステータス（1=有効 0=無効）',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_grade_code` (`company_id`,`code`,`active_deleted`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_grade_level` (`grade_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='職級マスタ';

/*Table structure for table `org_node` */

CREATE TABLE `org_node` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '組織ノードID',
  `company_id` bigint NOT NULL COMMENT '所属会社ID',
  `parent_id` bigint DEFAULT NULL COMMENT '親ノードID（NULLがルート）',
  `manager_id` bigint DEFAULT NULL COMMENT 'ノード責任者社員ID',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ノード名',
  `type_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ノードタイプコード（ORG_NODE_TYPE参照）',
  `dept_function` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部門機能区分（DEPT_FUNCTION参照）',
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ノードコード',
  `level` int NOT NULL DEFAULT '1' COMMENT '階層レベル',
  `sort` int NOT NULL DEFAULT '0' COMMENT '表示順',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT 'ステータス（1=有効 0=無効）',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_node_code` (`company_id`,`code`,`active_deleted`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_manager_id` (`manager_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='組織ノード（無限階層）';

/*Table structure for table `org_node_closure` */

CREATE TABLE `org_node_closure` (
  `ancestor_id` bigint NOT NULL COMMENT '祖先ノードID',
  `descendant_id` bigint NOT NULL COMMENT '子孫ノードID',
  `depth` int NOT NULL COMMENT '階層距離（0=自分自身）',
  PRIMARY KEY (`ancestor_id`,`descendant_id`),
  KEY `idx_descendant_id` (`descendant_id`),
  KEY `idx_depth` (`depth`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='組織閉包テーブル';

/*Table structure for table `sys_employee_role` */

CREATE TABLE `sys_employee_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '社員ロールID',
  `employee_id` bigint NOT NULL COMMENT '社員ID',
  `role_id` bigint NOT NULL COMMENT 'ロールID',
  `company_id` bigint NOT NULL COMMENT '付与会社ID',
  `start_date` date DEFAULT NULL COMMENT '有効開始日',
  `end_date` date DEFAULT NULL COMMENT '有効終了日（NULLは無期限）',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_role` (`employee_id`,`role_id`,`company_id`,`active_deleted`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社員個人追加ロール関連';

/*Table structure for table `sys_enum_type` */

CREATE TABLE `sys_enum_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '列挙タイプID',
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '列挙タイプコード',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '列挙タイプ名称',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '備考',
  `sort` int NOT NULL DEFAULT '0' COMMENT '表示順',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT 'ステータス（1=有効 0=無効）',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_enum_type_code` (`code`,`active_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='列挙タイプ定義';

/*Table structure for table `sys_enum_value` */

CREATE TABLE `sys_enum_value` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '列挙値ID',
  `enum_type_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '列挙タイプコード',
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '列挙値コード',
  `sort` int NOT NULL DEFAULT '0' COMMENT '表示順',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT 'ステータス（1=有効 0=無効）',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_enum_value_code` (`enum_type_code`,`code`,`active_deleted`),
  KEY `idx_enum_type_code` (`enum_type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='列挙値定義';

/*Table structure for table `sys_grade_role` */

CREATE TABLE `sys_grade_role` (
  `grade_id` bigint NOT NULL COMMENT '職級ID',
  `role_id` bigint NOT NULL COMMENT 'ロールID',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  PRIMARY KEY (`grade_id`,`role_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='職級ロール関連';

/*Table structure for table `sys_i18n` */

CREATE TABLE `sys_i18n` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '国際化ID',
  `ref_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参照タイプ（ENUM/GRADE/NODE等）',
  `ref_id` bigint NOT NULL COMMENT '参照レコードID',
  `language` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '言語コード（ja/zh/en）',
  `content` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '翻訳テキスト',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_i18n` (`ref_type`,`ref_id`,`language`,`active_deleted`),
  KEY `idx_ref` (`ref_type`,`ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='国際化翻訳テーブル';

/*Table structure for table `sys_menu` */

CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'メニューID',
  `parent_id` bigint DEFAULT NULL COMMENT '親メニューID（NULLがルート）',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'メニュー名',
  `code` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'メニューコード',
  `path` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'フロントエンドルートパス',
  `component` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'フロントエンドコンポーネントパス',
  `icon` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'アイコン',
  `type` tinyint NOT NULL DEFAULT '1' COMMENT 'タイプ（1=ディレクトリ 2=メニュー 3=ボタン）',
  `sort` int NOT NULL DEFAULT '0' COMMENT '表示順',
  `visible` tinyint NOT NULL DEFAULT '1' COMMENT '表示フラグ（1=表示 0=非表示）',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT 'ステータス（1=有効 0=無効）',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_menu_code` (`code`,`active_deleted`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='メニューマスタ';

/*Table structure for table `sys_notification` */

CREATE TABLE `sys_notification` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `company_id` bigint NOT NULL COMMENT '会社ID',
  `recipient_id` bigint NOT NULL COMMENT '受信者社員ID',
  `type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知タイプ',
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知タイトル',
  `content` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通知内容',
  `ref_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '関連業務タイプ',
  `ref_id` bigint DEFAULT NULL COMMENT '関連業務ID',
  `is_read` tinyint NOT NULL DEFAULT '0' COMMENT '既読フラグ（0=未読 1=既読）',
  `read_at` datetime DEFAULT NULL COMMENT '既読日時',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  PRIMARY KEY (`id`),
  KEY `idx_notification_recipient_unread` (`recipient_id`,`is_read`,`created_at`),
  KEY `idx_notification_reference` (`ref_type`,`ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='システム通知';

/*Table structure for table `sys_permission` */

CREATE TABLE `sys_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '権限ID',
  `menu_id` bigint NOT NULL COMMENT 'メニューID',
  `code` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '権限コード',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '権限名称',
  `method` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'HTTPメソッド（GET/POST/PUT/DELETE）',
  `path` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'APIパス',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '備考',
  `sort` int NOT NULL DEFAULT '0' COMMENT '表示順',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT 'ステータス（1=有効 0=無効）',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`code`,`active_deleted`),
  UNIQUE KEY `uk_method_path` (`method`,`path`,`active_deleted`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='権限マスタ';

/*Table structure for table `sys_role` */

CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ロールID',
  `company_id` bigint DEFAULT NULL COMMENT '所属会社ID（NULLは全社共通）',
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ロールコード',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ロール名',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '備考',
  `sort` int NOT NULL DEFAULT '0' COMMENT '表示順',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT 'ステータス（1=有効 0=無効）',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`company_id`,`code`,`active_deleted`),
  KEY `idx_company_id` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ロールマスタ';

/*Table structure for table `sys_role_menu` */

CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT 'ロールID',
  `menu_id` bigint NOT NULL COMMENT 'メニューID',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  PRIMARY KEY (`role_id`,`menu_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ロールメニュー関連';

/*Table structure for table `sys_role_permission` */

CREATE TABLE `sys_role_permission` (
  `role_id` bigint NOT NULL COMMENT 'ロールID',
  `permission_id` bigint NOT NULL COMMENT '権限ID',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ロール権限関連';

/*Table structure for table `wf_approval` */

CREATE TABLE `wf_approval` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '承認フローID',
  `request_id` bigint NOT NULL COMMENT '申請ID',
  `request_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申請タイプ',
  `applicant_id` bigint NOT NULL COMMENT '申請者社員ID',
  `company_id` bigint NOT NULL COMMENT '会社ID',
  `current_step` int NOT NULL DEFAULT '1' COMMENT '現在の承認ステップ',
  `total_steps` int NOT NULL DEFAULT '1' COMMENT '総承認ステップ数',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING' COMMENT '承認ステータス',
  `escalated` tinyint NOT NULL DEFAULT '0' COMMENT 'エスカレーションフラグ（0=通常 1=本社）',
  `escalated_at` datetime DEFAULT NULL COMMENT 'エスカレーション日時',
  `escalated_to` bigint DEFAULT NULL COMMENT 'エスカレーション先社員ID',
  `completed_at` datetime DEFAULT NULL COMMENT '承認完了日時',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_request_id` (`request_id`,`active_deleted`),
  KEY `idx_applicant_id` (`applicant_id`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_status` (`status`),
  KEY `idx_escalated` (`escalated`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='承認フロー';

/*Table structure for table `wf_approval_rule` */

CREATE TABLE `wf_approval_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '承認ルールID',
  `company_id` bigint NOT NULL COMMENT '会社ID',
  `request_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申請タイプ（REQUEST_TYPE参照）',
  `stop_condition` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '終止条件（DIRECT_ONLY/REACH_GRADE/REACH_DEPARTMENT）',
  `stop_grade_level` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '終止職級（REACH_GRADE時使用）',
  `stop_dept_func` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '終止部門機能（REACH_DEPARTMENT時使用）',
  `amount_threshold` decimal(10,2) DEFAULT NULL COMMENT '金額閾値（NULLは常に発動）',
  `sort` int NOT NULL DEFAULT '0' COMMENT '表示順',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT 'ステータス（1=有効 0=無効）',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rule` (`company_id`,`request_type`,`amount_threshold`,`active_deleted`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_request_type` (`request_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='承認ルール定義';

/*Table structure for table `wf_approval_step` */

CREATE TABLE `wf_approval_step` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '承認ステップID',
  `approval_id` bigint NOT NULL COMMENT '承認フローID',
  `step` int NOT NULL COMMENT 'ステップ番号',
  `approver_id` bigint NOT NULL COMMENT '承認者社員ID',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING' COMMENT 'ステータス',
  `comment` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '承認コメント',
  `approved_at` datetime DEFAULT NULL COMMENT '承認日時',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '論理削除（0=有効 1=削除）',
  `active_deleted` tinyint GENERATED ALWAYS AS (case when `is_deleted` = 0 then `is_deleted` else NULL end) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_approval_step` (`approval_id`,`step`,`active_deleted`),
  KEY `idx_approval_id` (`approval_id`),
  KEY `idx_approver_id` (`approver_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='承認ステップ明細';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
