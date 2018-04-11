-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.6.17


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema nimble
--

CREATE DATABASE IF NOT EXISTS nimble;
USE nimble;

--
-- Definition of table `invites`
--

DROP TABLE IF EXISTS `invites`;
CREATE TABLE `invites` (
  `inv_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `inv_idprt_from` int(10) unsigned NOT NULL,
  `inv_idprj` int(10) unsigned NOT NULL,
  `inv_userid` varchar(50) NOT NULL,
  PRIMARY KEY (`inv_id`),
  KEY `UNIQUE` (`inv_idprt_from`,`inv_idprj`,`inv_userid`),
  KEY `FK_invites_projects` (`inv_idprj`),
  CONSTRAINT `FK_invites_partners` FOREIGN KEY (`inv_idprt_from`) REFERENCES `partners` (`prt_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_invites_projects` FOREIGN KEY (`inv_idprj`) REFERENCES `projects` (`prj_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `invites`
--

/*!40000 ALTER TABLE `invites` DISABLE KEYS */;
INSERT INTO `invites` (`inv_id`,`inv_idprt_from`,`inv_idprj`,`inv_userid`) VALUES 
 (1,7,5,'3108');
/*!40000 ALTER TABLE `invites` ENABLE KEYS */;


--
-- Definition of table `partners`
--

DROP TABLE IF EXISTS `partners`;
CREATE TABLE `partners` (
  `prt_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `prt_username` varchar(50) NOT NULL,
  `prt_firstname` varchar(50) DEFAULT NULL,
  `prt_lastname` varchar(50) DEFAULT NULL,
  `prt_email` varchar(100) DEFAULT NULL,
  `prt_companyID` varchar(100) DEFAULT NULL,
  `prt_UserID` varchar(100) NOT NULL,
  PRIMARY KEY (`prt_id`),
  KEY `UNIQUE` (`prt_username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `partners`
--

/*!40000 ALTER TABLE `partners` DISABLE KEYS */;
INSERT INTO `partners` (`prt_id`,`prt_username`,`prt_firstname`,`prt_lastname`,`prt_email`,`prt_companyID`,`prt_UserID`) VALUES 
 (7,'ggariddi@dobi.it','Gabriele','Gariddi','ggariddi@dobi.it','2047','2045'),
 (8,'ssalvetti@dobi.it','Silvia','Salvetti','ssalvetti@dobi.it',NULL,'3108');
/*!40000 ALTER TABLE `partners` ENABLE KEYS */;


--
-- Definition of table `projects`
--

DROP TABLE IF EXISTS `projects`;
CREATE TABLE `projects` (
  `prj_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `prj_name` varchar(100) NOT NULL,
  `prj_idprt` int(10) unsigned NOT NULL,
  PRIMARY KEY (`prj_id`),
  KEY `UNIQUE` (`prj_name`),
  KEY `FK_projects_partners` (`prj_idprt`),
  CONSTRAINT `FK_projects_partners` FOREIGN KEY (`prj_idprt`) REFERENCES `partners` (`prt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `projects`
--

/*!40000 ALTER TABLE `projects` DISABLE KEYS */;
INSERT INTO `projects` (`prj_id`,`prj_name`,`prj_idprt`) VALUES 
 (5,'CAD1',7);
/*!40000 ALTER TABLE `projects` ENABLE KEYS */;


--
-- Definition of table `resources`
--

DROP TABLE IF EXISTS `resources`;
CREATE TABLE `resources` (
  `res_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `res_idprj` int(10) unsigned NOT NULL,
  `res_name` varchar(512) NOT NULL,
  `res_type` varchar(20) NOT NULL,
  `res_lastver` int(10) unsigned NOT NULL,
  `res_user` varchar(150) NOT NULL,
  `res_idres` int(10) unsigned DEFAULT NULL,
  `res_key` varchar(100) NOT NULL,
  PRIMARY KEY (`res_id`),
  KEY `FK_resources_resource` (`res_idres`),
  KEY `resourcesUX` (`res_idprj`,`res_key`),
  CONSTRAINT `FK_resources_projects` FOREIGN KEY (`res_idprj`) REFERENCES `projects` (`prj_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_resources_resource` FOREIGN KEY (`res_idres`) REFERENCES `resources` (`res_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `resources`
--

/*!40000 ALTER TABLE `resources` DISABLE KEYS */;
/*!40000 ALTER TABLE `resources` ENABLE KEYS */;


--
-- Definition of table `subscriptions`
--

DROP TABLE IF EXISTS `subscriptions`;
CREATE TABLE `subscriptions` (
  `sbp_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sbp_idprt` int(10) unsigned NOT NULL,
  `sbp_idprj` int(10) unsigned NOT NULL,
  PRIMARY KEY (`sbp_id`),
  KEY `UNIQUE` (`sbp_idprt`,`sbp_idprj`),
  KEY `FK_subscriptions_projects` (`sbp_idprj`),
  CONSTRAINT `FK_subscriptions_partners` FOREIGN KEY (`sbp_idprt`) REFERENCES `partners` (`prt_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_subscriptions_projects` FOREIGN KEY (`sbp_idprj`) REFERENCES `projects` (`prj_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `subscriptions`
--

/*!40000 ALTER TABLE `subscriptions` DISABLE KEYS */;
INSERT INTO `subscriptions` (`sbp_id`,`sbp_idprt`,`sbp_idprj`) VALUES 
 (2,7,5),
 (3,8,5);
/*!40000 ALTER TABLE `subscriptions` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
