<?php
include "common_includes.php";
    	
include "$common_folder/header.php";

include "$common_folder/main_container_start.php";	
?>

    <h2>Welcome to the SPECCHIO Administrator Site! </h2> <br><br>
    
    <?php

	if (isset($_COOKIE['admin_pw']) && isset($_COOKIE['admin_name']))
	{   
	    include "intro_txt.php";
	}
	else
	{
		print "Please log in:";	
	}
	?>		
	 <form action="store_admin_data.php" method="post">

	<input type="text" 
	 name="name" 
	 value="sdb_admin">
	 
	<input type="text" 
	 name="pw" 
	 value="">
	
	<input type="submit" value="Login">
	
	</form> 
  
<?php
include "$common_folder/main_container_end.php";	
?>

<?php
include "$common_folder/footer.php";	
?>








