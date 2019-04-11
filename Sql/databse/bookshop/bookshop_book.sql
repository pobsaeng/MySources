CREATE DATABASE  IF NOT EXISTS `bookshop` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `bookshop`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: bookshop
-- ------------------------------------------------------
-- Server version	5.0.51b-community-nt-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Not dumping tablespaces as no INFORMATION_SCHEMA.FILES table on this server
--

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `book` (
  `title` varchar(255) NOT NULL,
  `author` varchar(20) default NULL,
  `publisher` varchar(20) default NULL,
  `publishdate` date default NULL,
  `price` float default NULL,
  PRIMARY KEY  (`title`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES ('Enterprise JavaBeans','Tim Dario','O\'Reilly','2001-10-15',45),('Java in a Nutshell','Asli Bilgin','O\'Reilly','2003-03-01',40),('XML in a Nutshell','Jason Price','O\'Reilly','2002-12-15',40),('Java Enterprise','Tim Dario','O\'Reilly','2002-12-01',35),('Php Functions Reference','John Merrall','Manning','2002-07-25',50),('Linux Firewalls','Robin Aldale','Manning','2001-10-24',50),('Inside Photoshop 7','Robin Aldale','Manning','2002-07-22',50),('Network Security','Judy Noval','Manning','2003-02-27',46),('Mastering Delphi 6','Marco Cantu','Sybex','2002-11-25',60),('Mastering VB.NET','Asli Bilgin','Sybex','2002-01-11',50),('Mastering Visual C#.NET','Jason Price','Sybex','2003-02-20',40),('Mastering Java Scripts','Cate McCoy','Sybex','2003-03-14',50),('Beginning Java 2 SDK','Tim Dario','Wrox','2003-03-01',50),('Professional PHP5','Chris Ullman','Wrox','2003-03-15',50),('Professional Apache 2.0','Judy Noval','Wrox','2002-12-01',50),('Beginning VB.NET','Kim Blair','Wrox','2003-01-01',40);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-09-14 16:53:36
