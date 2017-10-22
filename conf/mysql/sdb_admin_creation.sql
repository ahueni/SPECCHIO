-- sdb_admin creation
-- 09.12.2007, ahueni
-- 09.01.2008, ahueni : added the old_password update, added flush privileges
-- 14.01.2007, ahueni : added the insert statement for the specchio_user table
-- 25.05.2009, ahueni : update of the GRANTS, deactivated the INSERT into the specchio_user table
-- 02.-0.2010, ahueni : added GRANT SUPER ON *.* TO sdb_admin;, TRIGGER for MySQL 5.1.6
-- 01.03.2013, nsheppard : updated for SPECCHIO V3
-- 26.01.2015, ahueni : removed insert of sdb_admin into specchio_user_group as already contained in DB dump


-- Set the admin user name and password here - make sure the username and password is the same on each line!
CREATE USER 'sdb_admin'@'localhost' IDENTIFIED BY 'sdb_admin_password';
INSERT INTO `specchio`.`specchio_user` (`user`, `first_name`, `last_name`, `email`, `admin`, `password`)
	VALUES ('sdb_admin', 'SPECCHIO', 'Administrator', '', 1, MD5('sdb_admin_password'));
-- INSERT INTO `specchio`.`specchio_user_group` VALUES('sdb_admin', 'admin');


-- Grant administrator privileges
GRANT SELECT, DELETE, INSERT, UPDATE, ALTER, DROP, CREATE, CREATE VIEW, GRANT OPTION, TRIGGER, REFERENCES ON `specchio`.* TO 'sdb_admin'@'localhost';
GRANT SELECT, DELETE, INSERT, UPDATE, DROP, CREATE TEMPORARY TABLES, GRANT OPTION ON `specchio_temp`.* TO 'sdb_admin'@'localhost';
GRANT SUPER, CREATE USER ON *.* TO 'sdb_admin'@'localhost';
GRANT INSERT ON `mysql`.`user` TO 'sdb_admin'@'localhost';
UPDATE `mysql`.`user`
	SET `Reload_priv`='Y', `Process_priv`='Y', `Update_priv`='Y', `Delete_priv`='Y', `Select_priv`='Y'
	WHERE `user`='sdb_admin' AND `host`='localhost';
FLUSH PRIVILEGES;
