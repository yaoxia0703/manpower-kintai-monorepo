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

/*Table structure for table `wf_approval` */

DROP TABLE IF EXISTS `wf_approval`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_request_id` (`request_id`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_applicant_id` (`applicant_id`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_status` (`status`),
  KEY `idx_escalated` (`escalated`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='承認フロー';

/*Table structure for table `wf_approval_rule` */

DROP TABLE IF EXISTS `wf_approval_rule`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rule` (`company_id`,`request_type`,`amount_threshold`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_request_type` (`request_type`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='承認ルール定義';

/*Table structure for table `wf_approval_step` */

DROP TABLE IF EXISTS `wf_approval_step`;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_approval_step` (`approval_id`,`step`,((case when (`is_deleted` = 0) then `is_deleted` else NULL end))),
  KEY `idx_approval_id` (`approval_id`),
  KEY `idx_approver_id` (`approver_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='承認ステップ明細';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
