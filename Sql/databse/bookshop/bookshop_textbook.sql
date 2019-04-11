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
-- Table structure for table `textbook`
--

DROP TABLE IF EXISTS `textbook`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `textbook` (
  `bid` int(10) unsigned NOT NULL auto_increment,
  `pid` int(10) unsigned NOT NULL,
  `title` varchar(255) NOT NULL,
  `aid` varchar(20) default NULL,
  `publishdate` date NOT NULL,
  `price` mediumint(9) NOT NULL,
  PRIMARY KEY  (`bid`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `textbook`
--

LOCK TABLES `textbook` WRITE;
/*!40000 ALTER TABLE `textbook` DISABLE KEYS */;
INSERT INTO `textbook` VALUES (1,3,'Mastering VB.NET','1','2002-01-11',50),(2,3,'Mastering Visual C#.NET','2','2003-02-20',40),(3,3,'Mastering Java Scripts','3','2003-03-14',50),(4,5,'Professional PHP5','1','2003-03-15',50),(5,1,'Linux Firewalls','4','2001-10-24',49),(6,1,'Network Security','5','2003-02-27',59),(7,2,'JSP Servlet ',NULL,'2014-01-01',65),(8,2,'Java Swing','5','2014-02-05',75),(9,4,'JSP Tutorials','3','2014-02-10',70),(10,4,'Hibernate Tutorials','1','2014-01-12',65),(11,5,'Spring MVC','3','2014-03-08',80),(12,1,'Struts v.2','2','2014-03-15',100),(13,5,'JSF v.2','1','2014-01-20',95),(14,3,'JSF v.1','4','2014-01-07',89),(15,2,'Java Structure','2','2014-02-13',77),(16,1,'J2EE Tutotials','5','2014-02-24',0),(17,1,'Servlet Tutorials',NULL,'2014-04-04',99),(18,4,'Struts 2 + Spring + Hibernate',NULL,'2014-03-19',67),(19,5,'MySql Tip','3','2014-01-11',0),(20,3,'Sql Server 2012','2','2014-01-22',97);
/*!40000 ALTER TABLE `textbook` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-09-14 16:53:39
