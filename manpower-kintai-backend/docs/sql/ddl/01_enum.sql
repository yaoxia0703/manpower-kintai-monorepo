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

/*Table structure for table `sys_enum_type` */

DROP TABLE IF EXISTS `sys_enum_type`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_enum_type_code` (`code`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end)))
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='列挙タイプ定義';

/*Table structure for table `sys_enum_value` */

DROP TABLE IF EXISTS `sys_enum_value`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_enum_value_code` (`enum_type_code`,`code`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_enum_type_code` (`enum_type_code`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='列挙値定義';

/*Table structure for table `sys_i18n` */

DROP TABLE IF EXISTS `sys_i18n`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_i18n` (`ref_type`,`ref_id`,`language`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_ref` (`ref_type`,`ref_id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='国際化翻訳テーブル';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
