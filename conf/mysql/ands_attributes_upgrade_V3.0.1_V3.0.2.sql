--
-- Additional ANDS attributes introduced in SPECCHIO V3.0.2
--

INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('ANDS Collection Name', (select category_id from `specchio`.`category` where name = 'Data Portal'), 'string_val', (select unit_id from `specchio`.`unit` where short_name = 'String'), NULL);
INSERT INTO `specchio`.`attribute` (`name`, `category_id`, `default_storage_field`, `default_unit_id`, `cardinality`) VALUES ('ANDS Collection Description', (select category_id from `specchio`.`category` where name = 'Data Portal'), 'string_val', (select unit_id from `specchio`.`unit` where short_name = 'String'), NULL);