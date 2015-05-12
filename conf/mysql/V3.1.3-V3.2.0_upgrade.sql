-- instrument related metadata

INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) VALUES ('Instrument Temperature', (select category_id from `specchio`.category where name = 'Instrument Settings'), 'double_val', 'Internal temperature of the instrument');

-- atmosphere

INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) VALUES ('Atmospheric Water Content', (select category_id from `specchio`.category where name = 'Environmental Conditions'), 'double_val', 'Amount of water in the air column');

INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`) values('Centimeter', 'Centimeter', 'cm');

update attribute set default_unit_id = (select unit_id from `specchio`.`unit` where short_name = 'cm') where name = 'Atmospheric Water Content';


-- target or reference

INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('Target/Reference Designator', 'Defines if the measured object was a reference surface (e.g. white reference) or a target that is then compared to a reference.', (select category_id from `specchio`.`category` where name = 'Generic Target Properties'), 'taxonomy_id');

INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Target/Reference Designator'), 'Target', 'Target', 'Measurement of a target object');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Target/Target Designator'), 'Reference', 'Reference', 'Measurement of a reference object');



-- data links

INSERT INTO `specchio`.`category` (`name`, `string_val`, `cardinality`) VALUES ('Data Links', '', NULL);

ALTER TABLE `specchio`.`eav` ADD COLUMN `spectrum_id` INTEGER NULL DEFAULT NULL;
ALTER TABLE `specchio`.`eav` ADD CONSTRAINT `spectrum_id_fk` FOREIGN KEY `spectrum_id_fk` (`spectrum_id`) REFERENCES `spectrum` (`spectrum_id`);

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




INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('Target Data Link', 'Points to an other spectrum taken by a different instrument but of the same target or to a spectrum of the target if the current spectrum is a reference spectrum.', (select category_id from `specchio`.`category` where name = 'Data Links'), 'spectrum_id');
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('Reference Data Link', 'Points to a reference spectrum taken by the same instrument.', (select category_id from `specchio`.`category` where name = 'Data Links'), 'spectrum_id');
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('Provenance Data Link', 'Points to a spectrum used in calculating the current spectrum.', (select category_id from `specchio`.`category` where name = 'Data Links'), 'spectrum_id');

