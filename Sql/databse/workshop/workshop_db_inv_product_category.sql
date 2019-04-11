CREATE DATABASE  IF NOT EXISTS `workshop_db` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `workshop_db`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: workshop_db
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
-- Table structure for table `inv_product_category`
--

DROP TABLE IF EXISTS `inv_product_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_product_category` (
  `id` int(10) NOT NULL auto_increment,
  `product_category_name` varchar(50) default NULL,
  `product_category_desc` varchar(50) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `product_category_name` (`product_category_name`)
) ENGINE=MyISAM AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_product_category`
--

LOCK TABLES `inv_product_category` WRITE;
/*!40000 ALTER TABLE `inv_product_category` DISABLE KEYS */;
INSERT INTO `inv_product_category` VALUES (1,'มาม่า','มาม่า 10 กล่อง'),(2,'ปลากระป๋อง','category red'),(3,'ข้าวสาร','ข้าวสาร 1'),(4,'ข้าวเปือก','ข้าวเปือก 20 กิโลกรัม'),(5,'น้ำผลไม้','น้ำผลไม้ 1'),(6,'นม','นม 1'),(7,'ยาสีฟัน','ยาสีฟัน 5 กล่อง'),(8,'แปลงสีฟัน','แปลงสีฟัน 2 แผง'),(9,'ปลาแห้ง','10 มัด'),(10,'ปลาหมึก','20 ตัว'),(11,'ขนมขบเคี้ยว','10 ห่อใหญ่'),(12,'นมผง','5 แพ็ค'),(13,'ถุงร้อน','2 แพ็ค'),(14,'ถุงเย็น','2 แพ็ค'),(15,'หนังสือการ์ตูน','10 เล่ม'),(16,'หนังสือชีวจี','5 เล่ม'),(17,'เสื้อยืดแขนยาว','5 ตัว'),(18,'เสื้อยืดแขนสั้น','5 ตัว'),(19,'กางเกงขาสั้น ','10 ตัว'),(20,'กางเกงขายาว','5 ตัว'),(21,'กางเก่งในชาย','10 ตัว'),(22,'กางเกงใจหญิง','10 ตัว'),(23,'เต้าหู้','2 ถัง');
/*!40000 ALTER TABLE `inv_product_category` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-09-14 16:55:50
