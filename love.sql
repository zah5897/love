/*
Navicat MySQL Data Transfer

Source Server         : haoqi
Source Server Version : 50520
Source Host           : localhost:3306
Source Database       : love

Target Server Type    : MYSQL
Target Server Version : 50520
File Encoding         : 65001

Date: 2016-06-14 08:40:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_relationship
-- ----------------------------
DROP TABLE IF EXISTS `t_relationship`;
CREATE TABLE `t_relationship` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `with_user_id` int(11) DEFAULT NULL,
  `relationship` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1264 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_report_record
-- ----------------------------
DROP TABLE IF EXISTS `t_report_record`;
CREATE TABLE `t_report_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `report_to_user_id` int(11) DEFAULT NULL,
  `report_tag_id` varchar(10) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sys_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_tag`;
CREATE TABLE `t_sys_tag` (
  `sys_id` int(11) NOT NULL AUTO_INCREMENT,
  `id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`sys_id`)
) ENGINE=InnoDB AUTO_INCREMENT=180 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `user_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(20) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `nick_name` varchar(32) DEFAULT NULL,
  `age` varchar(4) DEFAULT NULL,
  `birthday` timestamp NULL DEFAULT NULL,
  `sex` varchar(2) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `signature` varchar(255) DEFAULT NULL,
  `_ua` varchar(255) DEFAULT NULL,
  `lat` varchar(255) DEFAULT NULL,
  `lng` varchar(255) DEFAULT NULL,
  `token` varchar(36) DEFAULT NULL,
  `job_ids` varchar(255) DEFAULT NULL,
  `weight` varchar(10) DEFAULT NULL,
  `height` varchar(10) DEFAULT NULL,
  `my_tag_ids` varchar(255) DEFAULT NULL,
  `interest_ids` varchar(255) DEFAULT NULL,
  `animal_ids` varchar(255) DEFAULT NULL,
  `music_ids` varchar(255) DEFAULT NULL,
  `weekday_todo_ids` varchar(255) DEFAULT NULL,
  `footstep_ids` varchar(255) DEFAULT NULL,
  `want_to_where` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_images
-- ----------------------------
DROP TABLE IF EXISTS `t_user_images`;
CREATE TABLE `t_user_images` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_IMAGES_USER_ID` (`user_id`),
  CONSTRAINT `FK_IMAGES_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
