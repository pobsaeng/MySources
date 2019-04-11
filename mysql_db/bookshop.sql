-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Oct 18, 2012 at 11:40 PM
-- Server version: 5.0.51
-- PHP Version: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Database: `bookshop`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `book`
-- 

CREATE TABLE `book` (
  `id` int(11) NOT NULL auto_increment,
  `title` varchar(128) character set tis620 NOT NULL,
  `authors` varchar(256) character set tis620 NOT NULL,
  `price` int(11) NOT NULL,
  `stock` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=18 ;

-- 
-- Dumping data for table `book`
-- 

INSERT INTO `book` VALUES (1, 'ทิปส์เด็ด 3 โปรแกรม Word Excel PowerPoint', 'กัปตัน แจ็ค', 99, 5);
INSERT INTO `book` VALUES (2, 'กล้องดิจิตอล เต็มที่ทุกรายละเอียดของการถ่ายภาพ', 'ณัฐพงศ์ ฐิติมานะกุล, รักษ์ศักดิ์ สิทธิวิไล', 220, 3);
INSERT INTO `book` VALUES (3, 'The Secret เดอะซีเคร็ต', 'Rhonda Byrne', 245, 5);
INSERT INTO `book` VALUES (4, 'คู่มือสร้างเว็บไซท์ด้วยตนเอง', 'ดวงพร เกี๋ยงคำ', 249, 5);
INSERT INTO `book` VALUES (5, 'วิธีพูดกับลูก โดยไม่ทำร้ายจิตใจของเขา และทำให้เขาร่วมมือยอมทำตามคุณ', 'เฮม จีนอตต์', 189, 5);
INSERT INTO `book` VALUES (6, 'เขาเก็บเงินกันอย่างไรได้เป็นล้าน', 'อมิตา อริยอัชฌา', 140, 5);
INSERT INTO `book` VALUES (7, 'อัจฉริยะสร้างได้', 'วนิษา เรซ', 186, 5);
INSERT INTO `book` VALUES (8, '30 วิธีเอาชนะโชคชะตา ', 'บัณฑิต อึ้งรังษี', 175, 5);
INSERT INTO `book` VALUES (9, 'เรียนรู้เครือข่ายและอุปกรณ์ Cisco ด้วยโปรแกรม Simulation ', 'ธวัชชัย ชมศิริ, สุชาติ คุ้มมะณี', 249, 5);
INSERT INTO `book` VALUES (10, 'มองโลกแบบวิกรม ', 'วิกรม กรมดิษฐ์, วิมล ไทรนิ่มนวล', 100, 5);
INSERT INTO `book` VALUES (11, 'คู่มือสอบใบขับขี่รถยนต์และรถจักรยานยนต์', 'สมปอง คงนิ่ม', 49, 5);
INSERT INTO `book` VALUES (12, 'เว็บไซท์สวย สร้างง่ายด้วย Mambo', 'กอบเกียรติ สระอุบล', 195, 5);
INSERT INTO `book` VALUES (13, 'แฮร์รี่ พอตเตอร์กับเครื่องรางยมทูต เล่ม 7', 'J.K. ROWLING', 495, 5);
INSERT INTO `book` VALUES (14, 'ครบทุกเรื่อง คอมมือใหม่ เข้าใจง่าย', 'สุธีร์ นวกุล', 225, 5);
INSERT INTO `book` VALUES (15, 'เขียนโปรแกรม JAVA เบื้องต้น ', 'ทศพล ธนะทิพานนท์, วรเศรษฐ สุวรรณิก', 195, 5);
INSERT INTO `book` VALUES (16, 'เริ่มต้นเรียนรู้วิทยาการคอมพิวเตอร์', 'ยอดธง รอดแก้ว, วรเศรษฐ สุวรรณิก', 165, 5);
INSERT INTO `book` VALUES (17, 'Java GUI using NetBeans ', 'วรเศรษฐ สุวรรณิก', 145, 10);

-- --------------------------------------------------------

-- 
-- Table structure for table `employees`
-- 

CREATE TABLE `employees` (
  `Employee_ID` int(11) default NULL,
  `Name` varchar(30) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `employees`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `member`
-- 

CREATE TABLE `member` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `name` varchar(128) character set tis620 NOT NULL,
  `address` varchar(256) character set tis620 NOT NULL,
  `email` varchar(64) NOT NULL,
  `activated` tinyint(1) NOT NULL,
  `activate_code` char(32) NOT NULL,
  `register_date` datetime NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- 
-- Dumping data for table `member`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `order`
-- 

CREATE TABLE `order` (
  `id` int(11) NOT NULL auto_increment,
  `member_id` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `shipping_address` varchar(256) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- 
-- Dumping data for table `order`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `orders`
-- 

CREATE TABLE `orders` (
  `Prod_ID` int(11) default NULL,
  `ProductName` varchar(20) default NULL,
  `Employee_ID` int(11) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `orders`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `order_detail`
-- 

CREATE TABLE `order_detail` (
  `id` int(11) NOT NULL auto_increment,
  `order_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- 
-- Dumping data for table `order_detail`
-- 

