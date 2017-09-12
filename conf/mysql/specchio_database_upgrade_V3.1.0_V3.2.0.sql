-- instrument related metadata

INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) VALUES ('Instrument Temperature', (select category_id from `specchio`.category where name = 'Instrument Settings'), 'double_val', 'Internal temperature of the instrument');

-- atmosphere

INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) VALUES ('Atmospheric Water Content', (select category_id from `specchio`.category where name = 'Environmental Conditions'), 'double_val', 'Amount of water in the air column');

INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`) values('Centimeter', 'Centimeter', 'cm');

update attribute set default_unit_id = (select unit_id from `specchio`.`unit` where short_name = 'cm') where name = 'Atmospheric Water Content';


-- target or reference

INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('Target/Reference Designator', 'Defines if the measured object was a reference surface (e.g. white reference) or a target that is then compared to a reference.', (select category_id from `specchio`.`category` where name = 'Generic Target Properties'), 'taxonomy_id');

INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Target/Reference Designator'), 'Target', 'Target', 'Measurement of a target object');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Target/Reference Designator'), 'Reference', 'Reference', 'Measurement of a reference object');



-- data links

INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Data Links', '');

ALTER TABLE `specchio`.`eav` ADD COLUMN `spectrum_id` INTEGER NULL DEFAULT NULL;
ALTER TABLE `specchio`.`eav` ADD CONSTRAINT `spectrum_id_fk` FOREIGN KEY `spectrum_id_fk` (`spectrum_id`) REFERENCES `spectrum` (`spectrum_id`);

CREATE 
     OR REPLACE ALGORITHM = UNDEFINED 
    DEFINER = `root`@`localhost` 
    SQL SECURITY DEFINER
VIEW `specchio`.`eav_view` AS
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




INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `cardinality`) VALUES ('Target Data Link', 'Points to an other spectrum taken by a different instrument but of the same target or to a spectrum of the target if the current spectrum is a reference spectrum.', (select category_id from `specchio`.`category` where name = 'Data Links'), 'spectrum_id', NULL);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `cardinality`) VALUES ('Reference Data Link', 'Points to a reference spectrum taken by the same instrument.', (select category_id from `specchio`.`category` where name = 'Data Links'), 'spectrum_id', NULL);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `cardinality`) VALUES ('Provenance Data Link', 'Points to a spectrum used in calculating the current spectrum.', (select category_id from `specchio`.`category` where name = 'Data Links'), 'spectrum_id', NULL);



-- geochemistry update for CSIRO

INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `cardinality`) VALUES ('Depth', 'Distance from the Earth surface where sample was taken.', (select category_id from `specchio`.`category` where name = 'Location'), 'double_val', 1);


INSERT INTO `specchio`.`category` (`name`, `string_val`) VALUES ('Geochemistry', '');

INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`) values('Millimeter', 'Millimeter', 'mm');


INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Grain Size', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mm'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ag ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ag AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ag MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Al XRF', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Al AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Al MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('As ICP-M', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('As AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('As MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Au FA', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Au AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Au MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ba AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ba ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ba AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ba MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Be ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Be AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Bi ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Bi AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Bi MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ca XRF', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ca AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ca MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Cd ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Cd AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Cd MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ce ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ce AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ce MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Cl XRF', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Co ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Co AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Co MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Cr ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Cr AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Cr MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Cs ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Cs AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Cs MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Cu ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Cu AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Cu MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Dy ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Dy AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Dy MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Er ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Er AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Er MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Eu ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Eu AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Eu MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('F ISE', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('FeT XRF', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Fe AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Fe MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ga ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ga AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ga MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Gd ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Gd AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Gd MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ge ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ge AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Hf ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Hf AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Hg AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Hg MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ho ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ho AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('In AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('K XRF', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('K AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('K MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('La ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('La AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('La MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Li AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Li MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Lu ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Lu AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Mg XRF', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Mg AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Mg MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Mn XRF', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Mn AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Mn MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Mo ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Mo AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Mo MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Na XRF', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Na AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Nb ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Nb AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Nb MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Nd ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Nd AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Nd MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ni ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ni AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ni MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('P XRF', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('P MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Pb ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Pb AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Pb MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Pd FA', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Pd MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Pr ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Pr AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Pr MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Pt FA', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Pt MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Rb ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Rb AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Rb MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('S XRF', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sb ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sb AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sb MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sc ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sc AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sc MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Se AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Se MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Si XRF', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sm ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sm AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sm MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sn ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sn AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sn MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sr ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sr AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sr MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ta ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ta AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ta MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Tb ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Tb AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Tb MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Te AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Te MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Th ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Th AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Th MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ti XRF', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Ti MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Tl AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Tl MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Tm AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('U ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('U AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('U MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('V ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('V AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('V MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('W ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('W AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('W MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Y ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Y  AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Y MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Yb ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Yb MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Zn ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Zn AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Zn MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Zr ICP-MS', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Zr AR', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Zr MMI-ME', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('LOI Calc', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'mg/kg'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('FIELD pH', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'RAW'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('pH', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'RAW'), 1);
INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('EC', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where short_name = 'RAW'), 1);

INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('Sand', '', (select category_id from `specchio`.`category` where name = 'Geochemistry'), 'double_val', (select unit_id from `specchio`.`unit` where name = 'Percent'), 1);



-- remove unused fields and tables


alter table `specchio`.`spectrum` drop foreign key spectrum_ibfk_15;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `goniometer_id`;
DROP TABLE `specchio`.`goniometer`;

alter table `specchio`.`spectrum` drop foreign key spectrum_ibfk_14;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `target_homogeneity_id`;
DROP TABLE `specchio`.`target_homogeneity`;


alter table `specchio`.`spectrum` drop foreign key spectrum_ibfk_12;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `illumination_source_id`;
DROP TABLE `specchio`.`illumination_source`;

alter table `specchio`.`spectrum` drop foreign key spectrum_ibfk_11;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `sampling_environment_id`;
DROP TABLE `specchio`.`sampling_environment`;

alter table `specchio`.`spectrum` drop foreign key FK_spectrum_20;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `quality_level_id`;

alter table `specchio`.`spectrum` drop foreign key FK_spectrum_21;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `required_quality_level_id`;
DROP TABLE `specchio`.`quality_level`;

alter table `specchio`.`spectrum` drop foreign key spectrum_ibfk_10;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `measurement_type_id`;
DROP TABLE `specchio`.`measurement_type`;

alter table `specchio`.`spectrum` drop foreign key spectrum_ibfk_6;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `landcover_id`;
DROP TABLE `specchio`.`landcover`;

alter table `specchio`.`environmental_condition` drop foreign key FK_environmental_condition_3;
ALTER TABLE `specchio`.`environmental_condition` DROP COLUMN `cloud_cover_id`;
DROP TABLE `specchio`.`cloud_cover`;

alter table `specchio`.`environmental_condition` drop foreign key environmental_condition_ibfk_1;
ALTER TABLE `specchio`.`environmental_condition` DROP COLUMN `wind_direction_id`;
DROP TABLE `specchio`.`wind_direction`;

alter table `specchio`.`environmental_condition` drop foreign key environmental_condition_ibfk_2;
ALTER TABLE `specchio`.`environmental_condition` DROP COLUMN `wind_speed_id`;
DROP TABLE `specchio`.`wind_speed`;

alter table `specchio`.`spectrum` drop foreign key spectrum_ibfk_7;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `environmental_condition_id`;
DROP TABLE `specchio`.`environmental_condition`;

alter table `specchio`.`spectrum` drop foreign key spectrum_ibfk_13;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `foreoptic_id`;
DROP TABLE `specchio`.`foreoptic`;

alter table `specchio`.`spectrum` drop foreign key spectrum_ibfk_16;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `position_id`;
DROP TABLE `specchio`.`position`;

alter table `specchio`.`spectrum` drop foreign key spectrum_ibfk_8;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `sampling_geometry_id`;
DROP TABLE `specchio`.`sampling_geometry`;

ALTER TABLE `specchio`.`spectrum` DROP COLUMN `number`;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `file_comment`;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `date`;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `file_name`;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `internal_average_cnt`;
ALTER TABLE `specchio`.`spectrum` DROP COLUMN `loading_date`;

DROP TABLE `specchio`.`spectrum_x_target_type`;
DROP TABLE `specchio`.`target_type`;
DROP TABLE `specchio`.`target_category`;

DROP TABLE `specchio`.`spectrum_x_assoc_measurement`;
DROP TABLE `specchio`.`assoc_measurement`;

DROP TABLE `specchio`.`spectrum_x_spectrum_name`;
DROP TABLE `specchio`.`spectrum_name`;
DROP TABLE `specchio`.`spectrum_name_type`;

DROP TABLE `specchio`.`spectrum_x_instr_setting`;
DROP TABLE `specchio`.`instrument_setting`;
DROP TABLE `specchio`.`instr_setting_type`;

DROP TABLE `specchio`.`hierarchy_datalink`;

DROP TABLE `specchio`.`spectrum_x_picture`;
DROP TABLE `specchio`.`picture`;

DROP TABLE `specchio`.`spectrum_datalink`;
DROP TABLE `specchio`.`datalink_type`;



-- redefine spectrum view

USE `specchio`;
CREATE 
     OR REPLACE ALGORITHM = UNDEFINED 
    DEFINER = `root`@`localhost` 
    SQL SECURITY DEFINER
VIEW `spectrum_view` AS
    select 
        `spectrum`.`spectrum_id` AS `spectrum_id`,
        `spectrum`.`measurement_unit_id` AS `measurement_unit_id`,
        `spectrum`.`measurement` AS `measurement`,
        `spectrum`.`hierarchy_level_id` AS `hierarchy_level_id`,
        `spectrum`.`sensor_id` AS `sensor_id`,
        `spectrum`.`file_format_id` AS `file_format_id`,
        `spectrum`.`campaign_id` AS `campaign_id`,
        `spectrum`.`instrument_id` AS `instrument_id`,
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




-- db version
INSERT INTO `specchio`.`schema_info` (`version`, `date`) VALUES ('3.2', CURDATE());




