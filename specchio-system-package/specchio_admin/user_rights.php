<?php
include "common_includes.php";
include "$common_folder/header.php";	
$global_page_id = 7;
include "$common_folder/main_container_start.php";	
?>

    <h2 align=left>   User Right Grants <br> </h2>
    
    This page first lists all users in this database and their respective access rights on all specchio database schemas on this database server.
 
    <br><br>
    
 <table class="labelled">
   

 <tr><td class="label_back">
    User Name 
    </td>
    <td class="label_back">
    SPECCHIO schemas with access rights.
  </td>
  
  </tr>
  
  
      <?php
	/*
	 * get the campaign data
	 * 
	 */

	//include "db_and_common_functions.php";
 	// connect to database
 	connect_to_specchio_with_cookies();
	
 	// get all schemata which are SPECCHIO schemas
	$query = "SELECT table_schema FROM information_schema.`TABLES` T where table_name = 'specchio_user'";
	//print($query);
	$result = mysql_query($query);
	if (!$result) {
	    die('Invalid query: ' . mysql_error());
	}
	
	$num=mysql_numrows($result);
	
	$i = 0;
	$specchio_schemas = array();
	
	while ($i < $num)
	{
		$specchio_schemas[] = mysql_result($result,$i,"table_schema");	
		$i++;
	}
	
	//print_r ($specchio_schemas);	

 	//mysql_close();
 	
 	// get all users on this database server
	$query = "select user from mysql.user where user != 'root' order by user";
	$result = mysql_query($query);
	if (!$result) {
	    die('Invalid query: ' . mysql_error());
	}
	
	$num=mysql_numrows($result);
	
	$i = 0;
	$users = array();
	
	while ($i < $num)
	{
		$users[] = mysql_result($result,$i,"user");	
		$i++;
	}	
	
 	//mysql_close();
 	
	//print_r ($users);
 	
 	// get the schemata for ever user
 	$no_of_users = count($users);
	for ($u = 0; $u < $no_of_users; $u++) 		
	{
		
		print("<tr><td class=\"label_back\">");
    	print($users[$u]); 
    	print("</td>");
		print("<td class=\"label_back\">");
		
	 	$count = count($specchio_schemas);
	 	$access_cnt = 0;
		for ($s = 0; $s < $count; $s++) 		
		{
			$query = "select user from mysql.user u where u.user in (select user from $specchio_schemas[$s].specchio_user) and user = '$users[$u]' and user in (select user from mysql.tables_priv where Db = '$specchio_schemas[$s]')";
			
			//print ($query);
			
			$result = mysql_query($query);
			if (!$result) {
			    die('Invalid query: ' . mysql_error());
			}
			
			$num=mysql_numrows($result);
			
			if($num > 0)
			{
				if($access_cnt > 0)
				{
					print(", ");
				}
				print(" $specchio_schemas[$s]");	

				$access_cnt++;
			}
			
		}	

		print("</td>");
		print("</tr>");
		
	}
  
  ?>
  
  </table>
      
 <br>
 
 Grant schema access to a user:
 
    <?php
	global $server_address, $server_directory, $common_folder;	
	print("<form action = \"http://$server_address$server_directory/grant_schema_access.php\" method=\"post\" accept-charset=\"UTF-8\">");

	?>	
  
  <select class="formel_1"name="user">
  
    <?php
	for ($u = 0; $u < $no_of_users; $u++) 		
	{
	 print("<option value=\"$users[$u]\">$users[$u]</option>");
	}             
  	?>
  	
  </select>
  
  <select class="formel_1"name="schema">	 	
 	 	
 			
		 
    <?php
    $count = count($specchio_schemas);
	for ($s = 0; $s < $count; $s++)  		
	{
	 print("<option value=\"$specchio_schemas[$s]\">$specchio_schemas[$s]</option>");
	}             
  	?>
  	
  </select>
  
       <input type ="submit" class="formel_2" value="Grant access">
    
    </form>   
    


<?php
include "$common_folder/main_container_end.php";	
?>

<?php
include "$common_folder/footer.php";	
?>
