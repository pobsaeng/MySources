-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Oct 18, 2012 at 11:39 PM
-- Server version: 5.0.51
-- PHP Version: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Database: `ipos`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `customer`
-- 

CREATE TABLE `customer` (
  `CustomerID` varchar(10) NOT NULL,
  `InitialID` varchar(2) default NULL,
  `FullName` varchar(100) default NULL,
  `Sex` varchar(1) default NULL,
  `IDCard` varchar(17) default NULL,
  `Address` varchar(250) default NULL,
  `ProvinceID` varchar(2) default NULL,
  `Telephone` varchar(100) default NULL,
  `BranchID` varchar(4) default NULL,
  `CustomerTypeID` varchar(1) default NULL,
  `AdmitDate` datetime default NULL,
  `ExpireDate` datetime default NULL,
  `CustomerPicture` tinyblob,
  `IsMember` varchar(1) default NULL,
  `IsExpired` varchar(1) default NULL,
  PRIMARY KEY  (`CustomerID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `customer`
-- 

INSERT INTO `customer` VALUES ('0001000001', '02', '¡Ò¹´Ò áÊ§ÊÇèÒ§ (ÊÁÒªÔ¡ËÁ´ÍÒÂØ)', 'F', '5-2456-02577-99-6', '25/99 ËÁÙè·Õè 6 ºÒ§à¢¹ 10320', '¡·', '089-666-7777', '0001', '2', '2006-08-11 00:00:00', '2008-08-11 00:00:00', NULL, '1', '1');
INSERT INTO `customer` VALUES ('0001000002', '00', '¾ÃªÑÂ ¦éÍ§â¦¦Ôµ(ÊÁÒªÔ¡ËÁ´ÍÒÂØ)', 'M', '5-1234-69854-77-4', '29/99 ËÁÙèºéÒ¹ÈÃÕ·ÃÑ¾Âì ÅÒ´¾ÃéÒÇ', '¡·', '066-777-4499', '0001', '1', '2006-05-08 00:00:00', '2008-05-08 00:00:00', NULL, '1', '1');
INSERT INTO `customer` VALUES ('0001000003', '00', 'ÇÕÃÀÒ¾ áÊ§ªÑÂÈÃÕ(ÊÁÒªÔ¡ËÁ´ÍÒÂØ)', 'M', '3-7849-26857-66-4', '99/6 ÊÕÅÁ ºÒ§ÃÑ¡ 10700', '¡·', '081-699-1234', '0001', '2', '2006-08-21 00:00:00', '2007-08-21 00:00:00', NULL, '1', '1');
INSERT INTO `customer` VALUES ('0001000004', '00', '»ÃÐÀÒ ÈÃÕªÑÂ(ÊÁÒªÔ¡ËÁ´ÍÒÂØ)', 'F', '7-2354-56974-85-7', '66/77 ËÁÙè·Õè 3 ´ØÊÔµ', '¡·', '089-567-8888', '0001', '2', '2006-06-08 00:00:00', '2008-06-08 00:00:00', NULL, '1', '1');
INSERT INTO `customer` VALUES ('0001000005', '00', 'ÈÑ¡´ÔìªÑÂ ÇèÍ§ªÑÂÈÃÕ (ÊÁÒªÔ¡ËÁ´ÍÒÂØ)', 'M', '3-5748-45877-16-7', '77/66 ËÁÙèºéÒ¹ªÑÂ·ÇÕËÒ 10500', '¢¡', '086-777-7890', '0001', '1', '2004-08-21 00:00:00', '2005-08-21 00:00:00', NULL, '1', '1');

-- --------------------------------------------------------

-- 
-- Table structure for table `product`
-- 

CREATE TABLE `product` (
  `ProductID` varchar(13) NOT NULL,
  `ProductName` varchar(100) default NULL,
  `SupplierID` varchar(3) default NULL,
  `CategoryID` varchar(2) default NULL,
  `ProductTypeID` varchar(1) default NULL,
  `LastCost` double default NULL,
  `CostAvg` double default NULL,
  `SalePrice` double default NULL,
  `HirePurchaseRate` double default NULL,
  `RentPrice` int(11) default NULL,
  `NumberRentDate` int(11) default NULL,
  `FineRate` int(11) default NULL,
  `CustomerTypeID` varchar(1) default NULL,
  `ProductStatus` varchar(1) default NULL,
  PRIMARY KEY  (`ProductID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `product`
-- 

INSERT INTO `product` VALUES ('00001', 'คู่มือ Windows Vista', '001', '03', '1', 255, 257.5, 299, 0, 0, 0, 0, '1', '1');
INSERT INTO `product` VALUES ('00002', 'คู่มือ Windows XP', '001', '03', '2', 0, 0, 299, 30, 0, 0, 0, '2', '1');
INSERT INTO `product` VALUES ('00003', 'คู่มือ Potoshop', '001', '01', '1', 7, 7, 12, 0, 0, 0, 0, '0', '1');
INSERT INTO `product` VALUES ('00004', 'คู่มือ ภาษา C', '001', '01', '1', 8, 8, 12, 0, 0, 0, 0, '0', '1');
INSERT INTO `product` VALUES ('00005', 'ระบบร่างกาย', '002', '02', '1', 40, 40, 60, 0, 0, 0, 0, '0', '1');

-- --------------------------------------------------------

-- 
-- Table structure for table `producttype`
-- 

CREATE TABLE `producttype` (
  `ProductTypeID` varchar(7) NOT NULL,
  `ProductTypeName` varchar(50) NOT NULL,
  PRIMARY KEY  (`ProductTypeID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `producttype`
-- 

INSERT INTO `producttype` VALUES ('0000001', 'ข้าวสาร');
INSERT INTO `producttype` VALUES ('0000002', 'ของหวาน');
INSERT INTO `producttype` VALUES ('0000003', 'อาหารดอง');
INSERT INTO `producttype` VALUES ('0000004', 'อาหารกระป๋อง8888');
INSERT INTO `producttype` VALUES ('0000005', 'ผลไม้');
INSERT INTO `producttype` VALUES ('0000006', 'เครื่องชง');
INSERT INTO `producttype` VALUES ('0000007', 'น้ำชา');
INSERT INTO `producttype` VALUES ('0000008', 'น้ำผลไม้');
INSERT INTO `producttype` VALUES ('0000009', 'ขนมไทย');
INSERT INTO `producttype` VALUES ('0000010', 'ก๋วยเตี๋ยว');

-- --------------------------------------------------------

-- 
-- Table structure for table `supplier`
-- 

CREATE TABLE `supplier` (
  `SupplierID` varchar(5) NOT NULL,
  `SupplierName` varchar(50) NOT NULL,
  PRIMARY KEY  (`SupplierID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `supplier`
-- 

INSERT INTO `supplier` VALUES ('00001', 'ไทยน้ำทิพย์');
INSERT INTO `supplier` VALUES ('00002', 'สหพิบูรณ์');
INSERT INTO `supplier` VALUES ('00003', 'เป็บซี่');
INSERT INTO `supplier` VALUES ('00004', 'ซีพี');
