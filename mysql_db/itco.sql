-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Feb 26, 2013 at 12:18 AM
-- Server version: 5.0.51
-- PHP Version: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Database: `itco`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `tbserver`
-- 

CREATE TABLE `tbserver` (
  `ID` int(11) NOT NULL auto_increment,
  `ServerName` varchar(50) NOT NULL,
  `IP` varchar(12) NOT NULL,
  `SM` varchar(12) NOT NULL,
  `DG` varchar(12) NOT NULL,
  `DS` varchar(12) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=102 ;

-- 
-- Dumping data for table `tbserver`
-- 

INSERT INTO `tbserver` VALUES (98, 'AA', '192.168.1.7', '192.168.202', '192.168.1.25', '192.168.202');
INSERT INTO `tbserver` VALUES (97, 'BB', '192.168.1.7', '192.168.202', '192.168.1.25', '192.168.202');
INSERT INTO `tbserver` VALUES (96, 'CC', '192.168.1.7', '192.168.202', '192.168.1.25', '192.168.202');
INSERT INTO `tbserver` VALUES (94, 'SS', '192.168.1.7', '192.168.202', '192.168.1.25', '192.168.202');
INSERT INTO `tbserver` VALUES (95, 'DD', '192.168.1.7', '192.168.202', '192.168.1.25', '192.168.202');
INSERT INTO `tbserver` VALUES (86, 'NN', '192.168.1.7', '255.255.255.', '192.168.1.25', '192.168.202');
INSERT INTO `tbserver` VALUES (87, 'JJ', '192.168.1.7', '192.168.202', '192.168.1.25', '192.168.202');
INSERT INTO `tbserver` VALUES (88, 'HH', '192.168.1.7', '192.168.202', '192.168.1.25', '192.168.202');
INSERT INTO `tbserver` VALUES (89, 'VV', '192.168.1.7', '192.168.202', '192.168.1.25', '192.168.202');
INSERT INTO `tbserver` VALUES (90, 'TT', '192.168.1.7', '192.168.202', '192.168.1.25', '192.168.202');
