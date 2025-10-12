/*
SQLyog Community v13.3.0 (64 bit)
MySQL - 8.0.18 : Database - aplikacija
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`aplikacija` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `aplikacija`;

/*Table structure for table `korisnik` */

DROP TABLE IF EXISTS `korisnik`;

CREATE TABLE `korisnik` (
  `mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ime` varchar(255) NOT NULL,
  `prezime` varchar(255) NOT NULL,
  `starost` int(120) NOT NULL,
  `zipcode` int(10) NOT NULL,
  PRIMARY KEY (`mail`),
  KEY `zipcode` (`zipcode`),
  CONSTRAINT `korisnik_ibfk_1` FOREIGN KEY (`zipcode`) REFERENCES `mesto` (`zipcode`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `korisnik` */

insert  into `korisnik`(`mail`,`ime`,`prezime`,`starost`,`zipcode`) values 
('anamilo@gmail.com','Ana','Milosavljevic',32,11000),
('dusansimicc@gmail.com','Dusan','Simic',22,34000),
('ficafica@mail.com','Filip[','Stevic',24,11000),
('goranm3@gmail.com','Goran','Mitrovic',30,18000),
('goranstef344@yahoo.com','Goran','Stefanovic',29,19314),
('hristinamilosavljevic@gmail.com','Hristina','Milosavljevic',19,17501),
('ivica@gmail.com','Ivica','Ivanovic',12,11000),
('majamajic@gmail.com','Maja','Majic',27,23000),
('markostevic2003@gmail.com','Marko','Stevic',22,11000),
('martin@mail.com','Martin','Petrovic',33,18000),
('mihajlosavic3@gmail.com','Mihajlo','Savic',31,18000),
('milanpetrovic@gmail.com','Milan','Petrovic',32,15000),
('milicas888@gmail.com','Milica','Stojanovic',22,17501),
('miloradmiljko@hotmail.com','Milorad','Miljkovic',43,17501),
('miloradperisic3@yahoo.com','Milorad','Perisic',32,16000),
('milosmisko2@gmail.com','Milos','Milivojevic',22,17501),
('milovanpetrovic34@gmail.com','Milovan','Petrovic',34,17501),
('mm@gmail.com','Miljan','Miljanic',26,15300),
('nenadstevic60@yahoo.com','Nenad','Stevic',28,17501),
('nesto@gmail.com','nesto','nesto',23,15300),
('peraperic@gmail.com','Pera','Peric',22,17501),
('petarpetrovic97@gmail.com','Strahinja','Petrovic',54,19314),
('petronijevicmilos@gmail.com','Milos','Petronijevic',15,18000),
('petronijevicpetar@yahoo.com','Petar','Petronijevic',15,38250),
('predragpesa3@yahoo.com','Predrag','Nikolic',63,23000),
('predragstojkovic02@gmail.com','Predrag','Stojkovic',23,15300),
('urosprodanovic359@yahoo.com','Uros','Prodanović',34,18000),
('zivoradmili@gmail.com','Zivorad','Milivojevic',46,15300);

/*Table structure for table `licenca` */

DROP TABLE IF EXISTS `licenca`;

CREATE TABLE `licenca` (
  `id_licenca` bigint(20) NOT NULL AUTO_INCREMENT,
  `naziv_licence` varchar(100) NOT NULL,
  `institucija` varchar(100) NOT NULL,
  `ovlasceno_lice` varchar(100) NOT NULL,
  `broj_licence` varchar(12) NOT NULL,
  PRIMARY KEY (`id_licenca`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `licenca` */

insert  into `licenca`(`id_licenca`,`naziv_licence`,`institucija`,`ovlasceno_lice`,`broj_licence`) values 
(13,'Upotrebna dozvola za sportski objekat','Teniski savez Srbije','Pera Peric','328-9641-411');

/*Table structure for table `mesto` */

DROP TABLE IF EXISTS `mesto`;

CREATE TABLE `mesto` (
  `zipcode` int(5) NOT NULL,
  `naziv_mesta` varchar(255) NOT NULL,
  `opstina` varchar(255) NOT NULL,
  PRIMARY KEY (`zipcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `mesto` */

insert  into `mesto`(`zipcode`,`naziv_mesta`,`opstina`) values 
(11000,'Beograd','Beograd'),
(15000,'Šabac','Mačva'),
(15300,'Loznica','Mačvanska'),
(16000,'Leskovac','Leskovac'),
(17501,'Vranje','Vranje'),
(18000,'Nis','Nis'),
(19314,'Jabukovac','Negotin'),
(23000,'Zrenjanin','Zrenjanin'),
(34000,'Kragujevac','Kragujevac'),
(38250,'Peć','Peć AP KiM'),
(38252,'Silovo','Gnjilane');

/*Table structure for table `radnik` */

DROP TABLE IF EXISTS `radnik`;

CREATE TABLE `radnik` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ime` varchar(255) NOT NULL,
  `prezime` varchar(255) NOT NULL,
  `korisnicko_ime` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sifra` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `teniski_klub` bigint(20) NOT NULL,
  `rola` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`korisnicko_ime`),
  UNIQUE KEY `korisnicko_ime_unique` (`korisnicko_ime`),
  KEY `radnik_ibfk_1` (`teniski_klub`),
  CONSTRAINT `radnik_ibfk_1` FOREIGN KEY (`teniski_klub`) REFERENCES `teniski_klub` (`id_klub`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `radnik` */

insert  into `radnik`(`id`,`ime`,`prezime`,`korisnicko_ime`,`sifra`,`teniski_klub`,`rola`) values 
(14,'Marko','Stevic','markos03','8f45e66de16af0e5b48d1b035fbc9cf27b2a83e23655c49152280aa4de679f679998bf98314a8ffad744fef7f18102def53d038583502efefcfb3d0ae322ba17',1,'ADMIN'),
(16,'Hristina','Milosavljevic','h2510','f8235df1ce85a55111794dd210a81bc820ce38a031dbeb9835c3868f1cc96d38a0fb4481dc8edadbb1112d64f2c6b9967485289edc635ac5e4caf51c717f6efa',1,'RADNIK'),
(17,'Filip','Stevic','fstevic','f6746dba64861ca93710d56cc463b056bf4be2b989bdd729713d7646f63e05aa8269fb8016657620d6ffed0d508399ba350d3ce471b23e82fe660cc4d7c1fa58',1,'RADNIK');

/*Table structure for table `rezervacija` */

DROP TABLE IF EXISTS `rezervacija`;

CREATE TABLE `rezervacija` (
  `id_rezervacija` bigint(20) NOT NULL AUTO_INCREMENT,
  `datum_rezervacije` date NOT NULL,
  `ukupan_iznos` double NOT NULL,
  `napomena` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `mail_korisnik` varchar(255) NOT NULL,
  `id_klub` bigint(20) NOT NULL,
  PRIMARY KEY (`id_rezervacija`),
  KEY `jmbg_korisnik` (`mail_korisnik`),
  KEY `rezervacija_ibfk_2` (`id_klub`),
  CONSTRAINT `rezervacija_ibfk_3` FOREIGN KEY (`mail_korisnik`) REFERENCES `korisnik` (`mail`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rezervacija_ibfk_5` FOREIGN KEY (`id_klub`) REFERENCES `teniski_klub` (`id_klub`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `rezervacija` */

insert  into `rezervacija`(`id_rezervacija`,`datum_rezervacije`,`ukupan_iznos`,`napomena`,`mail_korisnik`,`id_klub`) values 
(126,'2025-09-18',1400,'Nesto drugo','mm@gmail.com',1),
(127,'2025-09-18',1000,'4 reketa','miloradperisic3@yahoo.com',1),
(130,'2025-10-06',4200,'hh','zivoradmili@gmail.com',1),
(137,'2025-10-07',3500,'','markostevic2003@gmail.com',1),
(138,'2025-10-07',2250,'Dodatni reketi.','mihajlosavic3@gmail.com',1);

/*Table structure for table `stavka_rezervacije` */

DROP TABLE IF EXISTS `stavka_rezervacije`;

CREATE TABLE `stavka_rezervacije` (
  `id_rezervacija` bigint(20) NOT NULL,
  `rb` bigint(20) NOT NULL,
  `vreme_od` time NOT NULL,
  `vreme_do` time NOT NULL,
  `datum` date NOT NULL,
  `iznos` double NOT NULL,
  `id_teren` int(11) NOT NULL,
  PRIMARY KEY (`id_rezervacija`,`rb`),
  KEY `rb` (`rb`),
  KEY `stavka_rezervacije_ibfk_2` (`id_teren`),
  CONSTRAINT `stavka_rezervacije_ibfk_1` FOREIGN KEY (`id_rezervacija`) REFERENCES `rezervacija` (`id_rezervacija`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `stavka_rezervacije_ibfk_2` FOREIGN KEY (`id_teren`) REFERENCES `teren` (`id_teren`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `stavka_rezervacije` */

insert  into `stavka_rezervacije`(`id_rezervacija`,`rb`,`vreme_od`,`vreme_do`,`datum`,`iznos`,`id_teren`) values 
(126,0,'17:00:00','18:00:00','2025-09-22',1400,12),
(127,0,'15:00:00','16:00:00','2025-09-21',1000,4),
(130,0,'10:00:00','12:00:00','2025-10-17',4200,5),
(137,3,'09:00:00','10:00:00','2025-10-31',1500,3),
(137,4,'12:00:00','14:00:00','2025-10-16',2000,4),
(138,0,'10:00:00','11:00:00','2025-10-16',1000,4),
(138,1,'21:00:00','22:00:00','2025-10-08',1250,1);

/*Table structure for table `teniski_klub` */

DROP TABLE IF EXISTS `teniski_klub`;

CREATE TABLE `teniski_klub` (
  `id_klub` bigint(20) NOT NULL AUTO_INCREMENT,
  `naziv_kluba` varchar(100) NOT NULL,
  `adresa` varchar(255) NOT NULL,
  `mail_kluba` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id_klub`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `teniski_klub` */

insert  into `teniski_klub`(`id_klub`,`naziv_kluba`,`adresa`,`mail_kluba`) values 
(1,'Win','Bulevar oslobodjenja 5, Beograd','win@tennisclub.com');

/*Table structure for table `teren` */

DROP TABLE IF EXISTS `teren`;

CREATE TABLE `teren` (
  `id_teren` int(11) NOT NULL AUTO_INCREMENT,
  `naziv_terena` varchar(100) NOT NULL,
  `tip_terena` varchar(100) NOT NULL,
  `cena_po_satu` double NOT NULL,
  PRIMARY KEY (`id_teren`),
  UNIQUE KEY `unique_naziv_terena` (`naziv_terena`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `teren` */

insert  into `teren`(`id_teren`,`naziv_terena`,`tip_terena`,`cena_po_satu`) values 
(1,'Teren \"GSM\"','HARD',1250),
(2,'Teren 1','GRASS',1500),
(3,'Sporedni teren 1','GRASS',1500),
(4,'Hala 1','CLAY',1000),
(5,'novak djokovic','HARD',2100),
(12,'Andy Murray','GRASS',1400),
(26,'Spoljni teren 2','CLAY',1350);

/*Table structure for table `tkl` */

DROP TABLE IF EXISTS `tkl`;

CREATE TABLE `tkl` (
  `id_klub` bigint(20) NOT NULL,
  `id_licenca` bigint(20) NOT NULL,
  `datum_izdavanja` date NOT NULL,
  `grad` varchar(255) NOT NULL,
  `vazi_do` date NOT NULL,
  PRIMARY KEY (`id_klub`,`id_licenca`),
  KEY `id_licenca` (`id_licenca`),
  CONSTRAINT `tkl_ibfk_1` FOREIGN KEY (`id_klub`) REFERENCES `teniski_klub` (`id_klub`),
  CONSTRAINT `tkl_ibfk_2` FOREIGN KEY (`id_licenca`) REFERENCES `licenca` (`id_licenca`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tkl` */

insert  into `tkl`(`id_klub`,`id_licenca`,`datum_izdavanja`,`grad`,`vazi_do`) values 
(1,13,'2025-04-30','Beograd','2030-04-30');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
