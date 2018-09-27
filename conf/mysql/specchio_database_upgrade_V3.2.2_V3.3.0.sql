

-- Spatial field (requires MySQL version 5.5 or higher) for SPECCHIO spatial upgrade
ALTER TABLE `specchio`.`eav` add COLUMN `spatial_val` GEOMETRY default null;

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


-- Spatial Attributes
INSERT INTO `attribute`(`name`, `category_id`, `default_storage_field`, `description`, `cardinality`) VALUES ('Spatial Position', (select category_id from category where name = 'Location'), 'spatial_val', 'Spatial location of a spectrum in 2D space as latitude and longitude', 1);
INSERT INTO `attribute`(`name`, `category_id`, `default_storage_field`, `description`, `cardinality`) VALUES ('Spatial Extent', (select category_id from category where name = 'Location'), 'spatial_val', 'Spatial extent of a spectrum in 2D space as a polygon defined by vertices given as latitude and longitude', 1);
INSERT INTO `attribute`(`name`, `category_id`, `default_storage_field`, `description`, `cardinality`) VALUES ('Spatial Transect', (select category_id from category where name = 'Location'), 'spatial_val', 'Spatial extent of a spectrum in 2D space as a polyline defined by vertices given as latitude and longitude', 1);



-- Unit change for water content
INSERT INTO `specchio`.`unit`(`name`, `description`, `short_name`) values('Cubicmeter/Cubicmeter', 'Cubicmeter/Cubicmeter', 'm3/m3');

update attribute set default_unit_id = (select unit_id from `specchio`.`unit` where short_name = 'm3/m3') where name = 'Water Content 0_1 Bar';

-- Processing Algorithm Note
INSERT INTO `specchio`.`attribute`(`name`, `category_id`, `default_storage_field`, `description`) VALUES ('Processing Algorithm Note', (select category_id from `specchio`.category where name = 'Processing'), 'string_val', 'Notes produced by algorithm');

-- Longer blobs (>32 MB)
ALTER TABLE `specchio`.`eav` CHANGE COLUMN `binary_val` `binary_val` LONGBLOB;


-- Support for configurable categories for hierarchical application domains
CREATE TABLE `specchio`.`taxonomy_x_category` (
	`taxonomy_id` INT(10) REFERENCES `specchio`.`taxonomy`(`taxonomy_id`),
	`category_id` INT(10) REFERENCES `specchio`.`category`(`category_id`),
	PRIMARY KEY(`taxonomy_id`,`category_id`)
);

INSERT INTO `attribute`(`name`, `category_id`, `default_storage_field`, `description`, `cardinality`) VALUES ('Application Domain', (select category_id from category where name = 'General'), 'taxonomy_id', 'Defines application domains, which are used to control the default metaparameter categories per application', 1);

INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain'), 'Point Spectra', '');
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `description`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain'), 'Extracted Image Spectra', '');

DROP TABLE IF EXISTS `specchio_temp`.`temp_tax_table`;
CREATE TEMPORARY TABLE IF NOT EXISTS `specchio_temp`.`temp_tax_table` AS (SELECT * FROM `specchio`.`taxonomy`);

INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain'), 'Vegetation', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra'));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain'), 'Soil', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra'));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain'), 'Urban Space', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra'));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain'), 'SGCPs', 'Spectral Ground Control Points', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra'));

INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain'), 'Vegetation', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Extracted Image Spectra'));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain'), 'Soil', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Extracted Image Spectra'));
INSERT INTO `specchio`.`taxonomy` (`attribute_id`, `name`, `description`, `parent_id`) VALUES ((select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain'), 'Urban Space', '', (select taxonomy_id from `specchio_temp`.`temp_tax_table` where attribute_id = (select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Extracted Image Spectra'));


-- assign categories to domain taxonomies
	
-- definitions for 'Point Spectra'
	
-- definition for 'Vegetation'
insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Vegetation' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'General'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Vegetation' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Generic Target Properties'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Vegetation' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Campaign Details'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Vegetation' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Environmental Conditions'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Vegetation' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Instrument'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Vegetation' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Instrument Settings'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Vegetation' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Location'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Vegetation' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Optics'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Vegetation' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'PDFs'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Vegetation' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Pictures'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Vegetation' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Processing'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Vegetation' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Sampling Geometry'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Vegetation' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Sampling Scheme'));


-- definition for 'SGCPs'
insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'SGCPs' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'General'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'SGCPs' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Generic Target Properties'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'SGCPs' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Associated Campaigns'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'SGCPs' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Campaign Details'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'SGCPs' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Environmental Conditions'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'SGCPs' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Instrument'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'SGCPs' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Instrument Settings'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'SGCPs' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Location'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'SGCPs' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Optics'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'SGCPs' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'PDFs'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'SGCPs' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Pictures'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'SGCPs' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Processing'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'SGCPs' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Sampling Geometry'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'SGCPs' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'Sampling Scheme'));
		
-- definition for 'Soil'
insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Soil' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'General'));

-- definition for 'Urban Space'
insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Urban Space' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Point Spectra')), 
(select category_id from category where name = 'General'));


-- definitions for 'Extracted Image Spectra'

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Vegetation' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Extracted Image Spectra')), 
(select category_id from category where name = 'General'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Vegetation' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Extracted Image Spectra')), 
(select category_id from category where name = 'Generic Target Properties'));
	

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Soil' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Extracted Image Spectra')), 
(select category_id from category where name = 'General'));

insert into `specchio`.`taxonomy_x_category` (`taxonomy_id`, `category_id`) VALUES 
((select taxonomy_id from `specchio`.`taxonomy` where name = 'Urban Space' and parent_id in 
(select taxonomy_id from `specchio`.`taxonomy` where attribute_id = 
(select `attribute_id` from `specchio`.`attribute` where name = 'Application Domain') and name like 'Extracted Image Spectra')), 
(select category_id from category where name = 'General'));


-- correction of cardinality
update attribute set cardinality = null where name = 'Airborne Mission ID';
update attribute set cardinality = null where name = 'Basic Target Type';

-- db version
INSERT INTO `specchio`.`schema_info` (`version`, `date`) VALUES ('3.3', CURDATE());

