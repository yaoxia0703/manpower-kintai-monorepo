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

/*Table structure for table `emp_account` */

DROP TABLE IF EXISTS `emp_account`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  UNIQUE KEY `uk_employee_id` (`employee_id`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_employee_id` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社員アカウント';

/*Table structure for table `emp_employee` */

DROP TABLE IF EXISTS `emp_employee`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_code` (`company_id`,`employee_code`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  UNIQUE KEY `uk_email` (`email`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社員マスタ';

/*Table structure for table `emp_employee_position` */

DROP TABLE IF EXISTS `emp_employee_position`;

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

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
