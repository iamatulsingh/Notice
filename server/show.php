<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
	$username = $_POST['username'];
 
	require_once('Connect.php');
	
	$sql = "select * from details where username='$username'"; 
	$result = mysqli_query($con,$sql);
	$json = array();

	if(mysqli_num_rows($result)){
		while($row=mysqli_fetch_assoc($result)){
			$json['details'][]=$row;
		}
	}
	
	mysqli_close($con);

	echo json_encode($json); 
}
else{
echo 'error';
}

?>