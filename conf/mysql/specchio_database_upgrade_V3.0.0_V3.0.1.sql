-- 
-- Upgrade a SPECCHIO database frm V3.0.0 to V3.0.1
-- 

-- add researcher description field
ALTER TABLE `specchio`.`specchio_user` ADD COLUMN `description` VARCHAR(250);

-- db version
INSERT INTO `specchio`.`schema_info` (`version`, `date`) VALUES ('3.01', CURDATE());

