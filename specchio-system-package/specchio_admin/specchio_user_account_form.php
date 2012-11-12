
<?php
include "common_includes.php";
include "$common_folder/header.php";	
$global_page_id = 4;
include "$common_folder/main_container_start.php";	
?>

	<b> Note: </b> Please do not use any special characters (e.g. umlauts) in your first or last name. <br>
	Special characters currently still pose problems during database connections on some platforms/operating systems. <br><br>

    <?php
	global $server_address, $server_directory, $common_folder;	
	print("<form action = \"http://$server_address$server_directory/create_db_user.php\" method=\"post\" accept-charset=\"UTF-8\">");
	
	include "$common_folder/user_data_form_fields.php";
	?>
    
  
    <tr><td class="noborder">
      <input type ="submit" class="formel_2" value="Create account">

    </td><td class="noborder">
      <input type ="reset" class="formel_1" value="Reset form">
    </td></tr>
    </td></tr>
    </table>

    <br>    
    
    </form>   
    

    
    
      </div>
 <?php
include "$common_folder/main_container_end.php";	
?>

<?php
include "$common_folder/footer.php";	
?>

