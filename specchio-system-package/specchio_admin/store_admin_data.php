<?php
include "common_includes.php";

		// delete cookies
	    //setcookie('admin_name', "", time()+3600);		   
	    //setcookie('admin_pw', "", time()+3600);

    	// if data was posted store it in cookie
    	if(count($_POST) > 0)
    	{
		   // get the values from the post
		   $name=$_POST['name'];
		   $pw= $_POST['pw'];

		   setcookie('admin_name', $name, time()+3600);		   
		   setcookie('admin_pw', $pw, time()+3600);
		   
		   // page reload
		   //unset($_POST);
		   
		   //print("<script language=\"Javascript\">");
		   //print("document.location.reload()");
		   //print("</script>");
    	}
    	
include "$common_folder/header.php";

include "$common_folder/main_container_start.php";	
?>
    	
Your admin name and password have been stored in a browser cookie.
<br>
This cookie will remain stored on you computer for 1 day by default.
<br> 
To log out select the 'Log out 

<?php
   print($_COOKIE['admin_name']);
?>

' from the main menu. 

<br>
<br>
Click OK to return to the main page.<br>   	
    	
 <form action="index.php" method="post">

<input type="submit" value="OK">

</form>    		    
	    
<?php
include "$common_folder/main_container_end.php";	
?>

<?php
include "$common_folder/footer.php";	
?>
    	