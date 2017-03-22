<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
	$id = $_POST['id'];
 
	require_once('Connect.php');
 
	$sql = "delete from director_notice where id='$id'";
	$sql_check = "SELECT * FROM notice WHERE id='$id'";
	if($id == '')
		echo 'details are missing!!!';
	else{					
				$sql_add = "insert into notice select * from director_notice where id='$id'";
				if(mysqli_query($con,$sql_add)){
					//echo 'successfully registered';
					if (mysqli_query($con, $sql))
						echo "Notice has been posted";
					else
						echo "Error " . mysqli_error($con);
				}else{
					echo 'oops! ' . mysqli_error($con);
				}
	}
}
else{
echo 'error';
}
?>