--  calibration upgrade


ALTER TABLE `specchio`.`spectrum` ADD COLUMN `calibration_id` INT UNSIGNED NULL DEFAULT NULL  AFTER `reference_id` ;


ALTER TABLE `specchio`.`spectrum` 
  ADD CONSTRAINT `spectrum_cal_fk`
  FOREIGN KEY (`calibration_id` )
  REFERENCES `specchio`.`calibration` (`calibration_id` )
  ON DELETE RESTRICT
  ON UPDATE NO ACTION
, ADD INDEX `spectrum_cal_fk_idx` (`calibration_id` ASC) ;



USE `specchio`;
CREATE 
     OR REPLACE ALGORITHM = UNDEFINED 
    DEFINER = `root`@`localhost` 
    SQL SECURITY DEFINER
VIEW `spectrum_view` AS
    select 
        `spectrum`.`spectrum_id` AS `spectrum_id`,
        `spectrum`.`goniometer_id` AS `goniometer_id`,
        `spectrum`.`target_homogeneity_id` AS `target_homogeneity_id`,
        `spectrum`.`illumination_source_id` AS `illumination_source_id`,
        `spectrum`.`sampling_environment_id` AS `sampling_environment_id`,
        `spectrum`.`measurement_type_id` AS `measurement_type_id`,
        `spectrum`.`measurement_unit_id` AS `measurement_unit_id`,
        `spectrum`.`landcover_id` AS `landcover_id`,
        `spectrum`.`measurement` AS `measurement`,
        `spectrum`.`is_reference` AS `is_reference`,
        `spectrum`.`hierarchy_level_id` AS `hierarchy_level_id`,
        `spectrum`.`sensor_id` AS `sensor_id`,
        `spectrum`.`file_format_id` AS `file_format_id`,
        `spectrum`.`campaign_id` AS `campaign_id`,
        `spectrum`.`instrument_id` AS `instrument_id`,
        `spectrum`.`required_quality_level_id` AS `required_quality_level_id`,
        `spectrum`.`quality_level_id` AS `quality_level_id`,
        `spectrum`.`reference_id` AS `reference_id`,
		`spectrum`.`calibration_id` AS `calibration_id`
    from
        `spectrum`
    where
        `spectrum`.`campaign_id` in (select 
                `campaign`.`campaign_id`
            from
                ((`campaign`
                join `research_group_members`)
                join `specchio_user`)
            where
                ((`campaign`.`research_group_id` = `research_group_members`.`research_group_id`)
                    and (`research_group_members`.`member_id` = `specchio_user`.`user_id`)
                    and (`specchio_user`.`user` = substring_index((select user()), '@', 1))));





ALTER TABLE `specchio`.`sensor` 
CHANGE COLUMN `name` `name` VARCHAR(100) NULL DEFAULT NULL ;

ALTER TABLE `specchio`.`instrument` 
CHANGE COLUMN `name` `name` VARCHAR(100) NULL DEFAULT NULL ;



--  unit and attribute updates

update `specchio`.`unit` set description = 'Angular value in degrees' where name = 'Degrees';


INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`) SELECT * FROM (SELECT 'Degrees Celcius', 'Temperature unit in degrees Celcius', '°C') AS tmp
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


update attribute set default_unit_id = (select unit_id from `specchio`.`unit` where short_name = '°C') where name = 'Ambient Temperature';
update attribute set default_unit_id = (select unit_id from `specchio`.`unit` where short_name = 'hPa') where name = 'Air Pressure';
update attribute set default_unit_id = (select unit_id from `specchio`.`unit` where short_name = 'm/s') where name = 'Wind Speed';



--  corine landcover, maybe not a brilliant solution, but anyway, could be converted to something more meaningful later on ....
-- Corine is already included in the online DB run by RSL, other instances may need this upgrade separately

-- INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('CORINE Landcover', '', (select category_id from `specchio`.`category` where name = 'Generic Target Properties'), 'taxonomy_id');
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Artificial Surfaces', '1', '');
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Agricultural Areas', '2', '');
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Forest and Semi-Natural Areas', '3', '');
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Wetlands', '4', '');
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Water Bodies', '5', '');
-- 
-- CREATE TEMPORARY TABLE IF NOT EXISTS `specchio_temp`.`temp_tax_table` AS (SELECT * FROM `specchio`.`taxonomy`);
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Urban fabric', '11', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 1));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Industrial, commercial and transport units', '12', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 1));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Mine, dump and construction sites', '13', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 1));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Artificial, non-agricultural vegetated areas', '14', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 1));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Arable Land', '21', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 2));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Permanent Crops', '22', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 2));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Pasture', '23', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 2));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Heterogeneous agricultural areas', '24', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 2));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Forests', '31', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 3));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Scrub and/or herbaceous associations', '32', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 3));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Open spaces with little or no vegetation', '33', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 3));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Inland wetlands', '41', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 4));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Marine wetlands', '42', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 4));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Inland waters', '51', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 5));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Marine waters', '52', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 5));
-- 
-- delete from `specchio_temp`.`temp_tax_table`;
-- insert into `specchio_temp`.`temp_tax_table`  (SELECT * FROM `specchio`.`taxonomy`);
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Continuous urban fabric', '111', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 11));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Discontinuous urban fabric', '112', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 11));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Industrial or commercial units', '121', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 12));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Road and rail networks and associated land', '122', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 12));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Port areas', '123', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 12));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Airports', '124', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 12));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Mineral extraction sites', '131', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 13));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Dump sites', '132', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 13));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Construction sites', '133', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 13));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Green urban areas', '141', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 14));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Port and leisure facilities', '142', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 14));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Non-irrigated arable land', '211', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 21));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Permanently irrigated land', '212', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 21));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Rice fields', '213', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 21));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Vineyards', '221', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 22));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Fruit trees and berry plantations', '222', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 22));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Olive groves', '223', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 22));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Pastures', '231', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 23));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Annual crops associated with permanent crops', '241', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 24));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Complex cultivation patterns', '242', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 24));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Mainly agricultural land with significant areas of natural vegetation', '243', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 24));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Agro-forestry areas', '244', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 24));
-- 
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Deciduous forest', '311', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 31));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Coniferous forest', '312', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 31));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Mixed forest', '313', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 31));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Natural grassland', '321', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 32));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Moors and heathland', '322', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 32));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Sclerophyllous vegetation', '323', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 32));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Transitional woodland-scrub', '324', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 32));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Beaches, dunes, sands', '331', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 33));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Bare rocks', '332', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 33));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Sparsely vegetated areas', '333', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 33));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Burnt areas', '334', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 33));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Glaciers and perpetual snow', '335', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 33));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Inland marshes', '411', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 41));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Peat bogs', '412', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 41));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Salt marshes', '421', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 42));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Salines', '422', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 42));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Intertidal flats', '423', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 42));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Water courses', '511', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 51));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Water bodies', '512', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 51));
-- 
-- 
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Coastal lagoons', '521', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 52));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Estuaries', '522', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 52));
-- INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover'), 'Sea and ocean', '523', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'CORINE Landcover') and code = 52));



--  Tram related metadata

INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) VALUES ('Tram Run', (select category_id from `specchio`.category where name = 'Generic Target Properties'), 'int_val', 'Refers to the number of a tram run within a sequence of runs');




--  db version
INSERT INTO `specchio`.`schema_info` (`version`, `date`) VALUES ('3.1', CURDATE());

