-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- โฮสต์: localhost
-- เวลาในการสร้าง: 
-- รุ่นของเซิร์ฟเวอร์: 5.0.51
-- รุ่นของ PHP: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- ฐานข้อมูล: `workshop_db`
-- 

-- --------------------------------------------------------

-- 
-- โครงสร้างตาราง `inv_product_category`
-- 

CREATE TABLE `inv_product_category` (
  `id` int(10) NOT NULL auto_increment,
  `product_category_name` varchar(50) default NULL,
  `product_category_desc` varchar(50) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `product_category_name` (`product_category_name`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=24 ;

-- 
-- dump ตาราง `inv_product_category`
-- 

INSERT INTO `inv_product_category` VALUES (1, 'มาม่า', 'มาม่า 10 กล่อง');
INSERT INTO `inv_product_category` VALUES (2, 'ปลากระป๋อง', 'category red');
INSERT INTO `inv_product_category` VALUES (3, 'ข้าวสาร', 'ข้าวสาร 1');
INSERT INTO `inv_product_category` VALUES (4, 'ข้าวเปือก', 'ข้าวเปือก 20 กิโลกรัม');
INSERT INTO `inv_product_category` VALUES (5, 'น้ำผลไม้', 'น้ำผลไม้ 1');
INSERT INTO `inv_product_category` VALUES (6, 'นม', 'นม 1');
INSERT INTO `inv_product_category` VALUES (7, 'ยาสีฟัน', 'ยาสีฟัน 5 กล่อง');
INSERT INTO `inv_product_category` VALUES (8, 'แปลงสีฟัน', 'แปลงสีฟัน 2 แผง');
INSERT INTO `inv_product_category` VALUES (9, 'ปลาแห้ง', '10 มัด');
INSERT INTO `inv_product_category` VALUES (10, 'ปลาหมึก', '20 ตัว');
INSERT INTO `inv_product_category` VALUES (11, 'ขนมขบเคี้ยว', '10 ห่อใหญ่');
INSERT INTO `inv_product_category` VALUES (12, 'นมผง', '5 แพ็ค');
INSERT INTO `inv_product_category` VALUES (13, 'ถุงร้อน', '2 แพ็ค');
INSERT INTO `inv_product_category` VALUES (14, 'ถุงเย็น', '2 แพ็ค');
INSERT INTO `inv_product_category` VALUES (15, 'หนังสือการ์ตูน', '10 เล่ม');
INSERT INTO `inv_product_category` VALUES (16, 'หนังสือชีวจี', '5 เล่ม');
INSERT INTO `inv_product_category` VALUES (17, 'เสื้อยืดแขนยาว', '5 ตัว');
INSERT INTO `inv_product_category` VALUES (18, 'เสื้อยืดแขนสั้น', '5 ตัว');
INSERT INTO `inv_product_category` VALUES (19, 'กางเกงขาสั้น ', '10 ตัว');
INSERT INTO `inv_product_category` VALUES (20, 'กางเกงขายาว', '5 ตัว');
INSERT INTO `inv_product_category` VALUES (21, 'กางเก่งในชาย', '10 ตัว');
INSERT INTO `inv_product_category` VALUES (22, 'กางเกงใจหญิง', '10 ตัว');
INSERT INTO `inv_product_category` VALUES (23, 'เต้าหู้', '2 ถัง');
