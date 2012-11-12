<?php
/*
 * Created on 4/10/2007
 * ahueni
 */
 
 $site_title = "SPECCHIO Database Administrator Tools";

 // web server settings
$host = gethostname();
if (strncmp($host, "gsw1-dc10", 9) == 0) {  
	$server_address = $host . ".intersect.org.au";
} else {
	$server_address = "localhost";
}
$server_directory = "/specchio_web/specchio_admin";
	 
// mail settings
$admin_email = "nicholas@intersect.org.au";
	 
// db server settings
$port = 3306;
$db_server = "localhost";	 
$database="specchio"; // default database (master database)
	 
// schemas
$schemas = array("specchio");

  
$common_folder =  $_SERVER['DOCUMENT_ROOT'] . "$server_directory/common";
$common_folder_http =  "http://$server_address$server_directory/common";
 
?>
