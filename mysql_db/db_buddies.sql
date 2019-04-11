-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Feb 26, 2013 at 12:15 AM
-- Server version: 5.0.51
-- PHP Version: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Database: `db_buddies`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `tbl_buddies`
-- 

CREATE TABLE `tbl_buddies` (
  `sender` varchar(45) NOT NULL,
  `receiver` varchar(45) NOT NULL,
  `status` int(10) unsigned NOT NULL,
  `message` varchar(45) default NULL,
  PRIMARY KEY  USING BTREE (`sender`,`receiver`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `tbl_buddies`
-- 

INSERT INTO `tbl_buddies` VALUES ('prash', 'admin', 2, 'Nothing');
INSERT INTO `tbl_buddies` VALUES ('prash', 'shru6', 2, 'Nothing');
INSERT INTO `tbl_buddies` VALUES ('shru6', 'admin', 2, 'Nothing');

-- --------------------------------------------------------

-- 
-- Table structure for table `tbl_offlinemsg`
-- 

CREATE TABLE `tbl_offlinemsg` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `msg_from` varchar(45) NOT NULL,
  `msg_to` varchar(45) NOT NULL,
  `msg` varchar(150) NOT NULL,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- 
-- Dumping data for table `tbl_offlinemsg`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `tbl_register`
-- 

CREATE TABLE `tbl_register` (
  `name` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `repeat` varchar(45) NOT NULL,
  `emailid` varchar(45) NOT NULL,
  `online` int(10) unsigned default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `tbl_register`
-- 

INSERT INTO `tbl_register` VALUES ('administrator', 'pob', '1234', 'admin', 'admin@admin.com', 0);
INSERT INTO `tbl_register` VALUES ('lucky', 'lucky6', 'shruti', 'shruti', 'lucky@yahoo.com', 0);
INSERT INTO `tbl_register` VALUES ('prashant', 'prash', 'prashant', 'prashant', 'yohoprashant@yahoo.com', 0);
INSERT INTO `tbl_register` VALUES ('sfasf', 'safasf', 'aa', 'aa', '', 0);
INSERT INTO `tbl_register` VALUES ('shruti', 'shru6', 'shruti', 'shruti', '', 1);
INSERT INTO `tbl_register` VALUES ('cazcz', 'sxcsa', 'ssds', 'dscx', 'sds', 0);
