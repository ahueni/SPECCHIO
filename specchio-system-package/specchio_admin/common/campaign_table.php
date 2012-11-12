    <h2 align=left>   Campaign overview (
    <?php
    global $db_server, $database;
    print "$database@$db_server";
    ?>
    )  <br> </h2>
    
    This page lists the campaigns that are available in the SPECCHIO database schema '
    <?php
    global $db_server, $database;
    print "$database' on $db_server.";
    ?>    
    <br><br>
    
 <table class="labelled">
   

 <tr><td class="label_back">
    Campaign 
    </td>
    <td class="label_back">
    Description
  </td>
  <td class="label_back">
    No of Spectra
  </td> 
  
  </tr>
  
  
      <?php
	/*
	 * get the campaign data
	 * 
	 */

	//include "db_and_common_functions.php";
 	// connect to database
 	connect_to_specchio();
	
	$query = "SELECT c.name, c.description, count(s.spectrum_id) FROM campaign c, spectrum s where s.campaign_id = c.campaign_id group by c.campaign_id order by name";
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
	 $name=mysql_result($result,$i,"c.name");
	 $desc=mysql_result($result,$i,"c.description");
	 $cnt=mysql_result($result,$i,2);
	 print("</td><td class=\"noborder\">");
	 print("$name");
	 print("</td><td class=\"noborder\">");
	 print("$desc");
	 print("</td><td class=\"noborder\">");
	 print("$cnt");
	 print("</tr>");
	 $i++;
	}             


 	mysql_close();
  
  ?>
  
  </table>
      
      <br>