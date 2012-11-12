<?php
include "common_includes.php";
include "$common_folder/header.php";	

$global_page_id = 6;
include "$common_folder/main_container_start.php";

   print("<h2 align=left>   User and Institute overview (");

    global $db_server, $database;
    print "$database@$db_server";

    ?>
    
    <br> </h2>
    
    This page lists the users and their institution having access on the SPECCHIO database schema '
    
    <?php
    global $db_server, $database;
    print "$database' on $db_server.";
    ?>    
    <br><br>
    
 <table class="labelled">
   

 <tr><td class="label_back">
    User Name 
    </td>
    <td class="label_back">
    Institution
  </td>
  <td class="label_back">
    Country
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
	
	$query = "SELECT first_name, last_name, i.name, department, c.name from specchio_user u, institute i, country c 
	where u.institute_id = i.institute_id and c.country_id = i.country_id";
	//print($query);
	$result = mysql_query($query);
	if (!$result) {
	    die('Invalid query: ' . mysql_error());
	}
	
	$num=mysql_numrows($result);
	
	$i = 0;
	//print ($num);
	
	while ($i < $num)
	{
	 print("<tr>");
	 $name=mysql_result($result,$i,"first_name") . " " . mysql_result($result,$i,"last_name");
	 $inst=mysql_result($result,$i,"department") . ", " . mysql_result($result,$i,"i.name");
	 $country=mysql_result($result,$i,"c.name");
	 print("</td><td class=\"noborder\">");
	 print("$name");
	 print("</td><td class=\"noborder\">");
	 print("$inst");
	 print("</td><td class=\"noborder\">");
	 print("$country");
	 print("</tr>");
	 $i++;
	}             

	
	// get users with no institute
	$query = "SELECT first_name, last_name from specchio_user u where u.institute_id is null";
	//print($query);
	$result = mysql_query($query);
	if (!$result) {
	    die('Invalid query: ' . mysql_error());
	}
	
	$num=mysql_numrows($result);
	
	$i = 0;
	//print ($num);
	
	while ($i < $num)
	{
	 print("<tr>");
	 $name=mysql_result($result,$i,"first_name") . " " . mysql_result($result,$i,"last_name");
	 print("</td><td class=\"noborder\">");
	 print("$name");
	 print("</td><td class=\"noborder\">");
	 print("");
	 print("</td><td class=\"noborder\">");
	 print("");
	 print("</tr>");
	 $i++;
	}             	
	
	

 	mysql_close();
 	
 	
 	
 	
 	
  
  ?>
  
  </table>
      
      <br>


<?php
include "$common_folder/main_container_end.php";	
?>

<?php
include "$common_folder/footer.php";	
?>

