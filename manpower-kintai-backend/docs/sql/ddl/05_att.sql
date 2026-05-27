/*
SQLyog Community v13.3.1 (64 bit)
MySQL - 8.0.44 : Database - manpower_kintai
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`manpower_kintai` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `manpower_kintai`;

/*Table structure for table `att_monthly_summary` */

DROP TABLE IF EXISTS `att_monthly_summary`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_summary_month` (`employee_id`,`summary_month`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_summary_month` (`summary_month`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='月次勤怠集計';

/*Table structure for table `att_paid_leave_balance` */

DROP TABLE IF EXISTS `att_paid_leave_balance`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_fiscal_year` (`employee_id`,`fiscal_year`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_expire_date` (`expire_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='有給休暇残数管理';

/*Table structure for table `att_record` */

DROP TABLE IF EXISTS `att_record`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_work_date` (`employee_id`,`work_date`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_work_date` (`work_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='打刻記録';

/*Table structure for table `att_request` */

DROP TABLE IF EXISTS `att_request`;

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

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
