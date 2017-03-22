<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
	$username = $_POST['username'];
 
	require_once('Connect.php');
 
	$sql = "delete from details where username='$username'";
	$sql_check = "SELECT * FROM teacher WHERE username='$username'";
	if($username == '')
		echo 'details are missing!!!';
	else{
	$check = mysqli_fetch_array(mysqli_query($con,$sql_check));
			
			if(isset($check)){
				echo 'username or email already exist';
			}else{				
				$sql_add = "insert into teacher select * from details where username='$username'";
				if(mysqli_query($con,$sql_add)){
					//echo 'successfully registered';
					if (mysqli_query($con, $sql))
						echo "Record registered";
					else
						echo "Error in record: " . mysqli_error($con);
				}else{
					echo 'oops! Please try again!';
				}
			}
	}
}
else{
echo 'error';
}
?>