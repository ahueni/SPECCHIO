<?php
/*
 * Created on 4/10/2007
 * ahueni
 */
 
 $host = 2; // environment switch
 $site_title = "SPECCHIO Database Administrator Tools";
 
 if($host == 0)
 {
	 
	 // web server settings
	 $server_address = "localhost";
	 $server_directory = "/specchio_web/specchio_admin";
	 
	 // mail settings
	 $admin_email = "ahueni@geo.uzh.ch";
	 
	 // db server settings
	 $port = 3306;
	 $db_server = "specchio.geo.uzh.ch";	 
	 $database="specchio"; // default database (master database)
	 
	 // schemas
	 $schemas = array("specchio");

 }
 
if($host == 1)
 {
	 
	 // web server settings
	 $server_address = "specchio.geo.uzh.ch";
	 $server_directory = "/prod/specchio_admin";
	 
	 // mail settings
	 $admin_email = "ahueni@geo.uzh.ch";
	 
	 // db server settings
	 $port = 3306;
	 $db_server = "specchio.geo.uzh.ch";	 
	 $database="specchio"; // default database (master database)
	 
	 // schemas
	 $schemas = array("specchio");

 }
 
 if($host == 2)
 {
	 
	 // web server settings
	 $server_address = "localhost";
	 $server_directory = "/specchio_web/specchio_admin";
	 
	 // mail settings
	 $admin_email = "ahueni@geo.uzh.ch";
	 
	 // db server settings
	 $port = 3306;
	 $db_server = "localhost";	 
	 $database="specchio"; // default database (master database)
	 
	 // schemas
	 $schemas = array("specchio");

 }
 
 
 
 $common_folder =  $_SERVER['DOCUMENT_ROOT'] . "$server_directory/common";
 $common_folder_http =  "http://$server_address$server_directory/common";
 
?>
