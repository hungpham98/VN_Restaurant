CREATE DATABASE if NOT EXISTS `vietnamese_restaurant`;
USE `vietnamese_restaurant`;
CREATE TABLE if NOT EXISTS product(
	`productID` INT PRIMARY KEY AUTO_INCREMENT,
	`name` VARCHAR(50) NOT null,
	`price` INT NOT NULL,
	`ingredients` VARCHAR(255),
	`amount` INT,
	`img` VARCHAR(255) NOT NULL,
	`categoryID` INT NOT NULL
)

CREATE TABLE if NOT EXISTS admin(
	`id` INT PRIMARY KEY AUTO_INCREMENT,
	`username` VARCHAR(50) UNIQUE NOT NULL,
	`password` VARCHAR(50) NOT NULL,
	`name` VARCHAR(50),
	`email` VARCHAR(50),
	`birthday` DATE NOT NULL 
)
CREATE TABLE if NOT EXISTS category(
	`categoryID` INT PRIMARY KEY AUTO_INCREMENT,
	`categoryname` VARCHAR(50) NOT NULL,
	`categoryBanner` VARCHAR(60) NOT NULL
)

ALTER TABLE	product 
ADD FOREIGN KEY (categoryID) REFERENCES category(categoryID);

CREATE TABLE if NOT EXISTS member(
	`id` INT PRIMARY KEY AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL,
	`address` VARCHAR(50) NOT NULL,
	`phone` VARCHAR(50) NOT NULL,
	`username` VARCHAR(50) UNIQUE NOT NULL,
	`password` VARCHAR(50) NOT NULL,
	`email` VARCHAR(50) NOT NULL
)

CREATE TABLE if NOT EXISTS `order`(
	`orderID` INT PRIMARY KEY AUTO_INCREMENT,
	`memberID` INT NOT NULL,
	`status` INT NOT NULL,
	`date` DATE NOT NULL
)

ALTER TABLE `order`
ADD FOREIGN KEY (memberID) REFERENCES member(id);

CREATE TABLE if NOT EXISTS `order_detail`(
	`orderID` INT, 
	`amount` INT,
	`productID` INT,
	PRIMARY KEY(`orderID`, `productID`)
)

ALTER TABLE `order_detail`
ADD FOREIGN KEY (`orderID`) REFERENCES `order`(`orderID`);

ALTER TABLE `order_detail`
ADD FOREIGN KEY (`productID`) REFERENCES `product`(`productID`);

INSERT INTO admin(`username`,`password`)
VALUES ("huy",MD5("123"));

INSERT INTO product(`name`,price,`ingredients`,`amount`,img,categoryID)
VALUES ("Cơm gà hội an",45000,"cơm, gà xé, hạt tiêu, hành tây, hành khô",1 ,"com_ga_hoi_an.jpg",1);

INSERT INTO product(`name`,price,`ingredients`,`amount`,img,categoryID)
VALUES ("Cơm sườn chua ngọt",45000,"cơm, sườn heo, hành lá, cà chua, tỏi",1 ,"com_suon_chua_ngot.jpg",1);

INSERT INTO product(`name`,price,`ingredients`,`amount`,img,categoryID)
VALUES ("Cơm rang dưa bò",40000,"cơm, bò, dưa chua, hành tây, hành khô",1 ,"com_rang_dua_bo.jpg",1);

INSERT INTO product(`name`,price,`ingredients`,`amount`,img,categoryID)
VALUES ("Cơm gà sốt chua ngọt",40000,"cơm, đùi gà 1 phần tư, hạt tiêu, cà chua, hành khô",1 ,"com_ga_sot_chua_ngot.jpg",1);

INSERT INTO product(`name`,price,`ingredients`,`amount`,img,categoryID)
VALUES ("Cơm trộn hàn quốc",50000,"cơm, hạt tiêu, bò, hành tây, rong biển, kim chi, trứng",2 ,"com_tron_han_quoc.jpg",1);

INSERT INTO product(`name`,price,`amount`,img,categoryID)
VALUES ("Phở bò tái chín",40000,1 ,"pho_bo_tai_chin.jpg",1);

INSERT INTO product(`name`,price,`amount`,img,categoryID)
VALUES ("Phở gầu bò",40000,1 ,"pho_gau_bo.jpg",1);

INSERT INTO product(`name`,price,`amount`,img,categoryID)
VALUES ("Phở bò tái nạm",40000,1 ,"pho_bo_tai_nam.jpg",1);

INSERT INTO product(`name`,price,`amount`,img,categoryID)
VALUES ("Phở đặc biệt",50000,1 ,"pho_dac_biet.jpg",1);

INSERT INTO product(`name`,price,`ingredients`,`amount`,img,categoryID)
VALUES ("Phở cuốn",30000,"bánh phở, bò, xà lách, dưa chuột, trứng gà, cà rốt",1 , "pho_cuon.jpg",1);


INSERT INTO product(`name`,price,img,categoryID)
VALUES ("Coca",15000, "coca.jpg",2);

INSERT INTO product(`name`,price,img,categoryID)
VALUES ("Sprite",15000, "sprite.jpg",2);

INSERT INTO product(`name`,price,img,categoryID)
VALUES ("Trà đá",3000, "tra_da.jpg",2);

INSERT INTO product(`name`,price,img,categoryID)
VALUES ("Bò húc",15000, "bo_huc.jpg",2);


INSERT INTO product(`name`,price,`ingredients`,`amount`,img,categoryID)
VALUES ("Lẩu ếch",250000, "1l nước lẩu, 500gr ếch, bún, mỳ, rau thả lẩu", 2 ,"lau_ech.jpg",1);

INSERT INTO product(`name`,price,`ingredients`,`amount`,img,categoryID)
VALUES ("Lẩu thái",250000, "1l nước lẩu, 300gr bò, 200gr bạch tuộc, mỳ, rau thả lẩu", 2 ,"lau_thai.jpg",1);

INSERT INTO product(`name`,price,`ingredients`,`amount`,img,categoryID)
VALUES ("Mỳ tim cật",35000, "mỳ, tim heo, cật heo, rau cải, rau hẹ, hành tây",1 ,"my_tim_cat.jpg",1);

INSERT INTO product(`name`,price,`ingredients`,`amount`,img,categoryID)
VALUES ("Mỳ indomie",25000, "2 gói mỳ indomie, viên cá, viên mực, trứng",1 ,"my_indomie.jpg",1);

INSERT INTO product(`name`,price,`ingredients`,`amount`,img,categoryID)
VALUES ("Mỳ xào",35000,"2 gói mỳ miliket, bò, rau cải, hành tây",1 ,"my_xao.jpg",1);

INSERT INTO product(`name`,price,`ingredients`,`amount`,img,categoryID)
VALUES ("Mỳ trộn hàn quốc",40000,"1 gói mỳ hàn quốc, bò, kim chi, rong biển, trứng",1 ,"my_tron_ham_quoc.jpg",1);

INSERT INTO product(`name`,price,`ingredients`,`amount`,img,categoryID)
VALUES ("Mỳ cay",35000,"1 gói mỳ hàn quốc, bột ớt, bạch tuộc, bò",1 ,"my_cay.jpg",1);

INSERT INTO category(`categoryname`)
VALUES ("Đồ ăn");

INSERT INTO category(`categoryname`)
VALUES ("Đồ uống");

INSERT INTO member(`name`,`address`,`phone`,`username`,`password`,`email`)
VALUES ("Mr.A","285 doi can","0123456789","mra","abcd1234","mra@yahoo.com")

INSERT INTO `order`(`orderID`,`memberID`, `total_price`)
VALUES (1,1,200);

INSERT INTO member(`name`,`address`,`phone`,`username`,`password`,`email`)
VALUES ("Mr.A","285 doi can","0123456789","a","123","mra@yahoo.com")
INSERT INTO `order`(memberID,`date`)
VALUES (1, "2021-07-22"))

SELECT SUM(p.price * o.amount)  FROM order_detail AS o JOIN product AS p ON p.productID = o.productID 
