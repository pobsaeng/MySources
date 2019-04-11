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
-- Database: `hirepurchase`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `bankaccounttype`
-- 

CREATE TABLE `bankaccounttype` (
  `BankAccountTypeID` varchar(1) NOT NULL,
  `BankAccountTypeName` varchar(30) default NULL,
  PRIMARY KEY  (`BankAccountTypeID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `bankaccounttype`
-- 

INSERT INTO `bankaccounttype` VALUES ('1', '¡ÃÐáÊÃÒÂÇÑ¹');
INSERT INTO `bankaccounttype` VALUES ('2', 'ÍÍÁ·ÃÑ¾Âì');

-- --------------------------------------------------------

-- 
-- Table structure for table `banklist`
-- 

CREATE TABLE `banklist` (
  `BID` varchar(25) NOT NULL,
  `BankID` varchar(4) NOT NULL,
  `BankAccountNumber` varchar(20) NOT NULL,
  `BankAccountTypeID` varchar(1) default NULL,
  `OwnName` varchar(50) default NULL,
  `BranchName` varchar(50) default NULL,
  PRIMARY KEY  (`BID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `banklist`
-- 

INSERT INTO `banklist` VALUES ('000', ' 000', 'à§Ô¹Ê´', '0', 'à§Ô¹Ê´', ' à§Ô¹Ê´');
INSERT INTO `banklist` VALUES ('BAY-1111', 'BAY', '1111', '1', 'ºÃÔÉÑ· Book Center ¨Ó¡Ñ´', 'ÃÒÁÍÔ¹·ÃÒ');
INSERT INTO `banklist` VALUES ('BBL-2222', 'BBL', '2222', '1', 'ºÃÔÉÑ· Book Center ¨Ó¡Ñ´', 'ºÒ§¡Ð»Ô');
INSERT INTO `banklist` VALUES ('GHB-3333', 'GHB', '3333', '2', 'ºÃÔÉÑ· Book Center ¨Ó¡Ñ´', 'á¨é§ÇÑ²¹Ð');

-- --------------------------------------------------------

-- 
-- Table structure for table `bankname`
-- 

CREATE TABLE `bankname` (
  `BankID` varchar(4) NOT NULL,
  `BankName` varchar(50) default NULL,
  PRIMARY KEY  (`BankID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `bankname`
-- 

INSERT INTO `bankname` VALUES ('BAY', '¸¹Ò¤ÒÃ¡ÃØ§ÈÃÕ');
INSERT INTO `bankname` VALUES ('BBL', '¸¹Ò¤ÒÃ¡ÃØ§à·¾');
INSERT INTO `bankname` VALUES ('BTI', '¸¹Ò¤ÒÃä·Â¸¹Ò¤ÒÃ');
INSERT INTO `bankname` VALUES ('GHB', '¸¹Ò¤ÒÃÍÒ¤ÒÃÊ§à¤ÃÒÐËì');
INSERT INTO `bankname` VALUES ('KTB', '¸¹Ò¤ÒÃ¡ÃØ§ä·Â');

-- --------------------------------------------------------

-- 
-- Table structure for table `branch`
-- 

CREATE TABLE `branch` (
  `BranchID` varchar(4) NOT NULL,
  `BranchName` varchar(100) default NULL,
  `BranchDetail` varchar(100) NOT NULL,
  PRIMARY KEY  (`BranchID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `branch`
-- 

INSERT INTO `branch` VALUES ('0001', 'เมืองใหม่', 'สาขา 1');
INSERT INTO `branch` VALUES ('0002', 'แยกคีรี', 'สาขา 2');
INSERT INTO `branch` VALUES ('0003', 'บางแสน', 'สาขา 3');
INSERT INTO `branch` VALUES ('0004', 'ชลบุรี', 'สาขา 4');
INSERT INTO `branch` VALUES ('0005', 'ศรีราชา', 'สาขา 5');

-- --------------------------------------------------------

-- 
-- Table structure for table `category`
-- 

CREATE TABLE `category` (
  `CategoryID` varchar(2) NOT NULL,
  `CategoryName` varchar(100) default NULL,
  PRIMARY KEY  (`CategoryID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `category`
-- 

INSERT INTO `category` VALUES ('01', 'หนังสือพิมพ์');
INSERT INTO `category` VALUES ('02', 'นิตยสาร');
INSERT INTO `category` VALUES ('03', 'หนังสือคอมพิวเตอร์ทั่วไป');
INSERT INTO `category` VALUES ('04', 'หนังสืออ่านนอกเวลา');
INSERT INTO `category` VALUES ('05', 'หนังสือวิชาการ');
INSERT INTO `category` VALUES ('06', 'หนังสือทั่วไป');
INSERT INTO `category` VALUES ('07', 'การ์ตูน');
INSERT INTO `category` VALUES ('08', 'หนังสือเขียนโปรแกรม');

-- --------------------------------------------------------

-- 
-- Table structure for table `consignment`
-- 

CREATE TABLE `consignment` (
  `CSMID` varchar(15) NOT NULL,
  `BranchID` varchar(4) NOT NULL,
  `SupplierID` varchar(3) default NULL,
  `ReceiveDate` datetime default NULL,
  `ReceiveBy` varchar(16) default NULL,
  PRIMARY KEY  (`BranchID`,`CSMID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `consignment`
-- 

INSERT INTO `consignment` VALUES ('200600010000001', '0001', '001', '2006-07-19 00:00:00', 'thaivb');
INSERT INTO `consignment` VALUES ('200600010000002', '0001', '001', '2006-07-22 00:00:00', 'thaivb');
INSERT INTO `consignment` VALUES ('200600010000003', '0001', '001', '2006-07-22 00:00:00', 'thaivb');
INSERT INTO `consignment` VALUES ('200600010000004', '0001', '001', '2006-08-09 00:00:00', 'thaivb');
INSERT INTO `consignment` VALUES ('200600010000005', '0001', '002', '2006-08-09 00:00:00', 'thaivb');

-- --------------------------------------------------------

-- 
-- Table structure for table `consignmentdetail`
-- 

CREATE TABLE `consignmentdetail` (
  `CSMID` varchar(15) NOT NULL,
  `ProductID` varchar(13) NOT NULL,
  `NumberToReceived` int(11) default NULL,
  `UnitID` varchar(4) default NULL,
  PRIMARY KEY  (`CSMID`,`ProductID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `consignmentdetail`
-- 

INSERT INTO `consignmentdetail` VALUES ('200600010000001', '00002', 1, 'b36');
INSERT INTO `consignmentdetail` VALUES ('200600010000001', '00027', 1, 'dz');
INSERT INTO `consignmentdetail` VALUES ('200600010000001', '00030', 1, 'b36');
INSERT INTO `consignmentdetail` VALUES ('200600010000001', '00039', 1, 'pk24');
INSERT INTO `consignmentdetail` VALUES ('200600010000002', '00002', 1, 'pk24');

-- --------------------------------------------------------

-- 
-- Table structure for table `consignmentreturn`
-- 

CREATE TABLE `consignmentreturn` (
  `ReturnID` varchar(15) NOT NULL,
  `BranchID` varchar(4) NOT NULL,
  `SupplierID` varchar(3) default NULL,
  `ReturnDate` datetime default NULL,
  `ReturnBy` varchar(16) default NULL,
  PRIMARY KEY  (`ReturnID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `consignmentreturn`
-- 

INSERT INTO `consignmentreturn` VALUES ('200600010000001', '0001', '001', '2006-08-10 00:00:00', 'thaivb');
INSERT INTO `consignmentreturn` VALUES ('200600010000002', '0001', '002', '2006-08-10 00:00:00', 'thaivb');
INSERT INTO `consignmentreturn` VALUES ('200600010000003', '0001', '003', '2006-08-10 00:00:00', 'thaivb');

-- --------------------------------------------------------

-- 
-- Table structure for table `consignmentreturndetail`
-- 

CREATE TABLE `consignmentreturndetail` (
  `ReturnID` varchar(15) NOT NULL,
  `ProductID` varchar(13) NOT NULL,
  `NumberToReturn` int(11) default NULL,
  PRIMARY KEY  (`ProductID`,`ReturnID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `consignmentreturndetail`
-- 

INSERT INTO `consignmentreturndetail` VALUES ('200600010000001', '00002', 1);
INSERT INTO `consignmentreturndetail` VALUES ('200600010000001', '00027', 2);
INSERT INTO `consignmentreturndetail` VALUES ('200600010000001', '00030', 3);
INSERT INTO `consignmentreturndetail` VALUES ('200600010000001', '00039', 4);
INSERT INTO `consignmentreturndetail` VALUES ('200600010000002', '00013', 5);

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
-- Table structure for table `dtproperties`
-- 

CREATE TABLE `dtproperties` (
  `id` int(11) NOT NULL,
  `objectid` int(11) default NULL,
  `property` varchar(64) NOT NULL,
  `value` varchar(255) default NULL,
  `uvalue` varchar(255) default NULL,
  `lvalue` tinyblob,
  `version` int(11) NOT NULL,
  PRIMARY KEY  (`id`,`property`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `dtproperties`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `incomebymember`
-- 

CREATE TABLE `incomebymember` (
  `ICID` varchar(10) NOT NULL,
  `BranchID` varchar(4) default NULL,
  `CustomerID` varchar(10) NOT NULL,
  `ReceiveDate` datetime default NULL,
  `Total` double default NULL,
  `ICType` varchar(1) default NULL,
  PRIMARY KEY  (`ICID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `incomebymember`
-- 

INSERT INTO `incomebymember` VALUES ('0001000001', '0001', '0001000007', '2006-05-08 00:00:00', 250, '0');
INSERT INTO `incomebymember` VALUES ('0001000002', '0001', '0001000002', '2006-05-08 00:00:00', 100, '1');
INSERT INTO `incomebymember` VALUES ('0001000003', '0001', '0001000020', '2006-05-08 00:00:00', 250, '0');
INSERT INTO `incomebymember` VALUES ('0001000004', '0001', '0001000021', '2006-05-08 00:00:00', 200, '0');
INSERT INTO `incomebymember` VALUES ('0001000005', '0001', '0001000022', '2006-05-09 00:00:00', 200, '0');

-- --------------------------------------------------------

-- 
-- Table structure for table `initial`
-- 

CREATE TABLE `initial` (
  `InitialID` varchar(2) NOT NULL,
  `InitialName` varchar(30) default NULL,
  PRIMARY KEY  (`InitialID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `initial`
-- 

INSERT INTO `initial` VALUES ('00', '¤Ø³');
INSERT INTO `initial` VALUES ('01', '¹ÒÂ');
INSERT INTO `initial` VALUES ('02', '¹Ò§ÊÒÇ');
INSERT INTO `initial` VALUES ('03', '¹Ò§');
INSERT INTO `initial` VALUES ('04', '´.ª.');

-- --------------------------------------------------------

-- 
-- Table structure for table `moreresource`
-- 

CREATE TABLE `moreresource` (
  `ResID` char(38) NOT NULL,
  `ProductID` varchar(13) default NULL,
  `FileName` varchar(100) default NULL,
  `Details` varchar(100) default NULL,
  `FileSize` varchar(30) default NULL,
  `AnyFile` longblob,
  PRIMARY KEY  (`ResID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `moreresource`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `orders`
-- 

CREATE TABLE `orders` (
  `OrderID` varchar(15) NOT NULL,
  `BranchID` varchar(4) default NULL,
  `SupplierID` varchar(3) default NULL,
  `PONo` varchar(15) default NULL,
  `OrderDate` datetime default NULL,
  `ReceiveDate` datetime default NULL,
  `PaidDate` datetime default NULL,
  `NetDC` double default NULL,
  `VATRate` double default NULL,
  `NetVAT` double default NULL,
  `Net` double default NULL,
  `IsReceivedAll` varchar(1) default NULL,
  `IsPaid` varchar(1) default NULL,
  `IsNormal` varchar(1) default NULL,
  `OrderBy` varchar(16) default NULL,
  `ReceivedBy` varchar(16) default NULL,
  `PaidBy` varchar(16) default NULL,
  PRIMARY KEY  (`OrderID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `orders`
-- 

INSERT INTO `orders` VALUES ('200600010000001', '0001', '001', '025178/2549', '2006-07-29 00:00:00', '2006-07-31 00:00:00', '2006-08-31 00:00:00', 0, 10, 3319.19995117188, 36511.2, '1', '1', '1', 'thaivb', 'thaivb', '');
INSERT INTO `orders` VALUES ('200600010000002', '0001', '001', '215608/2549', '2006-07-29 00:00:00', '2006-08-05 00:00:00', '2006-09-30 00:00:00', 0, 10, 753.599975585938, 8289.6, '1', '0', '1', 'thaivb', 'thaivb', '');
INSERT INTO `orders` VALUES ('200600010000003', '0001', '002', '1545157/2549', '2006-07-30 00:00:00', '2006-07-30 00:00:00', '2006-07-30 00:00:00', 0, 10, 6498, 71478, '1', '1', '1', 'thaivb', 'thaivb', '');
INSERT INTO `orders` VALUES ('200600010000004', '0001', '002', '115788/2549', '2006-08-02 00:00:00', '2006-08-02 00:00:00', '2006-08-02 00:00:00', 0, 10, 3216, 35376, '0', '0', '0', 'thaivb', '', '');
INSERT INTO `orders` VALUES ('200600010000005', '0001', '001', '025112/2549', '2006-08-05 00:00:00', '2006-08-12 00:00:00', '2006-08-31 00:00:00', 0, 10, 1860, 17160, '1', '1', '1', 'thaivb', 'thaivb', '');

-- --------------------------------------------------------

-- 
-- Table structure for table `ordersdetail`
-- 

CREATE TABLE `ordersdetail` (
  `OrderID` varchar(15) NOT NULL,
  `ProductID` varchar(13) NOT NULL,
  `NumberToOrder` int(11) default NULL,
  `UnitID` varchar(4) default NULL,
  `NumberToReceived` int(11) default NULL,
  `Cost` double default NULL,
  `DiscountPerUnit` double default NULL,
  `TotalDiscount` double default NULL,
  `Total` double default NULL,
  `IsReceivedAll` varchar(1) default NULL,
  PRIMARY KEY  (`OrderID`,`ProductID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `ordersdetail`
-- 

INSERT INTO `ordersdetail` VALUES ('200600010000001', '00001', 1, 'pk24', 1, 255, 0, 0, 6120, '1');
INSERT INTO `ordersdetail` VALUES ('200600010000001', '00009', 1, 'b36', 1, 270, 0, 0, 9720, '1');
INSERT INTO `ordersdetail` VALUES ('200600010000001', '00025', 2, 'pk24', 2, 299, 0, 0, 14352, '1');
INSERT INTO `ordersdetail` VALUES ('200600010000001', '00036', 1, 'dz', 1, 250, 0, 0, 3000, '1');
INSERT INTO `ordersdetail` VALUES ('200600010000002', '00003', 2, 'b24', 2, 7, 0, 0, 336, '1');

-- --------------------------------------------------------

-- 
-- Table structure for table `paidable`
-- 

CREATE TABLE `paidable` (
  `PID` varchar(15) NOT NULL,
  `PaidDate` datetime default NULL,
  `NetPaid` double default NULL,
  `PaidBy` varchar(16) default NULL,
  PRIMARY KEY  (`PID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `paidable`
-- 

INSERT INTO `paidable` VALUES ('200600010000001', '2006-08-09 00:00:00', 53671.2, 'thaivb');
INSERT INTO `paidable` VALUES ('200600010000002', '2006-08-09 00:00:00', 71478, 'thaivb');
INSERT INTO `paidable` VALUES ('200600010000003', '2006-08-09 00:00:00', 60944.4, 'thaivb');

-- --------------------------------------------------------

-- 
-- Table structure for table `paidablewithdetail`
-- 

CREATE TABLE `paidablewithdetail` (
  `PID` varchar(15) NOT NULL,
  `ListNumber` int(11) NOT NULL,
  `BID` varchar(25) default NULL,
  `ChequeNO` varchar(20) default NULL,
  `PaidByCheque` double default NULL,
  `PaidByCash` double default NULL,
  PRIMARY KEY  (`ListNumber`,`PID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `paidablewithdetail`
-- 

INSERT INTO `paidablewithdetail` VALUES ('200600010000001', 1, '000', '', 0, 13671);
INSERT INTO `paidablewithdetail` VALUES ('200600010000001', 2, 'BAY-1111', '2544170', 20000.2, 0);
INSERT INTO `paidablewithdetail` VALUES ('200600010000001', 3, 'BBL-2222', '56410054', 10000, 0);
INSERT INTO `paidablewithdetail` VALUES ('200600010000001', 4, 'GHB-3333', '5122058', 10000, 0);
INSERT INTO `paidablewithdetail` VALUES ('200600010000002', 1, '000', '', 0, 21478);

-- --------------------------------------------------------

-- 
-- Table structure for table `paidablewithpolist`
-- 

CREATE TABLE `paidablewithpolist` (
  `PID` varchar(15) NOT NULL,
  `ListNumber` int(11) NOT NULL,
  `OrderID` varchar(15) default NULL,
  `PONo` varchar(15) default NULL,
  `Net` double default NULL,
  PRIMARY KEY  (`ListNumber`,`PID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `paidablewithpolist`
-- 

INSERT INTO `paidablewithpolist` VALUES ('200600010000001', 1, '200600010000005', '025112/2549', 17160);
INSERT INTO `paidablewithpolist` VALUES ('200600010000001', 2, '200600010000001', '025178/2549', 36511.2);
INSERT INTO `paidablewithpolist` VALUES ('200600010000002', 1, '200600010000003', '1545157/2549', 71478);
INSERT INTO `paidablewithpolist` VALUES ('200600010000003', 1, '200600010000006', '552512/2549', 60944.4);

-- --------------------------------------------------------

-- 
-- Table structure for table `paidtosupplier`
-- 

CREATE TABLE `paidtosupplier` (
  `PID` varchar(15) NOT NULL,
  `PaidDate` datetime default NULL,
  `NetPaid` double default NULL,
  `PaidBy` varchar(16) default NULL,
  PRIMARY KEY  (`PID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `paidtosupplier`
-- 

INSERT INTO `paidtosupplier` VALUES ('200600010000001', '2006-07-20 00:00:00', 627.9, 'thaivb');
INSERT INTO `paidtosupplier` VALUES ('200600010000002', '2006-08-16 00:00:00', 2580.46, 'thaivb');
INSERT INTO `paidtosupplier` VALUES ('200600010000003', '2006-08-18 00:00:00', 878.19, 'thaivb');
INSERT INTO `paidtosupplier` VALUES ('200600010000004', '2006-08-18 00:00:00', 7, 'thaivb');

-- --------------------------------------------------------

-- 
-- Table structure for table `paidtosupplierwithdetail`
-- 

CREATE TABLE `paidtosupplierwithdetail` (
  `PID` varchar(15) NOT NULL,
  `ListNumber` int(11) NOT NULL,
  `BID` varchar(25) default NULL,
  `ChequeNO` varchar(20) default NULL,
  `PaidByCheque` double default NULL,
  `PaidByCash` double default NULL,
  PRIMARY KEY  (`ListNumber`,`PID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `paidtosupplierwithdetail`
-- 

INSERT INTO `paidtosupplierwithdetail` VALUES ('200600010000001', 1, '000', '', 0, 27.9);
INSERT INTO `paidtosupplierwithdetail` VALUES ('200600010000001', 2, 'BAY-1111', '111', 600, 0);
INSERT INTO `paidtosupplierwithdetail` VALUES ('200600010000002', 1, '000', '', 0, 580);
INSERT INTO `paidtosupplierwithdetail` VALUES ('200600010000002', 2, 'BBL-2222', '56412410', 2000.46, 0);
INSERT INTO `paidtosupplierwithdetail` VALUES ('200600010000003', 1, 'BBL-2222', '56456', 878.19, 0);

-- --------------------------------------------------------

-- 
-- Table structure for table `privilegeformember`
-- 

CREATE TABLE `privilegeformember` (
  `CustomerTypeID` varchar(1) NOT NULL,
  `CustomerTypeName` varchar(50) default NULL,
  `AdmitMemberFee` int(11) default NULL,
  `MemberContinue` int(11) default NULL,
  `MemberDuration` int(11) default NULL,
  `NumDateRentAdd` int(11) default NULL,
  `BuyRateDiscount` int(11) default NULL,
  `RentRateDiscount` int(11) default NULL,
  `FineRateDiscount` int(11) default NULL,
  `RentRate` int(11) default NULL,
  `RentFree` int(11) default NULL,
  PRIMARY KEY  (`CustomerTypeID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `privilegeformember`
-- 

INSERT INTO `privilegeformember` VALUES ('0', 'ลูกค้าปกติ', 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `privilegeformember` VALUES ('1', 'สมาชิก', 200, 100, 1, 3, 10, 12, 15, 3, 1);
INSERT INTO `privilegeformember` VALUES ('2', 'สมาชิก VIP', 250, 150, 2, 7, 15, 15, 15, 2, 1);

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
-- Table structure for table `productonbranch`
-- 

CREATE TABLE `productonbranch` (
  `ProductID` varchar(13) NOT NULL,
  `BranchID` varchar(4) NOT NULL,
  `UnitID` varchar(4) default NULL,
  `QuantityWithUnit` double default NULL,
  `RealQuantity` int(11) default NULL,
  `ProductInOrder` int(11) default NULL,
  `ProductInRent` int(11) default NULL,
  `LowLimitToOrder` int(11) default NULL,
  `LastAccessDate` datetime default NULL,
  PRIMARY KEY  (`BranchID`,`ProductID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `productonbranch`
-- 

INSERT INTO `productonbranch` VALUES ('00001', '0001', 'b12', 3.58333333333333, 43, 0, 0, 25, '2006-08-16 00:00:00');
INSERT INTO `productonbranch` VALUES ('00002', '0001', 'dz', 8.16666666666667, 98, 0, 0, 0, '2006-08-16 00:00:00');
INSERT INTO `productonbranch` VALUES ('00003', '0001', 'b24', 1.91666666666667, 46, 0, 0, 10, '2006-08-14 00:00:00');
INSERT INTO `productonbranch` VALUES ('00006', '0001', 'b36', 0.833333333333333, 30, 0, 0, 0, '2006-08-09 00:00:00');
INSERT INTO `productonbranch` VALUES ('00008', '0001', 'b24', 1.25, 30, 0, 0, 0, '2006-08-09 00:00:00');

-- --------------------------------------------------------

-- 
-- Table structure for table `producttype`
-- 

CREATE TABLE `producttype` (
  `ProductTypeID` varchar(1) NOT NULL,
  `ProductTypeName` varchar(20) default NULL,
  PRIMARY KEY  (`ProductTypeID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `producttype`
-- 

INSERT INTO `producttype` VALUES ('1', 'ขายตรง');
INSERT INTO `producttype` VALUES ('2', 'ขายฝาก');
INSERT INTO `producttype` VALUES ('3', 'ขายส่ง');
INSERT INTO `producttype` VALUES ('4', 'เช่าสินค้า');

-- --------------------------------------------------------

-- 
-- Table structure for table `productwithcost`
-- 

CREATE TABLE `productwithcost` (
  `ProductID` varchar(13) NOT NULL,
  `Cost` double NOT NULL,
  `RecordDate` datetime default NULL,
  PRIMARY KEY  (`Cost`,`ProductID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `productwithcost`
-- 

INSERT INTO `productwithcost` VALUES ('00001', 255, '2006-08-08 00:00:00');
INSERT INTO `productwithcost` VALUES ('00001', 260, '2006-06-25 00:00:00');
INSERT INTO `productwithcost` VALUES ('00003', 7, '2006-08-09 00:00:00');
INSERT INTO `productwithcost` VALUES ('00004', 8, '2006-08-12 00:00:00');
INSERT INTO `productwithcost` VALUES ('00005', 40, '2006-06-28 00:00:00');

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

INSERT INTO `province` VALUES ('ยธ', 'ยโสธร');
INSERT INTO `province` VALUES ('ชล', 'ชลบุรี');
INSERT INTO `province` VALUES ('นม', 'นครราชสีมา');
INSERT INTO `province` VALUES ('กม', 'กรุงเทพ');

-- --------------------------------------------------------

-- 
-- Table structure for table `rent`
-- 

CREATE TABLE `rent` (
  `RentID` varchar(15) NOT NULL,
  `BranchID` varchar(4) default NULL,
  `CustomerID` varchar(10) default NULL,
  `NetDC` int(11) default NULL,
  `NetFine` int(11) default NULL,
  `NetReceived` int(11) default NULL,
  `IsReturnAll` varchar(1) default NULL,
  `RentBy` varchar(16) default NULL,
  PRIMARY KEY  (`RentID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `rent`
-- 

INSERT INTO `rent` VALUES ('200600010000001', '0001', '0001000012', 20, 17, 85, '0', 'thaivb');
INSERT INTO `rent` VALUES ('200600010000002', '0001', '0001000005', 0, 0, 15, '0', 'thaivb');
INSERT INTO `rent` VALUES ('200600010000003', '0001', '0001000001', 15, 0, 57, '0', 'thaivb');
INSERT INTO `rent` VALUES ('200600010000004', '0001', '0001000018', 0, 0, 15, '0', 'thaivb');
INSERT INTO `rent` VALUES ('200600010000005', '0001', '0001000008', 13, 0, 64, '0', 'thaivb');

-- --------------------------------------------------------

-- 
-- Table structure for table `rentdetail`
-- 

CREATE TABLE `rentdetail` (
  `RentID` varchar(15) NOT NULL,
  `ProductID` varchar(13) NOT NULL,
  `RentDate` datetime default NULL,
  `ReturnDate` datetime default NULL,
  `CustomerReturnDate` datetime default NULL,
  `RentPrice` int(11) default NULL,
  `RentAmount` int(11) default NULL,
  `ReturnAmount` int(11) default NULL,
  `FineReceived` int(11) default NULL,
  `DCByMember` int(11) default NULL,
  `TotalReceived` int(11) default NULL,
  `IsReturn` varchar(1) default NULL,
  PRIMARY KEY  (`ProductID`,`RentID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `rentdetail`
-- 

INSERT INTO `rentdetail` VALUES ('200600010000001', '00037', '2006-08-19 00:00:00', '2006-08-21 00:00:00', '2006-08-24 00:00:00', 5, 1, 1, 12, 2, 20, '1');
INSERT INTO `rentdetail` VALUES ('200600010000001', '00044', '2006-08-19 00:00:00', '2006-08-22 00:00:00', '2006-08-24 00:00:00', 3, 2, 1, 5, 3, 20, '0');
INSERT INTO `rentdetail` VALUES ('200600010000001', '00046', '2006-08-19 00:00:00', '2006-08-25 00:00:00', '2006-08-24 00:00:00', 4, 2, 2, 0, 8, 40, '1');
INSERT INTO `rentdetail` VALUES ('200600010000001', '00049', '2006-08-19 00:00:00', '2006-08-28 00:00:00', '2006-08-28 00:00:00', 5, 1, 0, 0, 7, 38, '0');
INSERT INTO `rentdetail` VALUES ('200600010000002', '00037', '2006-08-19 00:00:00', '2006-08-22 00:00:00', '2006-08-22 00:00:00', 5, 1, 0, 0, 0, 15, '0');

-- --------------------------------------------------------

-- 
-- Table structure for table `sale`
-- 

CREATE TABLE `sale` (
  `SaleID` varchar(16) NOT NULL,
  `BranchID` varchar(4) default NULL,
  `CustomerID` varchar(10) default NULL,
  `SaleDate` datetime default NULL,
  `NetDCByMember` double default NULL,
  `NetDCBySale` double default NULL,
  `NetVAT` double default NULL,
  `NetTotal` double default NULL,
  `NetPaidToSupplier` double default NULL,
  `NetMeReceived` double default NULL,
  `SaleBy` varchar(16) default NULL,
  PRIMARY KEY  (`SaleID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `sale`
-- 

INSERT INTO `sale` VALUES ('200600010000001', '0001', '0001000056', '2006-08-13 00:00:00', 90, 76.2, 69, 754, 160.02, 525.78, 'thaivb');
INSERT INTO `sale` VALUES ('200600010000002', '0001', '0001000007', '2006-08-13 00:00:00', 98, 39.2, 52, 562, 7, 503.8, 'thaivb');
INSERT INTO `sale` VALUES ('200600010000003', '0001', '', '2006-08-13 00:00:00', 0, 0, 60, 658, 358.8, 239.2, 'thaivb');
INSERT INTO `sale` VALUES ('200600010000004', '0001', '', '2006-08-13 00:00:00', 0, 109.65, 119, 1305, 811.49, 374.86, 'thaivb');
INSERT INTO `sale` VALUES ('200600010000005', '0001', '0001000003', '2006-08-14 00:00:00', 130, 144.2, 132, 1439, 588.42, 719.38, 'thaivb');

-- --------------------------------------------------------

-- 
-- Table structure for table `saledetail`
-- 

CREATE TABLE `saledetail` (
  `SaleID` varchar(15) NOT NULL,
  `ProductID` varchar(13) NOT NULL,
  `Amount` int(11) default NULL,
  `SalePrice` double default NULL,
  `DCByMember` double default NULL,
  `DCBySale` double default NULL,
  `VAT` double default NULL,
  `Total` double default NULL,
  `PaidToSupplier` double default NULL,
  `MeReceived` double default NULL,
  `IsPaid` varchar(1) default NULL,
  PRIMARY KEY  (`ProductID`,`SaleID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `saledetail`
-- 

INSERT INTO `saledetail` VALUES ('200600010000001', '00001', 2, 254, 45, 50.8, 46, 503, 0, 457.2, '0');
INSERT INTO `saledetail` VALUES ('200600010000001', '00002', 1, 254, 45, 25.4, 23, 251, 160.02, 68.58, '1');
INSERT INTO `saledetail` VALUES ('200600010000002', '00001', 1, 254, 45, 25.4, 23, 251, 0, 228.6, '0');
INSERT INTO `saledetail` VALUES ('200600010000002', '00003', 1, 10, 2, 0, 1, 11, 0, 10, '0');
INSERT INTO `saledetail` VALUES ('200600010000002', '00009', 1, 276, 49, 13.8, 27, 289, 0, 262.2, '0');

-- --------------------------------------------------------

-- 
-- Table structure for table `supplier`
-- 

CREATE TABLE `supplier` (
  `SupplierID` varchar(3) NOT NULL,
  `SupplierName` varchar(100) default NULL,
  `Address` varchar(250) default NULL,
  `ContactName` varchar(100) default NULL,
  `Telephone` varchar(100) default NULL,
  `Fax` varchar(9) NOT NULL,
  `Email` varchar(50) NOT NULL,
  PRIMARY KEY  (`SupplierID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `supplier`
-- 

INSERT INTO `supplier` VALUES ('001', 'บริษัท ไทยโอสถ', '24/58 กรุงเทพฯ', 'คุณประวิทย์ เกรียงไกร', '0842131213', '212212', '546666');
INSERT INTO `supplier` VALUES ('002', 'บ.โอปอ', '99/88 สุขุมวิทย์ สาย 3', 'คุณพรหมศรี มีสุข', '0842132454', '5785645', '456666');
INSERT INTO `supplier` VALUES ('003', 'บริษัท ป.ประมง', '44/58 พัทยา', 'คุณณรงค์ศักดิ์ ทองมี', '0842132454', '45544', '45445');
INSERT INTO `supplier` VALUES ('004', 'บริษัท ไทยน้ำทิพย์', '199/55 โนนแดง', 'คุณเกรียงศักดิ์ ศรีสุนทร', '0805646521', '456546', '4656456');
INSERT INTO `supplier` VALUES ('005', 'บริษัทแสงชัย', '99/55 พระราม 5', 'ไกรภพ แสงขุนทด', '3313213123', '1121', '212');

-- --------------------------------------------------------

-- 
-- Table structure for table `unit`
-- 

CREATE TABLE `unit` (
  `UnitID` varchar(4) NOT NULL,
  `UnitName` varchar(100) default NULL,
  `QuantityPerUnit` int(11) default NULL,
  PRIMARY KEY  (`UnitID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `unit`
-- 

INSERT INTO `unit` VALUES ('b06', 'กล่อง(6 ชิ้น)', 6);
INSERT INTO `unit` VALUES ('b12', 'กล่อง(12 ชิ้น)', 12);
INSERT INTO `unit` VALUES ('b24', 'กล่อง(24 ชิ้น)', 24);
INSERT INTO `unit` VALUES ('b36', 'กล่อง(36 ชิ้น)', 36);
INSERT INTO `unit` VALUES ('dz', 'โหล (12 ชิ้น)', 12);
INSERT INTO `unit` VALUES ('g144', 'กุรุส (144 ชิ้น)', 144);
INSERT INTO `unit` VALUES ('p1', 'อัน/ชิ้น/แผ่น/เล่ม (1 ชิ้น)', 1);
INSERT INTO `unit` VALUES ('pk06', 'แพ็ค (6 ชิ้น)', 6);
INSERT INTO `unit` VALUES ('pk12', 'แพ็ค (12 ชิ้น)', 12);
INSERT INTO `unit` VALUES ('pk24', 'แพ็ค (24 ชิ้น)', 24);
INSERT INTO `unit` VALUES ('pk36', 'แพ็ค (36 ชิ้น)', 36);

-- --------------------------------------------------------

-- 
-- Table structure for table `username`
-- 

CREATE TABLE `username` (
  `UserName` varchar(250) NOT NULL,
  `Password` varchar(250) default NULL,
  `IsNormal` varchar(1) default NULL,
  PRIMARY KEY  (`UserName`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `username`
-- 

INSERT INTO `username` VALUES ('test', 'Lw7HXltgHIE=', '1');
INSERT INTO `username` VALUES ('thaivb', '7r3BAaCyHJ8=', '1');
