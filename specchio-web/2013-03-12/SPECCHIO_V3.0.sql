CREATE DATABASE  IF NOT EXISTS `specchio` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `specchio`;
-- MySQL dump 10.13  Distrib 5.5.24, for osx10.5 (i386)
--
-- Host: localhost    Database: specchio
-- ------------------------------------------------------
-- Server version	5.5.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `attribute`
--

DROP TABLE IF EXISTS `attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute` (
  `attribute_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `category_id` int(11) NOT NULL,
  `default_unit_id` int(11) DEFAULT NULL,
  `default_storage_field` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`attribute_id`),
  KEY `fk_attribute_category1` (`category_id`),
  KEY `fk_attribute_category2` (`default_unit_id`),
  CONSTRAINT `fk_attribute_category1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON UPDATE NO ACTION,
  CONSTRAINT `fk_attribute_category2` FOREIGN KEY (`default_unit_id`) REFERENCES `unit` (`unit_id`) ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute`
--

LOCK TABLES `attribute` WRITE;
/*!40000 ALTER TABLE `attribute` DISABLE KEYS */;
INSERT INTO `attribute` VALUES (1,'Ambient Temperature',NULL,4,NULL,'double_val'),(2,'Air Pressure',NULL,4,NULL,'double_val'),(3,'Wind Speed',NULL,4,NULL,'double_val'),(4,'Wind Direction',NULL,4,NULL,'double_val'),(5,'Relative Humidity',NULL,4,NULL,'double_val'),(6,'Cloud Cover',NULL,4,NULL,'double_val'),(7,'Weather Conditions','Textual description of weather conditions',4,NULL,'string_val'),(8,'Automatic Dark Current Correction','Dark current has been compensated for by the instrument (ON/OFF)',5,NULL,'string_val'),(9,'Integration Time',NULL,5,NULL,'int_val'),(10,'Number of internal Scans',NULL,5,NULL,'int_val'),(11,'Gain_SWIR1',NULL,5,NULL,'int_val'),(12,'Gain_SWIR2',NULL,5,NULL,'int_val'),(13,'Offset_SWIR1',NULL,5,NULL,'int_val'),(14,'Offset_SWIR2',NULL,5,NULL,'int_val'),(15,'Capturing Software Name',NULL,5,NULL,'string_val'),(16,'Capturing Software Version',NULL,5,NULL,'string_val'),(17,'UniSpec Spectral Resampling',NULL,5,NULL,'string_val'),(18,'Instrument Channel','Channel designation for multi-channel instruments, e.g. irradiance and reflected radiance channels',5,NULL,'string_val'),(19,'Time since last DC','Time since last dark current measurement',5,NULL,'int_val'),(20,'File Version',NULL,3,NULL,'string_val'),(21,'Spectrum Number',NULL,3,NULL,'int_val'),(22,'File Name',NULL,3,NULL,'string_val'),(23,'Acquisition Time',NULL,3,NULL,'datetime_val'),(24,'Loading Time',NULL,3,NULL,'datetime_val'),(25,'File Comments',NULL,3,NULL,'string_val'),(26,'Chlorophyll Content',NULL,12,2,'double_val'),(27,'Specific Leaf Area','Calculated by: LeafArea[cm2]/DryMass[g]',12,1,'double_val'),(28,'DBH','Diameter at breast height',12,NULL,'double_val'),(29,'Height','Height of vegetation',12,NULL,'double_val'),(30,'Approx. Crown Diameter','',12,NULL,'double_val'),(31,'% Crown Cover','Crown Cover Percentage',12,NULL,'double_val'),(32,'Wet Weight','',12,NULL,'double_val'),(33,'Dry Weight','',12,NULL,'double_val'),(34,'Leaf Area','',12,3,'double_val'),(35,'Water Content','',12,4,'double_val'),(36,'Crown Class (FPMRIS)','SOP 13 Measuring a Large Tree Plot',12,NULL,'taxonomy_id'),(37,'Publication','Publication relevant to these spectral data',15,NULL,'string_val'),(38,'Citation','Publication to be cited when using these spectral data',15,NULL,'string_val'),(39,'ANDS Collection Key',NULL,16,NULL,'string_val'),(40,'Target ID',NULL,17,NULL,'string_val'),(41,'Target Description',NULL,17,NULL,'string_val'),(42,'Target Picture',NULL,13,NULL,'binary_val'),(43,'Sampling Setup Picture',NULL,13,NULL,'binary_val'),(44,'Sky Picture',NULL,13,NULL,'binary_val'),(45,'Sampling Environment Picture','Picture showing the general sampling environment, i.e. vicinity of the target',13,NULL,'binary_val'),(46,'Field Protocol',NULL,14,NULL,'binary_val'),(47,'Experimental Design',NULL,14,NULL,'binary_val'),(48,'Longitude',NULL,1,NULL,'double_val'),(49,'Latitude',NULL,1,NULL,'double_val'),(50,'Altitude',NULL,1,NULL,'double_val'),(51,'Location Name',NULL,1,NULL,'string_val'),(52,'FOV',NULL,2,NULL,'int_val'),(53,'Optics Name',NULL,2,NULL,'String_val'),(54,'Processing Level',NULL,18,NULL,'double_val'),(55,'DC Flag','Designates this spectrum as dark current spectrum',18,NULL,'int_val'),(56,'Processing Module','Name of processing module applied to spectrum',18,NULL,'string_val'),(57,'Processing Algorithm','Description of processing algorithm applied to spectrum',18,NULL,'string_val'),(58,'Source File','File that provided the original data (applies if data were processed outside of SPECCHIO)',18,NULL,'string_val'),(59,'Data Ingestion Notes','Notes produced by the data ingestion module during data loading into SPECCHIO',18,NULL,'string_val'),(60,'Garbage Flag','Designates this spectrum is garbage. This flag can be used to automatically exclude garbage spectra from processing.',18,NULL,'int_val'),(61,'Time Shift','Notes produced by the SPECCHIO time shift routine',18,NULL,'string_val'),(62,'Calibration Number',NULL,7,NULL,'int_val'),(63,'Extended Instrument Name',NULL,7,NULL,'string_val'),(64,'Instrument Serial Number',NULL,7,NULL,'string_val'),(65,'Campaign Name','Further specification of a particular campaign. Mainly used where a SPECCHIO campaign comprises several original sampling campaigns',9,NULL,'string_val'),(66,'Azimuth Sensor Type','',10,NULL,'string_val'),(67,'Integrating Sphere','',10,NULL,'taxonomy_id'),(68,'Light Source Parameters','Settings of artificial light source',10,NULL,'string_val'),(69,'White Reference Target','Description of white reference target',10,NULL,'string_val'),(70,'Keyword',NULL,11,NULL,'string_val'),(71,'Common',NULL,8,NULL,'string_val'),(72,'Latin',NULL,8,NULL,'string_val'),(73,'ENVI Hdr','Name extracted from ENVI header',8,NULL,'string_val'),(74,'Illumination Azimuth',NULL,6,NULL,'double_val'),(75,'Illumination Zenith',NULL,6,NULL,'double_val'),(76,'Illumination Distance',NULL,6,NULL,'double_val'),(77,'Sensor Azimuth',NULL,6,NULL,'double_val'),(78,'Sensor Zenith',NULL,6,NULL,'double_val'),(79,'Sensor Distance',NULL,6,NULL,'double_val'),(80,'Investigator','Investigator of these data; fallback if not definable via existing SPECCHIO users',19,NULL,'string_val'),(81,'Polarization','Polarisation `description`, e.g. Horizontal or Vertical',20,NULL,'string_val'),(82,'Polarization Direction','Polarisation direction as degrees',20,NULL,'double_val');
/*!40000 ALTER TABLE `attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `calibration`
--

DROP TABLE IF EXISTS `calibration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `calibration` (
  `calibration_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `calibration_no` int(10) unsigned DEFAULT NULL,
  `calibration_date` date DEFAULT NULL,
  `comments` varchar(200) DEFAULT NULL,
  `calibrated_by` int(10) unsigned DEFAULT NULL,
  `reference_id` int(11) DEFAULT NULL,
  `instrument_id` int(10) unsigned DEFAULT NULL,
  `cal_factors` int(11) DEFAULT NULL,
  `uncertainty` int(11) DEFAULT NULL,
  PRIMARY KEY (`calibration_id`),
  KEY `calibration_fk_2` (`calibrated_by`),
  KEY `calibration_fk_3` (`reference_id`),
  KEY `calibration_fk_1` (`instrument_id`),
  KEY `calibration_fk_4` (`cal_factors`),
  KEY `calibration_fk_5` (`uncertainty`),
  CONSTRAINT `calibration_fk_1` FOREIGN KEY (`instrument_id`) REFERENCES `instrument` (`instrument_id`),
  CONSTRAINT `calibration_fk_2` FOREIGN KEY (`calibrated_by`) REFERENCES `institute` (`institute_id`),
  CONSTRAINT `calibration_fk_3` FOREIGN KEY (`reference_id`) REFERENCES `reference` (`reference_id`),
  CONSTRAINT `calibration_fk_4` FOREIGN KEY (`cal_factors`) REFERENCES `instrumentation_factors` (`instrumentation_factors_id`),
  CONSTRAINT `calibration_fk_5` FOREIGN KEY (`uncertainty`) REFERENCES `instrumentation_factors` (`instrumentation_factors_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 93184 kB; (instrument_id) REFER specchio/instru';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calibration`
--

LOCK TABLES `calibration` WRITE;
/*!40000 ALTER TABLE `calibration` DISABLE KEYS */;
/*!40000 ALTER TABLE `calibration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campaign`
--

DROP TABLE IF EXISTS `campaign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `campaign` (
  `campaign_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `path` varchar(500) NOT NULL,
  `quality_comply` tinyint(1) DEFAULT NULL,
  `user_id` int(10) unsigned NOT NULL DEFAULT '0',
  `research_group_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`campaign_id`),
  KEY `FK_campaign_user_id` (`user_id`),
  KEY `FK_campaign_research_group_id` (`research_group_id`),
  CONSTRAINT `FK_campaign_research_group_id` FOREIGN KEY (`research_group_id`) REFERENCES `research_group` (`research_group_id`),
  CONSTRAINT `FK_campaign_user_id` FOREIGN KEY (`user_id`) REFERENCES `specchio_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=FIXED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campaign`
--

LOCK TABLES `campaign` WRITE;
/*!40000 ALTER TABLE `campaign` DISABLE KEYS */;
/*!40000 ALTER TABLE `campaign` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER  `specchio`.`campaign_tr` BEFORE INSERT ON `campaign` FOR EACH ROW set new.user_id = (select user_id from specchio_user where user = SUBSTRING_INDEX((select user()), '@', 1)) */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Temporary table structure for view `campaign_view`
--

DROP TABLE IF EXISTS `campaign_view`;
/*!50001 DROP VIEW IF EXISTS `campaign_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `campaign_view` (
  `campaign_id` int(11),
  `name` varchar(45),
  `description` varchar(200),
  `path` varchar(500),
  `quality_comply` tinyint(1),
  `user_id` int(10) unsigned,
  `research_group_id` int(10)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `string_val` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Location',''),(2,'Optics',''),(3,'General',''),(4,'Environmental Conditions',''),(5,'Instrument Settings',''),(6,'Sampling Geometry',''),(7,'Instrument',''),(8,'Names',''),(9,'Campaign Details',''),(10,'Instrumentation',''),(11,'Keywords',''),(12,'Vegetation Biophysical Variables',''),(13,'Pictures',''),(14,'PDFs',''),(15,'Scientific References',''),(16,'Data Portal',''),(17,'Generic Target Properties',''),(18,'Processing',''),(19,'Personnel',''),(20,'Illumination','');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cloud_cover`
--

DROP TABLE IF EXISTS `cloud_cover`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cloud_cover` (
  `cloud_cover_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cover_in_octas` int(10) unsigned DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`cloud_cover_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cloud_cover`
--

LOCK TABLES `cloud_cover` WRITE;
/*!40000 ALTER TABLE `cloud_cover` DISABLE KEYS */;
INSERT INTO `cloud_cover` VALUES (1,0,'Clear'),(2,1,'1 okta or less, but not zero'),(3,2,'2 oktas'),(4,3,'3 oktas'),(5,4,'4 oktas'),(6,5,'5 oktas'),(7,6,'6 oktas'),(8,7,'7 oktas or more, but not 8 oktas'),(9,8,'8 oktas');
/*!40000 ALTER TABLE `cloud_cover` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `country_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=217 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` VALUES (1,'United States'),(2,'United Kingdom'),(3,'France'),(4,'Switzerland'),(5,'Afghanistan'),(6,'Albania'),(7,'Algeria'),(8,'American Somoa'),(9,'Andorra'),(10,'Angola'),(11,'Anguilla'),(12,'Antartica'),(13,'Antigua & Barbuda'),(14,'Argentina'),(15,'Armenia'),(16,'Aruba'),(17,'Australia'),(18,'Austria'),(19,'Azerbaijan'),(20,'Azores'),(21,'Bahamas'),(22,'Bahrain'),(23,'Balearic Islands'),(24,'Bangladesh'),(25,'Barbados'),(26,'Belarus'),(27,'Belgium'),(28,'Belize'),(29,'Benin'),(30,'Bermuda'),(31,'Bhutan'),(32,'Bolivia'),(33,'Bonaire'),(34,'Bosnia & Herzegovinia'),(35,'Botswana'),(36,'Brazil'),(37,'Brunei'),(38,'Bulgaria'),(39,'BurkinaFaso'),(40,'Burundi'),(41,'Cambodia'),(42,'Cameroon'),(43,'Canada'),(44,'Canary Islands'),(45,'Cape Verde'),(46,'Cayman Islands'),(47,'Central Africa Republic'),(48,'Chad'),(49,'Chile'),(50,'China'),(51,'Colombia'),(52,'Comoros'),(53,'Congo'),(54,'CostaRica'),(55,'Croatia'),(56,'Cuba'),(57,'Curacao'),(58,'Cyprus'),(59,'Czech Republic'),(60,'Denmark'),(61,'Djibouti'),(62,'Dominican Republic'),(63,'Ecuador'),(64,'Egypt'),(65,'ElSalvador'),(66,'Equatorial Guinea'),(67,'Eritrea'),(68,'Estonia'),(69,'Ethiopia'),(70,'Falkland Islands'),(71,'Fiji'),(72,'Finland'),(73,'French Guiana'),(74,'Gambia'),(75,'Georgia'),(76,'Germany'),(77,'Ghana'),(78,'Gibraltar'),(79,'Greece'),(80,'Greenland'),(81,'Grenada'),(82,'Guadeloupe'),(83,'Guatemala'),(84,'Guernsey'),(85,'Guinea Bissau'),(86,'Guyana'),(87,'Haiti'),(88,'Honduras'),(89,'HongKong'),(90,'Hungary'),(91,'Iceland'),(92,'India'),(93,'Indonesia'),(94,'Iran'),(95,'Iraq'),(96,'Ireland'),(97,'Israel'),(98,'Italy'),(99,'IvoryCoast'),(100,'Jamaica'),(101,'Japan'),(102,'Jersey'),(103,'Jordan'),(104,'Kazakhstan'),(105,'Kenya'),(106,'Kuwait'),(107,'Kyrgyzstan'),(108,'Laos'),(109,'Latvia'),(110,'Lebanon'),(111,'Lesotho'),(112,'Liberia'),(113,'Libya'),(114,'Liechtenstein'),(115,'Lithuania'),(116,'Luxembourg'),(117,'Macau'),(118,'Macedonia'),(119,'Madagascar'),(120,'Maderia'),(121,'Malawi'),(122,'Malaysia'),(123,'Maldives'),(124,'Mali'),(125,'Malta'),(126,'Martinique'),(127,'Mauritania'),(128,'Mauritius'),(129,'Mexico'),(130,'Moldova'),(131,'Monaco'),(132,'Mongolia'),(133,'Montserrat'),(134,'Morocco'),(135,'Mozambique'),(136,'Myanmar'),(137,'Myanmer'),(138,'Namibia'),(139,'Nauru'),(140,'Nepal'),(141,'Netherlands'),(142,'New Caledonia'),(143,'New Zealand'),(144,'Nicaragua'),(145,'Niger'),(146,'Nigeria'),(147,'North Korea'),(148,'Norway'),(149,'Oman'),(150,'Pakistan'),(151,'Panama'),(152,'Papua New Guinea'),(153,'Paraguay'),(154,'Peru'),(155,'Philippines'),(156,'Poland'),(157,'Portugal'),(158,'PuertoRico'),(159,'Qatar'),(160,'Reunion'),(161,'Romania'),(162,'Russia'),(163,'Rwanda'),(164,'Saint Eustatius'),(165,'Saint Kitts and Nevis'),(166,'Saint Lucia'),(167,'Saint Vincent and the Grenadines'),(168,'San Marino'),(169,'Sao Tome'),(170,'Saudi Arabia'),(171,'Senegal'),(172,'Seychelles'),(173,'SierraLeone'),(174,'Singapore'),(175,'Slovakia'),(176,'Slovenia'),(177,'Solomon Islands'),(178,'Somalia'),(179,'South Africa'),(180,'South Korea'),(181,'Spain'),(182,'Sri Lanka'),(183,'St Maarten'),(184,'Sudan'),(185,'Suriname'),(186,'Swaziland'),(187,'Sweden'),(188,'Syria'),(189,'Taiwan'),(190,'Tajikistan'),(191,'Tanzania'),(192,'Thailand'),(193,'Togo'),(194,'Trinidad and Tobago'),(195,'Tunisia'),(196,'Turkey'),(197,'Turkmenistan'),(198,'Turks and Caicos Islands'),(199,'Tuvalu'),(200,'Uganda'),(201,'Ukraine'),(202,'UnitedArabEmirates'),(203,'Uruguay'),(204,'Uzbekistan'),(205,'Vanuatu'),(206,'VaticanCity'),(207,'Venezuela'),(208,'Vietnam'),(209,'Virgin Islands - British'),(210,'Virgin Islands - US'),(211,'Yemen'),(212,'Yugoslavia'),(213,'Zaire (Congo)'),(214,'Zambia'),(215,'Zanzibar Island'),(216,'Zimbabwe');
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datalink_type`
--

DROP TABLE IF EXISTS `datalink_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `datalink_type` (
  `datalink_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`datalink_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datalink_type`
--

LOCK TABLES `datalink_type` WRITE;
/*!40000 ALTER TABLE `datalink_type` DISABLE KEYS */;
INSERT INTO `datalink_type` VALUES (1,'Photometer data'),(2,'Spectralon data'),(3,'Radiance data'),(4,'Cosine receptor data');
/*!40000 ALTER TABLE `datalink_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eav`
--

DROP TABLE IF EXISTS `eav`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eav` (
  `eav_id` int(11) NOT NULL AUTO_INCREMENT,
  `attribute_id` int(11) NOT NULL,
  `int_val` int(11) DEFAULT NULL,
  `double_val` double DEFAULT NULL,
  `string_val` varchar(500) DEFAULT NULL,
  `binary_val` mediumblob,
  `datetime_val` datetime DEFAULT NULL,
  `unit_id` int(11) NOT NULL,
  `campaign_id` int(11) DEFAULT NULL,
  `taxonomy_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`eav_id`),
  KEY `fk_eav_attribute1` (`attribute_id`),
  KEY `fk_eav_unit1` (`unit_id`),
  KEY `FK_eav_campaign_id` (`campaign_id`),
  KEY `taxonomy_id_fk` (`taxonomy_id`),
  CONSTRAINT `taxonomy_id_fk` FOREIGN KEY (`taxonomy_id`) REFERENCES `taxonomy` (`taxonomy_id`),
  CONSTRAINT `fk_eav_attribute1` FOREIGN KEY (`attribute_id`) REFERENCES `attribute` (`attribute_id`) ON UPDATE NO ACTION,
  CONSTRAINT `FK_eav_campaign_id` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`campaign_id`),
  CONSTRAINT `fk_eav_unit1` FOREIGN KEY (`unit_id`) REFERENCES `unit` (`unit_id`) ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eav`
--

LOCK TABLES `eav` WRITE;
/*!40000 ALTER TABLE `eav` DISABLE KEYS */;
/*!40000 ALTER TABLE `eav` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `eav_view`
--

DROP TABLE IF EXISTS `eav_view`;
/*!50001 DROP VIEW IF EXISTS `eav_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `eav_view` (
  `eav_id` int(11),
  `attribute_id` int(11),
  `int_val` int(11),
  `double_val` double,
  `string_val` varchar(500),
  `binary_val` mediumblob,
  `datetime_val` datetime,
  `unit_id` int(11),
  `campaign_id` int(11),
  `taxonomy_id` int(11)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `file_format`
--

DROP TABLE IF EXISTS `file_format`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_format` (
  `file_format_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `file_extension` varchar(10) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`file_format_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_format`
--

LOCK TABLES `file_format` WRITE;
/*!40000 ALTER TABLE `file_format` DISABLE KEYS */;
INSERT INTO `file_format` VALUES (1,'ASD Binary',NULL,'ASD Binary files'),(2,'ENVI SLB','slb','ENVI Spectral Library file'),(3,'GER Signature File',NULL,'GER Signature file (see GER 3700 User Manual R.3.2)'),(4,'MFR Out File','out','Yankee Environmental System Inc MFR Out file'),(5,'TXT File','txt','Space separated text file'),(6,'APOGEE','TRM','APOGEE formatted file'),(7,'SVC HR 1024','SIG','Spectra Vista HR-1024 Signature File'),(8,'HDF5 FGI','h5','HDF5 file generated by FGI'),(9,'OOSpectraSuite','csv','Ocean Optics SpectraSuite Data File');
/*!40000 ALTER TABLE `file_format` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goniometer`
--

DROP TABLE IF EXISTS `goniometer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goniometer` (
  `goniometer_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`goniometer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goniometer`
--

LOCK TABLES `goniometer` WRITE;
/*!40000 ALTER TABLE `goniometer` DISABLE KEYS */;
INSERT INTO `goniometer` VALUES (1,'LAGOS',NULL),(2,'FIGOS',NULL),(3,'FIGIFIGO',NULL);
/*!40000 ALTER TABLE `goniometer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hierarchy_level`
--

DROP TABLE IF EXISTS `hierarchy_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hierarchy_level` (
  `hierarchy_level_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `parent_level_id` int(11) DEFAULT NULL,
  `campaign_id` int(11) NOT NULL,
  PRIMARY KEY (`hierarchy_level_id`),
  KEY `FK_hierarchy_level_1` (`parent_level_id`),
  KEY `FK_hierarchy_level_2` (`campaign_id`),
  CONSTRAINT `FK_hierarchy_level_parent` FOREIGN KEY (`parent_level_id`) REFERENCES `hierarchy_level` (`hierarchy_level_id`),
  CONSTRAINT `hierarchy_level_ibfk_1` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`campaign_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hierarchy_level`
--

LOCK TABLES `hierarchy_level` WRITE;
/*!40000 ALTER TABLE `hierarchy_level` DISABLE KEYS */;
/*!40000 ALTER TABLE `hierarchy_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `hierarchy_level_view`
--

DROP TABLE IF EXISTS `hierarchy_level_view`;
/*!50001 DROP VIEW IF EXISTS `hierarchy_level_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `hierarchy_level_view` (
  `hierarchy_level_id` int(11),
  `name` varchar(45),
  `description` varchar(1000),
  `parent_level_id` int(11),
  `campaign_id` int(11)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `hierarchy_level_x_spectrum`
--

DROP TABLE IF EXISTS `hierarchy_level_x_spectrum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hierarchy_level_x_spectrum` (
  `hierarchy_level_id` int(11) NOT NULL,
  `spectrum_id` int(11) NOT NULL,
  `campaign_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`hierarchy_level_id`,`spectrum_id`),
  KEY `hierarchy_level_x_spectrum_fk1` (`hierarchy_level_id`),
  KEY `hierarchy_level_x_spectrum_fk2` (`spectrum_id`),
  KEY `FK_hierarchy_level_x_spectrum_campaign_id` (`campaign_id`),
  CONSTRAINT `FK_hierarchy_level_x_spectrum_campaign_id` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`campaign_id`),
  CONSTRAINT `hierarchy_level_x_spectrum_fk1` FOREIGN KEY (`hierarchy_level_id`) REFERENCES `hierarchy_level` (`hierarchy_level_id`) ON UPDATE NO ACTION,
  CONSTRAINT `hierarchy_level_x_spectrum_fk2` FOREIGN KEY (`spectrum_id`) REFERENCES `spectrum` (`spectrum_id`) ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hierarchy_level_x_spectrum`
--

LOCK TABLES `hierarchy_level_x_spectrum` WRITE;
/*!40000 ALTER TABLE `hierarchy_level_x_spectrum` DISABLE KEYS */;
/*!40000 ALTER TABLE `hierarchy_level_x_spectrum` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `specchio`.`hierarchy_level_x_spectrum_tr`
	BEFORE INSERT ON `specchio`.`hierarchy_level_x_spectrum`
	FOR EACH ROW SET new.`campaign_id` = (
		SELECT `campaign_id` FROM `specchio`.`spectrum` WHERE `spectrum`.`spectrum_id` = new.`spectrum_id`
	) */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Temporary table structure for view `hierarchy_level_x_spectrum_view`
--

DROP TABLE IF EXISTS `hierarchy_level_x_spectrum_view`;
/*!50001 DROP VIEW IF EXISTS `hierarchy_level_x_spectrum_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `hierarchy_level_x_spectrum_view` (
  `hierarchy_level_id` int(11),
  `spectrum_id` int(11),
  `campaign_id` int(11)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `illumination_source`
--

DROP TABLE IF EXISTS `illumination_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `illumination_source` (
  `illumination_source_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`illumination_source_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `illumination_source`
--

LOCK TABLES `illumination_source` WRITE;
/*!40000 ALTER TABLE `illumination_source` DISABLE KEYS */;
INSERT INTO `illumination_source` VALUES (1,'Sun',NULL),(2,'Oriel 1000 W QTH','research light source');
/*!40000 ALTER TABLE `illumination_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `institute`
--

DROP TABLE IF EXISTS `institute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `institute` (
  `institute_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `department` varchar(100) DEFAULT NULL,
  `street` varchar(100) DEFAULT NULL,
  `street_no` varchar(45) DEFAULT NULL,
  `po_code` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `country_id` int(10) unsigned DEFAULT NULL,
  `www` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`institute_id`),
  KEY `FK_institute_1` (`country_id`),
  CONSTRAINT `FK_institute_1` FOREIGN KEY (`country_id`) REFERENCES `country` (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `institute`
--

LOCK TABLES `institute` WRITE;
/*!40000 ALTER TABLE `institute` DISABLE KEYS */;
INSERT INTO `institute` VALUES (1,'University of Zurich','RSL',NULL,NULL,NULL,NULL,4,NULL),(2,'Massey University','INR',NULL,NULL,NULL,NULL,143,NULL),(3,'Finnish Geodetic Institute','Remote Sensing',NULL,NULL,NULL,NULL,72,NULL);
/*!40000 ALTER TABLE `institute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instrument`
--

DROP TABLE IF EXISTS `instrument`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instrument` (
  `instrument_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sensor_id` int(11) NOT NULL,
  `serial_number` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `institute_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`instrument_id`),
  KEY `sensor_id` (`sensor_id`),
  KEY `instrument_ibfk_2` (`institute_id`),
  CONSTRAINT `instrument_ibfk_1` FOREIGN KEY (`sensor_id`) REFERENCES `sensor` (`sensor_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `instrument_ibfk_2` FOREIGN KEY (`institute_id`) REFERENCES `institute` (`institute_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instrument`
--

LOCK TABLES `instrument` WRITE;
/*!40000 ALTER TABLE `instrument` DISABLE KEYS */;
INSERT INTO `instrument` VALUES (1,3,'6421','INR ASD',2),(2,4,'1002','RSL GER 3700',1),(3,3,'16006','RSL ASD FS3 1',1),(4,3,'16007','RSL ASD FS3 2',1),(5,3,'6213','FGI ASD',3);
/*!40000 ALTER TABLE `instrument` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instrument_x_picture`
--

DROP TABLE IF EXISTS `instrument_x_picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instrument_x_picture` (
  `instrumentation_picture_id` int(10) unsigned NOT NULL,
  `instrument_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`instrumentation_picture_id`,`instrument_id`),
  KEY `instrument_x_picture_fk2` (`instrument_id`),
  CONSTRAINT `instrument_x_picture_fk1` FOREIGN KEY (`instrumentation_picture_id`) REFERENCES `instrumentation_picture` (`instrumentation_picture_id`),
  CONSTRAINT `instrument_x_picture_fk2` FOREIGN KEY (`instrument_id`) REFERENCES `instrument` (`instrument_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instrument_x_picture`
--

LOCK TABLES `instrument_x_picture` WRITE;
/*!40000 ALTER TABLE `instrument_x_picture` DISABLE KEYS */;
/*!40000 ALTER TABLE `instrument_x_picture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instrumentation_factors`
--

DROP TABLE IF EXISTS `instrumentation_factors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instrumentation_factors` (
  `instrumentation_factors_id` int(11) NOT NULL AUTO_INCREMENT,
  `measurement_unit_id` int(10) unsigned DEFAULT NULL,
  `measurement` blob,
  `loading_date` datetime NOT NULL,
  `sensor_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`instrumentation_factors_id`),
  KEY `ifac_sensor_fk` (`sensor_id`),
  KEY `ifac_measurement_unit` (`measurement_unit_id`),
  CONSTRAINT `ifac_measurement_unit` FOREIGN KEY (`measurement_unit_id`) REFERENCES `measurement_unit` (`measurement_unit_id`),
  CONSTRAINT `ifac_sensor_fk` FOREIGN KEY (`sensor_id`) REFERENCES `sensor` (`sensor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instrumentation_factors`
--

LOCK TABLES `instrumentation_factors` WRITE;
/*!40000 ALTER TABLE `instrumentation_factors` DISABLE KEYS */;
/*!40000 ALTER TABLE `instrumentation_factors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instrumentation_picture`
--

DROP TABLE IF EXISTS `instrumentation_picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instrumentation_picture` (
  `instrumentation_picture_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) DEFAULT NULL,
  `image_data` longblob,
  PRIMARY KEY (`instrumentation_picture_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instrumentation_picture`
--

LOCK TABLES `instrumentation_picture` WRITE;
/*!40000 ALTER TABLE `instrumentation_picture` DISABLE KEYS */;
/*!40000 ALTER TABLE `instrumentation_picture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `landcover`
--

DROP TABLE IF EXISTS `landcover`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `landcover` (
  `landcover_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `level_no` int(10) unsigned DEFAULT NULL,
  `cover_no` int(10) unsigned DEFAULT NULL,
  `cover_desc` varchar(255) DEFAULT NULL,
  `upper_level_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`landcover_id`),
  KEY `FK_landcover_1` (`upper_level_id`),
  CONSTRAINT `FK_landcover_1` FOREIGN KEY (`upper_level_id`) REFERENCES `landcover` (`landcover_id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `landcover`
--

LOCK TABLES `landcover` WRITE;
/*!40000 ALTER TABLE `landcover` DISABLE KEYS */;
INSERT INTO `landcover` VALUES (2,1,1,'Artificial Surfaces',NULL),(3,1,2,'Agricultural Areas',NULL),(4,1,3,'Forest and Semi-Natural Areas',NULL),(5,1,4,'Wetlands',NULL),(6,1,5,'Water Bodies',NULL),(11,2,11,'Urban fabric',2),(12,2,12,'Industrial, commercial and transport units',2),(13,2,13,'Mine, dump and construction sites',2),(14,2,14,'Artificial, non-agricultural vegetated areas',2),(15,2,21,'Arable Land',3),(16,2,22,'Permanent Crops',3),(17,2,23,'Pasture',3),(18,2,24,'Heterogeneous agricultural areas',3),(19,2,31,'Forests',4),(20,2,32,'Scrub and/or herbaceous associations',4),(21,2,33,'Open spaces with little or no vegetation',4),(22,2,41,'Inland wetlands',5),(23,2,42,'Marine wetlands',5),(24,2,51,'Inland waters',6),(25,2,52,'Marine waters',6),(27,3,111,'Continuous urban fabric',11),(28,3,112,'Discontinuous urban fabric',11),(29,3,121,'Industrial or commercial units',12),(30,3,122,'Road and rail networks and associated land',12),(31,3,123,'Port areas',12),(32,3,124,'Airports',12),(33,3,131,'Mineral extraction sites',13),(34,3,132,'Dump sites',13),(35,3,133,'Construction sites',13),(36,3,141,'Green urban areas',14),(37,3,142,'Port and leisure facilities',14),(38,3,211,'Non-irrigated arable land',15),(39,3,212,'Permanently irrigated land',15),(40,3,213,'Rice fields',15),(41,3,221,'Vineyards',16),(42,3,222,'Fruit trees and berry plantations',16),(43,3,223,'Olive groves',16),(44,3,231,'Pastures',17),(45,3,241,'Annual crops associated with permanent crops',18),(46,3,242,'Complex cultivation patterns',18),(47,3,243,'Mainly agricultural land with significant areas of natural vegetation',18),(48,3,244,'Agro-forestry areas',18),(49,3,311,'Deciduous forest',19),(50,3,312,'Coniferous forest',19),(51,3,313,'Mixed forest',19),(52,3,321,'Natural grassland',20),(53,3,322,'Moors and heathland',20),(54,3,323,'Sclerophyllous vegetation',20),(55,3,324,'Transitional woodland-scrub',20),(56,3,331,'Beaches, dunes, sands',21),(57,3,332,'Bare rocks',21),(58,3,333,'Sparsely vegetated areas',21),(59,3,334,'Burnt areas',21),(60,3,335,'Glaciers and perpetual snow',21),(61,3,411,'Inland marshes',22),(62,3,412,'Peat bogs',22),(63,3,421,'Salt marshes',23),(64,3,422,'Salines',23),(65,3,423,'Intertidal flats',23),(66,3,511,'Water courses',24),(67,3,512,'Water bodies',24),(68,3,521,'Coastal lagoons',25),(69,3,522,'Estuaries',25),(70,3,523,'Sea and ocean',25);
/*!40000 ALTER TABLE `landcover` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manufacturer`
--

DROP TABLE IF EXISTS `manufacturer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manufacturer` (
  `manufacturer_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `www` varchar(100) DEFAULT NULL,
  `short_name` varchar(20) NOT NULL,
  PRIMARY KEY (`manufacturer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manufacturer`
--

LOCK TABLES `manufacturer` WRITE;
/*!40000 ALTER TABLE `manufacturer` DISABLE KEYS */;
INSERT INTO `manufacturer` VALUES (1,'Analytical Spectral Devices Inc (ASD)','www.asdi.com','ASD'),(2,'GER',NULL,'GER'),(3,'YES',NULL,'YES'),(4,'Beckman',NULL,'BECKMAN'),(5,'Labsphere Inc','www.labsphere.com','LABSPHERE'),(6,'APOGEE','www.apogee-inst.com','APOGEE'),(7,'Spectra Vista','www.spectravista.com/','SVC'),(8,'Ocean Optics','www.oceanoptics.com','OceanOptics');
/*!40000 ALTER TABLE `manufacturer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `measurement_type`
--

DROP TABLE IF EXISTS `measurement_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `measurement_type` (
  `measurement_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`measurement_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `measurement_type`
--

LOCK TABLES `measurement_type` WRITE;
/*!40000 ALTER TABLE `measurement_type` DISABLE KEYS */;
INSERT INTO `measurement_type` VALUES (1,'Bidirectional (CASE 1)',NULL),(2,'Directional-conical (CASE 2)',NULL),(3,'Directional-hemispherical (CASE 3)',NULL),(4,'Conical-directional (CASE 4)',NULL),(5,'Biconical (CASE 5)',NULL),(6,'Conical-hemispherical (CASE 6)',NULL),(7,'Hemispherical-directional (CASE 7)',NULL),(8,'Hemispherical-conical (CASE 8)',NULL),(9,'Bihemispherical (CASE 9)',NULL);
/*!40000 ALTER TABLE `measurement_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `measurement_unit`
--

DROP TABLE IF EXISTS `measurement_unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `measurement_unit` (
  `measurement_unit_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `ASD_coding` int(10) unsigned NOT NULL,
  PRIMARY KEY (`measurement_unit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `measurement_unit`
--

LOCK TABLES `measurement_unit` WRITE;
/*!40000 ALTER TABLE `measurement_unit` DISABLE KEYS */;
INSERT INTO `measurement_unit` VALUES (1,'Reflectance',1),(2,'Radiance',2),(3,'Absorbance',8),(4,'Transmittance',6),(5,'DN',0),(6,'Wavelength',100),(7,'Mueller10',110),(8,'Mueller20',120),(9,'Irradiance',4);
/*!40000 ALTER TABLE `measurement_unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quality_level`
--

DROP TABLE IF EXISTS `quality_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quality_level` (
  `quality_level_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`quality_level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality_level`
--

LOCK TABLES `quality_level` WRITE;
/*!40000 ALTER TABLE `quality_level` DISABLE KEYS */;
INSERT INTO `quality_level` VALUES (1,'A',NULL),(2,'B',NULL);
/*!40000 ALTER TABLE `quality_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reference`
--

DROP TABLE IF EXISTS `reference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reference` (
  `reference_id` int(11) NOT NULL AUTO_INCREMENT,
  `serial_number` varchar(45) DEFAULT NULL,
  `comments` varchar(100) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `reference_brand_id` int(11) DEFAULT NULL,
  `institute_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`reference_id`),
  KEY `reference_brand_fk` (`reference_brand_id`),
  KEY `reference_fk_2` (`institute_id`),
  CONSTRAINT `reference_brand_fk` FOREIGN KEY (`reference_brand_id`) REFERENCES `reference_brand` (`reference_brand_id`),
  CONSTRAINT `reference_fk_2` FOREIGN KEY (`institute_id`) REFERENCES `institute` (`institute_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reference`
--

LOCK TABLES `reference` WRITE;
/*!40000 ALTER TABLE `reference` DISABLE KEYS */;
/*!40000 ALTER TABLE `reference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reference_brand`
--

DROP TABLE IF EXISTS `reference_brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reference_brand` (
  `reference_brand_id` int(11) NOT NULL AUTO_INCREMENT,
  `reference_type_id` int(11) NOT NULL,
  `manufacturer_id` int(11) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`reference_brand_id`),
  KEY `manufacturer_ref__fk` (`manufacturer_id`),
  KEY `reference_type_fk` (`reference_type_id`),
  CONSTRAINT `manufacturer_ref__fk` FOREIGN KEY (`manufacturer_id`) REFERENCES `manufacturer` (`manufacturer_id`),
  CONSTRAINT `reference_type_fk` FOREIGN KEY (`reference_type_id`) REFERENCES `reference_type` (`reference_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reference_brand`
--

LOCK TABLES `reference_brand` WRITE;
/*!40000 ALTER TABLE `reference_brand` DISABLE KEYS */;
INSERT INTO `reference_brand` VALUES (1,1,5,'Spectralon');
/*!40000 ALTER TABLE `reference_brand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reference_type`
--

DROP TABLE IF EXISTS `reference_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reference_type` (
  `reference_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `comments` int(11) DEFAULT NULL,
  PRIMARY KEY (`reference_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reference_type`
--

LOCK TABLES `reference_type` WRITE;
/*!40000 ALTER TABLE `reference_type` DISABLE KEYS */;
INSERT INTO `reference_type` VALUES (1,'White reference panel',NULL);
/*!40000 ALTER TABLE `reference_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reference_x_picture`
--

DROP TABLE IF EXISTS `reference_x_picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reference_x_picture` (
  `instrumentation_picture_id` int(10) unsigned NOT NULL,
  `reference_id` int(11) NOT NULL,
  PRIMARY KEY (`instrumentation_picture_id`,`reference_id`),
  KEY `reference_x_picture_gear_fk` (`reference_id`),
  CONSTRAINT `reference_x_picture_gear_fk` FOREIGN KEY (`reference_id`) REFERENCES `reference` (`reference_id`),
  CONSTRAINT `reference_x_picture_pic_fk` FOREIGN KEY (`instrumentation_picture_id`) REFERENCES `instrumentation_picture` (`instrumentation_picture_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reference_x_picture`
--

LOCK TABLES `reference_x_picture` WRITE;
/*!40000 ALTER TABLE `reference_x_picture` DISABLE KEYS */;
/*!40000 ALTER TABLE `reference_x_picture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `research_group`
--

DROP TABLE IF EXISTS `research_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `research_group` (
  `research_group_id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`research_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `research_group`
--

LOCK TABLES `research_group` WRITE;
/*!40000 ALTER TABLE `research_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `research_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `research_group_members`
--

DROP TABLE IF EXISTS `research_group_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `research_group_members` (
  `research_group_id` int(10) NOT NULL DEFAULT '0',
  `member_id` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`research_group_id`,`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `research_group_members`
--

LOCK TABLES `research_group_members` WRITE;
/*!40000 ALTER TABLE `research_group_members` DISABLE KEYS */;
/*!40000 ALTER TABLE `research_group_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `research_group_members_view`
--

DROP TABLE IF EXISTS `research_group_members_view`;
/*!50001 DROP VIEW IF EXISTS `research_group_members_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `research_group_members_view` (
  `research_group_id` int(10),
  `member_id` int(10)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `research_group_view`
--

DROP TABLE IF EXISTS `research_group_view`;
/*!50001 DROP VIEW IF EXISTS `research_group_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `research_group_view` (
  `research_group_id` int(10),
  `name` varchar(100)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `sampling_environment`
--

DROP TABLE IF EXISTS `sampling_environment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sampling_environment` (
  `sampling_environment_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sampling_environment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sampling_environment`
--

LOCK TABLES `sampling_environment` WRITE;
/*!40000 ALTER TABLE `sampling_environment` DISABLE KEYS */;
INSERT INTO `sampling_environment` VALUES (1,'Field',NULL),(2,'Laboratory',NULL),(3,'Model',NULL),(4,'HDRF to BRF',NULL);
/*!40000 ALTER TABLE `sampling_environment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schema_info`
--

DROP TABLE IF EXISTS `schema_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schema_info` (
  `schema_info_id` int(11) NOT NULL AUTO_INCREMENT,
  `version` float DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`schema_info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schema_info`
--

LOCK TABLES `schema_info` WRITE;
/*!40000 ALTER TABLE `schema_info` DISABLE KEYS */;
INSERT INTO `schema_info` VALUES (1,2,'2009-05-25'),(2,2.1,'2010-09-08'),(3,2.2,'2012-05-31');
/*!40000 ALTER TABLE `schema_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensor`
--

DROP TABLE IF EXISTS `sensor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sensor` (
  `sensor_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `response_type` varchar(45) DEFAULT NULL,
  `sensor_type_no` int(11) NOT NULL,
  `no_of_channels` int(11) DEFAULT NULL,
  `manufacturer_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`sensor_id`),
  KEY `manufacturer_fk` (`manufacturer_id`),
  CONSTRAINT `manufacturer_fk` FOREIGN KEY (`manufacturer_id`) REFERENCES `manufacturer` (`manufacturer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor`
--

LOCK TABLES `sensor` WRITE;
/*!40000 ALTER TABLE `sensor` DISABLE KEYS */;
INSERT INTO `sensor` VALUES (3,'ASD FS FR-3','ASD FieldSpec FR or FieldSpec3 type',NULL,4,2151,1),(4,'GER 3700','GER 3700',NULL,3700,647,2),(6,'MFR-7','MFR-7 Rotating Shadowband Radiometer',NULL,7,7,3),(7,'Beckman UV-5240','Beckman UV-5240',NULL,5240,826,4),(9,'USGS Beckman UV-5240','Beckman UV-5240',NULL,5240,420,4),(10,'SVC HR-1024','Spectra Vista HR-1024',NULL,1024,1024,7);
/*!40000 ALTER TABLE `sensor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensor_element`
--

DROP TABLE IF EXISTS `sensor_element`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sensor_element` (
  `sensor_element_id` int(11) NOT NULL AUTO_INCREMENT,
  `avg_wavelength` decimal(10,4) DEFAULT NULL,
  `fwhm` decimal(10,4) DEFAULT NULL,
  `sensor_id` int(11) DEFAULT NULL,
  `sensor_element_type_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`sensor_element_id`),
  KEY `FK_sensor_element_1` (`sensor_id`),
  KEY `FK_sensor_element_2` (`sensor_element_type_id`),
  CONSTRAINT `FK_sensor_element_2` FOREIGN KEY (`sensor_element_type_id`) REFERENCES `sensor_element_type` (`sensor_element_type_id`),
  CONSTRAINT `sensor_element_ibfk_1` FOREIGN KEY (`sensor_id`) REFERENCES `sensor` (`sensor_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5902 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor_element`
--

LOCK TABLES `sensor_element` WRITE;
/*!40000 ALTER TABLE `sensor_element` DISABLE KEYS */;
INSERT INTO `sensor_element` VALUES (1,350.0000,NULL,3,1),(2,351.0000,NULL,3,1),(3,352.0000,NULL,3,1),(4,353.0000,NULL,3,1),(5,354.0000,NULL,3,1),(6,355.0000,NULL,3,1),(7,356.0000,NULL,3,1),(8,357.0000,NULL,3,1),(9,358.0000,NULL,3,1),(10,359.0000,NULL,3,1),(11,360.0000,NULL,3,1),(12,361.0000,NULL,3,1),(13,362.0000,NULL,3,1),(14,363.0000,NULL,3,1),(15,364.0000,NULL,3,1),(16,365.0000,NULL,3,1),(17,366.0000,NULL,3,1),(18,367.0000,NULL,3,1),(19,368.0000,NULL,3,1),(20,369.0000,NULL,3,1),(21,370.0000,NULL,3,1),(22,371.0000,NULL,3,1),(23,372.0000,NULL,3,1),(24,373.0000,NULL,3,1),(25,374.0000,NULL,3,1),(26,375.0000,NULL,3,1),(27,376.0000,NULL,3,1),(28,377.0000,NULL,3,1),(29,378.0000,NULL,3,1),(30,379.0000,NULL,3,1),(31,380.0000,NULL,3,1),(32,381.0000,NULL,3,1),(33,382.0000,NULL,3,1),(34,383.0000,NULL,3,1),(35,384.0000,NULL,3,1),(36,385.0000,NULL,3,1),(37,386.0000,NULL,3,1),(38,387.0000,NULL,3,1),(39,388.0000,NULL,3,1),(40,389.0000,NULL,3,1),(41,390.0000,NULL,3,1),(42,391.0000,NULL,3,1),(43,392.0000,NULL,3,1),(44,393.0000,NULL,3,1),(45,394.0000,NULL,3,1),(46,395.0000,NULL,3,1),(47,396.0000,NULL,3,1),(48,397.0000,NULL,3,1),(49,398.0000,NULL,3,1),(50,399.0000,NULL,3,1),(51,400.0000,NULL,3,1),(52,401.0000,NULL,3,1),(53,402.0000,NULL,3,1),(54,403.0000,NULL,3,1),(55,404.0000,NULL,3,1),(56,405.0000,NULL,3,1),(57,406.0000,NULL,3,1),(58,407.0000,NULL,3,1),(59,408.0000,NULL,3,1),(60,409.0000,NULL,3,1),(61,410.0000,NULL,3,1),(62,411.0000,NULL,3,1),(63,412.0000,NULL,3,1),(64,413.0000,NULL,3,1),(65,414.0000,NULL,3,1),(66,415.0000,NULL,3,1),(67,416.0000,NULL,3,1),(68,417.0000,NULL,3,1),(69,418.0000,NULL,3,1),(70,419.0000,NULL,3,1),(71,420.0000,NULL,3,1),(72,421.0000,NULL,3,1),(73,422.0000,NULL,3,1),(74,423.0000,NULL,3,1),(75,424.0000,NULL,3,1),(76,425.0000,NULL,3,1),(77,426.0000,NULL,3,1),(78,427.0000,NULL,3,1),(79,428.0000,NULL,3,1),(80,429.0000,NULL,3,1),(81,430.0000,NULL,3,1),(82,431.0000,NULL,3,1),(83,432.0000,NULL,3,1),(84,433.0000,NULL,3,1),(85,434.0000,NULL,3,1),(86,435.0000,NULL,3,1),(87,436.0000,NULL,3,1),(88,437.0000,NULL,3,1),(89,438.0000,NULL,3,1),(90,439.0000,NULL,3,1),(91,440.0000,NULL,3,1),(92,441.0000,NULL,3,1),(93,442.0000,NULL,3,1),(94,443.0000,NULL,3,1),(95,444.0000,NULL,3,1),(96,445.0000,NULL,3,1),(97,446.0000,NULL,3,1),(98,447.0000,NULL,3,1),(99,448.0000,NULL,3,1),(100,449.0000,NULL,3,1),(101,450.0000,NULL,3,1),(102,451.0000,NULL,3,1),(103,452.0000,NULL,3,1),(104,453.0000,NULL,3,1),(105,454.0000,NULL,3,1),(106,455.0000,NULL,3,1),(107,456.0000,NULL,3,1),(108,457.0000,NULL,3,1),(109,458.0000,NULL,3,1),(110,459.0000,NULL,3,1),(111,460.0000,NULL,3,1),(112,461.0000,NULL,3,1),(113,462.0000,NULL,3,1),(114,463.0000,NULL,3,1),(115,464.0000,NULL,3,1),(116,465.0000,NULL,3,1),(117,466.0000,NULL,3,1),(118,467.0000,NULL,3,1),(119,468.0000,NULL,3,1),(120,469.0000,NULL,3,1),(121,470.0000,NULL,3,1),(122,471.0000,NULL,3,1),(123,472.0000,NULL,3,1),(124,473.0000,NULL,3,1),(125,474.0000,NULL,3,1),(126,475.0000,NULL,3,1),(127,476.0000,NULL,3,1),(128,477.0000,NULL,3,1),(129,478.0000,NULL,3,1),(130,479.0000,NULL,3,1),(131,480.0000,NULL,3,1),(132,481.0000,NULL,3,1),(133,482.0000,NULL,3,1),(134,483.0000,NULL,3,1),(135,484.0000,NULL,3,1),(136,485.0000,NULL,3,1),(137,486.0000,NULL,3,1),(138,487.0000,NULL,3,1),(139,488.0000,NULL,3,1),(140,489.0000,NULL,3,1),(141,490.0000,NULL,3,1),(142,491.0000,NULL,3,1),(143,492.0000,NULL,3,1),(144,493.0000,NULL,3,1),(145,494.0000,NULL,3,1),(146,495.0000,NULL,3,1),(147,496.0000,NULL,3,1),(148,497.0000,NULL,3,1),(149,498.0000,NULL,3,1),(150,499.0000,NULL,3,1),(151,500.0000,NULL,3,1),(152,501.0000,NULL,3,1),(153,502.0000,NULL,3,1),(154,503.0000,NULL,3,1),(155,504.0000,NULL,3,1),(156,505.0000,NULL,3,1),(157,506.0000,NULL,3,1),(158,507.0000,NULL,3,1),(159,508.0000,NULL,3,1),(160,509.0000,NULL,3,1),(161,510.0000,NULL,3,1),(162,511.0000,NULL,3,1),(163,512.0000,NULL,3,1),(164,513.0000,NULL,3,1),(165,514.0000,NULL,3,1),(166,515.0000,NULL,3,1),(167,516.0000,NULL,3,1),(168,517.0000,NULL,3,1),(169,518.0000,NULL,3,1),(170,519.0000,NULL,3,1),(171,520.0000,NULL,3,1),(172,521.0000,NULL,3,1),(173,522.0000,NULL,3,1),(174,523.0000,NULL,3,1),(175,524.0000,NULL,3,1),(176,525.0000,NULL,3,1),(177,526.0000,NULL,3,1),(178,527.0000,NULL,3,1),(179,528.0000,NULL,3,1),(180,529.0000,NULL,3,1),(181,530.0000,NULL,3,1),(182,531.0000,NULL,3,1),(183,532.0000,NULL,3,1),(184,533.0000,NULL,3,1),(185,534.0000,NULL,3,1),(186,535.0000,NULL,3,1),(187,536.0000,NULL,3,1),(188,537.0000,NULL,3,1),(189,538.0000,NULL,3,1),(190,539.0000,NULL,3,1),(191,540.0000,NULL,3,1),(192,541.0000,NULL,3,1),(193,542.0000,NULL,3,1),(194,543.0000,NULL,3,1),(195,544.0000,NULL,3,1),(196,545.0000,NULL,3,1),(197,546.0000,NULL,3,1),(198,547.0000,NULL,3,1),(199,548.0000,NULL,3,1),(200,549.0000,NULL,3,1),(201,550.0000,NULL,3,1),(202,551.0000,NULL,3,1),(203,552.0000,NULL,3,1),(204,553.0000,NULL,3,1),(205,554.0000,NULL,3,1),(206,555.0000,NULL,3,1),(207,556.0000,NULL,3,1),(208,557.0000,NULL,3,1),(209,558.0000,NULL,3,1),(210,559.0000,NULL,3,1),(211,560.0000,NULL,3,1),(212,561.0000,NULL,3,1),(213,562.0000,NULL,3,1),(214,563.0000,NULL,3,1),(215,564.0000,NULL,3,1),(216,565.0000,NULL,3,1),(217,566.0000,NULL,3,1),(218,567.0000,NULL,3,1),(219,568.0000,NULL,3,1),(220,569.0000,NULL,3,1),(221,570.0000,NULL,3,1),(222,571.0000,NULL,3,1),(223,572.0000,NULL,3,1),(224,573.0000,NULL,3,1),(225,574.0000,NULL,3,1),(226,575.0000,NULL,3,1),(227,576.0000,NULL,3,1),(228,577.0000,NULL,3,1),(229,578.0000,NULL,3,1),(230,579.0000,NULL,3,1),(231,580.0000,NULL,3,1),(232,581.0000,NULL,3,1),(233,582.0000,NULL,3,1),(234,583.0000,NULL,3,1),(235,584.0000,NULL,3,1),(236,585.0000,NULL,3,1),(237,586.0000,NULL,3,1),(238,587.0000,NULL,3,1),(239,588.0000,NULL,3,1),(240,589.0000,NULL,3,1),(241,590.0000,NULL,3,1),(242,591.0000,NULL,3,1),(243,592.0000,NULL,3,1),(244,593.0000,NULL,3,1),(245,594.0000,NULL,3,1),(246,595.0000,NULL,3,1),(247,596.0000,NULL,3,1),(248,597.0000,NULL,3,1),(249,598.0000,NULL,3,1),(250,599.0000,NULL,3,1),(251,600.0000,NULL,3,1),(252,601.0000,NULL,3,1),(253,602.0000,NULL,3,1),(254,603.0000,NULL,3,1),(255,604.0000,NULL,3,1),(256,605.0000,NULL,3,1),(257,606.0000,NULL,3,1),(258,607.0000,NULL,3,1),(259,608.0000,NULL,3,1),(260,609.0000,NULL,3,1),(261,610.0000,NULL,3,1),(262,611.0000,NULL,3,1),(263,612.0000,NULL,3,1),(264,613.0000,NULL,3,1),(265,614.0000,NULL,3,1),(266,615.0000,NULL,3,1),(267,616.0000,NULL,3,1),(268,617.0000,NULL,3,1),(269,618.0000,NULL,3,1),(270,619.0000,NULL,3,1),(271,620.0000,NULL,3,1),(272,621.0000,NULL,3,1),(273,622.0000,NULL,3,1),(274,623.0000,NULL,3,1),(275,624.0000,NULL,3,1),(276,625.0000,NULL,3,1),(277,626.0000,NULL,3,1),(278,627.0000,NULL,3,1),(279,628.0000,NULL,3,1),(280,629.0000,NULL,3,1),(281,630.0000,NULL,3,1),(282,631.0000,NULL,3,1),(283,632.0000,NULL,3,1),(284,633.0000,NULL,3,1),(285,634.0000,NULL,3,1),(286,635.0000,NULL,3,1),(287,636.0000,NULL,3,1),(288,637.0000,NULL,3,1),(289,638.0000,NULL,3,1),(290,639.0000,NULL,3,1),(291,640.0000,NULL,3,1),(292,641.0000,NULL,3,1),(293,642.0000,NULL,3,1),(294,643.0000,NULL,3,1),(295,644.0000,NULL,3,1),(296,645.0000,NULL,3,1),(297,646.0000,NULL,3,1),(298,647.0000,NULL,3,1),(299,648.0000,NULL,3,1),(300,649.0000,NULL,3,1),(301,650.0000,NULL,3,1),(302,651.0000,NULL,3,1),(303,652.0000,NULL,3,1),(304,653.0000,NULL,3,1),(305,654.0000,NULL,3,1),(306,655.0000,NULL,3,1),(307,656.0000,NULL,3,1),(308,657.0000,NULL,3,1),(309,658.0000,NULL,3,1),(310,659.0000,NULL,3,1),(311,660.0000,NULL,3,1),(312,661.0000,NULL,3,1),(313,662.0000,NULL,3,1),(314,663.0000,NULL,3,1),(315,664.0000,NULL,3,1),(316,665.0000,NULL,3,1),(317,666.0000,NULL,3,1),(318,667.0000,NULL,3,1),(319,668.0000,NULL,3,1),(320,669.0000,NULL,3,1),(321,670.0000,NULL,3,1),(322,671.0000,NULL,3,1),(323,672.0000,NULL,3,1),(324,673.0000,NULL,3,1),(325,674.0000,NULL,3,1),(326,675.0000,NULL,3,1),(327,676.0000,NULL,3,1),(328,677.0000,NULL,3,1),(329,678.0000,NULL,3,1),(330,679.0000,NULL,3,1),(331,680.0000,NULL,3,1),(332,681.0000,NULL,3,1),(333,682.0000,NULL,3,1),(334,683.0000,NULL,3,1),(335,684.0000,NULL,3,1),(336,685.0000,NULL,3,1),(337,686.0000,NULL,3,1),(338,687.0000,NULL,3,1),(339,688.0000,NULL,3,1),(340,689.0000,NULL,3,1),(341,690.0000,NULL,3,1),(342,691.0000,NULL,3,1),(343,692.0000,NULL,3,1),(344,693.0000,NULL,3,1),(345,694.0000,NULL,3,1),(346,695.0000,NULL,3,1),(347,696.0000,NULL,3,1),(348,697.0000,NULL,3,1),(349,698.0000,NULL,3,1),(350,699.0000,NULL,3,1),(351,700.0000,NULL,3,1),(352,701.0000,NULL,3,1),(353,702.0000,NULL,3,1),(354,703.0000,NULL,3,1),(355,704.0000,NULL,3,1),(356,705.0000,NULL,3,1),(357,706.0000,NULL,3,1),(358,707.0000,NULL,3,1),(359,708.0000,NULL,3,1),(360,709.0000,NULL,3,1),(361,710.0000,NULL,3,1),(362,711.0000,NULL,3,1),(363,712.0000,NULL,3,1),(364,713.0000,NULL,3,1),(365,714.0000,NULL,3,1),(366,715.0000,NULL,3,1),(367,716.0000,NULL,3,1),(368,717.0000,NULL,3,1),(369,718.0000,NULL,3,1),(370,719.0000,NULL,3,1),(371,720.0000,NULL,3,1),(372,721.0000,NULL,3,1),(373,722.0000,NULL,3,1),(374,723.0000,NULL,3,1),(375,724.0000,NULL,3,1),(376,725.0000,NULL,3,1),(377,726.0000,NULL,3,1),(378,727.0000,NULL,3,1),(379,728.0000,NULL,3,1),(380,729.0000,NULL,3,1),(381,730.0000,NULL,3,1),(382,731.0000,NULL,3,1),(383,732.0000,NULL,3,1),(384,733.0000,NULL,3,1),(385,734.0000,NULL,3,1),(386,735.0000,NULL,3,1),(387,736.0000,NULL,3,1),(388,737.0000,NULL,3,1),(389,738.0000,NULL,3,1),(390,739.0000,NULL,3,1),(391,740.0000,NULL,3,1),(392,741.0000,NULL,3,1),(393,742.0000,NULL,3,1),(394,743.0000,NULL,3,1),(395,744.0000,NULL,3,1),(396,745.0000,NULL,3,1),(397,746.0000,NULL,3,1),(398,747.0000,NULL,3,1),(399,748.0000,NULL,3,1),(400,749.0000,NULL,3,1),(401,750.0000,NULL,3,1),(402,751.0000,NULL,3,1),(403,752.0000,NULL,3,1),(404,753.0000,NULL,3,1),(405,754.0000,NULL,3,1),(406,755.0000,NULL,3,1),(407,756.0000,NULL,3,1),(408,757.0000,NULL,3,1),(409,758.0000,NULL,3,1),(410,759.0000,NULL,3,1),(411,760.0000,NULL,3,1),(412,761.0000,NULL,3,1),(413,762.0000,NULL,3,1),(414,763.0000,NULL,3,1),(415,764.0000,NULL,3,1),(416,765.0000,NULL,3,1),(417,766.0000,NULL,3,1),(418,767.0000,NULL,3,1),(419,768.0000,NULL,3,1),(420,769.0000,NULL,3,1),(421,770.0000,NULL,3,1),(422,771.0000,NULL,3,1),(423,772.0000,NULL,3,1),(424,773.0000,NULL,3,1),(425,774.0000,NULL,3,1),(426,775.0000,NULL,3,1),(427,776.0000,NULL,3,1),(428,777.0000,NULL,3,1),(429,778.0000,NULL,3,1),(430,779.0000,NULL,3,1),(431,780.0000,NULL,3,1),(432,781.0000,NULL,3,1),(433,782.0000,NULL,3,1),(434,783.0000,NULL,3,1),(435,784.0000,NULL,3,1),(436,785.0000,NULL,3,1),(437,786.0000,NULL,3,1),(438,787.0000,NULL,3,1),(439,788.0000,NULL,3,1),(440,789.0000,NULL,3,1),(441,790.0000,NULL,3,1),(442,791.0000,NULL,3,1),(443,792.0000,NULL,3,1),(444,793.0000,NULL,3,1),(445,794.0000,NULL,3,1),(446,795.0000,NULL,3,1),(447,796.0000,NULL,3,1),(448,797.0000,NULL,3,1),(449,798.0000,NULL,3,1),(450,799.0000,NULL,3,1),(451,800.0000,NULL,3,1),(452,801.0000,NULL,3,1),(453,802.0000,NULL,3,1),(454,803.0000,NULL,3,1),(455,804.0000,NULL,3,1),(456,805.0000,NULL,3,1),(457,806.0000,NULL,3,1),(458,807.0000,NULL,3,1),(459,808.0000,NULL,3,1),(460,809.0000,NULL,3,1),(461,810.0000,NULL,3,1),(462,811.0000,NULL,3,1),(463,812.0000,NULL,3,1),(464,813.0000,NULL,3,1),(465,814.0000,NULL,3,1),(466,815.0000,NULL,3,1),(467,816.0000,NULL,3,1),(468,817.0000,NULL,3,1),(469,818.0000,NULL,3,1),(470,819.0000,NULL,3,1),(471,820.0000,NULL,3,1),(472,821.0000,NULL,3,1),(473,822.0000,NULL,3,1),(474,823.0000,NULL,3,1),(475,824.0000,NULL,3,1),(476,825.0000,NULL,3,1),(477,826.0000,NULL,3,1),(478,827.0000,NULL,3,1),(479,828.0000,NULL,3,1),(480,829.0000,NULL,3,1),(481,830.0000,NULL,3,1),(482,831.0000,NULL,3,1),(483,832.0000,NULL,3,1),(484,833.0000,NULL,3,1),(485,834.0000,NULL,3,1),(486,835.0000,NULL,3,1),(487,836.0000,NULL,3,1),(488,837.0000,NULL,3,1),(489,838.0000,NULL,3,1),(490,839.0000,NULL,3,1),(491,840.0000,NULL,3,1),(492,841.0000,NULL,3,1),(493,842.0000,NULL,3,1),(494,843.0000,NULL,3,1),(495,844.0000,NULL,3,1),(496,845.0000,NULL,3,1),(497,846.0000,NULL,3,1),(498,847.0000,NULL,3,1),(499,848.0000,NULL,3,1),(500,849.0000,NULL,3,1),(501,850.0000,NULL,3,1),(502,851.0000,NULL,3,1),(503,852.0000,NULL,3,1),(504,853.0000,NULL,3,1),(505,854.0000,NULL,3,1),(506,855.0000,NULL,3,1),(507,856.0000,NULL,3,1),(508,857.0000,NULL,3,1),(509,858.0000,NULL,3,1),(510,859.0000,NULL,3,1),(511,860.0000,NULL,3,1),(512,861.0000,NULL,3,1),(513,862.0000,NULL,3,1),(514,863.0000,NULL,3,1),(515,864.0000,NULL,3,1),(516,865.0000,NULL,3,1),(517,866.0000,NULL,3,1),(518,867.0000,NULL,3,1),(519,868.0000,NULL,3,1),(520,869.0000,NULL,3,1),(521,870.0000,NULL,3,1),(522,871.0000,NULL,3,1),(523,872.0000,NULL,3,1),(524,873.0000,NULL,3,1),(525,874.0000,NULL,3,1),(526,875.0000,NULL,3,1),(527,876.0000,NULL,3,1),(528,877.0000,NULL,3,1),(529,878.0000,NULL,3,1),(530,879.0000,NULL,3,1),(531,880.0000,NULL,3,1),(532,881.0000,NULL,3,1),(533,882.0000,NULL,3,1),(534,883.0000,NULL,3,1),(535,884.0000,NULL,3,1),(536,885.0000,NULL,3,1),(537,886.0000,NULL,3,1),(538,887.0000,NULL,3,1),(539,888.0000,NULL,3,1),(540,889.0000,NULL,3,1),(541,890.0000,NULL,3,1),(542,891.0000,NULL,3,1),(543,892.0000,NULL,3,1),(544,893.0000,NULL,3,1),(545,894.0000,NULL,3,1),(546,895.0000,NULL,3,1),(547,896.0000,NULL,3,1),(548,897.0000,NULL,3,1),(549,898.0000,NULL,3,1),(550,899.0000,NULL,3,1),(551,900.0000,NULL,3,1),(552,901.0000,NULL,3,1),(553,902.0000,NULL,3,1),(554,903.0000,NULL,3,1),(555,904.0000,NULL,3,1),(556,905.0000,NULL,3,1),(557,906.0000,NULL,3,1),(558,907.0000,NULL,3,1),(559,908.0000,NULL,3,1),(560,909.0000,NULL,3,1),(561,910.0000,NULL,3,1),(562,911.0000,NULL,3,1),(563,912.0000,NULL,3,1),(564,913.0000,NULL,3,1),(565,914.0000,NULL,3,1),(566,915.0000,NULL,3,1),(567,916.0000,NULL,3,1),(568,917.0000,NULL,3,1),(569,918.0000,NULL,3,1),(570,919.0000,NULL,3,1),(571,920.0000,NULL,3,1),(572,921.0000,NULL,3,1),(573,922.0000,NULL,3,1),(574,923.0000,NULL,3,1),(575,924.0000,NULL,3,1),(576,925.0000,NULL,3,1),(577,926.0000,NULL,3,1),(578,927.0000,NULL,3,1),(579,928.0000,NULL,3,1),(580,929.0000,NULL,3,1),(581,930.0000,NULL,3,1),(582,931.0000,NULL,3,1),(583,932.0000,NULL,3,1),(584,933.0000,NULL,3,1),(585,934.0000,NULL,3,1),(586,935.0000,NULL,3,1),(587,936.0000,NULL,3,1),(588,937.0000,NULL,3,1),(589,938.0000,NULL,3,1),(590,939.0000,NULL,3,1),(591,940.0000,NULL,3,1),(592,941.0000,NULL,3,1),(593,942.0000,NULL,3,1),(594,943.0000,NULL,3,1),(595,944.0000,NULL,3,1),(596,945.0000,NULL,3,1),(597,946.0000,NULL,3,1),(598,947.0000,NULL,3,1),(599,948.0000,NULL,3,1),(600,949.0000,NULL,3,1),(601,950.0000,NULL,3,1),(602,951.0000,NULL,3,1),(603,952.0000,NULL,3,1),(604,953.0000,NULL,3,1),(605,954.0000,NULL,3,1),(606,955.0000,NULL,3,1),(607,956.0000,NULL,3,1),(608,957.0000,NULL,3,1),(609,958.0000,NULL,3,1),(610,959.0000,NULL,3,1),(611,960.0000,NULL,3,1),(612,961.0000,NULL,3,1),(613,962.0000,NULL,3,1),(614,963.0000,NULL,3,1),(615,964.0000,NULL,3,1),(616,965.0000,NULL,3,1),(617,966.0000,NULL,3,1),(618,967.0000,NULL,3,1),(619,968.0000,NULL,3,1),(620,969.0000,NULL,3,1),(621,970.0000,NULL,3,1),(622,971.0000,NULL,3,1),(623,972.0000,NULL,3,1),(624,973.0000,NULL,3,1),(625,974.0000,NULL,3,1),(626,975.0000,NULL,3,1),(627,976.0000,NULL,3,1),(628,977.0000,NULL,3,1),(629,978.0000,NULL,3,1),(630,979.0000,NULL,3,1),(631,980.0000,NULL,3,1),(632,981.0000,NULL,3,1),(633,982.0000,NULL,3,1),(634,983.0000,NULL,3,1),(635,984.0000,NULL,3,1),(636,985.0000,NULL,3,1),(637,986.0000,NULL,3,1),(638,987.0000,NULL,3,1),(639,988.0000,NULL,3,1),(640,989.0000,NULL,3,1),(641,990.0000,NULL,3,1),(642,991.0000,NULL,3,1),(643,992.0000,NULL,3,1),(644,993.0000,NULL,3,1),(645,994.0000,NULL,3,1),(646,995.0000,NULL,3,1),(647,996.0000,NULL,3,1),(648,997.0000,NULL,3,1),(649,998.0000,NULL,3,1),(650,999.0000,NULL,3,1),(651,1000.0000,NULL,3,1),(652,1001.0000,NULL,3,1),(653,1002.0000,NULL,3,1),(654,1003.0000,NULL,3,1),(655,1004.0000,NULL,3,1),(656,1005.0000,NULL,3,1),(657,1006.0000,NULL,3,1),(658,1007.0000,NULL,3,1),(659,1008.0000,NULL,3,1),(660,1009.0000,NULL,3,1),(661,1010.0000,NULL,3,1),(662,1011.0000,NULL,3,1),(663,1012.0000,NULL,3,1),(664,1013.0000,NULL,3,1),(665,1014.0000,NULL,3,1),(666,1015.0000,NULL,3,1),(667,1016.0000,NULL,3,1),(668,1017.0000,NULL,3,1),(669,1018.0000,NULL,3,1),(670,1019.0000,NULL,3,1),(671,1020.0000,NULL,3,1),(672,1021.0000,NULL,3,1),(673,1022.0000,NULL,3,1),(674,1023.0000,NULL,3,1),(675,1024.0000,NULL,3,1),(676,1025.0000,NULL,3,1),(677,1026.0000,NULL,3,1),(678,1027.0000,NULL,3,1),(679,1028.0000,NULL,3,1),(680,1029.0000,NULL,3,1),(681,1030.0000,NULL,3,1),(682,1031.0000,NULL,3,1),(683,1032.0000,NULL,3,1),(684,1033.0000,NULL,3,1),(685,1034.0000,NULL,3,1),(686,1035.0000,NULL,3,1),(687,1036.0000,NULL,3,1),(688,1037.0000,NULL,3,1),(689,1038.0000,NULL,3,1),(690,1039.0000,NULL,3,1),(691,1040.0000,NULL,3,1),(692,1041.0000,NULL,3,1),(693,1042.0000,NULL,3,1),(694,1043.0000,NULL,3,1),(695,1044.0000,NULL,3,1),(696,1045.0000,NULL,3,1),(697,1046.0000,NULL,3,1),(698,1047.0000,NULL,3,1),(699,1048.0000,NULL,3,1),(700,1049.0000,NULL,3,1),(701,1050.0000,NULL,3,1),(702,1051.0000,NULL,3,1),(703,1052.0000,NULL,3,1),(704,1053.0000,NULL,3,1),(705,1054.0000,NULL,3,1),(706,1055.0000,NULL,3,1),(707,1056.0000,NULL,3,1),(708,1057.0000,NULL,3,1),(709,1058.0000,NULL,3,1),(710,1059.0000,NULL,3,1),(711,1060.0000,NULL,3,1),(712,1061.0000,NULL,3,1),(713,1062.0000,NULL,3,1),(714,1063.0000,NULL,3,1),(715,1064.0000,NULL,3,1),(716,1065.0000,NULL,3,1),(717,1066.0000,NULL,3,1),(718,1067.0000,NULL,3,1),(719,1068.0000,NULL,3,1),(720,1069.0000,NULL,3,1),(721,1070.0000,NULL,3,1),(722,1071.0000,NULL,3,1),(723,1072.0000,NULL,3,1),(724,1073.0000,NULL,3,1),(725,1074.0000,NULL,3,1),(726,1075.0000,NULL,3,1),(727,1076.0000,NULL,3,1),(728,1077.0000,NULL,3,1),(729,1078.0000,NULL,3,1),(730,1079.0000,NULL,3,1),(731,1080.0000,NULL,3,1),(732,1081.0000,NULL,3,1),(733,1082.0000,NULL,3,1),(734,1083.0000,NULL,3,1),(735,1084.0000,NULL,3,1),(736,1085.0000,NULL,3,1),(737,1086.0000,NULL,3,1),(738,1087.0000,NULL,3,1),(739,1088.0000,NULL,3,1),(740,1089.0000,NULL,3,1),(741,1090.0000,NULL,3,1),(742,1091.0000,NULL,3,1),(743,1092.0000,NULL,3,1),(744,1093.0000,NULL,3,1),(745,1094.0000,NULL,3,1),(746,1095.0000,NULL,3,1),(747,1096.0000,NULL,3,1),(748,1097.0000,NULL,3,1),(749,1098.0000,NULL,3,1),(750,1099.0000,NULL,3,1),(751,1100.0000,NULL,3,1),(752,1101.0000,NULL,3,1),(753,1102.0000,NULL,3,1),(754,1103.0000,NULL,3,1),(755,1104.0000,NULL,3,1),(756,1105.0000,NULL,3,1),(757,1106.0000,NULL,3,1),(758,1107.0000,NULL,3,1),(759,1108.0000,NULL,3,1),(760,1109.0000,NULL,3,1),(761,1110.0000,NULL,3,1),(762,1111.0000,NULL,3,1),(763,1112.0000,NULL,3,1),(764,1113.0000,NULL,3,1),(765,1114.0000,NULL,3,1),(766,1115.0000,NULL,3,1),(767,1116.0000,NULL,3,1),(768,1117.0000,NULL,3,1),(769,1118.0000,NULL,3,1),(770,1119.0000,NULL,3,1),(771,1120.0000,NULL,3,1),(772,1121.0000,NULL,3,1),(773,1122.0000,NULL,3,1),(774,1123.0000,NULL,3,1),(775,1124.0000,NULL,3,1),(776,1125.0000,NULL,3,1),(777,1126.0000,NULL,3,1),(778,1127.0000,NULL,3,1),(779,1128.0000,NULL,3,1),(780,1129.0000,NULL,3,1),(781,1130.0000,NULL,3,1),(782,1131.0000,NULL,3,1),(783,1132.0000,NULL,3,1),(784,1133.0000,NULL,3,1),(785,1134.0000,NULL,3,1),(786,1135.0000,NULL,3,1),(787,1136.0000,NULL,3,1),(788,1137.0000,NULL,3,1),(789,1138.0000,NULL,3,1),(790,1139.0000,NULL,3,1),(791,1140.0000,NULL,3,1),(792,1141.0000,NULL,3,1),(793,1142.0000,NULL,3,1),(794,1143.0000,NULL,3,1),(795,1144.0000,NULL,3,1),(796,1145.0000,NULL,3,1),(797,1146.0000,NULL,3,1),(798,1147.0000,NULL,3,1),(799,1148.0000,NULL,3,1),(800,1149.0000,NULL,3,1),(801,1150.0000,NULL,3,1),(802,1151.0000,NULL,3,1),(803,1152.0000,NULL,3,1),(804,1153.0000,NULL,3,1),(805,1154.0000,NULL,3,1),(806,1155.0000,NULL,3,1),(807,1156.0000,NULL,3,1),(808,1157.0000,NULL,3,1),(809,1158.0000,NULL,3,1),(810,1159.0000,NULL,3,1),(811,1160.0000,NULL,3,1),(812,1161.0000,NULL,3,1),(813,1162.0000,NULL,3,1),(814,1163.0000,NULL,3,1),(815,1164.0000,NULL,3,1),(816,1165.0000,NULL,3,1),(817,1166.0000,NULL,3,1),(818,1167.0000,NULL,3,1),(819,1168.0000,NULL,3,1),(820,1169.0000,NULL,3,1),(821,1170.0000,NULL,3,1),(822,1171.0000,NULL,3,1),(823,1172.0000,NULL,3,1),(824,1173.0000,NULL,3,1),(825,1174.0000,NULL,3,1),(826,1175.0000,NULL,3,1),(827,1176.0000,NULL,3,1),(828,1177.0000,NULL,3,1),(829,1178.0000,NULL,3,1),(830,1179.0000,NULL,3,1),(831,1180.0000,NULL,3,1),(832,1181.0000,NULL,3,1),(833,1182.0000,NULL,3,1),(834,1183.0000,NULL,3,1),(835,1184.0000,NULL,3,1),(836,1185.0000,NULL,3,1),(837,1186.0000,NULL,3,1),(838,1187.0000,NULL,3,1),(839,1188.0000,NULL,3,1),(840,1189.0000,NULL,3,1),(841,1190.0000,NULL,3,1),(842,1191.0000,NULL,3,1),(843,1192.0000,NULL,3,1),(844,1193.0000,NULL,3,1),(845,1194.0000,NULL,3,1),(846,1195.0000,NULL,3,1),(847,1196.0000,NULL,3,1),(848,1197.0000,NULL,3,1),(849,1198.0000,NULL,3,1),(850,1199.0000,NULL,3,1),(851,1200.0000,NULL,3,1),(852,1201.0000,NULL,3,1),(853,1202.0000,NULL,3,1),(854,1203.0000,NULL,3,1),(855,1204.0000,NULL,3,1),(856,1205.0000,NULL,3,1),(857,1206.0000,NULL,3,1),(858,1207.0000,NULL,3,1),(859,1208.0000,NULL,3,1),(860,1209.0000,NULL,3,1),(861,1210.0000,NULL,3,1),(862,1211.0000,NULL,3,1),(863,1212.0000,NULL,3,1),(864,1213.0000,NULL,3,1),(865,1214.0000,NULL,3,1),(866,1215.0000,NULL,3,1),(867,1216.0000,NULL,3,1),(868,1217.0000,NULL,3,1),(869,1218.0000,NULL,3,1),(870,1219.0000,NULL,3,1),(871,1220.0000,NULL,3,1),(872,1221.0000,NULL,3,1),(873,1222.0000,NULL,3,1),(874,1223.0000,NULL,3,1),(875,1224.0000,NULL,3,1),(876,1225.0000,NULL,3,1),(877,1226.0000,NULL,3,1),(878,1227.0000,NULL,3,1),(879,1228.0000,NULL,3,1),(880,1229.0000,NULL,3,1),(881,1230.0000,NULL,3,1),(882,1231.0000,NULL,3,1),(883,1232.0000,NULL,3,1),(884,1233.0000,NULL,3,1),(885,1234.0000,NULL,3,1),(886,1235.0000,NULL,3,1),(887,1236.0000,NULL,3,1),(888,1237.0000,NULL,3,1),(889,1238.0000,NULL,3,1),(890,1239.0000,NULL,3,1),(891,1240.0000,NULL,3,1),(892,1241.0000,NULL,3,1),(893,1242.0000,NULL,3,1),(894,1243.0000,NULL,3,1),(895,1244.0000,NULL,3,1),(896,1245.0000,NULL,3,1),(897,1246.0000,NULL,3,1),(898,1247.0000,NULL,3,1),(899,1248.0000,NULL,3,1),(900,1249.0000,NULL,3,1),(901,1250.0000,NULL,3,1),(902,1251.0000,NULL,3,1),(903,1252.0000,NULL,3,1),(904,1253.0000,NULL,3,1),(905,1254.0000,NULL,3,1),(906,1255.0000,NULL,3,1),(907,1256.0000,NULL,3,1),(908,1257.0000,NULL,3,1),(909,1258.0000,NULL,3,1),(910,1259.0000,NULL,3,1),(911,1260.0000,NULL,3,1),(912,1261.0000,NULL,3,1),(913,1262.0000,NULL,3,1),(914,1263.0000,NULL,3,1),(915,1264.0000,NULL,3,1),(916,1265.0000,NULL,3,1),(917,1266.0000,NULL,3,1),(918,1267.0000,NULL,3,1),(919,1268.0000,NULL,3,1),(920,1269.0000,NULL,3,1),(921,1270.0000,NULL,3,1),(922,1271.0000,NULL,3,1),(923,1272.0000,NULL,3,1),(924,1273.0000,NULL,3,1),(925,1274.0000,NULL,3,1),(926,1275.0000,NULL,3,1),(927,1276.0000,NULL,3,1),(928,1277.0000,NULL,3,1),(929,1278.0000,NULL,3,1),(930,1279.0000,NULL,3,1),(931,1280.0000,NULL,3,1),(932,1281.0000,NULL,3,1),(933,1282.0000,NULL,3,1),(934,1283.0000,NULL,3,1),(935,1284.0000,NULL,3,1),(936,1285.0000,NULL,3,1),(937,1286.0000,NULL,3,1),(938,1287.0000,NULL,3,1),(939,1288.0000,NULL,3,1),(940,1289.0000,NULL,3,1),(941,1290.0000,NULL,3,1),(942,1291.0000,NULL,3,1),(943,1292.0000,NULL,3,1),(944,1293.0000,NULL,3,1),(945,1294.0000,NULL,3,1),(946,1295.0000,NULL,3,1),(947,1296.0000,NULL,3,1),(948,1297.0000,NULL,3,1),(949,1298.0000,NULL,3,1),(950,1299.0000,NULL,3,1),(951,1300.0000,NULL,3,1),(952,1301.0000,NULL,3,1),(953,1302.0000,NULL,3,1),(954,1303.0000,NULL,3,1),(955,1304.0000,NULL,3,1),(956,1305.0000,NULL,3,1),(957,1306.0000,NULL,3,1),(958,1307.0000,NULL,3,1),(959,1308.0000,NULL,3,1),(960,1309.0000,NULL,3,1),(961,1310.0000,NULL,3,1),(962,1311.0000,NULL,3,1),(963,1312.0000,NULL,3,1),(964,1313.0000,NULL,3,1),(965,1314.0000,NULL,3,1),(966,1315.0000,NULL,3,1),(967,1316.0000,NULL,3,1),(968,1317.0000,NULL,3,1),(969,1318.0000,NULL,3,1),(970,1319.0000,NULL,3,1),(971,1320.0000,NULL,3,1),(972,1321.0000,NULL,3,1),(973,1322.0000,NULL,3,1),(974,1323.0000,NULL,3,1),(975,1324.0000,NULL,3,1),(976,1325.0000,NULL,3,1),(977,1326.0000,NULL,3,1),(978,1327.0000,NULL,3,1),(979,1328.0000,NULL,3,1),(980,1329.0000,NULL,3,1),(981,1330.0000,NULL,3,1),(982,1331.0000,NULL,3,1),(983,1332.0000,NULL,3,1),(984,1333.0000,NULL,3,1),(985,1334.0000,NULL,3,1),(986,1335.0000,NULL,3,1),(987,1336.0000,NULL,3,1),(988,1337.0000,NULL,3,1),(989,1338.0000,NULL,3,1),(990,1339.0000,NULL,3,1),(991,1340.0000,NULL,3,1),(992,1341.0000,NULL,3,1),(993,1342.0000,NULL,3,1),(994,1343.0000,NULL,3,1),(995,1344.0000,NULL,3,1),(996,1345.0000,NULL,3,1),(997,1346.0000,NULL,3,1),(998,1347.0000,NULL,3,1),(999,1348.0000,NULL,3,1),(1000,1349.0000,NULL,3,1),(1001,1350.0000,NULL,3,1),(1002,1351.0000,NULL,3,1),(1003,1352.0000,NULL,3,1),(1004,1353.0000,NULL,3,1),(1005,1354.0000,NULL,3,1),(1006,1355.0000,NULL,3,1),(1007,1356.0000,NULL,3,1),(1008,1357.0000,NULL,3,1),(1009,1358.0000,NULL,3,1),(1010,1359.0000,NULL,3,1),(1011,1360.0000,NULL,3,1),(1012,1361.0000,NULL,3,1),(1013,1362.0000,NULL,3,1),(1014,1363.0000,NULL,3,1),(1015,1364.0000,NULL,3,1),(1016,1365.0000,NULL,3,1),(1017,1366.0000,NULL,3,1),(1018,1367.0000,NULL,3,1),(1019,1368.0000,NULL,3,1),(1020,1369.0000,NULL,3,1),(1021,1370.0000,NULL,3,1),(1022,1371.0000,NULL,3,1),(1023,1372.0000,NULL,3,1),(1024,1373.0000,NULL,3,1),(1025,1374.0000,NULL,3,1),(1026,1375.0000,NULL,3,1),(1027,1376.0000,NULL,3,1),(1028,1377.0000,NULL,3,1),(1029,1378.0000,NULL,3,1),(1030,1379.0000,NULL,3,1),(1031,1380.0000,NULL,3,1),(1032,1381.0000,NULL,3,1),(1033,1382.0000,NULL,3,1),(1034,1383.0000,NULL,3,1),(1035,1384.0000,NULL,3,1),(1036,1385.0000,NULL,3,1),(1037,1386.0000,NULL,3,1),(1038,1387.0000,NULL,3,1),(1039,1388.0000,NULL,3,1),(1040,1389.0000,NULL,3,1),(1041,1390.0000,NULL,3,1),(1042,1391.0000,NULL,3,1),(1043,1392.0000,NULL,3,1),(1044,1393.0000,NULL,3,1),(1045,1394.0000,NULL,3,1),(1046,1395.0000,NULL,3,1),(1047,1396.0000,NULL,3,1),(1048,1397.0000,NULL,3,1),(1049,1398.0000,NULL,3,1),(1050,1399.0000,NULL,3,1),(1051,1400.0000,NULL,3,1),(1052,1401.0000,NULL,3,1),(1053,1402.0000,NULL,3,1),(1054,1403.0000,NULL,3,1),(1055,1404.0000,NULL,3,1),(1056,1405.0000,NULL,3,1),(1057,1406.0000,NULL,3,1),(1058,1407.0000,NULL,3,1),(1059,1408.0000,NULL,3,1),(1060,1409.0000,NULL,3,1),(1061,1410.0000,NULL,3,1),(1062,1411.0000,NULL,3,1),(1063,1412.0000,NULL,3,1),(1064,1413.0000,NULL,3,1),(1065,1414.0000,NULL,3,1),(1066,1415.0000,NULL,3,1),(1067,1416.0000,NULL,3,1),(1068,1417.0000,NULL,3,1),(1069,1418.0000,NULL,3,1),(1070,1419.0000,NULL,3,1),(1071,1420.0000,NULL,3,1),(1072,1421.0000,NULL,3,1),(1073,1422.0000,NULL,3,1),(1074,1423.0000,NULL,3,1),(1075,1424.0000,NULL,3,1),(1076,1425.0000,NULL,3,1),(1077,1426.0000,NULL,3,1),(1078,1427.0000,NULL,3,1),(1079,1428.0000,NULL,3,1),(1080,1429.0000,NULL,3,1),(1081,1430.0000,NULL,3,1),(1082,1431.0000,NULL,3,1),(1083,1432.0000,NULL,3,1),(1084,1433.0000,NULL,3,1),(1085,1434.0000,NULL,3,1),(1086,1435.0000,NULL,3,1),(1087,1436.0000,NULL,3,1),(1088,1437.0000,NULL,3,1),(1089,1438.0000,NULL,3,1),(1090,1439.0000,NULL,3,1),(1091,1440.0000,NULL,3,1),(1092,1441.0000,NULL,3,1),(1093,1442.0000,NULL,3,1),(1094,1443.0000,NULL,3,1),(1095,1444.0000,NULL,3,1),(1096,1445.0000,NULL,3,1),(1097,1446.0000,NULL,3,1),(1098,1447.0000,NULL,3,1),(1099,1448.0000,NULL,3,1),(1100,1449.0000,NULL,3,1),(1101,1450.0000,NULL,3,1),(1102,1451.0000,NULL,3,1),(1103,1452.0000,NULL,3,1),(1104,1453.0000,NULL,3,1),(1105,1454.0000,NULL,3,1),(1106,1455.0000,NULL,3,1),(1107,1456.0000,NULL,3,1),(1108,1457.0000,NULL,3,1),(1109,1458.0000,NULL,3,1),(1110,1459.0000,NULL,3,1),(1111,1460.0000,NULL,3,1),(1112,1461.0000,NULL,3,1),(1113,1462.0000,NULL,3,1),(1114,1463.0000,NULL,3,1),(1115,1464.0000,NULL,3,1),(1116,1465.0000,NULL,3,1),(1117,1466.0000,NULL,3,1),(1118,1467.0000,NULL,3,1),(1119,1468.0000,NULL,3,1),(1120,1469.0000,NULL,3,1),(1121,1470.0000,NULL,3,1),(1122,1471.0000,NULL,3,1),(1123,1472.0000,NULL,3,1),(1124,1473.0000,NULL,3,1),(1125,1474.0000,NULL,3,1),(1126,1475.0000,NULL,3,1),(1127,1476.0000,NULL,3,1),(1128,1477.0000,NULL,3,1),(1129,1478.0000,NULL,3,1),(1130,1479.0000,NULL,3,1),(1131,1480.0000,NULL,3,1),(1132,1481.0000,NULL,3,1),(1133,1482.0000,NULL,3,1),(1134,1483.0000,NULL,3,1),(1135,1484.0000,NULL,3,1),(1136,1485.0000,NULL,3,1),(1137,1486.0000,NULL,3,1),(1138,1487.0000,NULL,3,1),(1139,1488.0000,NULL,3,1),(1140,1489.0000,NULL,3,1),(1141,1490.0000,NULL,3,1),(1142,1491.0000,NULL,3,1),(1143,1492.0000,NULL,3,1),(1144,1493.0000,NULL,3,1),(1145,1494.0000,NULL,3,1),(1146,1495.0000,NULL,3,1),(1147,1496.0000,NULL,3,1),(1148,1497.0000,NULL,3,1),(1149,1498.0000,NULL,3,1),(1150,1499.0000,NULL,3,1),(1151,1500.0000,NULL,3,1),(1152,1501.0000,NULL,3,1),(1153,1502.0000,NULL,3,1),(1154,1503.0000,NULL,3,1),(1155,1504.0000,NULL,3,1),(1156,1505.0000,NULL,3,1),(1157,1506.0000,NULL,3,1),(1158,1507.0000,NULL,3,1),(1159,1508.0000,NULL,3,1),(1160,1509.0000,NULL,3,1),(1161,1510.0000,NULL,3,1),(1162,1511.0000,NULL,3,1),(1163,1512.0000,NULL,3,1),(1164,1513.0000,NULL,3,1),(1165,1514.0000,NULL,3,1),(1166,1515.0000,NULL,3,1),(1167,1516.0000,NULL,3,1),(1168,1517.0000,NULL,3,1),(1169,1518.0000,NULL,3,1),(1170,1519.0000,NULL,3,1),(1171,1520.0000,NULL,3,1),(1172,1521.0000,NULL,3,1),(1173,1522.0000,NULL,3,1),(1174,1523.0000,NULL,3,1),(1175,1524.0000,NULL,3,1),(1176,1525.0000,NULL,3,1),(1177,1526.0000,NULL,3,1),(1178,1527.0000,NULL,3,1),(1179,1528.0000,NULL,3,1),(1180,1529.0000,NULL,3,1),(1181,1530.0000,NULL,3,1),(1182,1531.0000,NULL,3,1),(1183,1532.0000,NULL,3,1),(1184,1533.0000,NULL,3,1),(1185,1534.0000,NULL,3,1),(1186,1535.0000,NULL,3,1),(1187,1536.0000,NULL,3,1),(1188,1537.0000,NULL,3,1),(1189,1538.0000,NULL,3,1),(1190,1539.0000,NULL,3,1),(1191,1540.0000,NULL,3,1),(1192,1541.0000,NULL,3,1),(1193,1542.0000,NULL,3,1),(1194,1543.0000,NULL,3,1),(1195,1544.0000,NULL,3,1),(1196,1545.0000,NULL,3,1),(1197,1546.0000,NULL,3,1),(1198,1547.0000,NULL,3,1),(1199,1548.0000,NULL,3,1),(1200,1549.0000,NULL,3,1),(1201,1550.0000,NULL,3,1),(1202,1551.0000,NULL,3,1),(1203,1552.0000,NULL,3,1),(1204,1553.0000,NULL,3,1),(1205,1554.0000,NULL,3,1),(1206,1555.0000,NULL,3,1),(1207,1556.0000,NULL,3,1),(1208,1557.0000,NULL,3,1),(1209,1558.0000,NULL,3,1),(1210,1559.0000,NULL,3,1),(1211,1560.0000,NULL,3,1),(1212,1561.0000,NULL,3,1),(1213,1562.0000,NULL,3,1),(1214,1563.0000,NULL,3,1),(1215,1564.0000,NULL,3,1),(1216,1565.0000,NULL,3,1),(1217,1566.0000,NULL,3,1),(1218,1567.0000,NULL,3,1),(1219,1568.0000,NULL,3,1),(1220,1569.0000,NULL,3,1),(1221,1570.0000,NULL,3,1),(1222,1571.0000,NULL,3,1),(1223,1572.0000,NULL,3,1),(1224,1573.0000,NULL,3,1),(1225,1574.0000,NULL,3,1),(1226,1575.0000,NULL,3,1),(1227,1576.0000,NULL,3,1),(1228,1577.0000,NULL,3,1),(1229,1578.0000,NULL,3,1),(1230,1579.0000,NULL,3,1),(1231,1580.0000,NULL,3,1),(1232,1581.0000,NULL,3,1),(1233,1582.0000,NULL,3,1),(1234,1583.0000,NULL,3,1),(1235,1584.0000,NULL,3,1),(1236,1585.0000,NULL,3,1),(1237,1586.0000,NULL,3,1),(1238,1587.0000,NULL,3,1),(1239,1588.0000,NULL,3,1),(1240,1589.0000,NULL,3,1),(1241,1590.0000,NULL,3,1),(1242,1591.0000,NULL,3,1),(1243,1592.0000,NULL,3,1),(1244,1593.0000,NULL,3,1),(1245,1594.0000,NULL,3,1),(1246,1595.0000,NULL,3,1),(1247,1596.0000,NULL,3,1),(1248,1597.0000,NULL,3,1),(1249,1598.0000,NULL,3,1),(1250,1599.0000,NULL,3,1),(1251,1600.0000,NULL,3,1),(1252,1601.0000,NULL,3,1),(1253,1602.0000,NULL,3,1),(1254,1603.0000,NULL,3,1),(1255,1604.0000,NULL,3,1),(1256,1605.0000,NULL,3,1),(1257,1606.0000,NULL,3,1),(1258,1607.0000,NULL,3,1),(1259,1608.0000,NULL,3,1),(1260,1609.0000,NULL,3,1),(1261,1610.0000,NULL,3,1),(1262,1611.0000,NULL,3,1),(1263,1612.0000,NULL,3,1),(1264,1613.0000,NULL,3,1),(1265,1614.0000,NULL,3,1),(1266,1615.0000,NULL,3,1),(1267,1616.0000,NULL,3,1),(1268,1617.0000,NULL,3,1),(1269,1618.0000,NULL,3,1),(1270,1619.0000,NULL,3,1),(1271,1620.0000,NULL,3,1),(1272,1621.0000,NULL,3,1),(1273,1622.0000,NULL,3,1),(1274,1623.0000,NULL,3,1),(1275,1624.0000,NULL,3,1),(1276,1625.0000,NULL,3,1),(1277,1626.0000,NULL,3,1),(1278,1627.0000,NULL,3,1),(1279,1628.0000,NULL,3,1),(1280,1629.0000,NULL,3,1),(1281,1630.0000,NULL,3,1),(1282,1631.0000,NULL,3,1),(1283,1632.0000,NULL,3,1),(1284,1633.0000,NULL,3,1),(1285,1634.0000,NULL,3,1),(1286,1635.0000,NULL,3,1),(1287,1636.0000,NULL,3,1),(1288,1637.0000,NULL,3,1),(1289,1638.0000,NULL,3,1),(1290,1639.0000,NULL,3,1),(1291,1640.0000,NULL,3,1),(1292,1641.0000,NULL,3,1),(1293,1642.0000,NULL,3,1),(1294,1643.0000,NULL,3,1),(1295,1644.0000,NULL,3,1),(1296,1645.0000,NULL,3,1),(1297,1646.0000,NULL,3,1),(1298,1647.0000,NULL,3,1),(1299,1648.0000,NULL,3,1),(1300,1649.0000,NULL,3,1),(1301,1650.0000,NULL,3,1),(1302,1651.0000,NULL,3,1),(1303,1652.0000,NULL,3,1),(1304,1653.0000,NULL,3,1),(1305,1654.0000,NULL,3,1),(1306,1655.0000,NULL,3,1),(1307,1656.0000,NULL,3,1),(1308,1657.0000,NULL,3,1),(1309,1658.0000,NULL,3,1),(1310,1659.0000,NULL,3,1),(1311,1660.0000,NULL,3,1),(1312,1661.0000,NULL,3,1),(1313,1662.0000,NULL,3,1),(1314,1663.0000,NULL,3,1),(1315,1664.0000,NULL,3,1),(1316,1665.0000,NULL,3,1),(1317,1666.0000,NULL,3,1),(1318,1667.0000,NULL,3,1),(1319,1668.0000,NULL,3,1),(1320,1669.0000,NULL,3,1),(1321,1670.0000,NULL,3,1),(1322,1671.0000,NULL,3,1),(1323,1672.0000,NULL,3,1),(1324,1673.0000,NULL,3,1),(1325,1674.0000,NULL,3,1),(1326,1675.0000,NULL,3,1),(1327,1676.0000,NULL,3,1),(1328,1677.0000,NULL,3,1),(1329,1678.0000,NULL,3,1),(1330,1679.0000,NULL,3,1),(1331,1680.0000,NULL,3,1),(1332,1681.0000,NULL,3,1),(1333,1682.0000,NULL,3,1),(1334,1683.0000,NULL,3,1),(1335,1684.0000,NULL,3,1),(1336,1685.0000,NULL,3,1),(1337,1686.0000,NULL,3,1),(1338,1687.0000,NULL,3,1),(1339,1688.0000,NULL,3,1),(1340,1689.0000,NULL,3,1),(1341,1690.0000,NULL,3,1),(1342,1691.0000,NULL,3,1),(1343,1692.0000,NULL,3,1),(1344,1693.0000,NULL,3,1),(1345,1694.0000,NULL,3,1),(1346,1695.0000,NULL,3,1),(1347,1696.0000,NULL,3,1),(1348,1697.0000,NULL,3,1),(1349,1698.0000,NULL,3,1),(1350,1699.0000,NULL,3,1),(1351,1700.0000,NULL,3,1),(1352,1701.0000,NULL,3,1),(1353,1702.0000,NULL,3,1),(1354,1703.0000,NULL,3,1),(1355,1704.0000,NULL,3,1),(1356,1705.0000,NULL,3,1),(1357,1706.0000,NULL,3,1),(1358,1707.0000,NULL,3,1),(1359,1708.0000,NULL,3,1),(1360,1709.0000,NULL,3,1),(1361,1710.0000,NULL,3,1),(1362,1711.0000,NULL,3,1),(1363,1712.0000,NULL,3,1),(1364,1713.0000,NULL,3,1),(1365,1714.0000,NULL,3,1),(1366,1715.0000,NULL,3,1),(1367,1716.0000,NULL,3,1),(1368,1717.0000,NULL,3,1),(1369,1718.0000,NULL,3,1),(1370,1719.0000,NULL,3,1),(1371,1720.0000,NULL,3,1),(1372,1721.0000,NULL,3,1),(1373,1722.0000,NULL,3,1),(1374,1723.0000,NULL,3,1),(1375,1724.0000,NULL,3,1),(1376,1725.0000,NULL,3,1),(1377,1726.0000,NULL,3,1),(1378,1727.0000,NULL,3,1),(1379,1728.0000,NULL,3,1),(1380,1729.0000,NULL,3,1),(1381,1730.0000,NULL,3,1),(1382,1731.0000,NULL,3,1),(1383,1732.0000,NULL,3,1),(1384,1733.0000,NULL,3,1),(1385,1734.0000,NULL,3,1),(1386,1735.0000,NULL,3,1),(1387,1736.0000,NULL,3,1),(1388,1737.0000,NULL,3,1),(1389,1738.0000,NULL,3,1),(1390,1739.0000,NULL,3,1),(1391,1740.0000,NULL,3,1),(1392,1741.0000,NULL,3,1),(1393,1742.0000,NULL,3,1),(1394,1743.0000,NULL,3,1),(1395,1744.0000,NULL,3,1),(1396,1745.0000,NULL,3,1),(1397,1746.0000,NULL,3,1),(1398,1747.0000,NULL,3,1),(1399,1748.0000,NULL,3,1),(1400,1749.0000,NULL,3,1),(1401,1750.0000,NULL,3,1),(1402,1751.0000,NULL,3,1),(1403,1752.0000,NULL,3,1),(1404,1753.0000,NULL,3,1),(1405,1754.0000,NULL,3,1),(1406,1755.0000,NULL,3,1),(1407,1756.0000,NULL,3,1),(1408,1757.0000,NULL,3,1),(1409,1758.0000,NULL,3,1),(1410,1759.0000,NULL,3,1),(1411,1760.0000,NULL,3,1),(1412,1761.0000,NULL,3,1),(1413,1762.0000,NULL,3,1),(1414,1763.0000,NULL,3,1),(1415,1764.0000,NULL,3,1),(1416,1765.0000,NULL,3,1),(1417,1766.0000,NULL,3,1),(1418,1767.0000,NULL,3,1),(1419,1768.0000,NULL,3,1),(1420,1769.0000,NULL,3,1),(1421,1770.0000,NULL,3,1),(1422,1771.0000,NULL,3,1),(1423,1772.0000,NULL,3,1),(1424,1773.0000,NULL,3,1),(1425,1774.0000,NULL,3,1),(1426,1775.0000,NULL,3,1),(1427,1776.0000,NULL,3,1),(1428,1777.0000,NULL,3,1),(1429,1778.0000,NULL,3,1),(1430,1779.0000,NULL,3,1),(1431,1780.0000,NULL,3,1),(1432,1781.0000,NULL,3,1),(1433,1782.0000,NULL,3,1),(1434,1783.0000,NULL,3,1),(1435,1784.0000,NULL,3,1),(1436,1785.0000,NULL,3,1),(1437,1786.0000,NULL,3,1),(1438,1787.0000,NULL,3,1),(1439,1788.0000,NULL,3,1),(1440,1789.0000,NULL,3,1),(1441,1790.0000,NULL,3,1),(1442,1791.0000,NULL,3,1),(1443,1792.0000,NULL,3,1),(1444,1793.0000,NULL,3,1),(1445,1794.0000,NULL,3,1),(1446,1795.0000,NULL,3,1),(1447,1796.0000,NULL,3,1),(1448,1797.0000,NULL,3,1),(1449,1798.0000,NULL,3,1),(1450,1799.0000,NULL,3,1),(1451,1800.0000,NULL,3,1),(1452,1801.0000,NULL,3,1),(1453,1802.0000,NULL,3,1),(1454,1803.0000,NULL,3,1),(1455,1804.0000,NULL,3,1),(1456,1805.0000,NULL,3,1),(1457,1806.0000,NULL,3,1),(1458,1807.0000,NULL,3,1),(1459,1808.0000,NULL,3,1),(1460,1809.0000,NULL,3,1),(1461,1810.0000,NULL,3,1),(1462,1811.0000,NULL,3,1),(1463,1812.0000,NULL,3,1),(1464,1813.0000,NULL,3,1),(1465,1814.0000,NULL,3,1),(1466,1815.0000,NULL,3,1),(1467,1816.0000,NULL,3,1),(1468,1817.0000,NULL,3,1),(1469,1818.0000,NULL,3,1),(1470,1819.0000,NULL,3,1),(1471,1820.0000,NULL,3,1),(1472,1821.0000,NULL,3,1),(1473,1822.0000,NULL,3,1),(1474,1823.0000,NULL,3,1),(1475,1824.0000,NULL,3,1),(1476,1825.0000,NULL,3,1),(1477,1826.0000,NULL,3,1),(1478,1827.0000,NULL,3,1),(1479,1828.0000,NULL,3,1),(1480,1829.0000,NULL,3,1),(1481,1830.0000,NULL,3,1),(1482,1831.0000,NULL,3,1),(1483,1832.0000,NULL,3,1),(1484,1833.0000,NULL,3,1),(1485,1834.0000,NULL,3,1),(1486,1835.0000,NULL,3,1),(1487,1836.0000,NULL,3,1),(1488,1837.0000,NULL,3,1),(1489,1838.0000,NULL,3,1),(1490,1839.0000,NULL,3,1),(1491,1840.0000,NULL,3,1),(1492,1841.0000,NULL,3,1),(1493,1842.0000,NULL,3,1),(1494,1843.0000,NULL,3,1),(1495,1844.0000,NULL,3,1),(1496,1845.0000,NULL,3,1),(1497,1846.0000,NULL,3,1),(1498,1847.0000,NULL,3,1),(1499,1848.0000,NULL,3,1),(1500,1849.0000,NULL,3,1),(1501,1850.0000,NULL,3,1),(1502,1851.0000,NULL,3,1),(1503,1852.0000,NULL,3,1),(1504,1853.0000,NULL,3,1),(1505,1854.0000,NULL,3,1),(1506,1855.0000,NULL,3,1),(1507,1856.0000,NULL,3,1),(1508,1857.0000,NULL,3,1),(1509,1858.0000,NULL,3,1),(1510,1859.0000,NULL,3,1),(1511,1860.0000,NULL,3,1),(1512,1861.0000,NULL,3,1),(1513,1862.0000,NULL,3,1),(1514,1863.0000,NULL,3,1),(1515,1864.0000,NULL,3,1),(1516,1865.0000,NULL,3,1),(1517,1866.0000,NULL,3,1),(1518,1867.0000,NULL,3,1),(1519,1868.0000,NULL,3,1),(1520,1869.0000,NULL,3,1),(1521,1870.0000,NULL,3,1),(1522,1871.0000,NULL,3,1),(1523,1872.0000,NULL,3,1),(1524,1873.0000,NULL,3,1),(1525,1874.0000,NULL,3,1),(1526,1875.0000,NULL,3,1),(1527,1876.0000,NULL,3,1),(1528,1877.0000,NULL,3,1),(1529,1878.0000,NULL,3,1),(1530,1879.0000,NULL,3,1),(1531,1880.0000,NULL,3,1),(1532,1881.0000,NULL,3,1),(1533,1882.0000,NULL,3,1),(1534,1883.0000,NULL,3,1),(1535,1884.0000,NULL,3,1),(1536,1885.0000,NULL,3,1),(1537,1886.0000,NULL,3,1),(1538,1887.0000,NULL,3,1),(1539,1888.0000,NULL,3,1),(1540,1889.0000,NULL,3,1),(1541,1890.0000,NULL,3,1),(1542,1891.0000,NULL,3,1),(1543,1892.0000,NULL,3,1),(1544,1893.0000,NULL,3,1),(1545,1894.0000,NULL,3,1),(1546,1895.0000,NULL,3,1),(1547,1896.0000,NULL,3,1),(1548,1897.0000,NULL,3,1),(1549,1898.0000,NULL,3,1),(1550,1899.0000,NULL,3,1),(1551,1900.0000,NULL,3,1),(1552,1901.0000,NULL,3,1),(1553,1902.0000,NULL,3,1),(1554,1903.0000,NULL,3,1),(1555,1904.0000,NULL,3,1),(1556,1905.0000,NULL,3,1),(1557,1906.0000,NULL,3,1),(1558,1907.0000,NULL,3,1),(1559,1908.0000,NULL,3,1),(1560,1909.0000,NULL,3,1),(1561,1910.0000,NULL,3,1),(1562,1911.0000,NULL,3,1),(1563,1912.0000,NULL,3,1),(1564,1913.0000,NULL,3,1),(1565,1914.0000,NULL,3,1),(1566,1915.0000,NULL,3,1),(1567,1916.0000,NULL,3,1),(1568,1917.0000,NULL,3,1),(1569,1918.0000,NULL,3,1),(1570,1919.0000,NULL,3,1),(1571,1920.0000,NULL,3,1),(1572,1921.0000,NULL,3,1),(1573,1922.0000,NULL,3,1),(1574,1923.0000,NULL,3,1),(1575,1924.0000,NULL,3,1),(1576,1925.0000,NULL,3,1),(1577,1926.0000,NULL,3,1),(1578,1927.0000,NULL,3,1),(1579,1928.0000,NULL,3,1),(1580,1929.0000,NULL,3,1),(1581,1930.0000,NULL,3,1),(1582,1931.0000,NULL,3,1),(1583,1932.0000,NULL,3,1),(1584,1933.0000,NULL,3,1),(1585,1934.0000,NULL,3,1),(1586,1935.0000,NULL,3,1),(1587,1936.0000,NULL,3,1),(1588,1937.0000,NULL,3,1),(1589,1938.0000,NULL,3,1),(1590,1939.0000,NULL,3,1),(1591,1940.0000,NULL,3,1),(1592,1941.0000,NULL,3,1),(1593,1942.0000,NULL,3,1),(1594,1943.0000,NULL,3,1),(1595,1944.0000,NULL,3,1),(1596,1945.0000,NULL,3,1),(1597,1946.0000,NULL,3,1),(1598,1947.0000,NULL,3,1),(1599,1948.0000,NULL,3,1),(1600,1949.0000,NULL,3,1),(1601,1950.0000,NULL,3,1),(1602,1951.0000,NULL,3,1),(1603,1952.0000,NULL,3,1),(1604,1953.0000,NULL,3,1),(1605,1954.0000,NULL,3,1),(1606,1955.0000,NULL,3,1),(1607,1956.0000,NULL,3,1),(1608,1957.0000,NULL,3,1),(1609,1958.0000,NULL,3,1),(1610,1959.0000,NULL,3,1),(1611,1960.0000,NULL,3,1),(1612,1961.0000,NULL,3,1),(1613,1962.0000,NULL,3,1),(1614,1963.0000,NULL,3,1),(1615,1964.0000,NULL,3,1),(1616,1965.0000,NULL,3,1),(1617,1966.0000,NULL,3,1),(1618,1967.0000,NULL,3,1),(1619,1968.0000,NULL,3,1),(1620,1969.0000,NULL,3,1),(1621,1970.0000,NULL,3,1),(1622,1971.0000,NULL,3,1),(1623,1972.0000,NULL,3,1),(1624,1973.0000,NULL,3,1),(1625,1974.0000,NULL,3,1),(1626,1975.0000,NULL,3,1),(1627,1976.0000,NULL,3,1),(1628,1977.0000,NULL,3,1),(1629,1978.0000,NULL,3,1),(1630,1979.0000,NULL,3,1),(1631,1980.0000,NULL,3,1),(1632,1981.0000,NULL,3,1),(1633,1982.0000,NULL,3,1),(1634,1983.0000,NULL,3,1),(1635,1984.0000,NULL,3,1),(1636,1985.0000,NULL,3,1),(1637,1986.0000,NULL,3,1),(1638,1987.0000,NULL,3,1),(1639,1988.0000,NULL,3,1),(1640,1989.0000,NULL,3,1),(1641,1990.0000,NULL,3,1),(1642,1991.0000,NULL,3,1),(1643,1992.0000,NULL,3,1),(1644,1993.0000,NULL,3,1),(1645,1994.0000,NULL,3,1),(1646,1995.0000,NULL,3,1),(1647,1996.0000,NULL,3,1),(1648,1997.0000,NULL,3,1),(1649,1998.0000,NULL,3,1),(1650,1999.0000,NULL,3,1),(1651,2000.0000,NULL,3,1),(1652,2001.0000,NULL,3,1),(1653,2002.0000,NULL,3,1),(1654,2003.0000,NULL,3,1),(1655,2004.0000,NULL,3,1),(1656,2005.0000,NULL,3,1),(1657,2006.0000,NULL,3,1),(1658,2007.0000,NULL,3,1),(1659,2008.0000,NULL,3,1),(1660,2009.0000,NULL,3,1),(1661,2010.0000,NULL,3,1),(1662,2011.0000,NULL,3,1),(1663,2012.0000,NULL,3,1),(1664,2013.0000,NULL,3,1),(1665,2014.0000,NULL,3,1),(1666,2015.0000,NULL,3,1),(1667,2016.0000,NULL,3,1),(1668,2017.0000,NULL,3,1),(1669,2018.0000,NULL,3,1),(1670,2019.0000,NULL,3,1),(1671,2020.0000,NULL,3,1),(1672,2021.0000,NULL,3,1),(1673,2022.0000,NULL,3,1),(1674,2023.0000,NULL,3,1),(1675,2024.0000,NULL,3,1),(1676,2025.0000,NULL,3,1),(1677,2026.0000,NULL,3,1),(1678,2027.0000,NULL,3,1),(1679,2028.0000,NULL,3,1),(1680,2029.0000,NULL,3,1),(1681,2030.0000,NULL,3,1),(1682,2031.0000,NULL,3,1),(1683,2032.0000,NULL,3,1),(1684,2033.0000,NULL,3,1),(1685,2034.0000,NULL,3,1),(1686,2035.0000,NULL,3,1),(1687,2036.0000,NULL,3,1),(1688,2037.0000,NULL,3,1),(1689,2038.0000,NULL,3,1),(1690,2039.0000,NULL,3,1),(1691,2040.0000,NULL,3,1),(1692,2041.0000,NULL,3,1),(1693,2042.0000,NULL,3,1),(1694,2043.0000,NULL,3,1),(1695,2044.0000,NULL,3,1),(1696,2045.0000,NULL,3,1),(1697,2046.0000,NULL,3,1),(1698,2047.0000,NULL,3,1),(1699,2048.0000,NULL,3,1),(1700,2049.0000,NULL,3,1),(1701,2050.0000,NULL,3,1),(1702,2051.0000,NULL,3,1),(1703,2052.0000,NULL,3,1),(1704,2053.0000,NULL,3,1),(1705,2054.0000,NULL,3,1),(1706,2055.0000,NULL,3,1),(1707,2056.0000,NULL,3,1),(1708,2057.0000,NULL,3,1),(1709,2058.0000,NULL,3,1),(1710,2059.0000,NULL,3,1),(1711,2060.0000,NULL,3,1),(1712,2061.0000,NULL,3,1),(1713,2062.0000,NULL,3,1),(1714,2063.0000,NULL,3,1),(1715,2064.0000,NULL,3,1),(1716,2065.0000,NULL,3,1),(1717,2066.0000,NULL,3,1),(1718,2067.0000,NULL,3,1),(1719,2068.0000,NULL,3,1),(1720,2069.0000,NULL,3,1),(1721,2070.0000,NULL,3,1),(1722,2071.0000,NULL,3,1),(1723,2072.0000,NULL,3,1),(1724,2073.0000,NULL,3,1),(1725,2074.0000,NULL,3,1),(1726,2075.0000,NULL,3,1),(1727,2076.0000,NULL,3,1),(1728,2077.0000,NULL,3,1),(1729,2078.0000,NULL,3,1),(1730,2079.0000,NULL,3,1),(1731,2080.0000,NULL,3,1),(1732,2081.0000,NULL,3,1),(1733,2082.0000,NULL,3,1),(1734,2083.0000,NULL,3,1),(1735,2084.0000,NULL,3,1),(1736,2085.0000,NULL,3,1),(1737,2086.0000,NULL,3,1),(1738,2087.0000,NULL,3,1),(1739,2088.0000,NULL,3,1),(1740,2089.0000,NULL,3,1),(1741,2090.0000,NULL,3,1),(1742,2091.0000,NULL,3,1),(1743,2092.0000,NULL,3,1),(1744,2093.0000,NULL,3,1),(1745,2094.0000,NULL,3,1),(1746,2095.0000,NULL,3,1),(1747,2096.0000,NULL,3,1),(1748,2097.0000,NULL,3,1),(1749,2098.0000,NULL,3,1),(1750,2099.0000,NULL,3,1),(1751,2100.0000,NULL,3,1),(1752,2101.0000,NULL,3,1),(1753,2102.0000,NULL,3,1),(1754,2103.0000,NULL,3,1),(1755,2104.0000,NULL,3,1),(1756,2105.0000,NULL,3,1),(1757,2106.0000,NULL,3,1),(1758,2107.0000,NULL,3,1),(1759,2108.0000,NULL,3,1),(1760,2109.0000,NULL,3,1),(1761,2110.0000,NULL,3,1),(1762,2111.0000,NULL,3,1),(1763,2112.0000,NULL,3,1),(1764,2113.0000,NULL,3,1),(1765,2114.0000,NULL,3,1),(1766,2115.0000,NULL,3,1),(1767,2116.0000,NULL,3,1),(1768,2117.0000,NULL,3,1),(1769,2118.0000,NULL,3,1),(1770,2119.0000,NULL,3,1),(1771,2120.0000,NULL,3,1),(1772,2121.0000,NULL,3,1),(1773,2122.0000,NULL,3,1),(1774,2123.0000,NULL,3,1),(1775,2124.0000,NULL,3,1),(1776,2125.0000,NULL,3,1),(1777,2126.0000,NULL,3,1),(1778,2127.0000,NULL,3,1),(1779,2128.0000,NULL,3,1),(1780,2129.0000,NULL,3,1),(1781,2130.0000,NULL,3,1),(1782,2131.0000,NULL,3,1),(1783,2132.0000,NULL,3,1),(1784,2133.0000,NULL,3,1),(1785,2134.0000,NULL,3,1),(1786,2135.0000,NULL,3,1),(1787,2136.0000,NULL,3,1),(1788,2137.0000,NULL,3,1),(1789,2138.0000,NULL,3,1),(1790,2139.0000,NULL,3,1),(1791,2140.0000,NULL,3,1),(1792,2141.0000,NULL,3,1),(1793,2142.0000,NULL,3,1),(1794,2143.0000,NULL,3,1),(1795,2144.0000,NULL,3,1),(1796,2145.0000,NULL,3,1),(1797,2146.0000,NULL,3,1),(1798,2147.0000,NULL,3,1),(1799,2148.0000,NULL,3,1),(1800,2149.0000,NULL,3,1),(1801,2150.0000,NULL,3,1),(1802,2151.0000,NULL,3,1),(1803,2152.0000,NULL,3,1),(1804,2153.0000,NULL,3,1),(1805,2154.0000,NULL,3,1),(1806,2155.0000,NULL,3,1),(1807,2156.0000,NULL,3,1),(1808,2157.0000,NULL,3,1),(1809,2158.0000,NULL,3,1),(1810,2159.0000,NULL,3,1),(1811,2160.0000,NULL,3,1),(1812,2161.0000,NULL,3,1),(1813,2162.0000,NULL,3,1),(1814,2163.0000,NULL,3,1),(1815,2164.0000,NULL,3,1),(1816,2165.0000,NULL,3,1),(1817,2166.0000,NULL,3,1),(1818,2167.0000,NULL,3,1),(1819,2168.0000,NULL,3,1),(1820,2169.0000,NULL,3,1),(1821,2170.0000,NULL,3,1),(1822,2171.0000,NULL,3,1),(1823,2172.0000,NULL,3,1),(1824,2173.0000,NULL,3,1),(1825,2174.0000,NULL,3,1),(1826,2175.0000,NULL,3,1),(1827,2176.0000,NULL,3,1),(1828,2177.0000,NULL,3,1),(1829,2178.0000,NULL,3,1),(1830,2179.0000,NULL,3,1),(1831,2180.0000,NULL,3,1),(1832,2181.0000,NULL,3,1),(1833,2182.0000,NULL,3,1),(1834,2183.0000,NULL,3,1),(1835,2184.0000,NULL,3,1),(1836,2185.0000,NULL,3,1),(1837,2186.0000,NULL,3,1),(1838,2187.0000,NULL,3,1),(1839,2188.0000,NULL,3,1),(1840,2189.0000,NULL,3,1),(1841,2190.0000,NULL,3,1),(1842,2191.0000,NULL,3,1),(1843,2192.0000,NULL,3,1),(1844,2193.0000,NULL,3,1),(1845,2194.0000,NULL,3,1),(1846,2195.0000,NULL,3,1),(1847,2196.0000,NULL,3,1),(1848,2197.0000,NULL,3,1),(1849,2198.0000,NULL,3,1),(1850,2199.0000,NULL,3,1),(1851,2200.0000,NULL,3,1),(1852,2201.0000,NULL,3,1),(1853,2202.0000,NULL,3,1),(1854,2203.0000,NULL,3,1),(1855,2204.0000,NULL,3,1),(1856,2205.0000,NULL,3,1),(1857,2206.0000,NULL,3,1),(1858,2207.0000,NULL,3,1),(1859,2208.0000,NULL,3,1),(1860,2209.0000,NULL,3,1),(1861,2210.0000,NULL,3,1),(1862,2211.0000,NULL,3,1),(1863,2212.0000,NULL,3,1),(1864,2213.0000,NULL,3,1),(1865,2214.0000,NULL,3,1),(1866,2215.0000,NULL,3,1),(1867,2216.0000,NULL,3,1),(1868,2217.0000,NULL,3,1),(1869,2218.0000,NULL,3,1),(1870,2219.0000,NULL,3,1),(1871,2220.0000,NULL,3,1),(1872,2221.0000,NULL,3,1),(1873,2222.0000,NULL,3,1),(1874,2223.0000,NULL,3,1),(1875,2224.0000,NULL,3,1),(1876,2225.0000,NULL,3,1),(1877,2226.0000,NULL,3,1),(1878,2227.0000,NULL,3,1),(1879,2228.0000,NULL,3,1),(1880,2229.0000,NULL,3,1),(1881,2230.0000,NULL,3,1),(1882,2231.0000,NULL,3,1),(1883,2232.0000,NULL,3,1),(1884,2233.0000,NULL,3,1),(1885,2234.0000,NULL,3,1),(1886,2235.0000,NULL,3,1),(1887,2236.0000,NULL,3,1),(1888,2237.0000,NULL,3,1),(1889,2238.0000,NULL,3,1),(1890,2239.0000,NULL,3,1),(1891,2240.0000,NULL,3,1),(1892,2241.0000,NULL,3,1),(1893,2242.0000,NULL,3,1),(1894,2243.0000,NULL,3,1),(1895,2244.0000,NULL,3,1),(1896,2245.0000,NULL,3,1),(1897,2246.0000,NULL,3,1),(1898,2247.0000,NULL,3,1),(1899,2248.0000,NULL,3,1),(1900,2249.0000,NULL,3,1),(1901,2250.0000,NULL,3,1),(1902,2251.0000,NULL,3,1),(1903,2252.0000,NULL,3,1),(1904,2253.0000,NULL,3,1),(1905,2254.0000,NULL,3,1),(1906,2255.0000,NULL,3,1),(1907,2256.0000,NULL,3,1),(1908,2257.0000,NULL,3,1),(1909,2258.0000,NULL,3,1),(1910,2259.0000,NULL,3,1),(1911,2260.0000,NULL,3,1),(1912,2261.0000,NULL,3,1),(1913,2262.0000,NULL,3,1),(1914,2263.0000,NULL,3,1),(1915,2264.0000,NULL,3,1),(1916,2265.0000,NULL,3,1),(1917,2266.0000,NULL,3,1),(1918,2267.0000,NULL,3,1),(1919,2268.0000,NULL,3,1),(1920,2269.0000,NULL,3,1),(1921,2270.0000,NULL,3,1),(1922,2271.0000,NULL,3,1),(1923,2272.0000,NULL,3,1),(1924,2273.0000,NULL,3,1),(1925,2274.0000,NULL,3,1),(1926,2275.0000,NULL,3,1),(1927,2276.0000,NULL,3,1),(1928,2277.0000,NULL,3,1),(1929,2278.0000,NULL,3,1),(1930,2279.0000,NULL,3,1),(1931,2280.0000,NULL,3,1),(1932,2281.0000,NULL,3,1),(1933,2282.0000,NULL,3,1),(1934,2283.0000,NULL,3,1),(1935,2284.0000,NULL,3,1),(1936,2285.0000,NULL,3,1),(1937,2286.0000,NULL,3,1),(1938,2287.0000,NULL,3,1),(1939,2288.0000,NULL,3,1),(1940,2289.0000,NULL,3,1),(1941,2290.0000,NULL,3,1),(1942,2291.0000,NULL,3,1),(1943,2292.0000,NULL,3,1),(1944,2293.0000,NULL,3,1),(1945,2294.0000,NULL,3,1),(1946,2295.0000,NULL,3,1),(1947,2296.0000,NULL,3,1),(1948,2297.0000,NULL,3,1),(1949,2298.0000,NULL,3,1),(1950,2299.0000,NULL,3,1),(1951,2300.0000,NULL,3,1),(1952,2301.0000,NULL,3,1),(1953,2302.0000,NULL,3,1),(1954,2303.0000,NULL,3,1),(1955,2304.0000,NULL,3,1),(1956,2305.0000,NULL,3,1),(1957,2306.0000,NULL,3,1),(1958,2307.0000,NULL,3,1),(1959,2308.0000,NULL,3,1),(1960,2309.0000,NULL,3,1),(1961,2310.0000,NULL,3,1),(1962,2311.0000,NULL,3,1),(1963,2312.0000,NULL,3,1),(1964,2313.0000,NULL,3,1),(1965,2314.0000,NULL,3,1),(1966,2315.0000,NULL,3,1),(1967,2316.0000,NULL,3,1),(1968,2317.0000,NULL,3,1),(1969,2318.0000,NULL,3,1),(1970,2319.0000,NULL,3,1),(1971,2320.0000,NULL,3,1),(1972,2321.0000,NULL,3,1),(1973,2322.0000,NULL,3,1),(1974,2323.0000,NULL,3,1),(1975,2324.0000,NULL,3,1),(1976,2325.0000,NULL,3,1),(1977,2326.0000,NULL,3,1),(1978,2327.0000,NULL,3,1),(1979,2328.0000,NULL,3,1),(1980,2329.0000,NULL,3,1),(1981,2330.0000,NULL,3,1),(1982,2331.0000,NULL,3,1),(1983,2332.0000,NULL,3,1),(1984,2333.0000,NULL,3,1),(1985,2334.0000,NULL,3,1),(1986,2335.0000,NULL,3,1),(1987,2336.0000,NULL,3,1),(1988,2337.0000,NULL,3,1),(1989,2338.0000,NULL,3,1),(1990,2339.0000,NULL,3,1),(1991,2340.0000,NULL,3,1),(1992,2341.0000,NULL,3,1),(1993,2342.0000,NULL,3,1),(1994,2343.0000,NULL,3,1),(1995,2344.0000,NULL,3,1),(1996,2345.0000,NULL,3,1),(1997,2346.0000,NULL,3,1),(1998,2347.0000,NULL,3,1),(1999,2348.0000,NULL,3,1),(2000,2349.0000,NULL,3,1),(2001,2350.0000,NULL,3,1),(2002,2351.0000,NULL,3,1),(2003,2352.0000,NULL,3,1),(2004,2353.0000,NULL,3,1),(2005,2354.0000,NULL,3,1),(2006,2355.0000,NULL,3,1),(2007,2356.0000,NULL,3,1),(2008,2357.0000,NULL,3,1),(2009,2358.0000,NULL,3,1),(2010,2359.0000,NULL,3,1),(2011,2360.0000,NULL,3,1),(2012,2361.0000,NULL,3,1),(2013,2362.0000,NULL,3,1),(2014,2363.0000,NULL,3,1),(2015,2364.0000,NULL,3,1),(2016,2365.0000,NULL,3,1),(2017,2366.0000,NULL,3,1),(2018,2367.0000,NULL,3,1),(2019,2368.0000,NULL,3,1),(2020,2369.0000,NULL,3,1),(2021,2370.0000,NULL,3,1),(2022,2371.0000,NULL,3,1),(2023,2372.0000,NULL,3,1),(2024,2373.0000,NULL,3,1),(2025,2374.0000,NULL,3,1),(2026,2375.0000,NULL,3,1),(2027,2376.0000,NULL,3,1),(2028,2377.0000,NULL,3,1),(2029,2378.0000,NULL,3,1),(2030,2379.0000,NULL,3,1),(2031,2380.0000,NULL,3,1),(2032,2381.0000,NULL,3,1),(2033,2382.0000,NULL,3,1),(2034,2383.0000,NULL,3,1),(2035,2384.0000,NULL,3,1),(2036,2385.0000,NULL,3,1),(2037,2386.0000,NULL,3,1),(2038,2387.0000,NULL,3,1),(2039,2388.0000,NULL,3,1),(2040,2389.0000,NULL,3,1),(2041,2390.0000,NULL,3,1),(2042,2391.0000,NULL,3,1),(2043,2392.0000,NULL,3,1),(2044,2393.0000,NULL,3,1),(2045,2394.0000,NULL,3,1),(2046,2395.0000,NULL,3,1),(2047,2396.0000,NULL,3,1),(2048,2397.0000,NULL,3,1),(2049,2398.0000,NULL,3,1),(2050,2399.0000,NULL,3,1),(2051,2400.0000,NULL,3,1),(2052,2401.0000,NULL,3,1),(2053,2402.0000,NULL,3,1),(2054,2403.0000,NULL,3,1),(2055,2404.0000,NULL,3,1),(2056,2405.0000,NULL,3,1),(2057,2406.0000,NULL,3,1),(2058,2407.0000,NULL,3,1),(2059,2408.0000,NULL,3,1),(2060,2409.0000,NULL,3,1),(2061,2410.0000,NULL,3,1),(2062,2411.0000,NULL,3,1),(2063,2412.0000,NULL,3,1),(2064,2413.0000,NULL,3,1),(2065,2414.0000,NULL,3,1),(2066,2415.0000,NULL,3,1),(2067,2416.0000,NULL,3,1),(2068,2417.0000,NULL,3,1),(2069,2418.0000,NULL,3,1),(2070,2419.0000,NULL,3,1),(2071,2420.0000,NULL,3,1),(2072,2421.0000,NULL,3,1),(2073,2422.0000,NULL,3,1),(2074,2423.0000,NULL,3,1),(2075,2424.0000,NULL,3,1),(2076,2425.0000,NULL,3,1),(2077,2426.0000,NULL,3,1),(2078,2427.0000,NULL,3,1),(2079,2428.0000,NULL,3,1),(2080,2429.0000,NULL,3,1),(2081,2430.0000,NULL,3,1),(2082,2431.0000,NULL,3,1),(2083,2432.0000,NULL,3,1),(2084,2433.0000,NULL,3,1),(2085,2434.0000,NULL,3,1),(2086,2435.0000,NULL,3,1),(2087,2436.0000,NULL,3,1),(2088,2437.0000,NULL,3,1),(2089,2438.0000,NULL,3,1),(2090,2439.0000,NULL,3,1),(2091,2440.0000,NULL,3,1),(2092,2441.0000,NULL,3,1),(2093,2442.0000,NULL,3,1),(2094,2443.0000,NULL,3,1),(2095,2444.0000,NULL,3,1),(2096,2445.0000,NULL,3,1),(2097,2446.0000,NULL,3,1),(2098,2447.0000,NULL,3,1),(2099,2448.0000,NULL,3,1),(2100,2449.0000,NULL,3,1),(2101,2450.0000,NULL,3,1),(2102,2451.0000,NULL,3,1),(2103,2452.0000,NULL,3,1),(2104,2453.0000,NULL,3,1),(2105,2454.0000,NULL,3,1),(2106,2455.0000,NULL,3,1),(2107,2456.0000,NULL,3,1),(2108,2457.0000,NULL,3,1),(2109,2458.0000,NULL,3,1),(2110,2459.0000,NULL,3,1),(2111,2460.0000,NULL,3,1),(2112,2461.0000,NULL,3,1),(2113,2462.0000,NULL,3,1),(2114,2463.0000,NULL,3,1),(2115,2464.0000,NULL,3,1),(2116,2465.0000,NULL,3,1),(2117,2466.0000,NULL,3,1),(2118,2467.0000,NULL,3,1),(2119,2468.0000,NULL,3,1),(2120,2469.0000,NULL,3,1),(2121,2470.0000,NULL,3,1),(2122,2471.0000,NULL,3,1),(2123,2472.0000,NULL,3,1),(2124,2473.0000,NULL,3,1),(2125,2474.0000,NULL,3,1),(2126,2475.0000,NULL,3,1),(2127,2476.0000,NULL,3,1),(2128,2477.0000,NULL,3,1),(2129,2478.0000,NULL,3,1),(2130,2479.0000,NULL,3,1),(2131,2480.0000,NULL,3,1),(2132,2481.0000,NULL,3,1),(2133,2482.0000,NULL,3,1),(2134,2483.0000,NULL,3,1),(2135,2484.0000,NULL,3,1),(2136,2485.0000,NULL,3,1),(2137,2486.0000,NULL,3,1),(2138,2487.0000,NULL,3,1),(2139,2488.0000,NULL,3,1),(2140,2489.0000,NULL,3,1),(2141,2490.0000,NULL,3,1),(2142,2491.0000,NULL,3,1),(2143,2492.0000,NULL,3,1),(2144,2493.0000,NULL,3,1),(2145,2494.0000,NULL,3,1),(2146,2495.0000,NULL,3,1),(2147,2496.0000,NULL,3,1),(2148,2497.0000,NULL,3,1),(2149,2498.0000,NULL,3,1),(2150,2499.0000,NULL,3,1),(2151,2500.0000,NULL,3,1),(2152,298.6050,NULL,4,1),(2153,300.2450,NULL,4,1),(2154,301.8900,NULL,4,1),(2155,303.5250,NULL,4,1),(2156,305.1550,NULL,4,1),(2157,306.7800,NULL,4,1),(2158,308.4050,NULL,4,1),(2159,310.0200,NULL,4,1),(2160,311.6350,NULL,4,1),(2161,313.2450,NULL,4,1),(2162,314.8500,NULL,4,1),(2163,316.4550,NULL,4,1),(2164,318.0500,NULL,4,1),(2165,319.6450,NULL,4,1),(2166,321.2350,NULL,4,1),(2167,322.8200,NULL,4,1),(2168,324.4000,NULL,4,1),(2169,325.9800,NULL,4,1),(2170,327.5550,NULL,4,1),(2171,329.1250,NULL,4,1),(2172,330.6950,NULL,4,1),(2173,332.2650,NULL,4,1),(2174,333.8250,NULL,4,1),(2175,335.3800,NULL,4,1),(2176,336.9400,NULL,4,1),(2177,338.4950,NULL,4,1),(2178,340.0450,NULL,4,1),(2179,341.5950,NULL,4,1),(2180,343.1350,NULL,4,1),(2181,344.6800,NULL,4,1),(2182,346.2250,NULL,4,1),(2183,347.7600,NULL,4,1),(2184,349.2950,NULL,4,1),(2185,350.8300,NULL,4,1),(2186,352.3600,NULL,4,1),(2187,353.8900,NULL,4,1),(2188,355.4200,NULL,4,1),(2189,356.9400,NULL,4,1),(2190,358.4650,NULL,4,1),(2191,359.9850,NULL,4,1),(2192,361.5000,NULL,4,1),(2193,363.0200,NULL,4,1),(2194,364.5350,NULL,4,1),(2195,366.0450,NULL,4,1),(2196,367.5600,NULL,4,1),(2197,369.0700,NULL,4,1),(2198,370.5800,NULL,4,1),(2199,372.0900,NULL,4,1),(2200,373.5900,NULL,4,1),(2201,375.1000,NULL,4,1),(2202,376.6000,NULL,4,1),(2203,378.1000,NULL,4,1),(2204,379.6000,NULL,4,1),(2205,381.1000,NULL,4,1),(2206,382.6000,NULL,4,1),(2207,384.1000,NULL,4,1),(2208,385.5900,NULL,4,1),(2209,387.0900,NULL,4,1),(2210,388.5800,NULL,4,1),(2211,390.0750,NULL,4,1),(2212,391.5700,NULL,4,1),(2213,393.0600,NULL,4,1),(2214,394.5550,NULL,4,1),(2215,396.0400,NULL,4,1),(2216,397.5300,NULL,4,1),(2217,399.0200,NULL,4,1),(2218,400.5100,NULL,4,1),(2219,401.9900,NULL,4,1),(2220,403.4800,NULL,4,1),(2221,404.9650,NULL,4,1),(2222,406.4550,NULL,4,1),(2223,407.9350,NULL,4,1),(2224,409.4250,NULL,4,1),(2225,410.9050,NULL,4,1),(2226,412.3900,NULL,4,1),(2227,413.8700,NULL,4,1),(2228,415.3600,NULL,4,1),(2229,416.8400,NULL,4,1),(2230,418.3200,NULL,4,1),(2231,419.8050,NULL,4,1),(2232,421.2850,NULL,4,1),(2233,422.7650,NULL,4,1),(2234,424.2500,NULL,4,1),(2235,425.7350,NULL,4,1),(2236,427.2150,NULL,4,1),(2237,428.6950,NULL,4,1),(2238,430.1800,NULL,4,1),(2239,431.6600,NULL,4,1),(2240,433.1400,NULL,4,1),(2241,434.6200,NULL,4,1),(2242,436.1000,NULL,4,1),(2243,437.5850,NULL,4,1),(2244,439.0650,NULL,4,1),(2245,440.5450,NULL,4,1),(2246,442.0300,NULL,4,1),(2247,443.5100,NULL,4,1),(2248,444.9950,NULL,4,1),(2249,446.4750,NULL,4,1),(2250,447.9600,NULL,4,1),(2251,449.4400,NULL,4,1),(2252,450.9250,NULL,4,1),(2253,452.4050,NULL,4,1),(2254,453.8900,NULL,4,1),(2255,455.3750,NULL,4,1),(2256,456.8600,NULL,4,1),(2257,458.3400,NULL,4,1),(2258,459.8300,NULL,4,1),(2259,461.3100,NULL,4,1),(2260,462.8000,NULL,4,1),(2261,464.2800,NULL,4,1),(2262,465.7700,NULL,4,1),(2263,467.2550,NULL,4,1),(2264,468.7450,NULL,4,1),(2265,470.2250,NULL,4,1),(2266,471.7150,NULL,4,1),(2267,473.2000,NULL,4,1),(2268,474.6900,NULL,4,1),(2269,476.1800,NULL,4,1),(2270,477.6700,NULL,4,1),(2271,479.1550,NULL,4,1),(2272,480.6500,NULL,4,1),(2273,482.1400,NULL,4,1),(2274,483.6300,NULL,4,1),(2275,485.1200,NULL,4,1),(2276,486.6150,NULL,4,1),(2277,488.1050,NULL,4,1),(2278,489.6000,NULL,4,1),(2279,491.0900,NULL,4,1),(2280,492.5850,NULL,4,1),(2281,494.0750,NULL,4,1),(2282,495.5750,NULL,4,1),(2283,497.0700,NULL,4,1),(2284,498.5650,NULL,4,1),(2285,500.0650,NULL,4,1),(2286,501.5600,NULL,4,1),(2287,503.0550,NULL,4,1),(2288,504.5550,NULL,4,1),(2289,506.0550,NULL,4,1),(2290,507.5500,NULL,4,1),(2291,509.0500,NULL,4,1),(2292,510.5500,NULL,4,1),(2293,512.0500,NULL,4,1),(2294,513.5500,NULL,4,1),(2295,515.0550,NULL,4,1),(2296,516.5550,NULL,4,1),(2297,518.0600,NULL,4,1),(2298,519.5600,NULL,4,1),(2299,521.0650,NULL,4,1),(2300,522.5700,NULL,4,1),(2301,524.0750,NULL,4,1),(2302,525.5800,NULL,4,1),(2303,527.0800,NULL,4,1),(2304,528.5900,NULL,4,1),(2305,530.0950,NULL,4,1),(2306,531.6050,NULL,4,1),(2307,533.1150,NULL,4,1),(2308,534.6200,NULL,4,1),(2309,536.1300,NULL,4,1),(2310,537.6350,NULL,4,1),(2311,539.1500,NULL,4,1),(2312,540.6600,NULL,4,1),(2313,542.1700,NULL,4,1),(2314,543.6800,NULL,4,1),(2315,545.1950,NULL,4,1),(2316,546.7050,NULL,4,1),(2317,548.2200,NULL,4,1),(2318,549.7350,NULL,4,1),(2319,551.2450,NULL,4,1),(2320,552.7600,NULL,4,1),(2321,554.2750,NULL,4,1),(2322,555.7900,NULL,4,1),(2323,557.3050,NULL,4,1),(2324,558.8250,NULL,4,1),(2325,560.3400,NULL,4,1),(2326,561.8550,NULL,4,1),(2327,563.3750,NULL,4,1),(2328,564.8900,NULL,4,1),(2329,566.4100,NULL,4,1),(2330,567.9300,NULL,4,1),(2331,569.4500,NULL,4,1),(2332,570.9650,NULL,4,1),(2333,572.4850,NULL,4,1),(2334,574.0100,NULL,4,1),(2335,575.5300,NULL,4,1),(2336,577.0500,NULL,4,1),(2337,578.5700,NULL,4,1),(2338,580.0950,NULL,4,1),(2339,581.6150,NULL,4,1),(2340,583.1400,NULL,4,1),(2341,584.6650,NULL,4,1),(2342,586.1850,NULL,4,1),(2343,587.7100,NULL,4,1),(2344,589.2350,NULL,4,1),(2345,590.7650,NULL,4,1),(2346,592.2850,NULL,4,1),(2347,593.8100,NULL,4,1),(2348,595.3350,NULL,4,1),(2349,596.8650,NULL,4,1),(2350,598.3900,NULL,4,1),(2351,599.9150,NULL,4,1),(2352,601.4450,NULL,4,1),(2353,602.9700,NULL,4,1),(2354,604.5000,NULL,4,1),(2355,606.0250,NULL,4,1),(2356,607.5550,NULL,4,1),(2357,609.0850,NULL,4,1),(2358,610.6100,NULL,4,1),(2359,612.1400,NULL,4,1),(2360,613.6700,NULL,4,1),(2361,615.2050,NULL,4,1),(2362,616.7300,NULL,4,1),(2363,618.2600,NULL,4,1),(2364,619.7900,NULL,4,1),(2365,621.3250,NULL,4,1),(2366,622.8550,NULL,4,1),(2367,624.3850,NULL,4,1),(2368,625.9150,NULL,4,1),(2369,627.4500,NULL,4,1),(2370,628.9800,NULL,4,1),(2371,630.5100,NULL,4,1),(2372,632.0450,NULL,4,1),(2373,633.5750,NULL,4,1),(2374,635.1050,NULL,4,1),(2375,636.6450,NULL,4,1),(2376,638.1750,NULL,4,1),(2377,639.7100,NULL,4,1),(2378,641.2400,NULL,4,1),(2379,642.7800,NULL,4,1),(2380,644.3100,NULL,4,1),(2381,645.8400,NULL,4,1),(2382,647.3800,NULL,4,1),(2383,648.9100,NULL,4,1),(2384,650.4450,NULL,4,1),(2385,651.9800,NULL,4,1),(2386,653.5150,NULL,4,1),(2387,655.0500,NULL,4,1),(2388,656.5850,NULL,4,1),(2389,658.1200,NULL,4,1),(2390,659.6550,NULL,4,1),(2391,661.1900,NULL,4,1),(2392,662.7250,NULL,4,1),(2393,664.2600,NULL,4,1),(2394,665.7950,NULL,4,1),(2395,667.3300,NULL,4,1),(2396,668.8700,NULL,4,1),(2397,670.4000,NULL,4,1),(2398,671.9400,NULL,4,1),(2399,673.4750,NULL,4,1),(2400,675.0100,NULL,4,1),(2401,676.5500,NULL,4,1),(2402,678.0850,NULL,4,1),(2403,679.6200,NULL,4,1),(2404,681.1550,NULL,4,1),(2405,682.6950,NULL,4,1),(2406,684.2300,NULL,4,1),(2407,685.7700,NULL,4,1),(2408,687.3000,NULL,4,1),(2409,688.8400,NULL,4,1),(2410,690.3750,NULL,4,1),(2411,691.9150,NULL,4,1),(2412,693.4500,NULL,4,1),(2413,694.9900,NULL,4,1),(2414,696.5250,NULL,4,1),(2415,698.0600,NULL,4,1),(2416,699.6000,NULL,4,1),(2417,701.1350,NULL,4,1),(2418,702.6750,NULL,4,1),(2419,704.2100,NULL,4,1),(2420,705.7500,NULL,4,1),(2421,707.2850,NULL,4,1),(2422,708.8250,NULL,4,1),(2423,710.3600,NULL,4,1),(2424,711.9000,NULL,4,1),(2425,713.4350,NULL,4,1),(2426,714.9750,NULL,4,1),(2427,716.5150,NULL,4,1),(2428,718.0500,NULL,4,1),(2429,719.5900,NULL,4,1),(2430,721.1250,NULL,4,1),(2431,722.6650,NULL,4,1),(2432,724.2000,NULL,4,1),(2433,725.7400,NULL,4,1),(2434,727.2750,NULL,4,1),(2435,728.8150,NULL,4,1),(2436,730.3500,NULL,4,1),(2437,731.8900,NULL,4,1),(2438,733.4300,NULL,4,1),(2439,734.9650,NULL,4,1),(2440,736.5050,NULL,4,1),(2441,738.0400,NULL,4,1),(2442,739.5800,NULL,4,1),(2443,741.1150,NULL,4,1),(2444,742.6600,NULL,4,1),(2445,744.2000,NULL,4,1),(2446,745.7350,NULL,4,1),(2447,747.2750,NULL,4,1),(2448,748.8100,NULL,4,1),(2449,750.3500,NULL,4,1),(2450,751.8900,NULL,4,1),(2451,753.4250,NULL,4,1),(2452,754.9700,NULL,4,1),(2453,756.5050,NULL,4,1),(2454,758.0450,NULL,4,1),(2455,759.5850,NULL,4,1),(2456,761.1200,NULL,4,1),(2457,762.6600,NULL,4,1),(2458,764.2050,NULL,4,1),(2459,765.7450,NULL,4,1),(2460,767.2800,NULL,4,1),(2461,768.8200,NULL,4,1),(2462,770.3650,NULL,4,1),(2463,771.9000,NULL,4,1),(2464,773.4400,NULL,4,1),(2465,774.9800,NULL,4,1),(2466,776.5200,NULL,4,1),(2467,778.0650,NULL,4,1),(2468,779.6050,NULL,4,1),(2469,781.1400,NULL,4,1),(2470,782.6850,NULL,4,1),(2471,784.2250,NULL,4,1),(2472,785.7650,NULL,4,1),(2473,787.3100,NULL,4,1),(2474,788.8500,NULL,4,1),(2475,790.3900,NULL,4,1),(2476,791.9350,NULL,4,1),(2477,793.4750,NULL,4,1),(2478,795.0150,NULL,4,1),(2479,796.5600,NULL,4,1),(2480,798.1000,NULL,4,1),(2481,799.6400,NULL,4,1),(2482,801.1900,NULL,4,1),(2483,802.7300,NULL,4,1),(2484,804.2750,NULL,4,1),(2485,805.8150,NULL,4,1),(2486,807.3650,NULL,4,1),(2487,808.9050,NULL,4,1),(2488,810.4500,NULL,4,1),(2489,811.9950,NULL,4,1),(2490,813.5400,NULL,4,1),(2491,815.0800,NULL,4,1),(2492,816.6300,NULL,4,1),(2493,818.1750,NULL,4,1),(2494,819.7200,NULL,4,1),(2495,821.2700,NULL,4,1),(2496,822.8150,NULL,4,1),(2497,824.3600,NULL,4,1),(2498,825.9050,NULL,4,1),(2499,827.4550,NULL,4,1),(2500,829.0000,NULL,4,1),(2501,830.5500,NULL,4,1),(2502,832.1000,NULL,4,1),(2503,833.6500,NULL,4,1),(2504,835.2000,NULL,4,1),(2505,836.7450,NULL,4,1),(2506,838.2950,NULL,4,1),(2507,839.8450,NULL,4,1),(2508,841.3950,NULL,4,1),(2509,842.9450,NULL,4,1),(2510,844.4950,NULL,4,1),(2511,846.0450,NULL,4,1),(2512,847.6000,NULL,4,1),(2513,849.1500,NULL,4,1),(2514,850.7050,NULL,4,1),(2515,852.2600,NULL,4,1),(2516,853.8150,NULL,4,1),(2517,855.3650,NULL,4,1),(2518,856.9200,NULL,4,1),(2519,858.4750,NULL,4,1),(2520,860.0300,NULL,4,1),(2521,861.5850,NULL,4,1),(2522,863.1450,NULL,4,1),(2523,864.7000,NULL,4,1),(2524,866.2600,NULL,4,1),(2525,867.8150,NULL,4,1),(2526,869.3750,NULL,4,1),(2527,870.9300,NULL,4,1),(2528,872.4900,NULL,4,1),(2529,874.0450,NULL,4,1),(2530,875.6100,NULL,4,1),(2531,877.1700,NULL,4,1),(2532,878.7300,NULL,4,1),(2533,880.2950,NULL,4,1),(2534,881.8550,NULL,4,1),(2535,883.4150,NULL,4,1),(2536,884.9800,NULL,4,1),(2537,886.5450,NULL,4,1),(2538,888.1100,NULL,4,1),(2539,889.6750,NULL,4,1),(2540,891.2400,NULL,4,1),(2541,892.8050,NULL,4,1),(2542,894.3750,NULL,4,1),(2543,895.9400,NULL,4,1),(2544,897.5050,NULL,4,1),(2545,899.0750,NULL,4,1),(2546,900.6450,NULL,4,1),(2547,902.2150,NULL,4,1),(2548,903.7900,NULL,4,1),(2549,905.3550,NULL,4,1),(2550,906.9300,NULL,4,1),(2551,908.5000,NULL,4,1),(2552,910.0750,NULL,4,1),(2553,911.6450,NULL,4,1),(2554,913.2200,NULL,4,1),(2555,914.7950,NULL,4,1),(2556,916.3700,NULL,4,1),(2557,917.9450,NULL,4,1),(2558,919.5250,NULL,4,1),(2559,921.1000,NULL,4,1),(2560,922.6750,NULL,4,1),(2561,924.2550,NULL,4,1),(2562,925.8350,NULL,4,1),(2563,927.4150,NULL,4,1),(2564,928.9950,NULL,4,1),(2565,930.5800,NULL,4,1),(2566,932.1600,NULL,4,1),(2567,933.7400,NULL,4,1),(2568,935.3250,NULL,4,1),(2569,936.9100,NULL,4,1),(2570,938.4950,NULL,4,1),(2571,940.0800,NULL,4,1),(2572,941.6650,NULL,4,1),(2573,943.2500,NULL,4,1),(2574,944.8400,NULL,4,1),(2575,946.4250,NULL,4,1),(2576,948.0150,NULL,4,1),(2577,949.6050,NULL,4,1),(2578,951.1950,NULL,4,1),(2579,952.7800,NULL,4,1),(2580,954.3750,NULL,4,1),(2581,955.9700,NULL,4,1),(2582,957.5600,NULL,4,1),(2583,959.1500,NULL,4,1),(2584,960.7450,NULL,4,1),(2585,962.3400,NULL,4,1),(2586,963.9350,NULL,4,1),(2587,965.5350,NULL,4,1),(2588,967.1250,NULL,4,1),(2589,968.7200,NULL,4,1),(2590,970.3200,NULL,4,1),(2591,971.9200,NULL,4,1),(2592,973.5200,NULL,4,1),(2593,975.1200,NULL,4,1),(2594,976.7200,NULL,4,1),(2595,978.3200,NULL,4,1),(2596,979.9200,NULL,4,1),(2597,981.5200,NULL,4,1),(2598,983.1200,NULL,4,1),(2599,984.7250,NULL,4,1),(2600,986.3300,NULL,4,1),(2601,987.9350,NULL,4,1),(2602,989.5400,NULL,4,1),(2603,991.1400,NULL,4,1),(2604,992.7500,NULL,4,1),(2605,994.3550,NULL,4,1),(2606,995.9600,NULL,4,1),(2607,997.5700,NULL,4,1),(2608,999.1750,NULL,4,1),(2609,1008.2300,NULL,4,1),(2610,1017.8200,NULL,4,1),(2611,1027.3600,NULL,4,1),(2612,1036.8500,NULL,4,1),(2613,1046.3101,NULL,4,1),(2614,1055.7300,NULL,4,1),(2615,1065.1000,NULL,4,1),(2616,1074.4399,NULL,4,1),(2617,1083.7300,NULL,4,1),(2618,1092.9800,NULL,4,1),(2619,1102.1899,NULL,4,1),(2620,1111.3600,NULL,4,1),(2621,1120.4900,NULL,4,1),(2622,1129.5699,NULL,4,1),(2623,1138.6300,NULL,4,1),(2624,1147.6300,NULL,4,1),(2625,1156.5900,NULL,4,1),(2626,1165.5100,NULL,4,1),(2627,1174.4000,NULL,4,1),(2628,1183.2300,NULL,4,1),(2629,1192.0300,NULL,4,1),(2630,1200.7900,NULL,4,1),(2631,1209.5000,NULL,4,1),(2632,1218.1801,NULL,4,1),(2633,1226.8101,NULL,4,1),(2634,1235.4100,NULL,4,1),(2635,1243.9500,NULL,4,1),(2636,1252.4600,NULL,4,1),(2637,1260.9301,NULL,4,1),(2638,1269.3600,NULL,4,1),(2639,1277.7400,NULL,4,1),(2640,1286.0900,NULL,4,1),(2641,1294.4000,NULL,4,1),(2642,1302.6600,NULL,4,1),(2643,1310.8800,NULL,4,1),(2644,1319.0601,NULL,4,1),(2645,1327.1899,NULL,4,1),(2646,1335.2900,NULL,4,1),(2647,1343.3400,NULL,4,1),(2648,1351.3600,NULL,4,1),(2649,1359.3300,NULL,4,1),(2650,1367.2700,NULL,4,1),(2651,1375.1600,NULL,4,1),(2652,1383.0100,NULL,4,1),(2653,1390.8199,NULL,4,1),(2654,1398.5800,NULL,4,1),(2655,1406.3101,NULL,4,1),(2656,1413.9900,NULL,4,1),(2657,1421.6400,NULL,4,1),(2658,1429.2400,NULL,4,1),(2659,1436.8000,NULL,4,1),(2660,1444.3199,NULL,4,1),(2661,1451.8000,NULL,4,1),(2662,1459.2300,NULL,4,1),(2663,1466.6300,NULL,4,1),(2664,1473.9800,NULL,4,1),(2665,1481.3000,NULL,4,1),(2666,1488.5601,NULL,4,1),(2667,1495.8000,NULL,4,1),(2668,1502.9800,NULL,4,1),(2669,1510.1400,NULL,4,1),(2670,1517.2400,NULL,4,1),(2671,1524.3101,NULL,4,1),(2672,1531.3300,NULL,4,1),(2673,1538.3101,NULL,4,1),(2674,1545.2600,NULL,4,1),(2675,1552.1600,NULL,4,1),(2676,1559.0100,NULL,4,1),(2677,1565.8300,NULL,4,1),(2678,1572.6000,NULL,4,1),(2679,1579.3300,NULL,4,1),(2680,1586.0300,NULL,4,1),(2681,1592.6899,NULL,4,1),(2682,1599.3000,NULL,4,1),(2683,1605.8600,NULL,4,1),(2684,1612.3900,NULL,4,1),(2685,1618.8800,NULL,4,1),(2686,1625.3199,NULL,4,1),(2687,1631.7200,NULL,4,1),(2688,1638.0800,NULL,4,1),(2689,1644.4100,NULL,4,1),(2690,1650.6899,NULL,4,1),(2691,1656.9301,NULL,4,1),(2692,1663.1100,NULL,4,1),(2693,1669.2800,NULL,4,1),(2694,1675.3900,NULL,4,1),(2695,1681.4600,NULL,4,1),(2696,1687.4900,NULL,4,1),(2697,1693.4800,NULL,4,1),(2698,1699.4301,NULL,4,1),(2699,1705.3300,NULL,4,1),(2700,1711.2000,NULL,4,1),(2701,1717.0300,NULL,4,1),(2702,1722.8101,NULL,4,1),(2703,1728.5500,NULL,4,1),(2704,1734.2400,NULL,4,1),(2705,1739.9100,NULL,4,1),(2706,1745.5200,NULL,4,1),(2707,1751.0900,NULL,4,1),(2708,1756.6300,NULL,4,1),(2709,1762.1200,NULL,4,1),(2710,1767.5699,NULL,4,1),(2711,1772.9800,NULL,4,1),(2712,1778.3500,NULL,4,1),(2713,1783.6801,NULL,4,1),(2714,1788.9600,NULL,4,1),(2715,1794.2100,NULL,4,1),(2716,1799.4100,NULL,4,1),(2717,1804.5699,NULL,4,1),(2718,1809.6899,NULL,4,1),(2719,1814.7700,NULL,4,1),(2720,1819.8101,NULL,4,1),(2721,1824.8000,NULL,4,1),(2722,1829.7500,NULL,4,1),(2723,1834.6700,NULL,4,1),(2724,1839.5400,NULL,4,1),(2725,1844.3600,NULL,4,1),(2726,1849.1500,NULL,4,1),(2727,1853.9000,NULL,4,1),(2728,1858.6000,NULL,4,1),(2729,1863.2700,NULL,4,1),(2730,1867.8900,NULL,4,1),(2731,1872.4600,NULL,4,1),(2732,1877.0100,NULL,4,1),(2733,1881.5000,NULL,4,1),(2734,1885.9500,NULL,4,1),(2735,1902.8900,NULL,4,1),(2736,1913.8400,NULL,4,1),(2737,1924.7400,NULL,4,1),(2738,1935.6000,NULL,4,1),(2739,1946.4200,NULL,4,1),(2740,1957.1801,NULL,4,1),(2741,1967.9000,NULL,4,1),(2742,1978.5601,NULL,4,1),(2743,1989.1899,NULL,4,1),(2744,1999.7700,NULL,4,1),(2745,2010.3000,NULL,4,1),(2746,2020.7700,NULL,4,1),(2747,2031.2000,NULL,4,1),(2748,2041.5900,NULL,4,1),(2749,2051.9399,NULL,4,1),(2750,2062.2300,NULL,4,1),(2751,2072.4800,NULL,4,1),(2752,2082.6699,NULL,4,1),(2753,2092.8201,NULL,4,1),(2754,2102.9399,NULL,4,1),(2755,2112.9900,NULL,4,1),(2756,2123.0000,NULL,4,1),(2757,2132.9600,NULL,4,1),(2758,2142.8799,NULL,4,1),(2759,2152.7500,NULL,4,1),(2760,2162.5701,NULL,4,1),(2761,2172.3401,NULL,4,1),(2762,2182.0701,NULL,4,1),(2763,2191.7500,NULL,4,1),(2764,2201.3899,NULL,4,1),(2765,2210.9800,NULL,4,1),(2766,2220.5100,NULL,4,1),(2767,2230.0100,NULL,4,1),(2768,2239.4600,NULL,4,1),(2769,2248.8501,NULL,4,1),(2770,2258.2100,NULL,4,1),(2771,2267.5100,NULL,4,1),(2772,2276.7600,NULL,4,1),(2773,2285.9800,NULL,4,1),(2774,2295.1399,NULL,4,1),(2775,2304.2500,NULL,4,1),(2776,2313.3201,NULL,4,1),(2777,2322.3401,NULL,4,1),(2778,2331.3201,NULL,4,1),(2779,2340.2500,NULL,4,1),(2780,2349.1299,NULL,4,1),(2781,2357.9700,NULL,4,1),(2782,2366.7500,NULL,4,1),(2783,2375.4900,NULL,4,1),(2784,2384.1799,NULL,4,1),(2785,2392.8201,NULL,4,1),(2786,2401.4299,NULL,4,1),(2787,2409.9700,NULL,4,1),(2788,2418.4800,NULL,4,1),(2789,2426.9299,NULL,4,1),(2790,2435.3401,NULL,4,1),(2791,2443.7100,NULL,4,1),(2792,2452.0200,NULL,4,1),(2793,2460.2900,NULL,4,1),(2794,2468.5100,NULL,4,1),(2795,2476.6899,NULL,4,1),(2796,2484.8101,NULL,4,1),(2797,2492.8999,NULL,4,1),(2798,2500.9299,NULL,4,1),(2799,673.0000,NULL,6,2),(2800,413.7000,NULL,6,1),(2801,496.4000,NULL,6,1),(2802,612.7000,NULL,6,1),(2803,671.3000,NULL,6,1),(2804,867.2000,NULL,6,1),(2805,935.8000,NULL,6,1),(2806,400.0000,NULL,7,1),(2807,401.0000,NULL,7,1),(2808,402.0000,NULL,7,1),(2809,403.0000,NULL,7,1),(2810,404.0000,NULL,7,1),(2811,405.0000,NULL,7,1),(2812,406.0000,NULL,7,1),(2813,407.0000,NULL,7,1),(2814,408.0000,NULL,7,1),(2815,409.0000,NULL,7,1),(2816,410.0000,NULL,7,1),(2817,411.0000,NULL,7,1),(2818,412.0000,NULL,7,1),(2819,413.0000,NULL,7,1),(2820,414.0000,NULL,7,1),(2821,415.0000,NULL,7,1),(2822,416.0000,NULL,7,1),(2823,417.0000,NULL,7,1),(2824,418.0000,NULL,7,1),(2825,419.0000,NULL,7,1),(2826,420.0000,NULL,7,1),(2827,421.0000,NULL,7,1),(2828,422.0000,NULL,7,1),(2829,423.0000,NULL,7,1),(2830,424.0000,NULL,7,1),(2831,425.0000,NULL,7,1),(2832,426.0000,NULL,7,1),(2833,427.0000,NULL,7,1),(2834,428.0000,NULL,7,1),(2835,429.0000,NULL,7,1),(2836,430.0000,NULL,7,1),(2837,431.0000,NULL,7,1),(2838,432.0000,NULL,7,1),(2839,433.0000,NULL,7,1),(2840,434.0000,NULL,7,1),(2841,435.0000,NULL,7,1),(2842,436.0000,NULL,7,1),(2843,437.0000,NULL,7,1),(2844,438.0000,NULL,7,1),(2845,439.0000,NULL,7,1),(2846,440.0000,NULL,7,1),(2847,441.0000,NULL,7,1),(2848,442.0000,NULL,7,1),(2849,443.0000,NULL,7,1),(2850,444.0000,NULL,7,1),(2851,445.0000,NULL,7,1),(2852,446.0000,NULL,7,1),(2853,447.0000,NULL,7,1),(2854,448.0000,NULL,7,1),(2855,449.0000,NULL,7,1),(2856,450.0000,NULL,7,1),(2857,451.0000,NULL,7,1),(2858,452.0000,NULL,7,1),(2859,453.0000,NULL,7,1),(2860,454.0000,NULL,7,1),(2861,455.0000,NULL,7,1),(2862,456.0000,NULL,7,1),(2863,457.0000,NULL,7,1),(2864,458.0000,NULL,7,1),(2865,459.0000,NULL,7,1),(2866,460.0000,NULL,7,1),(2867,461.0000,NULL,7,1),(2868,462.0000,NULL,7,1),(2869,463.0000,NULL,7,1),(2870,464.0000,NULL,7,1),(2871,465.0000,NULL,7,1),(2872,466.0000,NULL,7,1),(2873,467.0000,NULL,7,1),(2874,468.0000,NULL,7,1),(2875,469.0000,NULL,7,1),(2876,470.0000,NULL,7,1),(2877,471.0000,NULL,7,1),(2878,472.0000,NULL,7,1),(2879,473.0000,NULL,7,1),(2880,474.0000,NULL,7,1),(2881,475.0000,NULL,7,1),(2882,476.0000,NULL,7,1),(2883,477.0000,NULL,7,1),(2884,478.0000,NULL,7,1),(2885,479.0000,NULL,7,1),(2886,480.0000,NULL,7,1),(2887,481.0000,NULL,7,1),(2888,482.0000,NULL,7,1),(2889,483.0000,NULL,7,1),(2890,484.0000,NULL,7,1),(2891,485.0000,NULL,7,1),(2892,486.0000,NULL,7,1),(2893,487.0000,NULL,7,1),(2894,488.0000,NULL,7,1),(2895,489.0000,NULL,7,1),(2896,490.0000,NULL,7,1),(2897,491.0000,NULL,7,1),(2898,492.0000,NULL,7,1),(2899,493.0000,NULL,7,1),(2900,494.0000,NULL,7,1),(2901,495.0000,NULL,7,1),(2902,496.0000,NULL,7,1),(2903,497.0000,NULL,7,1),(2904,498.0000,NULL,7,1),(2905,499.0000,NULL,7,1),(2906,500.0000,NULL,7,1),(2907,501.0000,NULL,7,1),(2908,502.0000,NULL,7,1),(2909,503.0000,NULL,7,1),(2910,504.0000,NULL,7,1),(2911,505.0000,NULL,7,1),(2912,506.0000,NULL,7,1),(2913,507.0000,NULL,7,1),(2914,508.0000,NULL,7,1),(2915,509.0000,NULL,7,1),(2916,510.0000,NULL,7,1),(2917,511.0000,NULL,7,1),(2918,512.0000,NULL,7,1),(2919,513.0000,NULL,7,1),(2920,514.0000,NULL,7,1),(2921,515.0000,NULL,7,1),(2922,516.0000,NULL,7,1),(2923,517.0000,NULL,7,1),(2924,518.0000,NULL,7,1),(2925,519.0000,NULL,7,1),(2926,520.0000,NULL,7,1),(2927,521.0000,NULL,7,1),(2928,522.0000,NULL,7,1),(2929,523.0000,NULL,7,1),(2930,524.0000,NULL,7,1),(2931,525.0000,NULL,7,1),(2932,526.0000,NULL,7,1),(2933,527.0000,NULL,7,1),(2934,528.0000,NULL,7,1),(2935,529.0000,NULL,7,1),(2936,530.0000,NULL,7,1),(2937,531.0000,NULL,7,1),(2938,532.0000,NULL,7,1),(2939,533.0000,NULL,7,1),(2940,534.0000,NULL,7,1),(2941,535.0000,NULL,7,1),(2942,536.0000,NULL,7,1),(2943,537.0000,NULL,7,1),(2944,538.0000,NULL,7,1),(2945,539.0000,NULL,7,1),(2946,540.0000,NULL,7,1),(2947,541.0000,NULL,7,1),(2948,542.0000,NULL,7,1),(2949,543.0000,NULL,7,1),(2950,544.0000,NULL,7,1),(2951,545.0000,NULL,7,1),(2952,546.0000,NULL,7,1),(2953,547.0000,NULL,7,1),(2954,548.0000,NULL,7,1),(2955,549.0000,NULL,7,1),(2956,550.0000,NULL,7,1),(2957,551.0000,NULL,7,1),(2958,552.0000,NULL,7,1),(2959,553.0000,NULL,7,1),(2960,554.0000,NULL,7,1),(2961,555.0000,NULL,7,1),(2962,556.0000,NULL,7,1),(2963,557.0000,NULL,7,1),(2964,558.0000,NULL,7,1),(2965,559.0000,NULL,7,1),(2966,560.0000,NULL,7,1),(2967,561.0000,NULL,7,1),(2968,562.0000,NULL,7,1),(2969,563.0000,NULL,7,1),(2970,564.0000,NULL,7,1),(2971,565.0000,NULL,7,1),(2972,566.0000,NULL,7,1),(2973,567.0000,NULL,7,1),(2974,568.0000,NULL,7,1),(2975,569.0000,NULL,7,1),(2976,570.0000,NULL,7,1),(2977,571.0000,NULL,7,1),(2978,572.0000,NULL,7,1),(2979,573.0000,NULL,7,1),(2980,574.0000,NULL,7,1),(2981,575.0000,NULL,7,1),(2982,576.0000,NULL,7,1),(2983,577.0000,NULL,7,1),(2984,578.0000,NULL,7,1),(2985,579.0000,NULL,7,1),(2986,580.0000,NULL,7,1),(2987,581.0000,NULL,7,1),(2988,582.0000,NULL,7,1),(2989,583.0000,NULL,7,1),(2990,584.0000,NULL,7,1),(2991,585.0000,NULL,7,1),(2992,586.0000,NULL,7,1),(2993,587.0000,NULL,7,1),(2994,588.0000,NULL,7,1),(2995,589.0000,NULL,7,1),(2996,590.0000,NULL,7,1),(2997,591.0000,NULL,7,1),(2998,592.0000,NULL,7,1),(2999,593.0000,NULL,7,1),(3000,594.0000,NULL,7,1),(3001,595.0000,NULL,7,1),(3002,596.0000,NULL,7,1),(3003,597.0000,NULL,7,1),(3004,598.0000,NULL,7,1),(3005,599.0000,NULL,7,1),(3006,600.0000,NULL,7,1),(3007,601.0000,NULL,7,1),(3008,602.0000,NULL,7,1),(3009,603.0000,NULL,7,1),(3010,604.0000,NULL,7,1),(3011,605.0000,NULL,7,1),(3012,606.0000,NULL,7,1),(3013,607.0000,NULL,7,1),(3014,608.0000,NULL,7,1),(3015,609.0000,NULL,7,1),(3016,610.0000,NULL,7,1),(3017,611.0000,NULL,7,1),(3018,612.0000,NULL,7,1),(3019,613.0000,NULL,7,1),(3020,614.0000,NULL,7,1),(3021,615.0000,NULL,7,1),(3022,616.0000,NULL,7,1),(3023,617.0000,NULL,7,1),(3024,618.0000,NULL,7,1),(3025,619.0000,NULL,7,1),(3026,620.0000,NULL,7,1),(3027,621.0000,NULL,7,1),(3028,622.0000,NULL,7,1),(3029,623.0000,NULL,7,1),(3030,624.0000,NULL,7,1),(3031,625.0000,NULL,7,1),(3032,626.0000,NULL,7,1),(3033,627.0000,NULL,7,1),(3034,628.0000,NULL,7,1),(3035,629.0000,NULL,7,1),(3036,630.0000,NULL,7,1),(3037,631.0000,NULL,7,1),(3038,632.0000,NULL,7,1),(3039,633.0000,NULL,7,1),(3040,634.0000,NULL,7,1),(3041,635.0000,NULL,7,1),(3042,636.0000,NULL,7,1),(3043,637.0000,NULL,7,1),(3044,638.0000,NULL,7,1),(3045,639.0000,NULL,7,1),(3046,640.0000,NULL,7,1),(3047,641.0000,NULL,7,1),(3048,642.0000,NULL,7,1),(3049,643.0000,NULL,7,1),(3050,644.0000,NULL,7,1),(3051,645.0000,NULL,7,1),(3052,646.0000,NULL,7,1),(3053,647.0000,NULL,7,1),(3054,648.0000,NULL,7,1),(3055,649.0000,NULL,7,1),(3056,650.0000,NULL,7,1),(3057,651.0000,NULL,7,1),(3058,652.0000,NULL,7,1),(3059,653.0000,NULL,7,1),(3060,654.0000,NULL,7,1),(3061,655.0000,NULL,7,1),(3062,656.0000,NULL,7,1),(3063,657.0000,NULL,7,1),(3064,658.0000,NULL,7,1),(3065,659.0000,NULL,7,1),(3066,660.0000,NULL,7,1),(3067,661.0000,NULL,7,1),(3068,662.0000,NULL,7,1),(3069,663.0000,NULL,7,1),(3070,664.0000,NULL,7,1),(3071,665.0000,NULL,7,1),(3072,666.0000,NULL,7,1),(3073,667.0000,NULL,7,1),(3074,668.0000,NULL,7,1),(3075,669.0000,NULL,7,1),(3076,670.0000,NULL,7,1),(3077,671.0000,NULL,7,1),(3078,672.0000,NULL,7,1),(3079,673.0000,NULL,7,1),(3080,674.0000,NULL,7,1),(3081,675.0000,NULL,7,1),(3082,676.0000,NULL,7,1),(3083,677.0000,NULL,7,1),(3084,678.0000,NULL,7,1),(3085,679.0000,NULL,7,1),(3086,680.0000,NULL,7,1),(3087,681.0000,NULL,7,1),(3088,682.0000,NULL,7,1),(3089,683.0000,NULL,7,1),(3090,684.0000,NULL,7,1),(3091,685.0000,NULL,7,1),(3092,686.0000,NULL,7,1),(3093,687.0000,NULL,7,1),(3094,688.0000,NULL,7,1),(3095,689.0000,NULL,7,1),(3096,690.0000,NULL,7,1),(3097,691.0000,NULL,7,1),(3098,692.0000,NULL,7,1),(3099,693.0000,NULL,7,1),(3100,694.0000,NULL,7,1),(3101,695.0000,NULL,7,1),(3102,696.0000,NULL,7,1),(3103,697.0000,NULL,7,1),(3104,698.0000,NULL,7,1),(3105,699.0000,NULL,7,1),(3106,700.0000,NULL,7,1),(3107,701.0000,NULL,7,1),(3108,702.0000,NULL,7,1),(3109,703.0000,NULL,7,1),(3110,704.0000,NULL,7,1),(3111,705.0000,NULL,7,1),(3112,706.0000,NULL,7,1),(3113,707.0000,NULL,7,1),(3114,708.0000,NULL,7,1),(3115,709.0000,NULL,7,1),(3116,710.0000,NULL,7,1),(3117,711.0000,NULL,7,1),(3118,712.0000,NULL,7,1),(3119,713.0000,NULL,7,1),(3120,714.0000,NULL,7,1),(3121,715.0000,NULL,7,1),(3122,716.0000,NULL,7,1),(3123,717.0000,NULL,7,1),(3124,718.0000,NULL,7,1),(3125,719.0000,NULL,7,1),(3126,720.0000,NULL,7,1),(3127,721.0000,NULL,7,1),(3128,722.0000,NULL,7,1),(3129,723.0000,NULL,7,1),(3130,724.0000,NULL,7,1),(3131,725.0000,NULL,7,1),(3132,726.0000,NULL,7,1),(3133,727.0000,NULL,7,1),(3134,728.0000,NULL,7,1),(3135,729.0000,NULL,7,1),(3136,730.0000,NULL,7,1),(3137,731.0000,NULL,7,1),(3138,732.0000,NULL,7,1),(3139,733.0000,NULL,7,1),(3140,734.0000,NULL,7,1),(3141,735.0000,NULL,7,1),(3142,736.0000,NULL,7,1),(3143,737.0000,NULL,7,1),(3144,738.0000,NULL,7,1),(3145,739.0000,NULL,7,1),(3146,740.0000,NULL,7,1),(3147,741.0000,NULL,7,1),(3148,742.0000,NULL,7,1),(3149,743.0000,NULL,7,1),(3150,744.0000,NULL,7,1),(3151,745.0000,NULL,7,1),(3152,746.0000,NULL,7,1),(3153,747.0000,NULL,7,1),(3154,748.0000,NULL,7,1),(3155,749.0000,NULL,7,1),(3156,750.0000,NULL,7,1),(3157,751.0000,NULL,7,1),(3158,752.0000,NULL,7,1),(3159,753.0000,NULL,7,1),(3160,754.0000,NULL,7,1),(3161,755.0000,NULL,7,1),(3162,756.0000,NULL,7,1),(3163,757.0000,NULL,7,1),(3164,758.0000,NULL,7,1),(3165,759.0000,NULL,7,1),(3166,760.0000,NULL,7,1),(3167,761.0000,NULL,7,1),(3168,762.0000,NULL,7,1),(3169,763.0000,NULL,7,1),(3170,764.0000,NULL,7,1),(3171,765.0000,NULL,7,1),(3172,766.0000,NULL,7,1),(3173,767.0000,NULL,7,1),(3174,768.0000,NULL,7,1),(3175,769.0000,NULL,7,1),(3176,770.0000,NULL,7,1),(3177,771.0000,NULL,7,1),(3178,772.0000,NULL,7,1),(3179,773.0000,NULL,7,1),(3180,774.0000,NULL,7,1),(3181,775.0000,NULL,7,1),(3182,776.0000,NULL,7,1),(3183,777.0000,NULL,7,1),(3184,778.0000,NULL,7,1),(3185,779.0000,NULL,7,1),(3186,780.0000,NULL,7,1),(3187,781.0000,NULL,7,1),(3188,782.0000,NULL,7,1),(3189,783.0000,NULL,7,1),(3190,784.0000,NULL,7,1),(3191,785.0000,NULL,7,1),(3192,786.0000,NULL,7,1),(3193,787.0000,NULL,7,1),(3194,788.0000,NULL,7,1),(3195,789.0000,NULL,7,1),(3196,790.0000,NULL,7,1),(3197,791.0000,NULL,7,1),(3198,792.0000,NULL,7,1),(3199,793.0000,NULL,7,1),(3200,794.0000,NULL,7,1),(3201,795.0000,NULL,7,1),(3202,796.0000,NULL,7,1),(3203,797.0000,NULL,7,1),(3204,798.0000,NULL,7,1),(3205,799.0000,NULL,7,1),(3206,800.0000,NULL,7,1),(3207,804.0000,NULL,7,1),(3208,808.0000,NULL,7,1),(3209,812.0000,NULL,7,1),(3210,816.0000,NULL,7,1),(3211,820.0000,NULL,7,1),(3212,824.0000,NULL,7,1),(3213,828.0000,NULL,7,1),(3214,832.0000,NULL,7,1),(3215,836.0000,NULL,7,1),(3216,840.0000,NULL,7,1),(3217,844.0000,NULL,7,1),(3218,848.0000,NULL,7,1),(3219,852.0000,NULL,7,1),(3220,856.0000,NULL,7,1),(3221,860.0000,NULL,7,1),(3222,864.0000,NULL,7,1),(3223,868.0000,NULL,7,1),(3224,872.0000,NULL,7,1),(3225,876.0000,NULL,7,1),(3226,880.0000,NULL,7,1),(3227,884.0000,NULL,7,1),(3228,888.0000,NULL,7,1),(3229,892.0000,NULL,7,1),(3230,896.0000,NULL,7,1),(3231,900.0000,NULL,7,1),(3232,904.0000,NULL,7,1),(3233,908.0000,NULL,7,1),(3234,912.0000,NULL,7,1),(3235,916.0000,NULL,7,1),(3236,920.0000,NULL,7,1),(3237,924.0000,NULL,7,1),(3238,928.0000,NULL,7,1),(3239,932.0000,NULL,7,1),(3240,936.0000,NULL,7,1),(3241,940.0000,NULL,7,1),(3242,944.0000,NULL,7,1),(3243,948.0000,NULL,7,1),(3244,952.0000,NULL,7,1),(3245,956.0000,NULL,7,1),(3246,960.0000,NULL,7,1),(3247,964.0000,NULL,7,1),(3248,968.0000,NULL,7,1),(3249,972.0000,NULL,7,1),(3250,976.0000,NULL,7,1),(3251,980.0000,NULL,7,1),(3252,984.0000,NULL,7,1),(3253,988.0000,NULL,7,1),(3254,992.0000,NULL,7,1),(3255,996.0000,NULL,7,1),(3256,1000.0000,NULL,7,1),(3257,1004.0000,NULL,7,1),(3258,1008.0000,NULL,7,1),(3259,1012.0000,NULL,7,1),(3260,1016.0000,NULL,7,1),(3261,1020.0000,NULL,7,1),(3262,1024.0000,NULL,7,1),(3263,1028.0000,NULL,7,1),(3264,1032.0000,NULL,7,1),(3265,1036.0000,NULL,7,1),(3266,1040.0000,NULL,7,1),(3267,1044.0000,NULL,7,1),(3268,1048.0000,NULL,7,1),(3269,1052.0000,NULL,7,1),(3270,1056.0000,NULL,7,1),(3271,1060.0000,NULL,7,1),(3272,1064.0000,NULL,7,1),(3273,1068.0000,NULL,7,1),(3274,1072.0000,NULL,7,1),(3275,1076.0000,NULL,7,1),(3276,1080.0000,NULL,7,1),(3277,1084.0000,NULL,7,1),(3278,1088.0000,NULL,7,1),(3279,1092.0000,NULL,7,1),(3280,1096.0000,NULL,7,1),(3281,1100.0000,NULL,7,1),(3282,1104.0000,NULL,7,1),(3283,1108.0000,NULL,7,1),(3284,1112.0000,NULL,7,1),(3285,1116.0000,NULL,7,1),(3286,1120.0000,NULL,7,1),(3287,1124.0000,NULL,7,1),(3288,1128.0000,NULL,7,1),(3289,1132.0000,NULL,7,1),(3290,1136.0000,NULL,7,1),(3291,1140.0000,NULL,7,1),(3292,1144.0000,NULL,7,1),(3293,1148.0000,NULL,7,1),(3294,1152.0000,NULL,7,1),(3295,1156.0000,NULL,7,1),(3296,1160.0000,NULL,7,1),(3297,1164.0000,NULL,7,1),(3298,1168.0000,NULL,7,1),(3299,1172.0000,NULL,7,1),(3300,1176.0000,NULL,7,1),(3301,1180.0000,NULL,7,1),(3302,1184.0000,NULL,7,1),(3303,1188.0000,NULL,7,1),(3304,1192.0000,NULL,7,1),(3305,1196.0000,NULL,7,1),(3306,1200.0000,NULL,7,1),(3307,1204.0000,NULL,7,1),(3308,1208.0000,NULL,7,1),(3309,1212.0000,NULL,7,1),(3310,1216.0000,NULL,7,1),(3311,1220.0000,NULL,7,1),(3312,1224.0000,NULL,7,1),(3313,1228.0000,NULL,7,1),(3314,1232.0000,NULL,7,1),(3315,1236.0000,NULL,7,1),(3316,1240.0000,NULL,7,1),(3317,1244.0000,NULL,7,1),(3318,1248.0000,NULL,7,1),(3319,1252.0000,NULL,7,1),(3320,1256.0000,NULL,7,1),(3321,1260.0000,NULL,7,1),(3322,1264.0000,NULL,7,1),(3323,1268.0000,NULL,7,1),(3324,1272.0000,NULL,7,1),(3325,1276.0000,NULL,7,1),(3326,1280.0000,NULL,7,1),(3327,1284.0000,NULL,7,1),(3328,1288.0000,NULL,7,1),(3329,1292.0000,NULL,7,1),(3330,1296.0000,NULL,7,1),(3331,1300.0000,NULL,7,1),(3332,1304.0000,NULL,7,1),(3333,1308.0000,NULL,7,1),(3334,1312.0000,NULL,7,1),(3335,1316.0000,NULL,7,1),(3336,1320.0000,NULL,7,1),(3337,1324.0000,NULL,7,1),(3338,1328.0000,NULL,7,1),(3339,1332.0000,NULL,7,1),(3340,1336.0000,NULL,7,1),(3341,1340.0000,NULL,7,1),(3342,1344.0000,NULL,7,1),(3343,1348.0000,NULL,7,1),(3344,1352.0000,NULL,7,1),(3345,1356.0000,NULL,7,1),(3346,1360.0000,NULL,7,1),(3347,1364.0000,NULL,7,1),(3348,1368.0000,NULL,7,1),(3349,1372.0000,NULL,7,1),(3350,1376.0000,NULL,7,1),(3351,1380.0000,NULL,7,1),(3352,1384.0000,NULL,7,1),(3353,1388.0000,NULL,7,1),(3354,1392.0000,NULL,7,1),(3355,1396.0000,NULL,7,1),(3356,1400.0000,NULL,7,1),(3357,1404.0000,NULL,7,1),(3358,1408.0000,NULL,7,1),(3359,1412.0000,NULL,7,1),(3360,1416.0000,NULL,7,1),(3361,1420.0000,NULL,7,1),(3362,1424.0000,NULL,7,1),(3363,1428.0000,NULL,7,1),(3364,1432.0000,NULL,7,1),(3365,1436.0000,NULL,7,1),(3366,1440.0000,NULL,7,1),(3367,1444.0000,NULL,7,1),(3368,1448.0000,NULL,7,1),(3369,1452.0000,NULL,7,1),(3370,1456.0000,NULL,7,1),(3371,1460.0000,NULL,7,1),(3372,1464.0000,NULL,7,1),(3373,1468.0000,NULL,7,1),(3374,1472.0000,NULL,7,1),(3375,1476.0000,NULL,7,1),(3376,1480.0000,NULL,7,1),(3377,1484.0000,NULL,7,1),(3378,1488.0000,NULL,7,1),(3379,1492.0000,NULL,7,1),(3380,1496.0000,NULL,7,1),(3381,1500.0000,NULL,7,1),(3382,1504.0000,NULL,7,1),(3383,1508.0000,NULL,7,1),(3384,1512.0000,NULL,7,1),(3385,1516.0000,NULL,7,1),(3386,1520.0000,NULL,7,1),(3387,1524.0000,NULL,7,1),(3388,1528.0000,NULL,7,1),(3389,1532.0000,NULL,7,1),(3390,1536.0000,NULL,7,1),(3391,1540.0000,NULL,7,1),(3392,1544.0000,NULL,7,1),(3393,1548.0000,NULL,7,1),(3394,1552.0000,NULL,7,1),(3395,1556.0000,NULL,7,1),(3396,1560.0000,NULL,7,1),(3397,1564.0000,NULL,7,1),(3398,1568.0000,NULL,7,1),(3399,1572.0000,NULL,7,1),(3400,1576.0000,NULL,7,1),(3401,1580.0000,NULL,7,1),(3402,1584.0000,NULL,7,1),(3403,1588.0000,NULL,7,1),(3404,1592.0000,NULL,7,1),(3405,1596.0000,NULL,7,1),(3406,1600.0000,NULL,7,1),(3407,1604.0000,NULL,7,1),(3408,1608.0000,NULL,7,1),(3409,1612.0000,NULL,7,1),(3410,1616.0000,NULL,7,1),(3411,1620.0000,NULL,7,1),(3412,1624.0000,NULL,7,1),(3413,1628.0000,NULL,7,1),(3414,1632.0000,NULL,7,1),(3415,1636.0000,NULL,7,1),(3416,1640.0000,NULL,7,1),(3417,1644.0000,NULL,7,1),(3418,1648.0000,NULL,7,1),(3419,1652.0000,NULL,7,1),(3420,1656.0000,NULL,7,1),(3421,1660.0000,NULL,7,1),(3422,1664.0000,NULL,7,1),(3423,1668.0000,NULL,7,1),(3424,1672.0000,NULL,7,1),(3425,1676.0000,NULL,7,1),(3426,1680.0000,NULL,7,1),(3427,1684.0000,NULL,7,1),(3428,1688.0000,NULL,7,1),(3429,1692.0000,NULL,7,1),(3430,1696.0000,NULL,7,1),(3431,1700.0000,NULL,7,1),(3432,1704.0000,NULL,7,1),(3433,1708.0000,NULL,7,1),(3434,1712.0000,NULL,7,1),(3435,1716.0000,NULL,7,1),(3436,1720.0000,NULL,7,1),(3437,1724.0000,NULL,7,1),(3438,1728.0000,NULL,7,1),(3439,1732.0000,NULL,7,1),(3440,1736.0000,NULL,7,1),(3441,1740.0000,NULL,7,1),(3442,1744.0000,NULL,7,1),(3443,1748.0000,NULL,7,1),(3444,1752.0000,NULL,7,1),(3445,1756.0000,NULL,7,1),(3446,1760.0000,NULL,7,1),(3447,1764.0000,NULL,7,1),(3448,1768.0000,NULL,7,1),(3449,1772.0000,NULL,7,1),(3450,1776.0000,NULL,7,1),(3451,1780.0000,NULL,7,1),(3452,1784.0000,NULL,7,1),(3453,1788.0000,NULL,7,1),(3454,1792.0000,NULL,7,1),(3455,1796.0000,NULL,7,1),(3456,1800.0000,NULL,7,1),(3457,1804.0000,NULL,7,1),(3458,1808.0000,NULL,7,1),(3459,1812.0000,NULL,7,1),(3460,1816.0000,NULL,7,1),(3461,1820.0000,NULL,7,1),(3462,1824.0000,NULL,7,1),(3463,1828.0000,NULL,7,1),(3464,1832.0000,NULL,7,1),(3465,1836.0000,NULL,7,1),(3466,1840.0000,NULL,7,1),(3467,1844.0000,NULL,7,1),(3468,1848.0000,NULL,7,1),(3469,1852.0000,NULL,7,1),(3470,1856.0000,NULL,7,1),(3471,1860.0000,NULL,7,1),(3472,1864.0000,NULL,7,1),(3473,1868.0000,NULL,7,1),(3474,1872.0000,NULL,7,1),(3475,1876.0000,NULL,7,1),(3476,1880.0000,NULL,7,1),(3477,1884.0000,NULL,7,1),(3478,1888.0000,NULL,7,1),(3479,1892.0000,NULL,7,1),(3480,1896.0000,NULL,7,1),(3481,1900.0000,NULL,7,1),(3482,1904.0000,NULL,7,1),(3483,1908.0000,NULL,7,1),(3484,1912.0000,NULL,7,1),(3485,1916.0000,NULL,7,1),(3486,1920.0000,NULL,7,1),(3487,1924.0000,NULL,7,1),(3488,1928.0000,NULL,7,1),(3489,1932.0000,NULL,7,1),(3490,1936.0000,NULL,7,1),(3491,1940.0000,NULL,7,1),(3492,1944.0000,NULL,7,1),(3493,1948.0000,NULL,7,1),(3494,1952.0000,NULL,7,1),(3495,1956.0000,NULL,7,1),(3496,1960.0000,NULL,7,1),(3497,1964.0000,NULL,7,1),(3498,1968.0000,NULL,7,1),(3499,1972.0000,NULL,7,1),(3500,1976.0000,NULL,7,1),(3501,1980.0000,NULL,7,1),(3502,1984.0000,NULL,7,1),(3503,1988.0000,NULL,7,1),(3504,1992.0000,NULL,7,1),(3505,1996.0000,NULL,7,1),(3506,2000.0000,NULL,7,1),(3507,2004.0000,NULL,7,1),(3508,2008.0000,NULL,7,1),(3509,2012.0000,NULL,7,1),(3510,2016.0000,NULL,7,1),(3511,2020.0000,NULL,7,1),(3512,2024.0000,NULL,7,1),(3513,2028.0000,NULL,7,1),(3514,2032.0000,NULL,7,1),(3515,2036.0000,NULL,7,1),(3516,2040.0000,NULL,7,1),(3517,2044.0000,NULL,7,1),(3518,2048.0000,NULL,7,1),(3519,2052.0000,NULL,7,1),(3520,2056.0000,NULL,7,1),(3521,2060.0000,NULL,7,1),(3522,2064.0000,NULL,7,1),(3523,2068.0000,NULL,7,1),(3524,2072.0000,NULL,7,1),(3525,2076.0000,NULL,7,1),(3526,2080.0000,NULL,7,1),(3527,2084.0000,NULL,7,1),(3528,2088.0000,NULL,7,1),(3529,2092.0000,NULL,7,1),(3530,2096.0000,NULL,7,1),(3531,2100.0000,NULL,7,1),(3532,2104.0000,NULL,7,1),(3533,2108.0000,NULL,7,1),(3534,2112.0000,NULL,7,1),(3535,2116.0000,NULL,7,1),(3536,2120.0000,NULL,7,1),(3537,2124.0000,NULL,7,1),(3538,2128.0000,NULL,7,1),(3539,2132.0000,NULL,7,1),(3540,2136.0000,NULL,7,1),(3541,2140.0000,NULL,7,1),(3542,2144.0000,NULL,7,1),(3543,2148.0000,NULL,7,1),(3544,2152.0000,NULL,7,1),(3545,2156.0000,NULL,7,1),(3546,2160.0000,NULL,7,1),(3547,2164.0000,NULL,7,1),(3548,2168.0000,NULL,7,1),(3549,2172.0000,NULL,7,1),(3550,2176.0000,NULL,7,1),(3551,2180.0000,NULL,7,1),(3552,2184.0000,NULL,7,1),(3553,2188.0000,NULL,7,1),(3554,2192.0000,NULL,7,1),(3555,2196.0000,NULL,7,1),(3556,2200.0000,NULL,7,1),(3557,2204.0000,NULL,7,1),(3558,2208.0000,NULL,7,1),(3559,2212.0000,NULL,7,1),(3560,2216.0000,NULL,7,1),(3561,2220.0000,NULL,7,1),(3562,2224.0000,NULL,7,1),(3563,2228.0000,NULL,7,1),(3564,2232.0000,NULL,7,1),(3565,2236.0000,NULL,7,1),(3566,2240.0000,NULL,7,1),(3567,2244.0000,NULL,7,1),(3568,2248.0000,NULL,7,1),(3569,2252.0000,NULL,7,1),(3570,2256.0000,NULL,7,1),(3571,2260.0000,NULL,7,1),(3572,2264.0000,NULL,7,1),(3573,2268.0000,NULL,7,1),(3574,2272.0000,NULL,7,1),(3575,2276.0000,NULL,7,1),(3576,2280.0000,NULL,7,1),(3577,2284.0000,NULL,7,1),(3578,2288.0000,NULL,7,1),(3579,2292.0000,NULL,7,1),(3580,2296.0000,NULL,7,1),(3581,2300.0000,NULL,7,1),(3582,2304.0000,NULL,7,1),(3583,2308.0000,NULL,7,1),(3584,2312.0000,NULL,7,1),(3585,2316.0000,NULL,7,1),(3586,2320.0000,NULL,7,1),(3587,2324.0000,NULL,7,1),(3588,2328.0000,NULL,7,1),(3589,2332.0000,NULL,7,1),(3590,2336.0000,NULL,7,1),(3591,2340.0000,NULL,7,1),(3592,2344.0000,NULL,7,1),(3593,2348.0000,NULL,7,1),(3594,2352.0000,NULL,7,1),(3595,2356.0000,NULL,7,1),(3596,2360.0000,NULL,7,1),(3597,2364.0000,NULL,7,1),(3598,2368.0000,NULL,7,1),(3599,2372.0000,NULL,7,1),(3600,2376.0000,NULL,7,1),(3601,2380.0000,NULL,7,1),(3602,2384.0000,NULL,7,1),(3603,2388.0000,NULL,7,1),(3604,2392.0000,NULL,7,1),(3605,2396.0000,NULL,7,1),(3606,2400.0000,NULL,7,1),(3607,2404.0000,NULL,7,1),(3608,2408.0000,NULL,7,1),(3609,2412.0000,NULL,7,1),(3610,2416.0000,NULL,7,1),(3611,2420.0000,NULL,7,1),(3612,2424.0000,NULL,7,1),(3613,2428.0000,NULL,7,1),(3614,2432.0000,NULL,7,1),(3615,2436.0000,NULL,7,1),(3616,2440.0000,NULL,7,1),(3617,2444.0000,NULL,7,1),(3618,2448.0000,NULL,7,1),(3619,2452.0000,NULL,7,1),(3620,2456.0000,NULL,7,1),(3621,2460.0000,NULL,7,1),(3622,2464.0000,NULL,7,1),(3623,2468.0000,NULL,7,1),(3624,2472.0000,NULL,7,1),(3625,2476.0000,NULL,7,1),(3626,2480.0000,NULL,7,1),(3627,2484.0000,NULL,7,1),(3628,2488.0000,NULL,7,1),(3629,2492.0000,NULL,7,1),(3630,2496.0000,NULL,7,1),(3631,2500.0000,NULL,7,1),(4458,395.1000,NULL,9,1),(4459,397.1000,NULL,9,1),(4460,399.1000,NULL,9,1),(4461,401.1000,NULL,9,1),(4462,403.1000,NULL,9,1),(4463,405.1000,NULL,9,1),(4464,407.1000,NULL,9,1),(4465,409.1000,NULL,9,1),(4466,411.1000,NULL,9,1),(4467,412.8000,NULL,9,1),(4468,415.8000,NULL,9,1),(4469,418.8000,NULL,9,1),(4470,421.8000,NULL,9,1),(4471,424.8000,NULL,9,1),(4472,427.8000,NULL,9,1),(4473,430.8000,NULL,9,1),(4474,433.8000,NULL,9,1),(4475,436.8000,NULL,9,1),(4476,439.8000,NULL,9,1),(4477,442.8000,NULL,9,1),(4478,445.8000,NULL,9,1),(4479,448.8000,NULL,9,1),(4480,451.3000,NULL,9,1),(4481,453.3000,NULL,9,1),(4482,455.3000,NULL,9,1),(4483,457.3000,NULL,9,1),(4484,459.3000,NULL,9,1),(4485,461.3000,NULL,9,1),(4486,463.3000,NULL,9,1),(4487,465.3000,NULL,9,1),(4488,467.3000,NULL,9,1),(4489,469.3000,NULL,9,1),(4490,471.3000,NULL,9,1),(4491,473.3000,NULL,9,1),(4492,475.3000,NULL,9,1),(4493,477.3000,NULL,9,1),(4494,479.3000,NULL,9,1),(4495,481.3000,NULL,9,1),(4496,483.3000,NULL,9,1),(4497,485.3000,NULL,9,1),(4498,487.3000,NULL,9,1),(4499,489.3000,NULL,9,1),(4500,491.3000,NULL,9,1),(4501,493.3000,NULL,9,1),(4502,495.3000,NULL,9,1),(4503,497.3000,NULL,9,1),(4504,499.3000,NULL,9,1),(4505,501.3000,NULL,9,1),(4506,503.3000,NULL,9,1),(4507,505.3000,NULL,9,1),(4508,507.3000,NULL,9,1),(4509,509.3000,NULL,9,1),(4510,511.3000,NULL,9,1),(4511,513.3000,NULL,9,1),(4512,515.3000,NULL,9,1),(4513,517.3000,NULL,9,1),(4514,519.3000,NULL,9,1),(4515,521.3000,NULL,9,1),(4516,523.3000,NULL,9,1),(4517,525.3000,NULL,9,1),(4518,527.3000,NULL,9,1),(4519,529.3000,NULL,9,1),(4520,531.3000,NULL,9,1),(4521,533.3000,NULL,9,1),(4522,535.3000,NULL,9,1),(4523,537.3000,NULL,9,1),(4524,539.3000,NULL,9,1),(4525,541.3000,NULL,9,1),(4526,543.3000,NULL,9,1),(4527,545.3000,NULL,9,1),(4528,547.3000,NULL,9,1),(4529,549.3000,NULL,9,1),(4530,551.3000,NULL,9,1),(4531,553.3000,NULL,9,1),(4532,555.3000,NULL,9,1),(4533,557.3000,NULL,9,1),(4534,559.3000,NULL,9,1),(4535,561.3000,NULL,9,1),(4536,563.3000,NULL,9,1),(4537,565.3000,NULL,9,1),(4538,567.3000,NULL,9,1),(4539,569.3000,NULL,9,1),(4540,571.3000,NULL,9,1),(4541,573.3000,NULL,9,1),(4542,575.3000,NULL,9,1),(4543,577.3000,NULL,9,1),(4544,579.3000,NULL,9,1),(4545,581.3000,NULL,9,1),(4546,583.3000,NULL,9,1),(4547,585.3000,NULL,9,1),(4548,587.3000,NULL,9,1),(4549,589.3000,NULL,9,1),(4550,591.3000,NULL,9,1),(4551,593.3000,NULL,9,1),(4552,595.3000,NULL,9,1),(4553,597.3000,NULL,9,1),(4554,599.3000,NULL,9,1),(4555,601.1000,NULL,9,1),(4556,602.7000,NULL,9,1),(4557,604.3000,NULL,9,1),(4558,605.9000,NULL,9,1),(4559,607.5000,NULL,9,1),(4560,609.1000,NULL,9,1),(4561,610.7000,NULL,9,1),(4562,612.3000,NULL,9,1),(4563,613.9000,NULL,9,1),(4564,615.5000,NULL,9,1),(4565,617.1000,NULL,9,1),(4566,618.7000,NULL,9,1),(4567,620.3000,NULL,9,1),(4568,621.9000,NULL,9,1),(4569,623.5000,NULL,9,1),(4570,625.1000,NULL,9,1),(4571,626.7000,NULL,9,1),(4572,628.3000,NULL,9,1),(4573,629.9000,NULL,9,1),(4574,631.7000,NULL,9,1),(4575,633.7000,NULL,9,1),(4576,635.7000,NULL,9,1),(4577,637.7000,NULL,9,1),(4578,639.7000,NULL,9,1),(4579,641.7000,NULL,9,1),(4580,643.7000,NULL,9,1),(4581,645.7000,NULL,9,1),(4582,647.7000,NULL,9,1),(4583,649.7000,NULL,9,1),(4584,651.7000,NULL,9,1),(4585,653.7000,NULL,9,1),(4586,655.7000,NULL,9,1),(4587,657.7000,NULL,9,1),(4588,659.7000,NULL,9,1),(4589,661.7000,NULL,9,1),(4590,663.7000,NULL,9,1),(4591,665.7000,NULL,9,1),(4592,667.7000,NULL,9,1),(4593,670.2000,NULL,9,1),(4594,673.2000,NULL,9,1),(4595,676.2000,NULL,9,1),(4596,679.2000,NULL,9,1),(4597,682.2000,NULL,9,1),(4598,685.2000,NULL,9,1),(4599,688.2000,NULL,9,1),(4600,691.2000,NULL,9,1),(4601,694.2000,NULL,9,1),(4602,697.2000,NULL,9,1),(4603,700.2000,NULL,9,1),(4604,702.0000,NULL,9,1),(4605,704.0000,NULL,9,1),(4606,706.0000,NULL,9,1),(4607,708.0000,NULL,9,1),(4608,710.0000,NULL,9,1),(4609,712.0000,NULL,9,1),(4610,714.0000,NULL,9,1),(4611,716.0000,NULL,9,1),(4612,718.0000,NULL,9,1),(4613,720.0000,NULL,9,1),(4614,722.0000,NULL,9,1),(4615,724.0000,NULL,9,1),(4616,726.0000,NULL,9,1),(4617,728.0000,NULL,9,1),(4618,730.0000,NULL,9,1),(4619,732.0000,NULL,9,1),(4620,734.0000,NULL,9,1),(4621,736.0000,NULL,9,1),(4622,738.0000,NULL,9,1),(4623,740.0000,NULL,9,1),(4624,742.0000,NULL,9,1),(4625,744.0000,NULL,9,1),(4626,746.0000,NULL,9,1),(4627,748.0000,NULL,9,1),(4628,750.5000,NULL,9,1),(4629,753.5000,NULL,9,1),(4630,756.5000,NULL,9,1),(4631,759.5000,NULL,9,1),(4632,762.5000,NULL,9,1),(4633,765.5000,NULL,9,1),(4634,768.5000,NULL,9,1),(4635,771.5000,NULL,9,1),(4636,774.5000,NULL,9,1),(4637,777.5000,NULL,9,1),(4638,781.0000,NULL,9,1),(4639,785.0000,NULL,9,1),(4640,789.0000,NULL,9,1),(4641,793.0000,NULL,9,1),(4642,797.0000,NULL,9,1),(4643,801.5000,NULL,9,1),(4644,806.5000,NULL,9,1),(4645,811.5000,NULL,9,1),(4646,816.5000,NULL,9,1),(4647,822.0000,NULL,9,1),(4648,828.0000,NULL,9,1),(4649,835.0000,NULL,9,1),(4650,843.0000,NULL,9,1),(4651,851.0000,NULL,9,1),(4652,859.0000,NULL,9,1),(4653,871.0000,NULL,9,1),(4654,883.0000,NULL,9,1),(4655,894.0000,NULL,9,1),(4656,904.0000,NULL,9,1),(4657,914.0000,NULL,9,1),(4658,924.0000,NULL,9,1),(4659,933.0000,NULL,9,1),(4660,941.0000,NULL,9,1),(4661,949.0000,NULL,9,1),(4662,957.0000,NULL,9,1),(4663,964.0000,NULL,9,1),(4664,970.0000,NULL,9,1),(4665,976.0000,NULL,9,1),(4666,982.0000,NULL,9,1),(4667,988.0000,NULL,9,1),(4668,994.0000,NULL,9,1),(4669,1000.0000,NULL,9,1),(4670,1006.0000,NULL,9,1),(4671,1012.0000,NULL,9,1),(4672,1018.0000,NULL,9,1),(4673,1023.5000,NULL,9,1),(4674,1028.5000,NULL,9,1),(4675,1033.5000,NULL,9,1),(4676,1038.5000,NULL,9,1),(4677,1043.5000,NULL,9,1),(4678,1048.5000,NULL,9,1),(4679,1053.5000,NULL,9,1),(4680,1058.5000,NULL,9,1),(4681,1063.5000,NULL,9,1),(4682,1068.5000,NULL,9,1),(4683,1073.5000,NULL,9,1),(4684,1078.5000,NULL,9,1),(4685,1083.5000,NULL,9,1),(4686,1088.5000,NULL,9,1),(4687,1093.5000,NULL,9,1),(4688,1098.5000,NULL,9,1),(4689,1103.5000,NULL,9,1),(4690,1108.5000,NULL,9,1),(4691,1113.5000,NULL,9,1),(4692,1118.5000,NULL,9,1),(4693,1123.5000,NULL,9,1),(4694,1128.5000,NULL,9,1),(4695,1133.5000,NULL,9,1),(4696,1138.5000,NULL,9,1),(4697,1143.5000,NULL,9,1),(4698,1148.5000,NULL,9,1),(4699,1153.5000,NULL,9,1),(4700,1158.5000,NULL,9,1),(4701,1163.5000,NULL,9,1),(4702,1168.5000,NULL,9,1),(4703,1173.5000,NULL,9,1),(4704,1178.5000,NULL,9,1),(4705,1183.5000,NULL,9,1),(4706,1188.5000,NULL,9,1),(4707,1193.5000,NULL,9,1),(4708,1198.5000,NULL,9,1),(4709,1203.5000,NULL,9,1),(4710,1208.5000,NULL,9,1),(4711,1213.5000,NULL,9,1),(4712,1218.5000,NULL,9,1),(4713,1223.5000,NULL,9,1),(4714,1228.5000,NULL,9,1),(4715,1233.5000,NULL,9,1),(4716,1238.5000,NULL,9,1),(4717,1243.5000,NULL,9,1),(4718,1248.5000,NULL,9,1),(4719,1253.5000,NULL,9,1),(4720,1258.5000,NULL,9,1),(4721,1263.5000,NULL,9,1),(4722,1268.5000,NULL,9,1),(4723,1273.5000,NULL,9,1),(4724,1278.5000,NULL,9,1),(4725,1283.5000,NULL,9,1),(4726,1288.5000,NULL,9,1),(4727,1293.5000,NULL,9,1),(4728,1298.5000,NULL,9,1),(4729,1303.5000,NULL,9,1),(4730,1308.5000,NULL,9,1),(4731,1313.5000,NULL,9,1),(4732,1318.5000,NULL,9,1),(4733,1323.5000,NULL,9,1),(4734,1328.5000,NULL,9,1),(4735,1333.5000,NULL,9,1),(4736,1338.5000,NULL,9,1),(4737,1343.5000,NULL,9,1),(4738,1348.5000,NULL,9,1),(4739,1353.5000,NULL,9,1),(4740,1358.5000,NULL,9,1),(4741,1363.5000,NULL,9,1),(4742,1368.5000,NULL,9,1),(4743,1373.5000,NULL,9,1),(4744,1378.5000,NULL,9,1),(4745,1383.5000,NULL,9,1),(4746,1388.5000,NULL,9,1),(4747,1393.5000,NULL,9,1),(4748,1398.5000,NULL,9,1),(4749,1403.5000,NULL,9,1),(4750,1408.5000,NULL,9,1),(4751,1413.5000,NULL,9,1),(4752,1418.5000,NULL,9,1),(4753,1423.5000,NULL,9,1),(4754,1428.5000,NULL,9,1),(4755,1433.5000,NULL,9,1),(4756,1438.5000,NULL,9,1),(4757,1443.5000,NULL,9,1),(4758,1448.5000,NULL,9,1),(4759,1453.5000,NULL,9,1),(4760,1458.5000,NULL,9,1),(4761,1463.5000,NULL,9,1),(4762,1468.5000,NULL,9,1),(4763,1473.5000,NULL,9,1),(4764,1478.5000,NULL,9,1),(4765,1483.5000,NULL,9,1),(4766,1488.5000,NULL,9,1),(4767,1493.5000,NULL,9,1),(4768,1498.5000,NULL,9,1),(4769,1503.5000,NULL,9,1),(4770,1508.5000,NULL,9,1),(4771,1513.5000,NULL,9,1),(4772,1518.5000,NULL,9,1),(4773,1523.5000,NULL,9,1),(4774,1528.5000,NULL,9,1),(4775,1534.0000,NULL,9,1),(4776,1540.0000,NULL,9,1),(4777,1546.0000,NULL,9,1),(4778,1552.0000,NULL,9,1),(4779,1558.0000,NULL,9,1),(4780,1564.0000,NULL,9,1),(4781,1570.5000,NULL,9,1),(4782,1577.5000,NULL,9,1),(4783,1584.5000,NULL,9,1),(4784,1591.5000,NULL,9,1),(4785,1598.5000,NULL,9,1),(4786,1605.5000,NULL,9,1),(4787,1612.5000,NULL,9,1),(4788,1619.5000,NULL,9,1),(4789,1626.5000,NULL,9,1),(4790,1633.5000,NULL,9,1),(4791,1640.5000,NULL,9,1),(4792,1647.5000,NULL,9,1),(4793,1654.5000,NULL,9,1),(4794,1661.5000,NULL,9,1),(4795,1668.5000,NULL,9,1),(4796,1676.0000,NULL,9,1),(4797,1684.0000,NULL,9,1),(4798,1692.0000,NULL,9,1),(4799,1700.0000,NULL,9,1),(4800,1708.0000,NULL,9,1),(4801,1716.0000,NULL,9,1),(4802,1724.0000,NULL,9,1),(4803,1732.0000,NULL,9,1),(4804,1740.0000,NULL,9,1),(4805,1748.0000,NULL,9,1),(4806,1756.0000,NULL,9,1),(4807,1764.0000,NULL,9,1),(4808,1772.0000,NULL,9,1),(4809,1780.0000,NULL,9,1),(4810,1788.0000,NULL,9,1),(4811,1796.0000,NULL,9,1),(4812,1805.0000,NULL,9,1),(4813,1815.0000,NULL,9,1),(4814,1825.0000,NULL,9,1),(4815,1835.0000,NULL,9,1),(4816,1845.0000,NULL,9,1),(4817,1855.0000,NULL,9,1),(4818,1865.0000,NULL,9,1),(4819,1875.0000,NULL,9,1),(4820,1885.0000,NULL,9,1),(4821,1895.0000,NULL,9,1),(4822,1905.0000,NULL,9,1),(4823,1915.0000,NULL,9,1),(4824,1925.0000,NULL,9,1),(4825,1935.0000,NULL,9,1),(4826,1945.0000,NULL,9,1),(4827,1955.0000,NULL,9,1),(4828,1965.0000,NULL,9,1),(4829,1975.0000,NULL,9,1),(4830,1985.0000,NULL,9,1),(4831,1995.0000,NULL,9,1),(4832,2005.0000,NULL,9,1),(4833,2015.0000,NULL,9,1),(4834,2025.0000,NULL,9,1),(4835,2035.0000,NULL,9,1),(4836,2045.0000,NULL,9,1),(4837,2055.0000,NULL,9,1),(4838,2065.0000,NULL,9,1),(4839,2075.0000,NULL,9,1),(4840,2085.0000,NULL,9,1),(4841,2095.0000,NULL,9,1),(4842,2105.0000,NULL,9,1),(4843,2115.0000,NULL,9,1),(4844,2125.0000,NULL,9,1),(4845,2135.0000,NULL,9,1),(4846,2145.0000,NULL,9,1),(4847,2155.0000,NULL,9,1),(4848,2165.0000,NULL,9,1),(4849,2175.0000,NULL,9,1),(4850,2185.0000,NULL,9,1),(4851,2195.0000,NULL,9,1),(4852,2205.0000,NULL,9,1),(4853,2215.0000,NULL,9,1),(4854,2225.0000,NULL,9,1),(4855,2235.0000,NULL,9,1),(4856,2245.0000,NULL,9,1),(4857,2255.0000,NULL,9,1),(4858,2265.0000,NULL,9,1),(4859,2275.0000,NULL,9,1),(4860,2285.0000,NULL,9,1),(4861,2295.0000,NULL,9,1),(4862,2305.0000,NULL,9,1),(4863,2315.0000,NULL,9,1),(4864,2325.0000,NULL,9,1),(4865,2335.0000,NULL,9,1),(4866,2345.0000,NULL,9,1),(4867,2355.0000,NULL,9,1),(4868,2365.0000,NULL,9,1),(4869,2375.0000,NULL,9,1),(4870,2386.0000,NULL,9,1),(4871,2400.0000,NULL,9,1),(4872,2418.0000,NULL,9,1),(4873,2440.0000,NULL,9,1),(4874,2466.0000,NULL,9,1),(4875,2496.0000,NULL,9,1),(4876,2528.0000,NULL,9,1),(4877,2560.0000,NULL,9,1),(4878,344.2000,NULL,10,1),(4879,345.8000,NULL,10,1),(4880,347.3000,NULL,10,1),(4881,348.9000,NULL,10,1),(4882,350.4000,NULL,10,1),(4883,352.0000,NULL,10,1),(4884,353.5000,NULL,10,1),(4885,355.1000,NULL,10,1),(4886,356.6000,NULL,10,1),(4887,358.2000,NULL,10,1),(4888,359.7000,NULL,10,1),(4889,361.3000,NULL,10,1),(4890,362.8000,NULL,10,1),(4891,364.4000,NULL,10,1),(4892,365.9000,NULL,10,1),(4893,367.5000,NULL,10,1),(4894,369.0000,NULL,10,1),(4895,370.6000,NULL,10,1),(4896,372.1000,NULL,10,1),(4897,373.7000,NULL,10,1),(4898,375.2000,NULL,10,1),(4899,376.8000,NULL,10,1),(4900,378.3000,NULL,10,1),(4901,379.9000,NULL,10,1),(4902,381.4000,NULL,10,1),(4903,382.9000,NULL,10,1),(4904,384.5000,NULL,10,1),(4905,386.0000,NULL,10,1),(4906,387.6000,NULL,10,1),(4907,389.1000,NULL,10,1),(4908,390.7000,NULL,10,1),(4909,392.2000,NULL,10,1),(4910,393.8000,NULL,10,1),(4911,395.3000,NULL,10,1),(4912,396.8000,NULL,10,1),(4913,398.4000,NULL,10,1),(4914,399.9000,NULL,10,1),(4915,401.4000,NULL,10,1),(4916,403.0000,NULL,10,1),(4917,404.5000,NULL,10,1),(4918,406.1000,NULL,10,1),(4919,407.6000,NULL,10,1),(4920,409.1000,NULL,10,1),(4921,410.7000,NULL,10,1),(4922,412.2000,NULL,10,1),(4923,413.7000,NULL,10,1),(4924,415.3000,NULL,10,1),(4925,416.8000,NULL,10,1),(4926,418.3000,NULL,10,1),(4927,419.9000,NULL,10,1),(4928,421.4000,NULL,10,1),(4929,422.9000,NULL,10,1),(4930,424.4000,NULL,10,1),(4931,426.0000,NULL,10,1),(4932,427.5000,NULL,10,1),(4933,429.0000,NULL,10,1),(4934,430.6000,NULL,10,1),(4935,432.1000,NULL,10,1),(4936,433.6000,NULL,10,1),(4937,435.1000,NULL,10,1),(4938,436.6000,NULL,10,1),(4939,438.2000,NULL,10,1),(4940,439.7000,NULL,10,1),(4941,441.2000,NULL,10,1),(4942,442.7000,NULL,10,1),(4943,444.3000,NULL,10,1),(4944,445.8000,NULL,10,1),(4945,447.3000,NULL,10,1),(4946,448.8000,NULL,10,1),(4947,450.3000,NULL,10,1),(4948,451.8000,NULL,10,1),(4949,453.4000,NULL,10,1),(4950,454.9000,NULL,10,1),(4951,456.4000,NULL,10,1),(4952,457.9000,NULL,10,1),(4953,459.4000,NULL,10,1),(4954,460.9000,NULL,10,1),(4955,462.4000,NULL,10,1),(4956,463.9000,NULL,10,1),(4957,465.4000,NULL,10,1),(4958,466.9000,NULL,10,1),(4959,468.5000,NULL,10,1),(4960,470.0000,NULL,10,1),(4961,471.5000,NULL,10,1),(4962,473.0000,NULL,10,1),(4963,474.5000,NULL,10,1),(4964,476.0000,NULL,10,1),(4965,477.5000,NULL,10,1),(4966,479.0000,NULL,10,1),(4967,480.5000,NULL,10,1),(4968,482.0000,NULL,10,1),(4969,483.5000,NULL,10,1),(4970,485.0000,NULL,10,1),(4971,486.5000,NULL,10,1),(4972,488.0000,NULL,10,1),(4973,489.5000,NULL,10,1),(4974,491.0000,NULL,10,1),(4975,492.4000,NULL,10,1),(4976,493.9000,NULL,10,1),(4977,495.4000,NULL,10,1),(4978,496.9000,NULL,10,1),(4979,498.4000,NULL,10,1),(4980,499.9000,NULL,10,1),(4981,501.4000,NULL,10,1),(4982,502.9000,NULL,10,1),(4983,504.4000,NULL,10,1),(4984,505.9000,NULL,10,1),(4985,507.3000,NULL,10,1),(4986,508.8000,NULL,10,1),(4987,510.3000,NULL,10,1),(4988,511.8000,NULL,10,1),(4989,513.3000,NULL,10,1),(4990,514.8000,NULL,10,1),(4991,516.2000,NULL,10,1),(4992,517.7000,NULL,10,1),(4993,519.2000,NULL,10,1),(4994,520.7000,NULL,10,1),(4995,522.1000,NULL,10,1),(4996,523.6000,NULL,10,1),(4997,525.1000,NULL,10,1),(4998,526.6000,NULL,10,1),(4999,528.0000,NULL,10,1),(5000,529.5000,NULL,10,1),(5001,531.0000,NULL,10,1),(5002,532.5000,NULL,10,1),(5003,533.9000,NULL,10,1),(5004,535.4000,NULL,10,1),(5005,536.9000,NULL,10,1),(5006,538.3000,NULL,10,1),(5007,539.8000,NULL,10,1),(5008,541.2000,NULL,10,1),(5009,542.7000,NULL,10,1),(5010,544.2000,NULL,10,1),(5011,545.6000,NULL,10,1),(5012,547.1000,NULL,10,1),(5013,548.6000,NULL,10,1),(5014,550.0000,NULL,10,1),(5015,551.5000,NULL,10,1),(5016,552.9000,NULL,10,1),(5017,554.4000,NULL,10,1),(5018,555.9000,NULL,10,1),(5019,557.3000,NULL,10,1),(5020,558.8000,NULL,10,1),(5021,560.2000,NULL,10,1),(5022,561.7000,NULL,10,1),(5023,563.1000,NULL,10,1),(5024,564.6000,NULL,10,1),(5025,566.0000,NULL,10,1),(5026,567.5000,NULL,10,1),(5027,568.9000,NULL,10,1),(5028,570.4000,NULL,10,1),(5029,571.8000,NULL,10,1),(5030,573.2000,NULL,10,1),(5031,574.7000,NULL,10,1),(5032,576.1000,NULL,10,1),(5033,577.6000,NULL,10,1),(5034,579.0000,NULL,10,1),(5035,580.5000,NULL,10,1),(5036,581.9000,NULL,10,1),(5037,583.3000,NULL,10,1),(5038,584.8000,NULL,10,1),(5039,586.2000,NULL,10,1),(5040,587.6000,NULL,10,1),(5041,589.1000,NULL,10,1),(5042,590.5000,NULL,10,1),(5043,592.0000,NULL,10,1),(5044,593.4000,NULL,10,1),(5045,594.8000,NULL,10,1),(5046,596.2000,NULL,10,1),(5047,597.7000,NULL,10,1),(5048,599.1000,NULL,10,1),(5049,600.5000,NULL,10,1),(5050,602.0000,NULL,10,1),(5051,603.4000,NULL,10,1),(5052,604.8000,NULL,10,1),(5053,606.2000,NULL,10,1),(5054,607.7000,NULL,10,1),(5055,609.1000,NULL,10,1),(5056,610.5000,NULL,10,1),(5057,611.9000,NULL,10,1),(5058,613.4000,NULL,10,1),(5059,614.8000,NULL,10,1),(5060,616.2000,NULL,10,1),(5061,617.6000,NULL,10,1),(5062,619.0000,NULL,10,1),(5063,620.4000,NULL,10,1),(5064,621.8000,NULL,10,1),(5065,623.3000,NULL,10,1),(5066,624.7000,NULL,10,1),(5067,626.1000,NULL,10,1),(5068,627.5000,NULL,10,1),(5069,628.9000,NULL,10,1),(5070,630.3000,NULL,10,1),(5071,631.7000,NULL,10,1),(5072,633.1000,NULL,10,1),(5073,634.5000,NULL,10,1),(5074,635.9000,NULL,10,1),(5075,637.3000,NULL,10,1),(5076,638.7000,NULL,10,1),(5077,640.1000,NULL,10,1),(5078,641.5000,NULL,10,1),(5079,642.9000,NULL,10,1),(5080,644.3000,NULL,10,1),(5081,645.7000,NULL,10,1),(5082,647.1000,NULL,10,1),(5083,648.5000,NULL,10,1),(5084,649.9000,NULL,10,1),(5085,651.3000,NULL,10,1),(5086,652.7000,NULL,10,1),(5087,654.1000,NULL,10,1),(5088,655.5000,NULL,10,1),(5089,656.9000,NULL,10,1),(5090,658.2000,NULL,10,1),(5091,659.6000,NULL,10,1),(5092,661.0000,NULL,10,1),(5093,662.4000,NULL,10,1),(5094,663.8000,NULL,10,1),(5095,665.2000,NULL,10,1),(5096,666.6000,NULL,10,1),(5097,667.9000,NULL,10,1),(5098,669.3000,NULL,10,1),(5099,670.7000,NULL,10,1),(5100,672.1000,NULL,10,1),(5101,673.5000,NULL,10,1),(5102,674.8000,NULL,10,1),(5103,676.2000,NULL,10,1),(5104,677.6000,NULL,10,1),(5105,678.9000,NULL,10,1),(5106,680.3000,NULL,10,1),(5107,681.7000,NULL,10,1),(5108,683.0000,NULL,10,1),(5109,684.4000,NULL,10,1),(5110,685.8000,NULL,10,1),(5111,687.1000,NULL,10,1),(5112,688.5000,NULL,10,1),(5113,689.9000,NULL,10,1),(5114,691.2000,NULL,10,1),(5115,692.6000,NULL,10,1),(5116,694.0000,NULL,10,1),(5117,695.3000,NULL,10,1),(5118,696.7000,NULL,10,1),(5119,698.0000,NULL,10,1),(5120,699.4000,NULL,10,1),(5121,700.7000,NULL,10,1),(5122,702.1000,NULL,10,1),(5123,703.4000,NULL,10,1),(5124,704.8000,NULL,10,1),(5125,706.1000,NULL,10,1),(5126,707.5000,NULL,10,1),(5127,708.8000,NULL,10,1),(5128,710.2000,NULL,10,1),(5129,711.5000,NULL,10,1),(5130,712.9000,NULL,10,1),(5131,714.2000,NULL,10,1),(5132,715.5000,NULL,10,1),(5133,716.9000,NULL,10,1),(5134,718.2000,NULL,10,1),(5135,719.6000,NULL,10,1),(5136,720.9000,NULL,10,1),(5137,722.2000,NULL,10,1),(5138,723.6000,NULL,10,1),(5139,724.9000,NULL,10,1),(5140,726.2000,NULL,10,1),(5141,727.6000,NULL,10,1),(5142,728.9000,NULL,10,1),(5143,730.2000,NULL,10,1),(5144,731.5000,NULL,10,1),(5145,732.9000,NULL,10,1),(5146,734.2000,NULL,10,1),(5147,735.5000,NULL,10,1),(5148,736.8000,NULL,10,1),(5149,738.1000,NULL,10,1),(5150,739.5000,NULL,10,1),(5151,740.8000,NULL,10,1),(5152,742.1000,NULL,10,1),(5153,743.4000,NULL,10,1),(5154,744.7000,NULL,10,1),(5155,746.0000,NULL,10,1),(5156,747.3000,NULL,10,1),(5157,748.6000,NULL,10,1),(5158,749.9000,NULL,10,1),(5159,751.2000,NULL,10,1),(5160,752.5000,NULL,10,1),(5161,753.9000,NULL,10,1),(5162,755.1000,NULL,10,1),(5163,756.5000,NULL,10,1),(5164,757.8000,NULL,10,1),(5165,759.0000,NULL,10,1),(5166,760.3000,NULL,10,1),(5167,761.6000,NULL,10,1),(5168,762.9000,NULL,10,1),(5169,764.2000,NULL,10,1),(5170,765.5000,NULL,10,1),(5171,766.8000,NULL,10,1),(5172,768.1000,NULL,10,1),(5173,769.4000,NULL,10,1),(5174,770.6000,NULL,10,1),(5175,771.9000,NULL,10,1),(5176,773.2000,NULL,10,1),(5177,774.5000,NULL,10,1),(5178,775.8000,NULL,10,1),(5179,777.0000,NULL,10,1),(5180,778.3000,NULL,10,1),(5181,779.6000,NULL,10,1),(5182,780.9000,NULL,10,1),(5183,782.1000,NULL,10,1),(5184,783.4000,NULL,10,1),(5185,784.7000,NULL,10,1),(5186,785.9000,NULL,10,1),(5187,787.2000,NULL,10,1),(5188,788.5000,NULL,10,1),(5189,789.7000,NULL,10,1),(5190,791.0000,NULL,10,1),(5191,792.2000,NULL,10,1),(5192,793.5000,NULL,10,1),(5193,794.7000,NULL,10,1),(5194,796.0000,NULL,10,1),(5195,797.2000,NULL,10,1),(5196,798.5000,NULL,10,1),(5197,799.7000,NULL,10,1),(5198,801.0000,NULL,10,1),(5199,802.2000,NULL,10,1),(5200,803.5000,NULL,10,1),(5201,804.7000,NULL,10,1),(5202,806.0000,NULL,10,1),(5203,807.2000,NULL,10,1),(5204,808.4000,NULL,10,1),(5205,809.6000,NULL,10,1),(5206,810.9000,NULL,10,1),(5207,812.1000,NULL,10,1),(5208,813.4000,NULL,10,1),(5209,814.6000,NULL,10,1),(5210,815.8000,NULL,10,1),(5211,817.0000,NULL,10,1),(5212,818.2000,NULL,10,1),(5213,819.5000,NULL,10,1),(5214,820.7000,NULL,10,1),(5215,821.9000,NULL,10,1),(5216,823.1000,NULL,10,1),(5217,824.3000,NULL,10,1),(5218,825.5000,NULL,10,1),(5219,826.8000,NULL,10,1),(5220,828.0000,NULL,10,1),(5221,829.2000,NULL,10,1),(5222,830.4000,NULL,10,1),(5223,831.6000,NULL,10,1),(5224,832.8000,NULL,10,1),(5225,834.0000,NULL,10,1),(5226,835.2000,NULL,10,1),(5227,836.4000,NULL,10,1),(5228,837.6000,NULL,10,1),(5229,838.8000,NULL,10,1),(5230,840.0000,NULL,10,1),(5231,841.1000,NULL,10,1),(5232,842.3000,NULL,10,1),(5233,843.5000,NULL,10,1),(5234,844.7000,NULL,10,1),(5235,845.9000,NULL,10,1),(5236,847.1000,NULL,10,1),(5237,848.2000,NULL,10,1),(5238,849.4000,NULL,10,1),(5239,850.6000,NULL,10,1),(5240,851.8000,NULL,10,1),(5241,853.0000,NULL,10,1),(5242,854.1000,NULL,10,1),(5243,855.3000,NULL,10,1),(5244,856.5000,NULL,10,1),(5245,857.6000,NULL,10,1),(5246,858.8000,NULL,10,1),(5247,859.9000,NULL,10,1),(5248,861.1000,NULL,10,1),(5249,862.3000,NULL,10,1),(5250,863.4000,NULL,10,1),(5251,864.6000,NULL,10,1),(5252,865.7000,NULL,10,1),(5253,866.9000,NULL,10,1),(5254,868.0000,NULL,10,1),(5255,869.2000,NULL,10,1),(5256,870.3000,NULL,10,1),(5257,871.5000,NULL,10,1),(5258,872.6000,NULL,10,1),(5259,873.7000,NULL,10,1),(5260,874.9000,NULL,10,1),(5261,876.0000,NULL,10,1),(5262,877.1000,NULL,10,1),(5263,878.3000,NULL,10,1),(5264,879.4000,NULL,10,1),(5265,880.5000,NULL,10,1),(5266,881.7000,NULL,10,1),(5267,882.8000,NULL,10,1),(5268,883.9000,NULL,10,1),(5269,885.0000,NULL,10,1),(5270,886.2000,NULL,10,1),(5271,887.3000,NULL,10,1),(5272,888.4000,NULL,10,1),(5273,889.5000,NULL,10,1),(5274,890.6000,NULL,10,1),(5275,891.7000,NULL,10,1),(5276,892.9000,NULL,10,1),(5277,894.0000,NULL,10,1),(5278,895.1000,NULL,10,1),(5279,896.2000,NULL,10,1),(5280,897.3000,NULL,10,1),(5281,898.4000,NULL,10,1),(5282,899.5000,NULL,10,1),(5283,900.6000,NULL,10,1),(5284,901.7000,NULL,10,1),(5285,902.8000,NULL,10,1),(5286,903.9000,NULL,10,1),(5287,905.0000,NULL,10,1),(5288,906.0000,NULL,10,1),(5289,907.1000,NULL,10,1),(5290,908.2000,NULL,10,1),(5291,909.3000,NULL,10,1),(5292,910.4000,NULL,10,1),(5293,911.5000,NULL,10,1),(5294,912.6000,NULL,10,1),(5295,913.6000,NULL,10,1),(5296,914.7000,NULL,10,1),(5297,915.8000,NULL,10,1),(5298,916.9000,NULL,10,1),(5299,917.9000,NULL,10,1),(5300,919.0000,NULL,10,1),(5301,920.1000,NULL,10,1),(5302,921.1000,NULL,10,1),(5303,922.2000,NULL,10,1),(5304,923.3000,NULL,10,1),(5305,924.4000,NULL,10,1),(5306,925.4000,NULL,10,1),(5307,926.5000,NULL,10,1),(5308,927.5000,NULL,10,1),(5309,928.6000,NULL,10,1),(5310,929.6000,NULL,10,1),(5311,930.7000,NULL,10,1),(5312,931.8000,NULL,10,1),(5313,932.8000,NULL,10,1),(5314,933.9000,NULL,10,1),(5315,934.9000,NULL,10,1),(5316,936.0000,NULL,10,1),(5317,937.0000,NULL,10,1),(5318,938.1000,NULL,10,1),(5319,939.1000,NULL,10,1),(5320,940.2000,NULL,10,1),(5321,941.2000,NULL,10,1),(5322,942.2000,NULL,10,1),(5323,943.3000,NULL,10,1),(5324,944.3000,NULL,10,1),(5325,945.4000,NULL,10,1),(5326,946.4000,NULL,10,1),(5327,947.4000,NULL,10,1),(5328,948.5000,NULL,10,1),(5329,949.5000,NULL,10,1),(5330,950.5000,NULL,10,1),(5331,951.6000,NULL,10,1),(5332,952.6000,NULL,10,1),(5333,953.6000,NULL,10,1),(5334,954.7000,NULL,10,1),(5335,955.7000,NULL,10,1),(5336,956.7000,NULL,10,1),(5337,957.8000,NULL,10,1),(5338,958.8000,NULL,10,1),(5339,959.8000,NULL,10,1),(5340,960.8000,NULL,10,1),(5341,961.9000,NULL,10,1),(5342,962.9000,NULL,10,1),(5343,963.9000,NULL,10,1),(5344,964.9000,NULL,10,1),(5345,966.0000,NULL,10,1),(5346,967.0000,NULL,10,1),(5347,968.0000,NULL,10,1),(5348,969.0000,NULL,10,1),(5349,970.0000,NULL,10,1),(5350,971.1000,NULL,10,1),(5351,972.1000,NULL,10,1),(5352,973.1000,NULL,10,1),(5353,974.1000,NULL,10,1),(5354,975.1000,NULL,10,1),(5355,976.2000,NULL,10,1),(5356,977.2000,NULL,10,1),(5357,978.2000,NULL,10,1),(5358,979.2000,NULL,10,1),(5359,980.2000,NULL,10,1),(5360,981.3000,NULL,10,1),(5361,982.3000,NULL,10,1),(5362,983.3000,NULL,10,1),(5363,984.3000,NULL,10,1),(5364,985.3000,NULL,10,1),(5365,986.4000,NULL,10,1),(5366,987.4000,NULL,10,1),(5367,988.4000,NULL,10,1),(5368,989.4000,NULL,10,1),(5369,990.4000,NULL,10,1),(5370,991.5000,NULL,10,1),(5371,992.5000,NULL,10,1),(5372,993.5000,NULL,10,1),(5373,994.5000,NULL,10,1),(5374,995.5000,NULL,10,1),(5375,996.6000,NULL,10,1),(5376,997.6000,NULL,10,1),(5377,998.6000,NULL,10,1),(5378,999.6000,NULL,10,1),(5379,1000.7000,NULL,10,1),(5380,1001.7000,NULL,10,1),(5381,1002.7000,NULL,10,1),(5382,1003.8000,NULL,10,1),(5383,1004.8000,NULL,10,1),(5384,1005.8000,NULL,10,1),(5385,1006.8000,NULL,10,1),(5386,1007.9000,NULL,10,1),(5387,1008.9000,NULL,10,1),(5388,1009.9000,NULL,10,1),(5389,1011.0000,NULL,10,1),(5390,946.8000,NULL,10,1),(5391,950.7000,NULL,10,1),(5392,954.5000,NULL,10,1),(5393,958.4000,NULL,10,1),(5394,962.3000,NULL,10,1),(5395,966.2000,NULL,10,1),(5396,970.1000,NULL,10,1),(5397,973.9000,NULL,10,1),(5398,977.8000,NULL,10,1),(5399,981.7000,NULL,10,1),(5400,985.6000,NULL,10,1),(5401,989.4000,NULL,10,1),(5402,993.3000,NULL,10,1),(5403,997.2000,NULL,10,1),(5404,1001.1000,NULL,10,1),(5405,1004.9000,NULL,10,1),(5406,1008.8000,NULL,10,1),(5407,1012.7000,NULL,10,1),(5408,1016.5000,NULL,10,1),(5409,1020.4000,NULL,10,1),(5410,1024.3000,NULL,10,1),(5411,1028.2000,NULL,10,1),(5412,1032.0000,NULL,10,1),(5413,1035.9000,NULL,10,1),(5414,1039.8000,NULL,10,1),(5415,1043.6000,NULL,10,1),(5416,1047.5000,NULL,10,1),(5417,1051.4000,NULL,10,1),(5418,1055.2000,NULL,10,1),(5419,1059.1000,NULL,10,1),(5420,1063.0000,NULL,10,1),(5421,1066.8000,NULL,10,1),(5422,1070.7000,NULL,10,1),(5423,1074.5000,NULL,10,1),(5424,1078.4000,NULL,10,1),(5425,1082.3000,NULL,10,1),(5426,1086.1000,NULL,10,1),(5427,1090.0000,NULL,10,1),(5428,1093.8000,NULL,10,1),(5429,1097.7000,NULL,10,1),(5430,1101.6000,NULL,10,1),(5431,1105.4000,NULL,10,1),(5432,1109.3000,NULL,10,1),(5433,1113.1000,NULL,10,1),(5434,1117.0000,NULL,10,1),(5435,1120.8000,NULL,10,1),(5436,1124.7000,NULL,10,1),(5437,1128.5000,NULL,10,1),(5438,1132.4000,NULL,10,1),(5439,1136.2000,NULL,10,1),(5440,1140.1000,NULL,10,1),(5441,1143.9000,NULL,10,1),(5442,1147.8000,NULL,10,1),(5443,1151.6000,NULL,10,1),(5444,1155.4000,NULL,10,1),(5445,1159.3000,NULL,10,1),(5446,1163.1000,NULL,10,1),(5447,1167.0000,NULL,10,1),(5448,1170.8000,NULL,10,1),(5449,1174.6000,NULL,10,1),(5450,1178.5000,NULL,10,1),(5451,1182.3000,NULL,10,1),(5452,1186.1000,NULL,10,1),(5453,1190.0000,NULL,10,1),(5454,1193.8000,NULL,10,1),(5455,1197.6000,NULL,10,1),(5456,1201.4000,NULL,10,1),(5457,1205.3000,NULL,10,1),(5458,1209.1000,NULL,10,1),(5459,1212.9000,NULL,10,1),(5460,1216.7000,NULL,10,1),(5461,1220.5000,NULL,10,1),(5462,1224.4000,NULL,10,1),(5463,1228.2000,NULL,10,1),(5464,1232.0000,NULL,10,1),(5465,1235.8000,NULL,10,1),(5466,1239.6000,NULL,10,1),(5467,1243.4000,NULL,10,1),(5468,1247.2000,NULL,10,1),(5469,1251.0000,NULL,10,1),(5470,1254.8000,NULL,10,1),(5471,1258.7000,NULL,10,1),(5472,1262.5000,NULL,10,1),(5473,1266.2000,NULL,10,1),(5474,1270.0000,NULL,10,1),(5475,1273.8000,NULL,10,1),(5476,1277.6000,NULL,10,1),(5477,1281.4000,NULL,10,1),(5478,1285.2000,NULL,10,1),(5479,1289.0000,NULL,10,1),(5480,1292.8000,NULL,10,1),(5481,1296.6000,NULL,10,1),(5482,1300.3000,NULL,10,1),(5483,1304.1000,NULL,10,1),(5484,1307.9000,NULL,10,1),(5485,1311.7000,NULL,10,1),(5486,1315.5000,NULL,10,1),(5487,1319.2000,NULL,10,1),(5488,1323.0000,NULL,10,1),(5489,1326.8000,NULL,10,1),(5490,1330.5000,NULL,10,1),(5491,1334.3000,NULL,10,1),(5492,1338.1000,NULL,10,1),(5493,1341.8000,NULL,10,1),(5494,1345.6000,NULL,10,1),(5495,1349.3000,NULL,10,1),(5496,1353.1000,NULL,10,1),(5497,1356.8000,NULL,10,1),(5498,1360.6000,NULL,10,1),(5499,1364.3000,NULL,10,1),(5500,1368.1000,NULL,10,1),(5501,1371.8000,NULL,10,1),(5502,1375.6000,NULL,10,1),(5503,1379.3000,NULL,10,1),(5504,1383.0000,NULL,10,1),(5505,1386.8000,NULL,10,1),(5506,1390.5000,NULL,10,1),(5507,1394.2000,NULL,10,1),(5508,1398.0000,NULL,10,1),(5509,1401.7000,NULL,10,1),(5510,1405.4000,NULL,10,1),(5511,1409.1000,NULL,10,1),(5512,1412.8000,NULL,10,1),(5513,1416.5000,NULL,10,1),(5514,1420.3000,NULL,10,1),(5515,1424.0000,NULL,10,1),(5516,1427.7000,NULL,10,1),(5517,1431.4000,NULL,10,1),(5518,1435.1000,NULL,10,1),(5519,1438.8000,NULL,10,1),(5520,1442.5000,NULL,10,1),(5521,1446.2000,NULL,10,1),(5522,1449.9000,NULL,10,1),(5523,1453.5000,NULL,10,1),(5524,1457.2000,NULL,10,1),(5525,1460.9000,NULL,10,1),(5526,1464.6000,NULL,10,1),(5527,1468.3000,NULL,10,1),(5528,1471.9000,NULL,10,1),(5529,1475.6000,NULL,10,1),(5530,1479.3000,NULL,10,1),(5531,1483.0000,NULL,10,1),(5532,1486.6000,NULL,10,1),(5533,1490.3000,NULL,10,1),(5534,1493.9000,NULL,10,1),(5535,1497.6000,NULL,10,1),(5536,1501.2000,NULL,10,1),(5537,1504.9000,NULL,10,1),(5538,1508.5000,NULL,10,1),(5539,1512.2000,NULL,10,1),(5540,1515.8000,NULL,10,1),(5541,1519.4000,NULL,10,1),(5542,1523.1000,NULL,10,1),(5543,1526.7000,NULL,10,1),(5544,1530.3000,NULL,10,1),(5545,1534.0000,NULL,10,1),(5546,1537.6000,NULL,10,1),(5547,1541.2000,NULL,10,1),(5548,1544.8000,NULL,10,1),(5549,1548.4000,NULL,10,1),(5550,1552.0000,NULL,10,1),(5551,1555.6000,NULL,10,1),(5552,1559.2000,NULL,10,1),(5553,1562.8000,NULL,10,1),(5554,1566.4000,NULL,10,1),(5555,1570.0000,NULL,10,1),(5556,1573.6000,NULL,10,1),(5557,1577.2000,NULL,10,1),(5558,1580.8000,NULL,10,1),(5559,1584.3000,NULL,10,1),(5560,1587.9000,NULL,10,1),(5561,1591.5000,NULL,10,1),(5562,1595.0000,NULL,10,1),(5563,1598.6000,NULL,10,1),(5564,1602.2000,NULL,10,1),(5565,1605.7000,NULL,10,1),(5566,1609.3000,NULL,10,1),(5567,1612.8000,NULL,10,1),(5568,1616.4000,NULL,10,1),(5569,1619.9000,NULL,10,1),(5570,1623.5000,NULL,10,1),(5571,1627.0000,NULL,10,1),(5572,1630.5000,NULL,10,1),(5573,1634.0000,NULL,10,1),(5574,1637.6000,NULL,10,1),(5575,1641.1000,NULL,10,1),(5576,1644.6000,NULL,10,1),(5577,1648.1000,NULL,10,1),(5578,1651.6000,NULL,10,1),(5579,1655.1000,NULL,10,1),(5580,1658.6000,NULL,10,1),(5581,1662.1000,NULL,10,1),(5582,1665.6000,NULL,10,1),(5583,1669.1000,NULL,10,1),(5584,1672.6000,NULL,10,1),(5585,1676.1000,NULL,10,1),(5586,1679.6000,NULL,10,1),(5587,1683.0000,NULL,10,1),(5588,1686.5000,NULL,10,1),(5589,1690.0000,NULL,10,1),(5590,1693.4000,NULL,10,1),(5591,1696.9000,NULL,10,1),(5592,1700.3000,NULL,10,1),(5593,1703.8000,NULL,10,1),(5594,1707.2000,NULL,10,1),(5595,1710.7000,NULL,10,1),(5596,1714.1000,NULL,10,1),(5597,1717.6000,NULL,10,1),(5598,1721.0000,NULL,10,1),(5599,1724.4000,NULL,10,1),(5600,1727.8000,NULL,10,1),(5601,1731.2000,NULL,10,1),(5602,1734.7000,NULL,10,1),(5603,1738.1000,NULL,10,1),(5604,1741.5000,NULL,10,1),(5605,1744.9000,NULL,10,1),(5606,1748.3000,NULL,10,1),(5607,1751.7000,NULL,10,1),(5608,1755.0000,NULL,10,1),(5609,1758.4000,NULL,10,1),(5610,1761.8000,NULL,10,1),(5611,1765.2000,NULL,10,1),(5612,1768.6000,NULL,10,1),(5613,1771.9000,NULL,10,1),(5614,1775.3000,NULL,10,1),(5615,1778.6000,NULL,10,1),(5616,1782.0000,NULL,10,1),(5617,1785.3000,NULL,10,1),(5618,1788.7000,NULL,10,1),(5619,1792.0000,NULL,10,1),(5620,1795.3000,NULL,10,1),(5621,1798.7000,NULL,10,1),(5622,1802.0000,NULL,10,1),(5623,1805.3000,NULL,10,1),(5624,1808.6000,NULL,10,1),(5625,1812.0000,NULL,10,1),(5626,1815.3000,NULL,10,1),(5627,1818.6000,NULL,10,1),(5628,1821.8000,NULL,10,1),(5629,1825.2000,NULL,10,1),(5630,1828.4000,NULL,10,1),(5631,1831.7000,NULL,10,1),(5632,1835.0000,NULL,10,1),(5633,1838.3000,NULL,10,1),(5634,1841.5000,NULL,10,1),(5635,1844.8000,NULL,10,1),(5636,1848.0000,NULL,10,1),(5637,1851.3000,NULL,10,1),(5638,1854.5000,NULL,10,1),(5639,1857.8000,NULL,10,1),(5640,1861.0000,NULL,10,1),(5641,1864.3000,NULL,10,1),(5642,1867.5000,NULL,10,1),(5643,1870.7000,NULL,10,1),(5644,1873.9000,NULL,10,1),(5645,1877.2000,NULL,10,1),(5646,1876.7000,NULL,10,1),(5647,1879.5000,NULL,10,1),(5648,1882.3000,NULL,10,1),(5649,1885.1000,NULL,10,1),(5650,1887.9000,NULL,10,1),(5651,1890.7000,NULL,10,1),(5652,1893.5000,NULL,10,1),(5653,1896.3000,NULL,10,1),(5654,1899.1000,NULL,10,1),(5655,1901.9000,NULL,10,1),(5656,1904.7000,NULL,10,1),(5657,1907.4000,NULL,10,1),(5658,1910.2000,NULL,10,1),(5659,1913.0000,NULL,10,1),(5660,1915.8000,NULL,10,1),(5661,1918.5000,NULL,10,1),(5662,1921.3000,NULL,10,1),(5663,1924.1000,NULL,10,1),(5664,1926.8000,NULL,10,1),(5665,1929.6000,NULL,10,1),(5666,1932.3000,NULL,10,1),(5667,1935.1000,NULL,10,1),(5668,1937.8000,NULL,10,1),(5669,1940.6000,NULL,10,1),(5670,1943.3000,NULL,10,1),(5671,1946.1000,NULL,10,1),(5672,1948.8000,NULL,10,1),(5673,1951.6000,NULL,10,1),(5674,1954.3000,NULL,10,1),(5675,1957.0000,NULL,10,1),(5676,1959.8000,NULL,10,1),(5677,1962.5000,NULL,10,1),(5678,1965.2000,NULL,10,1),(5679,1967.9000,NULL,10,1),(5680,1970.6000,NULL,10,1),(5681,1973.4000,NULL,10,1),(5682,1976.1000,NULL,10,1),(5683,1978.8000,NULL,10,1),(5684,1981.5000,NULL,10,1),(5685,1984.2000,NULL,10,1),(5686,1986.9000,NULL,10,1),(5687,1989.6000,NULL,10,1),(5688,1992.3000,NULL,10,1),(5689,1995.0000,NULL,10,1),(5690,1997.7000,NULL,10,1),(5691,2000.4000,NULL,10,1),(5692,2003.1000,NULL,10,1),(5693,2005.8000,NULL,10,1),(5694,2008.4000,NULL,10,1),(5695,2011.1000,NULL,10,1),(5696,2013.8000,NULL,10,1),(5697,2016.5000,NULL,10,1),(5698,2019.1000,NULL,10,1),(5699,2021.8000,NULL,10,1),(5700,2024.5000,NULL,10,1),(5701,2027.1000,NULL,10,1),(5702,2029.8000,NULL,10,1),(5703,2032.4000,NULL,10,1),(5704,2035.1000,NULL,10,1),(5705,2037.8000,NULL,10,1),(5706,2040.4000,NULL,10,1),(5707,2043.0000,NULL,10,1),(5708,2045.7000,NULL,10,1),(5709,2048.3000,NULL,10,1),(5710,2051.0000,NULL,10,1),(5711,2053.6000,NULL,10,1),(5712,2056.2000,NULL,10,1),(5713,2058.9000,NULL,10,1),(5714,2061.5000,NULL,10,1),(5715,2064.1000,NULL,10,1),(5716,2066.8000,NULL,10,1),(5717,2069.4000,NULL,10,1),(5718,2072.0000,NULL,10,1),(5719,2074.6000,NULL,10,1),(5720,2077.2000,NULL,10,1),(5721,2079.8000,NULL,10,1),(5722,2082.4000,NULL,10,1),(5723,2085.0000,NULL,10,1),(5724,2087.6000,NULL,10,1),(5725,2090.2000,NULL,10,1),(5726,2092.8000,NULL,10,1),(5727,2095.4000,NULL,10,1),(5728,2098.0000,NULL,10,1),(5729,2100.6000,NULL,10,1),(5730,2103.2000,NULL,10,1),(5731,2105.8000,NULL,10,1),(5732,2108.3000,NULL,10,1),(5733,2110.9000,NULL,10,1),(5734,2113.5000,NULL,10,1),(5735,2116.1000,NULL,10,1),(5736,2118.7000,NULL,10,1),(5737,2121.2000,NULL,10,1),(5738,2123.8000,NULL,10,1),(5739,2126.3000,NULL,10,1),(5740,2128.9000,NULL,10,1),(5741,2131.4000,NULL,10,1),(5742,2134.0000,NULL,10,1),(5743,2136.6000,NULL,10,1),(5744,2139.1000,NULL,10,1),(5745,2141.7000,NULL,10,1),(5746,2144.2000,NULL,10,1),(5747,2146.7000,NULL,10,1),(5748,2149.3000,NULL,10,1),(5749,2151.8000,NULL,10,1),(5750,2154.3000,NULL,10,1),(5751,2156.9000,NULL,10,1),(5752,2159.4000,NULL,10,1),(5753,2161.9000,NULL,10,1),(5754,2164.4000,NULL,10,1),(5755,2167.0000,NULL,10,1),(5756,2169.5000,NULL,10,1),(5757,2172.0000,NULL,10,1),(5758,2174.5000,NULL,10,1),(5759,2177.0000,NULL,10,1),(5760,2179.5000,NULL,10,1),(5761,2182.0000,NULL,10,1),(5762,2184.5000,NULL,10,1),(5763,2187.0000,NULL,10,1),(5764,2189.5000,NULL,10,1),(5765,2192.0000,NULL,10,1),(5766,2194.5000,NULL,10,1),(5767,2197.0000,NULL,10,1),(5768,2199.5000,NULL,10,1),(5769,2201.9000,NULL,10,1),(5770,2204.4000,NULL,10,1),(5771,2206.9000,NULL,10,1),(5772,2209.4000,NULL,10,1),(5773,2211.8000,NULL,10,1),(5774,2214.3000,NULL,10,1),(5775,2216.8000,NULL,10,1),(5776,2219.2000,NULL,10,1),(5777,2221.7000,NULL,10,1),(5778,2224.2000,NULL,10,1),(5779,2226.6000,NULL,10,1),(5780,2229.1000,NULL,10,1),(5781,2231.5000,NULL,10,1),(5782,2234.0000,NULL,10,1),(5783,2236.4000,NULL,10,1),(5784,2238.9000,NULL,10,1),(5785,2241.3000,NULL,10,1),(5786,2243.7000,NULL,10,1),(5787,2246.2000,NULL,10,1),(5788,2248.6000,NULL,10,1),(5789,2251.0000,NULL,10,1),(5790,2253.4000,NULL,10,1),(5791,2255.9000,NULL,10,1),(5792,2258.3000,NULL,10,1),(5793,2260.7000,NULL,10,1),(5794,2263.1000,NULL,10,1),(5795,2265.5000,NULL,10,1),(5796,2267.9000,NULL,10,1),(5797,2270.4000,NULL,10,1),(5798,2272.8000,NULL,10,1),(5799,2275.2000,NULL,10,1),(5800,2277.6000,NULL,10,1),(5801,2280.0000,NULL,10,1),(5802,2282.4000,NULL,10,1),(5803,2284.8000,NULL,10,1),(5804,2287.1000,NULL,10,1),(5805,2289.5000,NULL,10,1),(5806,2291.9000,NULL,10,1),(5807,2294.3000,NULL,10,1),(5808,2296.7000,NULL,10,1),(5809,2299.0000,NULL,10,1),(5810,2301.4000,NULL,10,1),(5811,2303.8000,NULL,10,1),(5812,2306.2000,NULL,10,1),(5813,2308.5000,NULL,10,1),(5814,2310.9000,NULL,10,1),(5815,2313.2000,NULL,10,1),(5816,2315.6000,NULL,10,1),(5817,2317.9000,NULL,10,1),(5818,2320.3000,NULL,10,1),(5819,2322.7000,NULL,10,1),(5820,2325.0000,NULL,10,1),(5821,2327.3000,NULL,10,1),(5822,2329.7000,NULL,10,1),(5823,2332.0000,NULL,10,1),(5824,2334.3000,NULL,10,1),(5825,2336.7000,NULL,10,1),(5826,2339.0000,NULL,10,1),(5827,2341.3000,NULL,10,1),(5828,2343.7000,NULL,10,1),(5829,2346.0000,NULL,10,1),(5830,2348.3000,NULL,10,1),(5831,2350.6000,NULL,10,1),(5832,2352.9000,NULL,10,1),(5833,2355.2000,NULL,10,1),(5834,2357.6000,NULL,10,1),(5835,2359.9000,NULL,10,1),(5836,2362.2000,NULL,10,1),(5837,2364.5000,NULL,10,1),(5838,2366.8000,NULL,10,1),(5839,2369.1000,NULL,10,1),(5840,2371.4000,NULL,10,1),(5841,2373.7000,NULL,10,1),(5842,2375.9000,NULL,10,1),(5843,2378.2000,NULL,10,1),(5844,2380.5000,NULL,10,1),(5845,2382.8000,NULL,10,1),(5846,2385.1000,NULL,10,1),(5847,2387.3000,NULL,10,1),(5848,2389.6000,NULL,10,1),(5849,2391.9000,NULL,10,1),(5850,2394.1000,NULL,10,1),(5851,2396.4000,NULL,10,1),(5852,2398.7000,NULL,10,1),(5853,2400.9000,NULL,10,1),(5854,2403.2000,NULL,10,1),(5855,2405.4000,NULL,10,1),(5856,2407.7000,NULL,10,1),(5857,2409.9000,NULL,10,1),(5858,2412.2000,NULL,10,1),(5859,2414.4000,NULL,10,1),(5860,2416.7000,NULL,10,1),(5861,2418.9000,NULL,10,1),(5862,2421.1000,NULL,10,1),(5863,2423.3000,NULL,10,1),(5864,2425.6000,NULL,10,1),(5865,2427.8000,NULL,10,1),(5866,2430.0000,NULL,10,1),(5867,2432.2000,NULL,10,1),(5868,2434.5000,NULL,10,1),(5869,2436.7000,NULL,10,1),(5870,2438.9000,NULL,10,1),(5871,2441.1000,NULL,10,1),(5872,2443.3000,NULL,10,1),(5873,2445.5000,NULL,10,1),(5874,2447.7000,NULL,10,1),(5875,2449.9000,NULL,10,1),(5876,2452.1000,NULL,10,1),(5877,2454.3000,NULL,10,1),(5878,2456.5000,NULL,10,1),(5879,2458.7000,NULL,10,1),(5880,2460.9000,NULL,10,1),(5881,2463.1000,NULL,10,1),(5882,2465.2000,NULL,10,1),(5883,2467.4000,NULL,10,1),(5884,2469.6000,NULL,10,1),(5885,2471.8000,NULL,10,1),(5886,2473.9000,NULL,10,1),(5887,2476.1000,NULL,10,1),(5888,2478.3000,NULL,10,1),(5889,2480.4000,NULL,10,1),(5890,2482.6000,NULL,10,1),(5891,2484.8000,NULL,10,1),(5892,2486.9000,NULL,10,1),(5893,2489.1000,NULL,10,1),(5894,2491.2000,NULL,10,1),(5895,2493.4000,NULL,10,1),(5896,2495.5000,NULL,10,1),(5897,2497.6000,NULL,10,1),(5898,2499.8000,NULL,10,1),(5899,2501.9000,NULL,10,1),(5900,2504.1000,NULL,10,1),(5901,2506.2000,NULL,10,1);
/*!40000 ALTER TABLE `sensor_element` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensor_element_type`
--

DROP TABLE IF EXISTS `sensor_element_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sensor_element_type` (
  `sensor_element_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `code` int(10) unsigned NOT NULL,
  PRIMARY KEY (`sensor_element_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor_element_type`
--

LOCK TABLES `sensor_element_type` WRITE;
/*!40000 ALTER TABLE `sensor_element_type` DISABLE KEYS */;
INSERT INTO `sensor_element_type` VALUES (1,'Narrowband',0),(2,'Broadband',1);
/*!40000 ALTER TABLE `sensor_element_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specchio_group`
--

DROP TABLE IF EXISTS `specchio_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `specchio_group` (
  `group_id` int(10) NOT NULL,
  `group_name` char(16) DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specchio_group`
--

LOCK TABLES `specchio_group` WRITE;
/*!40000 ALTER TABLE `specchio_group` DISABLE KEYS */;
INSERT INTO `specchio_group` VALUES (1,'admin'),(2,'user'),(99,'anonymous');
/*!40000 ALTER TABLE `specchio_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specchio_user`
--

DROP TABLE IF EXISTS `specchio_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `specchio_user` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user` char(16) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `title` varchar(10) DEFAULT NULL,
  `email` varchar(45) NOT NULL,
  `www` varchar(250) DEFAULT NULL,
  `institute_id` int(10) unsigned DEFAULT NULL,
  `admin` tinyint(1) NOT NULL DEFAULT '0',
  `password` varchar(100) DEFAULT NULL,
  `external_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `FK_specchio_user_1` (`institute_id`),
  CONSTRAINT `FK_specchio_user_1` FOREIGN KEY (`institute_id`) REFERENCES `institute` (`institute_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specchio_user`
--

LOCK TABLES `specchio_user` WRITE;
/*!40000 ALTER TABLE `specchio_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `specchio_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specchio_user_group`
--

DROP TABLE IF EXISTS `specchio_user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `specchio_user_group` (
  `user` char(16) DEFAULT NULL,
  `group_name` char(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specchio_user_group`
--

LOCK TABLES `specchio_user_group` WRITE;
/*!40000 ALTER TABLE `specchio_user_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `specchio_user_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spectrum`
--

DROP TABLE IF EXISTS `spectrum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `spectrum` (
  `spectrum_id` int(11) NOT NULL AUTO_INCREMENT,
  `goniometer_id` int(10) unsigned DEFAULT NULL,
  `target_homogeneity_id` int(10) unsigned DEFAULT NULL,
  `illumination_source_id` int(10) unsigned DEFAULT NULL,
  `sampling_environment_id` int(10) unsigned DEFAULT NULL,
  `measurement_type_id` int(10) unsigned DEFAULT NULL,
  `measurement_unit_id` int(10) unsigned DEFAULT NULL,
  `landcover_id` int(10) unsigned DEFAULT NULL,
  `measurement` blob,
  `is_reference` tinyint(1) DEFAULT NULL,
  `hierarchy_level_id` int(11) DEFAULT NULL,
  `sensor_id` int(11) DEFAULT NULL,
  `file_format_id` int(11) DEFAULT NULL,
  `campaign_id` int(11) DEFAULT NULL,
  `instrument_id` int(10) unsigned DEFAULT NULL,
  `required_quality_level_id` int(10) unsigned DEFAULT NULL,
  `quality_level_id` int(10) unsigned DEFAULT NULL,
  `reference_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`spectrum_id`),
  KEY `FK_spectrum_1` (`hierarchy_level_id`),
  KEY `FK_spectrum_2` (`sensor_id`),
  KEY `FK_spectrum_3` (`file_format_id`),
  KEY `FK_spectrum_4` (`campaign_id`),
  KEY `instrument_id` (`instrument_id`),
  KEY `landcover_id` (`landcover_id`),
  KEY `measurement_unit_id` (`measurement_unit_id`),
  KEY `measurement_type_id` (`measurement_type_id`),
  KEY `sampling_environment_id` (`sampling_environment_id`),
  KEY `illumination_source_id` (`illumination_source_id`),
  KEY `target_homogeneity_id` (`target_homogeneity_id`),
  KEY `goniometer_id` (`goniometer_id`),
  KEY `FK_spectrum_20` (`quality_level_id`),
  KEY `FK_spectrum_21` (`required_quality_level_id`),
  KEY `spec_ref_brand_fk` (`reference_id`),
  CONSTRAINT `FK_spectrum_20` FOREIGN KEY (`quality_level_id`) REFERENCES `quality_level` (`quality_level_id`),
  CONSTRAINT `FK_spectrum_21` FOREIGN KEY (`required_quality_level_id`) REFERENCES `quality_level` (`quality_level_id`),
  CONSTRAINT `spectrum_ibfk_1` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`campaign_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `spectrum_ibfk_10` FOREIGN KEY (`measurement_type_id`) REFERENCES `measurement_type` (`measurement_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `spectrum_ibfk_11` FOREIGN KEY (`sampling_environment_id`) REFERENCES `sampling_environment` (`sampling_environment_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `spectrum_ibfk_12` FOREIGN KEY (`illumination_source_id`) REFERENCES `illumination_source` (`illumination_source_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `spectrum_ibfk_14` FOREIGN KEY (`target_homogeneity_id`) REFERENCES `target_homogeneity` (`target_homogeneity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `spectrum_ibfk_15` FOREIGN KEY (`goniometer_id`) REFERENCES `goniometer` (`goniometer_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `spectrum_ibfk_2` FOREIGN KEY (`file_format_id`) REFERENCES `file_format` (`file_format_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `spectrum_ibfk_3` FOREIGN KEY (`hierarchy_level_id`) REFERENCES `hierarchy_level` (`hierarchy_level_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `spectrum_ibfk_4` FOREIGN KEY (`sensor_id`) REFERENCES `sensor` (`sensor_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `spectrum_ibfk_5` FOREIGN KEY (`instrument_id`) REFERENCES `instrument` (`instrument_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `spectrum_ibfk_6` FOREIGN KEY (`landcover_id`) REFERENCES `landcover` (`landcover_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `spectrum_ibfk_9` FOREIGN KEY (`measurement_unit_id`) REFERENCES `measurement_unit` (`measurement_unit_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `spec_ref_brand_fk` FOREIGN KEY (`reference_id`) REFERENCES `reference` (`reference_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=FIXED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spectrum`
--

LOCK TABLES `spectrum` WRITE;
/*!40000 ALTER TABLE `spectrum` DISABLE KEYS */;
/*!40000 ALTER TABLE `spectrum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spectrum_datalink`
--

DROP TABLE IF EXISTS `spectrum_datalink`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `spectrum_datalink` (
  `spectrum_datalink_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `datalink_type_id` int(10) unsigned NOT NULL,
  `spectrum_id` int(11) NOT NULL,
  `linked_spectrum_id` int(11) NOT NULL,
  `campaign_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`spectrum_datalink_id`),
  KEY `FK_spectrum_datalink_1` (`spectrum_id`),
  KEY `FK_spectrum_datalink_2` (`datalink_type_id`),
  KEY `FK_spectrum_datalink_3` (`linked_spectrum_id`),
  KEY `spectrum_datalink_campaign_id` (`campaign_id`),
  CONSTRAINT `spectrum_datalink_campaign_id` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`campaign_id`),
  CONSTRAINT `FK_spectrum_datalink_1` FOREIGN KEY (`spectrum_id`) REFERENCES `spectrum` (`spectrum_id`),
  CONSTRAINT `FK_spectrum_datalink_2` FOREIGN KEY (`datalink_type_id`) REFERENCES `datalink_type` (`datalink_type_id`),
  CONSTRAINT `FK_spectrum_datalink_3` FOREIGN KEY (`linked_spectrum_id`) REFERENCES `spectrum` (`spectrum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=FIXED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spectrum_datalink`
--

LOCK TABLES `spectrum_datalink` WRITE;
/*!40000 ALTER TABLE `spectrum_datalink` DISABLE KEYS */;
/*!40000 ALTER TABLE `spectrum_datalink` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `specchio`.`spectrum_datalink_tr`
	BEFORE INSERT ON `specchio`.`spectrum_datalink`
	FOR EACH ROW SET new.`campaign_id` = (
		SELECT `campaign_id` FROM `specchio`.`spectrum` WHERE `spectrum`.`spectrum_id` = new.`spectrum_id`
	) */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Temporary table structure for view `spectrum_datalink_view`
--

DROP TABLE IF EXISTS `spectrum_datalink_view`;
/*!50001 DROP VIEW IF EXISTS `spectrum_datalink_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `spectrum_datalink_view` (
  `spectrum_datalink_id` int(10) unsigned,
  `datalink_type_id` int(10) unsigned,
  `spectrum_id` int(11),
  `linked_spectrum_id` int(11),
  `campaign_id` int(11)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `spectrum_view`
--

DROP TABLE IF EXISTS `spectrum_view`;
/*!50001 DROP VIEW IF EXISTS `spectrum_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `spectrum_view` (
  `spectrum_id` int(11),
  `goniometer_id` int(10) unsigned,
  `target_homogeneity_id` int(10) unsigned,
  `illumination_source_id` int(10) unsigned,
  `sampling_environment_id` int(10) unsigned,
  `measurement_type_id` int(10) unsigned,
  `measurement_unit_id` int(10) unsigned,
  `landcover_id` int(10) unsigned,
  `measurement` blob,
  `is_reference` tinyint(1),
  `hierarchy_level_id` int(11),
  `sensor_id` int(11),
  `file_format_id` int(11),
  `campaign_id` int(11),
  `instrument_id` int(10) unsigned,
  `required_quality_level_id` int(10) unsigned,
  `quality_level_id` int(10) unsigned,
  `reference_id` int(11)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `spectrum_x_eav`
--

DROP TABLE IF EXISTS `spectrum_x_eav`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `spectrum_x_eav` (
  `spectrum_id` int(11) NOT NULL,
  `eav_id` int(11) NOT NULL,
  `campaign_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`spectrum_id`,`eav_id`),
  KEY `spectrum_x_eav_fk1` (`spectrum_id`),
  KEY `spectrum_x_eav_fk2` (`eav_id`),
  KEY `FK_spectrum_x_eav_campaign_id` (`campaign_id`),
  CONSTRAINT `FK_spectrum_x_eav_campaign_id` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`campaign_id`),
  CONSTRAINT `spectrum_x_eav_fk1` FOREIGN KEY (`spectrum_id`) REFERENCES `spectrum` (`spectrum_id`) ON UPDATE NO ACTION,
  CONSTRAINT `spectrum_x_eav_fk2` FOREIGN KEY (`eav_id`) REFERENCES `eav` (`eav_id`) ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spectrum_x_eav`
--

LOCK TABLES `spectrum_x_eav` WRITE;
/*!40000 ALTER TABLE `spectrum_x_eav` DISABLE KEYS */;
/*!40000 ALTER TABLE `spectrum_x_eav` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `specchio`.`spectrum_x_eav_tr`
	BEFORE INSERT ON `specchio`.`spectrum_x_eav`
	FOR EACH ROW SET new.`campaign_id` = (
		SELECT `campaign_id` FROM `specchio`.`spectrum` WHERE `spectrum`.`spectrum_id` = new.`spectrum_id`
	) */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Temporary table structure for view `spectrum_x_eav_view`
--

DROP TABLE IF EXISTS `spectrum_x_eav_view`;
/*!50001 DROP VIEW IF EXISTS `spectrum_x_eav_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `spectrum_x_eav_view` (
  `spectrum_id` int(11),
  `eav_id` int(11),
  `campaign_id` int(11)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `target_category`
--

DROP TABLE IF EXISTS `target_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `target_category` (
  `target_category_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`target_category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `target_category`
--

LOCK TABLES `target_category` WRITE;
/*!40000 ALTER TABLE `target_category` DISABLE KEYS */;
INSERT INTO `target_category` VALUES (1,'Natural',NULL),(2,'Artificial',NULL);
/*!40000 ALTER TABLE `target_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `target_homogeneity`
--

DROP TABLE IF EXISTS `target_homogeneity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `target_homogeneity` (
  `target_homogeneity_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `homogeneity` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`target_homogeneity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `target_homogeneity`
--

LOCK TABLES `target_homogeneity` WRITE;
/*!40000 ALTER TABLE `target_homogeneity` DISABLE KEYS */;
INSERT INTO `target_homogeneity` VALUES (1,'homogenous'),(2,'mixed');
/*!40000 ALTER TABLE `target_homogeneity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taxonomy`
--

DROP TABLE IF EXISTS `taxonomy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `taxonomy` (
  `taxonomy_id` int(10) NOT NULL AUTO_INCREMENT,
  `parent_id` int(10) DEFAULT NULL,
  `attribute_id` int(10) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`taxonomy_id`),
  KEY `parent_fk` (`parent_id`),
  KEY `taxonomy_attribute_fk` (`attribute_id`),
  CONSTRAINT `parent_fk` FOREIGN KEY (`parent_id`) REFERENCES `taxonomy` (`taxonomy_id`) ON UPDATE NO ACTION,
  CONSTRAINT `taxonomy_attribute_fk` FOREIGN KEY (`attribute_id`) REFERENCES `attribute` (`attribute_id`) ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taxonomy`
--

LOCK TABLES `taxonomy` WRITE;
/*!40000 ALTER TABLE `taxonomy` DISABLE KEYS */;
INSERT INTO `taxonomy` VALUES (1,NULL,36,'Dominant','D','Trees with well developed crowns extending above the general level of the forest canopy. The crown receives full sunlight from above and partly from the sides.'),(2,NULL,36,'Co-dominant','C','Trees with medium-sized crowns forming the general level of the forest canopy. Each tree crown receives full sunlight from above but very little from the sides.'),(3,NULL,36,'Intermediate','I','Trees shorter than dominant and co-dominant trees and have small crowns extending into the forest canopy. Each tree receives a little direct light from holes in the canopy and very little light from the sides.'),(4,NULL,36,'Suppressed','S','Trees with crowns more or less entirely below the forest canopy and receiving very little direct light either from above or from the sides.'),(5,NULL,36,'Emergent','E','Trees with crowns totally above the canopy of the stand and receiving full sunlight from both above and from all sides.'),(6,NULL,36,'Open grown','OG','Trees not growing near any other tree and with crowns receiving full sunlight from both above and from all sides.'),(7,NULL,67,'ASD Integrating Sphere (2003 or newer)','','Analytical Spectral Devices integrating sphere, as produced after 2003'),(8,NULL,67,'ASD Integrating Sphere (before 2003)','','Analytical Spectral Devices integrating sphere, as produced before 2003'),(9,NULL,67,'LI-COR 1800-12','','');
/*!40000 ALTER TABLE `taxonomy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unit`
--

DROP TABLE IF EXISTS `unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unit` (
  `unit_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `short_name` varchar(45) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`unit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unit`
--

LOCK TABLES `unit` WRITE;
/*!40000 ALTER TABLE `unit` DISABLE KEYS */;
INSERT INTO `unit` VALUES (1,NULL,NULL,'cm2/g',NULL),(2,NULL,NULL,'ugrams/cm2',NULL),(3,NULL,NULL,'cm2',NULL),(4,NULL,NULL,'g/cm2',NULL),(5,NULL,NULL,'RAW',NULL),(6,'ms','Millisecond','ms',NULL),(7,'Degrees','Degree','Degrees',NULL),(8,'String','String','String',NULL);
/*!40000 ALTER TABLE `unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `campaign_view`
--

/*!50001 DROP TABLE IF EXISTS `campaign_view`*/;
/*!50001 DROP VIEW IF EXISTS `campaign_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `campaign_view` AS select `campaign`.`campaign_id` AS `campaign_id`,`campaign`.`name` AS `name`,`campaign`.`description` AS `description`,`campaign`.`path` AS `path`,`campaign`.`quality_comply` AS `quality_comply`,`campaign`.`user_id` AS `user_id`,`campaign`.`research_group_id` AS `research_group_id` from `campaign` where `campaign`.`research_group_id` in (select `research_group_members`.`research_group_id` from (`research_group_members` join `specchio_user`) where ((`research_group_members`.`member_id` = `specchio_user`.`user_id`) and (`specchio_user`.`user` = substring_index((select user()),'@',1)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `eav_view`
--

/*!50001 DROP TABLE IF EXISTS `eav_view`*/;
/*!50001 DROP VIEW IF EXISTS `eav_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `eav_view` AS select `eav`.`eav_id` AS `eav_id`,`eav`.`attribute_id` AS `attribute_id`,`eav`.`int_val` AS `int_val`,`eav`.`double_val` AS `double_val`,`eav`.`string_val` AS `string_val`,`eav`.`binary_val` AS `binary_val`,`eav`.`datetime_val` AS `datetime_val`,`eav`.`unit_id` AS `unit_id`,`eav`.`campaign_id` AS `campaign_id`,`eav`.`taxonomy_id` AS `taxonomy_id` from `eav` where `eav`.`campaign_id` in (select `campaign`.`campaign_id` from ((`campaign` join `research_group_members`) join `specchio_user`) where ((`campaign`.`research_group_id` = `research_group_members`.`research_group_id`) and (`research_group_members`.`member_id` = `specchio_user`.`user_id`) and (`specchio_user`.`user` = substring_index((select user()),'@',1)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `hierarchy_level_view`
--

/*!50001 DROP TABLE IF EXISTS `hierarchy_level_view`*/;
/*!50001 DROP VIEW IF EXISTS `hierarchy_level_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `hierarchy_level_view` AS select `hierarchy_level`.`hierarchy_level_id` AS `hierarchy_level_id`,`hierarchy_level`.`name` AS `name`,`hierarchy_level`.`description` AS `description`,`hierarchy_level`.`parent_level_id` AS `parent_level_id`,`hierarchy_level`.`campaign_id` AS `campaign_id` from `hierarchy_level` where `hierarchy_level`.`campaign_id` in (select `campaign`.`campaign_id` from ((`campaign` join `research_group_members`) join `specchio_user`) where ((`campaign`.`research_group_id` = `research_group_members`.`research_group_id`) and (`research_group_members`.`member_id` = `specchio_user`.`user_id`) and (`specchio_user`.`user` = substring_index((select user()),'@',1)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `hierarchy_level_x_spectrum_view`
--

/*!50001 DROP TABLE IF EXISTS `hierarchy_level_x_spectrum_view`*/;
/*!50001 DROP VIEW IF EXISTS `hierarchy_level_x_spectrum_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `hierarchy_level_x_spectrum_view` AS select `hierarchy_level_x_spectrum`.`hierarchy_level_id` AS `hierarchy_level_id`,`hierarchy_level_x_spectrum`.`spectrum_id` AS `spectrum_id`,`hierarchy_level_x_spectrum`.`campaign_id` AS `campaign_id` from `hierarchy_level_x_spectrum` where `hierarchy_level_x_spectrum`.`campaign_id` in (select `campaign`.`campaign_id` from ((`campaign` join `research_group_members`) join `specchio_user`) where ((`campaign`.`research_group_id` = `research_group_members`.`research_group_id`) and (`research_group_members`.`member_id` = `specchio_user`.`user_id`) and (`specchio_user`.`user` = substring_index((select user()),'@',1)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `research_group_members_view`
--

/*!50001 DROP TABLE IF EXISTS `research_group_members_view`*/;
/*!50001 DROP VIEW IF EXISTS `research_group_members_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `research_group_members_view` AS select `research_group_members`.`research_group_id` AS `research_group_id`,`research_group_members`.`member_id` AS `member_id` from `research_group_members` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `research_group_view`
--

/*!50001 DROP TABLE IF EXISTS `research_group_view`*/;
/*!50001 DROP VIEW IF EXISTS `research_group_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `research_group_view` AS select `research_group`.`research_group_id` AS `research_group_id`,`research_group`.`name` AS `name` from ((`research_group` join `research_group_members`) join `specchio_user`) where ((`research_group`.`research_group_id` = `research_group_members`.`research_group_id`) and (`research_group_members`.`member_id` = `specchio_user`.`user_id`) and (`specchio_user`.`user` = substring_index((select user()),'@',1))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `spectrum_datalink_view`
--

/*!50001 DROP TABLE IF EXISTS `spectrum_datalink_view`*/;
/*!50001 DROP VIEW IF EXISTS `spectrum_datalink_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `spectrum_datalink_view` AS select `spectrum_datalink`.`spectrum_datalink_id` AS `spectrum_datalink_id`,`spectrum_datalink`.`datalink_type_id` AS `datalink_type_id`,`spectrum_datalink`.`spectrum_id` AS `spectrum_id`,`spectrum_datalink`.`linked_spectrum_id` AS `linked_spectrum_id`,`spectrum_datalink`.`campaign_id` AS `campaign_id` from `spectrum_datalink` where `spectrum_datalink`.`campaign_id` in (select `campaign`.`campaign_id` from ((`campaign` join `research_group_members`) join `specchio_user`) where ((`campaign`.`research_group_id` = `research_group_members`.`research_group_id`) and (`research_group_members`.`member_id` = `specchio_user`.`user_id`) and (`specchio_user`.`user` = substring_index((select user()),'@',1)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `spectrum_view`
--

/*!50001 DROP TABLE IF EXISTS `spectrum_view`*/;
/*!50001 DROP VIEW IF EXISTS `spectrum_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `spectrum_view` AS select `spectrum`.`spectrum_id` AS `spectrum_id`,`spectrum`.`goniometer_id` AS `goniometer_id`,`spectrum`.`target_homogeneity_id` AS `target_homogeneity_id`,`spectrum`.`illumination_source_id` AS `illumination_source_id`,`spectrum`.`sampling_environment_id` AS `sampling_environment_id`,`spectrum`.`measurement_type_id` AS `measurement_type_id`,`spectrum`.`measurement_unit_id` AS `measurement_unit_id`,`spectrum`.`landcover_id` AS `landcover_id`,`spectrum`.`measurement` AS `measurement`,`spectrum`.`is_reference` AS `is_reference`,`spectrum`.`hierarchy_level_id` AS `hierarchy_level_id`,`spectrum`.`sensor_id` AS `sensor_id`,`spectrum`.`file_format_id` AS `file_format_id`,`spectrum`.`campaign_id` AS `campaign_id`,`spectrum`.`instrument_id` AS `instrument_id`,`spectrum`.`required_quality_level_id` AS `required_quality_level_id`,`spectrum`.`quality_level_id` AS `quality_level_id`,`spectrum`.`reference_id` AS `reference_id` from `spectrum` where `spectrum`.`campaign_id` in (select `campaign`.`campaign_id` from ((`campaign` join `research_group_members`) join `specchio_user`) where ((`campaign`.`research_group_id` = `research_group_members`.`research_group_id`) and (`research_group_members`.`member_id` = `specchio_user`.`user_id`) and (`specchio_user`.`user` = substring_index((select user()),'@',1)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `spectrum_x_eav_view`
--

/*!50001 DROP TABLE IF EXISTS `spectrum_x_eav_view`*/;
/*!50001 DROP VIEW IF EXISTS `spectrum_x_eav_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `spectrum_x_eav_view` AS select `spectrum_x_eav`.`spectrum_id` AS `spectrum_id`,`spectrum_x_eav`.`eav_id` AS `eav_id`,`spectrum_x_eav`.`campaign_id` AS `campaign_id` from `spectrum_x_eav` where `spectrum_x_eav`.`campaign_id` in (select `campaign`.`campaign_id` from ((`campaign` join `research_group_members`) join `specchio_user`) where ((`campaign`.`research_group_id` = `research_group_members`.`research_group_id`) and (`research_group_members`.`member_id` = `specchio_user`.`user_id`) and (`specchio_user`.`user` = substring_index((select user()),'@',1)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-03-12 11:45:26
CREATE DATABASE  IF NOT EXISTS `specchio_temp` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `specchio_temp`;
-- MySQL dump 10.13  Distrib 5.5.24, for osx10.5 (i386)
--
-- Host: localhost    Database: specchio_temp
-- ------------------------------------------------------
-- Server version	5.5.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-03-12 11:45:26
