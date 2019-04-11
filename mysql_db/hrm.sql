-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Oct 18, 2012 at 11:37 PM
-- Server version: 5.0.51
-- PHP Version: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Database: `hrm`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `bonusrate`
-- 

CREATE TABLE `bonusrate` (
  `PID` varchar(4) NOT NULL,
  `YearDuration` int(11) NOT NULL,
  `BonusRate` double default NULL,
  PRIMARY KEY  (`PID`,`YearDuration`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `bonusrate`
-- 

INSERT INTO `bonusrate` VALUES ('A002', 1, 2);
INSERT INTO `bonusrate` VALUES ('A002', 2, 3.5);
INSERT INTO `bonusrate` VALUES ('A002', 3, 5);
INSERT INTO `bonusrate` VALUES ('A002', 4, 6.5);
INSERT INTO `bonusrate` VALUES ('A002', 5, 8);

-- --------------------------------------------------------

-- 
-- Table structure for table `department`
-- 

CREATE TABLE `department` (
  `DID` varchar(2) NOT NULL,
  `DepartmentName` varchar(50) default NULL,
  PRIMARY KEY  (`DID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `department`
-- 

INSERT INTO `department` VALUES ('01', 'ºÃÔËÒÃ');
INSERT INTO `department` VALUES ('02', 'ºÑ­ªÕáÅÐ¡ÒÃà§Ô¹');
INSERT INTO `department` VALUES ('03', '¡ÒÃµÅÒ´');
INSERT INTO `department` VALUES ('04', 'ºØ¤¤Å');
INSERT INTO `department` VALUES ('05', '»¯ÔºÑµÔ¡ÒÃ');

-- --------------------------------------------------------

-- 
-- Table structure for table `human`
-- 

CREATE TABLE `human` (
  `HID` varchar(10) NOT NULL,
  `InitialID` varchar(2) default NULL,
  `FullName` varchar(100) default NULL,
  `Sex` varchar(1) default NULL,
  `IDCard` varchar(17) default NULL,
  `Address` varchar(250) default NULL,
  `ProvinceID` varchar(2) default NULL,
  `Telephone` varchar(100) default NULL,
  `BirthDate` datetime default NULL,
  `PID` varchar(4) default NULL,
  `FirstDateToWork` datetime default NULL,
  `Salary` int(11) default NULL,
  `PositionFee` int(11) default NULL,
  `DailyWage` int(11) default NULL,
  `HourWage` int(11) default NULL,
  `OTPerHour` int(11) default NULL,
  `Education` text,
  `IsLeave` varchar(1) default NULL,
  `LastAccessBy` varchar(16) default NULL,
  PRIMARY KEY  (`HID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `human`
-- 

INSERT INTO `human` VALUES ('A001000001', '02', '¾Ã¾ÃÃÃ³ á¡éÇÈÃÕ', 'F', '6-4512-45962-01-0', '28/9 ËÁÙèºéÒ¹áÁé¹ÈÃÕà¨ÃÔ­ »ÃÐªÒÍØ·ÔÈ', '¡·', '086-555-1122', '1987-12-26 00:00:00', 'A001', '2000-11-03 00:00:00', 12000, 3000, 0, 0, 0, '', '0', 'thaivb');
INSERT INTO `human` VALUES ('A003000001', '01', '¾§ÉìÈÑ¡´Ôì ÇÔªÑÂà¨ÃÔ­', 'M', '8-7415-49845-46-4', '33/7 ËÁÙè·Õè 3 ËÁÙèºéÒ¹ÁØè§ÁÑè¹', '¡·', '086-222-3333', '1982-02-13 00:00:00', 'A003', '2005-10-19 00:00:00', 7500, 0, 0, 0, 30, '', '0', 'thaivb');
INSERT INTO `human` VALUES ('A003000002', '00', 'ÇÕÃÐªÑÂ ÁÑè¹ã¨', 'M', '5-8461-20149-61-2', '33/4 ËÁÙèºéÒ¹ÁÔè§¢ÇÑ­ ÅÒ´¾ÃéÒÇ 49/1', '¡·', '086-999-5555', '1983-01-23 00:00:00', 'A003', '2004-09-13 00:00:00', 7500, 0, 0, 0, 30, '', '0', 'thaivb');
INSERT INTO `human` VALUES ('A003000003', '01', '»ÃÐÇÔ·Âì ÃÑ¡ÉÒ·ÃÑ¾Âì', 'M', '6-4189-56694-35-6', '12/34 ËÁÙèºéÒ¹à¨ÃÔ­¹¤Ã ¶.ÇÔÀÒÇ´Õ', '¡·', '086-555-6666', '1984-09-25 00:00:00', 'A003', '2006-09-25 00:00:00', 8000, 0, 0, 0, 30, '', '0', '');
INSERT INTO `human` VALUES ('A005000001', '00', '»ÔÂÐ¾Ã ÁÑè¹ã¨', 'F', '8-7413-21697-89-8', '123/77 ËÁÙè·Õè 2 ¶.ÊØ¢ØÁÇÔ· Í.àÁ×Í§', 'Ãº', '086-999-5566', '1986-03-19 00:00:00', 'A005', '2005-09-19 00:00:00', 0, 0, 200, 0, 25, '', '0', '');

-- --------------------------------------------------------

-- 
-- Table structure for table `humanwithpicture`
-- 

CREATE TABLE `humanwithpicture` (
  `PictureID` char(38) NOT NULL,
  `HID` varchar(10) default NULL,
  `HumanPicture` tinyblob,
  PRIMARY KEY  (`PictureID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `humanwithpicture`
-- 

INSERT INTO `humanwithpicture` VALUES ('{62948F50-5919-4C97-8EA2-7A58405B083E}', 'Mk02000002', 0xffd8ffe0);
INSERT INTO `humanwithpicture` VALUES ('{5BE46C97-BC57-42AC-9A58-A777748C3E2A}', 'Mk02000003', 0xffd8ffe0);

-- --------------------------------------------------------

-- 
-- Table structure for table `initial`
-- 

CREATE TABLE `initial` (
  `InitialID` varchar(4) NOT NULL,
  `InitialName` varchar(30) default NULL,
  PRIMARY KEY  (`InitialID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `initial`
-- 

INSERT INTO `initial` VALUES ('0006', 'dfds');
INSERT INTO `initial` VALUES ('0004', 'sdf');
INSERT INTO `initial` VALUES ('0003', 'ko');
INSERT INTO `initial` VALUES ('0002', 'นางสาว');
INSERT INTO `initial` VALUES ('0001', 'dfds');

-- --------------------------------------------------------

-- 
-- Table structure for table `paidbonus`
-- 

CREATE TABLE `paidbonus` (
  `CurrentYear` datetime NOT NULL,
  `HID` varchar(10) NOT NULL,
  `PID` varchar(4) default NULL,
  `WorkDuration` int(11) default NULL,
  `Net` int(11) default NULL,
  `RecordBy` varchar(16) default NULL,
  PRIMARY KEY  (`CurrentYear`,`HID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `paidbonus`
-- 

INSERT INTO `paidbonus` VALUES ('2006-09-23 00:00:00', 'M001000001', 'M001', 9, 450000, '');
INSERT INTO `paidbonus` VALUES ('2006-12-31 00:00:00', 'Mk02000001', 'Mk02', 6, 70125, '');
INSERT INTO `paidbonus` VALUES ('2006-12-31 00:00:00', 'Mk02000002', 'Mk02', 1, 16500, '');
INSERT INTO `paidbonus` VALUES ('2006-12-31 00:00:00', 'Mk02000003', 'Mk02', 0, 0, '');
INSERT INTO `paidbonus` VALUES ('2006-12-31 00:00:00', 'A001000001', 'A001', 6, 0, '');

-- --------------------------------------------------------

-- 
-- Table structure for table `position`
-- 

CREATE TABLE `position` (
  `PID` varchar(4) NOT NULL,
  `PositionName` varchar(100) default NULL,
  `DID` varchar(2) default NULL,
  `WID` varchar(1) default NULL,
  `SalaryRate` int(11) default NULL,
  `PositionFee` int(11) default NULL,
  `DailyWage` int(11) default NULL,
  `HourWage` int(11) default NULL,
  `OTPerHour` int(11) default NULL,
  PRIMARY KEY  (`PID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `position`
-- 

INSERT INTO `position` VALUES ('A001', 'ÃÍ§¼Ùé¨Ñ´¡ÒÃá¼¹¡ºÑ­ªÕ', '02', '1', 10000, 3000, 0, 0, 0);
INSERT INTO `position` VALUES ('A002', 'ËÑÇË¹éÒá¼¹¡ºÑ­ªÕ', '02', '1', 10000, 0, 0, 0, 45);
INSERT INTO `position` VALUES ('A003', '¾¹Ñ¡§Ò¹ºÑ­ªÕ', '02', '1', 7500, 0, 0, 0, 30);
INSERT INTO `position` VALUES ('A004', '¾¹Ñ¡§Ò¹ºÑ­ªÕ½Ö¡§Ò¹', '02', '1', 6000, 0, 0, 0, 0);
INSERT INTO `position` VALUES ('A005', '¹Ñ¡ÈÖ¡ÉÒºÑ­ªÕ½Ö¡§Ò¹', '02', '2', 0, 0, 200, 0, 25);

-- --------------------------------------------------------

-- 
-- Table structure for table `province`
-- 

CREATE TABLE `province` (
  `ProvinceID` varchar(2) NOT NULL,
  `ProvinceName` varchar(50) default NULL,
  PRIMARY KEY  (`ProvinceID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `province`
-- 

INSERT INTO `province` VALUES ('กพ', 'กำแพงเพชร');
INSERT INTO `province` VALUES ('กบ', 'กระบี่');
INSERT INTO `province` VALUES ('กท', 'กรุงเทพ');
INSERT INTO `province` VALUES ('กญ', 'กาญจนบุรี');
INSERT INTO `province` VALUES ('นม', 'นครราชสีมา');
INSERT INTO `province` VALUES ('กส', 'กาฬสินธุ์');
INSERT INTO `province` VALUES ('ขก', 'ขอนแก่น');
INSERT INTO `province` VALUES ('จท', 'จันทบุรี');
INSERT INTO `province` VALUES ('ฉท', 'ฉะเชิงเทรา');
INSERT INTO `province` VALUES ('ชน', 'ชัยนาท');

-- --------------------------------------------------------

-- 
-- Table structure for table `username`
-- 

CREATE TABLE `username` (
  `UserName` varchar(250) default NULL,
  `Password` varchar(250) default NULL,
  `IsNormal` varchar(1) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `username`
-- 

INSERT INTO `username` VALUES ('test', '1234', '1');
INSERT INTO `username` VALUES ('pob', '1234', '1');

-- --------------------------------------------------------

-- 
-- Table structure for table `workdata`
-- 

CREATE TABLE `workdata` (
  `WDID` varchar(12) NOT NULL,
  `HID` varchar(10) default NULL,
  `CurrentMonth` datetime default NULL,
  `Salary` int(11) default NULL,
  `PositionFee` int(11) default NULL,
  `OTPerHour` int(11) default NULL,
  `DailyWage` int(11) default NULL,
  `HourWage` int(11) default NULL,
  `TotalDate` int(11) default NULL,
  `TotalHour` int(11) default NULL,
  `TotalOT` int(11) default NULL,
  `TotalLateDate` int(11) default NULL,
  `TotalAbsenceDate` int(11) default NULL,
  `TotalSickDate` int(11) default NULL,
  `TotalBusinessDate` int(11) default NULL,
  `OtherDC` int(11) default NULL,
  `SocialDC` int(11) default NULL,
  `Net` int(11) default NULL,
  `RecordBy` varchar(16) default NULL,
  PRIMARY KEY  (`WDID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `workdata`
-- 

INSERT INTO `workdata` VALUES ('200600000001', 'A005000001', '2006-09-28 00:00:00', 0, 0, 25, 200, 0, 10, 0, 5, 2, 0, 0, 0, 0, 0, 2125, '');

-- --------------------------------------------------------

-- 
-- Table structure for table `workdatadetail`
-- 

CREATE TABLE `workdatadetail` (
  `WDID` varchar(12) NOT NULL,
  `CurrentMonth` datetime NOT NULL,
  `StatusID` varchar(2) default NULL,
  PRIMARY KEY  (`CurrentMonth`,`WDID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `workdatadetail`
-- 

INSERT INTO `workdatadetail` VALUES ('200600000001', '2006-08-28 00:00:00', '00');
INSERT INTO `workdatadetail` VALUES ('200600000001', '2006-08-31 00:00:00', '00');

-- --------------------------------------------------------

-- 
-- Table structure for table `workstatus`
-- 

CREATE TABLE `workstatus` (
  `StatusID` varchar(2) NOT NULL,
  `StatusDescription` varchar(50) default NULL,
  PRIMARY KEY  (`StatusID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `workstatus`
-- 

INSERT INTO `workstatus` VALUES ('00', 'ÁÒÊÒÂ');
INSERT INTO `workstatus` VALUES ('01', 'ÅÒ»èÇÂ');
INSERT INTO `workstatus` VALUES ('02', 'ÅÒ¡Ô¨');
INSERT INTO `workstatus` VALUES ('03', '¢Ò´§Ò¹');

-- --------------------------------------------------------

-- 
-- Table structure for table `worktype`
-- 

CREATE TABLE `worktype` (
  `WID` varchar(1) NOT NULL,
  `WorkTypeName` varchar(30) default NULL,
  PRIMARY KEY  (`WID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `worktype`
-- 

INSERT INTO `worktype` VALUES ('1', 'ÃÒÂà´×Í¹');
INSERT INTO `worktype` VALUES ('2', 'ÃÒÂÇÑ¹');
INSERT INTO `worktype` VALUES ('3', 'ÃÒÂªÑèÇâÁ§');
INSERT INTO `worktype` VALUES ('4', '½Ö¡§Ò¹');
