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

/*Data for the table `att_request` */

/*Data for the table `emp_account` */

/*Data for the table `emp_employee` */

/*Data for the table `emp_employee_position` */

/*Data for the table `org_company` */

/*Data for the table `org_grade` */

/*Data for the table `org_node` */

/*Data for the table `org_node_closure` */

/*Data for the table `sys_employee_role` */

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

/*Data for the table `sys_permission` */

/*Data for the table `sys_role` */

/*Data for the table `sys_role_menu` */

/*Data for the table `sys_role_permission` */

/*Data for the table `wf_approval` */

/*Data for the table `wf_approval_rule` */

/*Data for the table `wf_approval_step` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
