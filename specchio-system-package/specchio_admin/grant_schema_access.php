<?php
include "common_includes.php";
include "$common_folder/header.php";	

include "$common_folder/main_container_start.php";	
?>

      
<h2 align=left>   Grant schema access  <br> </h2>
    
 <?php
   // get the values from the post
   $user=$_POST['user'];
   $the_schemas= array($_POST['schema']);

  connect_to_specchio_with_cookies();
  grant_rights($user, $the_schemas);
  
  
  
  print("Access has been granted for user '$user' on $the_schemas[0]");

 ?>
      
      
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      
      
<?php
include "$common_folder/main_container_end.php";	
?>

<?php
include "$common_folder/footer.php";	
?>

