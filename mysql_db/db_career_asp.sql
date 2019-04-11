-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Oct 18, 2012 at 11:42 PM
-- Server version: 5.0.51
-- PHP Version: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Database: `db_career_asp`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `tbl_cand_education`
-- 

CREATE TABLE `tbl_cand_education` (
  `Cand_Code` int(10) unsigned NOT NULL,
  `Higer_Edu` varchar(45) default NULL,
  `University` varchar(45) default NULL,
  `Division` varchar(45) default NULL,
  `Percentage` int(3) default NULL,
  PRIMARY KEY  (`Cand_Code`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `tbl_cand_education`
-- 

INSERT INTO `tbl_cand_education` VALUES (1, 'MCA', 'BU', 'first', 80);
INSERT INTO `tbl_cand_education` VALUES (2, 'MCA', 'BU', '1', 72);

-- --------------------------------------------------------

-- 
-- Table structure for table `tbl_cand_general`
-- 

CREATE TABLE `tbl_cand_general` (
  `Cand_Code` int(10) unsigned NOT NULL,
  `Cell_No` varchar(12) default NULL,
  `Home_Phone` varchar(12) default NULL,
  `Gender` varchar(45) default NULL,
  `Address` varchar(45) default NULL,
  `DOB` varchar(45) default NULL,
  `Passport_No` varchar(45) default NULL,
  `Email_Id` varchar(45) default NULL,
  PRIMARY KEY  (`Cand_Code`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `tbl_cand_general`
-- 

INSERT INTO `tbl_cand_general` VALUES (1, '123456', '23456', 'F', 'vijayanagar', '1987-8-6', 'gg665hjh', 'shruti6@yaho.com');
INSERT INTO `tbl_cand_general` VALUES (2, '234324', '3243243', 'M', 'Bangalore, India', '1985-6-16', '445765344', 'yohoprashant@yahoo.com');
INSERT INTO `tbl_cand_general` VALUES (3, '234324', '324324', 'M', '', '1986-1-1', '', '');

-- --------------------------------------------------------

-- 
-- Table structure for table `tbl_cand_primary`
-- 

CREATE TABLE `tbl_cand_primary` (
  `Cand_Code` int(10) unsigned NOT NULL,
  `FName` varchar(45) NOT NULL,
  `LName` varchar(45) NOT NULL,
  `Picture` varchar(45) default NULL,
  `Resume` varchar(45) default NULL,
  `Employeed` varchar(3) default NULL,
  PRIMARY KEY  (`Cand_Code`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `tbl_cand_primary`
-- 

INSERT INTO `tbl_cand_primary` VALUES (1, 'Shruti', 'Hirur', NULL, NULL, '0');
INSERT INTO `tbl_cand_primary` VALUES (2, 'Prashant', 'Shrama', '1261198136859.jpg', '10.docx', '0');
INSERT INTO `tbl_cand_primary` VALUES (3, 'Syed', 'Shahabuddin', NULL, NULL, '1');
INSERT INTO `tbl_cand_primary` VALUES (4, 'Munendra', 'Kumar', 'Curriculum Vitae.docx', '1261200549890.jpg', '1');
INSERT INTO `tbl_cand_primary` VALUES (5, 'kraipob', 'saengkhunthod', '1349451956945.jpg', '1349451896715.jpg', '1');

-- --------------------------------------------------------

-- 
-- Table structure for table `tbl_cand_professional`
-- 

CREATE TABLE `tbl_cand_professional` (
  `Cand_Code` int(10) unsigned NOT NULL,
  `Experience` int(2) unsigned default NULL,
  `Expected_Salary` int(10) unsigned default NULL,
  `Preferred_Type` varchar(45) default NULL,
  `Managable` varchar(45) default NULL,
  `Preferred_Jobs` varchar(45) default NULL,
  `RecruitedBy` varchar(45) default NULL,
  PRIMARY KEY  (`Cand_Code`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `tbl_cand_professional`
-- 

INSERT INTO `tbl_cand_professional` VALUES (1, 3, 35000, 'Full Time', 'Y', 'Web, Graphic Design', '');
INSERT INTO `tbl_cand_professional` VALUES (2, 0, 0, 'Part Time', 'Y', 'Top Management- IT', '');
INSERT INTO `tbl_cand_professional` VALUES (3, 3, 0, 'Part Time', 'Y', 'Top Management- IT', '');
INSERT INTO `tbl_cand_professional` VALUES (4, 0, 0, 'Part Time', 'Y', 'Top Management- IT', '');
INSERT INTO `tbl_cand_professional` VALUES (5, 0, 0, 'Part Time', 'Y', 'Top Management- IT', '');

-- --------------------------------------------------------

-- 
-- Table structure for table `tbl_comp_general`
-- 

CREATE TABLE `tbl_comp_general` (
  `Comp_Code` int(10) unsigned NOT NULL,
  `contactno1` varchar(12) default NULL,
  `contactno2` varchar(12) default NULL,
  `website` varchar(30) default NULL,
  `size` varchar(5) default NULL,
  `address` varchar(45) default NULL,
  PRIMARY KEY  (`Comp_Code`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `tbl_comp_general`
-- 

INSERT INTO `tbl_comp_general` VALUES (1, '080878787656', '08087987654', 'www.wiproit.com', '50', 'Bangalore');
INSERT INTO `tbl_comp_general` VALUES (2, '0989432432', '0985656547', 'www.infosys.com', '50', 'Bangalore');
INSERT INTO `tbl_comp_general` VALUES (3, '09890000998', '09800000997', 'www.clicktheblick.bus', '100', 'Pune');
INSERT INTO `tbl_comp_general` VALUES (4, '02343432343', '09876776765', 'www.smithcline.com', '200', 'New Delhi');
INSERT INTO `tbl_comp_general` VALUES (5, '0909098787', '0981265789', 'www.ewcm.com', '10', 'Bangalore');

-- --------------------------------------------------------

-- 
-- Table structure for table `tbl_comp_primary`
-- 

CREATE TABLE `tbl_comp_primary` (
  `Comp_Code` int(10) unsigned NOT NULL,
  `Name` varchar(45) NOT NULL,
  `Logo` varchar(25) default NULL,
  PRIMARY KEY  (`Comp_Code`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `tbl_comp_primary`
-- 

INSERT INTO `tbl_comp_primary` VALUES (1, 'wipro', NULL);
INSERT INTO `tbl_comp_primary` VALUES (2, 'infosys', NULL);
INSERT INTO `tbl_comp_primary` VALUES (3, 'click the brick', NULL);
INSERT INTO `tbl_comp_primary` VALUES (4, 'smithcline', NULL);
INSERT INTO `tbl_comp_primary` VALUES (5, 'East West College of Manegament', NULL);

-- --------------------------------------------------------

-- 
-- Table structure for table `tbl_comp_professional`
-- 

CREATE TABLE `tbl_comp_professional` (
  `Comp_Code` int(10) unsigned NOT NULL,
  `Sal_scale` varchar(15) default NULL,
  `Hrm` varchar(45) default NULL,
  `Email_id` varchar(45) default NULL,
  `Contact_no` varchar(12) default NULL,
  `Fax` varchar(12) default NULL,
  `Emp_Benefits` varchar(45) default NULL,
  PRIMARY KEY  (`Comp_Code`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `tbl_comp_professional`
-- 

INSERT INTO `tbl_comp_professional` VALUES (1, '10000-20000', 'Rajnikanth', 'rajni@gmail.com', '0898878767', '08078787876', 'd,m,r,s,');
INSERT INTO `tbl_comp_professional` VALUES (2, '10000-20000', 'sharukh', 'sharukh@gmail.com', '09897654342', '080678786787', 'd,m,r,s,');
INSERT INTO `tbl_comp_professional` VALUES (3, '10000-20000', 'nanapateker', 'nana@gmail.com', '0990098765', '0243565432', 'm,s,');
INSERT INTO `tbl_comp_professional` VALUES (4, '10000-20000', 'ranbeer', 'beer@gmail.com', '0876754632', '0754545321', 'm,r,');
INSERT INTO `tbl_comp_professional` VALUES (5, '10000-20000', '', '', '0908987876', '08034565432', '');

-- --------------------------------------------------------

-- 
-- Table structure for table `tbl_job`
-- 

CREATE TABLE `tbl_job` (
  `job_code` int(10) unsigned NOT NULL,
  `company` varchar(45) NOT NULL,
  `designation` varchar(45) default NULL,
  `opening_date` date default NULL,
  `closeing_date` date default NULL,
  `shifts` varchar(45) default NULL,
  `ann_salary` decimal(10,0) default NULL,
  `no_of_vac` decimal(10,0) default NULL,
  PRIMARY KEY  (`job_code`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `tbl_job`
-- 

INSERT INTO `tbl_job` VALUES (1, 'wipro', 'IT, Software Services', '2009-09-17', '2009-12-19', '', 350000, 10);
INSERT INTO `tbl_job` VALUES (2, 'infosys', 'IT, Software Services', '2009-11-17', '2009-12-17', '', 300000, 50);
INSERT INTO `tbl_job` VALUES (3, 'click the brick', 'Top Management- Non IT', '2009-11-24', '2009-12-17', '', 275000, 100);
INSERT INTO `tbl_job` VALUES (4, 'smithcline', 'Marketing', '2009-11-24', '2009-12-19', '', 400000, 80);
INSERT INTO `tbl_job` VALUES (5, 'East West College of Manegament', 'Teaching, Education', '2009-10-17', '2009-12-24', '', 250000, 10);
INSERT INTO `tbl_job` VALUES (6, 'infosys', 'Top Management- IT', '2012-07-24', '2012-07-24', '', 0, 0);

-- --------------------------------------------------------

-- 
-- Table structure for table `tbl_user_auth`
-- 

CREATE TABLE `tbl_user_auth` (
  `user_name` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `authentications` varchar(100) default NULL,
  PRIMARY KEY  USING BTREE (`user_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `tbl_user_auth`
-- 

INSERT INTO `tbl_user_auth` VALUES ('admin', 'admin', '01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,');
INSERT INTO `tbl_user_auth` VALUES ('prashant', 'sharma', '01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,');
INSERT INTO `tbl_user_auth` VALUES ('shahabuddin', 'syed', '01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,');
INSERT INTO `tbl_user_auth` VALUES ('shurti', 'hirur', '01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,');
INSERT INTO `tbl_user_auth` VALUES ('sunny', 'pass', '03,04,05,07,08,09,11,');
INSERT INTO `tbl_user_auth` VALUES ('pob', '1234', '01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,');

-- --------------------------------------------------------

-- 
-- Table structure for table `tbl_user_login`
-- 

CREATE TABLE `tbl_user_login` (
  `Log_Id` int(10) unsigned NOT NULL,
  `User_Name` varchar(45) default NULL,
  `Login` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`Log_Id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `tbl_user_login`
-- 

INSERT INTO `tbl_user_login` VALUES (1, 'shahabuddin', '2009-12-18 19:29:22');
INSERT INTO `tbl_user_login` VALUES (2, 'shurti', '2009-12-18 19:29:38');
INSERT INTO `tbl_user_login` VALUES (3, 'prashant', '2009-12-18 19:41:12');
INSERT INTO `tbl_user_login` VALUES (4, 'prashant', '2009-12-19 09:19:28');
INSERT INTO `tbl_user_login` VALUES (5, 'admin', '2009-12-19 09:22:27');
INSERT INTO `tbl_user_login` VALUES (6, 'admin', '2009-12-19 10:08:53');
INSERT INTO `tbl_user_login` VALUES (7, 'admin', '2009-12-19 10:12:02');
INSERT INTO `tbl_user_login` VALUES (8, 'admin', '2009-12-19 10:14:22');
INSERT INTO `tbl_user_login` VALUES (9, 'admin', '2009-12-19 10:18:00');
INSERT INTO `tbl_user_login` VALUES (10, 'admin', '2009-12-19 10:28:46');
INSERT INTO `tbl_user_login` VALUES (11, 'admin', '2009-12-19 10:35:50');
INSERT INTO `tbl_user_login` VALUES (12, 'admin', '2009-12-19 10:58:33');
INSERT INTO `tbl_user_login` VALUES (13, 'admin', '2009-12-19 23:35:51');
INSERT INTO `tbl_user_login` VALUES (14, 'admin', '2009-12-19 23:36:34');
INSERT INTO `tbl_user_login` VALUES (15, 'admin', '2009-12-19 23:37:22');
INSERT INTO `tbl_user_login` VALUES (16, 'admin', '2012-07-25 22:57:22');
INSERT INTO `tbl_user_login` VALUES (17, 'admin', '2012-07-25 22:58:13');
INSERT INTO `tbl_user_login` VALUES (18, 'admin', '2012-07-25 23:02:29');
INSERT INTO `tbl_user_login` VALUES (19, 'admin', '2012-07-26 11:54:26');
INSERT INTO `tbl_user_login` VALUES (20, 'admin', '2012-08-02 13:00:02');
INSERT INTO `tbl_user_login` VALUES (21, 'pob', '2012-09-28 14:06:00');
INSERT INTO `tbl_user_login` VALUES (22, 'pob', '2012-09-29 15:50:52');
INSERT INTO `tbl_user_login` VALUES (23, 'pob', '2012-09-29 15:53:16');
INSERT INTO `tbl_user_login` VALUES (24, 'pob', '2012-09-29 15:54:21');
INSERT INTO `tbl_user_login` VALUES (25, 'pob', '2012-09-29 15:58:34');
INSERT INTO `tbl_user_login` VALUES (26, 'pob', '2012-09-30 02:55:35');
INSERT INTO `tbl_user_login` VALUES (27, 'pob', '2012-09-30 02:58:08');
INSERT INTO `tbl_user_login` VALUES (28, 'pob', '2012-10-05 22:07:37');
INSERT INTO `tbl_user_login` VALUES (29, 'pob', '2012-10-05 22:16:19');
INSERT INTO `tbl_user_login` VALUES (30, 'pob', '2012-10-05 22:21:20');
INSERT INTO `tbl_user_login` VALUES (31, 'pob', '2012-10-05 22:42:00');
INSERT INTO `tbl_user_login` VALUES (32, 'pob', '2012-10-05 23:36:23');
INSERT INTO `tbl_user_login` VALUES (33, 'pob', '2012-10-07 08:09:41');
INSERT INTO `tbl_user_login` VALUES (34, 'pob', '2012-10-07 08:10:32');
INSERT INTO `tbl_user_login` VALUES (35, 'pob', '2012-10-09 05:51:08');

-- --------------------------------------------------------

-- 
-- Table structure for table `tbl_user_logout`
-- 

CREATE TABLE `tbl_user_logout` (
  `log_id` int(10) unsigned NOT NULL,
  `logout` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`log_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `tbl_user_logout`
-- 

INSERT INTO `tbl_user_logout` VALUES (1, '2009-12-18 19:29:26');
INSERT INTO `tbl_user_logout` VALUES (2, '2009-12-18 19:29:40');
INSERT INTO `tbl_user_logout` VALUES (3, '2009-12-18 19:41:21');
INSERT INTO `tbl_user_logout` VALUES (4, '2009-12-19 09:20:59');
INSERT INTO `tbl_user_logout` VALUES (6, '2009-12-19 10:10:34');
INSERT INTO `tbl_user_logout` VALUES (7, '2009-12-19 10:12:48');
INSERT INTO `tbl_user_logout` VALUES (9, '2009-12-19 10:19:12');
INSERT INTO `tbl_user_logout` VALUES (10, '2009-12-19 10:35:28');
INSERT INTO `tbl_user_logout` VALUES (11, '2009-12-19 10:58:25');
INSERT INTO `tbl_user_logout` VALUES (12, '2009-12-19 11:11:39');
INSERT INTO `tbl_user_logout` VALUES (13, '2009-12-19 23:35:54');
INSERT INTO `tbl_user_logout` VALUES (14, '2009-12-19 23:36:37');
INSERT INTO `tbl_user_logout` VALUES (15, '2009-12-19 23:37:30');
INSERT INTO `tbl_user_logout` VALUES (372, '2009-12-18 19:29:07');
INSERT INTO `tbl_user_logout` VALUES (16, '2012-07-25 22:58:06');
INSERT INTO `tbl_user_logout` VALUES (17, '2012-07-25 23:02:20');
INSERT INTO `tbl_user_logout` VALUES (18, '2012-07-25 23:03:16');
INSERT INTO `tbl_user_logout` VALUES (19, '2012-07-26 11:56:02');
INSERT INTO `tbl_user_logout` VALUES (20, '2012-08-02 13:00:34');
INSERT INTO `tbl_user_logout` VALUES (21, '2012-09-28 14:06:19');
INSERT INTO `tbl_user_logout` VALUES (22, '2012-09-29 15:52:40');
INSERT INTO `tbl_user_logout` VALUES (23, '2012-09-29 15:54:05');
INSERT INTO `tbl_user_logout` VALUES (24, '2012-09-29 15:58:25');
INSERT INTO `tbl_user_logout` VALUES (25, '2012-09-29 15:59:40');
INSERT INTO `tbl_user_logout` VALUES (26, '2012-09-30 02:55:52');
INSERT INTO `tbl_user_logout` VALUES (27, '2012-09-30 02:58:59');
INSERT INTO `tbl_user_logout` VALUES (28, '2012-10-05 22:15:46');
INSERT INTO `tbl_user_logout` VALUES (29, '2012-10-05 22:19:17');
INSERT INTO `tbl_user_logout` VALUES (30, '2012-10-05 22:21:28');
INSERT INTO `tbl_user_logout` VALUES (31, '2012-10-05 22:46:10');
INSERT INTO `tbl_user_logout` VALUES (32, '2012-10-05 23:36:43');
INSERT INTO `tbl_user_logout` VALUES (33, '2012-10-07 08:10:25');
INSERT INTO `tbl_user_logout` VALUES (34, '2012-10-07 08:10:54');
INSERT INTO `tbl_user_logout` VALUES (35, '2012-10-09 05:53:20');
