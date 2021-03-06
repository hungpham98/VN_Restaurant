-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.17-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             11.2.0.6213
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for vietnamese_restaurant
CREATE DATABASE IF NOT EXISTS `vietnamese_restaurant` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `vietnamese_restaurant`;

-- Dumping structure for table vietnamese_restaurant.admin
CREATE TABLE IF NOT EXISTS `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `birthday` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table vietnamese_restaurant.admin: ~1 rows (approximately)
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` (`id`, `username`, `password`, `name`, `email`, `birthday`) VALUES
	(1, 'admin', '202cb962ac59075b964b07152d234b70', NULL, NULL, '0000-00-00');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;

-- Dumping structure for table vietnamese_restaurant.category
CREATE TABLE IF NOT EXISTS `category` (
  `categoryID` int(11) NOT NULL,
  `categoryname` varchar(50) NOT NULL,
  PRIMARY KEY (`categoryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table vietnamese_restaurant.category: ~2 rows (approximately)
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` (`categoryID`, `categoryname`) VALUES
	(1, '????? ??n'),
	(2, '????? u???ng');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;

-- Dumping structure for table vietnamese_restaurant.member
CREATE TABLE IF NOT EXISTS `member` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `address` varchar(50) NOT NULL,
  `phone` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table vietnamese_restaurant.member: ~1 rows (approximately)
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
/*!40000 ALTER TABLE `member` ENABLE KEYS */;

-- Dumping structure for table vietnamese_restaurant.order
CREATE TABLE IF NOT EXISTS `order` (
  `orderID` int(11) NOT NULL AUTO_INCREMENT,
  `memberID` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`orderID`),
  KEY `memberID` (`memberID`),
  CONSTRAINT `order_ibfk_1` FOREIGN KEY (`memberID`) REFERENCES `member` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table vietnamese_restaurant.order: ~5 rows (approximately)
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
/*!40000 ALTER TABLE `order` ENABLE KEYS */;

-- Dumping structure for table vietnamese_restaurant.order_detail
CREATE TABLE IF NOT EXISTS `order_detail` (
  `orderID` int(11) NOT NULL,
  `amount` int(11) DEFAULT NULL,
  `productID` int(11) NOT NULL,
  PRIMARY KEY (`orderID`,`productID`),
  KEY `productID` (`productID`),
  CONSTRAINT `order_detail_ibfk_1` FOREIGN KEY (`orderID`) REFERENCES `order` (`orderID`),
  CONSTRAINT `order_detail_ibfk_2` FOREIGN KEY (`productID`) REFERENCES `product` (`productID`),
  CONSTRAINT `order_detail_ibfk_3` FOREIGN KEY (`orderID`) REFERENCES `order` (`orderID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table vietnamese_restaurant.order_detail: ~7 rows (approximately)
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;

-- Dumping structure for table vietnamese_restaurant.product
CREATE TABLE IF NOT EXISTS `product` (
  `productID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `price` int(11) NOT NULL,
  `ingredients` varchar(255) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `img` varchar(255) NOT NULL,
  `categoryID` int(11) NOT NULL,
  PRIMARY KEY (`productID`),
  KEY `categoryID` (`categoryID`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`categoryID`) REFERENCES `category` (`categoryID`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table vietnamese_restaurant.product: ~21 rows (approximately)
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` (`productID`, `name`, `price`, `ingredients`, `amount`, `img`, `categoryID`) VALUES
	(1, 'C??m g?? h???i an', 45000, 'c??m, g?? x??, h???t ti??u, h??nh t??y, h??nh kh??', 1, 'com_ga_hoi_an.jpg', 1),
	(2, 'C??m s?????n chua ng???t', 45000, 'c??m, s?????n heo, h??nh l??, c?? chua, t???i', 1, 'com_suon_chua_ngot.jpg', 1),
	(3, 'C??m rang d??a b??', 40000, 'c??m, b??, d??a chua, h??nh t??y, h??nh kh??', 1, 'com_rang_dua_bo.jpg', 1),
	(4, 'C??m g?? s???t chua ng???t', 40000, 'c??m, ????i g?? 1 ph???n t??, h???t ti??u, c?? chua, h??nh kh??', 1, 'com_ga_sot_chua_ngot.jpg', 1),
	(5, 'C??m tr???n h??n qu???c', 50000, 'c??m, h???t ti??u, b??, h??nh t??y, rong bi???n, kim chi, tr???ng', 2, 'com_tron_han_quoc.jpg', 1),
	(6, 'Ph??? b?? t??i ch??n', 40000, NULL, 1, 'pho_bo_tai_chin.jpg', 1),
	(7, 'Ph??? g???u b??', 40000, NULL, 1, 'pho_gau_bo.jpg', 1),
	(8, 'Ph??? b?? t??i n???m', 40000, NULL, 1, 'pho_bo_tai_nam.jpg', 1),
	(9, 'Ph??? ?????c bi???t', 50000, NULL, 1, 'pho_dac_biet.jpg', 1),
	(10, 'Ph??? cu???n', 30000, 'b??nh ph???, b??, x?? l??ch, d??a chu???t, tr???ng g??, c?? r???t', 1, 'pho_cuon.jpg', 1),
	(11, 'Coca', 15000, NULL, NULL, 'coca.jpg', 2),
	(12, 'Sprite', 15000, NULL, NULL, 'sprite.jpg', 2),
	(13, 'Tr?? ????', 3000, NULL, NULL, 'tra_da.jpg', 2),
	(14, 'B?? h??c', 15000, NULL, NULL, 'bo_huc.jpg', 2),
	(15, 'L???u ???ch', 250000, '1l n?????c l???u, 500gr ???ch, b??n, m???, rau th??? l???u', 2, 'lau_ech.jpg', 1),
	(16, 'L???u th??i', 250000, '1l n?????c l???u, 300gr b??, 200gr b???ch tu???c, m???, rau th??? l???u', 2, 'lau_thai.jpg', 1),
	(17, 'M??? tim c???t', 35000, 'm???, tim heo, c???t heo, rau c???i, rau h???, h??nh t??y', 1, 'my_tim_cat.jpg', 1),
	(18, 'M??? indomie', 25000, '2 g??i m??? indomie, vi??n c??, vi??n m???c, tr???ng', 1, 'my_indomie.jpg', 1),
	(19, 'M??? x??o', 35000, '2 g??i m??? miliket, b??, rau c???i, h??nh t??y', 1, 'my_xao.jpg', 1),
	(20, 'M??? tr???n h??n qu???c', 40000, '1 g??i m??? h??n qu???c, b??, kim chi, rong bi???n, tr???ng', 1, 'my_tron_han_quoc.jpg', 1),
	(21, 'M??? cay', 35000, '1 g??i m??? h??n qu???c, b???t ???t, b???ch tu???c, b??', 1, 'my_cay.jpg', 1);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
