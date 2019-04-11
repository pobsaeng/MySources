-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Oct 18, 2012 at 11:44 PM
-- Server version: 5.0.51
-- PHP Version: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Database: `db_pos`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `tb_alert`
-- 

CREATE TABLE `tb_alert` (
  `alert_id` int(11) NOT NULL,
  `alert_topic` varchar(255) default NULL,
  `alert_day` date default NULL,
  `alert_detail` text,
  `alert_created_date` datetime default NULL,
  `alert_status` enum('wait','do','complete') default NULL,
  `user_id` int(11) default NULL,
  `branch_id` int(11) default NULL,
  PRIMARY KEY  (`alert_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `tb_alert`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_ar`
-- 

CREATE TABLE `tb_ar` (
  `ar_id` int(11) NOT NULL auto_increment,
  `member_id` int(11) NOT NULL,
  `bill_sale_id` int(11) default NULL,
  `ar_will_pay_date` date default NULL,
  `ar_pay_date` datetime default NULL,
  `ar_status` enum('wait','pay','cancel') collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`ar_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=39 ;

-- 
-- Dumping data for table `tb_ar`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_bill_import`
-- 

CREATE TABLE `tb_bill_import` (
  `bill_import_code` varchar(255) collate utf8_unicode_ci NOT NULL,
  `bill_import_created_date` datetime NOT NULL,
  `farmer_id` int(11) NOT NULL,
  `bill_import_pay` enum('cash','credit') collate utf8_unicode_ci NOT NULL,
  `bill_import_remark` varchar(1000) collate utf8_unicode_ci NOT NULL,
  `bill_import_pay_status` enum('pay','wait') collate utf8_unicode_ci NOT NULL,
  `bill_import_pay_date` datetime NOT NULL,
  `branch_id` int(11) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- Dumping data for table `tb_bill_import`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_bill_import_detail`
-- 

CREATE TABLE `tb_bill_import_detail` (
  `bill_import_detail_id` int(11) NOT NULL auto_increment,
  `bill_import_code` varchar(255) collate utf8_unicode_ci NOT NULL,
  `product_id` int(11) NOT NULL,
  `box_id` int(11) NOT NULL,
  `import_bill_detail_product_qty` int(5) NOT NULL,
  `import_bill_detail_price` float NOT NULL,
  `import_bill_detail_qty` int(5) NOT NULL,
  PRIMARY KEY  (`bill_import_detail_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=856 ;

-- 
-- Dumping data for table `tb_bill_import_detail`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_bill_sale`
-- 

CREATE TABLE `tb_bill_sale` (
  `bill_sale_id` int(11) NOT NULL auto_increment,
  `bill_sale_created_date` datetime NOT NULL,
  `bill_sale_status` enum('wait','pay','cancel','credit') collate utf8_unicode_ci NOT NULL,
  `member_id` int(11) NOT NULL,
  `bill_sale_vat` enum('no','vat') collate utf8_unicode_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  `branch_id` int(11) default NULL,
  `bill_sale_pay_date` date default NULL,
  `bill_sale_drop_bill_date` date default NULL,
  PRIMARY KEY  (`bill_sale_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=50759 ;

-- 
-- Dumping data for table `tb_bill_sale`
-- 

INSERT INTO `tb_bill_sale` VALUES (50755, '2012-08-24 21:51:45', 'pay', 0, 'no', 5, 0, NULL, NULL);
INSERT INTO `tb_bill_sale` VALUES (50756, '2012-08-24 21:51:57', 'pay', 0, 'no', 5, 0, NULL, NULL);
INSERT INTO `tb_bill_sale` VALUES (50757, '2012-09-08 03:20:40', 'pay', 0, 'no', 5, 0, NULL, NULL);
INSERT INTO `tb_bill_sale` VALUES (50758, '2012-09-23 11:06:21', 'pay', 0, 'no', 5, 0, NULL, NULL);

-- --------------------------------------------------------

-- 
-- Table structure for table `tb_bill_sale_detail`
-- 

CREATE TABLE `tb_bill_sale_detail` (
  `bill_sale_detail_id` int(11) NOT NULL auto_increment,
  `bill_id` int(11) NOT NULL,
  `bill_sale_detail_barcode` varchar(50) collate utf8_unicode_ci NOT NULL,
  `bill_sale_detail_price` int(5) NOT NULL,
  `bill_sale_detail_price_vat` double(5,2) NOT NULL,
  `bill_sale_detail_qty` int(5) NOT NULL,
  PRIMARY KEY  (`bill_sale_detail_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=301064 ;

-- 
-- Dumping data for table `tb_bill_sale_detail`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_bill_sale_temp`
-- 

CREATE TABLE `tb_bill_sale_temp` (
  `product_total` int(11) NOT NULL,
  `product_name` varchar(200) collate utf8_unicode_ci NOT NULL,
  `product_price` int(5) NOT NULL,
  `bill_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- Dumping data for table `tb_bill_sale_temp`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_box`
-- 

CREATE TABLE `tb_box` (
  `box_id` int(11) NOT NULL auto_increment,
  `box_name` varchar(255) collate utf8_unicode_ci NOT NULL,
  `box_total_per_unit` int(5) NOT NULL,
  `box_detail` text collate utf8_unicode_ci NOT NULL,
  `box_created_date` datetime NOT NULL,
  PRIMARY KEY  (`box_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=7 ;

-- 
-- Dumping data for table `tb_box`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_branch`
-- 

CREATE TABLE `tb_branch` (
  `branch_id` int(11) NOT NULL auto_increment,
  `branch_name` varchar(255) default NULL,
  `branch_tel` varchar(255) default NULL,
  `branch_email` varchar(255) default NULL,
  `branch_address` text,
  `branch_created_date` datetime default NULL,
  PRIMARY KEY  (`branch_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

-- 
-- Dumping data for table `tb_branch`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_client`
-- 

CREATE TABLE `tb_client` (
  `id` int(11) NOT NULL auto_increment,
  `client_name` varchar(255) collate utf8_unicode_ci NOT NULL,
  `client_score` int(11) NOT NULL,
  `client_regis_date` date NOT NULL,
  `client_serial` varchar(20) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=26 ;

-- 
-- Dumping data for table `tb_client`
-- 

INSERT INTO `tb_client` VALUES (25, 'mysale', 3000, '2012-08-04', NULL);

-- --------------------------------------------------------

-- 
-- Table structure for table `tb_farmer`
-- 

CREATE TABLE `tb_farmer` (
  `farmer_id` int(11) NOT NULL auto_increment,
  `farmer_name` varchar(500) collate utf8_unicode_ci NOT NULL,
  `farmer_tel` varchar(50) collate utf8_unicode_ci NOT NULL,
  `farmer_address` text collate utf8_unicode_ci NOT NULL,
  `farmer_created_date` datetime NOT NULL,
  PRIMARY KEY  (`farmer_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=8 ;

-- 
-- Dumping data for table `tb_farmer`
-- 

INSERT INTO `tb_farmer` VALUES (7, 'pob', '0809260836', '', '2012-08-24 21:54:37');

-- --------------------------------------------------------

-- 
-- Table structure for table `tb_group_product`
-- 

CREATE TABLE `tb_group_product` (
  `group_product_id` int(5) NOT NULL auto_increment,
  `group_product_code` varchar(50) collate utf8_unicode_ci NOT NULL,
  `group_product_name` varchar(255) collate utf8_unicode_ci NOT NULL,
  `group_product_detail` text collate utf8_unicode_ci NOT NULL,
  `group_product_created_date` datetime NOT NULL,
  `group_product_last_update` datetime NOT NULL,
  PRIMARY KEY  (`group_product_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=104 ;

-- 
-- Dumping data for table `tb_group_product`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_group_product_box`
-- 

CREATE TABLE `tb_group_product_box` (
  `group_product_id` int(11) NOT NULL,
  `box_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- Dumping data for table `tb_group_product_box`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_import_product`
-- 

CREATE TABLE `tb_import_product` (
  `import_product_id` int(11) NOT NULL auto_increment,
  `product_id` int(11) NOT NULL,
  `import_product_total` int(5) NOT NULL,
  `import_product_price` int(4) NOT NULL,
  `import_product_created_date` datetime NOT NULL,
  `farmer_id` int(11) NOT NULL,
  `box_id` int(11) NOT NULL,
  PRIMARY KEY  (`import_product_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=8 ;

-- 
-- Dumping data for table `tb_import_product`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_member`
-- 

CREATE TABLE `tb_member` (
  `member_id` int(11) NOT NULL auto_increment,
  `member_code` varchar(50) collate utf8_unicode_ci NOT NULL,
  `member_name` varchar(255) collate utf8_unicode_ci NOT NULL,
  `member_tel` varchar(255) collate utf8_unicode_ci NOT NULL,
  `member_address` varchar(255) collate utf8_unicode_ci NOT NULL,
  `member_created_date` datetime NOT NULL,
  PRIMARY KEY  (`member_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=48 ;

-- 
-- Dumping data for table `tb_member`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_organization`
-- 

CREATE TABLE `tb_organization` (
  `org_id` int(11) NOT NULL auto_increment,
  `org_name` varchar(255) collate utf8_unicode_ci NOT NULL,
  `org_address_1` varchar(255) collate utf8_unicode_ci NOT NULL,
  `org_address_2` varchar(255) collate utf8_unicode_ci NOT NULL,
  `org_address_3` varchar(255) collate utf8_unicode_ci NOT NULL,
  `org_address_4` varchar(255) collate utf8_unicode_ci NOT NULL,
  `org_tel` varchar(255) collate utf8_unicode_ci NOT NULL,
  `org_fax` varchar(255) collate utf8_unicode_ci NOT NULL,
  `org_tax_code` varchar(100) collate utf8_unicode_ci NOT NULL,
  `org_current_version` varchar(10) collate utf8_unicode_ci default NULL,
  `org_name_eng` varchar(255) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`org_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

-- 
-- Dumping data for table `tb_organization`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_percen_sale`
-- 

CREATE TABLE `tb_percen_sale` (
  `percen_sale_id` int(11) NOT NULL auto_increment,
  `percen_sale_total` int(3) NOT NULL,
  `percen_sale_created_date` datetime NOT NULL,
  PRIMARY KEY  (`percen_sale_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

-- 
-- Dumping data for table `tb_percen_sale`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_product`
-- 

CREATE TABLE `tb_product` (
  `product_id` int(11) NOT NULL auto_increment,
  `group_product_id` int(5) NOT NULL,
  `product_code` varchar(50) collate utf8_unicode_ci NOT NULL,
  `product_name` varchar(255) collate utf8_unicode_ci NOT NULL,
  `product_detail` text collate utf8_unicode_ci NOT NULL,
  `product_created_date` datetime default NULL,
  `product_last_update` datetime default NULL,
  `product_quantity` int(6) default '0',
  `product_pack_barcode` varchar(255) collate utf8_unicode_ci NOT NULL,
  `product_total_per_pack` int(5) NOT NULL,
  `product_expire` enum('expire','fresh') collate utf8_unicode_ci NOT NULL,
  `product_return` enum('in','out') collate utf8_unicode_ci NOT NULL,
  `product_expire_date` date default NULL,
  `product_sale_condition` enum('sale','prompt') collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`product_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=833 ;

-- 
-- Dumping data for table `tb_product`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_product_box`
-- 

CREATE TABLE `tb_product_box` (
  `product_id` int(11) NOT NULL,
  `box_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- Dumping data for table `tb_product_box`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_product_price`
-- 

CREATE TABLE `tb_product_price` (
  `product_price_id` int(11) NOT NULL auto_increment,
  `product_id` int(11) NOT NULL,
  `box_id` int(11) NOT NULL,
  `product_price_price` int(8) NOT NULL,
  `product_price_barcode` varchar(50) collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`product_price_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=18 ;

-- 
-- Dumping data for table `tb_product_price`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_product_price_per_unit`
-- 

CREATE TABLE `tb_product_price_per_unit` (
  `product_id` int(11) NOT NULL,
  `product_price_per_unit_barcode` varchar(255) collate utf8_unicode_ci default NULL,
  `product_price_per_unit_price` int(11) default NULL,
  `product_price_per_unit_old_price` float NOT NULL,
  `product_price_per_unit_sent` int(11) default NULL,
  `product_price_per_unit_idint` int(11) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- Dumping data for table `tb_product_price_per_unit`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_repair`
-- 

CREATE TABLE `tb_repair` (
  `repair_id` int(11) NOT NULL auto_increment,
  `user_id` int(11) default NULL,
  `brance_id` int(11) default NULL,
  `product_code` varchar(255) default NULL,
  `repair_date` date default NULL,
  `repair_problem` varchar(1000) default NULL,
  `repair_price` int(11) default NULL,
  `repair_type` enum('internal','center','external') default NULL,
  `repair_original` varchar(1000) default NULL,
  `repair_detail` varchar(1000) default NULL,
  `repair_created_date` datetime default NULL,
  `repair_status` enum('wait','do','complete') default NULL,
  PRIMARY KEY  (`repair_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- 
-- Dumping data for table `tb_repair`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_sale_per_day`
-- 

CREATE TABLE `tb_sale_per_day` (
  `sale_year` int(4) NOT NULL,
  `sale_month` int(2) NOT NULL,
  `sale_day` int(2) NOT NULL,
  `branch_id` int(11) NOT NULL,
  `sale_total_price` int(11) NOT NULL default '0',
  PRIMARY KEY  (`sale_year`,`sale_month`,`sale_day`,`branch_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- Dumping data for table `tb_sale_per_day`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_stock`
-- 

CREATE TABLE `tb_stock` (
  `id` int(11) NOT NULL auto_increment,
  `product_code` varchar(255) collate utf8_unicode_ci NOT NULL,
  `stock_qty` int(11) NOT NULL,
  `stock_created_date` datetime NOT NULL,
  `stock_pack_qty` int(11) NOT NULL,
  `branch_id` int(11) default NULL,
  `stock_qty_from_manual` int(11) default NULL,
  `stock_qty_real` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=26 ;

-- 
-- Dumping data for table `tb_stock`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tb_user`
-- 

CREATE TABLE `tb_user` (
  `user_id` int(11) NOT NULL auto_increment,
  `user_name` varchar(255) collate utf8_unicode_ci NOT NULL,
  `user_tel` varchar(50) collate utf8_unicode_ci NOT NULL,
  `user_level` enum('cacheer','admin') collate utf8_unicode_ci NOT NULL,
  `user_username` varchar(255) collate utf8_unicode_ci NOT NULL,
  `user_password` varchar(255) collate utf8_unicode_ci NOT NULL,
  `user_created_date` datetime NOT NULL,
  PRIMARY KEY  (`user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=9 ;

-- 
-- Dumping data for table `tb_user`
-- 

INSERT INTO `tb_user` VALUES (5, 'kraipob', '0803263215', 'admin', 'pob', '1234', '2011-12-22 15:30:32');
