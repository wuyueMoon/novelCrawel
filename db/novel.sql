/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50548
Source Host           : localhost:3306
Source Database       : novel

Target Server Type    : MYSQL
Target Server Version : 50548
File Encoding         : 65001

Date: 2019-05-04 21:55:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for chapter
-- ----------------------------
DROP TABLE IF EXISTS `chapter`;
CREATE TABLE `chapter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nId` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `content` text,
  `link` varchar(100) NOT NULL,
  `isFull` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否有章节内容',
  PRIMARY KEY (`id`,`nId`),
  KEY `cid` (`id`) USING BTREE,
  KEY `nid` (`nId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for novel
-- ----------------------------
DROP TABLE IF EXISTS `novel`;
CREATE TABLE `novel` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '小说id',
  `cid` int(10) NOT NULL COMMENT '类别id',
  `name` varchar(20) NOT NULL COMMENT '小说名字',
  `author` varchar(20) NOT NULL COMMENT '作者',
  `brief` text COMMENT '简介',
  `status` varchar(10) NOT NULL COMMENT '状态（连载/完结）',
  `uptime` varchar(30) NOT NULL COMMENT '更新时间',
  `link` varchar(100) NOT NULL COMMENT '小说链接',
  `size` int(10) unsigned zerofill NOT NULL COMMENT '章节总数',
  PRIMARY KEY (`id`),
  KEY `cid` (`cid`) USING BTREE,
  KEY `nid` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
