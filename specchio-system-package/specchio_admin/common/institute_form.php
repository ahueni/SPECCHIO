    <?php
	global $server_address, $server_directory;	
	print("<form action = \"http://$server_address$server_directory/create_institute.php\" method=\"post\" accept-charset=\"UTF-8\">");
	?>

    <table class="labelled">
    

 <tr><td class="label_back">
      Name *: 
    </td><td class="noborder">

      <input class="formel_1" type="text" name="name" value="">
  </td></tr>

 <tr><td class="label_back">
      Department : 
    </td><td class="noborder">

      <input class="formel_1" type="text" name="dept" value="">
  </td></tr>
  
  <tr><td class="label_back">
      Street : 
    </td><td class="noborder">

      <input class="formel_1" type="text" name="street" value="">
  </td></tr>
  
  <tr><td class="label_back">
      Street No : 
    </td><td class="noborder">

      <input class="formel_1" type="text" name="street_no" value="">
  </td></tr>  

  <tr><td class="label_back">
      PO code :
    </td><td class="noborder">

      <input class="formel_1" type="text" name="po_code" value="">
  </td></tr>

  <tr><td class="label_back">
      City :
    </td><td class="noborder">

      <input class="formel_1" type="text" name="city" value="">
  </td></tr>
  
  <tr><td class="label_back">
    Country: 
  </td><td class="noborder">
    
 
    <?php
	/*
	 * get the countries from the database
	 * 
	 */

	//include "db_and_common_functions.php";
 	// connect to database
 	connect_to_specchio();
	
	$query = "SELECT country_id, name FROM country order by name";
	//print($query);
	$result = mysql_query($query);
	if (!$result) {
	    die('Invalid query: ' . mysql_error());
	}
	
	$num=mysql_numrows($result);
	
	$i = 0;
	
	
	print ("<select class=\"formel_1\"name=\"country\">");
	
	//print("<option value=\"0\"> none </option>");
	
	
	while ($i < $num)
	{
	 $id=mysql_result($result,$i,"country_id");
	 $name=mysql_result($result,$i,"name");
	 print("<option value=\"$id\">$name</option>");
	 $i++;
	}             

	print ("</select>");
	print(" </td></tr>");


 	mysql_close();
 
  ?>


  <tr><td class="label_back">
      WWW:
    </td><td class="noborder">
      <input class="formel_1" type="text" name="www" value="">
  </td></tr>       
  



    
    
    <tr><td class="noborder">
      <input type ="submit" class="formel_1" value="Register institute">

    </td><td class="noborder">
      <input type ="reset" class="formel_1" value="Reset form">
    </td></tr>
    </table>


    </form>       
