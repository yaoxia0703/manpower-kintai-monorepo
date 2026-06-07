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

/*Data for the table `att_monthly_summary` */

/*Data for the table `att_paid_leave_balance` */

/*Data for the table `att_record` */

insert  into `att_record`(`id`,`employee_id`,`company_id`,`work_date`,`clock_in`,`clock_out`,`attendance_type`,`work_minutes`,`overtime_minutes`,`remark`,`status`,`created_by`,`created_at`,`updated_by`,`updated_at`,`is_deleted`) values 
(1,1,1,'2026-05-01','09:00:00','23:40:00','OFFICE',850,370,NULL,0,1,'2026-05-30 00:32:39',1,'2026-05-30 00:32:39',0),
(2,1,1,'2026-05-02','19:20:00','21:30:00','HOLIDAY_WORK',120,0,NULL,0,1,'2026-05-30 00:32:39',1,'2026-05-30 00:32:39',0),
(3,1,1,'2026-05-03','00:53:00','23:59:00','BUSINESS_TRIP',1298,818,NULL,0,1,'2026-05-30 00:53:09',1,'2026-05-30 00:53:09',0),
(4,5,3,'2026-06-01','09:10:00','18:10:00','OFFICE',470,0,'test',0,5,'2026-06-07 00:26:14',5,'2026-06-07 00:26:14',0);

/*Data for the table `att_request` */

/*Data for the table `emp_account` */

insert  into `emp_account`(`id`,`employee_id`,`username`,`password`,`last_login`,`status`,`created_by`,`created_at`,`updated_by`,`updated_at`,`is_deleted`) values 
(1,1,'admin','$2a$10$Zg3pJHTR0S/ysCoHid4JcOVeeLidl6QaXkBLWd6o66zSRsahIncAe','2026-06-04 13:19:31',1,NULL,'2026-05-27 23:19:16',1,'2026-06-02 13:47:07',0),
(2,2,'testHR@manpower.jp','$2a$10$JU8LQtgNkeiljdkCVTLO5.dYc5ivOYhz5BumavSA58RcbSyB0..my',NULL,1,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(3,3,'testManagerEXP@manpower.jp','$2a$10$JU8LQtgNkeiljdkCVTLO5.dYc5ivOYhz5BumavSA58RcbSyB0..my','2026-06-07 00:00:04',1,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(4,4,'testManagerSD@manpower.jp','$2a$10$JU8LQtgNkeiljdkCVTLO5.dYc5ivOYhz5BumavSA58RcbSyB0..my','2026-06-07 00:00:29',1,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(5,5,'testManagerWT@manpower.jp','$2a$10$JU8LQtgNkeiljdkCVTLO5.dYc5ivOYhz5BumavSA58RcbSyB0..my','2026-06-07 11:48:18',1,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(6,6,'testManagerSOL2@manpower.jp','$2a$10$JU8LQtgNkeiljdkCVTLO5.dYc5ivOYhz5BumavSA58RcbSyB0..my','2026-06-07 11:42:28',1,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(7,7,'testManagerIT@manpower.jp','$2a$10$JU8LQtgNkeiljdkCVTLO5.dYc5ivOYhz5BumavSA58RcbSyB0..my',NULL,1,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(8,8,'testEmployeeHR@manpower.jp','$2a$10$JU8LQtgNkeiljdkCVTLO5.dYc5ivOYhz5BumavSA58RcbSyB0..my',NULL,1,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(9,9,'testEmployeeSD@manpower.jp','$2a$10$JU8LQtgNkeiljdkCVTLO5.dYc5ivOYhz5BumavSA58RcbSyB0..my',NULL,1,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(10,10,'testEmployeeWT@manpower.jp','$2a$10$JU8LQtgNkeiljdkCVTLO5.dYc5ivOYhz5BumavSA58RcbSyB0..my',NULL,1,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(11,11,'testEmployeeSOL2@manpower.jp','$2a$10$JU8LQtgNkeiljdkCVTLO5.dYc5ivOYhz5BumavSA58RcbSyB0..my',NULL,1,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(12,12,'testEmployeeIT1@manpower.jp','$2a$10$JU8LQtgNkeiljdkCVTLO5.dYc5ivOYhz5BumavSA58RcbSyB0..my',NULL,1,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(13,13,'testEmployeeIT2@manpower.jp','$2a$10$JU8LQtgNkeiljdkCVTLO5.dYc5ivOYhz5BumavSA58RcbSyB0..my',NULL,1,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0);

/*Data for the table `emp_employee` */

insert  into `emp_employee`(`id`,`company_id`,`employee_code`,`last_name`,`first_name`,`last_name_kana`,`first_name_kana`,`email`,`phone`,`gender`,`birth_date`,`hire_date`,`leave_date`,`status`,`created_by`,`created_at`,`updated_by`,`updated_at`,`is_deleted`) values 
(1,1,'SUPER_ADMIN','Super','Admin',NULL,NULL,'admin@manpower.local',NULL,0,NULL,'2026-05-27',NULL,1,NULL,'2026-05-27 23:19:16',NULL,'2026-05-27 23:19:16',0),
(2,3,'JP-HR-001','中村','さん','ナカムラ','さん','testHR@manpower.jp',NULL,2,NULL,'2026-05-30',NULL,1,1,'2026-05-30 23:39:36',1,'2026-06-07 11:45:37',0),
(3,3,'JP-MGR-EXP','田中','太郎','タナカ','タロウ','testManagerEXP@manpower.jp',NULL,1,NULL,'2026-05-30',NULL,1,1,'2026-05-30 23:39:36',1,'2026-05-30 23:39:36',0),
(4,3,'JP-MGR-SD','鈴木','一郎','スズキ','イチロウ','testManagerSD@manpower.jp',NULL,1,NULL,'2026-05-30',NULL,1,1,'2026-05-30 23:39:36',1,'2026-05-30 23:39:36',0),
(5,3,'JP-MGR-WT','北田','さん','キタダ','サン','testManagerWT@manpower.jp',NULL,1,NULL,'2026-05-30',NULL,1,1,'2026-05-30 23:39:36',1,'2026-06-07 11:46:01',0),
(6,3,'JP-MGR-SOL2','松山','さん','マツヤマ','サン','testManagerSOL2@manpower.jp',NULL,1,NULL,'2026-05-30',NULL,1,1,'2026-05-30 23:39:36',1,'2026-06-07 11:46:04',0),
(7,3,'JP-MGR-ITENG','宋','さん','ソウ','サン','testManagerIT@manpower.jp',NULL,1,NULL,'2026-05-30',NULL,1,1,'2026-05-30 23:39:36',1,'2026-06-07 11:46:12',0),
(8,3,'JP-EMP-HR-001','天野','さん','アマノ','サン','testEmployeeHR@manpower.jp',NULL,2,NULL,'2026-05-30',NULL,1,1,'2026-05-30 23:39:36',1,'2026-06-07 11:47:20',0),
(9,3,'JP-EMP-SD-001','李','さん','イ','サン','testEmployeeSD@manpower.jp',NULL,1,NULL,'2026-05-30',NULL,1,1,'2026-05-30 23:39:36',1,'2026-06-07 11:47:22',0),
(10,3,'JP-EMP-WT-001','古田','さん','フルダ','サン','testEmployeeWT@manpower.jp',NULL,1,NULL,'2026-05-30',NULL,1,1,'2026-05-30 23:39:36',1,'2026-06-07 11:47:26',0),
(11,3,'JP-EMP-SOL2-001','北山','さん','キタヤマ','サン','testEmployeeSOL2@manpower.jp',NULL,2,NULL,'2026-05-30',NULL,1,1,'2026-05-30 23:39:36',1,'2026-06-07 11:47:34',0),
(12,3,'JP-EMP-ITENG-001','リョン','さん','リョン','サン','testEmployeeIT1@manpower.jp',NULL,2,NULL,'2026-05-30',NULL,1,1,'2026-05-30 23:39:36',1,'2026-06-07 11:47:54',0),
(13,3,'JP-EMP-ITENG-002','八木','さん','ヤギ','サン','testEmployeeIT2@manpower.jp',NULL,1,NULL,'2026-05-30',NULL,1,1,'2026-05-30 23:39:36',1,'2026-06-07 11:48:01',0);

/*Data for the table `emp_employee_position` */

insert  into `emp_employee_position`(`id`,`employee_id`,`company_id`,`node_id`,`grade_id`,`is_primary`,`start_date`,`end_date`,`status`,`created_by`,`created_at`,`updated_by`,`updated_at`,`is_deleted`) values 
(1,2,3,2,1,1,'2026-05-30',NULL,1,1,'2026-05-30 23:39:37',1,'2026-06-07 00:23:14',0),
(2,3,3,1,4,1,'2026-05-30',NULL,1,1,'2026-05-30 23:39:37',1,'2026-06-07 00:23:14',0),
(3,4,3,3,1,1,'2026-05-30',NULL,1,1,'2026-05-30 23:39:37',1,'2026-06-07 00:23:14',0),
(4,5,3,4,1,1,'2026-05-30',NULL,1,1,'2026-05-30 23:39:37',1,'2026-06-07 00:23:14',0),
(5,6,3,5,5,1,'2026-05-30',NULL,1,1,'2026-05-30 23:39:37',1,'2026-06-07 00:23:14',0),
(6,7,3,6,2,1,'2026-05-30',NULL,1,1,'2026-05-30 23:39:37',1,'2026-06-07 00:23:14',0),
(7,8,3,6,3,1,'2026-05-30',NULL,1,1,'2026-05-30 23:39:37',1,'2026-06-07 00:23:14',0),
(8,9,3,6,3,1,'2026-05-30',NULL,1,1,'2026-05-30 23:39:37',1,'2026-06-07 00:23:14',0),
(9,10,3,6,3,1,'2026-05-30',NULL,1,1,'2026-05-30 23:39:37',1,'2026-06-07 00:23:14',0),
(10,11,3,6,3,1,'2026-05-30',NULL,1,1,'2026-05-30 23:39:37',1,'2026-06-07 00:23:14',0),
(11,12,3,6,3,1,'2026-05-30',NULL,1,1,'2026-05-30 23:39:37',1,'2026-06-07 00:23:14',0),
(12,13,3,6,3,1,'2026-05-30',NULL,1,1,'2026-05-30 23:39:37',1,'2026-06-07 00:23:14',0);

/*Data for the table `org_company` */

insert  into `org_company`(`id`,`parent_id`,`name`,`company_code`,`level`,`sort`,`status`,`created_by`,`created_at`,`updated_by`,`updated_at`,`is_deleted`) values 
(1,NULL,'ManpowerGroup','MPG',1,0,1,NULL,'2026-05-27 23:19:16',1,'2026-05-30 23:24:07',0),
(3,1,'マンパワーグループ株式会社','MPG_JP',2,10,1,1,'2026-05-30 23:24:07',1,'2026-05-30 23:24:07',0);

/*Data for the table `org_grade` */

insert  into `org_grade`(`id`,`company_id`,`name`,`code`,`grade_level`,`sort`,`status`,`created_by`,`created_at`,`updated_by`,`updated_at`,`is_deleted`) values 
(1,3,'部長','JP_MANAGER','L2',20,1,1,'2026-05-30 23:39:36',1,'2026-06-07 00:23:14',0),
(2,3,'リーダー','JP_LEADER','L4',40,1,1,'2026-05-30 23:39:36',1,'2026-06-07 00:23:14',0),
(3,3,'一般社員','JP_STAFF','L5',50,1,1,'2026-05-30 23:39:36',1,'2026-06-07 00:23:14',0),
(4,3,'本部長','JP_DIVISION_MANAGER','L1',10,1,1,'2026-06-07 00:20:15',1,'2026-06-07 00:23:14',0),
(5,3,'課長','JP_SECTION_MANAGER','L3',30,1,1,'2026-06-07 00:20:15',1,'2026-06-07 00:23:14',0);

/*Data for the table `org_node` */

insert  into `org_node`(`id`,`company_id`,`parent_id`,`manager_id`,`name`,`type_code`,`dept_function`,`code`,`level`,`sort`,`status`,`created_by`,`created_at`,`updated_by`,`updated_at`,`is_deleted`) values 
(1,3,NULL,3,'エクスペリス事業本部','DIVISION','GENERAL','JP_EXP_DIV',1,10,1,1,'2026-05-30 23:39:36',1,'2026-05-30 23:39:37',0),
(2,3,NULL,2,'人事部','DEPT','HR','JP_HR_DEPT',1,20,1,1,'2026-05-30 23:39:36',1,'2026-05-30 23:39:37',0),
(3,3,1,4,'サービスデリバリー','DEPT','GENERAL','JP_EXP_SERVICE_DELIVERY',2,10,1,1,'2026-05-30 23:39:36',1,'2026-05-30 23:39:37',0),
(4,3,3,5,'西日本テクニカル','SECTION','GENERAL','JP_EXP_WEST_TECH',3,10,1,1,'2026-05-30 23:39:36',1,'2026-05-30 23:39:37',0),
(5,3,4,6,'第2ソリューション','SECTION','GENERAL','JP_EXP_SOLUTION_2',4,10,1,1,'2026-05-30 23:39:36',1,'2026-05-30 23:39:37',0),
(6,3,5,7,'ITエンジニアリング','TEAM','GENERAL','JP_EXP_IT_ENGINEERING',5,10,1,1,'2026-05-30 23:39:36',1,'2026-05-30 23:39:37',0);

/*Data for the table `org_node_closure` */

insert  into `org_node_closure`(`ancestor_id`,`descendant_id`,`depth`) values 
(1,1,0),
(2,2,0),
(3,3,0),
(4,4,0),
(5,5,0),
(6,6,0),
(1,3,1),
(3,4,1),
(4,5,1),
(5,6,1),
(1,4,2),
(3,5,2),
(4,6,2),
(1,5,3),
(3,6,3),
(1,6,4);

/*Data for the table `sys_employee_role` */

insert  into `sys_employee_role`(`id`,`employee_id`,`role_id`,`company_id`,`start_date`,`end_date`,`created_by`,`created_at`,`updated_by`,`updated_at`,`is_deleted`) values 
(2,2,7,3,'2026-05-30',NULL,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(3,3,6,3,'2026-05-30',NULL,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(4,4,6,3,'2026-05-30',NULL,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(5,5,6,3,'2026-05-30',NULL,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(6,6,6,3,'2026-05-30',NULL,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(7,7,6,3,'2026-05-30',NULL,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(8,8,5,3,'2026-05-30',NULL,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(9,9,5,3,'2026-05-30',NULL,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(10,10,5,3,'2026-05-30',NULL,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(11,11,5,3,'2026-05-30',NULL,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(12,12,5,3,'2026-05-30',NULL,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(13,13,5,3,'2026-05-30',NULL,1,'2026-05-30 23:39:37',1,'2026-05-30 23:39:37',0),
(14,1,1,1,'2026-06-02',NULL,1,'2026-06-02 13:47:07',1,'2026-06-02 13:47:07',0);

/*Data for the table `sys_enum_type` */

insert  into `sys_enum_type`(`id`,`code`,`name`,`remark`,`sort`,`status`,`created_by`,`created_at`,`updated_by`,`updated_at`,`is_deleted`) values 
(1,'ORG_NODE_TYPE','組織ノードタイプ','本部/部/課/係/チーム等',0,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(2,'GRADE_LEVEL','職級レベル','集団統一職級等級',0,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(3,'ATTENDANCE_TYPE','出勤区分','出社/在宅/出張/休日出勤',0,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(4,'REQUEST_TYPE','申請タイプ','有給/残業/出張等',0,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(5,'APPROVAL_STATUS','承認ステータス','申請中/承認/否認/取消',0,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(6,'STOP_CONDITION','承認終止条件','DIRECT_ONLY/REACH_GRADE/REACH_DEPARTMENT',0,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(7,'DEPT_FUNCTION','部門機能区分','FINANCE/HR/GENERAL等',0,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(8,'LANGUAGE','対応言語','ja/zh/en',0,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0);

/*Data for the table `sys_enum_value` */

insert  into `sys_enum_value`(`id`,`enum_type_code`,`code`,`sort`,`status`,`created_by`,`created_at`,`updated_by`,`updated_at`,`is_deleted`) values 
(1,'ORG_NODE_TYPE','DIVISION',1,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(2,'ORG_NODE_TYPE','DEPT',2,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(3,'ORG_NODE_TYPE','SECTION',3,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(4,'ORG_NODE_TYPE','TEAM',4,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(5,'GRADE_LEVEL','L1',1,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(6,'GRADE_LEVEL','L2',2,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(7,'GRADE_LEVEL','L3',3,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(8,'GRADE_LEVEL','L4',4,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(9,'GRADE_LEVEL','L5',5,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(10,'ATTENDANCE_TYPE','OFFICE',1,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(11,'ATTENDANCE_TYPE','REMOTE',2,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(12,'ATTENDANCE_TYPE','BUSINESS_TRIP',3,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(13,'ATTENDANCE_TYPE','HOLIDAY_WORK',4,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(14,'REQUEST_TYPE','PAID_LEAVE',1,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(15,'REQUEST_TYPE','OVERTIME',2,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(16,'REQUEST_TYPE','BUSINESS_TRIP',3,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(17,'REQUEST_TYPE','SUBSTITUTE',4,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(18,'REQUEST_TYPE','LEAVE_OF_ABSENCE',5,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(19,'APPROVAL_STATUS','PENDING',1,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(20,'APPROVAL_STATUS','APPROVED',2,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(21,'APPROVAL_STATUS','REJECTED',3,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(22,'APPROVAL_STATUS','CANCELLED',4,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(23,'STOP_CONDITION','DIRECT_ONLY',1,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(24,'STOP_CONDITION','REACH_GRADE',2,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(25,'STOP_CONDITION','REACH_DEPARTMENT',3,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(26,'DEPT_FUNCTION','GENERAL',1,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(27,'DEPT_FUNCTION','FINANCE',2,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(28,'DEPT_FUNCTION','HR',3,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(29,'DEPT_FUNCTION','SALES',4,1,NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0);

/*Data for the table `sys_grade_role` */

/*Data for the table `sys_i18n` */

insert  into `sys_i18n`(`id`,`ref_type`,`ref_id`,`language`,`content`,`created_by`,`created_at`,`updated_by`,`updated_at`,`is_deleted`) values 
(1,'ENUM',1,'ja','本部',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(2,'ENUM',1,'zh','总部',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(3,'ENUM',1,'en','Division',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(4,'ENUM',2,'ja','部',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(5,'ENUM',2,'zh','部门',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(6,'ENUM',2,'en','Department',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(7,'ENUM',3,'ja','課',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(8,'ENUM',3,'zh','科',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(9,'ENUM',3,'en','Section',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(10,'ENUM',4,'ja','チーム',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(11,'ENUM',4,'zh','团队',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(12,'ENUM',4,'en','Team',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(13,'ENUM',5,'ja','役員',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(14,'ENUM',5,'zh','总监',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(15,'ENUM',5,'en','Executive',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(16,'ENUM',6,'ja','部長',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(17,'ENUM',6,'zh','经理',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(18,'ENUM',6,'en','Manager',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(19,'ENUM',7,'ja','課長',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(20,'ENUM',7,'zh','主管',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(21,'ENUM',7,'en','Supervisor',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(22,'ENUM',8,'ja','主任',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(23,'ENUM',8,'zh','专员',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(24,'ENUM',8,'en','Specialist',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(25,'ENUM',9,'ja','一般社員',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(26,'ENUM',9,'zh','员工',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(27,'ENUM',9,'en','Staff',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(28,'ENUM',23,'ja','直属上司のみ',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(29,'ENUM',23,'zh','直属上级',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(30,'ENUM',23,'en','Direct Only',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(31,'ENUM',24,'ja','指定職級まで',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(32,'ENUM',24,'zh','到达指定职级',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(33,'ENUM',24,'en','Reach Grade',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(34,'ENUM',25,'ja','指定部門長まで',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(35,'ENUM',25,'zh','到达指定部门',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(36,'ENUM',25,'en','Reach Dept',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(37,'ENUM',26,'ja','一般部門',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(38,'ENUM',26,'zh','普通部门',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(39,'ENUM',26,'en','General',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(40,'ENUM',27,'ja','財務部門',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(41,'ENUM',27,'zh','财务部门',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(42,'ENUM',27,'en','Finance',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(43,'ENUM',28,'ja','人事部門',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(44,'ENUM',28,'zh','人事部门',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(45,'ENUM',28,'en','HR',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(46,'ENUM',18,'ja','停職留職',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(47,'ENUM',18,'zh','停薪留职',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0),
(48,'ENUM',18,'en','Leave of Absence',NULL,'2026-05-26 17:20:13',NULL,'2026-05-26 17:20:13',0);

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`id`,`parent_id`,`name`,`code`,`path`,`component`,`icon`,`type`,`sort`,`visible`,`status`,`created_by`,`created_at`,`updated_by`,`updated_at`,`is_deleted`) values 
(1,NULL,'ホーム','home','/admin','DashboardView','Home',2,10,1,1,NULL,'2026-05-30 10:43:34',NULL,'2026-05-30 10:43:37',0),
(2,NULL,'勤務表','timesheet','/admin/timesheet','timesheet/TimesheetView','Calendar',2,20,1,1,NULL,'2026-05-30 10:43:34',NULL,'2026-05-30 10:43:37',0),
(3,NULL,'部下管理','manager-subordinates','/admin/subordinates','manager/SubordinatesView','Users',2,30,1,1,NULL,'2026-05-30 10:43:34',NULL,'2026-05-30 10:43:37',0),
(4,NULL,'入社登録','hr-onboarding','/admin/hr/onboarding','hr/OnboardingView','UserPlus',2,40,1,1,NULL,'2026-05-30 10:43:34',NULL,'2026-05-30 10:43:37',0),
(6,NULL,'システム管理','system-management',NULL,NULL,'Setting',1,900,1,1,1,'2026-06-02 13:47:06',1,'2026-06-02 13:47:06',0),
(7,6,'メニュー管理','system-menus','/admin/system/menus','system/MenuManagementView','Menu',2,910,1,1,1,'2026-06-02 13:47:07',1,'2026-06-02 13:47:07',0),
(8,6,'権限管理','system-permissions','/admin/system/permissions','system/PermissionManagementView','Key',2,920,1,1,1,'2026-06-02 13:47:07',1,'2026-06-02 13:47:07',0),
(9,6,'ロール管理','system-roles','/admin/system/roles','system/RoleManagementView','UserCog',2,930,1,1,1,'2026-06-02 13:47:07',1,'2026-06-02 13:47:07',0);

/*Data for the table `sys_permission` */

insert  into `sys_permission`(`id`,`menu_id`,`code`,`name`,`method`,`path`,`remark`,`sort`,`status`,`created_by`,`created_at`,`updated_by`,`updated_at`,`is_deleted`) values 
(1,NULL,'admin:employee:read','社員参照','GET','/admin/emp/employees/**','管理者向け社員参照権限',100,1,NULL,'2026-05-30 10:22:51',NULL,'2026-05-30 10:43:37',0),
(2,NULL,'admin:employee:write','社員更新','POST','/admin/emp/employees/**','管理者向け社員更新権限',110,1,NULL,'2026-05-30 10:22:51',NULL,'2026-05-30 10:43:37',0),
(3,7,'admin:menu:read','メニュー参照','GET','/admin/sys/menus/**','メニュー管理参照',200,1,NULL,'2026-05-30 10:22:51',1,'2026-06-02 13:47:07',0),
(4,7,'admin:menu:write','メニュー更新','POST','/admin/sys/menus/**','メニュー管理更新',210,1,NULL,'2026-05-30 10:22:51',1,'2026-06-02 13:47:07',0),
(5,8,'admin:permission:read','権限参照','GET','/admin/sys/permissions/**','権限管理参照',250,1,NULL,'2026-05-30 10:22:51',1,'2026-06-02 13:47:07',0),
(6,8,'admin:permission:write','権限更新','POST','/admin/sys/permissions/**','権限管理更新',260,1,NULL,'2026-05-30 10:22:51',1,'2026-06-02 13:47:07',0),
(7,9,'admin:role:read','ロール参照','GET','/admin/sys/roles/**','ロール管理参照',300,1,NULL,'2026-05-30 10:22:51',1,'2026-06-02 13:47:07',0),
(8,9,'admin:role:write','ロール更新','POST','/admin/sys/roles/**','ロール管理更新',310,1,NULL,'2026-05-30 10:22:51',1,'2026-06-02 13:47:07',0),
(9,NULL,'employee:timesheet:read','勤務表参照','GET','/employee/att/timesheet/**','社員向け勤務表参照権限',400,1,NULL,'2026-05-30 10:22:51',NULL,'2026-05-30 10:43:37',0),
(10,NULL,'employee:timesheet:write','勤務表更新','PUT','/employee/att/timesheet/**','社員向け勤務表更新権限',410,1,NULL,'2026-05-30 10:22:51',NULL,'2026-05-30 10:43:37',0),
(11,NULL,'manager:subordinate:read','部下参照','GET','/manager/emp/subordinates/**','部門管理者向け部下参照権限',500,1,NULL,'2026-05-30 10:43:34',NULL,'2026-05-30 10:43:37',0),
(12,NULL,'hr:employee:onboard','入社登録','POST','/admin/hr/onboarding/employees','本部人事向け入社登録権限',600,1,NULL,'2026-05-30 10:43:34',NULL,'2026-05-30 10:43:37',0);

/*Data for the table `sys_role` */

insert  into `sys_role`(`id`,`company_id`,`code`,`name`,`remark`,`sort`,`status`,`created_by`,`created_at`,`updated_by`,`updated_at`,`is_deleted`) values 
(1,1,'SUPER_ADMIN','スーパー管理者','全権限管理者',0,1,NULL,'2026-05-27 23:19:16',1,'2026-06-02 13:47:07',0),
(2,1,'EMPLOYEE','一般社員','一般社員ロール',10,1,NULL,'2026-05-30 10:43:34',NULL,'2026-05-30 10:43:34',0),
(3,1,'DEPT_MANAGER','部門管理者','部下勤務状況を参照できる部門管理者ロール',20,1,NULL,'2026-05-30 10:43:34',NULL,'2026-05-30 10:43:34',0),
(4,1,'HR_ADMIN','人事管理者','入社社員を登録できる本部人事ロール',30,1,NULL,'2026-05-30 10:43:34',NULL,'2026-05-30 10:43:34',0),
(5,3,'EMPLOYEE','一般社員','日本会社一般社員ロール',10,1,1,'2026-05-30 23:39:36',1,'2026-05-30 23:39:36',0),
(6,3,'DEPT_MANAGER','部門管理者','日本会社部門管理者ロール',20,1,1,'2026-05-30 23:39:36',1,'2026-05-30 23:39:36',0),
(7,3,'HR_ADMIN','人事管理者','日本会社人事管理者ロール',30,1,1,'2026-05-30 23:39:36',1,'2026-05-30 23:39:36',0);

/*Data for the table `sys_role_menu` */

insert  into `sys_role_menu`(`role_id`,`menu_id`,`created_by`,`created_at`) values 
(1,1,NULL,'2026-05-30 10:43:34'),
(1,2,NULL,'2026-05-30 10:43:34'),
(1,3,1,'2026-05-30 23:39:36'),
(1,4,1,'2026-05-30 23:39:36'),
(1,6,1,'2026-06-02 13:47:07'),
(1,7,1,'2026-06-02 13:47:07'),
(1,8,1,'2026-06-02 13:47:07'),
(1,9,1,'2026-06-02 13:47:07'),
(2,1,NULL,'2026-05-30 10:43:34'),
(2,2,NULL,'2026-05-30 10:43:34'),
(3,1,NULL,'2026-05-30 10:43:34'),
(3,2,NULL,'2026-05-30 10:43:34'),
(4,1,NULL,'2026-05-30 10:43:34'),
(4,2,NULL,'2026-05-30 10:43:34'),
(5,1,1,'2026-05-30 23:39:36'),
(5,2,1,'2026-05-30 23:39:36'),
(6,1,1,'2026-05-30 23:39:36'),
(6,2,1,'2026-05-30 23:39:36'),
(6,3,1,'2026-05-30 23:39:36'),
(7,1,1,'2026-05-30 23:39:36'),
(7,2,1,'2026-05-30 23:39:36'),
(7,4,1,'2026-05-30 23:39:36');

/*Data for the table `sys_role_permission` */

insert  into `sys_role_permission`(`role_id`,`permission_id`,`created_by`,`created_at`) values 
(1,3,NULL,'2026-05-30 10:22:52'),
(1,4,NULL,'2026-05-30 10:22:52'),
(1,5,NULL,'2026-05-30 10:22:52'),
(1,6,NULL,'2026-05-30 10:22:52'),
(1,7,NULL,'2026-05-30 10:22:52'),
(1,8,NULL,'2026-05-30 10:22:52'),
(5,9,1,'2026-05-30 23:39:36'),
(5,10,1,'2026-05-30 23:39:36'),
(6,9,1,'2026-05-30 23:39:36'),
(6,10,1,'2026-05-30 23:39:36'),
(6,11,1,'2026-05-30 23:39:36'),
(7,9,1,'2026-05-30 23:39:36'),
(7,10,1,'2026-05-30 23:39:36'),
(7,12,1,'2026-05-30 23:39:36');

/*Data for the table `wf_approval` */

/*Data for the table `wf_approval_rule` */

/*Data for the table `wf_approval_step` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
