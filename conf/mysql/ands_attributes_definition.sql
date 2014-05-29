--
-- Attribute and taxonomy definitions for use of SPECCHIO with the Australian
-- National Data Service (ANDS)
--

--
-- This file is designed to be idempotent. The general strategy is:
--
--   1. Populate a temporary table with all of the desired attributes
--   2. Delete all of the attributes that already exist from the temporary table
--   3. Insert the reduced temporary table into the real table
--

--
-- ANDS data portal attributes
--

CREATE TEMPORARY TABLE `specchio_temp`.`attribute_insert` LIKE `specchio`.`attribute`;
INSERT INTO `specchio_temp`.`attribute_insert` (`name`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('ANDS Collection Key', (select category_id from `specchio`.`category` where name = 'Data Portal'), 'string_val', (select unit_id from `specchio`.`unit` where short_name = 'String'), NULL);
INSERT INTO `specchio_temp`.`attribute_insert` (`name`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('ANDS Collection Name', (select category_id from `specchio`.`category` where name = 'Data Portal'), 'string_val', (select unit_id from `specchio`.`unit` where short_name = 'String'), NULL);
INSERT INTO `specchio_temp`.`attribute_insert` (`name`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('ANDS Collection Description', (select category_id from `specchio`.`category` where name = 'Data Portal'), 'string_val', (select unit_id from `specchio`.`unit` where short_name = 'String'), NULL);
INSERT INTO `specchio_temp`.`attribute_insert` (`name`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('FOR Code', (select category_id from `specchio`.`category` where name = 'Data Portal'), 'taxonomy_id', (select unit_id from `specchio`.`unit` where short_name = 'String'), NULL);
DELETE FROM `specchio_temp`.`attribute_insert` WHERE `attribute_insert`.`name` IN (SELECT `attribute`.`name` FROM `specchio`.`attribute`) LIMIT 1000;
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) SELECT  `name`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality` FROM `specchio_temp`.`attribute_insert`;
DROP TABLE `specchio_temp`.`attribute_insert`;


--
-- Two-digit FOR codes
--

CREATE TEMPORARY TABLE `specchio_temp`.`taxonomy_insert` LIKE `specchio`.`taxonomy`;
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '01 Mathematical sciences', '01', 'Mathematical sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '02 Physical sciences', '02', 'Physical sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '03 Chemical sciences', '03', 'Chemical sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '04 Earth sciences', '04', 'Earth sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '05 Environmental sciences', '05', 'Environmental sciencess');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '06 Biological sciences', '06', 'Biological sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '07 Agricultural and veterinary sciences', '07', 'Agricultural and veterinary sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '08 Information and computing sciences', '08', 'Information and computing sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '09 Engineering', '09', 'Engineering');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '10 Technology', '10', 'Technology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '11 Medical and health sciences', '11', 'Medical and health sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '12 Built environment and design', '12', '12 Built environment and design');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '13 Education', '13', 'Education');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '14 Economics', '14', 'Economics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '15 Commerce, management, tourism, and services', '15', 'Commerce, management, tourism, and services');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '16 Studies in human society', '16', 'Studies in human society');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '17 Psychology and cognitive sciences', '17', 'Psychology and cognitive sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '18 Law and legal studies', '18', 'Law and legal studies');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '19 Studies in creative arts and writing', '19', 'Studies in creative arts and writing');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '20 Language, communication and culture', '20', 'Language, communication and culture');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '21 History and archaeology', '21', 'History and archaeology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '22 Philosophy and religious studies', '22', 'Philosophy and religious studies');
DELETE FROM `specchio_temp`.`taxonomy_insert` WHERE `code` IN (SELECT `taxonomy`.`code` FROM `specchio`.`taxonomy`) LIMIT 1000;
INSERT INTO `specchio`.`taxonomy`(`parent_id`, `attribute_id`, `name`, `code`, `description`) SELECT `parent_id`, `attribute_id`, `name`, `code`, `description` FROM `specchio_temp`.`taxonomy_insert`;
DROP TABLE `specchio_temp`.`taxonomy_insert`;

--
-- Create a temporary table mapping the two-digit FOR codes to their taxonomy identifers
--

CREATE TEMPORARY TABLE `specchio_temp`.`for_x_taxonomy` (
	`code` CHAR(2) PRIMARY KEY NOT NULL,
	`taxonomy_id` INT(10) NOT NULL
);
INSERT INTO `specchio_temp`.`for_x_taxonomy`(`code`, `taxonomy_id`) SELECT `code`,`taxonomy_id` FROM `specchio`.`taxonomy` WHERE `attribute_id` = (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code');

--
-- Insert four-digit FOR codes as children of their two-digit parent
--

CREATE TEMPORARY TABLE `specchio_temp`.`taxonomy_insert` LIKE `specchio`.`taxonomy`;
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '01'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0101 Pure mathematics', '0101', 'Pure mathematics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '01'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0102 Applied mathematics', '0102', 'Applied mathematics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '01'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0103 Numerical and computational mathematics', '0103', 'Numerical and computational mathematics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '01'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0104 Statistics', '0104', 'Statistics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '01'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0105 Mathematical physics', '0105', 'Mathematical physics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '01'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0199 Other mathematical sciences', '0199', 'Other mathematical sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '02'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0201 Astronomical and space sciences', '0201', 'Astronomical and space sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '02'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0202 Atomic, molecular, nuclear, particle and plasma physics', '0202', 'Atomic, molecular, nuclear, particle and plasma physics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '02'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0203 Classical physics', '0203', 'Classical physics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '02'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0204 Condensed matter physics', '0204', 'Condensed matter physics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '02'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0205 Optical physics', '0205', 'Optical physics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '02'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0206 Quantum physics', '0206', 'Quantum physics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '02'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0299 Other physical sciences', '0299', 'Other physical sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '03'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0301 Analytical chemistry', '0301', 'Analytical chemistry');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '03'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0302 Inorganic chemistry', '0302', 'Inorganic chemistry');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '03'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0303 Macromolecular and materials chemistry', '0303', 'Macromolecular and materials chemistry');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '03'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0304 Medicinal and biomolecular chemistry', '0304', 'Medicinal and biomolecular chemistry');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '03'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0305 Organic chemistry', '0305', 'Organic chemistry');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '03'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0306 Physical chemistry (incl. structural)', '0306', 'Physical chemistry (incl. structural)');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '03'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0307 Theoretical and computational chemistry', '0307', 'Theoretical and computational chemistry');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '03'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0399 Other chemical sciences', '0399', 'Other chemical sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '04'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0401 Atmospheric sciences', '0401', 'Atmospheric sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '04'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0402 Geochemistry', '0402', 'Geochemistry');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '04'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0403 Geology', '0403', 'Geology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '04'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0404 Geophysics', '0404', 'Geophysics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '04'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0405 Oceanography', '0405', 'Oceanography');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '04'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0406 Physical geography and environmental geoscience', '0406', 'Physical geography and environmental geoscience');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '04'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0499 Other earth sciences', '0499', 'Other earth sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '05'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0501 Ecological applications', '0501', 'Ecological applications');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '05'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0502 Environmental science and management', '0502', 'Environmental science and management');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '05'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0503 Soil sciences', '0503', 'Soil sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '05'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0599 Other environmental sciences', '0599', 'Other environmental sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '06'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0601 Biochemistry and cell biology', '0601', 'Biochemistry and cell biology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '06'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0602 Ecology', '0602', 'Ecology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '06'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0603 Evolutionary biology', '0603', 'Evolutionary biology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '06'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0604 Genetics', '0604', 'Genetics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '06'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0605 Microbiology', '0605', 'Microbiology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '06'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0606 Physiology', '0606', 'Physiology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '06'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0607 Plant biology', '0607', 'Plant biology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '06'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0608 Zoology', '0608', 'Zoology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '06'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0699 Other biological sciences', '0699', 'Other biological sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '07'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0701 Agriculture, land and farm management', '0701', 'Agriculture, land and farm management');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '07'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0702 Animal production', '0702', 'Animal production');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '07'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0703 Crop and pasture production', '0703', 'Crop and pasture production');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '07'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0704 Fisheries sciences', '0704', 'Fisheries sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '07'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0705 Forestry sciences', '0705', 'Forestry sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '07'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0706 Horticultural production', '0706', 'Horticultural production');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '07'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0707 Veterinary sciences', '0707', 'Veterinary sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '07'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0799 Other agricultural and veterinary sciences', '0799', 'Other agricultural and veterinary sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '08'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0801 Artificial intelligence and image processing', '0801', 'Artificial intelligence and image processing');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '08'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0802 Computation theory and mathematics', '0802', 'Computation theory and mathematics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '08'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0803 Computer software', '0803', 'Computer software');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '08'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0804 Data format', '0804', 'Data format');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '08'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0805 Distributed computing', '0805', 'Distributed computing');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '08'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0806 Information systems', '0806', 'Information systems');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '08'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0807 Library and information studies', '0807', 'Library and information studies');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '08'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0899 Other information and computing sciences', '0899', 'Other information and computing sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0901 Aerospace engineering', '0901', 'Aerospace engineering');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0902 Automotive engineering', '0902', 'Automotive engineering');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0903 Biomedical engineering', '0903', 'Biomedical engineering');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0904 Chemical engineering', '0904', 'Chemical engineering');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0905 Civil engineering', '0905', 'Civil engineering');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0906 Electrical and electronic engineering', '0906', 'Electrical and electronic engineering');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0907 Environmental engineering', '0907', 'Environmental engineering');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0908 Food sciences', '0908', 'Food sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0909 Geomatic engineering', '0909', 'Geomatic engineering');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0910 Manufacturing engineering', '0910', 'Manufacturing engineering');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0911 Maritime engineering', '0911', 'Maritime engineering');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0912 Materials engineering', '0912', 'Materials engineering');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0913 Mechanical engineering', '0913', 'Mechanical engineering');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0914 Resources engineering and extractive metallurgy', '0914', 'Resources engineering and extractive metallurgy');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0915 Interdisciplinary engineering', '0915', 'Interdisciplinary engineering');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '09'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0999 Other engineering', '0999', 'Other engineering');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '10'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1001 Agricultural biotechnology', '1001', 'Agricultural biotechnology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '10'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1002 Environmental biotechnology', '1002', 'Environmental biotechnology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '10'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1003 Industrial biotechnology', '1003', 'Industrial biotechnology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '10'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1004 Medical biotechnology', '1004', 'Medical biotechnology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '10'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1005 Communications technologies', '1005', 'Communications technologies');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '10'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1006 Computer hardware', '1006', 'Computer hardware');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '10'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1007 Nanotechnology', '1007', 'Nanotechnology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '10'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1099 Other technology', '1099', 'Other technology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1101 Medical biochemistry and metabolomics', '1101', 'Medical biochemistry and metabolomics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1102 Cardiovascular medicine and haematology', '1102', 'Cardiovascular medicine and haematology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1103 Clinical sciences', '1103', 'Clinical sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1104 Complementary and alternative medicine', '1104', 'Complementary and alternative medicine');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1105 Dentistry', '1105', 'Dentistry');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1106 Human movement and sports science', '1106', 'Human movement and sports science');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1107 Immunology', '1107', 'Immunology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1108 Medical microbiology', '1108', 'Medical microbiology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1109 Neurosciences', '1109', 'Neurosciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1110 Nursing', '1110', 'Nursing');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1111 Nutrition and dietetics', '1111', 'Nutrition and dietetics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1112 Oncology and carcinogenesis', '1112', 'Oncology and carcinogenesis');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1113 Ophthalmology and optometry', '1113', 'Ophthalmology and optometry');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1114 Paediatrics and reproductive medicine', '1114', 'Paediatrics and reproductive medicine');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1115 Pharmacology and pharmaceutical sciences', '1115', 'Pharmacology and pharmaceutical sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1116 Medical physiology', '1116', 'Medical physiology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1117 Public health and health services', '1117', 'Public health and health services');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '11'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1199 Other medical and health services', '1199', 'Other medical and health services');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '12'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1201 Architecture', '1201', 'Architecture');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '12'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1202 Building', '1202', 'Building');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '12'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1203 Design practice and management', '1203', 'Design practice and management');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '12'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1204 Engineering design', '1204', 'Engineering design');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '12'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1205 Urban and regional planning', '1205', 'Urban and regional planning');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '12'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1299 Other built environment and design', '1299', 'Other built environment and design');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '13'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1301 Education systems', '1301', 'Education systems');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '13'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1302 Curriculum and pedagogy', '1302', 'Curriculum and pedagogy');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '13'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1303 Specialist studies in education', '1303', 'Specialist studies in education');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '13'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1399 Other education', '1399', 'Other education');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '14'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1401 Economic theory', '1401', 'Economic theory');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '14'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1402 Applied economics', '1402', 'Applied economics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '14'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1403 Econometrics', '1403', 'Econometrics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '14'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1499 Other economics', '1499', 'Other economics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '15'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1501 Accounting, auditing and accountability', '1501', 'Accounting, auditing and accountability');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '15'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1502 Banking, finance and investment', '1502', 'Banking, finance and investment');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '15'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1503 Business and management', '1503', 'Business and management');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '15'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1504 Commercial services', '1504', 'Commercial services');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '15'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1505 Marketing', '1505', 'Marketing');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '15'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1506 Tourism', '1506', 'Tourism');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '15'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1507 Transportation and freight services', '1507', 'Transportation and freight services');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '15'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1599 Other commerce, management, tourism and services', '1599', 'Other commerce, management, tourism and services');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '16'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1601 Anthropology', '1601', 'Anthropology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '16'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1602 Criminology', '1602', 'Criminology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '16'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1603 Demography', '1603', 'Demography');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '16'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1604 Human geography', '1604', 'Human geography');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '16'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1605 Policy and administration', '1605', 'Policy and administration');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '16'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1606 Political science', '1606', 'Political science');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '16'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1607 Social work', '1607', 'Social work');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '16'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1608 Sociology', '1608', 'Sociology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '16'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1699 Other studies in human society', '1699', 'Other studies in human society');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '17'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1701 Psychology', '1701', 'Psychology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '17'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1702 Cognitive science', '1702', 'Cognitive science');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '17'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1799 Other psychology and cognitive sciences', '1799', 'Other psychology and cognitive sciences');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '18'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1801 Law', '1801', 'Law');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '18'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1802 Maori law', '1802', 'Maori law');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '18'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1899 Other law and legal studies', '1899', 'Other law and legal studies');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '19'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1901 Art theory and criticism', '1901', 'Art theory and criticism');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '19'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1902 Film, television and digital media', '1902', 'Film, television and digital media');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '19'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1903 Journalism and professional writing', '1903', 'Journalism and professional writing');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '19'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1904 Performing arts and creative writing', '1904', 'Performing arts and creative writing');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '19'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1905 Visual arts and crafts', '1905', 'Visual arts and crafts');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '19'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '1999 Other studies in creative arts and writing', '1999', 'Other studies in creative arts and writing');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '20'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '2001 Communication and media studies', '2001', 'Communication and media studies');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '20'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '2002 Cultural studies', '2002', 'Cultural studies');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '20'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '2003 Language studies', '2003', 'Language studies');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '20'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '2004 Linguistics', '2004', 'Linguistics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '20'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '2005 Literary studies', '2005', 'Literary studies');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '20'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '2099 Other language, communication and culture', '2099', 'Other language, communication and culture');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '21'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '2101 Archaeology', '2101', 'Archaeology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '21'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '2102 Curatorial and related studies', '2102', 'Curatorial and related studies');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '21'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '2103 Historical studies', '2103', 'Historical studies');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '21'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '2104 Other history and archaeology', '2104', 'Other history and archaeology');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '22'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '2201 Applied ethics', '2201', 'Applied ethics');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '22'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '2202 History and philosophy of specific fields', '2202', 'History and philosophy of specific fields');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '22'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '2203 Philosophy', '2203', 'Philosophy');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '22'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '2204 Religion and religious studies', '2204', 'Religion and religious studies');
INSERT INTO `specchio_temp`.`taxonomy_insert` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '22'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '2299 Other philosophy and religious studies', '2299', 'Other philosophy and religious studies');

--
-- Delete all of the taxonomy objects that already exist
--

DELETE FROM `specchio_temp`.`taxonomy_insert` WHERE `code` IN (SELECT `taxonomy`.`code` FROM `specchio`.`taxonomy`) LIMIT 1000;

--
-- Copy the temporary table into the real table
--

INSERT INTO `specchio`.`taxonomy`(`parent_id`, `attribute_id`, `name`, `code`, `description`) SELECT `parent_id`, `attribute_id`, `name`, `code`, `description` FROM `specchio_temp`.`taxonomy_insert`;

--
-- Remove temoporary tables
--

DROP TABLE `specchio_temp`.`for_x_taxonomy`;
DROP TABLE `specchio_temp`.`taxonomy_insert`;