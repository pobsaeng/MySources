DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock` (
  `STOCK_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `STOCK_CODE` varchar(10) NOT NULL,
  `STOCK_NAME` varchar(20) NOT NULL,
  PRIMARY KEY (`STOCK_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `stock_detail`;
CREATE TABLE `stock_detail` (
  `STOCK_DETAIL_ID` int(11) NOT NULL AUTO_INCREMENT,
  `STOCK_ID` int(10) unsigned NOT NULL,
  `COMP_NAME` varchar(100) DEFAULT NULL,
  `COMP_DESC` varchar(255) DEFAULT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `LISTED_DATE` date DEFAULT NULL,
  PRIMARY KEY (`STOCK_DETAIL_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;