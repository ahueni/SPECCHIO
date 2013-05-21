--
-- Attribute and taxonomy definitions for use of SPECCHIO with the Australian
-- National Data Service (ANDS)
--

-- ANDS data portal attributes
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('ANDS Collection Key', (select category_id from `specchio`.`category` where name = 'Data Portal'), 'string_val', (select unit_id from `specchio`.`unit` where short_name = 'String'));
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`) VALUES ('FOR Code', (select category_id from `specchio`.`category` where name = 'Data Portal'), 'taxonomy_id', (select unit_id from `specchio`.`unit` where short_name = 'String'));


-- FOR codes
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '04 Earth sciences', '04', 'Earth sciences');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '05 Environmental sciences', '05', 'Environmental sciencess');

-- create a temporary table mapping the two-digit FOR codes to their taxonomy identifers
CREATE TEMPORARY TABLE `specchio_temp`.`for_x_taxonomy` (
	`code` CHAR(2) PRIMARY KEY NOT NULL,
	`taxonomy_id` INT(10) NOT NULL
);
INSERT INTO `specchio_temp`.`for_x_taxonomy`(`code`, `taxonomy_id`) SELECT `code`,`taxonomy_id` FROM `specchio`.`taxonomy` WHERE `attribute_id` = (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code');

-- earth sciences FOR codes
INSERT INTO `specchio`.`taxonomy` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '04'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0401 Earth sciences', '0401', 'Earth sciences');
INSERT INTO `specchio`.`taxonomy` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '04'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0402 Geochemistry', '0402', 'Geochemistry');
INSERT INTO `specchio`.`taxonomy` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '04'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0403 Geology', '0403', 'Geology');
INSERT INTO `specchio`.`taxonomy` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '04'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0404 Geology', '0404', 'Geophysics');
INSERT INTO `specchio`.`taxonomy` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '04'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0405 Oceanography', '0405', 'Oceanography');
INSERT INTO `specchio`.`taxonomy` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '04'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0406 Physical geography and environmental geoscience', '0406', 'Physical geography and environmental geoscience');
INSERT INTO `specchio`.`taxonomy` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '04'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0499 Other earth sciences', '0499', 'Other earth sciences');

-- environmental sciences FOR codes
INSERT INTO `specchio`.`taxonomy` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '05'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0501 Ecological applications', '0501', 'Ecological applications');
INSERT INTO `specchio`.`taxonomy` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '05'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0502 Environmental science and management', '0502', 'Environmental science and management');
INSERT INTO `specchio`.`taxonomy` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '05'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0503 Soil sciences', '0503', 'Soil sciences');
INSERT INTO `specchio`.`taxonomy` (`parent_id`, `attribute_id`, `name`, `code`, `description`)
	VALUES ((select `taxonomy_id` from `specchio_temp`.`for_x_taxonomy` where `code` = '05'), (select `attribute_id` from `specchio`.`attribute` where name = 'FOR Code'), '0599 Other environmental sciences', '0599', 'Other environmental sciences');

-- remove temoporary tables
DROP TABLE `specchio_temp`.`for_x_taxonomy`;