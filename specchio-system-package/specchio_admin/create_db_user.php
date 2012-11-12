<?php
include "common_includes.php";
include "$common_folder/header.php";	

include "$common_folder/main_container_start.php";	
?>


<?php
 
  // get the values from the post
  $first_name=$_POST['first_name'];
  $last_name=$_POST['last_name'];
  $utitle = $_POST['utitle'];
  $uinstitute = $_POST['uinstitute'];
  $uemail = $_POST['uemail'];
  $uwww = $_POST['uwww'];

 
 $complete = 1; // all mandatory fields filled
 
 // check mandatory fields
 if($first_name=="")
 {
 	print("First name is mandatory!");
 	echo "<br>";
 	$complete = 0;
 } 
 if($last_name=="")
 {
 	print("Last name is mandatory!");
 	echo "<br>";
 	$complete = 0;
 } 
 if($uemail=="")
 {
 	print("Email address is mandatory!");
 	echo "<br>";
 	$complete = 0;
 }
 
 if($complete == 0)
 {
 	echo "<br>";
 	print("<a href=\"javascript:history.back()\">Click here to return to form and complete your details.</a>");
 }
 else
 {
	global $schemas;
 	create_user_account($schemas, $utitle, $first_name, $last_name, $uemail, $uinstitute, $uwww);
	
 }
 	
?>

<?php
include "$common_folder/main_container_end.php";	
?>

<?php
include "$common_folder/footer.php";	
?>



