<?php
 
    print_menu_item("index.php", "Home", 1);
    print_menu_item("campaign_overview.php", "Available Spectral Campaigns", 3);
    print_menu_item("user_overview.php", "User and Institutes", 6);
    print_menu_item("specchio_user_account_form.php", "Create database account", 4);
    print_menu_item("user_rights.php", "User Rights", 7);
    print_menu_item("admin_guides.php", "Admin Guides", 5);
    print("<a class=\"menu1\" href= \"http://www.specchio.ch\" >SPECCHIO web site</a>");


	if (isset($_COOKIE['admin_pw']) && isset($_COOKIE['admin_name']))
	{   
		$admin_name = $_COOKIE['admin_name'];
	    print_menu_item("logout.php", "Logout $admin_name", 0);
	}
	else
	{
		print_menu_item("admin_login.php", "Login as admin", 0);			
	}

?>