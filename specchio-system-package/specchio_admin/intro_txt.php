	<h3>Introduction <br></h3> 
	
	<br>
	
	<?php
    
    global $server_address, $server_directory; 
    
	if (isset($_COOKIE['admin_pw']) && isset($_COOKIE['admin_name']))
	{   
	    print("You are logged in as ");
	    print($_COOKIE['admin_name']);
	}
	else
	{
		print "You are currently not logged in. Certain functionality will not be available. <br>";		
		print("<a href= \"http://$server_address$server_directory/admin_login.php\" >Login in ...</a>"); 
	}
	?>
	
	<br><br>
	
	Available tools:<br>
	- Get an overview what campaigns are in your database,<br>
	- Get an overview what users and institutes are in your database,<br>
	- User creation without bothering with captchas,<br>
	- Grant existing users access to other SPECCHIO schemata on your database server,<br>
	- Download the latest user guide and administrator PHP files from the RSL ftp server,<br>

      <br>


	<h3><br>Feedback and Questions <br></h3>
      We very much appreciate administrator feedback which you will kindly send to: 
      <a href="mailto:admin@specchio.ch">admin@specchio.ch</a>

    
      </div>