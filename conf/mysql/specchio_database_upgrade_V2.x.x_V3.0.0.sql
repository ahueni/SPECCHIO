-- database for temporary tables
CREATE DATABASE `specchio_temp`;

-- access control
ALTER TABLE `specchio`.`specchio_user` ADD COLUMN `password` VARCHAR(100);
ALTER TABLE `specchio`.`specchio_user` ADD COLUMN `external_id` VARCHAR(255);
ALTER TABLE `specchio`.`specchio_user` ADD COLUMN signup_date datetime DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE `specchio`.`specchio_user` ADD COLUMN `description` VARCHAR(250);
CREATE TABLE `specchio`.`specchio_group`(
	`group_id` INT(10) NOT NULL PRIMARY KEY,
	`group_name` CHAR(16)
);
INSERT INTO `specchio`.`specchio_group` VALUES(1, 'admin');
INSERT INTO `specchio`.`specchio_group` VALUES(2, 'user');
INSERT INTO `specchio`.`specchio_group` VALUES(99, 'anonymous');
CREATE TABLE `specchio`.`specchio_user_group` (
	`user` CHAR(16),
	`group_name` CHAR(16)
);
INSERT INTO `specchio`.`specchio_user_group` VALUES('sdb_admin', 'admin');

-- change the password of sdb_admin in the two following lines
--UPDATE `specchio`.`specchio_user` SET PASSWORD=MD5('xXY7!fe@') where `user`='sdb_admin';
--UPDATE mysql.user SET Password=PASSWORD('xXY7!fe@') WHERE User='sdb_admin';

--GRANT SUPER ON *.* TO 'sdb_admin'@'localhost';
--GRANT CREATE USER ON *.* TO 'sdb_admin'@'localhost';
--FLUSH PRIVILEGES;


-- finer calibration date control
ALTER TABLE `specchio`.`calibration` MODIFY COLUMN `calibration_date` datetime DEFAULT NULL;

-- new file support
INSERT INTO `specchio`.`file_format` (`name`, `file_extension`, `description`) VALUES ('XLS', 'xls', 'Excel XLS file');


-- PP systems support
INSERT INTO file_format (name, file_extension, description) VALUES ('UniSpec', 'SPT', 'UniSpec Data File');
INSERT INTO manufacturer (name, www, short_name) VALUES ('PP Systems', 'www.ppsystems.com', 'PP Systems');
INSERT INTO file_format (name, file_extension, description) VALUES ('UniSpec_SPU', 'SPU', 'UniSpec Data File');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('system', 'instrument software');



-- Taxonomy Support
CREATE TABLE  IF NOT EXISTS `specchio`.`taxonomy`(
`taxonomy_id` INT(10) NOT NULL AUTO_INCREMENT,
`parent_id` INT(10),
`attribute_id` INT(10),
`name` VARCHAR(100),
`code` VARCHAR(50),
`description` VARCHAR(500),
PRIMARY KEY(`taxonomy_id`),
  CONSTRAINT `parent_fk`
    FOREIGN KEY (`parent_id`)
    REFERENCES `taxonomy` (`taxonomy_id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `taxonomy_attribute_fk`
    FOREIGN KEY (`attribute_id`)
    REFERENCES `attribute` (`attribute_id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB 
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `specchio`.`eav` ADD COLUMN `taxonomy_id` INTEGER NULL DEFAULT NULL;
ALTER TABLE `specchio`.`eav` ADD CONSTRAINT `taxonomy_id_fk` FOREIGN KEY `taxonomy_id_fk` (`taxonomy_id`) REFERENCES `taxonomy` (`taxonomy_id`);

ALTER TABLE `specchio`.`attribute` MODIFY COLUMN `description` varchar(200) DEFAULT NULL;
ALTER TABLE `specchio`.`unit` MODIFY COLUMN `description` varchar(200) DEFAULT NULL;

-- new categories
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Location', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Optics', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('General', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Environmental Conditions', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Instrument Settings', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Sampling Geometry', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Instrument', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Names', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Campaign Details', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Instrumentation', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Keywords', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Vegetation Biophysical Variables', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Pictures', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('PDFs', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Scientific References', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Data Portal', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Generic Target Properties', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Processing', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Personnel', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Illumination', '');
INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Soil Parameters', '');



-- units
INSERT INTO `specchio`.`unit`(`short_name`) VALUES ('cm2/g');
INSERT INTO `specchio`.`unit`(`short_name`) VALUES ('ugrams/cm2');
INSERT INTO `specchio`.`unit`(`short_name`) VALUES ('cm2');
INSERT INTO `specchio`.`unit`(`short_name`) VALUES ('g/cm2');
INSERT INTO `specchio`.`unit`(`short_name`) SELECT * FROM (SELECT 'RAW') AS tmp
WHERE NOT EXISTS (
    SELECT short_name FROM `specchio`.`unit` WHERE short_name = 'RAW'
) LIMIT 1;
INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`) values('ms', 'Millisecond', 'ms');
INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`) values('Degrees', 'Angular value in degrees', 'Degrees');
INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`) values('String', 'String', 'String');
INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`) values('pH', 'Figure expressing the acidity or alkalinity of a solution on a logarithmic scale on which 7 is neutral.', 'pH');
INSERT INTO `specchio`.`unit`(`short_name`) VALUES ('cmol/kg');
INSERT INTO `specchio`.`unit`(`short_name`) VALUES ('cm2/cm2');
INSERT INTO `specchio`.`unit`(`short_name`) VALUES ('mg/kg');
INSERT INTO `specchio`.`unit`(`short_name`) VALUES ('g/g');
INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`) values('Deci Siemens / Metre', null, 'dS/m');
INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`) values('Metres', null, 'm');
INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`) SELECT * FROM (SELECT 'Degrees Celcius', 'Temperature unit in degrees Celcius', '�C') AS tmp
WHERE NOT EXISTS (
    SELECT name FROM `specchio`.`unit` WHERE name = 'Degrees Celcius'
) LIMIT 1;
INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`)  SELECT * FROM (SELECT 'Hectopascal', 'Pressure unit in Hectopascal', 'hPa') AS tmp
WHERE NOT EXISTS (
    SELECT name FROM `specchio`.`unit` WHERE name = 'Hectopascal'
) LIMIT 1;
INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`) SELECT * FROM (SELECT 'Metres / second', 'Velocity', 'm/s') AS tmp
WHERE NOT EXISTS (
    SELECT name FROM `specchio`.`unit` WHERE name = 'Metres / second'
) LIMIT 1;



update specchio.eav set unit_id = (select unit_id from unit where name = 'String') where unit_id = (select unit_id from unit where name is null and short_name = 'String');
delete from specchio.unit where unit_id = (select unit_id from unit where name is null and short_name = 'String');



--INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`) values('Percent', null, '%'); -- apparently already exists in V2.2 schema ...




-- ---------------
-- new attributes
-- ---------------

-- cardinality
ALTER TABLE `specchio`.`attribute` ADD COLUMN `cardinality` INT(10) DEFAULT 1;

-- environmental conditions from `specchio`.`category` 
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Ambient Temperature', (select category_id from `specchio`.`category` where name = 'Environmental Conditions'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = '�C'));
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Air Pressure', (select category_id from `specchio`.`category` where name = 'Environmental Conditions'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'hPa'));
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Wind Speed', (select category_id from `specchio`.`category` where name = 'Environmental Conditions'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'm/s'));
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) VALUES ('Wind Direction', (select category_id from `specchio`.`category` where name = 'Environmental Conditions'), 'double_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) VALUES ('Relative Humidity', (select category_id from `specchio`.`category` where name = 'Environmental Conditions'), 'double_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) VALUES ('Cloud Cover', (select category_id from `specchio`.`category` where name = 'Environmental Conditions'), 'double_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) VALUES ('Weather Conditions', (select category_id from `specchio`.`category` where name = 'Environmental Conditions'), 'string_val', 'Textual description of weather conditions');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Sampling Environment', (select category_id from `specchio`.category where name = 'Environmental Conditions'), 'taxonomy_id');


-- instrument settings
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) VALUES ('Automatic Dark Current Correction', (select category_id from `specchio`.category where name = 'Instrument Settings'), 'string_val', 'Dark current has been compensated for by the instrument (ON/OFF)');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Integration Time', (select category_id from `specchio`.category where name = 'Instrument Settings'), 'int_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Number of internal Scans', (select category_id from `specchio`.category where name = 'Instrument Settings'), 'int_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Gain_SWIR1', (select category_id from `specchio`.category where name = 'Instrument Settings'), 'int_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Gain_SWIR2', (select category_id from `specchio`.category where name = 'Instrument Settings'), 'int_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Offset_SWIR1', (select category_id from `specchio`.category where name = 'Instrument Settings'), 'int_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Offset_SWIR2', (select category_id from `specchio`.category where name = 'Instrument Settings'), 'int_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Capturing Software Name', (select category_id from `specchio`.category where name = 'Instrument Settings'), 'string_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Capturing Software Version', (select category_id from `specchio`.category where name = 'Instrument Settings'), 'string_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('UniSpec Spectral Resampling', (select category_id from `specchio`.category where name = 'Instrument Settings'), 'string_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) values('Instrument Channel', (select category_id from `specchio`.category where name = 'Instrument Settings'), 'string_val', 'Channel designation for multi-channel instruments, e.g. irradiance and reflected radiance channels');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) values('Time since last DC', (select category_id from `specchio`.category where name = 'Instrument Settings'), 'int_val', 'Time since last dark current measurement');




-- General
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) VALUES ('File Version', (select category_id from `specchio`.category where name = 'General'), 'string_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Spectrum Number', (select category_id from `specchio`.category where name = 'General'), 'int_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('File Name', (select category_id from `specchio`.category where name = 'General'), 'string_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Acquisition Time', (select category_id from `specchio`.category where name = 'General'), 'datetime_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Loading Time', (select category_id from `specchio`.category where name = 'General'), 'datetime_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('File Comments', (select category_id from `specchio`.category where name = 'General'), 'string_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) values('Raw Data Format', (select category_id from `specchio`.category where name = 'General'), 'string_val', 'Information on where data originated from before being stored in the file loaded to SPECCHIO.');


-- Vegetation Parameters
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Chlorophyll Content', (select category_id from `specchio`.`category` where name = 'Vegetation Biophysical Variables'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'ugrams/cm2'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `description`, `default_unit_id`) VALUES ('Specific Leaf Area', (select category_id from `specchio`.`category` where name = 'Vegetation Biophysical Variables'), 'double_val', 'Calculated by: LeafArea[cm2]/DryMass[g]', (select unit_id from `specchio`.`unit` where short_name = 'cm2/g'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`,`description`, `default_unit_id`) VALUES ('DBH', (select category_id from `specchio`.`category` where name = 'Vegetation Biophysical Variables'), 'double_val', 'Diameter at breast height', (select unit_id from `specchio`.`unit` where short_name = 'm'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`,`description`, `default_unit_id`) VALUES ('Height', (select category_id from `specchio`.`category` where name = 'Vegetation Biophysical Variables'), 'double_val', 'Height of vegetation', (select unit_id from `specchio`.`unit` where short_name = 'm'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`,`description`, `default_unit_id`) VALUES ('Approx. Crown Diameter', (select category_id from `specchio`.`category` where name = 'Vegetation Biophysical Variables'), 'double_val', '', (select unit_id from `specchio`.`unit` where short_name = 'm'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`,`description`, `default_unit_id`) VALUES ('% Crown Cover', (select category_id from `specchio`.`category` where name = 'Vegetation Biophysical Variables'), 'double_val', 'Crown Cover Percentage', (select unit_id from `specchio`.`unit` where short_name = '%'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`,`description`, `default_unit_id`) VALUES ('Wet Weight', (select category_id from `specchio`.`category` where name = 'Vegetation Biophysical Variables'), 'double_val', '', (select unit_id from `specchio`.`unit` where short_name = 'g'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`,`description`, `default_unit_id`) VALUES ('Dry Weight', (select category_id from `specchio`.`category` where name = 'Vegetation Biophysical Variables'), 'double_val', '', (select unit_id from `specchio`.`unit` where short_name = 'g'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `description`, `default_unit_id`) VALUES ('Leaf Area', (select category_id from `specchio`.`category` where name = 'Vegetation Biophysical Variables'), 'double_val', '', (select unit_id from `specchio`.`unit` where short_name = 'cm2'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `description`, `default_unit_id`) VALUES ('Water Content', (select category_id from `specchio`.`category` where name = 'Vegetation Biophysical Variables'), 'double_val', '', (select unit_id from `specchio`.`unit` where short_name = 'g/cm2'));
INSERT INTO `specchio`.`attribute` (`name`, `description`, `category_id`, `default_storage_field`) VALUES ('Crown Class (FPMRIS)', 'SOP 13 Measuring a Large Tree Plot', (select `category_id` from `specchio`.`category` where name = 'Vegetation Biophysical Variables'), 'taxonomy_id');


-- Soil Parameters
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Horizon Name', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Horizon Number', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'int_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Sampling Upper Depth', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Sampling Lower Depth', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Carbon Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Carbon', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = '%'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('pH Water Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('pH Water', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'pH'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('pH CaCl2 Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('pH CaCl2', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'pH'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Clay Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Clay', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = '%'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Silt Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Silt', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = '%'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Fine Sand Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Fine Sand', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = '%'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Coarse Sand Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Coarse Sand', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = '%'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Bulk Density Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Bulk Density', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'g/cm2'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Geothite Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Geothite', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Gibsite Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Gibsite', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Hematite Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Hematite', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Illite Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Illite', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Kaolinite Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Kaolinite', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Montmorillonite Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Montmorillonite', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Smectite Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Smectite', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('CEC Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('CEC', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'cmol/kg'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Exchangeable Ca Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Exchangeable Ca', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'cmol/kg'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Exchangeable K Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Exchangeable K', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'cmol/kg'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Exchangeable Mg Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Exchangeable Mg', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'cmol/kg'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Exchangeable Na Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Exchangeable Na', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'cmol/kg'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Exchangeable Acidity Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Exchangeable Acidity', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'cmol/kg'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Extractable Fe Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Extractable Fe', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Total K Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Total K', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Total N Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Total N', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Total P Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Total P', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Available P Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Available P', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Water Content 0_1 Bar Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Water Content 0_1 Bar', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'cm2/cm2'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Water Holding Capacity 15 Bar Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Water Holding Capacity 15 Bar', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Air Dry Water Content Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Air Dry Water Content', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'g/g'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Electrical Conductivity Method', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('Electrical Conductivity', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'dS/m'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('ASC Order', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('ASC Sub-Order', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Horizon Desig. Master', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Horizon Desig. Sub. Division', (select category_id from `specchio`.`category` where name = 'Soil Parameters'), 'string_val');


-- Scientific References
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `description`, `cardinality`) VALUES ('Publication', (select category_id from `specchio`.`category` where name = 'Scientific References'), 'string_val', 'Publication relevant to these spectral data', NULL);
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `description`, `cardinality`) VALUES ('Citation', (select category_id from `specchio`.`category` where name = 'Scientific References'), 'string_val', 'Publication to be cited when using these spectral data', NULL);


-- Generic Target Properties
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Target ID', (select category_id from `specchio`.`category` where name = 'Generic Target Properties'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Target Description', (select category_id from `specchio`.`category` where name = 'Generic Target Properties'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Site ID', (select category_id from `specchio`.`category` where name = 'Generic Target Properties'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Sample Number', (select category_id from `specchio`.`category` where name = 'Generic Target Properties'), 'string_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `description`) VALUES ('Sample Collection Date', (select category_id from `specchio`.`category` where name = 'Generic Target Properties'), 'datetime_val', 'Date when the original sample was collected in the field.');


-- Pictures
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `cardinality`) VALUES ('Target Picture', (select category_id from `specchio`.`category` where name = 'Pictures'), 'binary_val', NULL);
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `cardinality`) VALUES ('Sampling Setup Picture', (select category_id from `specchio`.`category` where name = 'Pictures'), 'binary_val', NULL);
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `cardinality`) VALUES ('Sky Picture', (select category_id from `specchio`.`category` where name = 'Pictures'), 'binary_val', NULL);
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `cardinality`, `description`) VALUES ('Sampling Environment Picture', (select category_id from `specchio`.`category` where name = 'Pictures'), 'binary_val', NULL, 'Picture showing the general sampling environment, i.e. vicinity of the target');


-- PDFs
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Field Protocol', (select category_id from `specchio`.category where name = 'PDFs'), 'binary_val');
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`) VALUES ('Experimental Design', (select category_id from `specchio`.category where name = 'PDFs'), 'binary_val');

-- Location
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `default_unit_id`) values('Longitude', (select category_id from `specchio`.category where name = 'Location'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'Degrees'));
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `default_unit_id`) values('Latitude', (select category_id from `specchio`.category where name = 'Location'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'Degrees'));
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `default_unit_id`) values('Altitude', (select category_id from `specchio`.category where name = 'Location'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'm'));
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Location Name', (select category_id from `specchio`.category where name = 'Location'), 'string_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('State', (select category_id from `specchio`.category where name = 'Location'), 'string_val');


-- Optics
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('FOV', (select category_id from `specchio`.category where name = 'Optics'), 'int_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Optics Name', (select category_id from `specchio`.category where name = 'Optics'), 'string_val');


-- Processing
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Processing Level', (select category_id from `specchio`.category where name = 'Processing'), 'double_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) values('DC Flag', (select category_id from `specchio`.category where name = 'Processing'), 'int_val', 'Designates this spectrum as dark current spectrum');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`, `cardinality`) values('Processing Module', (select category_id from `specchio`.category where name = 'Processing'), 'string_val', 'Name of processing module applied to spectrum', NULL);
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`, `cardinality`) values('Processing Algorithm', (select category_id from `specchio`.category where name = 'Processing'), 'string_val', 'Description of processing algorithm applied to spectrum', NULL);
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`, `cardinality`) values('Source File', (select category_id from `specchio`.category where name = 'Processing'), 'string_val', 'File that provided the original data (applies if data were processed outside of SPECCHIO)', NULL);
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`, `cardinality`) values('Data Ingestion Notes', (select category_id from `specchio`.category where name = 'Processing'), 'string_val', 'Notes produced by the data ingestion module during data loading into SPECCHIO', NULL);
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) values('Garbage Flag', (select category_id from `specchio`.category where name = 'Processing'), 'int_val', 'Designates this spectrum is garbage. This flag can be used to automatically exclude garbage spectra from processing.');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`, `cardinality`) values('Time Shift', (select category_id from `specchio`.category where name = 'Processing'), 'string_val', 'Notes produced by the SPECCHIO time shift routine', NULL);




-- Instrument
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Calibration Number', (select category_id from `specchio`.category where name = 'Instrument'), 'int_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Extended Instrument Name', (select category_id from `specchio`.category where name = 'Instrument'), 'string_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Instrument Serial Number', (select category_id from `specchio`.category where name = 'Instrument'), 'string_val');


-- Campaign Details
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) values('Campaign Name', (select category_id from `specchio`.category where name = 'Campaign Details'), 'string_val', 'Further specification of a particular campaign. Mainly used where a SPECCHIO campaign comprises several original sampling campaigns');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) values('Agency Code', (select category_id from `specchio`.category where name = 'Campaign Details'), 'string_val', '');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) values('Project ID', (select category_id from `specchio`.category where name = 'Campaign Details'), 'string_val', '');


-- Instrumentation
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) values('Azimuth Sensor Type', (select category_id from `specchio`.category where name = 'Instrumentation'), 'string_val', '');
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('Integrating Sphere', '', (select category_id from `specchio`.`category` where name = 'Instrumentation'), 'taxonomy_id');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) values('Light Source Parameters', (select category_id from `specchio`.category where name = 'Instrumentation'), 'string_val', 'Settings of artificial light source');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) values('White Reference Panel Name', (select category_id from `specchio`.category where name = 'Instrumentation'), 'string_val', 'Description of white reference panel name');
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('Contact Probe', '', (select category_id from `specchio`.`category` where name = 'Instrumentation'), 'taxonomy_id');




-- crown class taxonomy example
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Crown Class (FPMRIS)'), 'Dominant', 'D', 'Trees with well developed crowns extending above the general level of the forest canopy. The crown receives full sunlight from above and partly from the sides.');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Crown Class (FPMRIS)'), 'Co-dominant', 'C', 'Trees with medium-sized crowns forming the general level of the forest canopy. Each tree crown receives full sunlight from above but very little from the sides.');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Crown Class (FPMRIS)'), 'Intermediate', 'I', 'Trees shorter than dominant and co-dominant trees and have small crowns extending into the forest canopy. Each tree receives a little direct light from holes in the canopy and very little light from the sides.');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Crown Class (FPMRIS)'), 'Suppressed', 'S', 'Trees with crowns more or less entirely below the forest canopy and receiving very little direct light either from above or from the sides.');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Crown Class (FPMRIS)'), 'Emergent', 'E', 'Trees with crowns totally above the canopy of the stand and receiving full sunlight from both above and from all sides.');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Crown Class (FPMRIS)'), 'Open grown', 'OG', 'Trees not growing near any other tree and with crowns receiving full sunlight from both above and from all sides.');


-- integrating spheres
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Integrating Sphere'), 'ASD Integrating Sphere (2003 or newer)', '', 'Analytical Spectral Devices integrating sphere, as produced after 2003');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Integrating Sphere'), 'ASD Integrating Sphere (before 2003)', '', 'Analytical Spectral Devices integrating sphere, as produced before 2003');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Integrating Sphere'), 'LI-COR 1800-12', '', '');


-- Contact Probes
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Contact Probe'), 'ASD Contact Probe (High Intensity Contact Probe)', '', 'Analytical Spectral Devices contact probe (High Intensity Contact Probe)');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Contact Probe'), 'ASD Contact Probe (Plant Probe)', '', 'Analytical Spectral Devices contact probe (Plant Probe)');

-- Sampling Environments
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Sampling Environment'), 'Field', '', 'Data acquisition under field conditions');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Sampling Environment'), 'Laboratory', '', 'Data acquisition under laboratory conditions');



-- Keywords
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `cardinality`) values('Keyword', (select category_id from `specchio`.category where name = 'Keywords'), 'string_val', NULL);


-- Names
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Common', (select category_id from `specchio`.category where name = 'Names'), 'string_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Latin', (select category_id from `specchio`.category where name = 'Names'), 'string_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) values('ENVI Hdr', (select category_id from `specchio`.category where name = 'Names'), 'string_val', 'Name extracted from ENVI header');


-- Sampling Geometry
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Illumination Azimuth', (select category_id from `specchio`.category where name = 'Sampling Geometry'), 'double_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Illumination Zenith', (select category_id from `specchio`.category where name = 'Sampling Geometry'), 'double_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `default_unit_id`) values('Illumination Distance', (select category_id from `specchio`.category where name = 'Sampling Geometry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'm'));
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Sensor Azimuth', (select category_id from `specchio`.category where name = 'Sampling Geometry'), 'double_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`) values('Sensor Zenith', (select category_id from `specchio`.category where name = 'Sampling Geometry'), 'double_val');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `default_unit_id`) values('Sensor Distance', (select category_id from `specchio`.category where name = 'Sampling Geometry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'm'));

-- Personnel
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`, `cardinality`) values('Investigator', (select category_id from `specchio`.category where name = 'Personnel'), 'string_val', 'Investigator of these data; fallback if not definable via existing SPECCHIO users', NULL);


-- Illumination
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) values('Polarization', (select category_id from `specchio`.category where name = 'Illumination'), 'string_val', 'Polarisation `description`, e.g. Horizontal or Vertical');
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`, `default_unit_id`) values('Polarization Direction', (select category_id from `specchio`.category where name = 'Illumination'), 'double_val', 'Polarisation direction as degrees', (select unit_id from `specchio`.`unit` where short_name = '%'));


-- goniometers

INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('Goniometer', '', (select category_id from `specchio`.`category` where name = 'Instrumentation'), 'taxonomy_id');

INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Goniometer'), 'LAGOS', 'LAGOS', 'RLSs Laboratory Goniometer System');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Goniometer'), 'FIGOS', 'FIGOS', 'RLSs Field Goniometer System');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Goniometer'), 'FIGIFIGO', 'FIGIFIGO', 'FGIs Field Goniometer System');



-- illumination sources

INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('Illumination Sources', '', (select category_id from `specchio`.`category` where name = 'Instrumentation'), 'taxonomy_id');

INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Illumination Sources'), 'Sun', '', 'Natural irradiance provided by the sun');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Illumination Sources'), 'Oriel 1000 W QTH', '', '');


-- measurement types -> now 'Beam Geometry'


INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('Beam Geometry', '', (select category_id from `specchio`.`category` where name = 'Sampling Geometry'), 'taxonomy_id');

INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Beam Geometry'), 'Bidirectional (CASE 1)', '1', '');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Beam Geometry'), 'Directional-conical (CASE 2)', '2', '');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Beam Geometry'), 'Directional-hemispherical (CASE 3)', '3', '');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Beam Geometry'), 'Conical-directional (CASE 4)', '4', '');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Beam Geometry'), 'Biconical (CASE 5)', '5', '');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Beam Geometry'), 'Conical-hemispherical (CASE 6)', '6', '');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Beam Geometry'), 'Hemispherical-directional (CASE 7)', '7', '');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Beam Geometry'), 'Hemispherical-conical (CASE 8)', '8', '');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Beam Geometry'), 'Bihemispherical (CASE 9)', '9', '');



-- corine landcover, maybe not a brilliant solution, but anyway, could be converted to something more meaningful later on ....


INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('CORINE Landcover', '', (select category_id from `specchio`.`category` where name = 'Generic Target Properties'), 'taxonomy_id');

INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Artificial Surfaces', '1', '');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Agricultural Areas', '2', '');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Forest and Semi-Natural Areas', '3', '');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Wetlands', '4', '');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Water Bodies', '5', '');

CREATE TEMPORARY TABLE IF NOT EXISTS `specchio_temp`.`temp_tax_table` AS (SELECT * FROM `specchio`.`taxonomy`);

INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Urban fabric', '11', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 1));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Industrial, commercial and transport units', '12', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 1));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Mine, dump and construction sites', '13', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 1));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Artificial, non-agricultural vegetated areas', '14', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 1));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Arable Land', '21', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 2));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Permanent Crops', '22', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 2));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Pasture', '23', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 2));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Heterogeneous agricultural areas', '24', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 2));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Forests', '31', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 3));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Scrub and/or herbaceous associations', '32', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 3));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Open spaces with little or no vegetation', '33', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 3));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Inland wetlands', '41', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 4));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Marine wetlands', '42', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 4));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Inland waters', '51', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 5));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Marine waters', '52', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 5));

delete from `specchio_temp`.`temp_tax_table`;
insert into `specchio_temp`.`temp_tax_table`  (SELECT * FROM `specchio`.`taxonomy`);


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Continuous urban fabric', '111', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 11));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Discontinuous urban fabric', '112', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 11));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Industrial or commercial units', '121', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 12));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Road and rail networks and associated land', '122', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 12));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Port areas', '123', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 12));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Airports', '124', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 12));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Mineral extraction sites', '131', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 13));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Dump sites', '132', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 13));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Construction sites', '133', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 13));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Green urban areas', '141', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 14));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Port and leisure facilities', '142', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 14));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Non-irrigated arable land', '211', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 21));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Permanently irrigated land', '212', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 21));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Rice fields', '213', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 21));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Vineyards', '221', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 22));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Fruit trees and berry plantations', '222', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 22));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Olive groves', '223', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 22));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Pastures', '231', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 23));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Annual crops associated with permanent crops', '241', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 24));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Complex cultivation patterns', '242', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 24));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Mainly agricultural land with significant areas of natural vegetation', '243', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 24));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Agro-forestry areas', '244', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 24));



INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Deciduous forest', '311', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 31));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Coniferous forest', '312', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 31));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Mixed forest', '313', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 31));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Natural grassland', '321', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 32));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Moors and heathland', '322', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 32));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Sclerophyllous vegetation', '323', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 32));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Transitional woodland-scrub', '324', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 32));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Beaches, dunes, sands', '331', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 33));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Bare rocks', '332', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 33));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Sparsely vegetated areas', '333', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 33));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Burnt areas', '334', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 33));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Glaciers and perpetual snow', '335', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 33));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Inland marshes', '411', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 41));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Peat bogs', '412', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 41));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Salt marshes', '421', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 42));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Salines', '422', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 42));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Intertidal flats', '423', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 42));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Water courses', '511', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 51));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Water bodies', '512', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 51));


INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Coastal lagoons', '521', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 52));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Estuaries', '522', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 52));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Sea and ocean', '523', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 52));





-- data portal
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('Data Usage Policy', '', (select category_id from `specchio`.`category` where name = 'Data Portal'), 'string_val');
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('Digital Object Identifier', '', (select category_id from `specchio`.`category` where name = 'Data Portal'), 'string_val');


-- remove obsolete categories and attributes of V2.2
-- Attention: data contained in the related metaparameters need copying using new attributes first!!!

-- renaming for unified naming of attributes
-- Integration time
update eav set attribute_id = (select attribute_id from attribute where binary name = 'Integration Time') where attribute_id = (select attribute_id from attribute where binary name = 'Integration time');
-- Number of internal scans (probably not existing in the current online system)
update eav set attribute_id = (select attribute_id from attribute where binary name = 'Number of internal Scans') where attribute_id = (select attribute_id from attribute where binary name = 'Number of internal scans');
-- Spectrum number
update eav set attribute_id = (select attribute_id from attribute where binary name = 'Spectrum Number') where attribute_id = (select attribute_id from attribute where binary name = 'Spectrum number');
-- "Capturing Software Name"
update eav set attribute_id = (select attribute_id from attribute where binary name = 'Capturing Software Name') where attribute_id = (select attribute_id from attribute where binary name = '');
-- "Capturing Software Version"
update eav set attribute_id = (select attribute_id from attribute where binary name = 'Capturing Software Version') where attribute_id = (select attribute_id from attribute where binary name = '');
--  "UniSpec Spectral Resampling"
update eav set attribute_id = (select attribute_id from attribute where binary name = 'UniSpec Spectral Resampling') where attribute_id = (select attribute_id from attribute where binary name = '');
-- "Instrument Channel"
update eav set attribute_id = (select attribute_id from attribute where binary name = 'Instrument Channel') where attribute_id = (select attribute_id from attribute where binary name = '');
-- "Processing Level"
update eav set attribute_id = (select attribute_id from attribute where binary name = 'Processing Level') where attribute_id = (select attribute_id from attribute where binary name = '');
-- "File Version"
update eav set attribute_id = (select attribute_id from attribute where binary name = 'File Version') where attribute_id = (select attribute_id from attribute where binary name = '');
-- Dark Current Correction
update eav set attribute_id = (select attribute_id from attribute where binary name = 'Dark Current Correction') where attribute_id = (select attribute_id from attribute where binary name = '');


-- IN WORK
-- *******


-- > steps to be done in Java:
--	Essential: do this for every campaign with the corresponding user, otherwise, data will no longer belong to the proper campaign and user, or in some way enforce that the research group is set correctly, or the user_id is set again if group update is not yet applied
--	select spectrum_ids and instrument_settings
--	create new eav entry, use EAV services methods to reduce redundancy
--  insert spectrum_x_eav entries

-- >> insert from instrument_setting into EAV
-- Number of internal scans -> Number of internal Scans
-- Gain_SWIR1
-- Gain_SWIR2
-- Offset_SWIR1
-- Offset_SWIR2

-- >> insert from spectrum into EAV (spectrum.column -> EAV attribute name)
-- internal_average_cnt -> Number of internal Scans  (to be checked: is this ever filled in V2.2?)
-- number -> Spectrum Number
-- file_comment -> File Comments
-- date -> Acquisition Time
-- loading_date -> Loading Time
-- file_name -> File Name

-- >> insert from foreoptic into EAV
-- foreoptic.degree -> FOV 	int_val

-- >> insert from environmental_condition
-- ambient_temperature -> 'Ambient Temperature' double_val
-- air_pressure -> 'Air Pressure'  double_val
-- relative_humidity -> 'Relative Humidity' double_val
-- wind_direction via wind_direction_id -> 'Wind Direction': N = 0, NE = 45, E = 90, SE=135, S = 180, SW=225, W = 270, NW = 315
-- wind_speed via wind_speed_id -> 'Wind Speed' calm = 0, windy = 8, stormy = 15  (see http://www.metoffice.gov.uk/weather/marine/guide/beaufortscale.html)
-- cloud_cover via cloud_cover_id -> 'Cloud Cover': 0 = , 1=, 2=, 3=, 4=, 5=, 6=, 7=, 8=


-- >> insert from position


-- >> insert from sampling_geometry


-- >> insert from spectrum_name


-- >> insert from spectrum_x_target_type
-- add V2.2 target types as text of attribute 'Target Description'


-- >> insert from picture


-- >> insert from assoc_measurement






-- *******

-- remove old user-based triggers and views
DROP TRIGGER `specchio`.`hierarchy_level_tr`;
DROP TRIGGER `specchio`.`hierarchy_level_x_spectrum_tr`;
DROP TRIGGER `specchio`.`spectrum_datalink_tr`;
DROP TRIGGER `specchio`.`spectrum_tr`;
DROP TRIGGER `specchio`.`eav_tr`;
DROP TRIGGER `specchio`.`spectrum_x_eav_tr`;
DROP VIEW `specchio`.`campaign_view`;
DROP VIEW `specchio`.`hierarchy_level_view`;
DROP VIEW `specchio`.`hierarchy_level_x_spectrum_view`;
DROP VIEW `specchio`.`spectrum_datalink_view`;
DROP VIEW `specchio`.`spectrum_view`;
DROP VIEW `specchio`.`eav_view`;
DROP VIEW `specchio`.`spectrum_x_eav_view`;


-- bigger blob
ALTER TABLE `specchio`.`eav` MODIFY COLUMN `binary_val` MEDIUMBLOB;


-- create campaign path table
CREATE TABLE `specchio`.`campaign_path` (
	`campaign_id` INT(11) REFERENCES `specchio`.`campaign`(`campaign_id`),
	`path` VARCHAR(500)
);
INSERT INTO `specchio`.`campaign_path` SELECT `campaign_id`,`path` FROM `specchio`.`campaign`;




-- create research group tables
CREATE TABLE `specchio`.`research_group` (
	`research_group_id`	INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`name` VARCHAR(100)
);	

CREATE TABLE `specchio`.`research_group_members` (
	`research_group_id` INT(10) REFERENCES `specchio`.`research_group`(`research_group_id`),
	`member_id` INT(10) REFERENCES `specchio`.`specchio_user`(`user_id`),
	PRIMARY KEY(`research_group_id`,`member_id`)
);

-- change user_id to campaign_id
ALTER TABLE `specchio`.`campaign`
	ADD COLUMN `research_group_id` INT(10),
	ADD CONSTRAINT `FK_campaign_research_group_id` FOREIGN KEY (`research_group_id`) REFERENCES `specchio`.`research_group`(`research_group_id`);
ALTER TABLE `specchio`.`hierarchy_level`
	DROP FOREIGN KEY `FK_hierarchy_level_user_id`,
	DROP COLUMN `user_id`;
	
	
ALTER TABLE `specchio`.`spectrum`
	DROP FOREIGN KEY `FK_spectrum_user_id`,
	DROP COLUMN `user_id`;
ALTER TABLE `specchio`.`eav`
	DROP FOREIGN KEY `fk_eav_specchio_user1`,
	DROP COLUMN `user_id`,
	ADD COLUMN `campaign_id` INT(11),
	ADD CONSTRAINT `FK_eav_campaign_id` FOREIGN KEY (`campaign_id`) REFERENCES `specchio`.`campaign`(`campaign_id`);
ALTER TABLE `specchio`.`spectrum_x_eav`
	DROP FOREIGN KEY `spectrum_x_eav_fk3`,
	DROP COLUMN `user_id`,
	ADD COLUMN `campaign_id` INT(11),
	ADD CONSTRAINT `FK_spectrum_x_eav_campaign_id` FOREIGN KEY (`campaign_id`) REFERENCES `specchio`.`campaign`(`campaign_id`);

-- re-define views to use research groups instead of users
CREATE VIEW `specchio`.`research_group_view` AS
	SELECT *
	FROM `specchio`.`research_group`
	WHERE `research_group_id` IN (
		SELECT `research_group_members`.`research_group_id`
		FROM `specchio`.`research_group_members`, `specchio`.`specchio_user`
		WHERE
			`research_group_members`.`member_id` = `specchio_user`.`user_id`
			AND
			`specchio_user`.`user` = SUBSTRING_INDEX((select user()), '@', 1)
	);
CREATE VIEW `specchio`.`research_group_members_view` AS
	SELECT *
	FROM `specchio`.`research_group_members`;
CREATE VIEW `specchio`.`campaign_view` AS
	SELECT `campaign`.*
	FROM `specchio`.`campaign`
	WHERE `campaign`.`research_group_id` IN (
		SELECT `research_group_members`.`research_group_id`
		FROM `specchio`.`research_group_members`, `specchio`.`specchio_user`
		WHERE
			`research_group_members`.`member_id` = `specchio_user`.`user_id`
			AND
			`specchio_user`.`user` = SUBSTRING_INDEX((select user()), '@', 1)
	);
CREATE VIEW `specchio`.`campaign_path_view` AS
	SELECT `campaign_path`.*
	FROM `specchio`.`campaign_path`
	WHERE `campaign_path`.`campaign_id` IN (
		SELECT `campaign`.`campaign_id`
		FROM `specchio`.`campaign`, `specchio`.`research_group_members`, `specchio`.`specchio_user`
		WHERE
			`campaign`.`research_group_id` = `research_group_members`.`research_group_id`
			AND
			`research_group_members`.`member_id` = `specchio_user`.`user_id`
			AND
			`specchio_user`.`user` = SUBSTRING_INDEX((select user()), '@', 1)
	);
CREATE VIEW `specchio`.`hierarchy_level_view` AS
	SELECT `hierarchy_level`.*
	FROM `specchio`.`hierarchy_level`
	WHERE `hierarchy_level`.`campaign_id` IN (
		SELECT `campaign`.`campaign_id`
		FROM `specchio`.`campaign`, `specchio`.`research_group_members`, `specchio`.`specchio_user`
		WHERE
			`campaign`.`research_group_id` = `research_group_members`.`research_group_id`
			AND
			`research_group_members`.`member_id` = `specchio_user`.`user_id`
			AND
			`specchio_user`.`user` = SUBSTRING_INDEX((select user()), '@', 1)
	);

CREATE VIEW `specchio`.`spectrum_view` AS
	SELECT `spectrum`.*
	FROM `specchio`.`spectrum`
	WHERE `spectrum`.`campaign_id`  IN (
		SELECT `campaign`.`campaign_id`
		FROM `specchio`.`campaign`, `specchio`.`research_group_members`, `specchio`.`specchio_user`
		WHERE
			`campaign`.`research_group_id` = `research_group_members`.`research_group_id`
			AND
			`research_group_members`.`member_id` = `specchio_user`.`user_id`
			AND
			`specchio_user`.`user` = SUBSTRING_INDEX((select user()), '@', 1)
	);
CREATE VIEW `specchio`.`spectrum_x_eav_view` AS
	SELECT `spectrum_x_eav`.*
	FROM `specchio`.`spectrum_x_eav`
	WHERE `spectrum_x_eav`.`campaign_id` IN (
		SELECT `campaign`.`campaign_id`
		FROM `specchio`.`campaign`, `specchio`.`research_group_members`, `specchio`.`specchio_user`
		WHERE
			`campaign`.`research_group_id` = `research_group_members`.`research_group_id`
			AND
			`research_group_members`.`member_id` = `specchio_user`.`user_id`
			AND
			`specchio_user`.`user` = SUBSTRING_INDEX((select user()), '@', 1)
	);
CREATE VIEW `specchio`.`eav_view` AS
	SELECT `eav`.*
	FROM `specchio`.`eav`
	WHERE `eav`.`campaign_id` IN (
		SELECT `campaign`.`campaign_id`
		FROM `specchio`.`campaign`, `specchio`.`research_group_members`, `specchio`.`specchio_user`
		WHERE
			`campaign`.`research_group_id` = `research_group_members`.`research_group_id`
			AND
			`research_group_members`.`member_id` = `specchio_user`.`user_id`
			AND
			`specchio_user`.`user` = SUBSTRING_INDEX((select user()), '@', 1)
	);


-- re-define triggers to fill the campaign_id instead of user_id
CREATE TRIGGER `specchio`.`spectrum_x_eav_tr`
	BEFORE INSERT ON `specchio`.`spectrum_x_eav`
	FOR EACH ROW SET new.`campaign_id` = (
		SELECT `campaign_id` FROM `specchio`.`spectrum` WHERE `spectrum`.`spectrum_id` = new.`spectrum_id`
	);



-- target homogeneity

INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('Target Homogeneity', '', (select category_id from `specchio`.`category` where name = 'Generic Target Properties'), 'taxonomy_id');

INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Target Homogeneity'), 'Homogenous', 'Homogenous', 'Dichotomous homogeneity attribute');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Target Homogeneity'), 'Heterogenous', 'Heterogenous', 'Dichotomous homogeneity attribute');


-- db version
INSERT INTO `specchio`.`schema_info` (`version`, `date`) VALUES ('3.0', CURDATE());





