<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
	$username = $_POST['username'];
	$password = $_POST['password'];
	$role = $_POST['role'];
 
	require_once('Connect.php');
 
	$sql = "select * from $role where username='$username' and password='$password'";  //use details instead of $role
	$res = mysqli_query($con,$sql);
 
	$check = mysqli_fetch_array($res);
 
	if(isset($check)){
		echo 'success';
	}else{
		echo 'failure';
	}
 
	mysqli_close($con);
}
else{
echo 'error';
}
?>