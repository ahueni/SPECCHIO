
    <table class="labelled">
   

 <tr><td class="label_back">
      First name *: 
    </td><td class="noborder">

      <input class="formel_1" type="text" name="first_name" value="" size="200">
  </td></tr>

 <tr><td class="label_back">
      Last name *: 
    </td><td class="noborder">

      <input class="formel_1" type="text" name="last_name" value="">
  </td></tr>
  
  <tr><td class="label_back">
      Title : 
    </td><td class="noborder">

      <select class="formel_1" name="utitle">
               <option  value="Dr.">Dr.</option>
               <option  value="Ing.">Ing.</option>
		   <option  value="Ph.D">Ph.D</option>
               <option  value="M.Sc.">M.Sc.</option>
      	   <option  value="B.Sc.">B.Sc.</option>
               <option  value="Mr.">Mr.</option>
               <option  value="Mrs.">Mrs.</option>
               <option  value="Ms.">Ms.</option>               
               </select>
  </td></tr>

  <tr><td class="label_back" rowspan="2">
      Institute name: <br> <class="label_back_small"> (Select existing or register new one.)  
    </td><td class="noborder">
    
    
    
    <?php
	/*
	 * get the institute names plus departments from the database
	 * 
	 */

	//include "db_and_common_functions.php";
 	// connect to database
 	connect_to_specchio_with_cookies();
	
	$query = "SELECT institute_id, name, department FROM institute order by name";
	//print($query);
	$result = mysql_query($query);
	if (!$result) {
	    die('Invalid query: ' . mysql_error());
	}
	
	$num=mysql_numrows($result);
	
	$i = 0;
	
	
	print ("<select class=\"formel_1\"name=\"uinstitute\">");
	
	print("<option value=\"0\"> none </option>");
	
	
	while ($i < $num)
	{
	 $id=mysql_result($result,$i,"institute_id");
	 $name=mysql_result($result,$i,"name");
	 $dep=mysql_result($result,$i,"department");
	 print("<option value=\"$id\">$name ($dep)</option>");
	 $i++;
	}             

	print ("</select>");
	print(" </td></tr>");


 	mysql_close();
 
    
    
  
  ?>

	<tr><td class="noborder_href">
	<?php
		global $server_address, $server_directory;
		print("<a href=\"http://$server_address$server_directory/institute_data_form.php\" >Register new institute</a>");
	?>
	</td>



  <tr><td class="label_back">
      Email *:
    </td><td class="noborder">

      <input class="formel_1" type="text" name="uemail" value="">
  </td></tr>

  <tr><td class="label_back">
      WWW:
    </td><td class="noborder">
      <input class="formel_1" type="text" name="uwww" value="">
  </td></tr>       
