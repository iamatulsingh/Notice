<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
	$username = $_POST['username'];
	$email = $_POST['email'];
	$role = $_POST['role'];
 
	require_once('Connect.php');
 
    $role = strtolower($role);
	$sql = "select password from $role where username='$username' and email='$email'";
	
	$res = mysqli_fetch_array(mysqli_query($con,$sql));

 
	//print_r($res);
	if(isset($res)){
		//echo $res[0];  //For print password from database 
		echo "success";
	}
	else
		echo "failed";
 
	mysqli_close($con);
}
else{
echo 'error';
}
?>