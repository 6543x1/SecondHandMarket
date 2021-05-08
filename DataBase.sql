-- MySQL dump 10.13  Distrib 5.7.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: secondhandmarket
-- ------------------------------------------------------
-- Server version	5.7.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `admin_operation`
--

DROP TABLE IF EXISTS `admin_operation`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_operation`
(
    `operator`      int(11)      NOT NULL,
    `operation`     varchar(100) NOT NULL,
    `targetUser`    int(11)      DEFAULT NULL,
    `targetData`    int(11)      DEFAULT NULL,
    `operationTime` datetime     NOT NULL,
    `reason`        varchar(100) DEFAULT NULL,
    `id`            int(11)      NOT NULL AUTO_INCREMENT COMMENT '充当主键用的，没啥别的作用',
    PRIMARY KEY (`id`),
    KEY `admin_operation_FK` (`operator`),
    CONSTRAINT `admin_operation_FK` FOREIGN KEY (`operator`) REFERENCES `user` (`uid`) ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 17
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_operation`
--

LOCK TABLES `admin_operation` WRITE;
/*!40000 ALTER TABLE `admin_operation`
    DISABLE KEYS */;
INSERT INTO `admin_operation`
VALUES (1, '通过合格商品', 2, 18, '2021-05-06 14:31:22', 'PASS', 1),
       (1, '通过合格商品', 8, 19, '2021-05-06 16:16:45', 'PASS', 2),
       (1, '通过合格商品', 8, 20, '2021-05-06 16:16:58', 'PASS', 3),
       (1, '删除不合格商品', 8, 25, '2021-05-06 16:28:23', NULL, 4),
       (1, '通过合格商品', 8, 26, '2021-05-06 16:28:36', 'PASS', 5),
       (1, '通过合格商品', 8, 26, '2021-05-06 16:28:41', 'PASS', 6),
       (1, '通过合格商品', 8, 27, '2021-05-06 21:36:18', 'PASS', 7),
       (1, '删除不合格商品', 8, 27, '2021-05-06 21:36:33', NULL, 8),
       (1, '通过合格商品', 8, 28, '2021-05-06 23:24:50', 'PASS', 9),
       (1, '通过合格商品', 8, 29, '2021-05-06 23:57:30', 'PASS', 10),
       (1, '通过合格商品', 8, 29, '2021-05-06 23:57:35', 'PASS', 11),
       (1, '通过合格商品', 8, 30, '2021-05-07 00:19:55', 'PASS', 12),
       (1, '通过合格商品', 8, 31, '2021-05-07 00:27:42', 'PASS', 13),
       (1, '通过合格商品', 8, 33, '2021-05-07 19:51:31', 'PASS', 14),
       (1, '删除不合格商品', 8, 32, '2021-05-07 19:51:43', NULL, 15),
       (1, '删除不合格商品', 8, 34, '2021-05-07 21:45:37', NULL, 16);
/*!40000 ALTER TABLE `admin_operation`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contactinfo`
--

DROP TABLE IF EXISTS `contactinfo`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contactinfo`
(
    `cid`         int(11)     NOT NULL AUTO_INCREMENT,
    `name`        varchar(20) NOT NULL,
    `phoneNumber` varchar(11) NOT NULL,
    `location`    varchar(100) DEFAULT NULL,
    `uid`         int(11)     NOT NULL,
    PRIMARY KEY (`cid`),
    KEY `contactInfo_FK` (`uid`),
    CONSTRAINT `contactInfo_FK` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contactinfo`
--

LOCK TABLES `contactinfo` WRITE;
/*!40000 ALTER TABLE `contactinfo`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `contactinfo`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods`
--

DROP TABLE IF EXISTS `goods`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goods`
(
    `gid`         int(11)      NOT NULL AUTO_INCREMENT,
    `description` varchar(500) NOT NULL COMMENT '商品描述，默认500字',
    `label`       varchar(100) NOT NULL COMMENT '商品标签',
    `brand`       varchar(15) DEFAULT NULL,
    `quality`     varchar(20)  NOT NULL COMMENT '应当为枚举值，应前端要求改20字',
    `uid`         int(11)      NOT NULL,
    `status`      int(11)      NOT NULL,
    `uploadTime`  datetime     NOT NULL,
    `price`       double       NOT NULL,
    `imgNum`      int(11)      NOT NULL,
    `contact`     varchar(100) NOT NULL,
    PRIMARY KEY (`gid`),
    KEY `goods_FK` (`uid`),
    FULLTEXT KEY `goods_description_IDX` (`description`) /*!50100 WITH PARSER `ngram` */,
    FULLTEXT KEY `goods_description_IDX_2` (`description`) /*!50100 WITH PARSER `ngram` */,
    CONSTRAINT `goods_FK` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 37
  DEFAULT CHARSET = utf8 COMMENT ='商品的信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods`
--

LOCK TABLES `goods` WRITE;
/*!40000 ALTER TABLE `goods`
    DISABLE KEYS */;
INSERT INTO `goods`
VALUES (1, '这是测试商品:1', 'test/theGoodsTest', NULL, '伊拉克成色', 1, 1, '2021-03-27 16:40:01', 999999.99, 2, 'WU'),
       (2, '这是测试商品:2', 'test/theGoodsTest', NULL, '九五新', 1, 2, '2021-03-27 16:40:01', 999999.99, 3, 'wu'),
       (3, '这是测试商品:3', 'test/theGoodsTest', NULL, '全新', 1, 1, '2021-03-27 16:40:01', 999999.99, 1, 'wu'),
       (4, '这是测试商品:4（活页本）', '活页本', NULL, '伊拉克战损版本', 1, 1, '2021-03-27 16:40:01', 999999.99, 1, 'wu'),
       (5, '这是测试商品:5（水龙头）', '水龙头', NULL, '九新', 1, 1, '2021-03-27 16:40:01', 999999.99, 1, 'wu'),
       (6, '这是测试商品:6（显示器）', '显示器', NULL, '全新', 1, 1, '2021-03-27 16:40:01', 999999.99, 1, 'wu'),
       (7, '这是测试商品:7（K30S至尊纪念版 小米红米手机）', '手机', NULL, '伊拉克成色', 1, 1, '2021-03-27 16:40:01', 999999.99, 1, 'wu'),
       (8, '这是测试商品:8（联想 拯救者 R7000P 2020，锐龙4800H,RTX2060)', '笔记本电脑', NULL, '伊拉克成色', 1, 1, '2021-03-27 16:40:01',
        999999.99, 1, 'wu'),
       (9, '这是测试商品:9（51单片机C语言教程）', '书本', NULL, '九五新', 1, 1, '2021-03-27 16:40:01', 999999.99, 1, 'wu'),
       (10, '这是测试商品:10（福岛核废水）', '其他', NULL, '九新', 1, 1, '2021-03-27 16:40:01', 999999.99, 1, 'wu'),
       (11, '这是测试商品:11', 'test/theGoodsTest', NULL, '伊拉克成色', 1, 1, '2021-03-27 16:40:01', 999999.99, 1, 'wu'),
       (12, '这是测试商品:12', 'test/theGoodsTest', NULL, '全新', 1, 1, '2021-03-27 16:40:01', 999999.99, 1, 'wu'),
       (13, '这是测试商品:13', 'test/theGoodsTest', NULL, '九新', 1, 1, '2021-03-27 16:40:01', 999999.99, 1, 'wu'),
       (14, '这是测试商品:14', 'test/theGoodsTest', NULL, '伊拉克战损版本', 1, 1, '2021-03-27 16:40:01', 999999.99, 1, 'wu'),
       (15, '这是测试商品:15', 'test/theGoodsTest', NULL, '全新', 1, 1, '2021-03-27 16:40:01', 999999.99, 1, 'wu'),
       (16, '这是new出来的商品，请勿购买', 'test,Test', 'Jessie', '伊拉克版本', 1, 1, '2021-04-14 20:03:27', 99999.8, 1, 'wu'),
       (17, 'user1的测试商品，请勿购买', 'test,Test', 'user1', '全新', 2, 1, '2021-05-02 19:49:46', 99999, 2, '杰哥家里'),
       (18, 'RX5800XT AMD显卡 游戏显卡', '显卡', 'AMD', '全新', 2, 1, '2021-05-04 11:41:52', 99999, 1, 'FZU某小树林'),
       (19, 'It is a good for test', 'Test1', '', 'Very good', 8, 1, '2021-05-04 11:51:52', 123, 3, '123456789'),
       (20, 'It is a good for Test', 'Test2', '', 'Very good', 8, 1, '2021-05-04 16:33:06', 1234, 2, '1111111111'),
       (21, 'It’s my brain', 'Brain', '', 'not well', 8, 1, '2021-05-06 14:16:25', 1.22, 1, '123123123'),
       (22, 'It is a test', 'Test4', '', 'Very well', 8, 1, '2021-05-06 16:03:16', 2222, 1, '123123123'),
       (23, 'It is a test', 'Test5', '', 'Bad', 8, 1, '2021-05-06 16:03:48', 333, 1, '9999999'),
       (24, 'It is a test', 'Test6', '', 'Bad', 8, -1, '2021-05-06 16:07:42', 987, 2, '123123123'),
       (25, 'It is a test', 'Test7', '', 'Bad', 8, -1, '2021-05-06 16:27:18', 123, 1, '123123123'),
       (26, 'It is a test', 'Test8', '', 'Good', 8, -1, '2021-05-06 16:27:51', 9876, 1, '123123123'),
       (27, 'It is a test', 'Test9', '', 'Good', 8, -1, '2021-05-06 21:34:15', -1234, 3, '1123123211'),
       (28, 'It is a test', 'Test9', '', 'Bad', 8, 1, '2021-05-06 21:54:55', 1.2233, 4, '12222122'),
       (29, 'It is a test', 'Test10', '', 'Bad', 8, 1, '2021-05-06 23:27:59', 1234, 1, '123123'),
       (30, 'It is a test', 'Test', '', 'Good', 8, 1, '2021-05-07 00:19:25', 1234, 1, '123123123'),
       (31, 'It is a test', 'Test', '', 'Good', 8, 1, '2021-05-07 00:27:12', 123, 1, '123123123'),
       (32, 'It is a book', '书籍', '', 'Good', 8, -1, '2021-05-07 15:24:14', 1111, 1, '123123123'),
       (33, 'It is a book', '书籍', '', 'Not bad', 8, 1, '2021-05-07 19:51:00', 122, 1, '123123123'),
       (34, 'It is a good for test', '鞋包', '', 'Good', 8, -1, '2021-05-07 21:44:39', 123, 1, '123123123'),
       (35, 'It is a good for test', '电脑配件', '', 'New', 1, 0, '2021-05-08 23:11:42', 3202, 1, '123123123'),
       (36, 'It is a good for test', '生活百货', '', 'Normal', 1, 0, '2021-05-08 23:12:16', 123, 1, '123123123');
/*!40000 ALTER TABLE `goods`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods_comment`
--

DROP TABLE IF EXISTS `goods_comment`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goods_comment`
(
    `gid`               int(11)      NOT NULL,
    `reviewer`          int(11)      NOT NULL,
    `content`           varchar(100) NOT NULL,
    `replyTo`           int(11)      NOT NULL COMMENT '回复给谁',
    `reviewer_nickName` varchar(15) DEFAULT NULL,
    `replyTo_nickName`  varchar(15) DEFAULT NULL,
    `visited`           int(11)      NOT NULL COMMENT '0仅买家可见，1为所有人可见',
    KEY `goods_comment_FK` (`gid`),
    CONSTRAINT `goods_comment_FK` FOREIGN KEY (`gid`) REFERENCES `goods` (`gid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_comment`
--

LOCK TABLES `goods_comment` WRITE;
/*!40000 ALTER TABLE `goods_comment`
    DISABLE KEYS */;
INSERT INTO `goods_comment`
VALUES (4, 1, '杰哥你房里有好多AK哦', 1, 'Jessie', 'Jessie', 0);
/*!40000 ALTER TABLE `goods_comment`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods_img`
--

DROP TABLE IF EXISTS `goods_img`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goods_img`
(
    `imageID` int(11)      NOT NULL AUTO_INCREMENT,
    `name`    varchar(100) NOT NULL,
    `gid`     int(11)      NOT NULL,
    `uid`     int(11)      NOT NULL,
    `path`    varchar(100) NOT NULL,
    PRIMARY KEY (`imageID`),
    KEY `goodsimg_FK` (`uid`),
    KEY `goodsimg_FK_1` (`gid`),
    CONSTRAINT `goodsimg_FK` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `goodsimg_FK_1` FOREIGN KEY (`gid`) REFERENCES `goods` (`gid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 57
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_img`
--

LOCK TABLES `goods_img` WRITE;
/*!40000 ALTER TABLE `goods_img`
    DISABLE KEYS */;
INSERT INTO `goods_img`
VALUES (1, '15dd4670f56f4a339a54457e726b2844.jpg', 1, 1, '/usr/tomcat/Img/Jessie'),
       (2, '2bf7318f68b4441c950c547af011aedf.jpg', 1, 1, '/usr/tomcat/Img/Jessie'),
       (4, '649fd203bb6b490889ae5032a128c71b.jpg', 2, 1, '/usr/tomcat/Img/Jessie'),
       (5, '8feaaf66d2234b03913999de5f252f85.jpg', 2, 1, '/usr/tomcat/Img/Jessie'),
       (6, 'a965e48186e34eac936c1be077518b6a.jpg', 2, 1, '/usr/tomcat/Img/Jessie'),
       (7, '14ac1b6771d54e10a7101d43baa252f9.png', 3, 1, '/usr/tomcat/Img/Jessie'),
       (8, '79b0ca03fe0340e9978c73df02220958.jpg', 4, 2, '/usr/tomcat/Img/user1'),
       (9, '40194ae3d5d54eccab516c31a6672228.jpg', 5, 2, '/usr/tomcat/Img/user1'),
       (10, 'd232ae18381f43108dfa2d6af0a2c089.jpg', 6, 2, '/usr/tomcat/Img/user1'),
       (11, 'f4bacf5819b34464998dbe7e214b2fca.jpg', 7, 2, '/usr/tomcat/Img/user1'),
       (12, '5d09c64c8adf471cbf4b52b9dd77c729.jpg', 8, 2, '/usr/tomcat/Img/user1'),
       (13, '7d46d9f8a85e4ef19f540fd1ce1fb1cc.jpg', 9, 2, '/usr/tomcat/Img/user1'),
       (14, 'b0021c0d90874f5587ebe43e70958b7e.jpg', 10, 2, '/usr/tomcat/Img/user1'),
       (15, 'dea73d9ed31d4b74a89c991fa789c969.jpg', 11, 2, '/usr/tomcat/Img/user1'),
       (16, '5509e6a1950440658074fbb1f2e9475d.jpeg', 12, 2, '/usr/tomcat/Img/user1'),
       (17, '969735aaacf64c699d230d8b8ba9012e.jpg', 13, 2, '/usr/tomcat/Img/user1'),
       (18, '339969eccebf45acaeaebf4b0bc832b0.jpg', 14, 2, '/usr/tomcat/Img/user1'),
       (19, '346d61d77636421d997acc0448af3ecd.jpg', 15, 2, '/usr/tomcat/Img/user1'),
       (20, '19_0.jpeg', 19, 8, '/usr/tomcat/Img/HK'),
       (21, '19_1.jpeg', 19, 8, '/usr/tomcat/Img/HK'),
       (22, '19_2.jpeg', 19, 8, '/usr/tomcat/Img/HK'),
       (23, '19_0.jpeg', 19, 8, '/usr/tomcat/Img/HK'),
       (24, '19_1.jpeg', 19, 8, '/usr/tomcat/Img/HK'),
       (25, '19_2.jpeg', 19, 8, '/usr/tomcat/Img/HK'),
       (26, '19_0.jpeg', 19, 8, '/usr/tomcat/Img/HK'),
       (27, '19_1.jpeg', 19, 8, '/usr/tomcat/Img/HK'),
       (28, '19_2.jpeg', 19, 8, '/usr/tomcat/Img/HK'),
       (29, '20_0.jpeg', 20, 8, '/usr/tomcat/Img/HK'),
       (30, '20_1.jpeg', 20, 8, '/usr/tomcat/Img/HK'),
       (31, '7_0.jpeg', 7, 1, '/usr/tomcat/Img/Jessie'),
       (32, '18_0.jpeg', 18, 2, '/usr/tomcat/Img/user1'),
       (33, '17_0.jpeg', 17, 2, '/usr/tomcat/Img/user1'),
       (34, '17_1.jpeg', 17, 2, '/usr/tomcat/Img/user1'),
       (35, '21_0.jpeg', 21, 8, '/usr/tomcat/Img/HK'),
       (36, '22_0.jpeg', 22, 8, '/usr/tomcat/Img/HK'),
       (37, '23_0.jpeg', 23, 8, '/usr/tomcat/Img/HK'),
       (38, '24_0.jpeg', 24, 8, '/usr/tomcat/Img/HK'),
       (39, '24_1.jpeg', 24, 8, '/usr/tomcat/Img/HK'),
       (40, '25_0.jpeg', 25, 8, '/usr/tomcat/Img/HK'),
       (41, '26_0.jpeg', 26, 8, '/usr/tomcat/Img/HK'),
       (42, '27_0.jpeg', 27, 8, '/usr/tomcat/Img/HK'),
       (43, '27_1.jpeg', 27, 8, '/usr/tomcat/Img/HK'),
       (44, '27_2.jpeg', 27, 8, '/usr/tomcat/Img/HK'),
       (45, '28_0.jpeg', 28, 8, '/usr/tomcat/Img/HK'),
       (46, '28_1.jpeg', 28, 8, '/usr/tomcat/Img/HK'),
       (47, '28_2.jpeg', 28, 8, '/usr/tomcat/Img/HK'),
       (48, '28_3.jpeg', 28, 8, '/usr/tomcat/Img/HK'),
       (49, '29_0.jpeg', 29, 8, '/usr/tomcat/Img/HK'),
       (50, '30_0.jpeg', 30, 8, '/usr/tomcat/Img/HK'),
       (51, '31_0.jpeg', 31, 8, '/usr/tomcat/Img/HK'),
       (52, '32_0.jpeg', 32, 8, '/usr/tomcat/Img/HK'),
       (53, '33_0.jpeg', 33, 8, '/usr/tomcat/Img/HK'),
       (54, '34_0.jpeg', 34, 8, '/usr/tomcat/Img/HK'),
       (55, '35_0.jpeg', 35, 1, '/usr/tomcat/Img/Jessie'),
       (56, '36_0.jpeg', 36, 1, '/usr/tomcat/Img/Jessie');
/*!40000 ALTER TABLE `goods_img`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods_label`
--

DROP TABLE IF EXISTS `goods_label`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goods_label`
(
    `label1` varchar(15) NOT NULL,
    `label2` varchar(15) NOT NULL,
    PRIMARY KEY (`label1`, `label2`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_label`
--

LOCK TABLES `goods_label` WRITE;
/*!40000 ALTER TABLE `goods_label`
    DISABLE KEYS */;
INSERT INTO `goods_label`
VALUES ('书', '书'),
       ('书', '小说'),
       ('书', '教科书'),
       ('书', '教程'),
       ('书', '辅导材料'),
       ('二刺猿', '二刺猿'),
       ('其他', '其他'),
       ('包', '包'),
       ('包', '双肩包'),
       ('包', '手提包'),
       ('包', '电脑包'),
       ('包', '腰包'),
       ('女装', '上衣'),
       ('女装', '其他'),
       ('女装', '内衣'),
       ('女装', '外套'),
       ('女装', '女装'),
       ('女装', '袜子'),
       ('女装', '裙子'),
       ('女装', '裤子'),
       ('家纺', '家纺'),
       ('家纺', '床单'),
       ('家纺', '床垫'),
       ('家纺', '枕头'),
       ('家纺', '枕芯'),
       ('家纺', '被子'),
       ('家纺', '被芯'),
       ('手机', '手机'),
       ('手机配件', '手机配件'),
       ('生活日用', '生活日用'),
       ('电器', '其他大家电'),
       ('电器', '小家电'),
       ('电器', '洗衣机'),
       ('电器', '电器'),
       ('电脑', '台式机'),
       ('电脑', '笔记本电脑'),
       ('电脑配件', 'U盘'),
       ('电脑配件', '外设'),
       ('电脑配件', '电脑配件'),
       ('电脑配件', '路由器'),
       ('男装', '上衣'),
       ('男装', '其他'),
       ('男装', '内衣'),
       ('男装', '外套'),
       ('男装', '男装'),
       ('男装', '袜子'),
       ('男装', '裙子（？)'),
       ('男装', '裤子'),
       ('运动用品', '乒乓球'),
       ('运动用品', '护具'),
       ('运动用品', '篮球'),
       ('运动用品', '羽毛球'),
       ('运动用品', '足球'),
       ('运动用品', '运动服类'),
       ('运动用品', '运动用品'),
       ('鞋', '板鞋'),
       ('鞋', '球鞋'),
       ('鞋', '跑步鞋'),
       ('鞋', '运动鞋'),
       ('鞋', '鞋');
/*!40000 ALTER TABLE `goods_label`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods_report`
--

DROP TABLE IF EXISTS `goods_report`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goods_report`
(
    `uid`        int(11)      NOT NULL,
    `target`     int(11)      NOT NULL,
    `reason`     varchar(100) NOT NULL,
    `status`     int(11)      NOT NULL,
    `reportId`   int(11)      NOT NULL AUTO_INCREMENT,
    `result`     varchar(100) DEFAULT NULL,
    `finishTime` datetime     DEFAULT NULL,
    PRIMARY KEY (`reportId`),
    KEY `goods_report_FK` (`uid`),
    KEY `goods_report_FK_1` (`target`),
    CONSTRAINT `goods_report_FK` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `goods_report_FK_1` FOREIGN KEY (`target`) REFERENCES `goods` (`gid`) ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_report`
--

LOCK TABLES `goods_report` WRITE;
/*!40000 ALTER TABLE `goods_report`
    DISABLE KEYS */;
INSERT INTO `goods_report`
VALUES (8, 1, 'badbad', 0, 1, NULL, NULL),
       (8, 18, '不喜欢', 0, 2, NULL, NULL);
/*!40000 ALTER TABLE `goods_report`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_comment`
--

DROP TABLE IF EXISTS `order_comment`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_comment`
(
    `oid`       int(11) NOT NULL,
    `buyer`     int(11) NOT NULL,
    `seller`    int(11) NOT NULL,
    `b_Comment` varchar(100) DEFAULT NULL,
    `b_Type`    varchar(3)   DEFAULT NULL,
    `s_Comment` varchar(100) DEFAULT NULL,
    `s_Type`    varchar(3)   DEFAULT NULL,
    PRIMARY KEY (`oid`),
    CONSTRAINT `order_comment_FK` FOREIGN KEY (`oid`) REFERENCES `user_order` (`oid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_comment`
--

LOCK TABLES `order_comment` WRITE;
/*!40000 ALTER TABLE `order_comment`
    DISABLE KEYS */;
INSERT INTO `order_comment`
VALUES (1, 2, 1, '开什么玩笑，我超勇的啦', '好评', '这么说，你很勇咯？', '好评');
/*!40000 ALTER TABLE `order_comment`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission`
(
    `id`          int(11)     NOT NULL AUTO_INCREMENT,
    `name`        varchar(20) NOT NULL,
    `description` varchar(100) DEFAULT NULL,
    `url`         varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission`
    DISABLE KEYS */;
INSERT INTO `permission`
VALUES (1, 'admin', '全部权限', '/'),
       (2, 'user', '普通用户的权限', NULL);
/*!40000 ALTER TABLE `permission`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop_cart`
--

DROP TABLE IF EXISTS `shop_cart`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop_cart`
(
    `uid`  int(11) NOT NULL,
    `data` varchar(500) DEFAULT NULL,
    KEY `shop_cart_FK` (`uid`),
    CONSTRAINT `shop_cart_FK` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop_cart`
--

LOCK TABLES `shop_cart` WRITE;
/*!40000 ALTER TABLE `shop_cart`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `shop_cart`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user`
(
    `username`   varchar(20)  NOT NULL,
    `password`   varchar(100) NOT NULL,
    `uid`        int(11)      NOT NULL AUTO_INCREMENT,
    `mailAddr`   varchar(100)          DEFAULT NULL,
    `nickName`   varchar(15)           DEFAULT NULL,
    `status`     int(11)      NOT NULL COMMENT '代表不合格产品数',
    `role`       varchar(10)  NOT NULL,
    `img_path`   varchar(100)          DEFAULT NULL,
    `evaluation` int(11)      NOT NULL DEFAULT '1',
    PRIMARY KEY (`uid`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 20
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user`
    DISABLE KEYS */;
INSERT INTO `user`
VALUES ('Jessie', '$2a$10$GGgQEodK/z42PIdpZxk1wuLSHgVukS6SbmnmrnnKbCZUs.OEXSJT2', 1, '1647389906@qq.com', 'Jessie', 100,
        'admin', '', 1),
       ('user1', '$2a$10$MRxzAiv6bttM5Nq1idPsVuXZtwv6TAnSVayRHK7.JfKo.FdsB..qS', 2, NULL, 'user1', 100, 'user',
        '/usr/tomcat/Img/user1/1619325129946.jpeg', 1),
       ('123123', '$2a$10$vTtiULjnkHPs0cLEt5ZHkeAfTPFzF20dbxqMZag4SycU/q8SdDdbq', 3, NULL, '123123', 100, 'user', '',
        1),
       ('shushu', '$2a$10$xSJhjU0QaaW/nwdAuy/Sve9SiYAbJO1SA32trn3Gr3B2PneONv3Ly', 4, NULL, 'shushu', 100, 'user', '',
        1),
       ('testuser1', '$2a$10$UGT/cJCzHw2r0VljiGaj9..P9xTo/Wym7kpIMk1Qm1jCowcpEpm7i', 5, NULL, 'testuser1', 100, 'user',
        '', 1),
       ('', '$2a$10$gCZxRPU06N7o2u3hTGZP4.KeVwbG23c9g.jEPA37GMTSIiADZkG9W', 6, NULL, 'NULLNAME', 100, 'user', NULL, 1),
       ('1', '$2a$10$/t2gtubvs6zf0Hk8jyXUsOIDcz4ZxdUuAvHd5fdnx62UzcM9AZ2dm', 7, NULL, '1', 100, 'user', NULL, 1),
       ('HK', '$2a$10$sFTFGbjEBuMHGfKQRduSAut6R9jKBN0EJs80iBuk7lAq8nihHv1ma', 8, '563589126@qq.com', 'HK', 100, 'user',
        '/usr/tomcat/Img/HK/8HK.jpeg', 1),
       ('HHKK', '$2a$10$OyA7oXUGTRXDuoZhfdU9T.880.QSSgUnedHjOmCbbn6tfoL5kDfMu', 9, '563589126@qq.com', 'HHKK', 100,
        'user', NULL, 1),
       ('11111111', '$2a$10$rFgEP65IrRL1m2mLSQ7bVu6H6MN38gI/n36.cIAITCqjPrUuN2HB.', 10, NULL, NULL, 100, 'user', NULL,
        2),
       ('031902606', '$2a$10$fCuaDhCf.GhGklDImK38o.D3HyBCqNKZS8mxcJMmF6r1SwRRn4/qq', 11, NULL, NULL, 100, 'user', NULL,
        2),
       ('123', '$2a$10$XJnibTQ7/h0cP8ELVsV1IuVdTgpjSkiW4XZmccQ9hLhr5WQQpMvi2', 12, NULL, NULL, 100, 'user', NULL, 2),
       ('testuser2', '$2a$10$5ZqpKSpJsuodxsCQzeiQc.BYhXkJ0sIe50DsF8PDKW.iQd/92uWJC', 13, NULL, NULL, 100, 'user', NULL,
        2),
       ('testuser3', '$2a$10$xzZn8BltHEl3B8bJx2EdUO5ZF7YsOZkdmdg3u3R3y0ffKNvPNNtdC', 14, NULL, NULL, 100, 'user', NULL,
        2),
       ('User2', '$2a$10$IFPUc/A8g512j4vvxgGQCed0MMTzr5Rsih/JnNe/QzOAhwQIBMRRS', 15, NULL, NULL, 100, 'user', NULL, 2),
       ('User3', '$2a$10$5ltOcOK75Wzj/k8QgRsb0OOqP5GPIzWO/2XTQzRWUnRgvfjrpZnOe', 16, NULL, NULL, 100, 'user', NULL, 2),
       ('User4', '$2a$10$6UDIKB2.upthKGxlo05I7eYHjtr3stg8ypCHskg0LaisfNfJ7AMsK', 17, NULL, NULL, 100, 'user', NULL, 2),
       ('User6', '$2a$10$HAAw6uP/DH2uiV2PVpFXpOf0m3pM/px3O6AwTqrht9Vf.ucAXrHtK', 18, NULL, NULL, 100, 'user', NULL, 2),
       ('User7', '$2a$10$Y2W76qyEEoADlr3JSrLBAukr3faz2GXc4oy.LjJOjW1y1FojIic.i', 19, NULL, NULL, 100, 'user', NULL, 2);
/*!40000 ALTER TABLE `user`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_identity`
--

DROP TABLE IF EXISTS `user_identity`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_identity`
(
    `uid`    int(11)     NOT NULL,
    `No`     varchar(15) NOT NULL,
    `school` varchar(30) NOT NULL,
    PRIMARY KEY (`uid`),
    CONSTRAINT `user_identity_FK` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_identity`
--

LOCK TABLES `user_identity` WRITE;
/*!40000 ALTER TABLE `user_identity`
    DISABLE KEYS */;
INSERT INTO `user_identity`
VALUES (1, 'TEST', 'FZU'),
       (2, '031902410', '福州大学'),
       (3, '0000', 'FZU'),
       (8, '031902606', '福州大学');
/*!40000 ALTER TABLE `user_identity`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_order`
--

DROP TABLE IF EXISTS `user_order`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_order`
(
    `oid`           int(11)  NOT NULL AUTO_INCREMENT,
    `generatedTime` datetime NOT NULL,
    `status`        int(11)  NOT NULL COMMENT '0—任何一方没有确认，1——买家确认，10——卖家确认，11——双方确认。负数——异常。',
    `buyer`         int(11)  NOT NULL,
    `seller`        int(11)  NOT NULL,
    `doneTime`      datetime     DEFAULT NULL,
    `deliveryTime`  datetime     DEFAULT NULL,
    `cid`           int(11)      DEFAULT NULL,
    `location`      varchar(100) DEFAULT NULL,
    `gid`           int(11)  NOT NULL,
    PRIMARY KEY (`oid`),
    KEY `order_FK` (`gid`),
    CONSTRAINT `order_FK` FOREIGN KEY (`gid`) REFERENCES `goods` (`gid`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_order`
--

LOCK TABLES `user_order` WRITE;
/*!40000 ALTER TABLE `user_order`
    DISABLE KEYS */;
INSERT INTO `user_order`
VALUES (1, '2021-04-17 00:00:00', 0, 2, 1, NULL, NULL, NULL, NULL, 1),
       (2, '2021-04-17 21:35:11', -1, 2, 1, NULL, NULL, 0, '???', 2),
       (3, '2021-04-19 19:39:11', 11, 2, 1, NULL, NULL, 1, '杰哥家中', 2),
       (4, '2021-04-19 19:43:23', 0, 2, 1, NULL, NULL, 1, '杰哥家中', 3),
       (5, '2021-04-19 19:47:21', 0, 2, 1, NULL, NULL, 1, '杰哥家中', 4);
/*!40000 ALTER TABLE `user_order`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_permission`
--

DROP TABLE IF EXISTS `user_permission`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_permission`
(
    `uid` int(11) NOT NULL,
    `pid` int(11) NOT NULL,
    KEY `user_permission_FK` (`uid`),
    KEY `user_permission_FK_1` (`pid`),
    CONSTRAINT `user_permission_FK` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `user_permission_FK_1` FOREIGN KEY (`pid`) REFERENCES `permission` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_permission`
--

LOCK TABLES `user_permission` WRITE;
/*!40000 ALTER TABLE `user_permission`
    DISABLE KEYS */;
INSERT INTO `user_permission`
VALUES (1, 1),
       (2, 2),
       (8, 2);
/*!40000 ALTER TABLE `user_permission`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_portrait`
--

DROP TABLE IF EXISTS `user_portrait`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_portrait`
(
    `uid`                      int(11) NOT NULL,
    `count_GoodComment`        int(11) NOT NULL DEFAULT '0',
    `count_MediumComment`      int(11) NOT NULL DEFAULT '0',
    `count_BadComment`         int(11) NOT NULL DEFAULT '0',
    `count_punishedScore`      int(11) NOT NULL DEFAULT '0',
    `count_additionalScore`    int(11) NOT NULL DEFAULT '0',
    `count_send_GoodsComment`  int(11) NOT NULL DEFAULT '0',
    `count_send_MediumComment` int(11) NOT NULL DEFAULT '0',
    `count_send_BadComment`    int(11) NOT NULL DEFAULT '0'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_portrait`
--

LOCK TABLES `user_portrait` WRITE;
/*!40000 ALTER TABLE `user_portrait`
    DISABLE KEYS */;
INSERT INTO `user_portrait`
VALUES (1, 0, 0, 0, 0, 0, 0, 0, 0),
       (2, 0, 0, 0, 0, 0, 0, 0, 0),
       (3, 0, 0, 0, 0, 0, 0, 0, 0),
       (4, 0, 0, 0, 0, 0, 0, 0, 0),
       (5, 0, 0, 0, 0, 0, 0, 0, 0),
       (9, 0, 0, 0, 0, 0, 0, 0, 0),
       (10, 0, 0, 0, 0, 0, 0, 0, 0),
       (15, 0, 0, 0, 0, 0, 0, 0, 0),
       (16, 0, 0, 0, 0, 0, 0, 0, 0),
       (17, 0, 0, 0, 0, 0, 0, 0, 0),
       (18, 0, 0, 0, 0, 0, 0, 0, 0),
       (19, 0, 0, 0, 0, 0, 0, 0, 0);
/*!40000 ALTER TABLE `user_portrait`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_token`
--

DROP TABLE IF EXISTS `user_token`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_token`
(
    `uid`      int(11)     NOT NULL,
    `username` varchar(20) NOT NULL,
    `token`    varchar(500) DEFAULT NULL,
    `mailCode` varchar(10)  DEFAULT NULL,
    `temp`     varchar(100) DEFAULT NULL,
    PRIMARY KEY (`uid`),
    CONSTRAINT `user_token_FK` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_token`
--

LOCK TABLES `user_token` WRITE;
/*!40000 ALTER TABLE `user_token`
    DISABLE KEYS */;
INSERT INTO `user_token`
VALUES (1, 'Jessie', NULL, 'pUN6a2', '2'),
       (2, 'user1', NULL, NULL, NULL),
       (3, '123123', NULL, NULL, NULL),
       (6, '', NULL, NULL, NULL),
       (7, '1', NULL, NULL, NULL),
       (8, 'HK', NULL, '', ''),
       (9, 'HHKK', NULL, '', ''),
       (10, '11111111', NULL, NULL, NULL),
       (11, '031902606', NULL, NULL, NULL),
       (12, '123', NULL, NULL, NULL),
       (13, 'testuser2', NULL, NULL, NULL),
       (14, 'testuser3', NULL, NULL, NULL),
       (15, 'User2', NULL, NULL, NULL),
       (16, 'User3', NULL, NULL, NULL),
       (17, 'User4', NULL, NULL, NULL),
       (18, 'User6', NULL, NULL, NULL),
       (19, 'User7', NULL, NULL, NULL);
/*!40000 ALTER TABLE `user_token`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'secondhandmarket'
--
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2021-05-08 23:47:27
