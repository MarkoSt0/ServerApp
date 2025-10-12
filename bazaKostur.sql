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

/*Table structure for table `mesto` */

DROP TABLE IF EXISTS `mesto`;

CREATE TABLE `mesto` (
  `zipcode` int(5) NOT NULL,
  `naziv_mesta` varchar(255) NOT NULL,
  `opstina` varchar(255) NOT NULL,
  PRIMARY KEY (`zipcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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

/*Table structure for table `teniski_klub` */

DROP TABLE IF EXISTS `teniski_klub`;

CREATE TABLE `teniski_klub` (
  `id_klub` bigint(20) NOT NULL AUTO_INCREMENT,
  `naziv_kluba` varchar(100) NOT NULL,
  `adresa` varchar(255) NOT NULL,
  `mail_kluba` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id_klub`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
