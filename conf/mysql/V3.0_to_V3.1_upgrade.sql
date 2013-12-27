--- calibration upgrade


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



