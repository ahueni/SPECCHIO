
-- vegetation specific updates
update specchio.attribute set name = 'Crown Relative Position', description = 'FPMRIS (DELWP) SOP 13 Measuring a Large Tree Plot' where name like 'Crown Class (FPMRIS)';


INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`, `default_unit_id`) VALUES ('Neoxanthin', (select category_id from `specchio`.category where name = 'Vegetation Biophysical Variables'), 'double_val', 'Carotenoid and xanthophyll pigment', (select unit_id from unit where short_name like 'ugrams/cm2'));
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`, `default_unit_id`) VALUES ('Violaxanthin', (select category_id from `specchio`.category where name = 'Vegetation Biophysical Variables'), 'double_val', 'Carotenoid and xanthophyll pigment', (select unit_id from unit where short_name like 'ugrams/cm2'));
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`, `default_unit_id`) VALUES ('Antheraxanthin', (select category_id from `specchio`.category where name = 'Vegetation Biophysical Variables'), 'double_val', 'Carotenoid and xanthophyll pigment', (select unit_id from unit where short_name like 'ugrams/cm2'));
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`, `default_unit_id`) VALUES ('Lutein', (select category_id from `specchio`.category where name = 'Vegetation Biophysical Variables'), 'double_val', 'Carotenoid and xanthophyll pigment', (select unit_id from unit where short_name like 'ugrams/cm2'));
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`, `default_unit_id`) VALUES ('Zeaxanthin', (select category_id from `specchio`.category where name = 'Vegetation Biophysical Variables'), 'double_val', 'Carotenoid and xanthophyll pigment', (select unit_id from unit where short_name like 'ugrams/cm2'));
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`, `default_unit_id`) VALUES ('B-Carotene', (select category_id from `specchio`.category where name = 'Vegetation Biophysical Variables'), 'double_val', 'Carotenoid pigment', (select unit_id from unit where short_name like 'ugrams/cm2'));
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) VALUES ('EPS', (select category_id from `specchio`.category where name = 'Vegetation Biophysical Variables'), 'double_val', 'Epoxidation state of xanthophyll cycle pigments: (V+0.5*A)/(V+A+Z)');

update specchio.attribute set default_unit_id  = (select unit_id from unit where short_name like 'm') where name like 'Approx. Crown Diameter';


-- scale updates

update category set name = 'Sampling Design' where name like 'Sampling Scheme';

INSERT INTO `specchio`.`attribute`(`name`, `description`, `category_id`, `default_storage_field`) VALUES ('Vegetation Sampling Scale', '', (select category_id from `specchio`.`category` where name = 'Sampling Design'), 'taxonomy_id');

INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Vegetation Sampling Scale'), 'Leaf', '', 'Measurements at single leaf scale');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Vegetation Sampling Scale'), 'Plant', '', 'Measurements at single plant scale');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `code`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Vegetation Sampling Scale'), 'Stand', '', 'Measurements at stand scale, i.e. encompassing several plants');


-- default unit updates and fixes
INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`) values('Seconds', 'Seconds', 's');
update specchio.attribute set default_unit_id  = (select unit_id from unit where short_name like 's') where name like 'Time since last DC';


-- blob data type update
CREATE TABLE `specchio`.`blob_data_type`(
	`blob_data_type_id` INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`data_type_name` CHAR(30)
);

insert into `specchio`.`blob_data_type` (data_type_name) VALUES ('PDF');
insert into `specchio`.`blob_data_type` (data_type_name) VALUES ('Image');

ALTER TABLE `specchio`.`attribute` ADD COLUMN `blob_data_type_id` INT(10);
ALTER TABLE `specchio`.`attribute` ADD CONSTRAINT `blob_data_type_id_fk` FOREIGN KEY `blob_data_type_id_fk` (`blob_data_type_id`) REFERENCES `blob_data_type` (`blob_data_type_id`);

update `specchio`.`attribute` set blob_data_type_id = (select blob_data_type_id from blob_data_type where data_type_name = 'Image') where name like '%Picture';
update `specchio`.`attribute` set blob_data_type_id = (select blob_data_type_id from blob_data_type where data_type_name = 'PDF') where name like 'Field Protocol';
update `specchio`.`attribute` set blob_data_type_id = (select blob_data_type_id from blob_data_type where data_type_name = 'PDF') where name like 'Experimental Design';


-- db version
INSERT INTO `specchio`.`schema_info` (`version`, `date`) VALUES ('3.31', CURDATE());
