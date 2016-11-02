-- UTC Time Attribute

INSERT INTO `attribute`(`name`, `category_id`, `default_storage_field`, `description`) VALUES ('Acquisition Time (UTC)', (select category_id from category where name = 'General'), 'datetime_val', 'UTC time of data capture');

INSERT INTO `attribute`(`name`, `category_id`, `default_storage_field`, `description`, `cardinality`) values('UTC Time Computation', (select category_id from category where name = 'Processing'), 'string_val', 'Notes produced by the SPECCHIO UTC computation routine', NULL);

-- Waypoint ID Attribute

INSERT INTO `attribute`(`name`, `category_id`, `default_storage_field`, `description`) VALUES ('Waypoint ID', (select category_id from category where name = 'Location'), 'string_val', 'Name or ID of a waypoint');



-- db version
INSERT INTO `specchio`.`schema_info` (`version`, `date`) VALUES ('3.2.1', CURDATE());

