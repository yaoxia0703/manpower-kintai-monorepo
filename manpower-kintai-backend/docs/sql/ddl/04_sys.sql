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

/*Table structure for table `sys_employee_role` */

DROP TABLE IF EXISTS `sys_employee_role`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_role` (`employee_id`,`role_id`,`company_id`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社員個人追加ロール関連';

/*Table structure for table `sys_grade_role` */

DROP TABLE IF EXISTS `sys_grade_role`;

CREATE TABLE `sys_grade_role` (
  `grade_id` bigint NOT NULL COMMENT '職級ID',
  `role_id` bigint NOT NULL COMMENT 'ロールID',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  PRIMARY KEY (`grade_id`,`role_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='職級ロール関連';

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_menu_code` (`code`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='メニューマスタ';

/*Table structure for table `sys_permission` */

DROP TABLE IF EXISTS `sys_permission`;

CREATE TABLE `sys_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '権限ID',
  `menu_id` bigint DEFAULT NULL COMMENT 'メニューID',
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`code`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  UNIQUE KEY `uk_method_path` (`method`,`path`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='権限マスタ';

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`company_id`,`code`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_company_id` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ロールマスタ';

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT 'ロールID',
  `menu_id` bigint NOT NULL COMMENT 'メニューID',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  PRIMARY KEY (`role_id`,`menu_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ロールメニュー関連';

/*Table structure for table `sys_role_permission` */

DROP TABLE IF EXISTS `sys_role_permission`;

CREATE TABLE `sys_role_permission` (
  `role_id` bigint NOT NULL COMMENT 'ロールID',
  `permission_id` bigint NOT NULL COMMENT '権限ID',
  `created_by` bigint DEFAULT NULL COMMENT '作成者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ロール権限関連';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
