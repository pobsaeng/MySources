-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Oct 18, 2012 at 11:38 PM
-- Server version: 5.0.51
-- PHP Version: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Database: `pos_db`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `branch`
-- 

CREATE TABLE `branch` (
  `BranchID` varchar(4) NOT NULL,
  `BranchName` varchar(100) default NULL,
  PRIMARY KEY  (`BranchID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `branch`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `category`
-- 

CREATE TABLE `category` (
  `CategoryID` varchar(4) NOT NULL,
  `CategoryName` varchar(100) default NULL,
  PRIMARY KEY  (`CategoryID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `category`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `product`
-- 

CREATE TABLE `product` (
  `ProductID` varchar(13) NOT NULL,
  `ProductName` varchar(100) default NULL,
  `ProductTypeID` varchar(4) default NULL,
  `BranceID` varchar(4) default NULL,
  `SupplierID` varchar(4) default NULL,
  `CategoryID` varchar(4) default NULL,
  `Cost` float default NULL,
  `SalePrice` float default NULL,
  `Amount` int(11) default NULL,
  `ProductStatus` varchar(1) default NULL,
  PRIMARY KEY  (`ProductID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `product`
-- 

INSERT INTO `product` VALUES ('1025452230005', 'ข้าวเสาไห้', NULL, NULL, NULL, NULL, 80, NULL, NULL, NULL);
INSERT INTO `product` VALUES ('1025452230011', 'น้ำตาลทราย', NULL, NULL, NULL, NULL, NULL, 60, NULL, NULL);

-- --------------------------------------------------------

-- 
-- Table structure for table `productonbranch`
-- 

CREATE TABLE `productonbranch` (
  `ProductID` varchar(13) NOT NULL,
  `BranchID` varchar(4) default NULL,
  `UnitID` varchar(2) default NULL,
  `QuantityWithUnit` float default NULL,
  `RealQuantity` int(10) unsigned default NULL,
  `ProductInOrder` int(10) unsigned default NULL,
  `LowLimitToOrder` int(10) unsigned default NULL,
  `LastAccessDate` datetime default NULL,
  PRIMARY KEY  (`ProductID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `productonbranch`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `producttype`
-- 

CREATE TABLE `producttype` (
  `ProductTypeID` varchar(4) NOT NULL,
  `ProductTypeName` varchar(100) default NULL,
  PRIMARY KEY  (`ProductTypeID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `producttype`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `sale`
-- 

CREATE TABLE `sale` (
  `SaleID` varchar(16) NOT NULL,
  `CustomerID` varchar(14) default NULL,
  `SaleDate` varchar(15) default NULL,
  `VAT` double default NULL,
  `NetTotal` double default NULL,
  PRIMARY KEY  (`SaleID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `sale`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `saledetail`
-- 

CREATE TABLE `saledetail` (
  `SaleID` varchar(16) NOT NULL,
  `ProductID` varchar(10) NOT NULL,
  `SerialNumber` varchar(20) default NULL,
  `SalePrice` double default NULL,
  `Amount` int(11) default NULL,
  `Total` double default NULL,
  PRIMARY KEY  (`ProductID`,`SaleID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `saledetail`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `unit`
-- 

CREATE TABLE `unit` (
  `UnitID` varchar(2) NOT NULL,
  `UnitName` varchar(50) default NULL,
  `QuantityPerUnit` int(11) default NULL,
  PRIMARY KEY  (`UnitID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `unit`
-- 

