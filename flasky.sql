# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.25)
# Database: flasky
# Generation Time: 2019-05-09 07:13:10 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table comments
# ------------------------------------------------------------

DROP TABLE IF EXISTS `comments`;

CREATE TABLE `comments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `created_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime(6) DEFAULT NULL COMMENT '最后修改时间',
  `body` longtext COMMENT 'markdown 评论内容',
  `body_html` longtext COMMENT 'html 格式评论内容',
  `disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁用这条评论, 不显示在页面上',
  `user_id` bigint(20) NOT NULL COMMENT '关联的用户 id',
  `post_id` bigint(20) NOT NULL COMMENT '关联的 post id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户评论表';



# Dump of table follows
# ------------------------------------------------------------

DROP TABLE IF EXISTS `follows`;

CREATE TABLE `follows` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `created_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime(6) DEFAULT NULL COMMENT '最后修改时间',
  `followed_id` bigint(20) DEFAULT NULL COMMENT '被关注者 id',
  `follower_id` bigint(20) DEFAULT NULL COMMENT '关注者 id',
  PRIMARY KEY (`id`),
  KEY `IDXhwoeqs3s498a03p3eqt313exv` (`follower_id`),
  KEY `IDX17vd7qgf0kx3dmnu55qhm84kd` (`followed_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户 follow 关联表';



# Dump of table posts
# ------------------------------------------------------------

DROP TABLE IF EXISTS `posts`;

CREATE TABLE `posts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `created_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime(6) DEFAULT NULL COMMENT '最后更新时间',
  `body` longtext NOT NULL COMMENT 'post 内容, markdown 格式',
  `body_html` longtext COMMENT 'post 内容, html 格式',
  `user_id` bigint(20) NOT NULL COMMENT '关联的用户 id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='post 表';



# Dump of table roles
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roles`;

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `created_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime(6) DEFAULT NULL COMMENT '最后修改时间',
  `default_role` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为默认角色',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '角色名称',
  `permissions` int(11) DEFAULT NULL COMMENT '角色权限',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户角色权限表, 权限控制用一个 32 bits 整形表示, 每个 bit 表示一个权限.  初始默认的一些权限, 未使用的 bit 用可于将来扩展. 关注权限: FOLLOW = 0x01, 评论权限: COMMENT = 0x02, 可写 post 权限: WRITE = 0x04, 可更改他人评论权限: MODERATE = 0x08, 管理员权限, 拥有所有权限: ADMIN = 0xff. 初始化三种角色: 普通用户 user(0x07), 设置为默认用户, 日常管理者: moderator(0x0e), 最高权限管理员: admin(0xff)';

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;

INSERT INTO `roles` (`id`, `created_date`, `last_modified_date`, `default_role`, `name`, `permissions`)
VALUES
	(1,'2019-01-01 00:00:00.000000','2019-01-01 00:00:00.000000',1,'user',7),
	(2,'2019-01-01 00:00:00.000000','2019-01-01 00:00:00.000000',0,'moderator',15),
	(3,'2019-01-01 00:00:00.000000','2019-01-01 00:00:00.000000',0,'admin',255);

/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table users
# ------------------------------------------------------------

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `created_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime(6) DEFAULT NULL COMMENT '最后修改时间',
  `about_me` longtext COMMENT '关于我',
  `avatar_hash` varchar(255) DEFAULT NULL COMMENT '头像 hash',
  `comment_count` bigint(20) unsigned DEFAULT NULL COMMENT '评论总数',
  `confirmed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '邮箱是否已确认',
  `email` varchar(64) NOT NULL DEFAULT '' COMMENT '邮箱',
  `follower_count` bigint(20) DEFAULT NULL COMMENT '多少人关注自己的数量',
  `following_count` bigint(20) DEFAULT NULL COMMENT '关注了多少人',
  `last_seen` datetime(6) DEFAULT NULL COMMENT '最后登录时间',
  `location` varchar(255) DEFAULT NULL COMMENT '位置',
  `name` varchar(30) DEFAULT NULL COMMENT '别名',
  `password_hash` varchar(255) NOT NULL DEFAULT '' COMMENT '密码 hash',
  `post_count` bigint(20) DEFAULT NULL COMMENT '发表 post 数量',
  `username` varchar(64) NOT NULL DEFAULT '' COMMENT '用户名',
  `role_id` bigint(20) NOT NULL COMMENT '角色 id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`),
  KEY `IDX6dotkott2kjsp8vw4d0m25fb7` (`email`),
  KEY `IDXr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表. 几个数量列作为冗余, 因为这几个统计常用到, 避免总是要去 count(*) 其它表';




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
