/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50527
Source Host           : localhost:3306
Source Database       : autocode

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2016-11-01 21:02:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for demo
-- ----------------------------
DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `tInt` int(255) DEFAULT NULL,
  `tDouble` double DEFAULT NULL,
  `tFloat` float DEFAULT NULL,
  `tVarchar` varchar(255) DEFAULT NULL,
  `tBigint` bigint(20) DEFAULT NULL,
  `tText` text,
  `tChar` char(255) DEFAULT NULL,
  `tDate` date DEFAULT NULL,
  `tTime` time DEFAULT NULL,
  `tYear` year(4) DEFAULT NULL,
  `tDatetime` datetime DEFAULT NULL,
  `tTimestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `tLongText` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of demo
-- ----------------------------
INSERT INTO `demo` VALUES ('1', 'demo', '1', '1.21', '1.321', 'varchar', '12321321321321132', 'text', 'c', '2016-11-01', '19:59:52', '2016', '2016-11-01 20:00:00', '2016-11-01 20:00:04', 'longText');
INSERT INTO `demo` VALUES ('3', '李四', '123', '321.1231', '1231.21', 'varcharvarchar', '1020123213213213213', 'Text', 'd', '2016-11-01', '20:59:02', '2016', '2016-11-01 20:59:02', '2016-11-01 20:59:02', 'LongText');
