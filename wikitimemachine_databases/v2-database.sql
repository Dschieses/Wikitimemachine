# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.6.20)
# Datenbank: wtm_german
# Erstellungsdauer: 2015-02-13 20:10:06 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Export von Tabelle category
# ------------------------------------------------------------

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `categoryId` int(11) NOT NULL AUTO_INCREMENT,
  `lang` varchar(2) NOT NULL,
  `categoryTitle` varchar(255) NOT NULL,
  `shortTitle` varchar(255) DEFAULT NULL,
  `connections` int(11) DEFAULT NULL,
  PRIMARY KEY (`categoryId`),
  UNIQUE KEY `categoryTitle` (`categoryTitle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Export von Tabelle connection
# ------------------------------------------------------------

DROP TABLE IF EXISTS `connection`;

CREATE TABLE `connection` (
  `fromPageId` int(11) NOT NULL,
  `toPageId` int(11) NOT NULL,
  `lang` varchar(2) NOT NULL,
  PRIMARY KEY (`fromPageId`,`toPageId`,`lang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Export von Tabelle pages
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pages`;

CREATE TABLE `pages` (
  `pageId` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `ns` int(11) DEFAULT NULL,
  `birthDate` int(4) DEFAULT NULL,
  `deathDate` int(4) DEFAULT NULL,
  `indegree` int(11) DEFAULT NULL,
  `outdegree` int(11) DEFAULT NULL,
  `pagerank` int(11) DEFAULT NULL,
  `lang` varchar(2) NOT NULL DEFAULT 'DE',
  PRIMARY KEY (`pageId`,`lang`),
  KEY `title` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Export von Tabelle pagetocategory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pagetocategory`;

CREATE TABLE `pagetocategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pageId` int(11) NOT NULL,
  `lang` varchar(2) NOT NULL,
  `categoryId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `pageId_lang_categoryId` (`pageId`,`lang`,`categoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
