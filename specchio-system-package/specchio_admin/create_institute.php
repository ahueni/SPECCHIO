<?php
include "common_includes.php";
include "$common_folder/header.php";	

include "$common_folder/main_container_start.php";	
?>


<?php
/*
 * Created on 20/08/2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
 
 //include "db_and_common_functions.php";

 // this checks on the productive database, the test database is assumed to be in sync 

  
 // ********************************************************
 // MAIN
 // ********************************************************
 
  // get the values from the post
  $name=$_POST['name'];
  $dept=$_POST['dept'];
  $street = $_POST['street'];
  $street_no = $_POST['street_no'];
  $po_code= $_POST['po_code'];
  $city= $_POST['city'];
  $country_id= $_POST['country'];
  $www = $_POST['www'];
  
  
//  print_nl($name);
//  print_nl($dept);
//  print_nl($street);
//  print_nl($street_no);
//  print_nl($po_code);
//  print_nl($city);
//  print_nl($country_id);
//  print_nl($www);
   
 
 $complete = 1; // all mandatory fields filled
 
 // check mandatory fields
 if($name=="")
 {
 	print("Institute name is mandatory!");
 	echo "<br>";
 	$complete = 0;
 } 
 
 if($complete == 0)
 {
 	echo "<br>";
 	print("<a href=\"javascript:history.back()\">Click here to return to form and complete the institute details.</a>");
 }
 else
 {
	create_new_institute($name, $dept, $street, $street_no, $po_code, $city, $www, $country_id);
 } 	
 	
 
 
?>

<?php
include "$common_folder/main_container_end.php";	
?>

<?php
include "$common_folder/footer.php";	
?>




