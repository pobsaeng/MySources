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
-- Database: `mobilecity`
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
-- Table structure for table `creditcompany`
-- 

CREATE TABLE `creditcompany` (
  `CreditCompanyID` varchar(4) NOT NULL,
  `CreditCompanyName` varchar(100) default NULL,
  `ContactName` varchar(100) default NULL,
  `Address` varchar(250) default NULL,
  `Telephone` varchar(50) default NULL,
  PRIMARY KEY  (`CreditCompanyID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `creditcompany`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `creditsale`
-- 

CREATE TABLE `creditsale` (
  `CreditSaleID` varchar(16) NOT NULL,
  `BranchID` varchar(4) NOT NULL,
  `ProductID` varchar(10) default NULL,
  `CustomerID` varchar(14) default NULL,
  `SerialNumber` varchar(20) default NULL,
  `DocReference` varchar(20) default NULL,
  `SaleDate` datetime default NULL,
  `CreditCompanyID` varchar(4) default NULL,
  `IntForPaidLatePerDate` double default NULL,
  `SalePrice` double default NULL,
  `VAT` double default NULL,
  `PreCash` int(11) default NULL,
  `AllInt` double default NULL,
  `NetPrice` double default NULL,
  `NetPriceReceived` double default NULL,
  `IsComplete` varchar(1) default NULL,
  `SaleBy` varchar(14) default NULL,
  PRIMARY KEY  (`CreditSaleID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `creditsale`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `creditsaledetail`
-- 

CREATE TABLE `creditsaledetail` (
  `CreditSaleID` varchar(16) NOT NULL,
  `PaidDate` datetime NOT NULL,
  `LastDateToPaid` datetime default NULL,
  `CustomerPaidDate` datetime default NULL,
  `PaidPerMonth` double default NULL,
  `IntWhenPaidLate` double default NULL,
  `NetToPaid` double default NULL,
  `IsPaid` varchar(1) default NULL,
  `ReceivedBy` varchar(14) default NULL,
  PRIMARY KEY  (`CreditSaleID`,`PaidDate`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `creditsaledetail`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `credittype`
-- 

CREATE TABLE `credittype` (
  `CreditTypeID` varchar(16) NOT NULL,
  `ProductID` varchar(10) default NULL,
  `SalePrice` double default NULL,
  `NetPrice` double default NULL,
  `CreditCompanyID` varchar(4) default NULL,
  `PreCash` int(11) default NULL,
  `PaidPerMonth` double default NULL,
  `IntPerMonth` double default NULL,
  `VATRate` double default NULL,
  `DurationToPaid` int(11) default NULL,
  `DateLateToCalculateInt` int(11) default NULL,
  `IntForPaidLatePerDate` double default NULL,
  PRIMARY KEY  (`CreditTypeID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `credittype`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `customer`
-- 

CREATE TABLE `customer` (
  `CustomerID` varchar(14) NOT NULL,
  `BranchID` varchar(4) default NULL,
  `FullName` varchar(100) default NULL,
  `Sex` varchar(1) default NULL,
  `IDCard` varchar(30) default NULL,
  `Address` varchar(250) default NULL,
  `Telephone` varchar(100) default NULL,
  PRIMARY KEY  (`CustomerID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `customer`
-- 

INSERT INTO `customer` VALUES ('1234', '02', 'kraipob', 'M', '1234545676', '12/3', '0802321456');

-- --------------------------------------------------------

-- 
-- Table structure for table `orders`
-- 

CREATE TABLE `orders` (
  `OrderID` varchar(16) NOT NULL,
  `BranchID` varchar(4) NOT NULL,
  `SupplierID` varchar(3) default NULL,
  `OrderDate` datetime default NULL,
  `SendDate` datetime default NULL,
  `PaidDate` datetime default NULL,
  `VAT` double default NULL,
  `Discount` double default NULL,
  `Net` double default NULL,
  `IsReceived` varchar(1) default NULL,
  `IsPaid` varchar(1) default NULL,
  `ChequeID` varchar(10) default NULL,
  `IsNormal` varchar(1) default NULL,
  `OrderBy` varchar(14) default NULL,
  `PaidBy` varchar(14) default NULL,
  PRIMARY KEY  (`OrderID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `orders`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `ordersdetail`
-- 

CREATE TABLE `ordersdetail` (
  `OrderID` varchar(16) NOT NULL,
  `ProductID` varchar(10) NOT NULL,
  `NumberToOrder` int(11) default NULL,
  `Cost` double default NULL,
  `Total` double default NULL,
  PRIMARY KEY  (`OrderID`,`ProductID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `ordersdetail`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `product`
-- 

CREATE TABLE `product` (
  `ProductID` varchar(10) NOT NULL,
  `ProductName` varchar(100) default NULL,
  `ProductDesc` varchar(250) default NULL,
  `ProductTypeID` varchar(2) default NULL,
  `SupplierID` varchar(3) default NULL,
  `Cost` double default NULL,
  `SalePrice` double default NULL,
  `PictureFileName` varchar(250) default NULL,
  `IsSale` varchar(1) default NULL,
  PRIMARY KEY  (`ProductID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `product`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `productonbranch`
-- 

CREATE TABLE `productonbranch` (
  `ProductID` varchar(10) NOT NULL,
  `BranchID` varchar(4) NOT NULL,
  `ProductInShop` int(11) default NULL,
  `ProductInOrder` int(11) default NULL,
  PRIMARY KEY  (`BranchID`,`ProductID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `productonbranch`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `producttype`
-- 

CREATE TABLE `producttype` (
  `ProductTypeID` varchar(2) NOT NULL,
  `ProductTypeName` varchar(100) default NULL,
  PRIMARY KEY  (`ProductTypeID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `producttype`
-- 

INSERT INTO `producttype` VALUES ('01', 'Android 1.2');
INSERT INTO `producttype` VALUES ('02', 'iPhone4');

-- --------------------------------------------------------

-- 
-- Table structure for table `sale`
-- 

CREATE TABLE `sale` (
  `SaleID` varchar(16) NOT NULL,
  `BranchID` varchar(4) NOT NULL,
  `CustomerID` varchar(14) default NULL,
  `SaleDate` datetime default NULL,
  `VAT` double default NULL,
  `NetTotal` double default NULL,
  `SaleBy` varchar(14) default NULL,
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
-- Table structure for table `supplier`
-- 

CREATE TABLE `supplier` (
  `SupplierID` varchar(3) NOT NULL,
  `SupplierName` varchar(100) default NULL,
  `Address` varchar(250) default NULL,
  `ContactName` varchar(100) default NULL,
  `Telephone` varchar(100) default NULL,
  PRIMARY KEY  (`SupplierID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `supplier`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `username`
-- 

CREATE TABLE `username` (
  `UserName` varchar(14) NOT NULL,
  `Password` varchar(14) default NULL,
  PRIMARY KEY  (`UserName`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `username`
-- 

INSERT INTO `username` VALUES ('pob', '1234');
