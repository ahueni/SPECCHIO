<?php
/*
 * Created on 4/10/2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
 
 //include "config.php";
 
  function quote($str)
  {
  	$res = "\"".$str."\"";
  	return $res;
  }
  
  function quote_c($str)
  {
  	return "\"$str\", ";
  }
  
  function fk_value($id)
  {
  	// return "null" for zero ids, otherwise return the value of $id
  	if($id == 0)
  	{
  		return "null";
  	}
  	else
  	{
  		return $id;
  	}
  }
  
  function varchar_value($val)
  {
  	if($val == "")
  	{
  		return "null";
  	}
  	else
  	{
  		return $val;
  	}
  }
  
  function print_nl($value)
  {
  	print($value);
  	print("<br>");
  }
  
  
    function print_menu_item($file, $desc, $page_id)
    {

    	global $server_address, $server_directory;
   		global $global_page_id;
   		
    	if($global_page_id == $page_id)
    	{
    		print("<a class=\"menu2\"");
    	}
    	else
    	{
    	   print("<a class=\"menu1\"");
    	}
    	print("href= \"http://$server_address$server_directory/$file\" >$desc</a>");   
    }
  
    function print_file_link($file, $desc)
    {
    	global $server_address, $server_directory;
    	print("<a ");
    	print("href= \"http://$server_address$server_directory/$file\" >$desc</a>"); 
    }
  
  function connect_to_specchio()
  {
  	global $db_server, $port, $database;
	global $server_address, $server_directory, $db_admin, $db_admin_pw;  
	
	//print "connect without cookies";
	
	// try to read the name from the cookie
	if (isset($_COOKIE['admin_pw']) && isset($_COOKIE['admin_name']))
	{   
		$db_admin = $_COOKIE['admin_name']; 
		$db_admin_pw = $_COOKIE['admin_pw'];
	}
  
  	db_connect($db_server, $port, $database, $db_admin, $db_admin_pw);
  }
  
  function connect_to_specchio_with_cookies()
  {
 	global $db_server, $port, $database;
	global $server_address, $server_directory; 
	
	//print_nl( "connect with cookies");
	
  	if (isset($_COOKIE['admin_pw']) && isset($_COOKIE['admin_name']))
	{   
	    //print_nl ("cookies are set");
		$db_admin = $_COOKIE['admin_name']; 
		$db_admin_pw = $_COOKIE['admin_pw'];
	}
	else
	{
		print("<script language=\"Javascript\">");
		print("alert(\"You are currently not logged in! The function you selected is not available\");");
		print("location.href = \"http://$server_address$server_directory/index.php\"");
		print("</script>");		
	}	 
  
	db_connect($db_server, $port, $database, $db_admin, $db_admin_pw);
  
  }
  
  function db_connect($db_server, $port, $database, $db_admin, $db_admin_pw)
  {
  	
	//echo ("connecting ...");
	//$link = mysql_connect("db.specchio.ch:4406",$username,$password);
	$conn_str = "$db_server:$port";
	//print_nl ($conn_str);
	//print_nl ($db_admin);
	//print_nl ($db_admin_pw);
	$link = mysql_connect("$db_server:$port",$db_admin,$db_admin_pw);
	
	
	if (!$link) {
	    die('Could not connect to database: ' . mysql_error());
	}
	
	@mysql_select_db($database) or die( "Unable to select database");
	
	// this makes sure that the umlauts are transfered correctly (see http://php.group.stumbleupon.com/forum/37465/)
	// at least we hope so ....
	mysql_query("SET NAMES 'utf8';");
	mysql_query("SET CHARACTER SET 'utf8';");
	
	
  } 
  
  // create new database user and grant all needed rights
  function create_user_and_grant_rights($user, $pw)
  {
  	global $schemas;

  	// create new database user by a grant statement
  	$query = "GRANT select, insert, update, delete ON $db_server.campaign_view to '$user' identified by '$pw'";
  	// GRANT does not work on speccio.geo.uzh.ch
  	//print ($db_server);
  	//$query = "USE $db_server";
  	//print ($query);
    //$result = mysql_query($query);
	//if (!$result) {
	//    die('Invalid query: ' . mysql_error());
	//}
  	//$query = "CREATE USER '$user'@'%' identified by '$pw'";
  	
  	//print ($query);
  	
  	$result = mysql_query($query);
	if (!$result) {
	    die('Invalid query: ' . mysql_error());
	}
	
	// update the user entry to support old clients (see http://dev.mysql.com/doc/refman/5.0/en/old-client.html)
	$query = "UPDATE mysql.user SET Password = OLD_PASSWORD('$pw') WHERE Host = '%' AND User = '$user'";
	
	//print ($query);
	
  	$result = mysql_query($query);
	if (!$result) {
	    die('Invalid user update query: ' . mysql_error());
	}
	
	// set the privileges in the server  .....
	$query = "FLUSH PRIVILEGES";
	
  	$result = mysql_query($query);
	if (!$result) {
	    die('Invalid query: ' . mysql_error());
	}		
	
	grant_rights($user, $schemas);
		 	
  }
  
  function grant_rights($user, $schemas)
  {
  	
  	// define views to be granted
	$views = array(
		"campaign_view",
		"hierarchy_level_view",
		"hierarchy_datalink_view",
		"spectrum_datalink_view",
		"position_view",
		"environmental_condition_view",
		"sampling_geometry_view",
		"spectrum_x_spectrum_name_view",
		"spectrum_name_view",
		"spectrum_x_target_type_view",
		"picture_view",
		"spectrum_x_picture_view",
		"spectrum_x_instr_setting_view",
		"instrument_setting_view",
		"assoc_measurement_view",
		"spectrum_x_assoc_measurement_view",
		"spectrum_x_eav_view",
		"eav_view",
		"hierarchy_level_x_spectrum_view"
	);
	
	foreach ($views as $i => $view) 
	{   
		// grant on all schemas
		foreach($schemas as $j => $schema)
		{
			$query = "GRANT SELECT, INSERT, UPDATE, DELETE ON $schema.$view TO '$user'";
			//print("<br>");   
			//print($query); 
			
			$result = mysql_query($query);
			if (!$result) {
			    die('Invalid query: ' . mysql_error());
			}			
			
		}


	}
  	  	
  	// define tables to be granted
  	$tables = array("campaign",
		"hierarchy_level",
		"hierarchy_datalink",
		"spectrum_datalink",
		"spectrum",
		"position",
		"environmental_condition",
		"sampling_geometry",
		"spectrum_x_spectrum_name",
		"spectrum_name",
		"spectrum_x_target_type",
		"picture",
		"spectrum_x_picture",
		"specchio_user",
		"institute",
		"quality_level",
		"datalink_type",
		"target_homogeneity",
		"file_format",
		"landcover",
		"wind_speed",
		"wind_direction",
		"measurement_unit",
		"measurement_type",
		"illumination_source",
		"sampling_environment",
		"spectrum_name_type",
		"target_type",
		"target_category",
		"sensor",
		"sensor_element",
		"sensor_element_type",
		"instrument",
		"calibration",
		"cloud_cover",
		"goniometer",
		"foreoptic",
  		"manufacturer",
  		"instrument_x_picture",
  	  	"reference_x_picture",  
  		"reference",
  		"reference_brand",
  		"schema_info",	
  		"country",
  		"instrumentation_picture",
  		"instrumentation_factors",
  		"spectrum_x_instr_setting",
  		"instrument_setting",
  		"instr_setting_type",
  		"assoc_measurement",
  		"spectrum_x_assoc_measurement",
  		"unit",
  		"category",
  		"attribute",
  		"eav",
  		"spectrum_x_eav",
  		"hierarchy_level_x_spectrum"
  	    );
  	
	foreach ($tables as $i => $table) 
	{
 		foreach($schemas as $j => $schema)
		{
 			$query = "GRANT SELECT ON $schema.$table TO '$user'";
		
			//print("<br>");   
			//print($query); 	
			
			$result = mysql_query($query);
			if (!$result) {
			    die('Invalid query: ' . mysql_error());
			}
		}
	}
	
	
	// special grants for spectrum table
	foreach($schemas as $j => $schema)
	{
		$query = "grant delete on $schema.spectrum_view TO '$user'";
		$result = mysql_query($query);
		if (!$result) {
			die('Invalid query: ' . mysql_error());
		}	
		$query = "grant select, insert, update (spectrum_id,goniometer_id,target_homogeneity_id,foreoptic_id,illumination_source_id,sampling_environment_id,measurement_type_id,measurement_unit_id,sampling_geometry_id,environmental_condition_id,position_id,landcover_id,number,measurement,file_comment,date,file_name,internal_average_cnt,hierarchy_level_id,sensor_id,file_format_id,campaign_id,instrument_id,loading_date,reference_id,required_quality_level_id,quality_level_id) ON $schema.spectrum_view to '$user'";
		$result = mysql_query($query);
		if (!$result) {
			die('Invalid query: ' . mysql_error());
		}	
		$query = "grant select (is_reference) ON $schema.spectrum_view TO '$user'";
		$result = mysql_query($query);
		if (!$result) {
			die('Invalid query: ' . mysql_error());
		}	
	}	
  	
  	
  }
 
 function create_password()
 {
 	$pw = generatePassword(6, 2);
 	
 	//print $pw;
 	
 	return $pw;
 }
 
 
 function generatePassword($length=9, $strength=0) {
    $vowels = 'aeuy';
    $consonants = 'bdghjmnpqrstvz';
    if ($strength & 1) {
        $consonants .= 'BDGHJLMNPQRSTVWXZ';
    }
    if ($strength & 2) {
        $vowels .= "AEUY";
    }
    if ($strength & 4) {
        $consonants .= '23456789';
    }
    if ($strength & 8) {
        $consonants .= '@#$%';
    }

    $password = '';
    $alt = time() % 2;
    for ($i = 0; $i < $length; $i++) {
        if ($alt == 1) {
            $password .= $consonants[(rand() % strlen($consonants))];
            $alt = 0;
        } else {
            $password .= $vowels[(rand() % strlen($vowels))];
            $alt = 1;
        }
    }
    return $password;
}
 
 // user name check is carried out on the productive system
 // We assume that as the users are created via this interface only, the users in the prod
 // and test system are always identical.
 function check_name($username)
 {
 	// return 1 if the username already exists, 0 otherwise
 	// use utf8 encoding in order to handle 'umlaut' names
 	//$query = "select count(*) from specchio_user where user = " . quote(utf8_encode($username));
 	$query = "select count(*) from specchio_user where user = " . quote($username);
 	
    //print_nl($query);
 	
 	$result = mysql_query($query);
	if (!$result) {
	    die('Invalid query: ' . mysql_error());
	}
	
	$num=mysql_numrows($result);
	
	$i = 0;
	
	while ($i < $num)
	{
	 //print_nl("fetch");	
	
	 $count=mysql_result($result,$i,0);
	 $i++;
	}
	
	//print ("no of names in db: ");
	//print_nl($count);
	
	if($count > 0)
	{
		return 1;
	}
	else
	{
		return 0;
	}
 	
 } 
 

  function create_username($first_name, $last_name)
  {
 	// take the first character of the first name and the maximal first 6 
 	// characters of the last name
 	//print_nl("input: ", $last_name);

 	$tmp1 = $first_name;
 	$tmp2 = $last_name;//utf8_decode($last_name);
 	$username = substr($tmp1,0,1) . FixUtf8BrokenString(substr($tmp2,0,6));
 	//print_nl(substr($tmp1,0,1));
 	//print_nl($tmp2);
 	//print_nl(utf8_encode((substr($tmp2,0,6))));
 	//print("substr concat: ");
 	//print_nl($username);
 
 	// as long as the username is not unique keep on adding characters from the last name
 	$i = 6;
//      print(strlen($last_name));
 	while (check_name($username) == 1 && strlen($tmp2) >= $i)
 	{
 		$username = $username . substr($tmp2,$i,1);
            //print_nl($username);
            //print_nl(strlen($tmp2));

            $i = $i + 1;
 	}
 	
 	$username = FixUtf8BrokenString($username);
 	
 	// if still not unique after the whole last name is added start to 
 	// concatenate with a numerical index 
      if(check_name($username) == 1)
      {
        $n = 1;
        $temp = $username;
        //print_nl("add numerical ending");
  	    while (check_name($temp) == 1)
 	    {
 	  	  $temp = $username . "_" . $n;
		  //print_nl($temp);
          $n = $n + 1;
 	    }
 	    $username = $temp;
      }
 	
 	return $username;
  }
  
  
  function send_email($user_name, $pw, $email)
  {
  	//include "config.php";
  	global $schemas;
  	global $db_server, $port, $database;
  	global $admin_email;
  	
  	// create schema string
  	$schema_str = "";
  	foreach($schemas as $j => $schema)
	{
		if($schema_str == "")
		{
			$schema_str = $schema;
		}
		else
		{
			$schema_str = $schema_str . " and " . $schema;
		}	
	}
  	

	$subject = "SPECCHIO user account";
	$body = "Thank you for registering as a SPECCHIO user.\nYour user name is: " . 
			utf8_decode($user_name) . "\n" .
			"Your password is: $pw \n\n" .
			"The above account provides access to the productive and the test databases " .
			"($schema_str) on $db_server.\n" . 
			"Please use the test database for all your tests, including the tutorial sessions.\n" .
			"For more information please refer to the SPECCHIO user guide. \n\n" .
			"Have a nice day.\nThe SPECCHIO team\n\nThis is an automated email. Please do not reply.";
	$headers = "From: $admin_email\n";
	
	if(mail($email,$subject,$body,$headers))
	{
	 echo "Thank you for registering as a SPECCHIO user.<br>";
	 echo "An email with your user name and password was sent to $email";
	 //echo  "Your user name is: " . utf8_decode($user_name) . "\n" . "Your password is: $pw \n\n";
	 echo "<br><br>Have a nice day. <br> <br> The SPECCHIO team";
	 }
	 else
	 {
	 echo "Problem sending mail! Please try registering again!";
	 }
	 
	echo "<br>";
  }

  function notify_admin($first_name, $last_name, $email, $ip)
  {
  	global $schemas;
  	global $db_server, $port, $database, $admin_email;
  	
  	
  	$subject = "New user added to SPECCHIO";
	$body = "The new user " . utf8_decode($first_name) . " " . utf8_decode($last_name) . ", email: " . utf8_decode($email) . ", IP: " . utf8_decode($ip) . " has been added to the SPECCHIO database on $db_server.\n\n" .
			"Have a nice day.\nThe SPECCHIO team\n\nThis is an automated email. Please do not reply.";
	$headers = "From: $admin_email\n";
	
	if(mail($admin_email,$subject,$body,$headers))
	{
	 }
	 else
	 {
	 echo "Internal problem sending mail. (of no concern to the online user)";
	 }
	 
	echo "<br>";
  }

  
function create_user_account($schemas, $utitle, $first_name, $last_name, $uemail, $uinstitute, $uwww)
{
	global $database;
	//global $schemas;
 	// account for empty strings and zero ids
 	$uinstitute = fk_value($uinstitute);
 	$uwww = varchar_value($uwww);
 
 	// connect to database
 	connect_to_specchio();
 		
	// create user name
    //$user  = "dummy";
    $user = create_username($first_name, $last_name);

    
    // create an automatic password
    $pw = create_password();
 	//print ($pw);
 	
 	//print ("<br>");
// 	print("user name: ") ;
// 	print_nl($user) ;
 	//print ("<br>");

	
 	// create insert statement
  		foreach($schemas as $j => $schema)
		{

			// insert specchio user
		 	$query = "insert into $schema.specchio_user (user, first_name, last_name, title, email, www, institute_id, password) " .
					"values (" . quote_c($user) . quote_c($first_name) . quote_c($last_name) .
		            quote_c($utitle) . quote_c($uemail) . quote_c($uwww) .
		            "(select institute_id from $schema.institute where name = (select name from " .
		            "$database.institute where institute_id = " . $uinstitute . ") limit 1)" . 
		 			", md5(" . quote($pw) . ")" . ")";
			//print_nl($query);	
		    if(1==1)
		  	{
		    	$result = mysql_query($query);
		    	if (!$result) {
		       		die('Invalid query: ' . mysql_error());
		    	}
		  	}
		  	
		  	// insert specchio user group
		  	$query = "insert into $schema.specchio_user_group (user, group_name) values (" . quote_c($user) . quote("user") . ")";
		    if(1==1)
		  	{
		    	$result = mysql_query($query);
		    	if (!$result) {
		       		die('Invalid query: ' . mysql_error());
		    	}
		  	}
		  	
		}
    
    print ("<br>");
 	
 	// get the IP
 	//$ip=@$REMOTE_ADDR; 
 	$ip=$_SERVER['REMOTE_ADDR'];
    
    // create database user and grant the necessary rights
    create_user_and_grant_rights($user, $pw);
    
    // create an email to inform the user of the creation of the account 
 	send_email($user, $pw, $uemail);
 	
 	notify_admin($first_name, $last_name, $uemail, $ip);

}


 function check_institute($name)
 {
 	// return 1 if the institute already exists, 0 otherwise
 	$query = "select count(*) from institute where name = " . quote($name);
 	
 	//print_nl ($query);
 	
 	$result = mysql_query($query);
	if (!$result) {
	    die('Invalid query: ' . mysql_error());
	}
	
	$num=mysql_numrows($result);
	
	$i = 0;
	
	while ($i < $num)
	{
	 $count=mysql_result($result,$i,0);
	 $i++;
	}

	if($count == 0)
	{
		return 0;
	}
	else
	{
		return 1; // institute already exists
	}
	   
	   
 }
 
  function notify_admin_of_new_institute($name)
  {
	global $admin_email;
  	$subject = "New institute added to SPECCHIO";
	$body = "The institute $name has been added to the SPECCHIO database.\n\n" .
			"Have a nice day.\nThe SPECCHIO team\n\nThis is an automated email. Please do not reply.";
	$headers = "From: specchio@geo.uzh.ch\n";
	
	if(mail($admin_email,$subject,$body,$headers))
	{
	 }
	 else
	 {
	 echo "Internal problem sending mail. (of no concern to the online user)";
	 }
	 
	echo "<br>";
  }
 
  
  function create_new_institute($name, $dept, $street, $street_no, $po_code, $city, $www, $country_id)
  {
  	global $schemas, $server_address, $server_directory;
 
 	// connect to database
 	connect_to_specchio();

	$country_id = fk_value($country_id);
	
	// check if institute already exists
	if(check_institute($name) == 1)
	{
		print_nl("Institute of the same name already exists!");
		print_nl("");
		print_nl("<a href=\"javascript:history.back()\">Click here to return to form and change the institute details.</a>");		
	}
	else
	{
		
		
	 	foreach($schemas as $j => $schema)
		{
	
		 	// create insert statement
 		$query = "insert into $schema.institute (name, department, street, street_no, po_code, city, country_id, www) " .
			"values (" . quote_c($name) . quote_c($dept) . quote_c($street) .
            quote_c($street_no) . quote_c($po_code) . quote_c($city) .
            quote_c($country_id) . quote($www) . ")";
	
		//print($query);	
		
	    // insert institute
	    if(1==1)
	    {
	      $result = mysql_query($query);
	    	if (!$result) {
	       		die('Invalid query: ' . mysql_error());
	    	}
    	}
	}
    
    notify_admin_of_new_institute($name);
    
    print_nl("New institute added to database.");
    print_nl("");

   	
  }
  
  print_nl("<a href= \"http://$server_address$server_directory/specchio_user_account_form.php\">Return to user account form.</a><br>"); 
 
  }
  
 
 function FixUtf8BrokenString($Desc)
{
 // UTF-8 encoding
 // bytes : representation
 // 1     : 0bbbbbbb
 // 2    : 110bbbbb 10bbbbbb
 // 3     : 1110bbbb 10bbbbbb 10bbbbbb
 // 4     : 11110bbb 10bbbbbb 10bbbbbb 10bbbbbb
 // to see if a string is broken in middle of a utf8 char
 // we search for last byte encoding size of utf-8 char
 // if number of last bytes in string is lower than encoding size
 // we remove those last bytes

 // if last byte is ord < 128, ok. we return !
 if (ord($Desc[strlen($Desc) - 1]) < (0x80))
   return $Desc;

 // loop for finding byte encoding size
 $nbbytes = 1;
 while (ord($Desc[strlen($Desc) - $nbbytes]) > 0x7F)
 {
   if (ord($Desc[strlen($Desc) - $nbbytes]) > 0xBF)
     break;
   $nbbytes++;
 }

  // check if byte encoding size is encoding a size of 4 bytes
  if ((ord($Desc[strlen($Desc) - $nbbytes]) > 0xF0) && ($nbbytes == 4))
    return $Desc;
  // check if byte encoding size is encoding a size of 3 bytes
  if ((ord($Desc[strlen($Desc) - $nbbytes]) > 0xE0) && ($nbbytes == 3))
    return $Desc;
  // check if byte encoding size is encoding a size of 2 bytes
  if ((ord($Desc[strlen($Desc) - $nbbytes]) > 0xC0) && ($nbbytes == 2))
    return $Desc;
  // then this is the case where string is badly broken, we remove last bytes
  return substr($Desc, 0, -$nbbytes);
}

  
 
?>
