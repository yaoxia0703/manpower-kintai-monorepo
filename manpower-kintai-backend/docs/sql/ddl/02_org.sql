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

/*Table structure for table `org_company` */

DROP TABLE IF EXISTS `org_company`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_company_code` (`company_code`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会社マスタ';

/*Table structure for table `org_grade` */

DROP TABLE IF EXISTS `org_grade`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_grade_code` (`company_id`,`code`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_grade_level` (`grade_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='職級マスタ';

/*Table structure for table `org_node` */

DROP TABLE IF EXISTS `org_node`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_node_code` (`company_id`,`code`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_manager_id` (`manager_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='組織ノード（無限階層）';

/*Table structure for table `org_node_closure` */

DROP TABLE IF EXISTS `org_node_closure`;

CREATE TABLE `org_node_closure` (
  `ancestor_id` bigint NOT NULL COMMENT '祖先ノードID',
  `descendant_id` bigint NOT NULL COMMENT '子孫ノードID',
  `depth` int NOT NULL COMMENT '階層距離（0=自分自身）',
  PRIMARY KEY (`ancestor_id`,`descendant_id`),
  KEY `idx_descendant_id` (`descendant_id`),
  KEY `idx_depth` (`depth`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='組織閉包テーブル';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
